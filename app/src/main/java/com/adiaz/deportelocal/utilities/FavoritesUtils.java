package com.adiaz.deportelocal.utilities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.adiaz.deportelocal.database.DeporteLocalDbContract;
import com.adiaz.deportelocal.entities.Competition;
import com.adiaz.deportelocal.entities.Favorite;
import com.adiaz.deportelocal.entities.TeamFavorite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toni on 02/09/2017.
 */

public class FavoritesUtils {
	public static final void addFavorites(Context context, Long idCompetition){
		addFavorites(context, idCompetition, null);
	}

	public static final void addFavorites(Context context, Long idCompetition, String idTeam){
		ContentResolver contentResolver = context.getContentResolver();
		Uri uri = DeporteLocalDbContract.FavoritesEntry.CONTENT_URI;
		ContentValues contentValues = new ContentValues();
		contentValues.put(DeporteLocalDbContract.FavoritesEntry.COLUMN_ID_COMPETITION, idCompetition);
		contentValues.put(DeporteLocalDbContract.FavoritesEntry.COLUMN_ID_TEAM, idTeam);
		contentResolver.insert(uri, contentValues);
	}

	public static final void removeFavorites(Context context, Long idFavorite){
		ContentResolver contentResolver = context.getContentResolver();
		Uri uri = DeporteLocalDbContract.FavoritesEntry.buildFavoritesUri(idFavorite);
		contentResolver.delete(uri, null, null);
	}

	public static final boolean isFavoriteCompetition(Context context, Long idCompetition) {
		return queryFavoriteCompetition(context, idCompetition)!=null;
	}

	public static final boolean isFavoriteTeam(Context context, Long idCompetition, String idTeam) {
		return queryFavoriteTeam(context, idCompetition, idTeam)!=null;
	}

	public static final Favorite queryFavoriteCompetition(Context context, Long idCompetition) {
		ContentResolver contentResolver = context.getContentResolver();
		Uri uri = DeporteLocalDbContract.FavoritesEntry.CONTENT_URI;
		String selection = DeporteLocalDbContract.FavoritesEntry.COLUMN_ID_COMPETITION  + "=? ";
		selection += " AND " + DeporteLocalDbContract.FavoritesEntry.COLUMN_ID_TEAM + " IS NULL";
		String[] selectionArgs = new String[]{idCompetition.toString()};
		Cursor cursor = contentResolver.query(uri, DeporteLocalDbContract.FavoritesEntry.PROJECTION, selection, selectionArgs, null);
		Favorite favorite = null;
		try {
			if (cursor.getCount()>0) {
				cursor.moveToNext();
				favorite = DeporteLocalDbContract.FavoritesEntry.initEntity(cursor);
			}
		} finally {
			cursor.close();
		}
		return favorite;
	}

	public static final Favorite queryFavoriteTeam(Context context, Long idCompetition, String idTeam) {
		ContentResolver contentResolver = context.getContentResolver();
		Uri uri = DeporteLocalDbContract.FavoritesEntry.CONTENT_URI;
		String selection = DeporteLocalDbContract.FavoritesEntry.COLUMN_ID_COMPETITION  + "=? ";
		selection += " AND " + DeporteLocalDbContract.FavoritesEntry.COLUMN_ID_TEAM + " =? ";
		String[] selectionArgs = new String[]{idCompetition.toString(), idTeam};
		Cursor cursor = contentResolver.query(uri, DeporteLocalDbContract.FavoritesEntry.PROJECTION, selection, selectionArgs, null);
		Favorite favorite = null;
		try {
			if (cursor.getCount()>0) {
				cursor.moveToNext();
				favorite = DeporteLocalDbContract.FavoritesEntry.initEntity(cursor);
			}
		} finally {
			cursor.close();
		}
		return favorite;
	}

	public static List<Competition> getCompetitionsFavorites(Context context) {
		List<Competition> competitionsFavorites = new ArrayList<>();
		Uri uri = DeporteLocalDbContract.FavoritesEntry.CONTENT_URI;
		String selection = DeporteLocalDbContract.FavoritesEntry.COLUMN_ID_TEAM + " IS NULL";
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, DeporteLocalDbContract.FavoritesEntry.PROJECTION, selection, null, null);
		try {
			while (cursor.moveToNext()) {
				long idCompetition = cursor.getLong(DeporteLocalDbContract.FavoritesEntry.INDEX_ID_COMPETITION);
				Uri uriCompetition = DeporteLocalDbContract.CompetitionsEntry.buildCompetitionUriWithServerId(idCompetition);
				Cursor cursorCompetition = resolver.query(uriCompetition, DeporteLocalDbContract.CompetitionsEntry.PROJECTION, null, null, null);
				if (cursorCompetition.moveToNext()) {
					Competition competition = DeporteLocalDbContract.CompetitionsEntry.initEntity(cursorCompetition);
					competitionsFavorites.add(competition);
				}
			}
		} finally {
			cursor.close();
		}
		return competitionsFavorites;
	}

	public static List<TeamFavorite> getTeamsFavorites(Context context) {
		List<TeamFavorite> favoritTeams = new ArrayList<>();
		Uri uri = DeporteLocalDbContract.FavoritesEntry.CONTENT_URI;
		String selection = DeporteLocalDbContract.FavoritesEntry.COLUMN_ID_TEAM + " IS NOT NULL";
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, DeporteLocalDbContract.FavoritesEntry.PROJECTION, selection, null, null);
		try {
			while (cursor.moveToNext()) {
				long idCompetition = cursor.getLong(DeporteLocalDbContract.FavoritesEntry.INDEX_ID_COMPETITION);
				Competition competition = CompetitionDbUtils.queryCompetition(context.getContentResolver(), idCompetition);
				if (competition!=null) {
					TeamFavorite teamFavorite = new TeamFavorite();
					teamFavorite.setName(cursor.getString(DeporteLocalDbContract.FavoritesEntry.INDEX_ID_TEAM));
					teamFavorite.setIdCompetitionServer(idCompetition);
					teamFavorite.setCompetitionName(competition.name());
					teamFavorite.setCategoryTag(competition.categoryName());
					teamFavorite.setSportTag(competition.sportName());
					favoritTeams.add(teamFavorite);
				}
			}
		} finally {
			cursor.close();
		}
		return favoritTeams;
	}

	public static List<Favorite> queryFavoritesTeams(Context context) {
		return queryFavorites(context, true);
	}

	public static List<Favorite> queryFavoritesCompetitions(Context context) {
		return queryFavorites(context, false);
	}

	/**
	 * If teams = true return favorites teams else return favorites competitions.
	 * @param context
	 * @param teams
	 * @return
	 */
	private static List<Favorite> queryFavorites(Context context, boolean teams) {
		List<Favorite> favorites = new ArrayList<>();
		Uri uri = DeporteLocalDbContract.FavoritesEntry.CONTENT_URI;
		String selection;
		if (teams) {
			selection = DeporteLocalDbContract.FavoritesEntry.COLUMN_ID_TEAM + " IS NOT NULL";
		} else {
			selection = DeporteLocalDbContract.FavoritesEntry.COLUMN_ID_TEAM + " IS NULL";
		}
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, DeporteLocalDbContract.FavoritesEntry.PROJECTION, selection, null, null);
		try {
			while (cursor.moveToNext()) {
				Favorite fav = DeporteLocalDbContract.FavoritesEntry.initEntity(cursor);
				favorites.add(fav);
			}
		} finally {
			cursor.close();
		}
		return favorites;
	}

	public static void updateLastNotification(ContentResolver contentResolver, Long idFavorite, Long lastNotification) {
		//disable notification for this favorite.
		Uri uri = DeporteLocalDbContract.FavoritesEntry.buildFavoritesUri(idFavorite);
		ContentValues contentValues = new ContentValues();
		contentValues.put(DeporteLocalDbContract.FavoritesEntry.COLUMN_LAST_NOTIFICATION, lastNotification);
		contentResolver.update(uri, contentValues, null, null);
	}
}

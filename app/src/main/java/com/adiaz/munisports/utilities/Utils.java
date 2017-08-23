package com.adiaz.munisports.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.adiaz.munisports.R;
import com.adiaz.munisports.entities.CompetitionEntity;
import com.adiaz.munisports.entities.CourtEntity;
import com.adiaz.munisports.entities.TeamFavoriteEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.adiaz.munisports.database.MuniSportsDbContract.CompetitionsEntry;
import static com.adiaz.munisports.database.MuniSportsDbContract.SportCourtsEntry;
/* Created by toni on 28/03/2017. */

public class Utils {

	private static final String TAG = Utils.class.getSimpleName();

	public static boolean checkIfFavoritSelected(Context context, String teamName, String key) {
		SharedPreferences preferences = getDefaultSharedPreferences(context);
		Set<String> defaultSet = new HashSet<>();
		Set<String> stringsSet = preferences.getStringSet(key, defaultSet);
		return stringsSet.contains(teamName);
	}

	public static void unMarkFavoriteTeam(Context context, String myTeamName, String key) {
		Utils.updateListFavoritesTeam(context, myTeamName, key, false);
	}


	public static void markFavoriteTeam(Context context, String myTeamName, String key) {
		Utils.updateListFavoritesTeam(context, myTeamName, key, true);
	}

	private static void updateListFavoritesTeam(Context context, String myTeamName, String key, boolean addFavorite) {
		SharedPreferences preferences = getDefaultSharedPreferences(context);
		Set<String> stringsSetCopy = new HashSet<String>(preferences.getStringSet(key, new HashSet<String>()));
		if (addFavorite) {
			stringsSetCopy.add(myTeamName);
		} else {
			stringsSetCopy.remove(myTeamName);
		}
		SharedPreferences.Editor editor = preferences.edit();
		editor.putStringSet(key, stringsSetCopy);
		editor.commit();
	}

	public static List<String> getFavorites(Context context, String key) {
		SharedPreferences preferences = getDefaultSharedPreferences(context);
		Set<String> favoritesSet = preferences.getStringSet(key, new HashSet<String>());
		List<String> favoritesList = new ArrayList<>(favoritesSet);
		Collections.sort(favoritesList);
		return favoritesList;
	}

	public static String getStringResourceByName(Context context, String aString) {
		String packageName = context.getPackageName();
		String strResource = context.getString(R.string.NOT_FOUND);
		try {
			int resId = context.getResources().getIdentifier(aString, "string", packageName);
			strResource = context.getString(resId);
		} catch (Exception e) {
			Log.e(TAG, "getStringResourceByName: " + e.getMessage(), e);
		}
		return strResource;
	}

	public static String generateTeamKey(String tag, String idCompetitionServer) {
		return tag + "|" + idCompetitionServer;
	}

	public static String composeFavoriteTeamId(String teamName, String idCompetitionServer) {
		return teamName + "|" + idCompetitionServer;
	}

	public static List<CompetitionEntity> getCompetitionsFavorites(Context context) {
		List<CompetitionEntity> competitionsFavorites = new ArrayList<>();
		List<String> favorites = Utils.getFavorites(context, context.getString(R.string.key_favorites_competitions));
		Cursor cursorCompetitions = context.getContentResolver().query(
				CompetitionsEntry.CONTENT_URI, CompetitionsEntry.PROJECTION, null, null, null);
		while (cursorCompetitions.moveToNext()) {
			String idServer = cursorCompetitions.getString(CompetitionsEntry.INDEX_ID_SERVER);
			String name = cursorCompetitions.getString(CompetitionsEntry.INDEX_NAME);
			String sport = cursorCompetitions.getString(CompetitionsEntry.INDEX_SPORT);
			String category = cursorCompetitions.getString(CompetitionsEntry.INDEX_CATEGORY);
			if (favorites.contains(idServer)) {
				CompetitionEntity myCompetition = new CompetitionEntity();
				myCompetition.setServerId(idServer);
				myCompetition.setName(name);
				myCompetition.setSportName(sport);
				myCompetition.setCategoryName(category);
				competitionsFavorites.add(myCompetition);
			}
		}
		cursorCompetitions.close();
		return competitionsFavorites;
	}

	public static List<TeamFavoriteEntity> getTeamsFavorites(Context context) {
		List<TeamFavoriteEntity> teamsFavorites = new ArrayList<>();
		List<String> favorites = Utils.getFavorites(context, context.getString(R.string.key_favorites_teams));
		for (String favorite : favorites) {
			if (favorite.split("\\|").length >= 2) {
				String teamName = favorite.split("\\|")[0];
				String idCompetitionsServer = favorite.split("\\|")[1];
				TeamFavoriteEntity teamFavoriteEntity = null;
				if (!TextUtils.isEmpty(idCompetitionsServer)) {
					teamFavoriteEntity = new TeamFavoriteEntity();
					teamFavoriteEntity.setName(teamName);
					teamFavoriteEntity.setIdCompetitionServer(idCompetitionsServer);
					String selection = CompetitionsEntry.COLUMN_ID_SERVER + "=?";
					String[] selectionArgs = new String[]{idCompetitionsServer};
					Cursor cursor = context.getContentResolver().query(
							CompetitionsEntry.CONTENT_URI, CompetitionsEntry.PROJECTION, selection, selectionArgs, null);
					if (cursor.getCount() == 1) {
						cursor.moveToNext();
						teamFavoriteEntity.setSportTag(cursor.getString(CompetitionsEntry.INDEX_SPORT));
						teamFavoriteEntity.setCategoryTag(cursor.getString(CompetitionsEntry.INDEX_CATEGORY));
						teamFavoriteEntity.setCompetitionName(cursor.getString(CompetitionsEntry.INDEX_NAME));
					}
					cursor.close();
				}
				teamsFavorites.add(teamFavoriteEntity);
			}
		}
		return teamsFavorites;
	}

	public static void showNoInternetAlert(Context context, View view) {
		String strError = context.getString(R.string.internet_required);
		final Snackbar snackbar = Snackbar.make(view, strError, Snackbar.LENGTH_INDEFINITE);
		snackbar.show();
	}

	public static Map<Long, CourtEntity> initCourts(Context context) {
		Uri uriCourts = SportCourtsEntry.CONTENT_URI;
		Cursor cursorCourts = context.getContentResolver().query(uriCourts, SportCourtsEntry.PROJECTION, null, null, null);
		Map<Long, CourtEntity> mapCourts = new HashMap<>();
		try {
			while (cursorCourts.moveToNext()) {
				CourtEntity courtEntity = new CourtEntity();
				Long id = cursorCourts.getLong(SportCourtsEntry.INDEX_ID_SERVER);
				String centerName = cursorCourts.getString(SportCourtsEntry.INDEX_CENTER_NAME);
				String courtName = cursorCourts.getString(SportCourtsEntry.INDEX_COURT_NAME);
				courtEntity.setCenterName(centerName);
				courtEntity.setCourtName(courtName);
				courtEntity.setCourtFullName(centerName);
				if (!TextUtils.isEmpty(courtName)) {
					courtEntity.setCourtFullName(centerName + " - " + courtName);
				}
				courtEntity.setCenterAddress(cursorCourts.getString(SportCourtsEntry.INDEX_CENTER_ADDRESS));
				mapCourts.put(id, courtEntity);
			}
		} finally {
			cursorCourts.close();
		}
		return mapCourts;
	}
}



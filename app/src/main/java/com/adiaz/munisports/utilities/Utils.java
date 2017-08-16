package com.adiaz.munisports.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.adiaz.munisports.R;
import com.adiaz.munisports.entities.ClassificationEntity;
import com.adiaz.munisports.entities.CompetitionEntity;
import com.adiaz.munisports.entities.JornadaEntity;
import com.adiaz.munisports.entities.MatchEntity;
import com.adiaz.munisports.entities.TeamEntity;
import com.adiaz.munisports.entities.TeamFavoriteEntity;
import com.adiaz.munisports.entities.TeamMatchEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.adiaz.munisports.database.MuniSportsDbContract.ClassificationEntry;
import static com.adiaz.munisports.database.MuniSportsDbContract.CompetitionsEntry;
import static com.adiaz.munisports.database.MuniSportsDbContract.MatchesEntry;

/* Created by toni on 28/03/2017. */

public class Utils {

	private static final String TAG = Utils.class.getSimpleName();

	private static List<JornadaEntity> calendar;

	// TODO: 27/04/2017 OPTIMIZE THIS METHOD!!!
	public static List<TeamEntity> initTeams(Cursor cursorMatches) {
		cursorMatches.moveToPosition(-1);
		List<TeamEntity> teams = new ArrayList<>();
		Set<String> teamsSet = new HashSet<>();
		while (cursorMatches.moveToNext()) {
			teamsSet.add(cursorMatches.getString(MatchesEntry.INDEX_TEAM_LOCAL));
			teamsSet.add(cursorMatches.getString(MatchesEntry.INDEX_TEAM_VISITOR));
		}
		List<String> teamsList = new ArrayList<>();
		teamsList.addAll(teamsSet);
		Collections.sort(teamsList);
		for (String s : teamsList) {
			List<TeamMatchEntity> matches = new ArrayList<>();
			cursorMatches.moveToPosition(-1);
			while (cursorMatches.moveToNext()) {
				String teamLocal = cursorMatches.getString(MatchesEntry.INDEX_TEAM_LOCAL);
				String teamVisitor = cursorMatches.getString(MatchesEntry.INDEX_TEAM_VISITOR);
				if (s.equals(teamLocal) || s.equals(teamVisitor)) {
					TeamMatchEntity teamMatchEntity = new TeamMatchEntity();
					if (s.equals(teamLocal)) {
						teamMatchEntity.setLocal(true);
						teamMatchEntity.setOpponent(teamVisitor);
					} else {
						teamMatchEntity.setLocal(false);
						teamMatchEntity.setOpponent(teamLocal);
					}
					String matchPlace = cursorMatches.getString(MatchesEntry.INDEX_PLACE);
					Long dateLong = cursorMatches.getLong(MatchesEntry.INDEX_DATE);
					Integer scoreLocal = cursorMatches.getInt(MatchesEntry.INDEX_SCORE_LOCAL);
					Integer scoreVisitor = cursorMatches.getInt(MatchesEntry.INDEX_SCORE_VISITOR);
					teamMatchEntity.setPlace(matchPlace);
					teamMatchEntity.setDate(new Date(dateLong));
					teamMatchEntity.setTeamScore(scoreLocal);
					teamMatchEntity.setOpponentScore(scoreVisitor);
					matches.add(teamMatchEntity);
				}
			}
			TeamEntity teamEntity = new TeamEntity(s);
			teamEntity.setMatches(matches);
			teams.add(teamEntity);
		}
		return teams;
	}

	public static List<JornadaEntity> initCalendar(Cursor cursorMatches) {
		List<JornadaEntity> calendar = new ArrayList<>();
		int weekNumber = 1;
		List<MatchEntity> matches = new ArrayList<>();
		cursorMatches.moveToPosition(-1);
		while (cursorMatches.moveToNext()) {
			int week = cursorMatches.getInt(MatchesEntry.INDEX_WEEK);
			if (weekNumber!=week) {
				calendar.add (new JornadaEntity(matches));
				matches = new ArrayList<>();
				weekNumber = week;
			}
			MatchEntity matchEntity = new MatchEntity();
			matchEntity.setTeamLocal(cursorMatches.getString(MatchesEntry.INDEX_TEAM_LOCAL));
			matchEntity.setTeamVisitor(cursorMatches.getString(MatchesEntry.INDEX_TEAM_VISITOR));
			matchEntity.setScoreLocal(cursorMatches.getInt(MatchesEntry.INDEX_SCORE_LOCAL));
			matchEntity.setScoreVisitor(cursorMatches.getInt(MatchesEntry.INDEX_SCORE_VISITOR));
			matchEntity.setPlace(cursorMatches.getString(MatchesEntry.INDEX_PLACE));
			matchEntity.setDate(new Date(cursorMatches.getLong(MatchesEntry.INDEX_DATE)));
			matches.add(matchEntity);
		}
		return calendar;
	}

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

	public static TeamEntity initTeamCompetition(Context context, String teamName, String idCompetitionServer) {
		TeamEntity teamEntity = new TeamEntity(teamName);
		Uri uri = MatchesEntry.buildMatchesUriWithCompetitions(idCompetitionServer);
		Cursor cursor = context.getContentResolver().query(uri, MatchesEntry.PROJECTION, null, null, null);
		while (cursor.moveToNext()) {
			String teamLocal = cursor.getString(MatchesEntry.INDEX_TEAM_LOCAL);
			String teamVisitor = cursor.getString(MatchesEntry.INDEX_TEAM_VISITOR);
			if (teamName.equals(teamLocal) || teamName.equals(teamVisitor)) {
				TeamMatchEntity teamMatchEntity = new TeamMatchEntity();
				Long longDate = cursor.getLong(MatchesEntry.INDEX_DATE);
				Integer scoreLocal = cursor.getInt(MatchesEntry.INDEX_SCORE_LOCAL);
				Integer scoreVisitor = cursor.getInt(MatchesEntry.INDEX_SCORE_VISITOR);
				if (teamName.equals(teamLocal)) {
					teamMatchEntity.setLocal(true);
					teamMatchEntity.setOpponent(teamVisitor);
				} else {
					teamMatchEntity.setLocal(false);
					teamMatchEntity.setOpponent(teamLocal);
				}
				teamMatchEntity.setPlace(cursor.getString(MatchesEntry.INDEX_PLACE));
				teamMatchEntity.setDate(new Date(longDate));
				teamMatchEntity.setTeamScore(scoreLocal);
				teamMatchEntity.setOpponentScore(scoreVisitor);
				teamEntity.getMatches().add(teamMatchEntity);
			}
		}
		cursor.close();
		return teamEntity;
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
		return  tag + "|" + idCompetitionServer;
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
			if (favorite.split("\\|").length>=2) {
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
					if (cursor.getCount()==1) {
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

	public static List<ClassificationEntity> initClassification(Cursor cursorClassification) {
		List<ClassificationEntity> list = new ArrayList<>();
		Log.d(TAG, "initClassification: " + cursorClassification.getCount());
		while (cursorClassification.moveToNext()) {
			ClassificationEntity classificationEntry = new ClassificationEntity();
			classificationEntry.setPosition(cursorClassification.getInt(ClassificationEntry.INDEX_POSITION));
			classificationEntry.setTeam(cursorClassification.getString(ClassificationEntry.INDEX_TEAM));
			classificationEntry.setPoints(cursorClassification.getInt(ClassificationEntry.INDEX_POINTS));
			classificationEntry.setMatchesPlayed(cursorClassification.getInt(ClassificationEntry.INDEX_MACHES_PLAYED));
			classificationEntry.setMatchesWon(cursorClassification.getInt(ClassificationEntry.INDEX_MACHES_WON));
			classificationEntry.setMatchesDrawn(cursorClassification.getInt(ClassificationEntry.INDEX_MACHES_DRAWN));
			classificationEntry.setMatchesLost(cursorClassification.getInt(ClassificationEntry.INDEX_MACHES_LOST));
			list.add(classificationEntry);
		}
		return list;
	}
}



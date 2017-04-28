package com.adiaz.legasports.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.adiaz.legasports.R;
import com.adiaz.legasports.entities.CompetitionEntity;
import com.adiaz.legasports.entities.JornadaEntity;
import com.adiaz.legasports.entities.MatchEntity;
import com.adiaz.legasports.entities.TeamEntity;
import com.adiaz.legasports.entities.TeamFavoriteEntity;
import com.adiaz.legasports.entities.TeamMatchEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.adiaz.legasports.database.LegaSportsDbContract.CompetitionsEntry;
import static com.adiaz.legasports.database.LegaSportsDbContract.MatchesEntry;

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

	public static List<TeamEntity> initTeams(Context context) {
		List<TeamEntity> teams = new ArrayList<>();
		List<JornadaEntity> jornadas = initCalendar(context);
		JornadaEntity jornadaEntity = jornadas.get(0);
		Set<String> teamsSet = new HashSet<>();
		for (MatchEntity matchEntity : jornadaEntity.getMatches()) {
			teamsSet.add(matchEntity.getTeamLocal());
			teamsSet.add(matchEntity.getTeamVisitor());
		}
		List<String> teamsList = new ArrayList<>();
		teamsList.addAll(teamsSet);
		Collections.sort(teamsList);
		for (String s : teamsList) {
			List<TeamMatchEntity> matches = new ArrayList<>();
			for (JornadaEntity jornada : jornadas) {
				for (MatchEntity matchEntity : jornada.getMatches()) {
					if (s.equals(matchEntity.getTeamLocal()) || s.equals(matchEntity.getTeamVisitor())) {
						TeamMatchEntity teamMatchEntity = new TeamMatchEntity();
						if (s.equals(matchEntity.getTeamLocal())) {
							teamMatchEntity.setLocal(true);
							teamMatchEntity.setOpponent(matchEntity.getTeamVisitor());
						} else {
							teamMatchEntity.setLocal(false);
							teamMatchEntity.setOpponent(matchEntity.getTeamLocal());
						}
						teamMatchEntity.setPlace(matchEntity.getPlace());
						DateFormat dateFormat = new SimpleDateFormat(LegaSportsConstants.DATE_FORMAT);
	/*					try {
							teamMatchEntity.setDate(dateFormat.parse(matchEntity.getDate() + " " + matchEntity.getHour()));
						} catch (ParseException e) {
							Log.e(TAG, "initTeams: error in parse", e);
						}*/
						teamMatchEntity.setTeamScore(0);
						teamMatchEntity.setOpponentScore(0);
						matches.add(teamMatchEntity);
					}
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

	public static List<JornadaEntity> initCalendar(Context context) {
		if (calendar == null) {
			calendar = new ArrayList<>();
			AssetManager assetManager = context.getResources().getAssets();
			try {
				InputStreamReader inputStream = new InputStreamReader(assetManager.open("calendar.txt"));
				BufferedReader reader = new BufferedReader(inputStream);
				boolean started = false;
				String line;
				List<MatchEntity> matches = new ArrayList<>();
				while ((line = reader.readLine()) != null) {
					if (line.startsWith("Jornada")) {
						if (started) {
							//save previous jornada
							calendar.add(new JornadaEntity(matches));
						}
						matches = new ArrayList<>();
						started = true;
					} else {
						// is a match
						MatchEntity matchEntity = new MatchEntity(line);
						matches.add(matchEntity);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
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

	public static TeamEntity initTeam(Context context, String teamName) {
		TeamEntity teamEntity = null;
		List<TeamEntity> teamEntities = initTeams(context);
		for (TeamEntity entity : teamEntities) {
			if (entity.getTeamName().equals(teamName)) {
				teamEntity = entity;
			}
		}
		return teamEntity;
	}

	public static List<TeamEntity> initFavoritesTeams(Context context) {
		List<TeamEntity> teamEntitiesFavorites = new ArrayList<>();
		List<TeamEntity> teamEntities = initTeams(context);
		String key = context.getString(R.string.key_favorites_teams);
		Set<String> teamsFavoritesSet = new HashSet<String>(getFavorites(context, key));
		for (TeamEntity teamEntity : teamEntities) {
			if (teamsFavoritesSet.contains(teamEntity.getTeamName())) {
				teamEntitiesFavorites.add(teamEntity);
			}
		}
		return teamEntitiesFavorites;
	}


	public static List<String> getCategories (Context context, String sport) {
		List<String> categories = new ArrayList<>();
		AssetManager assetManager = context.getResources().getAssets();
		InputStreamReader inputStream = null;
		try {
			inputStream = new InputStreamReader(assetManager.open("categories.txt"));
			BufferedReader reader = new BufferedReader(inputStream);
			String line;
			boolean started = false;
			while ((line = reader.readLine()) != null) {
				if (sport.equals(line)) {
					started = true;
				} else {
					if (started) {
						if (line.startsWith(LegaSportsConstants.TAB)) {
							categories.add(line.replaceAll(LegaSportsConstants.TAB, ""));
						} else {
							started = false;
						}
					}
				}
			}
			inputStream = new InputStreamReader(assetManager.open("categories.txt"));
			reader = new BufferedReader(inputStream);
			if (categories.size()==0) {
				while ((line = reader.readLine()) != null) {
					if (LegaSportsConstants.DEFAULT_SPORT.equals(line)) {
						started = true;
					} else {
						if (started) {
							if (line.startsWith(LegaSportsConstants.TAB)) {
								categories.add(line.replaceAll(LegaSportsConstants.TAB, ""));
							} else {
								started = false;
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return categories;
	}

	public static String getStringResourceByName(Context context, String aString) {
		Log.d(TAG, "getStringResourceByName: " + aString);
		String packageName = context.getPackageName();
		int resId = context.getResources().getIdentifier(aString, "string", packageName);
		String strResource = context.getString(R.string.NOT_FOUND);
		try {
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
}



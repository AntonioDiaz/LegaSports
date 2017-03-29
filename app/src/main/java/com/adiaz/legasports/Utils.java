package com.adiaz.legasports;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.adiaz.legasports.entities.JornadaEntity;
import com.adiaz.legasports.entities.MatchEntity;
import com.adiaz.legasports.entities.TeamEntity;
import com.adiaz.legasports.entities.TeamMatchEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/* Created by toni on 28/03/2017. */

public class Utils {

	private static final String TAG = Utils.class.getSimpleName();
	
	public static List<TeamEntity> initTeams(Context context) {
		List<TeamEntity> teams = new ArrayList<>();
		List<JornadaEntity> jornadas = initCalendar(context);
		JornadaEntity jornadaEntity = jornadas.get(0);
		Set<String> teamsSet = new HashSet<>();
		for (MatchEntity matchEntity : jornadaEntity.getMatches()) {
			teamsSet.add(matchEntity.getTeamLocal());
			teamsSet.add(matchEntity.getTeamVisitor());
		}
		Log.d(TAG, "initTeams: teamsSet " + teamsSet.size());
		List<String> teamsList = new ArrayList<>();
		teamsList.addAll(teamsSet);
		Collections.sort(teamsList);
		for (String s : teamsList) {
			List <TeamMatchEntity> matches = new ArrayList<>();
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
						try {
							teamMatchEntity.setDate(dateFormat.parse(matchEntity.getDate() + " " + matchEntity.getHour()));
						} catch (ParseException e) {
							Log.e(TAG, "initTeams: error in parse", e);
						}
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



	public static List<JornadaEntity> initCalendar(Context context) {
		List<JornadaEntity> calendar = new ArrayList<>();
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
		return calendar;
	}


	public static boolean checkIfFavoritSelected(Context context, String teamName) {
		SharedPreferences preferences = getDefaultSharedPreferences(context);
		Set<String> defaultSet = new HashSet<>();
		String key = context.getString(R.string.key_favorites);
		Set<String> stringsSet = preferences.getStringSet(key, defaultSet);
		Log.d(TAG, "checkIfFavoritSelected: stringsSet " + stringsSet);
		return stringsSet.contains(teamName);
	}


	public static void unMarkFavorite(ChampionshipActivity context, String myTeamName) {
		Utils.updateListFavorites(context, myTeamName, false);
	}


	public static void markFavorite(Context context, String myTeamName) {
		Utils.updateListFavorites(context, myTeamName, true);
	}

	private static void updateListFavorites(Context context, String myTeamName, boolean addFavorite) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String key = context.getString(R.string.key_favorites);
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

	public static List<String> favoritesList(Context context) {
		SharedPreferences preferences = getDefaultSharedPreferences(context);
		String key = context.getString(R.string.key_favorites);
		Set<String> favoritesSet = preferences.getStringSet(key, new HashSet<String>());
		List<String> favoritesList = new ArrayList<>(favoritesSet);
		Collections.sort(favoritesList);
		return favoritesList;
	}

	public static TeamEntity initTeam(Context context, String teamName) {
		TeamEntity teamEntity = null;
		List<TeamEntity> teamEntities = initTeams(context);
		for (TeamEntity entity : teamEntities) {
			if (teamName.equals(teamName)) {
				teamEntity = entity;
			}
		}
		return teamEntity;
	}

	public static List<TeamEntity> initFavoritesTeams(Context context) {
		List<TeamEntity> teamEntitiesFavorites = new ArrayList<>();
		List<TeamEntity> teamEntities = initTeams(context);
		Set<String> teamsFavoritesSet = new HashSet<String>(favoritesList(context));
		for (TeamEntity teamEntity : teamEntities) {
			if (teamsFavoritesSet.contains(teamEntity.getTeamName())) {
				teamEntitiesFavorites.add(teamEntity);
			}
		}
		return teamEntitiesFavorites;
	}
}



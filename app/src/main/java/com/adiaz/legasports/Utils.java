package com.adiaz.legasports;

import android.content.Context;
import android.content.res.AssetManager;
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
						DateFormat dateFormat = new SimpleDateFormat(LegaSportsConstants.DATE_FORMAT);
						try {
							teamMatchEntity.setDate(dateFormat.parse(matchEntity.getDate() + " " + matchEntity.getHour()));
						} catch (ParseException e) {
							Log.e(TAG, "initTeams: error in parse", e);
						}
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


}

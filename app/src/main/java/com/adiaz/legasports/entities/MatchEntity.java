package com.adiaz.legasports.entities;

import android.util.Log;

/** Created by toni on 23/03/2017. */

public class MatchEntity {

	private static final String TAG = MatchEntity.class.getSimpleName();
	private String date;
	private String hour;
	private String teamLocal;
	private String teamVisitor;
	private int scoreLocal;
	private int scoreVisitor;
	private String place;


	public MatchEntity(String line) {
		Log.d(TAG, "MatchEntity: "  + line);
		String[] strings = line.split("\\t");
		date = strings[0];
		hour = strings[1];
		teamLocal = strings[2];
		teamVisitor = strings[3];
		if (strings.length>=5) {
			place = strings[4];
		}
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getTeamLocal() {
		return teamLocal;
	}

	public void setTeamLocal(String teamLocal) {
		this.teamLocal = teamLocal;
	}

	public String getTeamVisitor() {
		return teamVisitor;
	}

	public void setTeamVisitor(String teamVisitor) {
		this.teamVisitor = teamVisitor;
	}

	public int getScoreLocal() {
		return scoreLocal;
	}

	public void setScoreLocal(int scoreLocal) {
		this.scoreLocal = scoreLocal;
	}

	public int getScoreVisitor() {
		return scoreVisitor;
	}

	public void setScoreVisitor(int scoreVisitor) {
		this.scoreVisitor = scoreVisitor;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}
}

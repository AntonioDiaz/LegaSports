package com.adiaz.munisports.entities;

import java.util.Date;

/** Created by toni on 23/03/2017. */

public class MatchEntity {

	private static final String TAG = MatchEntity.class.getSimpleName();
	private Date date;
	private String teamLocal;
	private String teamVisitor;
	private int scoreLocal;
	private int scoreVisitor;
	private String placeName;
	private String placeAddress;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getPlaceAddress() {
		return placeAddress;
	}

	public void setPlaceAddress(String placeAddress) {
		this.placeAddress = placeAddress;
	}
}

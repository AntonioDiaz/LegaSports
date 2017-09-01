package com.adiaz.munisports.entities;

import java.util.Date;

/* Created by toni on 28/03/2017. */

public class TeamMatch {

	private boolean isLocal;
	private Date date;
	private String placeName;
	private String placeAddress;
	private String opponent;
	private Integer opponentScore;
	private Integer teamScore;

	public TeamMatch() {
		super();
	}


	public boolean isLocal() {
		return isLocal;
	}

	public void setLocal(boolean local) {
		isLocal = local;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getOpponent() {
		return opponent;
	}

	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}

	public Integer getOpponentScore() {
		return opponentScore;
	}

	public void setOpponentScore(Integer opponentScore) {
		this.opponentScore = opponentScore;
	}

	public Integer getTeamScore() {
		return teamScore;
	}

	public void setTeamScore(Integer teamScore) {
		this.teamScore = teamScore;
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

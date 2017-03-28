package com.adiaz.legasports.entities;

import java.util.Date;

/* Created by toni on 28/03/2017. */

public class TeamMatchEntity {

	private boolean isLocal;
	private Date date;
	private String opponent;
	private Integer opponentScore;
	private Integer teamScore;

	public TeamMatchEntity(boolean isLocal, Date date, String opponent) {
		this.isLocal = isLocal;
		this.date = date;
		this.opponent = opponent;
	}

	public TeamMatchEntity() {
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
}

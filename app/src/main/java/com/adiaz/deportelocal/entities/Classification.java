package com.adiaz.deportelocal.entities;

/**
 * Created by toni on 03/05/2017.
 */

public class Classification {
	private Integer position = 0;
	private String team;
	private Integer matchesPlayed = 0;
	private Integer matchesWon = 0;
	private Integer matchesDrawn = 0;
	private Integer matchesLost = 0;
	private Integer points = 0;
	private Integer sanctions = 0;

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public Integer getMatchesPlayed() {
		return matchesPlayed;
	}

	public void setMatchesPlayed(Integer matchesPlayed) {
		this.matchesPlayed = matchesPlayed;
	}

	public Integer getMatchesWon() {
		return matchesWon;
	}

	public void setMatchesWon(Integer matchesWon) {
		this.matchesWon = matchesWon;
	}

	public Integer getMatchesLost() {
		return matchesLost;
	}

	public void setMatchesLost(Integer matchesLost) {
		this.matchesLost = matchesLost;
	}

	public Integer getMatchesDrawn() {
		return matchesDrawn;
	}

	public void setMatchesDrawn(Integer matchesDrawn) {
		this.matchesDrawn = matchesDrawn;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getSanctions() {
		return sanctions;
	}

	public void setSanctions(Integer sanctions) {
		this.sanctions = sanctions;
	}
}


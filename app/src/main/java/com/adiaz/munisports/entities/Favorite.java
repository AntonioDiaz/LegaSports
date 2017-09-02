package com.adiaz.munisports.entities;

/**
 * Created by toni on 02/09/2017.
 */

public class Favorite {
	private Long id;
	private Long idCompetition;
	private String teamName;
	private Long lastNotification;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdCompetition() {
		return idCompetition;
	}

	public void setIdCompetition(Long idCompetition) {
		this.idCompetition = idCompetition;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Long getLastNotification() {
		return lastNotification;
	}

	public void setLastNotification(Long lastNotification) {
		this.lastNotification = lastNotification;
	}
}

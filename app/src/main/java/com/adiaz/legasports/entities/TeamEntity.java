package com.adiaz.legasports.entities;

import java.util.ArrayList;
import java.util.List;

/* Created by toni on 28/03/2017. */

public class TeamEntity {

	private String teamName;
	private List<TeamMatchEntity> matches;

	public TeamEntity(String teamName) {
		this.teamName = teamName;
		this.matches = new ArrayList<>();
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public List<TeamMatchEntity> getMatches() {
		return matches;
	}

	public void setMatches(List<TeamMatchEntity> matches) {
		this.matches = matches;
	}
}

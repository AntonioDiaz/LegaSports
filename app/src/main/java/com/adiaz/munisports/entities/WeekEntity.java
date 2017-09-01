package com.adiaz.munisports.entities;

import java.util.List;

/** Created by toni on 23/03/2017. */

public class WeekEntity {

	List<Match> matches;

	public WeekEntity(List<Match> matches) {
		this.matches = matches;
	}

	public List<Match> getMatches() {
		return matches;
	}

	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}
}

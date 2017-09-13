package com.adiaz.munisports.entities;

import android.content.Context;

import com.adiaz.munisports.R;
import com.adiaz.munisports.utilities.MuniSportsConstants;
import com.adiaz.munisports.utilities.MuniSportsCourts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/** Created by toni on 23/03/2017. */
public class Match {

	private static final String TAG = Match.class.getSimpleName();
	private Date date;
	private String teamLocal;
	private String teamVisitor;
	private Integer scoreLocal;
	private Integer scoreVisitor;
	private int week;
	private int state;
	private Long idSportCenter;

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

	public Integer getScoreLocal() {
		return scoreLocal;
	}

	public void setScoreLocal(Integer scoreLocal) {
		this.scoreLocal = scoreLocal;
	}

	public Integer getScoreVisitor() {
		return scoreVisitor;
	}

	public void setScoreVisitor(Integer scoreVisitor) {
		this.scoreVisitor = scoreVisitor;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public Long getIdSportCenter() {
		return idSportCenter;
	}

	public void setIdSportCenter(Long idSportCenter) {
		this.idSportCenter = idSportCenter;
	}

	public String obtainDateStr(Context context) {
		String dateStr = context.getString(R.string.undefined_date);
		if (this.getDate()!=null && this.getDate().getTime()!=0) {
			DateFormat dateFormat = new SimpleDateFormat(MuniSportsConstants.DATE_FORMAT);
			dateStr = dateFormat.format(this.getDate());
		}
		return dateStr;
	}

	public String obtainCenterName(Context context) {
		Court court = MuniSportsCourts.obteinTownCourt(context, this.getIdSportCenter());
		String strName = context.getString(R.string.undefined_center);
		if (court!=null) {
			strName = court.getCenterName();
		}
		return strName;
	}

	public String obtainCenterNameFull(Context context) {
		Court court = MuniSportsCourts.obteinTownCourt(context, this.getIdSportCenter());
		String strName = context.getString(R.string.undefined_court);
		if (court!=null) {
			strName = court.getCourtFullName();
		}
		return strName;

	}

	public String obtainCenterAddress(Context context) {
		Court court = MuniSportsCourts.obteinTownCourt(context, this.getIdSportCenter());
		String strName = "";
		if (court!=null) {
			strName = court.getCenterAddress();
		}
		return strName;
	}

}

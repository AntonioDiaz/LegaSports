package com.adiaz.munisports.utilities;

import android.content.Context;

import com.adiaz.munisports.R;
import com.adiaz.munisports.entities.Court;
import com.adiaz.munisports.entities.Match;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by toni on 16/09/2017.
 */

public class MatchUtilities {

	public static String obtainDateStr(Context context, Match match) {
		String dateStr = context.getString(R.string.undefined_date);
		if (match.date()!=null && match.date().getTime()!=0) {
			DateFormat dateFormat = new SimpleDateFormat(MuniSportsConstants.DATE_FORMAT);
			dateStr = dateFormat.format(match.date());
		}
		return dateStr;
	}

	public static String obtainCenterName(Context context, Match match) {
		Court court = MuniSportsCourts.obteinTownCourt(context, match.idSportCenter());
		String strName = context.getString(R.string.undefined_center);
		if (court!=null) {
			strName = court.getCenterName();
		}
		return strName;
	}

	public static String obtainCenterNameFull(Context context, Match match) {
		Court court = MuniSportsCourts.obteinTownCourt(context, match.idSportCenter());
		String strName = context.getString(R.string.undefined_court);
		if (court!=null) {
			strName = court.getCourtFullName();
		}
		return strName;
	}

	public static String obtainCenterAddress(Context context, Match match) {
		Court court = MuniSportsCourts.obteinTownCourt(context, match.idSportCenter());
		String strName = "";
		if (court!=null) {
			strName = court.getCenterAddress();
		}
		return strName;
	}

	public static String obtainMatchDescription(Context context, Match match) {
		String teamLocal = match.teamLocal() != null ? match.teamLocal() : "_";
		String teamVisitor = match.teamVisitor() != null ? match.teamVisitor() : "_";
		return context.getString(R.string.dialog_match_description, match.week().toString(), teamLocal, teamVisitor);
	}
}

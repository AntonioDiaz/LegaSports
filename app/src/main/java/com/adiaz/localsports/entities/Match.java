package com.adiaz.localsports.entities;

import android.content.Context;
import android.os.Parcelable;

import com.adiaz.localsports.R;
import com.adiaz.localsports.utilities.DateTypeAdapter;
import com.adiaz.localsports.utilities.LocalSportsConstants;
import com.adiaz.localsports.utilities.LocalSportsCourts;
import com.google.auto.value.AutoValue;
import com.ryanharter.auto.value.parcel.ParcelAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by toni on 16/09/2017.
 */

@AutoValue
public abstract class Match implements Parcelable {

	public static Builder builder() {
		return new AutoValue_Match.Builder();
	}

	@AutoValue.Builder
	public abstract static class Builder {
		public abstract Builder setIdMatch(Long value);
		public abstract Builder setDate(Date value);
		public abstract Builder setTeamLocal(String value);
		public abstract Builder setTeamVisitor(String value);
		public abstract Builder setScoreLocal(Integer value);
		public abstract Builder setScoreVisitor(Integer value);
		public abstract Builder setWeek(Integer value);
		public abstract Builder setState(Integer value);
		public abstract Builder setIdSportCenter(Long value);
		public abstract Match build();
	}


	public abstract Long idMatch();
	@ParcelAdapter(DateTypeAdapter.class) public abstract Date date();
	public abstract String teamLocal();
	public abstract String teamVisitor();
	public abstract Integer scoreLocal();
	public abstract Integer scoreVisitor();
	public abstract Integer week();
	public abstract Integer state();
	public abstract Long idSportCenter();

	public boolean isDateDefined() {
		return (this.date()!=null && this.date().getTime()!=0);
	}

	public boolean isCourtDefinded(Context context) {
		Court court = LocalSportsCourts.obteinTownCourt(context, this.idSportCenter());
		return court!=null;
	}

	public String obtainDateStr(Context context) {
		String dateStr = context.getString(R.string.undefined_date);
		if (this.isDateDefined()) {
			DateFormat dateFormat = new SimpleDateFormat(LocalSportsConstants.DATE_FORMAT);
			dateStr = dateFormat.format(this.date());
		}
		return dateStr;
	}

	public String obtainCenterName(Context context) {
		Court court = LocalSportsCourts.obteinTownCourt(context, this.idSportCenter());
		String strName = context.getString(R.string.undefined_center);
		if (court!=null) {
			strName = court.getCenterName();
		}
		return strName;
	}

	public String obtainCenterNameFull(Context context) {
		Court court = LocalSportsCourts.obteinTownCourt(context, this.idSportCenter());
		String strName = context.getString(R.string.undefined_court);
		if (court!=null) {
			strName = court.getCourtFullName();
		}
		return strName;
	}

	public String obtainCenterAddress(Context context) {
		Court court = LocalSportsCourts.obteinTownCourt(context, this.idSportCenter());
		String strName = "";
		if (court!=null) {
			strName = court.getCenterAddress();
		}
		return strName;
	}

	public String obtainMatchDescription(Context context) {
		String teamLocal = this.teamLocal() != null ? this.teamLocal() : "_";
		String teamVisitor = this.teamVisitor() != null ? this.teamVisitor() : "_";
		return context.getString(R.string.dialog_match_description, this.week().toString(), teamLocal, teamVisitor);
	}

}
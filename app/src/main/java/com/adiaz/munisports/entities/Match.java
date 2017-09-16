package com.adiaz.munisports.entities;

import android.content.Context;
import android.os.Parcelable;

import com.adiaz.munisports.R;
import com.adiaz.munisports.utilities.DateTypeAdapter;
import com.adiaz.munisports.utilities.MuniSportsConstants;
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


	public String obtainDateStr(Context context) {
		String dateStr = context.getString(R.string.undefined_date);
		if (this.date()!=null && this.date().getTime()!=0) {
			DateFormat dateFormat = new SimpleDateFormat(MuniSportsConstants.DATE_FORMAT);
			dateStr = dateFormat.format(this.date());
		}
		return dateStr;
	}

}
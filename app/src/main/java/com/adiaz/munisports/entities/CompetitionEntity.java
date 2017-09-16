package com.adiaz.munisports.entities;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

/**
 * Created by toni on 16/09/2017.
 */

@AutoValue
public abstract class CompetitionEntity implements Parcelable {

	public static Builder builder() {
		return new AutoValue_CompetitionEntity.Builder();
	}

	@AutoValue.Builder
	public abstract static class Builder {
		public abstract Builder setName(String value);
		public abstract Builder setSportName(String value);
		public abstract Builder setCategoryName(String value);
		public abstract Builder setServerId(Long value);
		public abstract Builder setLastUpdateServer(Long value);
		public abstract Builder setLastUpdateApp(Long value);
		public abstract CompetitionEntity build();
	}

	public abstract String name();
	public abstract String sportName();
	public abstract String categoryName();
	public abstract Long serverId();
	public abstract Long lastUpdateServer();
	public abstract Long lastUpdateApp();
}
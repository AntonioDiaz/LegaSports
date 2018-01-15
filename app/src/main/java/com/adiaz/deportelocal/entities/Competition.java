package com.adiaz.deportelocal.entities;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

/**
 * Created by toni on 16/09/2017.
 */

@AutoValue
public abstract class Competition implements Parcelable {

	public static Builder builder() {
		return new AutoValue_Competition.Builder();
	}

	@AutoValue.Builder
	public abstract static class Builder {
		public abstract Builder setName(String value);
		public abstract Builder setSportName(String value);
		public abstract Builder setCategoryName(String value);
		public abstract Builder setServerId(Long value);
		public abstract Builder setIsDirty(boolean isDirty);
		public abstract Competition build();
	}

	public abstract String name();
	public abstract String sportName();
	public abstract String categoryName();
	public abstract Long serverId();
	public abstract boolean isDirty();
}
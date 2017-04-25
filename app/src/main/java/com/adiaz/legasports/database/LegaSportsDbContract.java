package com.adiaz.legasports.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by toni on 20/04/2017.
 */

public class LegaSportsDbContract {

	public static final String AUTHORITY = "com.adiaz.legasports";
	public static final Uri BASE_CONTENT = Uri.parse("content://" + AUTHORITY);
	public static final String PATH_COMPETITIONS = "competitions";
	public static final String PATH_MATCHES = "matches";



	public static final	class CompetitionsEntry implements BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_COMPETITIONS).build();

		public static final String TABLE_NAME = "competitions";
		public static final String COLUMN_LAST_UPDATE = "last_update";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_SPORT = "sport";
		public static final String COLUMN_CATEGORY = "category";
		public static final String COLUMN_ID_SERVER = "id_server";
	}

	public static final	class MatchesEntry implements BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_MATCHES).build();

		public static final String TABLE_NAME = "matches";
		public static final String COLUMN_LAST_UPDATE = "last_update";
		public static final String COLUMN_TEAM_LOCAL = "team_local";
		public static final String COLUMN_TEAM_VISITOR = "team_visitor";
		public static final String COLUMN_SCORE_LOCAL = "score_local";
		public static final String COLUMN_SCORE_VISITOR = "score_visitor";
		public static final String COLUMN_WEEK = "week";
		public static final String COLUMN_PLACE = "place";
		public static final String COLUMN_DATE = "date";
		public static final String COLUMN_ID_SERVER = "id_server";
		public static final String COLUMN_ID_COMPETITION_SERVER = "id_competition_server";
	}
}

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

		public static Uri buildCompetitionsUriWithSports(String sport) {
			return CONTENT_URI.buildUpon()
					.appendPath(sport)
					.build();
		}

		public static final String TABLE_NAME = "competitions";
		public static final String COLUMN_LAST_UPDATE = "last_update";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_SPORT = "sport";
		public static final String COLUMN_CATEGORY = "category";
		public static final String COLUMN_CATEGORY_ORDER = "category_order";
		public static final String COLUMN_ID_SERVER = "id_server";

		public static final String[] PROJECTION = {
				COLUMN_LAST_UPDATE,
				COLUMN_NAME,
				COLUMN_SPORT,
				COLUMN_CATEGORY,
				COLUMN_ID_SERVER
		};
		public static final int INDEX_LAST_UDPATE = 0;
		public static final int INDEX_NAME = 1;
		public static final int INDEX_SPORT = 2;
		public static final int INDEX_CATEGORY = 3;
		public static final int INDEX_ID_SERVER = 4;
	}

	public static final	class MatchesEntry implements BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_MATCHES).build();

		public static Uri buildMatchesUriWithCompetitions(String competition) {
			return CONTENT_URI.buildUpon()
					.appendPath(competition)
					.build();
		}

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

		public static final String[] PROJECTION = {
				COLUMN_LAST_UPDATE,
				COLUMN_TEAM_LOCAL,
				COLUMN_TEAM_VISITOR,
				COLUMN_SCORE_LOCAL,
				COLUMN_SCORE_VISITOR,
				COLUMN_WEEK,
				COLUMN_PLACE,
				COLUMN_DATE,
				COLUMN_ID_SERVER,
				COLUMN_ID_COMPETITION_SERVER
		};
		public static final int INDEX_LAST_UDPATE = 0;
		public static final int INDEX_TEAM_LOCAL = 1;
		public static final int INDEX_TEAM_VISITOR = 2;
		public static final int INDEX_SCORE_LOCAL = 3;
		public static final int INDEX_SCORE_VISITOR = 4;
		public static final int INDEX_WEEK = 5;
		public static final int INDEX_PLACE = 6;
		public static final int INDEX_DATE = 7;
		public static final int INDEX_ID_SERVER = 8;
		public static final int INDEX_ID_COMPETITION_SERVER = 9;
	}
}

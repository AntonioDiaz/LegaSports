package com.adiaz.munisports.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by toni on 20/04/2017.
 */

public class MuniSportsDbContract {

	public static final String AUTHORITY = "com.adiaz.munisports";
	public static final Uri BASE_CONTENT = Uri.parse("content://" + AUTHORITY);
	public static final String PATH_COMPETITIONS = "competitions";
	public static final String PATH_MATCHES = "matches";
	public static final String PATH_CLASSIFICATION = "classification";
	public static final String PATH_SPORT_COURTS = "sportcourts";



	public static final	class CompetitionsEntry implements BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_COMPETITIONS).build();

		public static Uri buildCompetitionUriWithServerId(Long competitionServerId) {
			return CONTENT_URI.buildUpon()
					.appendPath(competitionServerId.toString())
					.build();
		}

		public static Uri buildCompetitionsUriWithSports(String sport) {
			return CONTENT_URI.buildUpon()
					.appendPath(sport)
					.build();
		}

		public static final String TABLE_NAME = "competitions";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_SPORT = "sport";
		public static final String COLUMN_CATEGORY = "category";
		public static final String COLUMN_CATEGORY_ORDER = "category_order";
		public static final String COLUMN_ID_SERVER = "id_server";
		public static final String COLUMN_LAST_UPDATE_SERVER = "last_update_server";
		public static final String COLUMN_LAST_UPDATE_APP = "last_update_app";

		public static final String[] PROJECTION = {
				COLUMN_NAME,
				COLUMN_SPORT,
				COLUMN_CATEGORY,
				COLUMN_ID_SERVER,
				COLUMN_LAST_UPDATE_SERVER,
				COLUMN_LAST_UPDATE_APP
		};
		public static final int INDEX_NAME = 0;
		public static final int INDEX_SPORT = 1;
		public static final int INDEX_CATEGORY = 2;
		public static final int INDEX_ID_SERVER = 3;
		public static final int INDEX_LAST_UPDATE_SERVER = 4;
		public static final int INDEX_LAST_UPDATE_APP = 5;
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
		public static final String COLUMN_ID_SPORTCENTER = "id_sportcenter";
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
				COLUMN_ID_SPORTCENTER,
				COLUMN_DATE,
				COLUMN_ID_SERVER,
				COLUMN_ID_COMPETITION_SERVER
		};
		public static final int INDEX_LAST_UPDATE = 0;
		public static final int INDEX_TEAM_LOCAL = 1;
		public static final int INDEX_TEAM_VISITOR = 2;
		public static final int INDEX_SCORE_LOCAL = 3;
		public static final int INDEX_SCORE_VISITOR = 4;
		public static final int INDEX_WEEK = 5;
		public static final int INDEX_ID_SPORTCENTER = 6;
		public static final int INDEX_DATE = 7;
		public static final int INDEX_ID_SERVER = 8;
		public static final int INDEX_ID_COMPETITION_SERVER = 9;
	}

	public static final	class ClassificationEntry implements BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_CLASSIFICATION).build();

		public static Uri buildClassificationUriWithCompetitions(String competition) {
			return CONTENT_URI.buildUpon()
					.appendPath(competition)
					.build();
		}

		public static final String TABLE_NAME = "classification";
		public static final String COLUMN_POSITION = "position";
		public static final String COLUMN_TEAM = "team";
		public static final String COLUMN_POINTS = "points";
		public static final String COLUMN_MATCHES_PLAYED = "matches_played";
		public static final String COLUMN_MATCHES_WON = "matches_won";
		public static final String COLUMN_MATCHES_DRAWN = "matches_drawn";
		public static final String COLUMN_MATCHES_LOST = "matches_lost";
		public static final String COLUMN_ID_COMPETITION_SERVER = "id_competition_server";


		public static final String[] PROJECTION = {
				COLUMN_POSITION,
				COLUMN_TEAM,
				COLUMN_POINTS,
				COLUMN_MATCHES_PLAYED,
				COLUMN_MATCHES_WON,
				COLUMN_MATCHES_DRAWN,
				COLUMN_MATCHES_LOST,
				COLUMN_ID_COMPETITION_SERVER
		};

		public static final int INDEX_POSITION = 0;
		public static final int INDEX_TEAM = 1;
		public static final int INDEX_POINTS = 2;
		public static final int INDEX_MACHES_PLAYED = 3;
		public static final int INDEX_MACHES_WON = 4;
		public static final int INDEX_MACHES_DRAWN = 5;
		public static final int INDEX_MACHES_LOST = 6;
		public static final int INDEX_ID_COMPETITION_SERVER = 7;
	}

	public static final	class SportCourtsEntry implements BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_SPORT_COURTS).build();
		public static Uri buildSportCourtsUri(Long sportCourtId) {
			return CONTENT_URI.buildUpon()
					.appendPath(sportCourtId.toString())
					.build();
		}
		public static final String TABLE_NAME = "SportCenterCourt";
		public static final String COLUMN_ID_SERVER = "id_server";
		public static final String COLUMN_CENTER_NAME = "center_name";
		public static final String COLUMN_COURT_NAME = "court_name";
		public static final String COLUMN_CENTER_ADDRESS = "center_address";


		public static final String[] PROJECTION = {COLUMN_ID_SERVER, COLUMN_CENTER_NAME, COLUMN_COURT_NAME, COLUMN_CENTER_ADDRESS};

		public static final int INDEX_ID_SERVER = 0;
		public static final int INDEX_CENTER_NAME = 1;
		public static final int INDEX_COURT_NAME = 2;
		public static final int INDEX_CENTER_ADDRESS = 3;

	}

}



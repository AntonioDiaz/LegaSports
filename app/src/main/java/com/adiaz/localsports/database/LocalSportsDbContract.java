package com.adiaz.localsports.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.adiaz.localsports.entities.Classification;
import com.adiaz.localsports.entities.Competition;
import com.adiaz.localsports.entities.Favorite;
import com.adiaz.localsports.entities.Match;
import com.adiaz.localsports.entities.Sport;

import java.util.Date;

/**
 * Created by toni on 20/04/2017.
 */

public class LocalSportsDbContract {

	public static final String AUTHORITY = "com.adiaz.localsports";
	public static final Uri BASE_CONTENT = Uri.parse("content://" + AUTHORITY);
	public static final String PATH_COMPETITIONS = "competitions";
	public static final String PATH_MATCHES = "matches";
	public static final String PATH_CLASSIFICATION = "classification";
	public static final String PATH_SPORT_COURTS = "sportcourts";
	public static final String PATH_FAVORITES = "favorites";
	public static final String PATH_SPORTS = "sports";

	public static final	class CompetitionsEntry implements BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_COMPETITIONS).build();

		public static final Uri buildCompetitionUriWithServerId(Long competitionServerId) {
			return CONTENT_URI.buildUpon()
					.appendPath(competitionServerId.toString())
					.build();
		}

		public static final Uri buildCompetitionsUriWithSports(String sport) {
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
		public static final String COLUMN_IS_DIRTY = "is_dirty";


		public static final String[] PROJECTION = {
				COLUMN_NAME,
				COLUMN_SPORT,
				COLUMN_CATEGORY,
				COLUMN_ID_SERVER,
                COLUMN_IS_DIRTY

		};
		public static final int INDEX_NAME = 0;
		public static final int INDEX_SPORT = 1;
		public static final int INDEX_CATEGORY = 2;
		public static final int INDEX_ID_SERVER = 3;
		public static final int INDEX_IS_DIRTY = 4;


		public static Competition initEntity(Cursor c) {
			Competition competition = Competition.builder()
					.setServerId(c.getLong(CompetitionsEntry.INDEX_ID_SERVER))
					.setName(c.getString(CompetitionsEntry.INDEX_NAME))
					.setSportName(c.getString(CompetitionsEntry.INDEX_SPORT))
					.setCategoryName(c.getString(CompetitionsEntry.INDEX_CATEGORY))
					.setIsDirty(c.getInt(CompetitionsEntry.INDEX_IS_DIRTY)==1)
					.build();
			return competition;
		}

	}

	public static final	class MatchesEntry implements BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_MATCHES).build();
		public static final Uri buildMatchesUriWithCompetitions(Long competition) {
			return CONTENT_URI.buildUpon()
					.appendPath(competition.toString())
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
		public static final String COLUMN_STATE = "state";

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
				COLUMN_ID_COMPETITION_SERVER,
				COLUMN_STATE
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
		public static final int INDEX_STATE = 10;

		public static Match initEntity(Cursor cursorMatches) {
			Long dateLong = cursorMatches.getLong(MatchesEntry.INDEX_DATE);
			Match match = Match.builder()
					.setIdMatch(cursorMatches.getLong(MatchesEntry.INDEX_ID_SERVER))
					.setDate(new Date(dateLong))
					.setScoreLocal(cursorMatches.getInt(MatchesEntry.INDEX_SCORE_LOCAL))
					.setScoreVisitor(cursorMatches.getInt(MatchesEntry.INDEX_SCORE_VISITOR))
					.setTeamLocal(cursorMatches.getString(MatchesEntry.INDEX_TEAM_LOCAL))
					.setTeamVisitor(cursorMatches.getString(MatchesEntry.INDEX_TEAM_VISITOR))
					.setWeek(cursorMatches.getInt(MatchesEntry.INDEX_WEEK))
					.setState(cursorMatches.getInt(MatchesEntry.INDEX_STATE))
					.setIdSportCenter(cursorMatches.getLong(MatchesEntry.INDEX_ID_SPORTCENTER))
					.build();
			return match;
		}
	}

	public static final	class ClassificationEntry implements BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_CLASSIFICATION).build();
		public static final Uri buildClassificationUriWithCompetitions(Long competition) {
			return CONTENT_URI.buildUpon()
					.appendPath(competition.toString())
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
		public static final String COLUMN_SANCTIONS = "sanctions";


		public static final String[] PROJECTION = {
				COLUMN_POSITION,
				COLUMN_TEAM,
				COLUMN_POINTS,
				COLUMN_MATCHES_PLAYED,
				COLUMN_MATCHES_WON,
				COLUMN_MATCHES_DRAWN,
				COLUMN_MATCHES_LOST,
				COLUMN_ID_COMPETITION_SERVER,
				COLUMN_SANCTIONS
		};

		public static final int INDEX_POSITION = 0;
		public static final int INDEX_TEAM = 1;
		public static final int INDEX_POINTS = 2;
		public static final int INDEX_MACHES_PLAYED = 3;
		public static final int INDEX_MACHES_WON = 4;
		public static final int INDEX_MACHES_DRAWN = 5;
		public static final int INDEX_MACHES_LOST = 6;
		public static final int INDEX_ID_COMPETITION_SERVER = 7;
		public static final int INDEX_SANCTIONS = 8;

		public static Classification initEntity(Cursor cursor) {
			Classification classification = new Classification();
			classification.setPosition(cursor.getInt(INDEX_POSITION));
			classification.setTeam(cursor.getString(INDEX_TEAM));
			classification.setPoints(cursor.getInt(INDEX_POINTS));
			classification.setMatchesPlayed(cursor.getInt(INDEX_MACHES_PLAYED));
			classification.setMatchesWon(cursor.getInt(INDEX_MACHES_WON));
			classification.setMatchesDrawn(cursor.getInt(INDEX_MACHES_DRAWN));
			classification.setMatchesLost(cursor.getInt(INDEX_MACHES_LOST));
			classification.setSanctions(cursor.getInt(INDEX_SANCTIONS));
			return classification;
		}
	}

	public static final	class SportCourtsEntry implements BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_SPORT_COURTS).build();
		public static final Uri buildSportCourtsUri(Long sportCourtId) {
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

	public static final class FavoritesEntry implements BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_FAVORITES).build();
		public static final Uri buildFavoritesUri (Long id) {
			return CONTENT_URI.buildUpon()
					.appendPath(id.toString())
					.build();
		}
		public static final String TABLE_NAME = "Favorites";
		public static final String COLUMN_ID_COMPETITION = "id_competition";
		public static final String COLUMN_ID_TEAM = "id_team";
		public static final String COLUMN_LAST_NOTIFICATION = "last_notification";

		public static final String[] PROJECTION = {_ID, COLUMN_ID_COMPETITION, COLUMN_ID_TEAM, COLUMN_LAST_NOTIFICATION};
		public static final int INDEX_ID = 0;
		public static final int INDEX_ID_COMPETITION = 1;
		public static final int INDEX_ID_TEAM = 2;
		public static final int INDEX_ID_LAST_NOTIFICATION = 3;


		public static Favorite initEntity(Cursor cursor) {
			Favorite favorite = new Favorite();
			favorite.setId(cursor.getLong(INDEX_ID));
			favorite.setIdCompetition(cursor.getLong(INDEX_ID_COMPETITION));
			favorite.setTeamName(cursor.getString(INDEX_ID_TEAM));
			favorite.setLastNotification(cursor.getLong(INDEX_ID_LAST_NOTIFICATION));
			return favorite;
		}
	}

    public static final class SportsEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_SPORTS).build();
        public static final String TABLE_NAME = "SPORTS";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TAG = "tag";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_ORDER = "order_sport";

        public static final String[] PROJECTION = {COLUMN_ID, COLUMN_NAME, COLUMN_TAG, COLUMN_IMAGE, COLUMN_ORDER};
        public static final int INDEX_ID = 0;
        public static final int INDEX_NAME = 1;
        public static final int INDEX_TAG = 2;
        public static final int INDEX_IMAGE = 3;
        public static final int INDEX_ORDER = 4;

        public static Sport initEntity(Cursor cursor) {
            Sport sport = Sport.builder()
                    .id(cursor.getLong(INDEX_ID))
                    .name(cursor.getString(INDEX_NAME))
                    .tag(cursor.getString(INDEX_TAG))
                    .order(cursor.getInt(INDEX_ORDER))
                    .image(cursor.getString(INDEX_IMAGE)).build();
            return sport;
        }
    }

}



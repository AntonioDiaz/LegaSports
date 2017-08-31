package com.adiaz.munisports.utilities;

/* Created by toni on 28/03/2017. */

import java.util.concurrent.TimeUnit;

public class MuniSportsConstants {
	public static final java.lang.String DATE_FORMAT = "dd/MM/yyyy HH:mm";

	public static final String TAG_MATCH_DATE = "tag_match_date";
	public static final String TAG_MATCH_TEAM = "tag_match_team";
	public static final String TAG_MATCH_OPPONENT = "tag_match_oponent";
	public static final String TAG_MATCH_PLACE = "tag_match_place";

	public static final String TAB = "    ";
	public static final String DEFAULT_SPORT = "default sport";

	public static final String BASE_URL = "http://munisports-web.appspot.com/";

	//	public static final String EXTRA_COMPETITION_ID_SERVER = "extra_competition_id_server";


	public static final String INTENT_SPORT_TAG = "intent_sport_tag";
	public static final String INTENT_CATEGORY_TAG = "intent_category_tag";
	public static final String INTENT_ID_COMPETITION_SERVER = "extra_competition_chosen";
	public static final String INTENT_COMPETITION_NAME = "intent_competition_name";
	public static final String BUNDLE_ID_COMPETITION_SERVER = "bundle_id_competition_server";
	public static final String INTENT_TEAM_NAME = "intent_team_name";
	public static final String KEY_TOWN_NAME = "KEY_TOWN_NAME";
	public static final String KEY_TOWN_ID = "KEY_TOWN_ID";
	public static final String KEY_LASTUPDATE = "KEY_LASTUPDATE";
	public static final String KEY_FAVORITES_TEAMS = "KEY_FAVORITES_TEAMS";
	public static final String KEY_FAVORITES_COMPETITIONS = "KEY_FAVORITES_COMPETITIONS";

	public static final String UNDEFINDED_FIELD = " - ";

	public static final Integer MINUTES_NECESSARY_TO_UPDATE = 30;
	public static final Long MILISECONDS_NECESSARY_TO_UPDATE = TimeUnit.MINUTES.toMillis(MINUTES_NECESSARY_TO_UPDATE);
}

package com.adiaz.deportelocal.utilities;

/* Created by toni on 28/03/2017. */

import java.util.concurrent.TimeUnit;

public class DeporteLocalConstants {
    public static final java.lang.String DATE_FORMAT = "dd/MM/yyyy HH:mm";

    public static final String TAG_MATCH_DATE = "tag_match_date";
    public static final String TAG_MATCH_TEAM = "tag_match_team";
    public static final String TAG_MATCH_OPPONENT = "tag_match_oponent";
    public static final String TAG_MATCH_PLACE = "tag_match_place";

    public static final String TAB = "    ";
    public static final String DEFAULT_SPORT = "default sport";

    public static final String BASE_URL = "https://localsports-web.appspot.com/";
    //public static final String BASE_URL = "http://192.168.0.28:8080/";

    public static final String INTENT_SPORT_TAG = "intent_sport_tag";
    public static final String INTENT_ID_COMPETITION_SERVER = "extra_competition_chosen";
    public static final String INTENT_COMPETITION = "intent_competition";
    public static final String INTENT_TEAM_NAME = "intent_team_name";
    public static final String KEY_TOWN_NAME = "KEY_TOWN_NAME";
    public static final String KEY_TOWN_ID = "KEY_TOWN_ID";
    public static final String KEY_TOWN_TOPIC = "KEY_TOPIC";
    public static final String KEY_LASTUPDATE = "KEY_LASTUPDATE";

    public static final String KEY_FAVORITES_TEAMS = "KEY_FAVORITES_TEAMS";
    public static final String KEY_UUID = "KEY_UUID";

    public static final String KEY_FAVORITES_COMPETITIONS = "KEY_FAVORITES_COMPETITIONS";

    public static final String UNDEFINDED_FIELD = " - ";

    public static final int STATE_PENDING = 0;
    public static final int STATE_PLAYED = 1;
    public static final int STATE_CANCELED = 2;
    public static final int STATE_RESTING = 3;

    public static final String INTESTITIAL_AD_ID = "ca-app-pub-3940256099942544/1033173712";
    public static final String ADMOB_ID = "ca-app-pub-3940256099942544~3347511713";
    public static final Integer INTERSTITIAL_FRECUENCY = 5;
}

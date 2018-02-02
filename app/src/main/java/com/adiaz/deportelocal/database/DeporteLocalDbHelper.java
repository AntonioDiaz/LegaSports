package com.adiaz.deportelocal.database;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.adiaz.deportelocal.utilities.DeporteLocalConstants;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.adiaz.deportelocal.database.DeporteLocalDbContract.ClassificationEntry;
import static com.adiaz.deportelocal.database.DeporteLocalDbContract.CompetitionsEntry;
import static com.adiaz.deportelocal.database.DeporteLocalDbContract.MatchesEntry;
import static com.adiaz.deportelocal.database.DeporteLocalDbContract.SportCourtsEntry;
import static com.adiaz.deportelocal.database.DeporteLocalDbContract.FavoritesEntry;
import static com.adiaz.deportelocal.database.DeporteLocalDbContract.SportsEntry;

/**
 * Created by toni on 20/04/2017.
 */
public class DeporteLocalDbHelper extends SQLiteOpenHelper {

    private static final String TAG = DeporteLocalDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "localsports.db";
    private static final int DATABASE_VERSION = 35;
    public static final String REFRESH_BROADCAST = "REFRESH_COMPETITIONS_BROADCAST";
    private Context mContext;

    public DeporteLocalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_TABLE_COMPETITION =
                "CREATE TABLE " + CompetitionsEntry.TABLE_NAME +
                        "(" +
                        CompetitionsEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                        CompetitionsEntry.COLUMN_SPORT + " TEXT NOT NULL, " +
                        CompetitionsEntry.COLUMN_CATEGORY + " TEXT NOT NULL, " +
                        CompetitionsEntry.COLUMN_CATEGORY_ORDER + " INTEGER NOT NULL, " +
                        CompetitionsEntry.COLUMN_ID_SERVER + " TEXT NOT NULL, " +
                        CompetitionsEntry.COLUMN_IS_DIRTY + " INTEGER NOT NULL, " +
                        "UNIQUE (" + CompetitionsEntry.COLUMN_ID_SERVER + ") ON CONFLICT REPLACE" +
                        ")";

        // TODO: 18/08/2017 remove last_update column, because is not used.
        String SQL_CREATE_TABLE_MATCHES =
                "CREATE TABLE " + MatchesEntry.TABLE_NAME +
                        "(" +
                        MatchesEntry.COLUMN_LAST_UPDATE + " INTEGER, " +
                        MatchesEntry.COLUMN_TEAM_LOCAL + " TEXT NOT NULL, " +
                        MatchesEntry.COLUMN_TEAM_VISITOR + " TEXT NOT NULL, " +
                        MatchesEntry.COLUMN_SCORE_LOCAL + " INTEGER NOT NULL, " +
                        MatchesEntry.COLUMN_SCORE_VISITOR + " INTEGER NOT NULL, " +
                        MatchesEntry.COLUMN_WEEK + " INTEGER NOT NULL, " +
                        MatchesEntry.COLUMN_WEEK_NAME + " TEXT, " +
                        MatchesEntry.COLUMN_ID_SPORTCENTER + " INTEGER, " +
                        MatchesEntry.COLUMN_DATE + " INTEGER, " +
                        MatchesEntry.COLUMN_ID_SERVER + " TEXT NOT NULL, " +
                        MatchesEntry.COLUMN_ID_COMPETITION_SERVER + " TEXT NOT NULL," +
                        MatchesEntry.COLUMN_STATE + " INTEGER NOT NULL," +
                        "UNIQUE (" + MatchesEntry.COLUMN_ID_SERVER + ") ON CONFLICT REPLACE" +
                        ")";

        String SQL_CREATE_TABLE_CLASSIFICATION =
                "CREATE TABLE " + ClassificationEntry.TABLE_NAME +
                        "(" +
                        ClassificationEntry.COLUMN_POSITION + " INTEGER NOT NULL, " +
                        ClassificationEntry.COLUMN_TEAM + " TEXT NOT NULL, " +
                        ClassificationEntry.COLUMN_POINTS + " INTEGER NOT NULL, " +
                        ClassificationEntry.COLUMN_MATCHES_PLAYED + " INTEGER, " +
                        ClassificationEntry.COLUMN_MATCHES_WON + " INTEGER, " +
                        ClassificationEntry.COLUMN_MATCHES_DRAWN + " INTEGER, " +
                        ClassificationEntry.COLUMN_MATCHES_LOST + " INTEGER, " +
                        ClassificationEntry.COLUMN_ID_COMPETITION_SERVER + " TEXT NOT NULL, " +
                        ClassificationEntry.COLUMN_SANCTIONS + " INTEGER " +
                        ")";

        String SQL_CREATE_TABLE_SPORTCOURTS =
                "CREATE TABLE " + SportCourtsEntry.TABLE_NAME +
                        "(" +
                        SportCourtsEntry.COLUMN_ID_SERVER + " INTEGER NOT NULL, " +
                        SportCourtsEntry.COLUMN_CENTER_NAME + " TEXT NOT NULL, " +
                        SportCourtsEntry.COLUMN_COURT_NAME + " TEXT, " +
                        SportCourtsEntry.COLUMN_CENTER_ADDRESS + " TEXT NOT NULL, " +
                        "UNIQUE (" + SportCourtsEntry.COLUMN_ID_SERVER + ") ON CONFLICT REPLACE" +
                        ")";

        String SQL_CREATE_TABLE_FAVORITES =
                "CREATE TABLE " + FavoritesEntry.TABLE_NAME +
                        "(" +
                        FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavoritesEntry.COLUMN_ID_COMPETITION + " INTEGER NOT NULL, " +
                        FavoritesEntry.COLUMN_ID_TEAM + " TEXT, " +
                        FavoritesEntry.COLUMN_LAST_NOTIFICATION + " INTEGER" +
                        ") ";

        final String SQL_CREATE_TABLE_SPORTS =
                "CREATE TABLE " + SportsEntry.TABLE_NAME +
                        "(" +
                        SportsEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        SportsEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                        SportsEntry.COLUMN_TAG + " TEXT NOT NULL, " +
                        SportsEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
                        SportsEntry.COLUMN_ORDER + " INTEGER " +
                        ")";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_COMPETITION);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MATCHES);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_CLASSIFICATION);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_SPORTCOURTS);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_FAVORITES);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_SPORTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoritesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SportCourtsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MatchesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ClassificationEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CompetitionsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SportsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
        mContext.sendBroadcast(new Intent(DeporteLocalDbHelper.REFRESH_BROADCAST));
    }
}

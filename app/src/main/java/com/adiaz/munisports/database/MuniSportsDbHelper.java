package com.adiaz.munisports.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.adiaz.munisports.database.MuniSportsDbContract.CompetitionsEntry;
import static com.adiaz.munisports.database.MuniSportsDbContract.MatchesEntry;
import static com.adiaz.munisports.database.MuniSportsDbContract.ClassificationEntry;
import static com.adiaz.munisports.database.MuniSportsDbContract.SportCourtsEntry;

/**
 * Created by toni on 20/04/2017.
 */
public class MuniSportsDbHelper extends SQLiteOpenHelper {


	private static final String DATABASE_NAME = "munisports.db";
	private static final int DATABASE_VERSION = 10;

	public MuniSportsDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
							CompetitionsEntry.COLUMN_LAST_UPDATE_SERVER + " INTEGER NOT NULL, " +
							CompetitionsEntry.COLUMN_LAST_UPDATE_LOCAL + " INTEGER NOT NULL, " +
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
						MatchesEntry.COLUMN_ID_SPORTCENTER + " INTEGER, " +
						MatchesEntry.COLUMN_DATE + " INTEGER, " +
						MatchesEntry.COLUMN_ID_SERVER + " TEXT NOT NULL, " +
						MatchesEntry.COLUMN_ID_COMPETITION_SERVER + " TEXT NOT NULL," +
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
						ClassificationEntry.COLUMN_ID_COMPETITION_SERVER + " TEXT NOT NULL "+
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
		sqLiteDatabase.execSQL(SQL_CREATE_TABLE_COMPETITION);
		sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MATCHES);
		sqLiteDatabase.execSQL(SQL_CREATE_TABLE_CLASSIFICATION);
		sqLiteDatabase.execSQL(SQL_CREATE_TABLE_SPORTCOURTS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SportCourtsEntry.TABLE_NAME);
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MatchesEntry.TABLE_NAME);
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ClassificationEntry.TABLE_NAME);
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CompetitionsEntry.TABLE_NAME);
		onCreate(sqLiteDatabase);
	}
}

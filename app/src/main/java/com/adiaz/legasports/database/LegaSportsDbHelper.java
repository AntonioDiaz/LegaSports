package com.adiaz.legasports.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.adiaz.legasports.database.LegaSportsDbContract.CompetitionsEntry;
import static com.adiaz.legasports.database.LegaSportsDbContract.MatchesEntry;

/**
 * Created by toni on 20/04/2017.
 */
public class LegaSportsDbHelper extends SQLiteOpenHelper {


	private static final String DATABASE_NAME = "legasports.db";
	private static final int DATABASE_VERSION = 1;

	public LegaSportsDbHelper(Context context) {	
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		String SQL_CREATE_TABLE_COMPETITION =
				"CREATE TABLE " + CompetitionsEntry.TABLE_NAME +
						"(" +
							CompetitionsEntry.COLUMN_LAST_UPDATE + " INTEGER NOT NULL, " +
							CompetitionsEntry.COLUMN_NAME + " TEXT NOT NULL, " +
							CompetitionsEntry.COLUMN_SPORT + " TEXT NOT NULL, " +
							CompetitionsEntry.COLUMN_CATEGORY + " TEXT NOT NULL, " +
							CompetitionsEntry.COLUMN_ID_SERVER + " TEXT NOT NULL, " +
							"UNIQUE (" + CompetitionsEntry.COLUMN_ID_SERVER + ") ON CONFLICT REPLACE" +
						")";
		String SQL_CREATE_TABLE_MATCHES =
				"CREATE TABLE " + MatchesEntry.TABLE_NAME +
						"(" +
						MatchesEntry.COLUMN_LAST_UPDATE + " INTEGER NOT NULL, " +
						MatchesEntry.COLUMN_TEAM_LOCAL + " TEXT NOT NULL, " +
						MatchesEntry.COLUMN_TEAM_VISITOR + " TEXT NOT NULL, " +
						MatchesEntry.COLUMN_SCORE_LOCAL + " INTEGER NOT NULL, " +
						MatchesEntry.COLUMN_SCORE_VISITOR + " INTEGER NOT NULL, " +
						MatchesEntry.COLUMN_WEEK + " INTEGER NOT NULL, " +
						MatchesEntry.COLUMN_PLACE + " TEXT NOT NULL, " +
						MatchesEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
						MatchesEntry.COLUMN_ID_SERVER + " TEXT NOT NULL, " +
						MatchesEntry.COLUMN_ID_COMPETITION_SERVER + " TEXT NOT NULL," +
						"UNIQUE (" + MatchesEntry.COLUMN_ID_SERVER + ") ON CONFLICT REPLACE" +
						")";
		sqLiteDatabase.execSQL(SQL_CREATE_TABLE_COMPETITION);
		sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MATCHES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MatchesEntry.TABLE_NAME);
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CompetitionsEntry.TABLE_NAME);
		onCreate(sqLiteDatabase);
	}
}

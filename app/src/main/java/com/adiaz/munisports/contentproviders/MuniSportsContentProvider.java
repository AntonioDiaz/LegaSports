package com.adiaz.munisports.contentproviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.adiaz.munisports.database.MuniSportsDbContract;
import com.adiaz.munisports.database.MuniSportsDbHelper;

import static com.adiaz.munisports.database.MuniSportsDbContract.ClassificationEntry;
import static com.adiaz.munisports.database.MuniSportsDbContract.CompetitionsEntry;
import static com.adiaz.munisports.database.MuniSportsDbContract.MatchesEntry;
import static com.adiaz.munisports.database.MuniSportsDbContract.SportCourtsEntry;

/**
 * Created by toni on 22/04/2017.
 */

public class MuniSportsContentProvider extends ContentProvider {

	private static final String TAG = MuniSportsContentProvider.class.getSimpleName();
	private MuniSportsDbHelper muniSportsDbHelper;

	public static final int COMPETITIONS = 100;
	public static final int COMPETITIONS_WITH_ID = 101;
	public static final int COMPETITIONS_WITH_SPORT = 102;
	public static final int COMPETITIONS_WITH_ID_LASTUPDATE = 103;
	public static final int MATCHES = 200;
	public static final int MATCHES_WITH_COMPETITION = 201;
	public static final int CLASSIFICATION = 300;
	public static final int CLASSIFICATION_WITH_COMPETITION = 301;
	public static final int SPORTCOURTS = 400;


	private static final UriMatcher sUriMatcher = buildUriMatcher();

	private static UriMatcher buildUriMatcher() {
		UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(MuniSportsDbContract.AUTHORITY, MuniSportsDbContract.PATH_COMPETITIONS, COMPETITIONS);
		uriMatcher.addURI(MuniSportsDbContract.AUTHORITY, MuniSportsDbContract.PATH_COMPETITIONS + "/#", COMPETITIONS_WITH_ID);
		uriMatcher.addURI(MuniSportsDbContract.AUTHORITY, MuniSportsDbContract.PATH_COMPETITIONS + "/*", COMPETITIONS_WITH_SPORT);
		uriMatcher.addURI(MuniSportsDbContract.AUTHORITY, MuniSportsDbContract.PATH_COMPETITIONS + "/#/#", COMPETITIONS_WITH_ID_LASTUPDATE);
		uriMatcher.addURI(MuniSportsDbContract.AUTHORITY, MuniSportsDbContract.PATH_MATCHES, MATCHES);
		uriMatcher.addURI(MuniSportsDbContract.AUTHORITY, MuniSportsDbContract.PATH_MATCHES + "/#", MATCHES_WITH_COMPETITION);
		uriMatcher.addURI(MuniSportsDbContract.AUTHORITY, MuniSportsDbContract.PATH_CLASSIFICATION, CLASSIFICATION);
		uriMatcher.addURI(MuniSportsDbContract.AUTHORITY, MuniSportsDbContract.PATH_CLASSIFICATION + "/#", CLASSIFICATION_WITH_COMPETITION);
		uriMatcher.addURI(MuniSportsDbContract.AUTHORITY, MuniSportsDbContract.PATH_SPORT_COURTS, SPORTCOURTS);
		return uriMatcher;
	}

	@Override
	public boolean onCreate() {
		muniSportsDbHelper = new MuniSportsDbHelper(getContext());
		return true;
	}

	@Override
	public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
		SQLiteDatabase db = muniSportsDbHelper.getWritableDatabase();
		int rowsInserted = 0;
		switch (sUriMatcher.match(uri)) {
			case COMPETITIONS:
				try {
					db.beginTransaction();
					for (ContentValues contentValues : values) {
						long id = db.insert(CompetitionsEntry.TABLE_NAME, null, contentValues);
						if (id!=-1) {
							rowsInserted++;
						}
					}
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
				break;
			case MATCHES:
				try {
					db.beginTransaction();
					for (ContentValues contentValues : values) {
						long id = db.insert(MatchesEntry.TABLE_NAME, null, contentValues);
						if (id!=-1) {
							rowsInserted++;
						}
					}
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
				break;
			case CLASSIFICATION:
				try {
					db.beginTransaction();
					for (ContentValues contentValues : values) {
						long id = db.insert(ClassificationEntry.TABLE_NAME, null, contentValues);
						if (id!=-1) {
							rowsInserted++;
						}
					}
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
				break;
			case SPORTCOURTS:
				try {
					db.beginTransaction();
					for (ContentValues contentValues : values) {
						long id = db.insert(SportCourtsEntry.TABLE_NAME, null, contentValues);
						if (id!=-1) {
							rowsInserted++;
						}
					}
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
				break;
			default:
				return super.bulkInsert(uri, values);
		}
		if (rowsInserted>0) {
			Log.d(TAG, "bulkInsert: deletedItems: " + rowsInserted);
			Log.d(TAG, "bulkInsert: uri: " + uri);
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return rowsInserted;
	}

	@Nullable
	@Override
	public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
		throw new RuntimeException("We are not implementing insert in MuniSports. Use bulkInsert instead");
	}

	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, @Nullable String[] columns, @Nullable String selection,
						@Nullable String[] selectionArgs, @Nullable String sortOrder) {
		Cursor cursorReturned;
		SQLiteDatabase db = muniSportsDbHelper.getReadableDatabase();
		switch (sUriMatcher.match(uri)) {
			case COMPETITIONS:
				cursorReturned = db.query(CompetitionsEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, sortOrder);
				break;
			case COMPETITIONS_WITH_ID:
				String competitionServerID = uri.getPathSegments().get(1);
				selection = CompetitionsEntry.COLUMN_ID_SERVER + "=?";
				selectionArgs = new String[]{competitionServerID};
				cursorReturned = db.query(CompetitionsEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, sortOrder);
				break;
			case COMPETITIONS_WITH_SPORT:
				String sportName = uri.getPathSegments().get(1);
				selection = CompetitionsEntry.COLUMN_SPORT + "=?";
				selectionArgs = new String[]{sportName};
				cursorReturned = db.query(CompetitionsEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, sortOrder);
				break;
			case MATCHES:
				cursorReturned = db.query(MatchesEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, sortOrder);
				break;
			case MATCHES_WITH_COMPETITION:
				String competitionServerId = uri.getPathSegments().get(1);
				selection = MatchesEntry.COLUMN_ID_COMPETITION_SERVER + "=?";
				selectionArgs = new String[]{competitionServerId};
				cursorReturned = db.query(MatchesEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, sortOrder);
				break;
			case CLASSIFICATION:
				cursorReturned = db.query(ClassificationEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, sortOrder);
				break;
			case CLASSIFICATION_WITH_COMPETITION:
				String competitionServerIdClassification = uri.getPathSegments().get(1);
				selection = ClassificationEntry.COLUMN_ID_COMPETITION_SERVER + "=?";
				selectionArgs = new String[]{competitionServerIdClassification};
				cursorReturned = db.query(ClassificationEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, sortOrder);
				break;
			case SPORTCOURTS:
				cursorReturned = db.query(SportCourtsEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, sortOrder);
				break;
			default:
				throw new UnsupportedOperationException("error " + uri);

		}
		if (cursorReturned!=null) {
			cursorReturned.setNotificationUri(getContext().getContentResolver(), uri);
		}
		return cursorReturned;
	}

	@Nullable
	@Override
	public String getType(@NonNull Uri uri) {
		return null;
	}

	@Override
	public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
		int deletedItems;
		SQLiteDatabase db = muniSportsDbHelper.getReadableDatabase();
		switch (sUriMatcher.match(uri)) {
			case COMPETITIONS:
				deletedItems = db.delete(CompetitionsEntry.TABLE_NAME, selection, selectionArgs);
				break;
			case MATCHES:
				deletedItems = db.delete(MatchesEntry.TABLE_NAME, selection, selectionArgs);
				break;
			case CLASSIFICATION:
				deletedItems = db.delete(ClassificationEntry.TABLE_NAME, selection, selectionArgs);
				break;
			case CLASSIFICATION_WITH_COMPETITION:
				selection = MatchesEntry.COLUMN_ID_COMPETITION_SERVER + "=?";
				selectionArgs = new String[]{uri.getPathSegments().get(1)};
				deletedItems = db.delete(ClassificationEntry.TABLE_NAME, selection, selectionArgs);
				break;
			case MATCHES_WITH_COMPETITION:
				selection = MatchesEntry.COLUMN_ID_COMPETITION_SERVER + "=?";
				selectionArgs = new String[]{uri.getPathSegments().get(1)};
				deletedItems = db.delete(MatchesEntry.TABLE_NAME, selection, selectionArgs);
				break;
			case SPORTCOURTS:
				deletedItems = db.delete(SportCourtsEntry.TABLE_NAME, selection, selectionArgs);
				break;
			default:
				throw new UnsupportedOperationException("error " + uri);
		}
		if (deletedItems>0) {
			Log.d(TAG, "delete: deletedItems: " + deletedItems);
			Log.d(TAG, "delete: uri: " + uri);
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return deletedItems;
	}

	@Override
	public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
		int updateRecors = -1;
		SQLiteDatabase db = muniSportsDbHelper.getWritableDatabase();
		switch (sUriMatcher.match(uri)) {
			case COMPETITIONS:
				updateRecors = db.update(CompetitionsEntry.TABLE_NAME, contentValues, selection, selectionArgs);
				break;
			default:
				throw new UnsupportedOperationException("error " + uri);

		}
		getContext().getContentResolver().notifyChange(uri, null);
		return updateRecors;
	}
}

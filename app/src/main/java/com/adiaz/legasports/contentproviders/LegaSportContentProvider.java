package com.adiaz.legasports.contentproviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.adiaz.legasports.database.LegaSportsDbContract;
import com.adiaz.legasports.database.LegaSportsDbHelper;

import static com.adiaz.legasports.database.LegaSportsDbContract.CompetitionsEntry;
import static com.adiaz.legasports.database.LegaSportsDbContract.MatchesEntry;
import static com.adiaz.legasports.database.LegaSportsDbContract.ClassificationEntry;

/**
 * Created by toni on 22/04/2017.
 */

public class LegaSportContentProvider extends ContentProvider {

	private LegaSportsDbHelper legaSportsDbHelper;

	public static final int COMPETITIONS = 100;
	public static final int COMPETITIONS_WITH_SPORT = 101;
	public static final int MATCHES = 200;
	public static final int MATCHES_WITH_COMPETITION = 201;
	public static final int CLASSIFICATION = 300;
	public static final int CLASSIFICATION_WITH_COMPETITION = 301;


	private static final UriMatcher sUriMatcher = buildUriMatcher();

	private static UriMatcher buildUriMatcher() {
		UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(LegaSportsDbContract.AUTHORITY, LegaSportsDbContract.PATH_COMPETITIONS, COMPETITIONS);
		uriMatcher.addURI(LegaSportsDbContract.AUTHORITY, LegaSportsDbContract.PATH_COMPETITIONS + "/*", COMPETITIONS_WITH_SPORT);
		uriMatcher.addURI(LegaSportsDbContract.AUTHORITY, LegaSportsDbContract.PATH_MATCHES, MATCHES);
		uriMatcher.addURI(LegaSportsDbContract.AUTHORITY, LegaSportsDbContract.PATH_MATCHES + "/*", MATCHES_WITH_COMPETITION);
		uriMatcher.addURI(LegaSportsDbContract.AUTHORITY, LegaSportsDbContract.PATH_CLASSIFICATION, CLASSIFICATION);
		uriMatcher.addURI(LegaSportsDbContract.AUTHORITY, LegaSportsDbContract.PATH_CLASSIFICATION + "/*", CLASSIFICATION_WITH_COMPETITION);
		return uriMatcher;
	}

	@Override
	public boolean onCreate() {
		legaSportsDbHelper = new LegaSportsDbHelper(getContext());
		return true;
	}

	@Override
	public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
		SQLiteDatabase db = legaSportsDbHelper.getWritableDatabase();
		int rowsInserted = 0;
		switch (sUriMatcher.match(uri)) {
			case COMPETITIONS:
				try {
					db.beginTransaction();
					db.delete(CompetitionsEntry.TABLE_NAME, null, null);
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
					db.delete(MatchesEntry.TABLE_NAME, null, null);
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
					db.delete(ClassificationEntry.TABLE_NAME, null, null);
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
			default:
				return super.bulkInsert(uri, values);
		}
		if (rowsInserted>0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return rowsInserted;
	}

	@Nullable
	@Override
	public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
		throw new RuntimeException("We are not implementing insert in LegaSports. Use bulkInsert instead");
	}

	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, @Nullable String[] columns, @Nullable String selection,
						@Nullable String[] selectionArgs, @Nullable String sortOrder) {
		Cursor cursorReturned;
		SQLiteDatabase db = legaSportsDbHelper.getReadableDatabase();
		switch (sUriMatcher.match(uri)) {
			case COMPETITIONS:
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
	public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
		return 0;
	}

	@Override
	public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
		throw new RuntimeException("We are not implementing update in LegaSports");
	}
}

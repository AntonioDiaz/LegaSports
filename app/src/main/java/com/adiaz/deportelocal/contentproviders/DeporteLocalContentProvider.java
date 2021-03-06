package com.adiaz.deportelocal.contentproviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.adiaz.deportelocal.database.DeporteLocalDbContract;
import com.adiaz.deportelocal.database.DeporteLocalDbHelper;

import static com.adiaz.deportelocal.database.DeporteLocalDbContract.ClassificationEntry;
import static com.adiaz.deportelocal.database.DeporteLocalDbContract.CompetitionsEntry;
import static com.adiaz.deportelocal.database.DeporteLocalDbContract.FavoritesEntry;
import static com.adiaz.deportelocal.database.DeporteLocalDbContract.MatchesEntry;
import static com.adiaz.deportelocal.database.DeporteLocalDbContract.SportCourtsEntry;
import static com.adiaz.deportelocal.database.DeporteLocalDbContract.SportsEntry;

/**
 * Created by toni on 22/04/2017.
 */

public class DeporteLocalContentProvider extends ContentProvider {

	private static final String TAG = DeporteLocalContentProvider.class.getSimpleName();
	private DeporteLocalDbHelper deporteLocalDbHelper;

	private static final int COMPETITIONS = 100;
	private static final int COMPETITIONS_WITH_ID = 101;
	private static final int COMPETITIONS_WITH_SPORT = 102;
	private static final int COMPETITIONS_WITH_ID_LASTUPDATE = 103;
	private static final int MATCHES = 200;
	private static final int MATCHES_WITH_COMPETITION = 201;
	private static final int CLASSIFICATION = 300;
	private static final int CLASSIFICATION_WITH_COMPETITION = 301;
	private static final int SPORTCOURTS = 400;
	private static final int FAVORITES = 500;
	private static final int FAVORITES_WITH_ID = 501;
	private static final int SPORTS = 600;


	private static final UriMatcher sUriMatcher = buildUriMatcher();

	private static UriMatcher buildUriMatcher() {
		UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(DeporteLocalDbContract.AUTHORITY, DeporteLocalDbContract.PATH_COMPETITIONS, COMPETITIONS);
		uriMatcher.addURI(DeporteLocalDbContract.AUTHORITY, DeporteLocalDbContract.PATH_COMPETITIONS + "/#", COMPETITIONS_WITH_ID);
		uriMatcher.addURI(DeporteLocalDbContract.AUTHORITY, DeporteLocalDbContract.PATH_COMPETITIONS + "/*", COMPETITIONS_WITH_SPORT);
		uriMatcher.addURI(DeporteLocalDbContract.AUTHORITY, DeporteLocalDbContract.PATH_COMPETITIONS + "/#/#", COMPETITIONS_WITH_ID_LASTUPDATE);
		uriMatcher.addURI(DeporteLocalDbContract.AUTHORITY, DeporteLocalDbContract.PATH_MATCHES, MATCHES);
		uriMatcher.addURI(DeporteLocalDbContract.AUTHORITY, DeporteLocalDbContract.PATH_MATCHES + "/#", MATCHES_WITH_COMPETITION);
		uriMatcher.addURI(DeporteLocalDbContract.AUTHORITY, DeporteLocalDbContract.PATH_CLASSIFICATION, CLASSIFICATION);
		uriMatcher.addURI(DeporteLocalDbContract.AUTHORITY, DeporteLocalDbContract.PATH_CLASSIFICATION + "/#", CLASSIFICATION_WITH_COMPETITION);
		uriMatcher.addURI(DeporteLocalDbContract.AUTHORITY, DeporteLocalDbContract.PATH_SPORT_COURTS, SPORTCOURTS);
		uriMatcher.addURI(DeporteLocalDbContract.AUTHORITY, DeporteLocalDbContract.PATH_FAVORITES, FAVORITES);
		uriMatcher.addURI(DeporteLocalDbContract.AUTHORITY, DeporteLocalDbContract.PATH_FAVORITES + "/#", FAVORITES_WITH_ID);
        uriMatcher.addURI(DeporteLocalDbContract.AUTHORITY, DeporteLocalDbContract.PATH_SPORTS, SPORTS);
        return uriMatcher;
	}

	@Override
	public boolean onCreate() {
		deporteLocalDbHelper = new DeporteLocalDbHelper(getContext());
		return true;
	}

	@Override
	public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
		SQLiteDatabase db = deporteLocalDbHelper.getWritableDatabase();
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
            case SPORTS:
                try {
                    db.beginTransaction();
                    for (ContentValues contentValues : values) {
                        long id = db.insert(SportsEntry.TABLE_NAME, null, contentValues);
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
		Uri uriReturned = null;
		SQLiteDatabase db = deporteLocalDbHelper.getWritableDatabase();
		switch (sUriMatcher.match(uri)) {
			case FAVORITES:
				long idInserted = db.insert(FavoritesEntry.TABLE_NAME, null, contentValues);
				if (idInserted>0) {
					uriReturned = FavoritesEntry.buildFavoritesUri(idInserted);
				} else {
					throw new SQLException("error no insert for " + uri);
				}
				break;
			default:
				throw new UnsupportedOperationException("error " + uri);
		}
		getContext().getContentResolver().notifyChange(uriReturned, null);
		return uriReturned;
	}

	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, @Nullable String[] columns, @Nullable String selection,
						@Nullable String[] selectionArgs, @Nullable String sortOrder) {
		Cursor cursorReturned;
		SQLiteDatabase db = deporteLocalDbHelper.getReadableDatabase();
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
			case FAVORITES:
				cursorReturned = db.query(FavoritesEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, sortOrder);
				break;
			case FAVORITES_WITH_ID:
				String idFavorite = uri.getPathSegments().get(1);
				selection = FavoritesEntry._ID + "=?";
				selectionArgs = new String[]{idFavorite};
				cursorReturned = db.query(FavoritesEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, sortOrder);
				break;
            case SPORTS:
                cursorReturned = db.query(SportsEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, sortOrder);
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
		SQLiteDatabase db = deporteLocalDbHelper.getReadableDatabase();
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
			case FAVORITES:
				deletedItems = db.delete(FavoritesEntry.TABLE_NAME, selection, selectionArgs);
				break;
			case FAVORITES_WITH_ID:
				String idFavorite = uri.getPathSegments().get(1);
				selection = FavoritesEntry._ID + "=?";
				selectionArgs = new String[]{idFavorite};
				deletedItems = db.delete(FavoritesEntry.TABLE_NAME, selection, selectionArgs);
				break;
			case SPORTS:
				deletedItems = db.delete(SportsEntry.TABLE_NAME, selection, selectionArgs);
				break;
			default:
				throw new UnsupportedOperationException("error " + uri);
		}
		if (deletedItems>0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return deletedItems;
	}

	@Override
	public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
		int updateRecors = -1;
		SQLiteDatabase db = deporteLocalDbHelper.getWritableDatabase();
		switch (sUriMatcher.match(uri)) {
			case COMPETITIONS:
				updateRecors = db.update(CompetitionsEntry.TABLE_NAME, contentValues, selection, selectionArgs);
				break;
			case FAVORITES_WITH_ID:
				String idFavorite = uri.getPathSegments().get(1);
				selection = FavoritesEntry._ID + "=?";
				selectionArgs = new String[]{idFavorite};
				updateRecors = db.update(FavoritesEntry.TABLE_NAME, contentValues, selection, selectionArgs);
				break;
			default:
				throw new UnsupportedOperationException("error " + uri);

		}
		getContext().getContentResolver().notifyChange(uri, null);
		return updateRecors;
	}
}

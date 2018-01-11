package com.adiaz.localsports.database.daos;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.adiaz.localsports.database.LocalSportsDbContract;
import com.adiaz.localsports.entities.Competition;
import static com.adiaz.localsports.database.LocalSportsDbContract.CompetitionsEntry;

/**
 * Created by adiaz on 11/1/18.
 */

public class CompetitionsDAO {

    public static final Competition findCompetition(Context context, Long idCompetition) {
        ContentValues contentValues = new ContentValues();
        String selection = LocalSportsDbContract.CompetitionsEntry.COLUMN_ID_SERVER + "=?";
        String[] selectionArgs = new String[]{idCompetition.toString()};
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(CompetitionsEntry.CONTENT_URI, CompetitionsEntry.PROJECTION, selection, selectionArgs, null);
        Competition competition = null;
        if(cursor!=null && cursor.getCount()==1) {
            cursor.moveToFirst();
            competition = CompetitionsEntry.initEntity(cursor);
        }
        cursor.close();
        return competition;
    }

    public static final void markCompetitionsAsDirty(Context context, Long idCompetition, boolean isDirty) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocalSportsDbContract.CompetitionsEntry.COLUMN_IS_DIRTY, isDirty?1:0);
        String selection = LocalSportsDbContract.CompetitionsEntry.COLUMN_ID_SERVER + "=?";
        String[] selectionArgs = new String[]{idCompetition.toString()};
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.update(LocalSportsDbContract.CompetitionsEntry.CONTENT_URI, contentValues, selection, selectionArgs);
    }

}

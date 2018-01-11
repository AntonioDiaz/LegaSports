package com.adiaz.localsports.sync.retrofit.callbacks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.adiaz.localsports.sync.retrofit.entities.sport.SportsRestEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.adiaz.localsports.database.LocalSportsDbContract.SportsEntry;

/**
 * Created by adiaz on 10/1/18.
 */

public class SportsCallback implements Callback<List<SportsRestEntity>> {

    private static final String TAG = SportsCallback.class.getSimpleName();
    private Context mContext;
    private SportsLoadedCallback mSportsLoadedCallback;

    public SportsCallback(Context mContext, SportsLoadedCallback sportsLoadedCallback) {
        this.mContext = mContext;
        this.mSportsLoadedCallback = sportsLoadedCallback;
    }

    @Override
    public void onResponse(Call<List<SportsRestEntity>> call, Response<List<SportsRestEntity>> response) {
        ContentValues[] sports = new ContentValues[response.body().size()];
        for (int i = 0; i < response.body().size(); i++) {
            SportsRestEntity sport = response.body().get(i);
            ContentValues values = new ContentValues();
            values.put(SportsEntry.COLUMN_ID, sport.getId());
            values.put(SportsEntry.COLUMN_NAME, sport.getName());
            values.put(SportsEntry.COLUMN_TAG, sport.getTag());
            values.put(SportsEntry.COLUMN_IMAGE, sport.getImage());
            values.put(SportsEntry.COLUMN_ORDER, sport.getOrder());
            sports[i] = values;
        }
        ContentResolver contentResolver = mContext.getContentResolver();
        contentResolver.delete(SportsEntry.CONTENT_URI, null, null);
        contentResolver.bulkInsert(SportsEntry.CONTENT_URI, sports);
        if (mSportsLoadedCallback!=null) {
            mSportsLoadedCallback.finishLoadSports();
        }
    }

    @Override
    public void onFailure(Call<List<SportsRestEntity>> call, Throwable t) {
        Log.e(TAG, "onFailure: " + t.getMessage() , t);
    }

    public interface SportsLoadedCallback {
        void finishLoadSports();
    }
}

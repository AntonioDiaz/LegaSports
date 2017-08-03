package com.adiaz.legasports.sync;

import android.util.Log;

import com.adiaz.legasports.sync.retrofit.entities.Town;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by toni on 02/08/2017.
 */

public class TownsAvailableCallback implements Callback<List<Town>> {

	private TownsLoadedCallback townsLoadedCallback;

	public TownsAvailableCallback(TownsLoadedCallback townsLoadedCallback) {
		this.townsLoadedCallback = townsLoadedCallback;
	}

	private static final String TAG = TownsAvailableCallback.class.getSimpleName();

	@Override
	public void onResponse(Call<List<Town>> call, Response<List<Town>> response) {
		townsLoadedCallback.updateActivity(response.body());
		Log.d(TAG, "onResponse: " + response.body());
	}

	@Override
	public void onFailure(Call<List<Town>> call, Throwable t) {
		Log.d(TAG, "onFailure: " + t.getMessage());
	}

	public interface TownsLoadedCallback {
		void updateActivity(List<Town> townList);
	}
}

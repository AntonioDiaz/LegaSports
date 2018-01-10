package com.adiaz.localsports.sync.retrofit.callbacks;

import android.util.Log;

import com.adiaz.localsports.sync.retrofit.entities.town.TownRestEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by toni on 02/08/2017.
 */

public class TownsAvailableCallback implements Callback<List<TownRestEntity>> {

	private TownsLoadedCallback townsLoadedCallback;

	public TownsAvailableCallback(TownsLoadedCallback townsLoadedCallback) {
		this.townsLoadedCallback = townsLoadedCallback;
	}

	private static final String TAG = TownsAvailableCallback.class.getSimpleName();

	@Override
	public void onResponse(Call<List<TownRestEntity>> call, Response<List<TownRestEntity>> response) {
		townsLoadedCallback.updateActivity(response.body());
	}

	@Override
	public void onFailure(Call<List<TownRestEntity>> call, Throwable t) {
		Log.d(TAG, "onFailure: " + t.getMessage());
	}

	public interface TownsLoadedCallback {
		void updateActivity(List<TownRestEntity> townRestEntityList);
	}
}

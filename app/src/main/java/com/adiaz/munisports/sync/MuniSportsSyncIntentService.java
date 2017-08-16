package com.adiaz.munisports.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by toni on 21/04/2017.
 */

public class MuniSportsSyncIntentService extends IntentService {

	public MuniSportsSyncIntentService() {
		super("MuniSportsSyncIntentService");
	}

	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		MuniSportsSyncTask.syncCompetitions(this);
	}
}

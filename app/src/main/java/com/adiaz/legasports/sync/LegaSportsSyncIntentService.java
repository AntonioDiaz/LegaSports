package com.adiaz.legasports.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by toni on 21/04/2017.
 */

public class LegaSportsSyncIntentService extends IntentService {

	public LegaSportsSyncIntentService() {
		super("LegaSportsSyncIntentService");
	}

	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		LegaSportsSyncTask.syncCompetitions(this);
	}
}

package com.adiaz.deportelocal.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


/**
 * Created by toni on 17/09/2017.
 */

public class PreferencesUtils {

	public static String queryUUID(Context context) {
		String clientId;
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		if (sharedPreferences.contains(DeporteLocalConstants.KEY_UUID)) {
			clientId = sharedPreferences.getString(DeporteLocalConstants.KEY_UUID, "");
		} else {
			clientId = UUID.randomUUID().toString();
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(DeporteLocalConstants.KEY_UUID, clientId);
			editor.commit();
		}
		return clientId;
	}

	public static String queryPreferenceFCMTopic(Context context) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		return sharedPreferences.getString(DeporteLocalConstants.KEY_TOWN_TOPIC, null);
	}

	public static String queryPreferenceTown(Context context) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		return sharedPreferences.getString(DeporteLocalConstants.KEY_TOWN_NAME, null);
	}

	public static Long queryPreferenceTownId(Context context) {
		Long idTown = null;
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		if (sharedPreferences.contains(DeporteLocalConstants.KEY_TOWN_ID)) {
			idTown = sharedPreferences.getLong(DeporteLocalConstants.KEY_TOWN_ID, 0L);
		}
		return idTown;
	}
}

package com.adiaz.munisports.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.adiaz.munisports.R;
import com.adiaz.munisports.entities.Court;

import java.util.HashMap;
import java.util.Map;

import static android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.adiaz.munisports.database.MuniSportsDbContract.SportCourtsEntry;
/* Created by toni on 28/03/2017. */

public class MuniSportsUtils {

	private static final String TAG = MuniSportsUtils.class.getSimpleName();


	public static String getStringResourceByName(Context context, String aString) {
		String packageName = context.getPackageName();
		String strResource = context.getString(R.string.NOT_FOUND);
		try {
			int resId = context.getResources().getIdentifier(aString, "string", packageName);
			strResource = context.getString(resId);
		} catch (Exception e) {
			Log.e(TAG, "getStringResourceByName: " + e.getMessage(), e);
		}
		return strResource;
	}

	public static void showNoInternetAlert(Context context, View view) {
		String strError = context.getString(R.string.internet_required);
		final Snackbar snackbar = Snackbar.make(view, strError, Snackbar.LENGTH_INDEFINITE);
		snackbar.show();
	}

	public static Map<Long, Court> initCourts(Context context) {
		Uri uriCourts = SportCourtsEntry.CONTENT_URI;
		Cursor cursorCourts = context.getContentResolver().query(uriCourts, SportCourtsEntry.PROJECTION, null, null, null);
		Map<Long, Court> mapCourts = new HashMap<>();
		try {
			while (cursorCourts.moveToNext()) {
				Court court = new Court();
				Long id = cursorCourts.getLong(SportCourtsEntry.INDEX_ID_SERVER);
				String centerName = cursorCourts.getString(SportCourtsEntry.INDEX_CENTER_NAME);
				String courtName = cursorCourts.getString(SportCourtsEntry.INDEX_COURT_NAME);
				court.setCenterName(centerName);
				court.setCourtName(courtName);
				court.setCourtFullName(centerName);
				if (!TextUtils.isEmpty(courtName)) {
					court.setCourtFullName(centerName + " - " + courtName);
				}
				court.setCenterAddress(cursorCourts.getString(SportCourtsEntry.INDEX_CENTER_ADDRESS));
				mapCourts.put(id, court);
			}
		} finally {
			cursorCourts.close();
		}
		return mapCourts;
	}

	public static boolean isShowNotification(Context context) {
		SharedPreferences preferences = getDefaultSharedPreferences(context);
		String notificationsKey = context.getString(R.string.pref_notifications_key);
		return  preferences.getBoolean(notificationsKey, false);
	}
}



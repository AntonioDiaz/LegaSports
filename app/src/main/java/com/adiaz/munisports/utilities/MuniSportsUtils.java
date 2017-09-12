package com.adiaz.munisports.utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.adiaz.munisports.R;
import com.adiaz.munisports.database.MuniSportsDbContract;
import com.adiaz.munisports.entities.Court;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.adiaz.munisports.database.MuniSportsDbContract.SportCourtsEntry;
/* Created by toni on 28/03/2017. */

public class MuniSportsUtils {

	private static final String TAG = MuniSportsUtils.class.getSimpleName();


	public static String getStringResourceByName(Context context, String aString) {
		String packageName = context.getPackageName();
		String strResource = aString;
		try {
			int resId = context.getResources().getIdentifier(aString, "string", packageName);
			strResource = context.getString(resId);
		} catch (Exception e) {
			Log.d(TAG, "resource not found at getStringResourceByName: " + aString);
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

	public static List<String> sportsList(Context context){
		List<String> sportsList = new ArrayList<>();
		sportsList.add("");
		sportsList.add(MuniSportsUtils.getStringResourceByName(context, context.getString(R.string.basketball_tag)));
		sportsList.add(MuniSportsUtils.getStringResourceByName(context, context.getString(R.string.football_11_tag)));
		sportsList.add(MuniSportsUtils.getStringResourceByName(context, context.getString(R.string.football_7_tag)));
		sportsList.add(MuniSportsUtils.getStringResourceByName(context, context.getString(R.string.football_sala_tag)));
		sportsList.add(MuniSportsUtils.getStringResourceByName(context, context.getString(R.string.volleyball_tag)));
		sportsList.add(MuniSportsUtils.getStringResourceByName(context, context.getString(R.string.handball_tag)));
		sportsList.add(MuniSportsUtils.getStringResourceByName(context, context.getString(R.string.unihockey_tag)));
		return sportsList;
	}

	public static List<String> categoriesList(Context context) {
		List<String> categories = new ArrayList<>();
		categories.add("");
		ContentResolver cr = context.getContentResolver();
		Uri uri = MuniSportsDbContract.CompetitionsEntry.CONTENT_URI;
		Cursor cursor = cr.query(uri, MuniSportsDbContract.CompetitionsEntry.PROJECTION, null, null, null);
		while (cursor.moveToNext()) {
			String categoryName = cursor.getString(MuniSportsDbContract.CompetitionsEntry.INDEX_CATEGORY);
			if (!categories.contains(categoryName)) {
				categories.add(categoryName);
			}
		}
		cursor.close();
		return categories;
	}

	public static List<String> competitionList(Context context) {
		List<String> competitions = new ArrayList<>();
		competitions.add("");
		ContentResolver cr = context.getContentResolver();
		Uri uri = MuniSportsDbContract.CompetitionsEntry.CONTENT_URI;
		Cursor cursor = cr.query(uri, MuniSportsDbContract.CompetitionsEntry.PROJECTION, null, null, null);
		while (cursor.moveToNext()) {
			String competitionName = cursor.getString(MuniSportsDbContract.CompetitionsEntry.INDEX_NAME);
			if (!competitions.contains(competitionName)) {
				competitions.add(competitionName);
			}
		}
		cursor.close();
		return competitions;
	}
}



package com.adiaz.munisports.utilities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.adiaz.munisports.entities.Court;

import java.util.HashMap;
import java.util.Map;

import static com.adiaz.munisports.database.MuniSportsDbContract.SportCourtsEntry;
/**
 * Created by toni on 13/09/2017.
 */

public class MuniSportsCourts {

	private static Map<Long, Court> courts;

	public static void refreshCourts(Context context) {
		courts = new HashMap<>();
		Uri uriCourts = SportCourtsEntry.CONTENT_URI;
		Cursor cursorCourts = context.getContentResolver().query(uriCourts, SportCourtsEntry.PROJECTION, null, null, null);
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
				courts.put(id, court);
			}
		} finally {
			cursorCourts.close();
		}
	}

	public static Court obteinTownCourt (Context context, Long idCourt) {
		if (courts==null) {
			refreshCourts(context);
		}
		return courts.get(idCourt);
	}


}

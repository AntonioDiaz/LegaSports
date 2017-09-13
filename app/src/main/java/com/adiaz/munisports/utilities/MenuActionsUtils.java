package com.adiaz.munisports.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ShareCompat;

import com.adiaz.munisports.R;
import com.adiaz.munisports.entities.Match;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by toni on 13/09/2017.
 */

public class MenuActionsUtils {

	/**
	 * Open Sport center address in the device map.
	 * @param context
	 * @param courtAddress
	 */
	public static void showMatchLocation(Context context, String courtAddress) {
		Uri addressUri = Uri.parse("geo:0,0?q=" + courtAddress);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(addressUri);
		if (intent.resolveActivity(context.getPackageManager()) != null) {
			context.startActivity(intent);
		}
	}

	/**
	 * Share text with match details.
	 * @param activity
	 * @param localTeam
	 * @param visitorTeam
	 * @param matchDate
	 * @param sportCenter
	 */
	public static void shareMatchDetails(Activity activity, String localTeam, String visitorTeam, Date matchDate, String sportCenter) {
		String mimeType = "text/plain";
		String title = activity.getString(R.string.calendar_title, localTeam, visitorTeam);
		DateFormat df = new SimpleDateFormat(MuniSportsConstants.DATE_FORMAT);
		String strDate = df.format(matchDate);
		String subject = activity.getString(R.string.share_description, localTeam, visitorTeam, strDate, sportCenter);
		ShareCompat.IntentBuilder
				.from(activity)
				.setChooserTitle(title)
				.setType(mimeType)
				.setText(subject)
				.startChooser();
	}

	/**
	 * Add match event to the calendar.
	 * @param context
	 * @param local
	 * @param visitor
	 * @param date
	 * @param place
	 */
	public static void addMatchEvent(Context context, String local, String visitor, Date date, String place) {
		Calendar beginTime = Calendar.getInstance();
		beginTime.setTime(date);
		Calendar endTime = Calendar.getInstance();
		endTime.setTime(date);
		endTime.add(Calendar.HOUR, 2);
		String titleStr = context.getString(R.string.calendar_title, local, visitor);
		String descStr = context.getString(R.string.calendar_description, local, visitor);
		Intent intent = new Intent(Intent.ACTION_INSERT)
				.setData(CalendarContract.Events.CONTENT_URI)
				.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
				.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
				.putExtra(CalendarContract.Events.TITLE, titleStr)
				.putExtra(CalendarContract.Events.DESCRIPTION, descStr)
				.putExtra(CalendarContract.Events.EVENT_LOCATION, place);
		context.startActivity(intent);
	}

	public static void addMatchEvent(FragmentActivity activity, Match match) {
		addMatchEvent(activity, match.getTeamLocal(), match.getTeamVisitor(), match.getDate(), match.getPlaceName());
	}

	public static void shareMatchDetails(FragmentActivity activity, Match match) {
		shareMatchDetails(activity, match.getTeamLocal(), match.getTeamVisitor(), match.getDate(), match.getPlaceName());
	}
}

package com.adiaz.munisports.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ShareCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.adiaz.munisports.R;
import com.adiaz.munisports.entities.CompetitionEntity;
import com.adiaz.munisports.entities.Court;
import com.adiaz.munisports.entities.Match;

import java.util.Calendar;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by toni on 13/09/2017.
 */

public class MenuActionsUtils {

	/**
	 * Open Sport center address in the device map.
	 * @param context
	 * @param match
	 */
	public static void showMatchLocation(Context context, Match match) {
		Court court = MuniSportsCourts.obteinTownCourt(context, match.idSportCenter());
		if (court==null) {
			String noDateStr = context.getString(R.string.no_match_address);
			Toast.makeText(context, noDateStr, Toast.LENGTH_SHORT).show();
		} else {
			if (!TextUtils.isEmpty(court.getCenterAddress())) {
				Uri addressUri = Uri.parse("geo:0,0?q=" + court.getCenterAddress());
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(addressUri);
				if (intent.resolveActivity(context.getPackageManager()) != null) {
					context.startActivity(intent);
				}
			}
		}
	}

	public static void shareMatchDetails(Activity activity, Match match, CompetitionEntity competition) {
		String localTeam = match.teamLocal();
		String visitorTeam = match.teamVisitor();
		String sportCenter = MatchUtilities.obtainCenterName(activity, match);
		String competitionName = competition.name();
		String sport = MuniSportsUtils.getStringResourceByName(activity, competition.sportName());
		String category = MuniSportsUtils.getStringResourceByName(activity, competition.categoryName());
		String dateStr = MatchUtilities.obtainDateStr(activity, match);
		SharedPreferences preferences = getDefaultSharedPreferences(activity);
		String town = preferences.getString(MuniSportsConstants.KEY_TOWN_NAME, null);
		String mimeType = "text/plain";
		String titleStr = activity.getString(R.string.calendar_title, competition.name(), String.valueOf(match.week()), localTeam, visitorTeam);
		String subject = activity.getString(R.string.match_description,
				competitionName, sport, category, String.valueOf(match.week()), localTeam, visitorTeam, dateStr, sportCenter, town);
		ShareCompat.IntentBuilder
				.from(activity)
				.setChooserTitle(titleStr)
				.setSubject(titleStr)
				.setType(mimeType)
				.setText(subject)
				.startChooser();
	}

	public static void addMatchEvent(Context context, Match match, CompetitionEntity competition) {
		if (match.date()==null || match.date().getTime()==0) {
			String noDateStr = context.getString(R.string.no_match_event);
			Toast.makeText(context, noDateStr, Toast.LENGTH_SHORT).show();
		} else {
			String localTeam = match.teamLocal();
			String visitorTeam = match.teamVisitor();
			String sportCenter = MatchUtilities.obtainCenterName(context, match);
			String sport = MuniSportsUtils.getStringResourceByName(context, competition.sportName());
			String category = MuniSportsUtils.getStringResourceByName(context, competition.categoryName());
			SharedPreferences preferences = getDefaultSharedPreferences(context);
			String town = preferences.getString(MuniSportsConstants.KEY_TOWN_NAME, null);
			Calendar beginTime = Calendar.getInstance();
			beginTime.setTime(match.date());
			Calendar endTime = Calendar.getInstance();
			endTime.setTime(match.date());
			endTime.add(Calendar.HOUR, 2);
			String strDate = MatchUtilities.obtainDateStr(context, match);
			String titleStr = context.getString(R.string.calendar_title, competition.name(), String.valueOf(match.week()), localTeam, visitorTeam);
			String descMatch = context.getString(R.string.match_description,
					competition.name(), sport, category, String.valueOf(match.week()), localTeam, visitorTeam, strDate, sportCenter, town);
			Intent intent = new Intent(Intent.ACTION_INSERT)
					.setData(CalendarContract.Events.CONTENT_URI)
					.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
					.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
					.putExtra(CalendarContract.Events.TITLE, titleStr)
					.putExtra(CalendarContract.Events.DESCRIPTION, descMatch)
					.putExtra(CalendarContract.Events.EVENT_LOCATION, sportCenter);
			context.startActivity(intent);
		}
	}
}

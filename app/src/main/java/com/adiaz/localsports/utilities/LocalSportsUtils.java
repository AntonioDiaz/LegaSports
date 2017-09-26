package com.adiaz.localsports.utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.adiaz.localsports.R;
import com.adiaz.localsports.database.LocalSportsDbContract;
import com.adiaz.localsports.entities.Competition;
import com.adiaz.localsports.entities.Match;
import com.adiaz.localsports.sync.retrofit.AddIssueCallback;
import com.adiaz.localsports.sync.retrofit.LocalSportsRestApi;
import com.adiaz.localsports.sync.retrofit.entities.issue.Issue;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences;
/* Created by toni on 28/03/2017. */

public class LocalSportsUtils {

	private static final String TAG = LocalSportsUtils.class.getSimpleName();


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

	public static boolean isShowNotification(Context context) {
		SharedPreferences preferences = getDefaultSharedPreferences(context);
		String notificationsKey = context.getString(R.string.pref_notifications_key);
		boolean notificationsDefault = Boolean.parseBoolean(context.getString(R.string.pref_notifications_default));
		return  preferences.getBoolean(notificationsKey, notificationsDefault);
	}

	public static List<String> sportsList(Context context){
		List<String> sportsList = new ArrayList<>();
		sportsList.add("");
		sportsList.add(LocalSportsUtils.getStringResourceByName(context, context.getString(R.string.basketball_tag)));
		sportsList.add(LocalSportsUtils.getStringResourceByName(context, context.getString(R.string.football_11_tag)));
		sportsList.add(LocalSportsUtils.getStringResourceByName(context, context.getString(R.string.football_7_tag)));
		sportsList.add(LocalSportsUtils.getStringResourceByName(context, context.getString(R.string.football_sala_tag)));
		sportsList.add(LocalSportsUtils.getStringResourceByName(context, context.getString(R.string.volleyball_tag)));
		sportsList.add(LocalSportsUtils.getStringResourceByName(context, context.getString(R.string.handball_tag)));
		sportsList.add(LocalSportsUtils.getStringResourceByName(context, context.getString(R.string.unihockey_tag)));
		return sportsList;
	}

	public static List<String> categoriesList(Context context) {
		List<String> categories = new ArrayList<>();
		categories.add("");
		ContentResolver cr = context.getContentResolver();
		Uri uri = LocalSportsDbContract.CompetitionsEntry.CONTENT_URI;
		Cursor cursor = cr.query(uri, LocalSportsDbContract.CompetitionsEntry.PROJECTION, null, null, null);
		while (cursor.moveToNext()) {
			String categoryName = cursor.getString(LocalSportsDbContract.CompetitionsEntry.INDEX_CATEGORY);
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
		Uri uri = LocalSportsDbContract.CompetitionsEntry.CONTENT_URI;
		Cursor cursor = cr.query(uri, LocalSportsDbContract.CompetitionsEntry.PROJECTION, null, null, null);
		while (cursor.moveToNext()) {
			String competitionName = cursor.getString(LocalSportsDbContract.CompetitionsEntry.INDEX_NAME);
			if (!competitions.contains(competitionName)) {
				competitions.add(competitionName);
			}
		}
		cursor.close();
		return competitions;
	}

	public static void sendIssue(Context context, Competition competition, Match match, String description) {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(LocalSportsConstants.BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		LocalSportsRestApi localSportsRestApi = retrofit.create(LocalSportsRestApi.class);
		Issue issue = new Issue();
		issue.setClientId(PreferencesUtils.queryUUID(context));
		issue.setCompetitionId(competition.serverId());
		issue.setMatchId(match.idMatch());
		issue.setDescription(description);
		Call<Long> call = localSportsRestApi.addIssue(issue);
		call.enqueue(new AddIssueCallback(context));
	}

	public static void disableImageButton(ImageButton imageButton) {
		imageButton.setClickable(false);
		imageButton.setEnabled(false);
		Drawable drawableDisabled = LocalSportsUtils.convertDrawableToGrayScale(imageButton.getDrawable());
		imageButton.setImageDrawable(drawableDisabled);
	}

	public static Drawable convertDrawableToGrayScale(Drawable drawable) {
		if (drawable == null) {
			return null;
		}
		Drawable res = drawable.mutate();
		res.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
		return res;
	}
}



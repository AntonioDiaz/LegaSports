package com.adiaz.munisports.sync.retrofit;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.adiaz.munisports.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by toni on 16/09/2017.
 */

public class AddIssueCallback implements Callback<Long> {

	private static final String TAG = AddIssueCallback.class.getSimpleName();
	private Context context;

	public AddIssueCallback(Context context) {
		this.context = context;
	}

	@Override
	public void onResponse(Call<Long> call, Response<Long> response) {
		Log.d(TAG, "onResponse: code" + response.code());
		Log.d(TAG, "onResponse: body" + response.body());
		String msgToast = context.getString(R.string.dialog_issue_error_http, String.valueOf(response.code()));
		if (response.code()==200) {
			msgToast = context.getString(R.string.dialog_issue_added);
		}
		Toast.makeText(context, msgToast, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onFailure(Call<Long> call, Throwable t) {
		String msgToast = context.getString(R.string.dialog_issue_error);
		Toast.makeText(context, msgToast, Toast.LENGTH_SHORT).show();
	}
}

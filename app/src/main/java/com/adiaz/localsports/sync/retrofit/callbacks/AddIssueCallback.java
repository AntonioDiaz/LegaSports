package com.adiaz.localsports.sync.retrofit.callbacks;

import android.content.Context;
import android.widget.Toast;

import com.adiaz.localsports.R;

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

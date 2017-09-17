package com.adiaz.munisports.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adiaz.munisports.R;
import com.adiaz.munisports.activities.CompetitionActivity;
import com.adiaz.munisports.entities.Competition;
import com.adiaz.munisports.entities.Match;
import com.adiaz.munisports.utilities.MuniSportsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by toni on 15/09/2017.
 */

public class SendIssueDialogFragment extends DialogFragment {

	private static final String TAG = SendIssueDialogFragment.class.getSimpleName();

	@BindView(R.id.tv_dialog_competition)
	TextView tvCompetition;
	@BindView(R.id.tv_dialog_category)
	TextView tvCategory;
	@BindView(R.id.tv_dialog_match)
	TextView tvMatch;
	@BindView(R.id.ed_dialog_description)
	EditText edDescription;

	private Context mContext;


	static SendIssueDialogFragment newInstance(Match match, Competition competition) {
		SendIssueDialogFragment f = new SendIssueDialogFragment();
		Bundle args = new Bundle();
		args.putParcelable("match", match);
		args.putParcelable("competition", competition);
		f.setArguments(args);
		return f;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mContext = this.getContext();
		final Match match = getArguments().getParcelable("match");
		final Competition competition = getArguments().getParcelable("competition");
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater layoutInflater = getActivity().getLayoutInflater();
		View view = layoutInflater.inflate(R.layout.dialog_notification, null);
		builder.setView(view);
		ButterKnife.bind(this, view);
		tvCompetition.setText(competition.name());
		String strSport = MuniSportsUtils.getStringResourceByName(this.getActivity(), competition.sportName());
		tvCategory.setText(strSport + " (" + competition.categoryName() + ")");
		tvMatch.setText(match.obtainMatchDescription(this.getActivity()));
		builder.setTitle(R.string.dialog_title_send_issue);
		builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if (!TextUtils.isEmpty(edDescription.getText())) {
							CompetitionActivity activity = (CompetitionActivity)getActivity();
							activity.sendNotification(competition, match, edDescription.getText().toString());
						} else {
							String msgToast = mContext.getString(R.string.dialog_description_empty);
							Toast.makeText(mContext, msgToast, Toast.LENGTH_SHORT).show();
						}
				}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
					}
				});
		return builder.create();
	}
}

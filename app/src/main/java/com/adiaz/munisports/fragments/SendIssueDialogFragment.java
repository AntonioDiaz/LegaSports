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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adiaz.munisports.R;
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
	private Match mMatch;
	private Competition mCompetition;
	private OnSendIssue onSendIssue;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		onSendIssue = (OnSendIssue) context;
	}

	public static SendIssueDialogFragment newInstance(Match match, Competition competition) {
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
		mMatch = getArguments().getParcelable("match");
		mCompetition = getArguments().getParcelable("competition");
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater layoutInflater = getActivity().getLayoutInflater();
		View view = layoutInflater.inflate(R.layout.dialog_notification, null);
		builder.setView(view);
		ButterKnife.bind(this, view);
		tvCompetition.setText(mCompetition.name());
		String strSport = MuniSportsUtils.getStringResourceByName(this.getActivity(), mCompetition.sportName());
		tvCategory.setText(strSport + " (" + mCompetition.categoryName() + ")");
		tvMatch.setText(mMatch.obtainMatchDescription(this.getActivity()));
		builder.setTitle(R.string.dialog_title_send_issue);
		builder
				.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) { }
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) { }
				});
		return builder.create();
	}

	@Override
	public void onStart() {
		super.onStart();
		AlertDialog alertDialog = (AlertDialog)getDialog();
		if (alertDialog!=null) {
			Button positiveButton = (Button) alertDialog.getButton(Dialog.BUTTON_POSITIVE);
			positiveButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (!TextUtils.isEmpty(edDescription.getText())) {
						onSendIssue.doSendIssue(mCompetition, mMatch, edDescription.getText().toString());
						dismiss();
					} else {
						String msgToast = mContext.getString(R.string.dialog_description_empty);
						Toast.makeText(mContext, msgToast, Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
	}

	public interface OnSendIssue {
		public void doSendIssue(Competition competition, Match match, String description);
	}
}

package com.adiaz.munisports.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.adiaz.munisports.R;
import com.adiaz.munisports.entities.Match;
import com.adiaz.munisports.utilities.MuniSportsConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/* Created by toni on 23/03/2017. */

public class CalendarAdapter extends BaseExpandableListAdapter {

	private static final String TAG = CalendarAdapter.class.getSimpleName();
	private Context mContext;
	private List<List<Match>> weeksList;

	@Nullable @BindView(R.id.childItem_teamlocal) TextView tvLocalTeam;
	@Nullable @BindView(R.id.childItem_teamvisitor) TextView tvVisitorTeam;
	@Nullable @BindView(R.id.childItem_date) TextView date;
	@Nullable @BindView(R.id.childItem_place) TextView place;
	@Nullable @BindView(R.id.childItem_result) TextView result;
	@Nullable @BindView(R.id.heading) TextView textViewHeading;

	public CalendarAdapter(Context mContext, List<List<Match>> weeksList) {
		this.mContext = mContext;
		this.weeksList = weeksList;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		List<Match> matches = weeksList.get(groupPosition);
		Match match = matches.get(childPosition);
		return match;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
		Match match = (Match) getChild(groupPosition, childPosition);
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.listitem_child_calendar, null);
		}
		ButterKnife.bind(this, view);
		view.setTag(match);
		String teamLocal = match.getTeamLocal();
		String teamVisitor = match.getTeamVisitor();
		if (teamLocal.equals(MuniSportsConstants.UNDEFINDED_FIELD) && teamVisitor.equals(MuniSportsConstants.UNDEFINDED_FIELD)) {
			tvLocalTeam.setText(mContext.getString(R.string.undefined_match));
			tvVisitorTeam.setText("");
		} else {
			if (teamLocal.equals(MuniSportsConstants.UNDEFINDED_FIELD)) {
				teamLocal = mContext.getString(R.string.rest_team);
			}
			tvLocalTeam.setText(teamLocal);
			if (teamVisitor.equals(MuniSportsConstants.UNDEFINDED_FIELD)) {
				teamVisitor = mContext.getString(R.string.rest_team);
			}
			tvVisitorTeam.setText(teamVisitor);
		}
		if (match.getDate().getTime()==0) {
			date.setText(mContext.getString(R.string.undefined_date));
		} else {
			DateFormat df = new SimpleDateFormat(MuniSportsConstants.DATE_FORMAT);
			date.setText(df.format(match.getDate()));
		}
		place.setText(match.getPlaceName());
		String strResult = "";
		switch (match.getState()) {
			case MuniSportsConstants.STATE_PENDING:
				strResult = mContext.getString(R.string.match_pending);
				break;
			case MuniSportsConstants.STATE_CANCELED:
				strResult = mContext.getString(R.string.match_cancel);
				break;
			case MuniSportsConstants.STATE_PLAYED:
				strResult = String.format("%d - %d", match.getScoreLocal(), match.getScoreVisitor());
				break;
		}
		result.setText(strResult);
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.weeksList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return weeksList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return weeksList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isLastChild, View view, ViewGroup parent) {
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.listitem_header_calendar, null);
		}
		ButterKnife.bind(this, view);
		TextView textViewHeading = (TextView) view.findViewById(R.id.heading);
		String jornadaStr = mContext.getString(R.string.jornada_header, groupPosition + 1);
		textViewHeading.setText(jornadaStr);
		return view;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}


}
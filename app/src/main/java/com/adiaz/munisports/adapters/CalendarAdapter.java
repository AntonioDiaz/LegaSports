package com.adiaz.munisports.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.adiaz.munisports.R;
import com.adiaz.munisports.entities.MatchEntity;
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
	private List<List<MatchEntity>> weeksList;

	@Nullable @BindView(R.id.childItem_teamlocal) TextView localTeam;
	@Nullable @BindView(R.id.childItem_teamvisitor) TextView visitorTeam;
	@Nullable @BindView(R.id.childItem_date) TextView date;
	@Nullable @BindView(R.id.childItem_place) TextView place;
	@Nullable @BindView(R.id.heading) TextView textViewHeading;

	public CalendarAdapter(Context mContext, List<List<MatchEntity>> weeksList) {
		this.mContext = mContext;
		this.weeksList = weeksList;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		List<MatchEntity> matches = weeksList.get(groupPosition);
		MatchEntity matchEntity = matches.get(childPosition);
		return matchEntity;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
		MatchEntity matchEntity = (MatchEntity) getChild(groupPosition, childPosition);
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.listitem_child_calendar, null);
		}
		ButterKnife.bind(this, view);
		String teamLocal = matchEntity.getTeamLocal();
		if (teamLocal.equals(MuniSportsConstants.UNDEFINDED_FIELD)) {
			teamLocal = mContext.getString(R.string.rest_team);
		}
		localTeam.setText(teamLocal);
		String teamVisitor = matchEntity.getTeamVisitor();
		if (teamVisitor.equals(MuniSportsConstants.UNDEFINDED_FIELD)) {
			teamVisitor = mContext.getString(R.string.rest_team);
		}

		visitorTeam.setText(teamVisitor);
		if (matchEntity.getDate().getTime()==0) {
			date.setText(mContext.getString(R.string.undefined_date));
		} else {
			DateFormat df = new SimpleDateFormat(MuniSportsConstants.DATE_FORMAT);
			date.setText(df.format(matchEntity.getDate()));
		}
		place.setText(matchEntity.getPlace());
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
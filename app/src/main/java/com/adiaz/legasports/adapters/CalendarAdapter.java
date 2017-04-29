package com.adiaz.legasports.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.adiaz.legasports.R;
import com.adiaz.legasports.entities.JornadaEntity;
import com.adiaz.legasports.entities.MatchEntity;
import com.adiaz.legasports.utilities.LegaSportsConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/* Created by toni on 23/03/2017. */

public class CalendarAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private List<JornadaEntity> jornadas;

	@Nullable @BindView(R.id.childItem_teamlocal) TextView localTeam;
	@Nullable @BindView(R.id.childItem_teamvisitor) TextView visitorTeam;
	@Nullable @BindView(R.id.childItem_date) TextView date;
	@Nullable @BindView(R.id.childItem_place) TextView place;
	@Nullable @BindView(R.id.heading) TextView textViewHeading;

	public CalendarAdapter(Context mContext, List<JornadaEntity> jornadas) {
		this.mContext = mContext;
		this.jornadas = jornadas;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		JornadaEntity jornadaEntity = jornadas.get(groupPosition);
		MatchEntity matchEntity = jornadaEntity.getMatches().get(childPosition);
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
		localTeam.setText(matchEntity.getTeamLocal());
		visitorTeam.setText(matchEntity.getTeamVisitor());
		DateFormat df = new SimpleDateFormat(LegaSportsConstants.DATE_FORMAT);
		date.setText(df.format(matchEntity.getDate()));
		place.setText(matchEntity.getPlace());
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		JornadaEntity jornadaEntity = jornadas.get(groupPosition);
		return jornadaEntity.getMatches().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return jornadas.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return jornadas.size();
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
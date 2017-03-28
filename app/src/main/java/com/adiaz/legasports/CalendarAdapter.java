package com.adiaz.legasports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

/* Created by toni on 23/03/2017. */

public class CalendarAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private List<JornadaEntity> jornadas;

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
			view = layoutInflater.inflate(R.layout.child_row, null);
		}
		TextView localTeam = (TextView) view.findViewById(R.id.childItem_teamlocal);
		TextView visitorTeam = (TextView) view.findViewById(R.id.childItem_teamvisitor);
		TextView date = (TextView) view.findViewById(R.id.childItem_date);
		TextView place = (TextView) view.findViewById(R.id.childItem_place);

		localTeam.setText(matchEntity.getTeamLocal());
		visitorTeam.setText(matchEntity.getTeamVisitor());
		date.setText(matchEntity.getDate() + " " + matchEntity.getHour());
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
		JornadaEntity jornadaEntity = (JornadaEntity) getGroup(groupPosition);
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.group_header, null);
		}
		TextView textView = (TextView) view.findViewById(R.id.heading);
		String jornadaStr = mContext.getString(R.string.jornada_header, groupPosition + 1);
		textView.setText(jornadaStr);
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
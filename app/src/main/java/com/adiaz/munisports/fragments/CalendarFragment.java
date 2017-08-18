package com.adiaz.munisports.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.munisports.R;
import com.adiaz.munisports.activities.CompetitionActivity;
import com.adiaz.munisports.adapters.CalendarAdapter;
import com.adiaz.munisports.utilities.NonScrollExpandableListView;

import butterknife.BindView;
import butterknife.ButterKnife;



/* Created by toni on 21/03/2017. */

public class CalendarFragment extends Fragment {

	@BindView(R.id.elv_jornadas) NonScrollExpandableListView nonScrollExpandableListView;
	@BindView(R.id.tv_empty_list_item) TextView tvEmptyListItem;

	public CalendarFragment() { }

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_calendar, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		CalendarAdapter calendarAdapter = new CalendarAdapter(getActivity(), CompetitionActivity.jornadas);
		nonScrollExpandableListView.setAdapter(calendarAdapter);
		nonScrollExpandableListView.setEmptyView(tvEmptyListItem);
		calendarAdapter.notifyDataSetChanged();
	}
}

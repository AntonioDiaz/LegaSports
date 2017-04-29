package com.adiaz.legasports.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiaz.legasports.R;
import com.adiaz.legasports.activities.CompetitionActivity;
import com.adiaz.legasports.adapters.CalendarAdapter;
import com.adiaz.legasports.utilities.NonScrollExpandableListView;

import butterknife.BindView;
import butterknife.ButterKnife;



/* Created by toni on 21/03/2017. */

public class CalendarFragment extends Fragment {

	@BindView(R.id.elv_jornadas) NonScrollExpandableListView nonScrollExpandableListView;

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
		calendarAdapter.notifyDataSetChanged();
	}
}

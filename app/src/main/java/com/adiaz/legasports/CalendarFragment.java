package com.adiaz.legasports;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/* Created by toni on 21/03/2017. */

public class CalendarFragment extends Fragment {

	public CalendarFragment() { }

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_calendar, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		NonScrollExpandableListView expandableListView = (NonScrollExpandableListView) getActivity().findViewById(R.id.elv_jornadas);
//		ExpandableListView expandableListView = (ExpandableListView) getActivity().findViewById(R.id.elv_jornadas);
		List<JornadaEntity> jornadas = initCalendar();
		CalendarAdapter calendarAdapter = new CalendarAdapter(getActivity(), jornadas);
		expandableListView.setAdapter(calendarAdapter);
		calendarAdapter.notifyDataSetChanged();
	}

	private List<JornadaEntity> initCalendar() {
		List<JornadaEntity> calendar = new ArrayList<>();
		AssetManager assetManager = getResources().getAssets();
		try {
			InputStreamReader inputStream = new InputStreamReader(assetManager.open("calendar.txt"));
			BufferedReader reader = new BufferedReader(inputStream);
			boolean started = false;
			String line;
			List<MatchEntity> matches = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("Jornada")) {
					if (started) {
						//save previous jornada
						calendar.add(new JornadaEntity(matches));
					}
					matches = new ArrayList<>();
					started = true;
				} else {
					// is a match
					MatchEntity matchEntity = new MatchEntity(line);
					matches.add(matchEntity);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return calendar;
	}
}

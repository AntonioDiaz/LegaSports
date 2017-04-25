package com.adiaz.legasports.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiaz.legasports.utilities.NonScrollExpandableListView;
import com.adiaz.legasports.R;
import com.adiaz.legasports.adapters.TeamsAdapter;
import com.adiaz.legasports.utilities.Utils;
import com.adiaz.legasports.entities.TeamEntity;

import java.util.List;

/* Created by toni on 21/03/2017. */

public class TeamsFragment extends Fragment {

	public TeamsFragment() { }

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_teams, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		NonScrollExpandableListView expandableListView = (NonScrollExpandableListView) getActivity().findViewById(R.id.elv_teams);
		List<TeamEntity> teams = Utils.initTeams(getActivity());
		TeamsAdapter teamsAdapter = new TeamsAdapter(getActivity(), teams);
		expandableListView.setAdapter(teamsAdapter);
		teamsAdapter.notifyDataSetChanged();
	}

}

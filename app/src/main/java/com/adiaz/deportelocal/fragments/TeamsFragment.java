package com.adiaz.deportelocal.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.deportelocal.R;
import com.adiaz.deportelocal.activities.CompetitionActivity;
import com.adiaz.deportelocal.adapters.TeamsAdapter;
import com.adiaz.deportelocal.utilities.NonScrollExpandableListView;

import butterknife.BindView;
import butterknife.ButterKnife;


/* Created by toni on 21/03/2017. */

public class TeamsFragment extends Fragment {

	private static final String TAG = TeamsFragment.class.getSimpleName();

	@BindView(R.id.elv_teams) NonScrollExpandableListView expandableListView;
	@BindView(R.id.tv_empty_list_item) TextView tvEmptyListItem;


	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_teams, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Long competitionId = CompetitionActivity.mCompetition.serverId();
		TeamsAdapter teamsAdapter = new TeamsAdapter(getActivity(), CompetitionActivity.mTeams, competitionId);
		expandableListView.setAdapter(teamsAdapter);
		expandableListView.setEmptyView(tvEmptyListItem);
		teamsAdapter.notifyDataSetChanged();
	}
}

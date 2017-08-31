package com.adiaz.munisports.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.munisports.R;
import com.adiaz.munisports.activities.CompetitionActivity;
import com.adiaz.munisports.adapters.TeamsAdapter;
import com.adiaz.munisports.entities.TeamEntity;
import com.adiaz.munisports.utilities.NonScrollExpandableListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/* Created by toni on 21/03/2017. */

public class TeamsFragment extends Fragment {

	private static final String TAG = TeamsFragment.class.getSimpleName();

	@BindView(R.id.elv_teams) NonScrollExpandableListView expandableListView;
	@BindView(R.id.tv_empty_list_item) TextView tvEmptyListItem;
	private OnDataPassTeams onDataPassTeams;


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
		List<TeamEntity> teams = onDataPassTeams.onDataPassTeams();
		String idCompetition = getArguments().getString(CompetitionActivity.BUNDLE_KEY_ID_COMPETITION);
		TeamsAdapter teamsAdapter = new TeamsAdapter(getActivity(), teams, idCompetition);
		expandableListView.setAdapter(teamsAdapter);
		expandableListView.setEmptyView(tvEmptyListItem);
		teamsAdapter.notifyDataSetChanged();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		onDataPassTeams = (OnDataPassTeams) context;
	}

	public interface OnDataPassTeams {
		List<TeamEntity> onDataPassTeams();
	}

}

package com.adiaz.legasports;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiaz.legasports.entities.TeamEntity;

import java.util.List;

/* Created by toni on 31/03/2017. */

public class FavoritesTeamsFragment extends Fragment {


	public FavoritesTeamsFragment() {
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_fav_teams, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		FavoritesTeamsAdapter favoritesTeamsAdapter = new FavoritesTeamsAdapter(getActivity());
		RecyclerView recyclerView = (RecyclerView)getActivity().findViewById(R.id.rv_fav_teams);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(favoritesTeamsAdapter);
		List<TeamEntity> teamEntities = Utils.initFavoritesTeams(getActivity());
		favoritesTeamsAdapter.setTeamsFavorites(teamEntities);
	}
}

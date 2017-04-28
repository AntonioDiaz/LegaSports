package com.adiaz.legasports.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiaz.legasports.R;
import com.adiaz.legasports.activities.FavoriteTeamActivity;
import com.adiaz.legasports.activities.FavoritesActivity;
import com.adiaz.legasports.adapters.FavoritesTeamsAdapter;
import com.adiaz.legasports.entities.TeamFavoriteEntity;
import com.adiaz.legasports.utilities.LegaSportsConstants;

/* Created by toni on 31/03/2017. */

public class FavoritesTeamsFragment extends Fragment implements FavoritesTeamsAdapter.ListItemClickListener {


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
		FavoritesTeamsAdapter favoritesTeamsAdapter = new FavoritesTeamsAdapter(getActivity(), this);
		RecyclerView recyclerView = (RecyclerView)getActivity().findViewById(R.id.rv_fav_teams);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(favoritesTeamsAdapter);
		favoritesTeamsAdapter.setTeamsFavorites(FavoritesActivity.teamsFavorites);
	}

	@Override
	public void onListItemClick(int clickedItemIndex) {
		Intent intent = new Intent(getActivity(), FavoriteTeamActivity.class);
		TeamFavoriteEntity teamFavoriteEntity = FavoritesActivity.teamsFavorites.get(clickedItemIndex);
		intent.putExtra(LegaSportsConstants.INTENT_TEAM_NAME, teamFavoriteEntity.getName());
		intent.putExtra(LegaSportsConstants.INTENT_ID_COMPETITION_SERVER, teamFavoriteEntity.getIdCompetitionServer());
		startActivity(intent);

	}
}

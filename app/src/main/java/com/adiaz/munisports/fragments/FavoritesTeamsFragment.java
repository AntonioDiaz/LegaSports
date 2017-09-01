package com.adiaz.munisports.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.munisports.R;
import com.adiaz.munisports.activities.FavoriteTeamActivity;
import com.adiaz.munisports.activities.FavoritesActivity;
import com.adiaz.munisports.adapters.FavoritesTeamsAdapter;
import com.adiaz.munisports.entities.TeamFavorite;
import com.adiaz.munisports.utilities.MuniSportsConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

/* Created by toni on 31/03/2017. */

public class FavoritesTeamsFragment extends Fragment implements FavoritesTeamsAdapter.ListItemClickListener {

	@BindView(R.id.rv_fav_teams) RecyclerView recyclerView;
	@BindView(R.id.tv_empty_list_item) TextView tvEmptyList;

	public FavoritesTeamsFragment() {
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_fav_teams, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (FavoritesActivity.teamsFavorites.size()==0) {
			recyclerView.setVisibility(View.INVISIBLE);
			tvEmptyList.setVisibility(View.VISIBLE);
		} else {
			recyclerView.setVisibility(View.VISIBLE);
			tvEmptyList.setVisibility(View.INVISIBLE);
			FavoritesTeamsAdapter favoritesTeamsAdapter = new FavoritesTeamsAdapter(getActivity(), this);
			recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
			recyclerView.setHasFixedSize(true);
			recyclerView.setAdapter(favoritesTeamsAdapter);
			favoritesTeamsAdapter.setTeamsFavorites(FavoritesActivity.teamsFavorites);
		}
	}

	@Override
	public void onListItemClick(int clickedItemIndex) {
		Intent intent = new Intent(getActivity(), FavoriteTeamActivity.class);
		TeamFavorite teamFavorite = FavoritesActivity.teamsFavorites.get(clickedItemIndex);
		intent.putExtra(MuniSportsConstants.INTENT_TEAM_NAME, teamFavorite.getName());
		intent.putExtra(MuniSportsConstants.INTENT_ID_COMPETITION_SERVER, teamFavorite.getIdCompetitionServer());
		intent.putExtra(MuniSportsConstants.INTENT_COMPETITION_NAME, teamFavorite.getCompetitionName());
		intent.putExtra(MuniSportsConstants.INTENT_SPORT_TAG, teamFavorite.getSportTag());
		intent.putExtra(MuniSportsConstants.INTENT_CATEGORY_TAG, teamFavorite.getCategoryTag());
		startActivity(intent);
	}
}

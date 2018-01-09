package com.adiaz.localsports.fragments;

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

import com.adiaz.localsports.R;
import com.adiaz.localsports.activities.CompetitionActivity;
import com.adiaz.localsports.activities.FavoritesActivity;
import com.adiaz.localsports.adapters.FavoritesCompetitionsAdapter;
import com.adiaz.localsports.entities.Competition;
import com.adiaz.localsports.utilities.LocalSportsConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

/* Created by toni on 31/03/2017. */

public class FavoritesCompetitionsFragment extends Fragment implements FavoritesCompetitionsAdapter.ListItemClickListener {

	@BindView(R.id.rv_fav_competitions) RecyclerView recyclerView;
	@BindView(R.id.tv_empty_list_item) TextView tvEmptyList;

	public FavoritesCompetitionsFragment() {
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_fav_competitions, container, false);
		ButterKnife.bind(this, view);
		return view;
	}


	@Override
	public void onResume() {
		super.onResume();
		if (FavoritesActivity.competitionsFavorites.size()==0) {
			recyclerView.setVisibility(View.INVISIBLE);
			tvEmptyList.setVisibility(View.VISIBLE);

		} else {
			recyclerView.setVisibility(View.VISIBLE);
			tvEmptyList.setVisibility(View.INVISIBLE);
			FavoritesCompetitionsAdapter favoritesCompetitionsAdapter = new FavoritesCompetitionsAdapter(getActivity(), this);
			recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
			recyclerView.setHasFixedSize(true);
			recyclerView.setAdapter(favoritesCompetitionsAdapter);
			favoritesCompetitionsAdapter.setCompetitionsFavorites(FavoritesActivity.competitionsFavorites);
		}
	}

	@Override
	public void onListItemClick(int clickedItemIndex) {
		Intent intent = new Intent(getActivity(), CompetitionActivity.class);
		Competition competition = FavoritesActivity.competitionsFavorites.get(clickedItemIndex);
		intent.putExtra(LocalSportsConstants.INTENT_COMPETITION, competition);
		startActivity(intent);
	}
}

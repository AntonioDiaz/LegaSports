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
import com.adiaz.legasports.activities.CompetitionActivity;
import com.adiaz.legasports.activities.FavoritesActivity;
import com.adiaz.legasports.adapters.FavoritesCompetitionsAdapter;
import com.adiaz.legasports.entities.CompetitionEntity;
import com.adiaz.legasports.utilities.LegaSportsConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

/* Created by toni on 31/03/2017. */

public class FavoritesCompetitionsFragment extends Fragment implements FavoritesCompetitionsAdapter.ListItemClickListener {

	@BindView(R.id.rv_fav_competitions) RecyclerView recyclerView;

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
		FavoritesCompetitionsAdapter favoritesCompetitionsAdapter = new FavoritesCompetitionsAdapter(getActivity(), this);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(favoritesCompetitionsAdapter);
		favoritesCompetitionsAdapter.setCompetitionsFavorites(FavoritesActivity.competitionsFavorites);
	}

	@Override
	public void onListItemClick(int clickedItemIndex) {
		Intent intent = new Intent(getActivity(), CompetitionActivity.class);
		CompetitionEntity competitionEntity = FavoritesActivity.competitionsFavorites.get(clickedItemIndex);
		intent.putExtra(LegaSportsConstants.INTENT_COMPETITION_NAME, competitionEntity.getName());
		intent.putExtra(LegaSportsConstants.INTENT_SPORT_TAG, competitionEntity.getSportName());
		intent.putExtra(LegaSportsConstants.INTENT_CATEGORY_TAG, competitionEntity.getCategoryName());
		intent.putExtra(LegaSportsConstants.INTENT_ID_COMPETITION_SERVER, competitionEntity.getServerId());
		startActivity(intent);
	}
}

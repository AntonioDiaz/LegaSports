package com.adiaz.deportelocal.fragments;

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

import com.adiaz.deportelocal.R;
import com.adiaz.deportelocal.activities.FavoriteTeamActivity;
import com.adiaz.deportelocal.activities.FavoritesActivity;
import com.adiaz.deportelocal.activities.SportsActivity;
import com.adiaz.deportelocal.adapters.FavoritesTeamsAdapter;
import com.adiaz.deportelocal.entities.TeamFavorite;
import com.adiaz.deportelocal.utilities.DeporteLocalConstants;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import butterknife.BindView;
import butterknife.ButterKnife;

/* Created by toni on 31/03/2017. */

public class FavoritesTeamsFragment extends Fragment implements FavoritesTeamsAdapter.ListItemClickListener {

	@BindView(R.id.rv_fav_teams) RecyclerView recyclerView;
	@BindView(R.id.tv_empty_list_item) TextView tvEmptyList;

	InterstitialAd mInterstitialAd;
	int mClickedItemIndex;

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
        mInterstitialAd = new InterstitialAd(this.getActivity());
        mInterstitialAd.setAdUnitId(DeporteLocalConstants.INTESTITIAL_AD_ID);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                FavoritesTeamsFragment.this.onAdClosed();
            }
        });
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
        mClickedItemIndex = clickedItemIndex;
        SportsActivity.interstitialCount++;
        if (mInterstitialAd.isLoaded() && SportsActivity.interstitialCount % DeporteLocalConstants.INTERSTITIAL_FRECUENCY==0) {
            mInterstitialAd.show();
        } else {
            onAdClosed();
        }
	}

    private void onAdClosed() {
        Intent intent = new Intent(getActivity(), FavoriteTeamActivity.class);
        TeamFavorite teamFavorite = FavoritesActivity.teamsFavorites.get(mClickedItemIndex);
        intent.putExtra(DeporteLocalConstants.INTENT_TEAM_NAME, teamFavorite.getName());
        intent.putExtra(DeporteLocalConstants.INTENT_ID_COMPETITION_SERVER, teamFavorite.getIdCompetitionServer());
        startActivity(intent);
    }


}

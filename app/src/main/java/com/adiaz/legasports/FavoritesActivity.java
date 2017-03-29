package com.adiaz.legasports;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.adiaz.legasports.entities.TeamEntity;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

	private static final String TAG = FavoritesActivity.class.getSimpleName();
	public static final String TEAM_NAME = "team_name";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
		collapsingToolbar.setTitle(getString(R.string.favorites));
	}

	@Override
	protected void onResume() {
		super.onResume();
		FavoritesAdapter favoritesAdapter = new FavoritesAdapter(this);
		RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_favorites);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(favoritesAdapter);
		List<TeamEntity> teamEntities = Utils.initFavoritesTeams(this);
		Log.d(TAG, "onResume: teamEntities " + teamEntities);
		favoritesAdapter.setTeamsFavorites(teamEntities);

	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}

	public void openFavorite(View view) {
		/*Toast.makeText(this, "view " + view.getTag(), Toast.LENGTH_SHORT).show();*/
		Intent intent = new Intent(this, FavoriteTeamActivity.class);
		intent.putExtra(TEAM_NAME, (String)view.getTag());
		startActivity(intent);
	}
}

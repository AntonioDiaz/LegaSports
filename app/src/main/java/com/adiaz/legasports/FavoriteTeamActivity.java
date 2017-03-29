package com.adiaz.legasports;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.adiaz.legasports.entities.TeamEntity;

public class FavoriteTeamActivity extends AppCompatActivity {

	private String teamName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite_team);

		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
		teamName = getIntent().getStringExtra(FavoritesActivity.TEAM_NAME);
		collapsingToolbarLayout.setTitle(teamName);

		TeamEntity teamEntity = Utils.initTeam(this, teamName);

		FavoriteTeamAdapter adapter = new FavoriteTeamAdapter(this);
		RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_fav_team_jornadas);
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setNestedScrollingEnabled(false);
		adapter.setTeamEntity(teamEntity);
	}




	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}
}

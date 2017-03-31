package com.adiaz.legasports;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import static com.adiaz.legasports.MainActivity.EXTRA_SPORT_CHOSEN;

public class CategoriesActivity extends AppCompatActivity {

	public static final String EXTRA_CATEGORY_CHOSEN = "extra_category_chosen";
	private static final String TAG = CategoriesActivity.class.getSimpleName();
	private String sportTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_categories);
		sportTitle = getIntent().getStringExtra(MainActivity.EXTRA_SPORT_CHOSEN);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
		collapsingToolbar.setTitle(sportTitle);

		List<String> categories = Utils.getCategories(this, sportTitle);
		CategoriesAdapter categoriesAdapter = new CategoriesAdapter(this);
		categoriesAdapter.setCategories(categories);
		RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_categories);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(categoriesAdapter);
		recyclerView.setNestedScrollingEnabled(false);
	}

	public void openCategory(View view) {
		Intent intent = new Intent(this, ChampionshipActivity.class);
		intent.putExtra(EXTRA_CATEGORY_CHOSEN, (String)view.getTag());
		intent.putExtra(EXTRA_SPORT_CHOSEN, sportTitle);
		startActivity(intent);
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}

}

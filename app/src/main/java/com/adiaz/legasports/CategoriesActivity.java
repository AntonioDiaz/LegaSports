package com.adiaz.legasports;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

	}

	public void openCategory(View view) {
		Intent intent = new Intent(this, ChampionshipActivity.class);
		View childAt = ((ViewGroup) view).getChildAt(0);
		CharSequence category = ((TextView) childAt).getText();
		intent.putExtra(EXTRA_CATEGORY_CHOSEN, category);
		intent.putExtra(EXTRA_SPORT_CHOSEN, sportTitle);
		startActivity(intent);
	}
}

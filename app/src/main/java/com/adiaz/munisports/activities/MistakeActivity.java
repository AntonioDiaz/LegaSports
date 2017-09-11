package com.adiaz.munisports.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adiaz.munisports.R;
import com.adiaz.munisports.utilities.MuniSportsConstants;
import com.adiaz.munisports.utilities.MuniSportsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by toni on 06/09/2017.
 */

public class MistakeActivity extends AppCompatActivity {

	@BindView(R.id.toolbar)
	Toolbar toolbar;

	@BindView(R.id.tv_title)
	TextView tvTitle;

	@BindView(R.id.sp_sport)
	Spinner spSport;

	@BindView(R.id.sp_category)
	Spinner spCategory;

	@BindView(R.id.sp_competition)
	Spinner spCompetition;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mistake);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		SharedPreferences preferences = getDefaultSharedPreferences(this);
		String townSelect = preferences.getString(MuniSportsConstants.KEY_TOWN_NAME, null);
		tvTitle.setText(townSelect + " - " + getString(R.string.app_name));

		ArrayAdapter<String> adapterSports = new ArrayAdapter<String>(this, R.layout.spinner_item, MuniSportsUtils.sportsList(this));
		ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(this, R.layout.spinner_item, MuniSportsUtils.categoriesList(this));
		ArrayAdapter<String> adapterCompetition= new ArrayAdapter<String>(this, R.layout.spinner_item, MuniSportsUtils.competitionList(this));
		spSport.setAdapter(adapterSports);
		spCategory.setAdapter(adapterCategories);
		spCompetition.setAdapter(adapterCompetition);
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}

	public void returnActivity(View view) {
		onBackPressed();
	}

	public void sendMistakeToServer(View view) {
		String sport = spSport.getSelectedItem().toString();
		String category = spCategory.getSelectedItem().toString();
		String competition = spCompetition.getSelectedItem().toString();
		Toast.makeText(this, R.string.mistake_sent, Toast.LENGTH_SHORT).show();
		onBackPressed();
	}
}

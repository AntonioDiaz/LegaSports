package com.adiaz.munisports.activities;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adiaz.munisports.R;
import com.adiaz.munisports.entities.ClassificationEntity;
import com.adiaz.munisports.entities.JornadaEntity;
import com.adiaz.munisports.entities.TeamEntity;
import com.adiaz.munisports.fragments.CalendarFragment;
import com.adiaz.munisports.fragments.ClassificationFragment;
import com.adiaz.munisports.fragments.TeamsFragment;
import com.adiaz.munisports.utilities.MuniSportsConstants;
import com.adiaz.munisports.utilities.NetworkUtilities;
import com.adiaz.munisports.utilities.Utils;
import com.adiaz.munisports.utilities.ViewPagerAdapter;
import com.adiaz.munisports.utilities.harcoPro.HeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class CompetitionActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

	private static final String TAG = CompetitionActivity.class.getSimpleName();

	@Nullable
	@BindView((R.id.layout_activity_competition)) View activityView;


	@BindView(R.id.app_bar_layout)
	AppBarLayout appBarLayout;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.collapsing_toolbar)
	CollapsingToolbarLayout collapsingToolbar;
	@BindView(R.id.toolbar_header_view)
	HeaderView toolbarHeaderView;
	@BindView(R.id.float_header_view)
	HeaderView floatHeaderView;
	@BindView(R.id.tabs)
	TabLayout tabLayout;
	@BindView(R.id.viewpager)
	ViewPager viewPager;
	@BindView(R.id.tv_title)
	TextView tvTitle;
	@BindView(R.id.ll_progress_competition)
	LinearLayout llCompetition;

	private boolean isHideToolbarView = false;

	private String sportTitle;
	public static String idCompetitionServer;
	public static List<TeamEntity> teams;
	public static List<JornadaEntity> jornadas;
	public static List<ClassificationEntity> classificationList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_competition);
		ButterKnife.bind(this);
		SharedPreferences preferences = getDefaultSharedPreferences(this);
		String townSelect = preferences.getString(MuniSportsConstants.KEY_TOWN_NAME, null);
		tvTitle.setText(townSelect + " - " + getString(R.string.app_name));

		idCompetitionServer = getIntent().getStringExtra(MuniSportsConstants.INTENT_ID_COMPETITION_SERVER);

		String sportTag = getIntent().getStringExtra(MuniSportsConstants.INTENT_SPORT_TAG);
		String categoryTag = getIntent().getStringExtra(MuniSportsConstants.INTENT_CATEGORY_TAG);
		String competitionName = getIntent().getStringExtra(MuniSportsConstants.INTENT_COMPETITION_NAME);

		String sport = Utils.getStringResourceByName(this, sportTag);
		String category = Utils.getStringResourceByName(this, categoryTag);

		sportTitle = sport + " (" + category + ")";

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		collapsingToolbar.setTitle(" ");

		toolbarHeaderView.bindTo(competitionName, sportTitle, 0);
		floatHeaderView.bindTo(competitionName, sportTitle, 16);

		appBarLayout.addOnOffsetChangedListener(this);


		setupViewPager(viewPager);
		tabLayout.setupWithViewPager(viewPager);

		teams = new ArrayList<>();
		jornadas = new ArrayList<>();
		classificationList = new ArrayList<>();

	}

	@Override
	protected void onResume() {
		super.onResume();
		showLoading();
		if (NetworkUtilities.isNetworkAvailable(this)) {
			hideLoading();
		} else {
			hideLoading();
			Utils.showNoInternetAlert(this, activityView);
		}
	}

	private void hideLoading() {
		llCompetition.setVisibility(View.INVISIBLE);
		viewPager.setVisibility(View.VISIBLE);
	}

	private void showLoading() {
		llCompetition.setVisibility(View.VISIBLE);
		viewPager.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_competition, menu);
		for (int i = 0; i < menu.size(); i++) {
			if (menu.getItem(i).getItemId() == R.id.action_favorites) {
				String key = getString(R.string.key_favorites_competitions);
				if (Utils.checkIfFavoritSelected(this, idCompetitionServer, key)) {
					AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
					Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_fill);
					menu.getItem(i).setIcon(drawable);
				}
				Drawable icon = menu.getItem(i).getIcon();
				int colorWhite = ContextCompat.getColor(this, R.color.colorWhite);
				final PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(colorWhite, PorterDuff.Mode.SRC_IN);
				icon.setColorFilter(colorFilter);
			}
		}
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void setupViewPager(ViewPager viewPager) {
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(new TeamsFragment(), getString(R.string.teams));
		adapter.addFragment(new ClassificationFragment(), getString(R.string.classification));
		adapter.addFragment(new CalendarFragment(), getString(R.string.calendar));
		viewPager.setAdapter(adapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_favorites:
				String key = getString(R.string.key_favorites_competitions);
				AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
				Drawable drawable;
				if (Utils.checkIfFavoritSelected(this, idCompetitionServer, key)) {
					Utils.unMarkFavoriteTeam(this, idCompetitionServer, key);
					drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite);
				} else {
					Utils.markFavoriteTeam(this, idCompetitionServer, key);
					drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_fill);
				}
				int colorWhite = ContextCompat.getColor(this, R.color.colorWhite);
				final PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(colorWhite, PorterDuff.Mode.SRC_IN);
				drawable.setColorFilter(colorFilter);
				item.setIcon(drawable);

				break;
			case android.R.id.home:
				onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	public void selectFavorite(View view) {
		ImageView imageView = (ImageView) view.findViewById(R.id.iv_favorites);
		String myTeamId = Utils.generateTeamKey((String) imageView.getTag(), idCompetitionServer);
		String keyFavorites = getString(R.string.key_favorites_teams);
		if (Utils.checkIfFavoritSelected(this, myTeamId, keyFavorites)) {
			imageView.setImageResource(R.drawable.ic_favorite);
			Utils.unMarkFavoriteTeam(this, myTeamId, keyFavorites);
		} else {
			imageView.setImageResource(R.drawable.ic_favorite_fill);
			Utils.markFavoriteTeam(this, myTeamId, keyFavorites);
		}
	}

	@Override
	public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
		int maxScroll = appBarLayout.getTotalScrollRange();
		float percentage = (float) Math.abs(offset) / (float) maxScroll;
		if (percentage == 1f && isHideToolbarView) {
			toolbarHeaderView.setVisibility(View.VISIBLE);
			isHideToolbarView = !isHideToolbarView;
		} else if (percentage < 1f && !isHideToolbarView) {
			toolbarHeaderView.setVisibility(View.GONE);
			isHideToolbarView = !isHideToolbarView;
		}
	}
}


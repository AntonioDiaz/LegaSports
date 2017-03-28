package com.adiaz.legasports;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChampionshipActivity extends AppCompatActivity {

	private static final String TAG = ChampionshipActivity.class.getSimpleName();

	private Toolbar toolbar;
	private TabLayout tabLayout;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_championship);

		String sport = getIntent().getStringExtra(MainActivity.EXTRA_SPORT_CHOSEN);
		String category = getIntent().getStringExtra(CategoriesActivity.EXTRA_CATEGORY_CHOSEN);
		String sportTitle = sport + " (" + category + ")";

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
		collapsingToolbar.setTitle(sportTitle);

		viewPager = (ViewPager) findViewById(R.id.viewpager);
		setupViewPager(viewPager);

		tabLayout = (TabLayout) findViewById(R.id.detail_tabs);
		tabLayout.setupWithViewPager(viewPager);
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
		Log.d(TAG, "onOptionsItemSelected: item.getItemId() " + item.getItemId());
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	public void selectFavorite(View view) {
		String str = (String)view.getTag();
		ImageView imageView = (ImageView)view.findViewById(R.id.iv_favorites);
		String myTeamName = (String)imageView.getTag();
//		imageView.setImageResource(R.drawable.ic_favorite_fill);
		String keyFavorites = getString(R.string.key_favorites);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Set<String> setDefault = new HashSet<>();
		Set<String> favoritesSet = preferences.getStringSet(keyFavorites, setDefault);
		SharedPreferences.Editor editor = preferences.edit();
		if (favoritesSet.contains(myTeamName)) {
			favoritesSet.remove(myTeamName);
			imageView.setImageResource(R.drawable.ic_favorite);
		} else {
			favoritesSet.add(myTeamName);
			imageView.setImageResource(R.drawable.ic_favorite_fill);
		}
		editor.putStringSet(keyFavorites, favoritesSet);
		editor.apply();
	}


	class ViewPagerAdapter extends FragmentPagerAdapter {

		private final List<Fragment> mFragmentList = new ArrayList<>();
		private final List<String> mFragmentTitleList = new ArrayList<>();

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return mFragmentList.get(position);
		}

		@Override
		public int getCount() {
			return mFragmentList.size();
		}

		public void addFragment (Fragment fragment, String title) {
			mFragmentList.add(fragment);
			mFragmentTitleList.add(title);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mFragmentTitleList.get(position);
		}
	}

}

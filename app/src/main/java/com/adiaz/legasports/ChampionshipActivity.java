package com.adiaz.legasports;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class ChampionshipActivity extends AppCompatActivity {

	private static final String TAG = ChampionshipActivity.class.getSimpleName();

	private Toolbar toolbar;
	private TabLayout tabLayout;
	private ViewPager viewPager;
	private String sportTitle;

	@Override
	protected void 	onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_championship);

		String sport = getIntent().getStringExtra(MainActivity.EXTRA_SPORT_CHOSEN);
		String category = getIntent().getStringExtra(CategoriesActivity.EXTRA_CATEGORY_CHOSEN);
		sportTitle = sport + " (" + category + ")";

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		/*getSupportActionBar().setTitle(null);*/
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		/*getSupportActionBar().setDisplayShowTitleEnabled(false);

		TextView text = new TextView(this);
		text.setText(sport);
		text.setTextAppearance(this, android.R.style.TextAppearance_Material_Widget_ActionBar_Title_Inverse);
		toolbar.addView(text);*/

		CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
		collapsingToolbar.setTitle(category + " - " + sport);
		/*collapsingToolbar.setTitleEnabled(true);*/

		viewPager = (ViewPager) findViewById(R.id.viewpager);
		setupViewPager(viewPager);

		tabLayout = (TabLayout) findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(viewPager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(TAG, "onCreateOptionsMenu: sportTitle " + sportTitle);
		getMenuInflater().inflate(R.menu.menu_championship, menu);
		for(int i = 0; i < menu.size(); i++) {
			if (menu.getItem(i).getItemId()== R.id.action_favorites) {
				String key = getString(R.string.key_favorites_championship);
				if (Utils.checkIfFavoritSelected(this, sportTitle, key)) {
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
				String key = getString(R.string.key_favorites_championship);
				AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
				Drawable drawable;
				if (Utils.checkIfFavoritSelected(this, sportTitle, key)) {
					Utils.unMarkFavoriteTeam(this, sportTitle, key);
					drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite);
				} else {
					Utils.markFavoriteTeam(this, sportTitle, key);
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
		ImageView imageView = (ImageView)view.findViewById(R.id.iv_favorites);
		String myTeamName = (String)imageView.getTag();
		String keyFavorites = getString(R.string.key_favorites_teams);
		if (Utils.checkIfFavoritSelected(this, myTeamName, keyFavorites)) {
			imageView.setImageResource(R.drawable.ic_favorite);
			Utils.unMarkFavoriteTeam(this, myTeamName, keyFavorites);
		} else {
			imageView.setImageResource(R.drawable.ic_favorite_fill);
			Utils.markFavoriteTeam(this, myTeamName, keyFavorites);
		}
	}
}

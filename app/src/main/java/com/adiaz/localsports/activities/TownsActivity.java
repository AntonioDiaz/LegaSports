package com.adiaz.localsports.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.adiaz.localsports.R;
import com.adiaz.localsports.adapters.TownsAdapter;
import com.adiaz.localsports.sync.TownsAvailableCallback;
import com.adiaz.localsports.sync.retrofit.LocalSportsRestApi;
import com.adiaz.localsports.sync.retrofit.entities.town.TownRestEntity;
import com.adiaz.localsports.utilities.LocalSportsConstants;
import com.adiaz.localsports.utilities.LocalSportsUtils;
import com.adiaz.localsports.utilities.NetworkUtilities;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by adiaz on 9/1/18.
 */

public class TownsActivity extends AppCompatActivity implements TownsAvailableCallback.TownsLoadedCallback,TownsAdapter.ListItemClickListener {

    @BindView(R.id.ll_progress)
    LinearLayout llProgress;

    @BindView(R.id.rv_towns)
    RecyclerView rvTowns;

    @BindView((R.id.layout_activity_towns))
    View activitySplash;

    private List<TownRestEntity> mTownRestEntityList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_towns);
        ButterKnife.bind(this);
        if (NetworkUtilities.isNetworkAvailable(this)) {
            startLoadingTowns();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(LocalSportsConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            LocalSportsRestApi localSportsRestApi = retrofit.create(LocalSportsRestApi.class);
            Call<List<TownRestEntity>> call = localSportsRestApi.townsQuery();
            call.enqueue(new TownsAvailableCallback(this));
        } else {
            LocalSportsUtils.showNoInternetAlert(this, activitySplash);
            llProgress.setVisibility(View.INVISIBLE);
        }
    }

    private void startLoadingTowns() {
        if (llProgress != null) {
            llProgress.setVisibility(View.VISIBLE);
        }
        if (rvTowns != null) {
            rvTowns.setVisibility(View.INVISIBLE);
        }
    }

    private void endLoadingTowns() {
        if (llProgress != null) {
            llProgress.setVisibility(View.INVISIBLE);
        }
        if (rvTowns != null) {
            rvTowns.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateActivity(List<TownRestEntity> townRestEntityList) {
        // TODO: 03/08/2017 show alert when there is no towns.
        TownsAdapter townsAdapter = new TownsAdapter(townRestEntityList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTowns.setLayoutManager(linearLayoutManager);
        rvTowns.setAdapter(townsAdapter);
        mTownRestEntityList = townRestEntityList;
        endLoadingTowns();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        SharedPreferences preferences = getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LocalSportsConstants.KEY_TOWN_NAME, mTownRestEntityList.get(clickedItemIndex).getName());
        editor.putLong(LocalSportsConstants.KEY_TOWN_ID, mTownRestEntityList.get(clickedItemIndex).getId());
        editor.commit();
        finish();
        Intent intent = new Intent(this, SportsActivity.class);
        startActivity(intent);
    }
}

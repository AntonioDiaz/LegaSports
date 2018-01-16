package com.adiaz.deportelocal.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.adiaz.deportelocal.R;
import com.adiaz.deportelocal.adapters.TownsAdapter;
import com.adiaz.deportelocal.sync.retrofit.callbacks.TownsAvailableCallback;
import com.adiaz.deportelocal.sync.retrofit.DeporteLocalRestApi;
import com.adiaz.deportelocal.sync.retrofit.entities.town.TownRestEntity;
import com.adiaz.deportelocal.utilities.DeporteLocalConstants;
import com.adiaz.deportelocal.utilities.DeporteLocalUtils;
import com.adiaz.deportelocal.utilities.NetworkUtilities;
import com.google.firebase.messaging.FirebaseMessaging;

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
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        }
        ButterKnife.bind(this);
        if (NetworkUtilities.isNetworkAvailable(this)) {
            startLoadingTowns();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(DeporteLocalConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            DeporteLocalRestApi deporteLocalRestApi = retrofit.create(DeporteLocalRestApi.class);
            Call<List<TownRestEntity>> call = deporteLocalRestApi.townsQuery();
            call.enqueue(new TownsAvailableCallback(this));
        } else {
            DeporteLocalUtils.showNoInternetAlert(this, activitySplash);
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
        TownRestEntity town = mTownRestEntityList.get(clickedItemIndex);
        editor.putString(DeporteLocalConstants.KEY_TOWN_NAME, town.getName());
        editor.putLong(DeporteLocalConstants.KEY_TOWN_ID, town.getId());
        editor.putString(DeporteLocalConstants.KEY_TOWN_TOPIC, town.getFcmTopic());
        editor.commit();
        if (!TextUtils.isEmpty(town.getFcmTopic())) {
            FirebaseMessaging.getInstance().subscribeToTopic(town.getFcmTopic());
        }
        finish();
        Intent intent = new Intent(this, SportsActivity.class);
        startActivity(intent);
    }
}

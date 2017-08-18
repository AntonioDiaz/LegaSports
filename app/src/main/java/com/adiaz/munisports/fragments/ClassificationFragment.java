package com.adiaz.munisports.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.munisports.R;
import com.adiaz.munisports.activities.CompetitionActivity;
import com.adiaz.munisports.adapters.ClassificationRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/* Created by toni on 21/03/2017. */

public class ClassificationFragment extends Fragment {

	@BindView(R.id.rv_classification) RecyclerView recyclerView;
	@BindView(R.id.tv_empty_list_item) TextView tvEmptyListItem;

	public ClassificationFragment() {}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_classification, container, false);
		ButterKnife.bind(this, view);
		return view;
	}


	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ClassificationRecyclerViewAdapter adapter = new ClassificationRecyclerViewAdapter(getActivity(), CompetitionActivity.classificationList);
		recyclerView.setAdapter(adapter);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		if (CompetitionActivity.classificationList.isEmpty()) {
			recyclerView.setVisibility(View.GONE);
			tvEmptyListItem.setVisibility(View.VISIBLE);
		} else {
			recyclerView.setVisibility(View.VISIBLE);
			tvEmptyListItem.setVisibility(View.GONE);
		}
	}
}

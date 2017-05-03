package com.adiaz.legasports.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiaz.legasports.R;
import com.adiaz.legasports.activities.CompetitionActivity;
import com.adiaz.legasports.adapters.ClassificationRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/* Created by toni on 21/03/2017. */

public class ClassificationFragment extends Fragment {

	@BindView(R.id.rv_classification) RecyclerView recyclerView;

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
		ClassificationRecyclerViewAdapter adapter = new ClassificationRecyclerViewAdapter(CompetitionActivity.classificationList);
		recyclerView.setAdapter(adapter);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

	}
}

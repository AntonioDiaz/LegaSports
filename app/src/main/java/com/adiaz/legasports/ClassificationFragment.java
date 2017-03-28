package com.adiaz.legasports;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* Created by toni on 21/03/2017. */

public class ClassificationFragment extends Fragment {

	public ClassificationFragment() {}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_classification, container, false);
	}


	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ClassificationRecyclerViewAdapter adapter = new ClassificationRecyclerViewAdapter(generateClassificationSample());

		RecyclerView recyclerView = (RecyclerView)getActivity().findViewById(R.id.rv_classification);
		recyclerView.setAdapter(adapter);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

	}

	public static final List<String[]> generateClassificationSample() {
		List<String[]> myList = new ArrayList<>();
		myList.add(new String[]{"", "Equipo", "PJ", "PG", "PE", "PP", "F", "C", "PF", "PC", "PS", "PT", "Sanciones"});
		myList.add(new String[]{"1", "AD CEPA SPORT", "19", "16", "1", "2", "76", "18", "0", "0", "0", "49", "0"});
		myList.add(new String[]{"2", "CD OLIMPICO PIZARRO", "19", "14", "1", "4", "68", "24", "0", "0", "0", "43", "0"});
		myList.add(new String[]{"3", "AC RUBEN´S", "19", "11", "2", "6", "41", "31", "0", "0", "0", "35", "0"});
		myList.add(new String[]{"4", "PAVIMARCON", "19", "11", "1", "7", "68", "39", "0", "0", "0", "34", "0"});
		myList.add(new String[]{"5", "BALOMPEDICA CF", "19", "10", "1", "8", "61", "56", "0", "0", "0", "31", "0"});
		myList.add(new String[]{"6", "RACING SPORT", "19", "9", "2", "8", "51", "46", "0", "0", "0", "29", "0"});
		myList.add(new String[]{"7", "CD ATLETICO 2000", "19", "8", "0", "11", "46", "57", "0", "0", "0", "24", "0"});
		myList.add(new String[]{"8", "NEW UNITED", "17", "7", "2", "8", "53", "46", "0", "0", "0", "23", "0"});
		myList.add(new String[]{"9", "NUEVO ROMANS", "18", "7", "2", "9", "41", "66", "0", "0", "0", "23", "0"});
		myList.add(new String[]{"10", "ABURRUCA TEAM", "18", "6", "4", "8", "40", "49", "0", "0", "0", "22", "0"});
		myList.add(new String[]{"11", "UNION DEPORTIVA LEGANES", "19", "4", "1", "14", "36", "68", "0", "0", "0", "13", "0"});
		myList.add(new String[]{"12", "ADCR ATLETICO LEGANES 78", "19", "0", "1", "18", "17", "98", "0", "0", "0", "1", "0"});
		return myList;
	}

	public static final List<String> generateTeamsList() {
		List<String> teams = new ArrayList<>();
		List<String[]> strings = generateClassificationSample();
		for (String[] string : strings) {
			teams.add(string[1]);
		}
		Collections.sort(teams);
		return teams;
	}
}

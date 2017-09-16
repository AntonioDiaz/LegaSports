package com.adiaz.munisports.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.adiaz.munisports.R;
import com.adiaz.munisports.activities.CompetitionActivity;
import com.adiaz.munisports.adapters.CalendarAdapter;
import com.adiaz.munisports.entities.Match;
import com.adiaz.munisports.utilities.MenuActionsUtils;
import com.adiaz.munisports.utilities.NonScrollExpandableListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



/* Created by toni on 21/03/2017. */

public class CalendarFragment extends Fragment {

	//private static final String TAG = CalendarAdapter.class.getSimpleName();

	@BindView(R.id.elv_jornadas) NonScrollExpandableListView nonScrollExpandableListView;
	@BindView(R.id.tv_empty_list_item) TextView tvEmptyListItem;

	public CalendarFragment() { }

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_calendar, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		List<List<Match>> weeks = CompetitionActivity.mWeeks;
		CalendarAdapter calendarAdapter = new CalendarAdapter(getActivity(), weeks);
		nonScrollExpandableListView.setAdapter(calendarAdapter);
		nonScrollExpandableListView.setEmptyView(tvEmptyListItem);
		calendarAdapter.notifyDataSetChanged();
		registerForContextMenu(nonScrollExpandableListView);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
		int type = ExpandableListView.getPackedPositionType(info.packedPosition);
		if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			menu.setHeaderTitle(getString(R.string.menu_match_title));
			MenuInflater menuInflater = getActivity().getMenuInflater();
			menuInflater.inflate(R.menu.menu_match, menu);
		}
	}


	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ExpandableListView.ExpandableListContextMenuInfo menuInfo = (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
		View targetView = menuInfo.targetView;
		Match match	 = (Match)targetView.getTag();
		switch (item.getItemId()) {
			case R.id.action_add_calendar:
				MenuActionsUtils.addMatchEvent(this.getActivity(), match, CompetitionActivity.mCompetition);
				break;
			case R.id.action_view_map:
				MenuActionsUtils.showMatchLocation(this.getActivity(), match);
				break;
			case R.id.action_share:
				MenuActionsUtils.shareMatchDetails(this.getActivity(), match, CompetitionActivity.mCompetition);
				break;
			case R.id.action_notify_error:
				//Match zipotegato = Match.builder().setName("zipotegato").build();
				//String name = zipotegato.name();
				//Match.Builder zipotegago = Match.builder().setName("zipotegago").setYear(2109).build();

				SendIssueDialogFragment dialog = SendIssueDialogFragment.newInstance(match, CompetitionActivity.mCompetition);
				dialog.show(getFragmentManager(), "dialog");
				break;
		}
		return super.onContextItemSelected(item);
	}
}

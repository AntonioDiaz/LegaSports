package com.adiaz.munisports.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adiaz.munisports.R;
import com.adiaz.munisports.entities.TeamEntity;
import com.adiaz.munisports.entities.TeamMatchEntity;
import com.adiaz.munisports.utilities.MuniSportsConstants;
import com.adiaz.munisports.utilities.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/* Created by toni on 23/03/2017. */
public class TeamsAdapter extends BaseExpandableListAdapter {

	// private static final String TAG = TeamsAdapter.class.getSimpleName();

	private Context mContext;
	private List<TeamEntity> teamsList;
	private String idCompetitionServer;

	@Nullable @BindView(R.id.ll_matchinfo) LinearLayout llMatchInfo;
	@Nullable @BindView(R.id.tv_undefined) TextView tvUndefined;
	@Nullable @BindView(R.id.tv_local) TextView tvLocal;
	@Nullable @BindView(R.id.tv_visitor) TextView tvVisitor;
	@Nullable @BindView(R.id.tv_local_score) TextView tvLocalScore;
	@Nullable @BindView(R.id.tv_visitor_score) TextView tvVisitorScore;
	@Nullable @BindView(R.id.tv_heading) TextView tvHeading;
	@Nullable @BindView(R.id.iv_favorites) ImageView imageView;
	@Nullable @BindView(R.id.tv_week) TextView tvWeek;

	public TeamsAdapter(Context mContext, List<TeamEntity> teamsList, String idCompetitionServer) {
		this.mContext = mContext;
		this.teamsList = teamsList;
		this.idCompetitionServer = idCompetitionServer;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		TeamEntity teamEntity = teamsList.get(groupPosition);
		return teamEntity.getMatches()[childPosition];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.listitem_child_teams, null);
		}
		ButterKnife.bind(this, view);
		tvWeek.setText(Integer.toString(childPosition + 1));
		TeamMatchEntity teamMatchEntity = (TeamMatchEntity) getChild(groupPosition, childPosition);
		if (teamMatchEntity != null) {
			/*Check if the time have rest this week. */
			if (teamMatchEntity.getOpponent().equals(MuniSportsConstants.UNDEFINDED_FIELD)) {
				llMatchInfo.setVisibility(View.GONE);
				tvUndefined.setVisibility(View.VISIBLE);
				tvUndefined.setText(mContext.getString(R.string.rest_team));
			} else {
				String teamEntity = getGroup(groupPosition);
				llMatchInfo.setVisibility(View.VISIBLE);
				tvUndefined.setVisibility(View.GONE);
				String localStr;
				String visitorStr;
				String localScoreStr;
				String visitorScoreStr;
				if (teamMatchEntity.isLocal()) {
					localStr = teamEntity;
					localScoreStr = teamMatchEntity.getTeamScore() == null ? "_" : teamMatchEntity.getTeamScore().toString();
					visitorStr = teamMatchEntity.getOpponent();
					visitorScoreStr = teamMatchEntity.getOpponentScore() == null ? "_" : teamMatchEntity.getOpponentScore().toString();
/*					tvLocal.setTypeface(null, Typeface.BOLD);
					tvLocalScore.setTypeface(null, Typeface.BOLD);
					tvVisitor.setTypeface(null, Typeface.NORMAL);
					tvVisitorScore.setTypeface(null, Typeface.NORMAL);*/
				} else {
					visitorStr = teamEntity;
					localStr = teamMatchEntity.getOpponent();
					localScoreStr = teamMatchEntity.getOpponentScore() == null ? "_" : teamMatchEntity.getOpponentScore().toString();
					visitorScoreStr = teamMatchEntity.getTeamScore() == null ? "_" : teamMatchEntity.getTeamScore().toString();
/*
					tvLocal.setTypeface(null, Typeface.NORMAL);
					tvLocalScore.setTypeface(null, Typeface.NORMAL);
					tvVisitor.setTypeface(null, Typeface.BOLD);
					tvVisitorScore.setTypeface(null, Typeface.BOLD);
*/
				}
				tvLocal.setText(localStr);
				tvLocalScore.setText(localScoreStr);
				tvVisitor.setText(visitorStr);
				tvVisitorScore.setText(visitorScoreStr);
			}
		} else {
			llMatchInfo.setVisibility(View.GONE);
			tvUndefined.setVisibility(View.VISIBLE);
			tvUndefined.setText(mContext.getString(R.string.undefined_week));
		}
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return teamsList.get(groupPosition).getMatches().length;
	}

	@Override
	public String getGroup(int groupPosition) {
		return teamsList.get(groupPosition).getTeamName();
	}

	@Override
	public int getGroupCount() {
		return teamsList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isLastChild, View view, ViewGroup parent) {
		String teamEntity = getGroup(groupPosition);
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.listitem_header_teams, null);
		}
		ButterKnife.bind(this, view);
		tvHeading.setText(teamEntity);
		String key = MuniSportsConstants.KEY_FAVORITES_TEAMS;
		String teamName = Utils.generateTeamKey(teamEntity, idCompetitionServer);
		if (Utils.checkIfFavoritSelected(mContext, teamName, key)) {
			imageView.setImageResource(R.drawable.ic_favorite_fill);
		} else {
			imageView.setImageResource(R.drawable.ic_favorite);
		}
		imageView.setTag(teamEntity);
		return view;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
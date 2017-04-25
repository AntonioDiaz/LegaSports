package com.adiaz.legasports.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adiaz.legasports.R;
import com.adiaz.legasports.utilities.Utils;
import com.adiaz.legasports.entities.TeamEntity;
import com.adiaz.legasports.entities.TeamMatchEntity;

import java.util.List;

/* Created by toni on 23/03/2017. */

public class TeamsAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private List<TeamEntity> teams;

	public TeamsAdapter(Context mContext, List<TeamEntity> teams) {
		this.mContext = mContext;
		this.teams = teams;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		TeamEntity teamEntity = teams.get(groupPosition);
		TeamMatchEntity teamMatchEntity = teamEntity.getMatches().get(childPosition);
		return teamMatchEntity;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
		TeamEntity teamEntity = (TeamEntity) getGroup(groupPosition);
		TeamMatchEntity teamMatchEntity = (TeamMatchEntity) getChild(groupPosition, childPosition);
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.listitem_child_teams, null);
		}
		TextView tvLocal = (TextView) view.findViewById(R.id.tv_local);
		TextView tvVisitor = (TextView) view.findViewById(R.id.tv_visitor);
		TextView tvLocalScore = (TextView) view.findViewById(R.id.tv_local_score);
		TextView tvVisitorScore = (TextView) view.findViewById(R.id.tv_visitor_score);
		String localStr;
		String visitorStr;
		String localScoreStr;
		String visitorScoreStr;
		if (teamMatchEntity.isLocal()) {
			localStr = teamEntity.getTeamName();
			localScoreStr = teamMatchEntity.getTeamScore()==null?"_":teamMatchEntity.getTeamScore().toString();
			visitorStr = teamMatchEntity.getOpponent();
			visitorScoreStr = teamMatchEntity.getOpponentScore()==null?"_":teamMatchEntity.getOpponentScore().toString();
			tvLocal.setTypeface(null, Typeface.BOLD);
			tvLocalScore.setTypeface(null, Typeface.BOLD);
			tvVisitor.setTypeface(null, Typeface.NORMAL);
			tvVisitorScore.setTypeface(null, Typeface.NORMAL);

		} else {
			localStr = teamMatchEntity.getOpponent();
			localScoreStr = teamMatchEntity.getOpponentScore()==null?"_":teamMatchEntity.getOpponentScore().toString();
			visitorStr = teamEntity.getTeamName();
			visitorScoreStr = teamMatchEntity.getTeamScore()==null?"_":teamMatchEntity.getTeamScore().toString();
			tvLocal.setTypeface(null, Typeface.NORMAL);
			tvLocalScore.setTypeface(null, Typeface.NORMAL);
			tvVisitor.setTypeface(null, Typeface.BOLD);
			tvVisitorScore.setTypeface(null, Typeface.BOLD);

		}
		tvLocal.setText(localStr);
		tvLocalScore.setText(localScoreStr);
		tvVisitor.setText(visitorStr);
		tvVisitorScore.setText(visitorScoreStr);

		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		TeamEntity teamEntity = teams.get(groupPosition);
		return teamEntity.getMatches().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return teams.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return teams.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isLastChild, View view, ViewGroup parent) {
		TeamEntity teamEntity = (TeamEntity) getGroup(groupPosition);
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.listitem_header_teams, null);
		}
		TextView textView = (TextView) view.findViewById(R.id.tv_heading);
		ImageView imageView = (ImageView) view.findViewById(R.id.iv_favorites);

		textView.setText(teamEntity.getTeamName());
		String key = mContext.getString(R.string.key_favorites_teams);
		if (Utils.checkIfFavoritSelected(mContext, teamEntity.getTeamName(), key)) {
			imageView.setImageResource(R.drawable.ic_favorite_fill);
		} else {
			imageView.setImageResource(R.drawable.ic_favorite);
		}
		imageView.setTag(teamEntity.getTeamName());
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
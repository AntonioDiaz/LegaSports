package com.adiaz.legasports.adapters;

/* Created by toni on 29/03/2017. */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adiaz.legasports.R;
import com.adiaz.legasports.entities.TeamEntity;
import com.adiaz.legasports.entities.TeamMatchEntity;
import com.adiaz.legasports.utilities.LegaSportsConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteTeamAdapter extends RecyclerView.Adapter<FavoriteTeamAdapter.ViewHolder> {

	private Context context;
	private TeamEntity teamEntity;

	public FavoriteTeamAdapter(Context context) {
		this.context = context;
	}

	public void setTeamEntity(TeamEntity teamEntity) {
		this.teamEntity = teamEntity;
		notifyDataSetChanged();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.listitem_favorite_team_calendar, parent, false);
		ViewHolder viewHolder = new ViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		String jornadaStr = context.getString(R.string.jornada_header, position + 1);
		holder.tvJornada.setText(jornadaStr);
		TeamMatchEntity teamMatchEntity = teamEntity.getMatches().get(position);
		String dateStr = "";
		if (teamMatchEntity.getDate()!=null) {
			DateFormat dateFormat = new SimpleDateFormat(LegaSportsConstants.DATE_FORMAT);
			dateStr = dateFormat.format(teamMatchEntity.getDate());
		}
		holder.tvDate.setText(dateStr);
		holder.tvPlace.setText(teamMatchEntity.getPlace());
		if (teamMatchEntity.isLocal()) {
			holder.tvTeamLocal.setText(teamEntity.getTeamName());
			holder.tvTeamLocalScore.setText(teamMatchEntity.getTeamScore().toString());
			holder.tvTeamVisitor.setText(teamMatchEntity.getOpponent());
			holder.tvTeamVisitorScore.setText(teamMatchEntity.getOpponentScore().toString());
		} else {
			holder.tvTeamLocal.setText(teamMatchEntity.getOpponent());
			holder.tvTeamLocalScore.setText(teamMatchEntity.getOpponentScore().toString());
			holder.tvTeamVisitor.setText(teamEntity.getTeamName());
			holder.tvTeamVisitorScore.setText(teamMatchEntity.getTeamScore().toString());
		}
		holder.ivLocation.setTag(teamMatchEntity);
		holder.ivCalendar.setTag(teamMatchEntity);
		holder.ivShare.setTag(teamMatchEntity);
	}

	@Override
	public int getItemCount() {
		return teamEntity == null ? 0 : teamEntity.getMatches().size();
	}


	public class ViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.tv_fav_team_jornada) TextView tvJornada;
		@BindView(R.id.tv_fav_team_date) TextView tvDate;
		@BindView(R.id.tv_fav_team_place) TextView tvPlace;
		@BindView(R.id.tv_fav_team_local) TextView tvTeamLocal;
		@BindView(R.id.tv_fav_team_visitor) TextView tvTeamVisitor;
		@BindView(R.id.tv_fav_team_local_score) TextView tvTeamLocalScore;
		@BindView(R.id.tv_fav_team_visitor_score) TextView tvTeamVisitorScore;
		@BindView(R.id.iv_fav_team_visitor_calendar) ImageView ivCalendar;
		@BindView(R.id.iv_fav_team_location) ImageView ivLocation;
		@BindView(R.id.iv_fav_team_visitor_share) ImageView ivShare;

		public ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}
	}
}

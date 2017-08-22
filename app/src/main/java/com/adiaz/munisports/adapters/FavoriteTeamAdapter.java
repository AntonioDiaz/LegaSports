package com.adiaz.munisports.adapters;

/* Created by toni on 29/03/2017. */

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adiaz.munisports.R;
import com.adiaz.munisports.entities.TeamEntity;
import com.adiaz.munisports.entities.TeamMatchEntity;
import com.adiaz.munisports.utilities.MuniSportsConstants;

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
		TeamMatchEntity teamMatchEntity = teamEntity.getMatches()[position];
		if (teamMatchEntity==null || teamMatchEntity.getOpponent().equals(MuniSportsConstants.UNDEFINDED_FIELD)) {
			holder.tvJornadaEmpty.setText(jornadaStr);
			holder.clMatchDetails.setVisibility(View.GONE);
			holder.clMatchEmpty.setVisibility(View.VISIBLE);
			if (teamMatchEntity==null) {
				holder.tvFavTeamUndefined.setText(context.getString(R.string.undefined_match));
			} else {
				holder.tvFavTeamUndefined.setText(context.getString(R.string.rest_team));
			}
		} else {
			holder.tvJornada.setText(jornadaStr);
			holder.clMatchDetails.setVisibility(View.VISIBLE);
			holder.clMatchEmpty.setVisibility(View.GONE);
			String dateStr = context.getString(R.string.undefined_date);
			if (teamMatchEntity.getDate()!=null && teamMatchEntity.getDate().getTime()!=0) {
				DateFormat dateFormat = new SimpleDateFormat(MuniSportsConstants.DATE_FORMAT);
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

	}

	@Override
	public int getItemCount() {
		return teamEntity == null ? 0 : teamEntity.getMatches().length;
	}


	public class ViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.cl_match_details) ConstraintLayout clMatchDetails;
		@BindView(R.id.cl_match_empty) ConstraintLayout clMatchEmpty;
		@BindView(R.id.tv_fav_team_jornada) TextView tvJornada;
		@BindView(R.id.tv_fav_team_jornada_empty) TextView tvJornadaEmpty;
		@BindView(R.id.tv_fav_team_date) TextView tvDate;
		@BindView(R.id.tv_fav_team_place) TextView tvPlace;
		@BindView(R.id.tv_fav_team_local) TextView tvTeamLocal;
		@BindView(R.id.tv_fav_team_visitor) TextView tvTeamVisitor;
		@BindView(R.id.tv_fav_team_local_score) TextView tvTeamLocalScore;
		@BindView(R.id.tv_fav_team_visitor_score) TextView tvTeamVisitorScore;
		@BindView(R.id.iv_fav_team_visitor_calendar) ImageView ivCalendar;
		@BindView(R.id.iv_fav_team_location) ImageView ivLocation;
		@BindView(R.id.iv_fav_team_visitor_share) ImageView ivShare;
		@BindView(R.id.tv_fav_team_undefined) TextView tvFavTeamUndefined;
		public ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}
	}
}

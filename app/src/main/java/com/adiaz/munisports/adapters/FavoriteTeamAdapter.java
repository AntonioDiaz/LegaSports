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
import com.adiaz.munisports.entities.Team;
import com.adiaz.munisports.entities.TeamMatch;
import com.adiaz.munisports.utilities.MuniSportsConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteTeamAdapter extends RecyclerView.Adapter<FavoriteTeamAdapter.ViewHolder> {

	private Context context;
	private Team team;

	public FavoriteTeamAdapter(Context context) {
		this.context = context;
	}

	public void setTeam(Team team) {
		this.team = team;
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
		TeamMatch teamMatch = team.getMatches()[position];
		if (teamMatch ==null || teamMatch.getOpponent().equals(MuniSportsConstants.UNDEFINDED_FIELD)) {
			holder.tvJornadaEmpty.setText(jornadaStr);
			holder.clMatchDetails.setVisibility(View.GONE);
			holder.clMatchEmpty.setVisibility(View.VISIBLE);
			if (teamMatch ==null) {
				holder.tvFavTeamUndefined.setText(context.getString(R.string.undefined_week));
			} else {
				holder.tvFavTeamUndefined.setText(context.getString(R.string.rest_team));
			}
		} else {
			holder.tvJornada.setText(jornadaStr);
			holder.clMatchDetails.setVisibility(View.VISIBLE);
			holder.clMatchEmpty.setVisibility(View.GONE);
			String dateStr = context.getString(R.string.undefined_date);
			if (teamMatch.getDate()!=null && teamMatch.getDate().getTime()!=0) {
				DateFormat dateFormat = new SimpleDateFormat(MuniSportsConstants.DATE_FORMAT);
				dateStr = dateFormat.format(teamMatch.getDate());
			}
			holder.tvDate.setText(dateStr);
			holder.tvPlace.setText(teamMatch.getPlaceName());
			if (teamMatch.isLocal()) {
				holder.tvTeamLocal.setText(team.getTeamName());
				holder.tvTeamLocalScore.setText(teamMatch.getTeamScore().toString());
				holder.tvTeamVisitor.setText(teamMatch.getOpponent());
				holder.tvTeamVisitorScore.setText(teamMatch.getOpponentScore().toString());
			} else {
				holder.tvTeamLocal.setText(teamMatch.getOpponent());
				holder.tvTeamLocalScore.setText(teamMatch.getOpponentScore().toString());
				holder.tvTeamVisitor.setText(team.getTeamName());
				holder.tvTeamVisitorScore.setText(teamMatch.getTeamScore().toString());
			}
			holder.ivLocation.setTag(teamMatch);
			holder.ivCalendar.setTag(teamMatch);
			holder.ivShare.setTag(teamMatch);
		}

	}

	@Override
	public int getItemCount() {
		return team == null ? 0 : team.getMatches().length;
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

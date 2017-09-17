package com.adiaz.munisports.adapters;

/* Created by toni on 29/03/2017. */

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.adiaz.munisports.R;
import com.adiaz.munisports.entities.Match;
import com.adiaz.munisports.entities.Team;
import com.adiaz.munisports.utilities.MuniSportsConstants;

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
		Match match = team.getMatches()[position];
		if (match == null
				|| match.teamLocal().equals(MuniSportsConstants.UNDEFINDED_FIELD)
				|| match.teamVisitor().equals(MuniSportsConstants.UNDEFINDED_FIELD)) {
			holder.tvJornadaEmpty.setText(jornadaStr);
			holder.clMatchDetails.setVisibility(View.GONE);
			holder.clMatchEmpty.setVisibility(View.VISIBLE);
			if (match == null) {
				holder.tvFavTeamUndefined.setText(context.getString(R.string.undefined_week));
			} else {
				holder.tvFavTeamUndefined.setText(context.getString(R.string.rest_team));
			}
		} else {
			holder.tvJornada.setText(jornadaStr);
			holder.clMatchDetails.setVisibility(View.VISIBLE);
			holder.clMatchEmpty.setVisibility(View.GONE);
			holder.tvDate.setText(match.obtainDateStr(context));
			holder.tvPlace.setText(match.obtainCenterNameFull(context));
			holder.tvTeamLocal.setText(match.teamLocal());
			holder.tvTeamLocalScore.setText(match.scoreLocal().toString());
			holder.tvTeamVisitor.setText(match.teamVisitor());
			holder.tvTeamVisitorScore.setText(match.scoreVisitor().toString());
			holder.ibLocation.setTag(match);
			holder.ibCalendar.setTag(match);
			holder.ibShare.setTag(match);
			holder.ibIssue.setTag(match);
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
		@BindView(R.id.ib_calendar) ImageButton ibCalendar;
		@BindView(R.id.ib_location)	ImageButton ibLocation;
		@BindView(R.id.ib_share) ImageButton ibShare;
		@BindView(R.id.ib_issue) ImageButton ibIssue;
		@BindView(R.id.tv_fav_team_undefined) TextView tvFavTeamUndefined;
		public ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}
	}
}

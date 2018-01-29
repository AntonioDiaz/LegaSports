package com.adiaz.deportelocal.adapters;

/* Created by toni on 29/03/2017. */

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.adiaz.deportelocal.R;
import com.adiaz.deportelocal.entities.Match;
import com.adiaz.deportelocal.entities.Team;
import com.adiaz.deportelocal.utilities.DeporteLocalConstants;
import com.adiaz.deportelocal.utilities.DeporteLocalUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteTeamAdapter extends RecyclerView.Adapter<FavoriteTeamAdapter.ViewHolder> {

	private static final String TAG = FavoriteTeamAdapter.class.getSimpleName();
	private Context context;
	private Team team;
	private ListItemClickListener mOnClickListener;

	public FavoriteTeamAdapter(Context context, ListItemClickListener listener) {
		this.context = context;
		this.mOnClickListener = listener;
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
        Match match = team.getMatches()[position];
        String jornadaStr = context.getString(R.string.jornada_header, position + 1);
        holder.clMatchDetails.setVisibility(View.GONE);
        holder.clMatchEmpty.setVisibility(View.GONE);
        if (match == null) {
            holder.clMatchEmpty.setVisibility(View.VISIBLE);
            holder.tvJornadaEmpty.setText(jornadaStr);
        } else {
            holder.clMatchDetails.setVisibility(View.VISIBLE);
            holder.tvJornada.setText(jornadaStr);
            String scoreLocal = DeporteLocalConstants.UNDEFINDED_FIELD;
            String scoreVisitor = DeporteLocalConstants.UNDEFINDED_FIELD;
            String courtStr = DeporteLocalConstants.UNDEFINDED_FIELD;
            String dateStr = DeporteLocalUtils.matchStateName(context, match.state());
            holder.tvTeamLocal.setText(match.teamLocal());
            holder.tvTeamVisitor.setText(match.teamVisitor());
            if (match.state()== DeporteLocalConstants.STATE_PLAYED) {
                scoreLocal = match.scoreLocal().toString();
                scoreVisitor = match.scoreVisitor().toString();
                dateStr = match.obtainDateStr(context);
                courtStr = match.obtainCenterNameFull(context);
            } else if (match.state()== DeporteLocalConstants.STATE_PENDING) {
                dateStr = match.obtainDateStr(context);
                courtStr = match.obtainCenterNameFull(context);
            } else if (match.state()== DeporteLocalConstants.STATE_CANCELED) {

            } else if (match.state()== DeporteLocalConstants.STATE_RESTING) {
                holder.tvTeamVisitor.setText(DeporteLocalConstants.UNDEFINDED_FIELD);
            }

            holder.tvPlace.setText(courtStr);
            holder.tvDate.setText(dateStr);
            holder.tvTeamLocalScore.setText(scoreLocal);
            holder.tvTeamVisitorScore.setText(scoreVisitor);

        /*
            holder.tvFavTeamUndefined.setText(context.getString(R.string.undefined_week));
            /*
            String state = DeporteLocalUtils.matchStateName(context, match.state());



            holder.tvMatchState.setText(state);
			/*

			//holder.tvPlace.setText(match.obtainCenterNameFull(context));
			holder.tvTeamLocal.setText(match.teamLocal());
            if (match.state()== DeporteLocalConstants.STATE_PLAYED) {
                holder.tvTeamLocalScore.setText(match.scoreLocal().toString());
                holder.tvTeamVisitorScore.setText(match.scoreVisitor().toString());
                holder.tvMatchState.setText("");
            } else {
				holder.tvTeamLocalScore.setText(DeporteLocalConstants.UNDEFINDED_FIELD);
				holder.tvTeamVisitorScore.setText(DeporteLocalConstants.UNDEFINDED_FIELD);
                holder.tvMatchState.setText(DeporteLocalUtils.matchStateName(context, match.state()));
			}
			holder.tvTeamVisitor.setText(match.teamVisitor());
			holder.ibLocation.setTag(match);
			holder.ibCalendar.setTag(match);
			holder.ibShare.setTag(match);
			holder.ibIssue.setTag(match);

			/**
            if (!match.isDateDefined()) {
                holder.ibCalendar.setVisibility(View.GONE);
			} else {
                holder.ibCalendar.setVisibility(View.VISIBLE);
            }
			if (!match.isCourtDefinded(context)) {
                holder.ibLocation.setVisibility(View.GONE);
			}
             */
        }
		//}

	}

	@Override
	public int getItemCount() {
		return team == null ? 0 : team.getMatches().length;
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
        /*
		@BindView(R.id.tv_match_state) TextView tvMatchState;
		@BindView(R.id.ib_calendar) ImageButton ibCalendar;
		@BindView(R.id.ib_location)	ImageButton ibLocation;
		@BindView(R.id.ib_share) ImageButton ibShare;
		@BindView(R.id.ib_issue) ImageButton ibIssue;
		@BindView(R.id.tv_fav_team_undefined) TextView tvFavTeamUndefined;
		*/
		public ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
			view.setOnClickListener(this);
		}

        @Override
        public void onClick(View view) {
            mOnClickListener.onListItemClick(getAdapterPosition());
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}

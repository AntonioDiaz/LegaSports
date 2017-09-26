package com.adiaz.localsports.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.localsports.R;
import com.adiaz.localsports.entities.TeamFavorite;
import com.adiaz.localsports.utilities.LocalSportsUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/** Created by toni on 29/03/2017. */

public class FavoritesTeamsAdapter extends RecyclerView.Adapter<FavoritesTeamsAdapter.ViewHolder> {

	List<TeamFavorite> teamsFavorites;
	Context context;
	private ListItemClickListener listItemClickListener;

	public FavoritesTeamsAdapter(Context context, ListItemClickListener listItemClickListener) {
		this.context = context;
		this.listItemClickListener = listItemClickListener;
	}

	public void setTeamsFavorites(List<TeamFavorite> teamsFavorites) {
		this.teamsFavorites = teamsFavorites;
		notifyDataSetChanged();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.listitem_favorites, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		String teamName = teamsFavorites.get(position).getName();
		String sportTag = teamsFavorites.get(position).getSportTag();
		String categoryTag = teamsFavorites.get(position).getCategoryTag();
		String competitionName = teamsFavorites.get(position).getCompetitionName();
		String subtitle = LocalSportsUtils.getStringResourceByName(context, sportTag);
		subtitle += " - " + LocalSportsUtils.getStringResourceByName(context, categoryTag);
		holder.tvTitle.setText(teamName);
		holder.tvSubTitle01.setText(competitionName);
		holder.tvSubTitle02.setText(subtitle);
		holder.cvCompetitions.setTag(teamName);
	}

	@Override
	public int getItemCount() {
		return teamsFavorites.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@BindView(R.id.tv_fav_title) TextView tvTitle;
		@BindView(R.id.tv_fav_subtitle01) TextView tvSubTitle01;
		@BindView(R.id.tv_fav_subtitle02) TextView tvSubTitle02;
		@BindView(R.id.cv_fav_competition) CardView cvCompetitions;
		public ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
			cvCompetitions.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			listItemClickListener.onListItemClick(getAdapterPosition());
		}
	}

	public interface ListItemClickListener {
		void onListItemClick(int clickedItemIndex);
	}
}

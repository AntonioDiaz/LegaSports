package com.adiaz.legasports.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.legasports.R;
import com.adiaz.legasports.entities.TeamFavoriteEntity;
import com.adiaz.legasports.utilities.Utils;

import java.util.List;

/** Created by toni on 29/03/2017. */

public class FavoritesTeamsAdapter extends RecyclerView.Adapter<FavoritesTeamsAdapter.ViewHolder> {

	List<TeamFavoriteEntity> teamsFavorites;
	Context context;
	private ListItemClickListener listItemClickListener;

	public FavoritesTeamsAdapter(Context context, ListItemClickListener listItemClickListener) {
		this.context = context;
		this.listItemClickListener = listItemClickListener;
	}

	public void setTeamsFavorites(List<TeamFavoriteEntity> teamsFavorites) {
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
		String subtitle = Utils.getStringResourceByName(context, sportTag);
		subtitle += " - " + Utils.getStringResourceByName(context, categoryTag);
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
		private TextView tvTitle;
		private TextView tvSubTitle02;
		private TextView tvSubTitle01;
		private CardView cvCompetitions;
		public ViewHolder(View itemView) {
			super(itemView);
			tvTitle = (TextView) itemView.findViewById(R.id.tv_fav_title);
			tvSubTitle01 = (TextView) itemView.findViewById(R.id.tv_fav_subtitle01);
			tvSubTitle02 = (TextView) itemView.findViewById(R.id.tv_fav_subtitle02);
			cvCompetitions = (CardView) itemView.findViewById(R.id.cv_fav_competition);
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

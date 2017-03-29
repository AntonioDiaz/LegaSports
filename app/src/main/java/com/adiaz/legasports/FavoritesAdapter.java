package com.adiaz.legasports;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.legasports.entities.TeamEntity;

import java.util.List;

/** Created by toni on 29/03/2017. */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolderFavorites> {

	List<TeamEntity> teamsFavorites;
	Context context;

	public FavoritesAdapter(Context context) {
		this.context = context;
	}

	public void setTeamsFavorites(List<TeamEntity> teamsFavorites) {
		this.teamsFavorites = teamsFavorites;
		notifyDataSetChanged();
	}

	@Override
	public ViewHolderFavorites onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.listitem_favorites, parent, false);
		return new ViewHolderFavorites(view);
	}

	@Override
	public void onBindViewHolder(ViewHolderFavorites holder, int position) {
		String teamName = teamsFavorites.get(position).getTeamName();
		holder.tvFavoriteName.setText(teamName);
		holder.tvFavoritesSportCategory.setText(context.getString(R.string.example_sport_category));
		holder.cvFavoriteId.setTag((String)teamName);
		}

	@Override
	public int getItemCount() {
		return teamsFavorites.size();
	}


	class ViewHolderFavorites extends RecyclerView.ViewHolder {

		private TextView tvFavoriteName;
		private TextView tvFavoritesSportCategory;
		private CardView cvFavoriteId;

		public ViewHolderFavorites(View itemView) {
			super(itemView);
			tvFavoriteName = (TextView)itemView.findViewById(R.id.tv_fav_name);
			tvFavoritesSportCategory = (TextView)itemView.findViewById(R.id.tv_fav_sport_category);
			cvFavoriteId = (CardView)itemView.findViewById(R.id.cv_fav_card);
		}
	}
}

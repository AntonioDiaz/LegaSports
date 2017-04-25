package com.adiaz.legasports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.legasports.R;

import java.util.List;

/* Created by toni on 31/03/2017. */

public class FavoritesChampionShipAdapter extends RecyclerView.Adapter<FavoritesChampionShipAdapter.ViewHolder> {

	private Context context;
	private List<String> championshipFavorites;


	public FavoritesChampionShipAdapter(Context context) {
		this.context = context;
	}

	public void setChampionshipFavorites(List<String> championshipFavorites) {
		this.championshipFavorites = championshipFavorites;
		notifyDataSetChanged();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.listitem_favorites_championship, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		String championshipName = championshipFavorites.get(position);
		holder.tvFavChampionshipName.setText(championshipName);
		holder.vFavChampionship.setTag(championshipName);
	}

	@Override
	public int getItemCount() {
		return championshipFavorites.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		private TextView tvFavChampionshipName;
		private View vFavChampionship;
//		private TextView tvFavSportName;

		public ViewHolder(View itemView) {
			super(itemView);
			tvFavChampionshipName = (TextView) itemView.findViewById(R.id.tv_fav_championship_name);
//			tvFavSportName = (TextView) itemView.findViewById(R.id.tv_fav_championship_sport);
			vFavChampionship = itemView.findViewById(R.id.cv_fav_championship);
		}
	}


}

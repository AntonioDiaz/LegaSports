package com.adiaz.legasports.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.legasports.R;
import com.adiaz.legasports.entities.CompetitionEntity;
import com.adiaz.legasports.utilities.Utils;

import java.util.List;

/* Created by toni on 31/03/2017. */

public class FavoritesCompetitionsAdapter extends RecyclerView.Adapter<FavoritesCompetitionsAdapter.ViewHolder> {

	private Context context;
	private ListItemClickListener listItemClickListener;

	private List<CompetitionEntity> competitionsFavorites;

	public FavoritesCompetitionsAdapter(Context context, ListItemClickListener listItemClickListener) {
		this.context = context;
		this.listItemClickListener = listItemClickListener;
	}

	public void setCompetitionsFavorites(List<CompetitionEntity> competitionsFavorites) {
		this.competitionsFavorites = competitionsFavorites;
		notifyDataSetChanged();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.listitem_favorites, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.tvTitle.setText(competitionsFavorites.get(position).getName());
		holder.tvSubTitle01.setText(Utils.getStringResourceByName(context, competitionsFavorites.get(position).getSportName()));
		holder.tvSubTitle02.setText(Utils.getStringResourceByName(context,competitionsFavorites.get(position).getCategoryName()));
		holder.cvCompetitions.setTag(competitionsFavorites.get(position).getServerId());
	}

	@Override
	public int getItemCount() {
		return competitionsFavorites.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

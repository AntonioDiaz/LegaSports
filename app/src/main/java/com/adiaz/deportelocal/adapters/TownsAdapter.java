package com.adiaz.deportelocal.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.deportelocal.R;
import com.adiaz.deportelocal.utilities.retrofit.entities.town.TownRestEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by toni on 03/08/2017.
 */

public class TownsAdapter extends RecyclerView.Adapter<TownsAdapter.ViewHolder> {

	private List<TownRestEntity> mTownRestEntityList;
	private ListItemClickListener mListItemClickListener;

	public TownsAdapter(List<TownRestEntity> townRestEntityList, ListItemClickListener listItemClickListener) {
		this.mTownRestEntityList = townRestEntityList;
		this.mListItemClickListener = listItemClickListener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		Context context = viewGroup.getContext();
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.listitem_towns, viewGroup, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.tvTownName.setText(mTownRestEntityList.get(position).getName());
		holder.tvTownName.setTag(mTownRestEntityList.get(position).getId());
	}

	@Override
	public int getItemCount() {
		return mTownRestEntityList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@BindView(R.id.tv_town_name) TextView tvTownName;
		@BindView(R.id.cv_town) CardView cvTown;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			cvTown.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			mListItemClickListener.onListItemClick(getAdapterPosition());
		}
	}

	public interface ListItemClickListener {
		void onListItemClick(int clickedItemIndex);
	}
}

package com.adiaz.munisports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.munisports.R;
import com.adiaz.munisports.sync.retrofit.entities.Town;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by toni on 03/08/2017.
 */

public class TownsAdapter extends RecyclerView.Adapter<TownsAdapter.ViewHolder> {

	private List<Town> mTownList;
	private ListItemClickListener mListItemClickListener;

	public TownsAdapter(List<Town> townList, ListItemClickListener listItemClickListener) {
		this.mTownList = townList;
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
		holder.tvTownName.setText(mTownList.get(position).getName());
		holder.tvTownName.setTag(mTownList.get(position).getId());
	}

	@Override
	public int getItemCount() {
		return mTownList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@BindView(R.id.tv_town_name) TextView tvTownName;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			tvTownName.setOnClickListener(this);
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

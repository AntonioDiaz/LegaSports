package com.adiaz.legasports.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.legasports.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by toni on 25/03/2017.
 */

public class ClassificationRecyclerViewAdapter extends RecyclerView.Adapter<ClassificationRecyclerViewAdapter.MyViewHolder> {

	List<String[]> teamsRows;
	private static final int TYPE_HEADER = 0;
	private static final int TYPE_ITEM = 1;

	public ClassificationRecyclerViewAdapter(List<String[]> teamsRows) {
		this.teamsRows = teamsRows;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		int itemType = R.layout.listitem_classification;
		if (viewType==TYPE_HEADER) {
			itemType = R.layout.listitem_header_classification;
		}
		LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
		View view = layoutInflater.inflate(itemType, parent, false);
		return new MyViewHolder(view);
	}


	@Override
	public int getItemViewType(int position) {
		int viewType = TYPE_ITEM;
		if (position==0) {
			viewType = TYPE_HEADER;
		}
		return viewType;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		String[] item = teamsRows.get(position);
		holder.tvPosition.setText(item[0]);
		holder.tvTeam.setText(item[1]);
		holder.tvPj.setText(item[2]);
		holder.tvPg.setText(item[3]);
		holder.tvPe.setText(item[4]);
		holder.tvPp.setText(item[5]);
		holder.tvPt.setText(item[11]);
	}

	@Override
	public int getItemCount() {
		return teamsRows.size();
	}

	public class MyViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.tv_classification_position) TextView tvPosition;
		@BindView(R.id.tv_classification_team) TextView tvTeam;
		@BindView(R.id.tv_classification_pj) TextView tvPj;
		@BindView(R.id.tv_classification_pg) TextView tvPg;
		@BindView(R.id.tv_classification_pe) TextView tvPe;
		@BindView(R.id.tv_classification_pp) TextView tvPp;
		@BindView(R.id.tv_classification_pt) TextView tvPt;

		public MyViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}
	}
}

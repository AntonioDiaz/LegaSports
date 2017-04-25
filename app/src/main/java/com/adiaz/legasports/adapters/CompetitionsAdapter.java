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

public class CompetitionsAdapter extends RecyclerView.Adapter<CompetitionsAdapter.ViewHolder>{

	private Context context;
	private List<String> competitions;

	public CompetitionsAdapter(Context context) {
		this.context = context;
	}

	public void setCompetitions(List<String> competitions) {
		this.competitions = competitions;
		notifyDataSetChanged();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.listitem_competitions, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.tvCategoryName.setText(competitions.get(position));
		holder.cvCategories.setTag(competitions.get(position));
	}

	@Override
	public int getItemCount() {
		return competitions.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		private TextView tvCategoryName;
		private View cvCategories;
		public ViewHolder(View itemView) {
			super(itemView);
			tvCategoryName = (TextView)itemView.findViewById(R.id.tv_competition_name);
			cvCategories = itemView.findViewById(R.id.cv_categories);
		}
	}
}

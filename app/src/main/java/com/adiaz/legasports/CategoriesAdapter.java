package com.adiaz.legasports;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/* Created by toni on 31/03/2017. */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>{

	private Context context;
	private List<String>categories;

	public CategoriesAdapter(Context context) {
		this.context = context;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
		notifyDataSetChanged();
	}

	@Override
	public CategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.listitem_categories, parent, false);
		return new CategoriesViewHolder(view);
	}

	@Override
	public void onBindViewHolder(CategoriesViewHolder holder, int position) {
		holder.tvCategoryName.setText(categories.get(position));
		holder.cvCategories.setTag(categories.get(position));
	}

	@Override
	public int getItemCount() {
		return categories.size();
	}

	public class CategoriesViewHolder extends RecyclerView.ViewHolder {
		private TextView tvCategoryName;
		private View cvCategories;
		public CategoriesViewHolder(View itemView) {
			super(itemView);
			tvCategoryName = (TextView)itemView.findViewById(R.id.tv_category_name);
			cvCategories = itemView.findViewById(R.id.cv_categories);
		}
	}
}

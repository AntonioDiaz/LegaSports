package com.adiaz.deportelocal.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.deportelocal.R;
import com.adiaz.deportelocal.database.DeporteLocalDbContract;
import com.adiaz.deportelocal.entities.Competition;

import butterknife.BindView;
import butterknife.ButterKnife;

/* Created by toni on 31/03/2017. */

public class CompetitionsAdapter extends RecyclerView.Adapter<CompetitionsAdapter.ViewHolder> {

	private Context context;
	private Cursor competitions;
	private final ListItemClickListener mOnClickListener;

	public CompetitionsAdapter(Context context, ListItemClickListener mOnClickListener) {
		this.context = context;
		this.mOnClickListener = mOnClickListener;
	}

	public void setCompetitions(Cursor competitions) {
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
		competitions.moveToPosition(position);
        Competition competition = DeporteLocalDbContract.CompetitionsEntry.initEntity(competitions);
		holder.tvCompetitionName.setText(competition.name());
		holder.tvCategoryName.setText(competition.categoryName());
	}

	@Override
	public int getItemCount() {
		return competitions == null ? 0 : competitions.getCount();
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@BindView(R.id.tv_competition_name)
		TextView tvCompetitionName;
		@BindView(R.id.tv_category_name)
		TextView tvCategoryName;
		@BindView(R.id.cv_competition)
		CardView cvCompetitions;

		public ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
			cvCompetitions.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			mOnClickListener.onListItemClick(getAdapterPosition());
		}
	}

	public interface ListItemClickListener {
		void onListItemClick(int clickedItemIndex);
	}
}

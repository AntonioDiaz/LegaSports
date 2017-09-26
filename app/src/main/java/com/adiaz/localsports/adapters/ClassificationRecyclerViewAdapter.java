package com.adiaz.localsports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.localsports.R;
import com.adiaz.localsports.entities.Classification;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by toni on 25/03/2017.
 */

public class ClassificationRecyclerViewAdapter extends RecyclerView.Adapter<ClassificationRecyclerViewAdapter.MyViewHolder> {


	Context mContext;
	List<Classification> classificationList;
	private static final int TYPE_HEADER = 0;
	private static final int TYPE_ITEM = 1;

	public ClassificationRecyclerViewAdapter(Context mContext, List<Classification> classificationList) {
		this.mContext = mContext;
		this.classificationList = classificationList;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		int itemType = R.layout.listitem_classification;
		if (viewType == TYPE_HEADER) {
			itemType = R.layout.listitem_header_classification;
		}
		LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
		View view = layoutInflater.inflate(itemType, parent, false);
		return new MyViewHolder(view);
	}


	@Override
	public int getItemViewType(int position) {
		int viewType = TYPE_ITEM;
		if (position == 0) {
			viewType = TYPE_HEADER;
		}
		return viewType;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		if (position==0) {
			holder.tvPosition.setText(" ");
			holder.tvTeam.setText("Equipo");
			holder.tvPj.setText("PJ");
			holder.tvPg.setText("PG");
			holder.tvPe.setText("PE");
			holder.tvPp.setText("PP");
			holder.tvPt.setText("PT");
		} else {
			Classification c = classificationList.get(position - 1);
			holder.tvPosition.setText(c.getPosition() == null ? "0" : c.getPosition().toString());
			holder.tvTeam.setText(c.getTeam());
			holder.tvPj.setText("" + c.getMatchesPlayed());
			holder.tvPg.setText("" + c.getMatchesWon());
			holder.tvPe.setText("" + c.getMatchesDrawn());
			holder.tvPp.setText("" + c.getMatchesLost());
			holder.tvPt.setText("" + c.getPoints());
		}
	}

	@Override
	public int getItemCount() {
		return classificationList.size() + 1;
	}

	public class MyViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.tv_classification_position)
		TextView tvPosition;
		@BindView(R.id.tv_classification_team)
		TextView tvTeam;
		@BindView(R.id.tv_classification_pj)
		TextView tvPj;
		@BindView(R.id.tv_classification_pg)
		TextView tvPg;
		@BindView(R.id.tv_classification_pe)
		TextView tvPe;
		@BindView(R.id.tv_classification_pp)
		TextView tvPp;
		@BindView(R.id.tv_classification_pt)
		TextView tvPt;

		public MyViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}
	}
}

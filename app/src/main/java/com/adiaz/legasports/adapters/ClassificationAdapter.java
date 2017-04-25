package com.adiaz.legasports.adapters;

/* Created by toni on 22/03/2017. */

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adiaz.legasports.R;

public class ClassificationAdapter extends ArrayAdapter<String[]> {

	public ClassificationAdapter(@NonNull Context context, @LayoutRes int resource) {
		super(context, resource);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		String[] item = getItem(position);
		if (convertView==null) {
			LayoutInflater layoutInflater = LayoutInflater.from(getContext());
			if (getItemViewType(position)==0) {
				convertView = layoutInflater.inflate(R.layout.listitem_header_classification, parent, false);
			} else {
				convertView = layoutInflater.inflate(R.layout.listitem_classification, parent, false);
			}
		}
		TextView tvPosition = (TextView) convertView.findViewById(R.id.tv_classification_position);
		TextView tvTeam = (TextView) convertView.findViewById(R.id.tv_classification_team);
		TextView tvPj = (TextView) convertView.findViewById(R.id.tv_classification_pj);
		TextView tvPg = (TextView) convertView.findViewById(R.id.tv_classification_pg);
		TextView tvPe = (TextView) convertView.findViewById(R.id.tv_classification_pe);
		TextView tvPp = (TextView) convertView.findViewById(R.id.tv_classification_pp);
		TextView tvPt = (TextView) convertView.findViewById(R.id.tv_classification_pt);

		tvPosition.setText(item[0]);
		tvTeam.setText(item[1]);
		tvPj.setText(item[2]);
		tvPg.setText(item[3]);
		tvPe.setText(item[4]);
		tvPp.setText(item[5]);
		tvPt.setText(item[11]);
		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return position ==0?0:1;
	}
}

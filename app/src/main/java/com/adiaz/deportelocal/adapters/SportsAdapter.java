package com.adiaz.deportelocal.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adiaz.deportelocal.R;
import com.adiaz.deportelocal.database.DeporteLocalDbContract;
import com.adiaz.deportelocal.entities.Sport;
import com.adiaz.deportelocal.utilities.DeporteLocalUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by adiaz on 10/1/18.
 */

public class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.ViewHolder> {

    Context mContext;
    ListItemClickListener mListItemClickListener;
    Cursor mCursor;


    public SportsAdapter(Context mContext, ListItemClickListener mListItemClickListener) {
        this.mContext = mContext;
        this.mListItemClickListener = mListItemClickListener;
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.listitem_sports;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new SportsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int colorPrimaryDark = ResourcesCompat.getColor(mContext.getResources(), R.color.colorPrimaryDark, null);
        int colorAccent = ResourcesCompat.getColor(mContext.getResources(), R.color.colorAccent, null);
        //holder.mSportName.setBackgroundColor(colorPrimaryDark);
        if (position==0) {
            holder.mSportName.setText(mContext.getString(R.string.favorites));
            holder.mIvSport.setImageResource(R.drawable.favorite);
            holder.mIvSport.setColorFilter(colorAccent, PorterDuff.Mode.SRC_IN);
        } else {
            mCursor.moveToPosition(position - 1);
            Sport sport = DeporteLocalDbContract.SportsEntry.initEntity(mCursor);
            holder.mSportName.setText(DeporteLocalUtils.getStringResourceByName(mContext, sport.tag()));
            holder.mIvSport.setColorFilter(colorPrimaryDark, PorterDuff.Mode.SRC_IN);
            try {
                int identifier = mContext.getResources().getIdentifier(sport.image(), "drawable", mContext.getPackageName());
                holder.mIvSport.setImageResource(identifier);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_sport)
        TextView mSportName;

        @BindView(R.id.iv_sport)
        ImageView mIvSport;

        public ViewHolder (View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            int clickedItemIndex = getAdapterPosition();
            mListItemClickListener.onListItemClick(clickedItemIndex);
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}

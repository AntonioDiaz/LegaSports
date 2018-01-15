package com.adiaz.deportelocal.utilities;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

/**
 * Created by toni on 27/03/2017.
 */

public class NonScrollExpandableListView extends ExpandableListView {

	public NonScrollExpandableListView(Context context) {
		super(context);
	}

	public NonScrollExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NonScrollExpandableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			int heightMeasureSpecCustom = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, heightMeasureSpecCustom);
			ViewGroup.LayoutParams params = getLayoutParams();
			params.height = getMeasuredHeight();
	}
}

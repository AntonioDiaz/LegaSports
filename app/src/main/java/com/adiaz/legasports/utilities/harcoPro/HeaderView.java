package com.adiaz.legasports.utilities.harcoPro;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adiaz.legasports.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HeaderView extends LinearLayout {

	@BindView(R.id.header_view_title)
	TextView title;

	@BindView(R.id.header_view_sub_title)
	TextView subTitle;


	public HeaderView(Context context) {
		super(context);
	}

	public HeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}


	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		ButterKnife.bind(this);
	}

	public void bindTo(String title) {
		bindTo(title, "", 0);
	}

	public void bindTo(String title, String subTitle, int bottonMargin) {
		hideOrSetText(this.title, title);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)this.title.getLayoutParams();
		params.setMargins(0, 0, 0, bottonMargin);
		this.title.setLayoutParams(params);
		hideOrSetText(this.subTitle, subTitle);
	}

	private void hideOrSetText(TextView tv, String text) {
		if (text == null || text.equals("")) {
			tv.setVisibility(GONE);
		} else {
			tv.setText(text);
		}
	}

	public void setScaleXTitle(float scaleXTitle) {
		title.setScaleX(scaleXTitle);
		title.setPivotX(0);
	}

	public void setScaleYTitle(float scaleYTitle) {
		title.setScaleY(scaleYTitle);
		title.setPivotY(30);
	}
}
package com.sagetech.conference_android.app.ui.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sagetech.conference_android.app.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by willmetz on 1/24/16.
 */
public class ConferenceSessionListItemHeader extends LinearLayout
{
    @Bind( R.id.header_day_text)
    TextView headerText;

    public ConferenceSessionListItemHeader(Context context) {
        super(context);
        init();
    }

    public ConferenceSessionListItemHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ConferenceSessionListItemHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(ViewGroup.LayoutParams.MATCH_PARENT, 50 );
    }

    public void setHeaderInfo( String headerText )
    {
        this.headerText.setText( headerText );
    }

    private void init()
    {
        inflate(getContext(), R.layout.conference_session_list_item_header, this);
        ButterKnife.bind(this);
    }

}

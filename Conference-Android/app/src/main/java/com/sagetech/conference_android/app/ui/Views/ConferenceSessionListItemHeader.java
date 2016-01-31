package com.sagetech.conference_android.app.ui.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sagetech.conference_android.app.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Custom view for the conference list header view.
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


    public void setHeaderInfo( String headerText )
    {
        this.headerText.setText(headerText);

    }



    private void init()
    {
        inflate(getContext(), R.layout.conference_session_list_item_header, this);
        ButterKnife.bind(this);


        //as a merge layout doesn't retain any parameters we need to set the layout dimensions programmatically
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );
        setLayoutParams(params);

    }

}

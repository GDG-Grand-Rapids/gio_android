package com.sagetech.conference_android.app.ui.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sagetech.conference_android.app.R;
import com.sagetech.conference_android.app.ui.viewModel.ConferenceSessionViewModel;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by willmetz on 1/23/16.
 */
public class ConferenceSessionViewItem extends RelativeLayout
{

    @Bind( R.id.room )
    TextView sessionRoom;

    @Bind( R.id.time )
    TextView sessionTime;

    @Bind( R.id.title)
    TextView sessionTitle;

    @Bind( R.id.iconType )
    ImageView icon;


    public ConferenceSessionViewItem(Context context)
    {
        super(context);

        init();
    }

    public ConferenceSessionViewItem(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public ConferenceSessionViewItem(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setSessionInfo( ConferenceSessionViewModel data )
    {
        this.sessionRoom.setText( data.getRoom() );
        this.sessionTime.setText( data.getTime() );
        this.sessionTitle.setText( data.getTitle() );


        Picasso.with( getContext() ).load( data.getSessionImageResource() ).into( icon );

    }

    private void init()
    {
        inflate(getContext(), R.layout.conference_session_list_item, this);
        ButterKnife.bind( this );
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        //as a merge layout removes the layout attribute values we need to manually set the height
        super.onMeasure(widthMeasureSpec, (int) getResources().getDimension( R.dimen.conference_session_list_item_height ) );
    }
}

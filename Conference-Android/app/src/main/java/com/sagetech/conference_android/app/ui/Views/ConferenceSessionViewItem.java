package com.sagetech.conference_android.app.ui.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sagetech.conference_android.app.R;
import com.sagetech.conference_android.app.ui.viewModel.ConferenceSessionViewModel;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by willmetz on 1/23/16.
 */
public class ConferenceSessionViewItem extends RelativeLayout implements View.OnClickListener
{

    @Bind( R.id.room )
    TextView sessionRoom;

    @Bind( R.id.time )
    TextView sessionTime;

    @Bind( R.id.title)
    TextView sessionTitle;

    @Bind( R.id.iconType )
    ImageView icon;

    @Bind( R.id.favorite_icon )
    ImageView favoriteIcon;

    private OnConferenceSessionClickListener listener;


    public interface OnConferenceSessionClickListener
    {
        void onFavoriteItemSelected( );
        void onSessionClicked();
    }


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

    public void setListener( OnConferenceSessionClickListener listener )
    {
        this.listener = listener;
    }

    public void setSessionInfo( ConferenceSessionViewModel data )
    {
        this.sessionRoom.setText( data.getRoom() );
        this.sessionTime.setText( data.getTime() );
        this.sessionTitle.setText( data.getTitle() );


        Picasso.with( getContext() ).load( data.getSessionImageResource() ).into( icon );

    }

    public void setFavorite( boolean isFavorite )
    {
        if( isFavorite )
        {
            Picasso.with( getContext() ).load( R.drawable.star_icon_filled ).into(favoriteIcon);
        }
        else
        {
            Picasso.with( getContext() ).load( R.drawable.star_icon_unfilled ).into( favoriteIcon );
        }
    }

    private void init()
    {
        inflate(getContext(), R.layout.conference_session_list_item, this);
        ButterKnife.bind(this);

        setOnClickListener( this );
    }

    @OnClick( R.id.favorite_icon )
    void favoriteIconClicked()
    {
        if( listener != null )
        {
            listener.onFavoriteItemSelected();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        //as a merge layout removes the layout attribute values we need to manually set the height
        super.onMeasure(widthMeasureSpec, (int) getResources().getDimension(R.dimen.conference_session_list_item_height));
    }

    @Override
    public void onClick(View v)
    {
        if( listener != null )
        {
            listener.onSessionClicked();
        }
    }
}

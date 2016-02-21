package com.sagetech.conference_android.app.ui.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.sagetech.conference_android.app.R;


/**
 * Custom class to hold the logic for enabling and disabling a filter view icon.
 */
public class FavoriteFilterView extends ImageView implements View.OnClickListener
{

    private boolean filterEnabled;
    private FavoritesFilterViewListener listener;

    public interface FavoritesFilterViewListener
    {
        void enabledFilter( boolean enabled );
    }

    public FavoriteFilterView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init( context, attrs );
    }

    public FavoriteFilterView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init( Context context, AttributeSet attrs )
    {

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.FavoriteFilterView,
                0, 0);

        boolean filterEnabled = false;

        try {
            filterEnabled = a.getBoolean( R.styleable.FavoriteFilterView_filter_enabled, false );
        } finally {
            a.recycle();
        }

        if( filterEnabled )
        {
            setImageResource(R.drawable.star_icon_filled);
        }
        else
        {
            setImageResource(R.drawable.star_icon_unfilled);
        }

        setOnClickListener(this);
    }

    public void setListener( FavoritesFilterViewListener listener )
    {
        this.listener = listener;
    }

    public boolean filterEnabled()
    {
        return filterEnabled;
    }

    public void showFilterEnabled( boolean enabled )
    {


        if( enabled )
        {
            setImageResource(R.drawable.star_icon_filled);
        }
        else
        {
            setImageResource(R.drawable.star_icon_unfilled);
        }

        if( listener != null )
        {
            listener.enabledFilter( enabled );
        }

    }

    public void hide()
    {
        setVisibility( INVISIBLE );
    }

    public void show()
    {
        //always make sure the indicator is visible
        setVisibility( VISIBLE );
    }

    @Override
    public void onClick(View v)
    {
        filterEnabled = !filterEnabled;

        showFilterEnabled( filterEnabled );
    }
}

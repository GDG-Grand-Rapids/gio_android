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
public class StarView extends ImageView implements View.OnClickListener
{

    private boolean starFilled;
    private StarViewListener listener;

    public interface StarViewListener
    {
        void starSelected(boolean enabled);
    }

    public StarView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init( context, attrs );
    }

    public StarView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init( Context context, AttributeSet attrs )
    {

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StarView,
                0, 0);

        //init icon state
        starFilled = false;

        try {
            starFilled = a.getBoolean( R.styleable.StarView_filter_enabled, false );
        } finally {
            a.recycle();
        }

        setIcon(starFilled);

        setOnClickListener(this);
    }

    public void setListener( StarViewListener listener )
    {
        this.listener = listener;
    }

    public boolean filled()
    {
        return starFilled;
    }

    public void setState( boolean filled )
    {
        starFilled = filled;

        setIcon( starFilled );
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
        starFilled = !starFilled;

        setState(starFilled);

        if( listener != null )
        {
            listener.starSelected( starFilled );
        }
    }

    private void setIcon(boolean filled)
    {
        if( filled )
        {
            setImageResource(R.drawable.star_icon_filled);
        }
        else
        {
            setImageResource(R.drawable.star_icon_unfilled);
        }
    }

}

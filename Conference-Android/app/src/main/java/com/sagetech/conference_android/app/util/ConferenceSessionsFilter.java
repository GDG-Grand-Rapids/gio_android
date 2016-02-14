package com.sagetech.conference_android.app.util;

import android.widget.Filter;

import com.sagetech.conference_android.app.ui.viewModel.ConferenceSessionViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 *  A customer filter class for a list of ConferenceSessionViewModel objects.
 */
public class ConferenceSessionsFilter extends Filter
{

    public interface ConferenceSessionFilterListener
    {
        void onFilterApplied( FilterResults filterResults );
        void onFilterCleared( FilterResults filterResults );
    }

    private List<ConferenceSessionViewModel> conferenceSessions;
    private ConferenceSessionFilterListener listener;

    public ConferenceSessionsFilter( List<ConferenceSessionViewModel> conferenceSessions )
    {
        this.conferenceSessions = conferenceSessions;
    }

    public void updateSessionData( List<ConferenceSessionViewModel> conferenceSessions )
    {
        this.conferenceSessions = conferenceSessions;
    }

    public void setListener( ConferenceSessionFilterListener listener )
    {
        this.listener = listener;
    }

    @Override
    public CharSequence convertResultToString(Object resultValue)
    {
        return super.convertResultToString(resultValue);
    }

    @Override
    protected FilterResults performFiltering( CharSequence constraint )
    {
        FilterResults filteredResults = new FilterResults();

        if( constraint == null || constraint.length() == 0 )
        {
            filteredResults.count = conferenceSessions.size();
            filteredResults.values = conferenceSessions;
        }
        else
        {
            //since there is currently only one filter available any constraint means apply this filter
            ArrayList<ConferenceSessionViewModel> favoritesList = new ArrayList<>();

            for( ConferenceSessionViewModel session : conferenceSessions )
            {
                if( session.isFavorite )
                {
                    favoritesList.add( session );
                }
            }

            filteredResults.count = favoritesList.size();
            filteredResults.values = favoritesList;

        }

        return filteredResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results)
    {
        if( listener != null )
        {
            if( constraint == null || constraint.length() == 0 )
            {
                listener.onFilterCleared( results );
            }
            else
            {
                listener.onFilterApplied( results );
            }
        }
    }
}

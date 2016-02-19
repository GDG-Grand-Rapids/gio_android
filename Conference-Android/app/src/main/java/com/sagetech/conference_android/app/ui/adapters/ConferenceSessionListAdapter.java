package com.sagetech.conference_android.app.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.sagetech.conference_android.app.R;
import com.sagetech.conference_android.app.ui.Views.ConferenceSessionListItemHeader;
import com.sagetech.conference_android.app.ui.Views.ConferenceSessionViewItem;
import com.sagetech.conference_android.app.ui.viewModel.ConferenceSessionViewModel;
import com.sagetech.conference_android.app.util.ConferenceSessionsFilter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;
import timber.log.Timber;

/**
 * Created by carlushenry on 3/5/15.
 */
public class ConferenceSessionListAdapter extends RecyclerView.Adapter<ConferenceSessionListAdapter.ViewHolder>
    implements StickyRecyclerHeadersAdapter<ConferenceSessionListAdapter.DayViewHolder>,
        Filterable,
        ConferenceSessionsFilter.ConferenceSessionFilterListener

{
    private List<ConferenceSessionViewModel> conferenceSessions;
    private ConferenceSessionListOnClickListener onClickListener;
    private HashMap< Integer, String > sessionDateToHeaderMap;
    private ConferenceSessionsFilter conferenceSessionsFilter;


    public interface ConferenceSessionListOnClickListener
    {
        void onViewConferenceDetails( Long id );
    }

    public ConferenceSessionListAdapter(List<ConferenceSessionViewModel> conferenceSessions,
                                        ConferenceSessionListOnClickListener onClickListener,
                                        boolean applyFilter )
    {

        this.onClickListener = onClickListener;

        initLists(conferenceSessions);

        if( applyFilter )
        {
            applyFavoritesFilter(applyFilter);
        }

    }

    public void applyFavoritesFilter( boolean apply )
    {
        if( apply )
        {
            getFilter().filter( ConferenceSessionsFilter.FAVORITES_FILTER );
        }
        else
        {
            getFilter().filter( ConferenceSessionsFilter.CLEAR_FILTER );
        }
    }

    protected void initLists( List<ConferenceSessionViewModel> conferenceSessions )
    {
        this.conferenceSessions = conferenceSessions;

        sessionDateToHeaderMap = new HashMap<>();

        //determine how many headers will be needed based on the days the sessions are on
        Calendar currentSession = Calendar.getInstance();
        for ( ConferenceSessionViewModel item : conferenceSessions )
        {
            currentSession.setTime(item.getStartDttm());

            if( !sessionDateToHeaderMap.containsKey( currentSession.get( Calendar.DAY_OF_YEAR ) ) )
            {
                sessionDateToHeaderMap.put( currentSession.get(Calendar.DAY_OF_YEAR), item.getDay() );
            }
        }
    }

    //
    // ConferenceSessionsFilter.ConferenceSessionFilterListener implementation
    //
    @Override
    public void onFilterApplied(List<ConferenceSessionViewModel> filterResults )
    {
        initLists( filterResults );
        notifyDataSetChanged();

    }

    @Override
    public void onFilterCleared(List<ConferenceSessionViewModel> filterResults )
    {
        initLists( filterResults );
        notifyDataSetChanged();
    }


    @Override
    public Filter getFilter()
    {
        Timber.d("getFilter called");

        if( conferenceSessionsFilter == null )
        {
            conferenceSessionsFilter = new ConferenceSessionsFilter( conferenceSessions );
            conferenceSessionsFilter.setListener( this );
        }

        return conferenceSessionsFilter;
    }

    //RecyclerView.Adapter implementation
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ViewHolder( new ConferenceSessionViewItem( parent.getContext() ) );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.setData( getItem(position) );
    }

    @Override
    public long getItemId(int position)
    {
        return getItem(position).getId();
    }


    //StickyRecyclerHeadersAdapter implementation
    @Override
    public long getHeaderId(int position)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getItem(position).getStartDttm() );

        return calendar.get( Calendar.DAY_OF_YEAR );
    }

    @Override
    public DayViewHolder onCreateHeaderViewHolder(ViewGroup parent)
    {
        return new DayViewHolder( new ConferenceSessionListItemHeader( parent.getContext() ) );
    }

    @Override
    public void onBindHeaderViewHolder(DayViewHolder holder, int position)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( getItem(position).getStartDttm() );

        holder.setDay( sessionDateToHeaderMap.get( calendar.get( Calendar.DAY_OF_YEAR ) ) );
    }

    @Override
    public int getItemCount() {
        return conferenceSessions.size();
    }

    public ConferenceSessionViewModel getItem(int position) {
        return conferenceSessions.get(position);
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements ConferenceSessionViewItem.OnConferenceSessionClickListener
    {
        private ConferenceSessionViewModel conferenceSessionViewModel;
        private ConferenceSessionViewItem itemView;

        // each data item is just a string in this case
        public ViewHolder(View v)
        {
            super(v);

            if( v instanceof ConferenceSessionViewItem ) {
                itemView = (ConferenceSessionViewItem) v;
            }

            itemView.setListener( this );

        }


        public void setData(ConferenceSessionViewModel conferenceSessionViewModel)
        {
            this.conferenceSessionViewModel = conferenceSessionViewModel;

            itemView.setSessionInfo(conferenceSessionViewModel);

            itemView.setFavorite( conferenceSessionViewModel.isFavorite );
        }

        //ConferenceSessionViewItem.OnConferenceSessionClickListener
        @Override
        public void onFavoriteItemSelected()
        {
            conferenceSessionViewModel.isFavorite = !conferenceSessionViewModel.isFavorite;

            itemView.setFavorite( conferenceSessionViewModel.isFavorite );
        }

        @Override
        public void onSessionClicked()
        {
            onClickListener.onViewConferenceDetails( conferenceSessionViewModel.getId() );
        }
    }




    public class DayViewHolder extends RecyclerView.ViewHolder
    {

        private ConferenceSessionListItemHeader headerView;

        // each data item is just a string in this case
        public DayViewHolder(View v)
        {
            super(v);

            if( v instanceof  ConferenceSessionListItemHeader )
            {
                headerView = (ConferenceSessionListItemHeader)v;
            }
        }



        public void setDay( String sessionDay )
        {
            headerView.setHeaderInfo( sessionDay );
        }
    }

}

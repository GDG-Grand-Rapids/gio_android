package com.sagetech.conference_android.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.sagetech.conference_android.app.R;
import com.sagetech.conference_android.app.ui.Views.FavoriteFilterView;
import com.sagetech.conference_android.app.ui.Views.SimpleDividerLineDecorator;
import com.sagetech.conference_android.app.ui.adapters.ConferenceSessionListAdapter;
import com.sagetech.conference_android.app.ui.presenter.IConferenceSessionActivity;
import com.sagetech.conference_android.app.ui.presenter.IConferenceSessionListPresenter;
import com.sagetech.conference_android.app.ui.viewModel.ConferenceSessionViewModel;
import com.sagetech.conference_android.app.util.ConferenceIntents;
import com.sagetech.conference_android.app.util.module.ConferenceSessionListModule;
import com.squareup.picasso.Picasso;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by adam on 2/21/15.
 */
public class ConferenceSessionListActivity extends InjectableActionBarActivity
        implements IConferenceSessionActivity,
        ConferenceSessionListAdapter.ConferenceSessionListOnClickListener,
        FavoriteFilterView.FavoritesFilterViewListener
{

    @Inject
    IConferenceSessionListPresenter presenter;

    @Bind(R.id.confSessionView)
    RecyclerView mRecyclerView;

    @Bind(R.id.txtConferenceName)
    TextView txtConferenceName;

    @Bind(R.id.favorites_filter)
    FavoriteFilterView favoriteFilter;

    private final String FILTER_APPLIED_KEY = "com.sagetech.conference_android.filterApplied";
    private final int SESSION_DETAIL_REQUEST_CODE = 1;

    private long conferenceID;

    private ConferenceSessionListAdapter mAdapter;

    //need to keep these guys as class variables so they can be removed when needed
    private StickyRecyclerHeadersDecoration headersDecor;
    private SimpleDividerLineDecorator dividerLineDecorator;
    
    private boolean refreshData;
    private boolean filterApplied;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_session_items);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        setTitle("Event Sessions");

        if( savedInstanceState == null )
        {
            txtConferenceName.setText(getIntent().getStringExtra( ConferenceIntents.CONFERENCE_NAME_KEY ) );
            conferenceID = getIntent().getLongExtra( ConferenceIntents.CONFERENCE_ID_KEY, 0 );
            filterApplied = false;
        }
        else
        {
            txtConferenceName.setText(savedInstanceState.getString(ConferenceIntents.CONFERENCE_NAME_KEY));
            conferenceID = savedInstanceState.getLong(ConferenceIntents.CONFERENCE_ID_KEY, 0);

            //TODO - remove when saving data is implemented
            filterApplied = false;

            //TODO - implement when saving this data is implemented
            //filterApplied = savedInstanceState.getBoolean(FILTER_APPLIED_KEY);
            //setFilterAppliedIcon(filterApplied);
        }

        //make sure the filter view is in the correct state and the listener is setup
        if( filterApplied )
        {
            favoriteFilter.setFilterEnabled();
        }
        else
        {
            favoriteFilter.clearFilter();
        }
        favoriteFilter.setListener(this);

        //assume we are going to refresh the data
        refreshData = true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if( refreshData )
        {
            presenter.initialize(conferenceID);
        }

        //mark data as needing refresh in case the app goes in the background
        refreshData = true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        //if the activity was ever killed make sure we refresh the data on start
        presenter.initialize(conferenceID);

    }

    @Override
    protected void onStop()
    {
        super.onStop();

        //in case there was an issue receiving the data in the subscriber, ensure it is unsubscribed
        presenter.onUnsubscribe();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putString(ConferenceIntents.CONFERENCE_NAME_KEY,
                getIntent().getStringExtra(ConferenceIntents.CONFERENCE_NAME_KEY));

        outState.putLong(ConferenceIntents.CONFERENCE_ID_KEY, conferenceID);

        outState.putBoolean(FILTER_APPLIED_KEY, filterApplied);

        super.onSaveInstanceState(outState);
    }


    @Override
    protected List<Object> getModules() {
        return Arrays.<Object>asList(new ConferenceSessionListModule(this));
    }


    @Override
    public void populateConferenceSessions(List<ConferenceSessionViewModel> conferenceSessions)
    {
        mAdapter = new ConferenceSessionListAdapter(conferenceSessions, this, favoriteFilter.filterEnabled() );
        mRecyclerView.setAdapter(mAdapter);

        //ensure the old decors are removed (if there are any)
        if( headersDecor != null )
        {
            mRecyclerView.removeItemDecoration(headersDecor);
        }
        if( dividerLineDecorator != null )
        {
            mRecyclerView.removeItemDecoration( dividerLineDecorator );
        }

        // Add the sticky headers decoration
        headersDecor = new StickyRecyclerHeadersDecoration( mAdapter );
        mRecyclerView.addItemDecoration(headersDecor);

        dividerLineDecorator = new SimpleDividerLineDecorator( this );
        mRecyclerView.addItemDecoration( dividerLineDecorator );

        presenter.onUnsubscribe();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == SESSION_DETAIL_REQUEST_CODE && resultCode == RESULT_OK )
        {
            refreshData = false;
        }
    }


    //
    // ConferenceSessionListAdapter.ConferenceSessionListOnClickListener implementation
    //
    @Override
    public void onViewConferenceDetails( Long sessionId )
    {

        Timber.d(String.format("Session Selected: %s", sessionId));

        Intent eventDetailIntent = new Intent(this, ConferenceSessionDetailActivity.class);
        eventDetailIntent.putExtra("id", sessionId);
        startActivityForResult(eventDetailIntent, SESSION_DETAIL_REQUEST_CODE );
    }


    //
    // FavoriteFilterView.FavoritesFilterViewListener implementation
    //

    @Override
    public void showFavoritesOnly()
    {
        if( mAdapter != null )
        {
            mAdapter.applyFavoritesFilter( true );
        }
    }

    @Override
    public void showAll()
    {
        if( mAdapter != null )
        {
            mAdapter.applyFavoritesFilter( false );
        }
    }
}

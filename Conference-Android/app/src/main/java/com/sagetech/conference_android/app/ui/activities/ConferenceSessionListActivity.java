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
        ConferenceSessionListAdapter.ConferenceSessionListOnClickListener
{

    @Inject
    IConferenceSessionListPresenter presenter;

    @Bind(R.id.confSessionView)
    RecyclerView mRecyclerView;

    @Bind(R.id.txtConferenceName)
    TextView txtConferenceName;

    @Bind(R.id.favorites_filter)
    ImageView favoriteFilter;

    private final String FILTER_APPLIED_KEY = "com.sagetech.conference_android.filterApplied";

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
            setFavoritesFilterImage(false);
            filterApplied = false;
        }
        else
        {
            txtConferenceName.setText( savedInstanceState.getString( ConferenceIntents.CONFERENCE_NAME_KEY ) );
            conferenceID = savedInstanceState.getLong(ConferenceIntents.CONFERENCE_ID_KEY, 0);
            filterApplied = savedInstanceState.getBoolean(FILTER_APPLIED_KEY);
            setFavoritesFilterImage(filterApplied);
        }

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
        outState.putString(ConferenceIntents.CONFERENCE_NAME_KEY ,
                getIntent().getStringExtra( ConferenceIntents.CONFERENCE_NAME_KEY  ) );

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
        mAdapter = new ConferenceSessionListAdapter(conferenceSessions, this, filterApplied);
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

        if( requestCode == 1 && resultCode == RESULT_OK )
        {
            refreshData = false;
        }
    }

    @OnClick( R.id.favorites_filter )
    public void favoritesFilterClicked()
    {
        filterApplied = !filterApplied;

        setFavoritesFilterImage(filterApplied);
        mAdapter.applyFavoritesFilter(filterApplied);
    }

    private void setFavoritesFilterImage(boolean apply)
    {
        if( apply )
        {
            Picasso.with( this ).load( R.drawable.star_icon_filled ).into( favoriteFilter );
        }
        else
        {
            Picasso.with( this ).load( R.drawable.star_icon_unfilled ).into( favoriteFilter );
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
        startActivityForResult(eventDetailIntent, 1);
    }


}

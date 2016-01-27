package com.sagetech.conference_android.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.sagetech.conference_android.app.R;
import com.sagetech.conference_android.app.ui.Views.SimpleDividerLineDecorator;
import com.sagetech.conference_android.app.ui.adapters.ConferenceSessionListAdapter;
import com.sagetech.conference_android.app.ui.presenter.IConferenceSessionActivity;
import com.sagetech.conference_android.app.ui.presenter.IConferenceSessionListPresenter;
import com.sagetech.conference_android.app.ui.viewModel.ConferenceSessionViewModel;
import com.sagetech.conference_android.app.util.module.ConferenceSessionListModule;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Bind;
import timber.log.Timber;

/**
 * Created by adam on 2/21/15.
 */
public class ConferenceSessionListActivity extends InjectableActionBarActivity implements IConferenceSessionActivity, ConferenceSessionListAdapter.ConferenceSessionListOnClickListener {

    @Inject
    IConferenceSessionListPresenter presenter = null;

    @Bind(R.id.confSessionView)
    RecyclerView mRecyclerView;

    @Bind(R.id.txtConferenceName)
    TextView txtConferenceName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_session_items);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        setTitle("Event Sessions");

        this.txtConferenceName.setText(getIntent().getStringExtra("conferenceName"));

        Long conferenceId = getIntent().getLongExtra("id", 0);
        presenter.initialize(conferenceId);
    }

    @Override
    protected List<Object> getModules() {
        return Arrays.<Object>asList(new ConferenceSessionListModule(this));
    }


    @Override
    public void populateConferenceSessions(List<ConferenceSessionViewModel> conferenceSessions)
    {
        ConferenceSessionListAdapter mAdapter = new ConferenceSessionListAdapter(conferenceSessions, this);
        mRecyclerView.setAdapter(mAdapter);

        // Add the sticky headers decoration
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration( mAdapter );
        mRecyclerView.addItemDecoration( headersDecor );

        mRecyclerView.addItemDecoration( new SimpleDividerLineDecorator( this ) );

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }


    private void launchEventDetailActivity(Long sessionId) {
        Timber.d(String.format("Session Selected: %s", sessionId));

        Intent eventDetailIntent = new Intent(this, ConferenceSessionDetailActivity.class);
        eventDetailIntent.putExtra("id", sessionId);
        startActivity(eventDetailIntent);
    }


    @Override
    public void clicked(Long id) {
        launchEventDetailActivity(id);
    }
}

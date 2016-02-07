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
import com.sagetech.conference_android.app.ui.adapters.SessionPresenterAdapter;
import com.sagetech.conference_android.app.ui.presenter.IConferenceSessionDetailActivity;
import com.sagetech.conference_android.app.ui.presenter.IConferenceSessionDetailPresenter;
import com.sagetech.conference_android.app.ui.viewModel.ConferenceSessionDetailViewModel;
import com.sagetech.conference_android.app.ui.viewModel.ConferenceSessionType;
import com.sagetech.conference_android.app.util.module.ConferenceSessionDetailModule;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Bind;
import timber.log.Timber;

/**
 * This is a class for displaying the conference session detail.
 */
public class ConferenceSessionDetailActivity extends InjectableActionBarActivity implements
        IConferenceSessionDetailActivity
{

    @Inject
    IConferenceSessionDetailPresenter presenter;

    @Bind(R.id.txtTitle)
    TextView title;

    @Bind(R.id.txtSchedule)
    TextView schedule;

    @Bind(R.id.txtRoom)
    TextView room;

    @Bind(R.id.imgSessionType)
    ImageView sessionType;

    @Bind(R.id.txtDescription)
    TextView description;

    @Bind(R.id.presenterView)
    RecyclerView mPresenterView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_session_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        mPresenterView.setHasFixedSize(true);
        mPresenterView.setLayoutManager(new LinearLayoutManager(this));

        setTitle("Session Details");

        Long eventId = getIntent().getLongExtra("id", 0);
        presenter.initialize(eventId);
    }


    @Override
    public void populateWithConferenceSessionDetailView(ConferenceSessionDetailViewModel eventDetailViewModel)
    {
        title.setText(eventDetailViewModel.getTitle());

        schedule.setText(eventDetailViewModel.getEventDateAndDuration());

        room.setText(eventDetailViewModel.getRoomName());

        description.setText(eventDetailViewModel.getDescription());

        Picasso.with( this ).load( eventDetailViewModel.getType().getImage() ).into(sessionType);

        mPresenterView.setAdapter(new SessionPresenterAdapter(eventDetailViewModel.getPresenters()));
    }

    @Override
    protected List<Object> getModules()
    {
        return Arrays.<Object>asList(new ConferenceSessionDetailModule(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

                setResult( RESULT_OK );
                //Allows us to reuse the already running instance of this activity as opposed to
                //starting a new activity without any state data.
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpTo(this, intent);



                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

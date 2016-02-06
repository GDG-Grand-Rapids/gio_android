package com.sagetech.conference_android.app.ui.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sagetech.conference_android.app.Config;
import com.sagetech.conference_android.app.R;
import com.sagetech.conference_android.app.ui.presenter.IConferenceDetailActivity;
import com.sagetech.conference_android.app.ui.presenter.IConferenceDetailActivityPresenter;
import com.sagetech.conference_android.app.ui.presenter.IConferenceListPresenter;
import com.sagetech.conference_android.app.ui.viewModel.ConferenceDetailViewModel;
import com.sagetech.conference_android.app.util.module.ConferenceDetailModule;
import com.sagetech.conference_android.app.util.module.ConferenceListModule;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;
import timber.log.Timber;

public class ConferenceDetailActivity extends InjectableActionBarActivity implements IConferenceDetailActivity
{


    @Bind(R.id.txtConferenceName)
    TextView txtConferenceName;

    @Bind(R.id.conferenceImageView)
    ImageView imgConferenceImageView;

    @Bind(R.id.txtConferenceDate)
    TextView txtConferenceDate;

    @Bind(R.id.txtConferenceFullDesc)
    TextView txtConferenceFullDesc;

    @Inject
    IConferenceDetailActivityPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conference_detail);
        ButterKnife.bind(this);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if( actionBar != null )
        {
            actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.actionbar_conference_detail);
            actionBar.setDisplayHomeAsUpEnabled(true);

            ImageView imgConfMap = ButterKnife.findById( actionBar.getCustomView(), R.id.imgConfDetailMap );

            imgConfMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent conferenceSessionListIntent = new Intent( v.getContext(), ConferenceSessionListActivity.class);
                    conferenceSessionListIntent.putExtra("conferenceName", txtConferenceName.getText());
                    conferenceSessionListIntent.putExtra("conferenceDate", txtConferenceDate.getText());
                    startActivity(conferenceSessionListIntent);
                }
            });
        }

        // yes I am being lazy...
        presenter.initialize( Config.CONFERENCE_ID );

    }

    @Override
    protected List<Object> getModules() {
        return Arrays.<Object>asList(new ConferenceDetailModule(this));
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
    public void populateConferenceData(ConferenceDetailViewModel conferenceDetailViewModel) {
        // load the conference image
        Picasso.with(this)
                .load(conferenceDetailViewModel.getImageUrl())
                .placeholder(R.drawable.default_event)
                .error(R.drawable.default_event)
                .into(imgConferenceImageView);

        txtConferenceName.setText(conferenceDetailViewModel.getName());
        txtConferenceDate.setText(conferenceDetailViewModel.getDateInformation());
        txtConferenceFullDesc.setText(conferenceDetailViewModel.getFullDescription());
    }
}

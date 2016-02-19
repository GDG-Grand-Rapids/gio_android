package com.sagetech.conference_android.app.ui.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sagetech.conference_android.app.R;
import com.sagetech.conference_android.app.ui.adapters.ConferenceListAdapter;
import com.sagetech.conference_android.app.ui.presenter.IConferenceListActivity;
import com.sagetech.conference_android.app.ui.presenter.IConferenceListPresenter;
import com.sagetech.conference_android.app.ui.viewModel.ConferenceDataViewModel;
import com.sagetech.conference_android.app.util.module.ConferenceListModule;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Bind;
import timber.log.Timber;

public class ConferenceListActivity extends InjectableActionBarActivity implements IConferenceListActivity, ConferenceListAdapter.ConferenceOnClickListener {

    @Inject
    IConferenceListPresenter presenter;

    @Bind(R.id.confView)
    RecyclerView mRecyclerView;

    private ConferenceListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conferences);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_conf_list);

        ButterKnife.bind(this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        presenter.initialize();
    }

    @Override
    protected List<Object> getModules()
    {
        return Arrays.<Object>asList(new ConferenceListModule(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void populateConferences(List<ConferenceDataViewModel> datas) {
        mAdapter = new ConferenceListAdapter(datas, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void launchConferenceSessionsListActivity(Long conferenceId, View imgView)
    {
        Timber.d(String.format("Conference Selected: %s", conferenceId));


        Intent conferenceSessionListIntent = new Intent(this, ConferenceDetailActivity.class);
        String transitionName = getString(R.string.transition_conference_image);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        imgView,   // The view which starts the transition
                        transitionName    // The transitionName of the view we’re transitioning to
                );
        ActivityCompat.startActivity(this, conferenceSessionListIntent, options.toBundle());

    }

    @Override
    public void clicked(Long conferenceId, View imgView) {
        launchConferenceSessionsListActivity(conferenceId, imgView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }



}

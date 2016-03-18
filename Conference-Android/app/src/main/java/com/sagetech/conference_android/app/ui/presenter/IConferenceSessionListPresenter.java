package com.sagetech.conference_android.app.ui.presenter;

import com.sagetech.conference_android.app.util.ConferencePreferences;

/**
 * Created by carlushenry on 3/22/15.
 */
public interface IConferenceSessionListPresenter
{
    void initialize( long conferenceId, ConferencePreferences preferences );

    void onUnsubscribe();
}

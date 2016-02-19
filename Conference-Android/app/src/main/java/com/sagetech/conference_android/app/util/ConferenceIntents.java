package com.sagetech.conference_android.app.util;

import android.content.Context;
import android.content.Intent;

import com.sagetech.conference_android.app.ui.activities.ConferenceSessionListActivity;

/**
 * Created by willmetz on 2/7/16.
 */
public class ConferenceIntents
{
    public static final String CONFERENCE_NAME_KEY = "com.sagetech.conference_android.conferenceName";
    public static final String CONFERENCE_DATE_KEY = "com.sagetech.conference_android.conferenceDate";
    public static final String CONFERENCE_ID_KEY = "com.sagetech.conference_android.conferenceID";


    public static Intent getConferenceDetailsIntent( Context context, String confName, String confDate, long confID )
    {
        Intent conferenceSessionListIntent = new Intent( context, ConferenceSessionListActivity.class);
        conferenceSessionListIntent.putExtra( CONFERENCE_NAME_KEY, confName );
        conferenceSessionListIntent.putExtra( CONFERENCE_DATE_KEY, confDate );
        conferenceSessionListIntent.putExtra( CONFERENCE_ID_KEY, confID );

        return conferenceSessionListIntent;
    }
}

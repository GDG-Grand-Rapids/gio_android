package com.example.conference_android.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.conference_android.app.api.ConferenceController;
import com.example.conference_android.app.model.EventData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class EventDetailActivity extends Activity {
    private static final String TAG = "EventDetailActivity";
    private TextView title;
    private TextView location;
    private TextView description;
    private ConferenceController conferenceController;
    private Subscription subscription;
    private Integer eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        eventId = getIntent().getExtras().getInt("id");

        this.conferenceController = ((ConferenceApplication) getApplication()).getConferenceController();

        this.title = (TextView) findViewById(R.id.title);
        this.location = (TextView) findViewById(R.id.location);
        this.description = (TextView) findViewById(R.id.description);


        subscription = Observable.create(new Observable.OnSubscribe<EventData>() {
            @Override
            public void call(Subscriber<? super EventData> subscriber) {
                try {
                    Log.i(TAG, "Looking up first event leader");
                    EventData events = conferenceController.getEvent(eventId);
                    subscriber.onNext(events);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<EventData>() {
                    @Override
                    public void call(EventData o) {
                        Log.i(TAG, "Updating Screen");
                        updateScreen(o);
                    }
                });
        Log.i(TAG, "Completed the onCreate() method");
    }

    private void updateScreen(EventData eventData) {
        this.title.setText(eventData.getEvent().getTitle());
        this.location.setText(formatLocation(eventData.getStart_dttm(), eventData.getEnd_dttm(), eventData.getRoom().getName()));
        this.description.setText(eventData.getEvent().getDescription());
    }

    private String formatLocation(String startString, String endString, String room) {
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(startString);
            endDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(endString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormatter1 = new SimpleDateFormat("EEE, h:mm-", Locale.US);
        SimpleDateFormat timeFormatter2 = new SimpleDateFormat("h:mm aaa", Locale.US);

        return timeFormatter1.format(startDate) + timeFormatter2.format(endDate) + " in " + room;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}

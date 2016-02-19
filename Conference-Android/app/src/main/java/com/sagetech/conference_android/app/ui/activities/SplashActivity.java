package com.sagetech.conference_android.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

import com.sagetech.conference_android.app.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.app.AppObservable;
import timber.log.Timber;

/**
 * A splash screen activity that is displayed on app start for
 * a brief time.
 */
public class SplashActivity extends AppCompatActivity
{

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        subscription = AppObservable.bindActivity(this, Observable.just(1))
                .delay(3, TimeUnit.SECONDS)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Timber.i("onCompleted");
                        Intent intent = new Intent(getApplicationContext(), ConferenceDetailActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.i("onError", e);
                    }

                    @Override
                    public void onNext(Integer s) {
                        Timber.i("On Next");
                    }
                });
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        subscription.unsubscribe();
    }

}

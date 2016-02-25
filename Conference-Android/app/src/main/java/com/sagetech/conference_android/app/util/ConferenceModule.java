package com.sagetech.conference_android.app.util;


import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sagetech.conference_android.app.BuildConfig;
import com.sagetech.conference_android.app.ConferenceApplication;
import com.sagetech.conference_android.app.Config;
import com.sagetech.conference_android.app.api.ConferenceApi;
import com.sagetech.conference_android.app.api.ConferenceController;

import java.lang.reflect.Type;
import java.util.Date;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import timber.log.Timber;

@Module(
        injects = {
                ConferenceApplication.class
        }, library = true
)
public final class ConferenceModule
{

    private final ConferenceApplication app;

    public ConferenceModule(ConferenceApplication app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    Context provideContext( Application application )
    {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    ConferenceApi provideConferenceApi()
    {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return new Date(json.getAsJsonPrimitive().getAsLong());
                    }
                })
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setLog(new RestAdapter.Log() {

            public void log(String arg0) {
                Timber.i(arg0);
            }

        }).setEndpoint(Config.API_BASE_URL).setConverter(new GsonConverter(gson)).build();

        return restAdapter.create(ConferenceApi.class);
    }

    @Provides
    @Singleton
    ConferenceController provideConferenceController(ConferenceApi conferenceApi) {
        return new ConferenceController(conferenceApi);
    }

    @Provides
    @Singleton
    ConferencePreferences provideConferencePreferences( Context context )
    {
        return new ConferencePreferences( context );
    }

}

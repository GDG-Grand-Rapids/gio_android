package com.sagetech.conference_android.app.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.sagetech.conference_android.app.ConferenceApplication;
import com.sagetech.conference_android.app.Config;

import dagger.Module;


@Module(
        library = true
)
/**
 * Created by willmetz on 2/24/16.
 */
public class ConferencePreferences
{
    private SharedPreferences sharedPreferences;

    public ConferencePreferences( @ForApplication Context context )
    {
        sharedPreferences = context.getSharedPreferences(Config.CONFERENCE_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }


    /*
     * Used to mark a ConferenceSession as a favorite.
     *
     * @param sessionID The id of the session to mark as a favorite
     */
    public void setSessionFavorite( long sessionID )
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean( String.valueOf( sessionID ), true );

        editor.apply();
    }

    /*
     * Used to clear a ConferenceSession as a favorite.
     *
     * @param sessionID The id of the session remove from favorite list
     */
    public void clearSessionFavorite( long sessionID )
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove( String.valueOf( sessionID ) );

        editor.apply();
    }

    /*
     * Determines if a given session ID is a favorite
     *
     * @return true if favorite, false otherwise
     */
    public boolean isSessionFavorite( long sessionID )
    {
        return sharedPreferences.getBoolean( String.valueOf( sessionID ), false );
    }


}

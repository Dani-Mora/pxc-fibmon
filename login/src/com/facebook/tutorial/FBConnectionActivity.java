package com.facebook.tutorial;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

public abstract class FBConnectionActivity extends Activity {
    
	public static final String TAG = "FACEBOOK";
    private Facebook mFacebook;
    public static final String APP_ID = "389992591018483";  										// ID de l'aplicació
    private AsyncFacebookRunner mAsyncRunner;
    private static final String[] PERMS = new String[] { "read_stream" }; 					// Permissos de l'aplicació
    private SharedPreferences sharedPrefs;
    private Context mContext;
    private static String tokenUser = "";																			// Token de l'usuari

    private TextView username;
    private ProgressBar pb;

    public void setConnection() {
            mContext = this;
            mFacebook = new Facebook(APP_ID);
            mAsyncRunner = new AsyncFacebookRunner(mFacebook);
    }

    public void getID(TextView txtUserName, ProgressBar progbar) {
    		System.out.println("Get id: ");
    	
            username = txtUserName;
            pb = progbar;
            if (isSession()) {
            		System.out.println("Session valid");
                    Log.d(TAG, "sessionValid");
                    mAsyncRunner.request("me", new IDRequestListener());
            } else {
                    // no logged in, so relogin
            		System.out.println("Session not valid");
                    Log.d(TAG, "sessionNOTValid, relogin");
                    mFacebook.authorize(this, PERMS, new LoginDialogListener());
            }
    }
    
    public String getToken() {
    	if(isSession()) {
    		return tokenUser;
    	}
    	return "";
    }

    public boolean isSession() {
            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            tokenUser = sharedPrefs.getString("access_token", "x");
            Long expires = sharedPrefs.getLong("access_expires", -1);
            Log.d(TAG, tokenUser);

            System.out.println("Session Token: " + tokenUser);
            
            if (tokenUser != null && expires != -1) {
                    mFacebook.setAccessToken(tokenUser);
                    mFacebook.setAccessExpires(expires);
            }
            return mFacebook.isSessionValid();
    }

    private class LoginDialogListener implements DialogListener {

            @Override
            public void onComplete(Bundle values) {
            	System.out.println("Login complete");
            	
                    Log.d(TAG, "LoginONComplete");
                    String token = mFacebook.getAccessToken();
                    long token_expires = mFacebook.getAccessExpires();
                    Log.d(TAG, "AccessToken: " + token);
                    Log.d(TAG, "AccessExpires: " + token_expires);
                    sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                    sharedPrefs.edit().putLong("access_expires", token_expires).commit();
                    sharedPrefs.edit().putString("access_token", token).commit();
                    mAsyncRunner.request("me", new IDRequestListener());
            }

            @Override
            public void onFacebookError(FacebookError e) {
            		System.out.println("FacebookError: " + e.getMessage());
                    Log.d(TAG, "FacebookError: " + e.getMessage());
            }

            @Override
            public void onError(DialogError e) {
            	System.out.println("DialogError: " + e.getMessage());
                    Log.d(TAG, "Error: " + e.getMessage());
            }

            @Override
            public void onCancel() {
            	System.out.println("Canceled");
                    Log.d(TAG, "OnCancel");
            }
    }

    private class IDRequestListener implements RequestListener {

            @Override
            public void onComplete(String response, Object state) {
                    try {
                            Log.d(TAG, "IDRequestONComplete");
                            Log.d(TAG, "Response: " + response.toString());
                            JSONObject json = Util.parseJson(response);

                            final String id = json.getString("id");
                            final String name = json.getString("name");
                            FBConnectionActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                            username.setText("Welcome: " + name+"\n ID: "+id);
                                            pb.setVisibility(ProgressBar.GONE);
                                    }
                            });
                    } catch (JSONException e) {
                            Log.d(TAG, "JSONException: " + e.getMessage());
                    } catch (FacebookError e) {
                            Log.d(TAG, "FacebookError: " + e.getMessage());
                    }
            }

            @Override
            public void onIOException(IOException e, Object state) {
                    Log.d(TAG, "IOException: " + e.getMessage());
            }

            @Override
            public void onFileNotFoundException(FileNotFoundException e,
                            Object state) {
                    Log.d(TAG, "FileNotFoundException: " + e.getMessage());
            }

            @Override
            public void onMalformedURLException(MalformedURLException e,
                            Object state) {
                    Log.d(TAG, "MalformedURLException: " + e.getMessage());
            }

            @Override
            public void onFacebookError(FacebookError e, Object state) {
                    Log.d(TAG, "FacebookError: " + e.getMessage());
            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            mFacebook.authorizeCallback(requestCode, resultCode, data);
    }
}
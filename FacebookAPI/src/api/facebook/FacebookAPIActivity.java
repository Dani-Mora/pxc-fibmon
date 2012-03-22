package api.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public class FacebookAPIActivity extends Activity {

    Facebook facebook = new Facebook("389992591018483");
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
    private AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);

    @Override
    public void onResume() {    
        super.onResume();
        facebook.extendAccessTokenIfNeeded(this, null);
    }
    
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /*
         * Get existing access_token if any
         */
        mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        if(access_token != null) {
            facebook.setAccessToken(access_token);
        }
        if(expires != 0) {
            facebook.setAccessExpires(expires);
        }
                
        /*
         * Only call authorize if the access_token has expired.
         */
        if(!facebook.isSessionValid()) {

            facebook.authorize(this, new String[] {}, new DialogListener() {
                @Override
                public void onComplete(Bundle values) {
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("access_token", facebook.getAccessToken());
                    editor.putLong("access_expires", facebook.getAccessExpires());
                    editor.commit();
                }
    
                @Override
                public void onFacebookError(FacebookError error) {}
    
                @Override
                public void onError(DialogError e) {}
    
                @Override
                public void onCancel() {}
            });
        }
               
        String method = "DELETE";
        Bundle params = new Bundle();
        /*
         * this will revoke 'publish_stream' permission
         * Note: If you don't specify a permission then this will de-authorize the application completely.
         */
        params.putString("permission", "publish_stream");
        mAsyncRunner.request("/me/permissions", params, method, new IDRequestListener(), null);
        
    }

    
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
    
    private class IDRequestListener implements RequestListener {

        @Override
        public void onComplete(String response, Object state) {

        }

        @Override
        public void onIOException(IOException e, Object state) {

        }

        @Override
        public void onFileNotFoundException(FileNotFoundException e,
                        Object state) {
              
        }

        @Override
        public void onMalformedURLException(MalformedURLException e,
                        Object state) {
                
        }

        @Override
        public void onFacebookError(FacebookError e, Object state) {
               
        }

}
}
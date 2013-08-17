package me.mikec.tipple;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.androauth.api.TwitterApi;
import com.androauth.oauth.OAuth10Request;
import com.androauth.oauth.OAuth10Service;
import com.androauth.oauth.OAuth10Token;
import com.androauth.oauth.OAuth20Token;
import com.androauth.oauth.OAuthRequest;
import com.androauth.oauth.OAuthRequest.OnRequestCompleteListener;
import com.androauth.oauth.OAuthService;
import com.twotoasters.android.hoot.HootResult;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import me.mikec.tipple.Twitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context ctx = getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    @Override
    protected void onStart() {
        Log.v("1", "In onStart");
        super.onStart();
        getAccessToken();
    }
    public void getAccessToken() {
        if (Twitter.getInstance().hasToken()) {
            showTweets();
            return;
        }
        if (prefs.contains("access_token")) {
            Twitter.getInstance().setToken(new OAuth10Token(prefs.getString("access_token", ""), prefs.getString("user_secret", "")));
            Log.v("1", "Found access token in preferences");
            showTweets();
        }
        else {
            Intent intent = new Intent(this, TwitterAuth.class);
            startActivityForResult(intent, 0);
            Log.v("1", "Getting access token from intent");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String token = data.getStringExtra("access_token");
        String key = data.getStringExtra("user_secret");
        // save key and things
        SharedPreferences.Editor editor = getEditor();
        editor.putString("access_token", token);
        editor.putString("user_secret", key);
        editor.commit();
        showTweets();
    }

    public SharedPreferences.Editor getEditor() {
        return prefs.edit();
    }

    public void displayResults(String res) {
        try {
            JSONArray j = new JSONArray(res);
            ArrayList<Tweet> tweets = new ArrayList<Tweet>();

            for (int i = 0; i < j.length(); i++) {
                JSONObject jo = j.getJSONObject(i);
                String msg = jo.getString("text");
                JSONObject userDetails = jo.getJSONObject("user");
                String userName = userDetails.getString("screen_name");
                String userFullName = userDetails.getString("name");
                String userIcon = userDetails.getString("profile_image_url");
                Tweet tw = new Tweet(msg, userFullName, userName, userIcon);
                tweets.add(tw);

            }
            TweetAdapter adapter = new TweetAdapter(tweets, this);
            ListView lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showTweets() {
        String baseUrl = "https://api.twitter.com/1.1/statuses/home_timeline.json";
        Twitter t = Twitter.getInstance();
        OAuth10Request request = OAuthRequest.newInstance(baseUrl, t.token, t.service, new OnRequestCompleteListener() {

            @Override
            public void onSuccess(HootResult result) {
                String res = result.getResponseString();
                displayResults(res);
            }

            @Override
            public void onNewAccessTokenReceived(OAuth20Token token) {
            }

            @Override
            public void onFailure(HootResult result) {
            }
        });
        Map<String,String> queryParameters = new HashMap<String,String>();
        queryParameters.put("screen_name", "remember_attica");
        queryParameters.put("count", "30");
        request.setRequestParams(queryParameters);
        request.get();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}

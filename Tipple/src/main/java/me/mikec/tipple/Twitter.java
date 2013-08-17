package me.mikec.tipple;

import android.webkit.WebView;

import com.androauth.api.TwitterApi;
import com.androauth.oauth.OAuth10Service;
import com.androauth.oauth.OAuth10Token;
import com.androauth.oauth.OAuthService;


public class Twitter {

    OAuth10Service service;
    OAuth10Token token;
    WebView webview;

    private static final Twitter instance = new Twitter();

    public void setToken(OAuth10Token token) {
        this.token = token;
    }

    public boolean hasToken() {
        return (token != null);
    }

    public void setWebView(WebView wv) {
        webview = wv;
    }
    private Twitter() {
        service = OAuthService.newInstance(new TwitterApi(), TippleApiKey.API_CONSUMER_KEY, TippleApiKey.API_CONSUMER_SECRET, null);
        service.setApiCallback("tipple://auth");
    }

    public static Twitter getInstance() {
        return instance;
    }
}

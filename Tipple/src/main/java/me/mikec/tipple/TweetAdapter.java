package me.mikec.tipple;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mike on 17/08/13.
 */
public class TweetAdapter extends BaseAdapter {
    private ArrayList<Tweet> tweets;
    Context _c;

    TweetAdapter (ArrayList<Tweet> data, Context c) {
        tweets = data;
        _c = c;
    }
    @Override
    public int getCount() {
        return tweets.size();
    }

    @Override
    public Object getItem(int position) {
        return tweets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View v = convertView;
        if (v == null)
        {
            LayoutInflater vi = (LayoutInflater)_c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_tweet, null);
        }
        assert v != null;
        ImageView image = (ImageView) v.findViewById(R.id.icon);
        TextView snView = (TextView)v.findViewById(R.id.screenName);
        TextView fnView = (TextView)v.findViewById(R.id.fullName);
        TextView msgView = (TextView)v.findViewById(R.id.message);

        Tweet msg = tweets.get(position);
        new DownloadImageTask(image).execute(msg.iconURL);
        snView.setText("@" + msg.userName);
        fnView.setText(msg.fullName);
        msgView.setText(msg.text);

        return v;
    }
}

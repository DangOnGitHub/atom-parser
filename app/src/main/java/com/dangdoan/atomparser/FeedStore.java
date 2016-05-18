package com.dangdoan.atomparser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import rx.Observable;

/**
 * Created by dangdoan on 5/11/16.
 */
public class FeedStore {
    public Observable<List<Feed>> getRecentQuestions() {
        FeedParser parser = new FeedParser();
        return Observable.create(subscriber -> {
            InputStream stream = null;
            try {
                stream = downloadUrl("http://stackoverflow.com/feeds");
                List<Feed> entries = parser.parse(stream);
                subscriber.onNext(entries);
                subscriber.onCompleted();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                subscriber.onError(e);
            } catch (IOException e) {
                e.printStackTrace();
                subscriber.onError(e);
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }
                }
            }
        });
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setReadTimeout(10000 /* milliseconds */);
//        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
}

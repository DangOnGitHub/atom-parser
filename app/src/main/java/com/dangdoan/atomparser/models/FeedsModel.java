package com.dangdoan.atomparser.models;

import com.dangdoan.atomparser.FeedsMvp;
import com.dangdoan.atomparser.network.FirebaseApi;

/**
 * Created by dangdoan on 5/18/16.
 */
public class FeedsModel implements FeedsMvp.Model {
    @Override
    public void markRead(Feed feed) {
        feed.markRead();
    }

    @Override
    public void saveFeed(Feed feed) {
        FirebaseApi.saveFeed(feed);
    }
}

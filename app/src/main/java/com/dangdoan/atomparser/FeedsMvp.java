package com.dangdoan.atomparser;

import com.dangdoan.atomparser.models.Feed;

import java.util.List;

/**
 * Created by dangdoan on 5/18/16.
 */
public interface FeedsMvp {
    interface Model {
        void markRead(Feed feed);

        void saveFeed(Feed feed);
    }

    interface View extends BaseMvp.View {
        void showFeeds(List<Feed> feeds);

        void showError(String message);

        void showProgress();

        void hideProgress();

        void markRead(Feed feed);
    }

    interface Presenter extends BaseMvp.Presenter<FeedsMvp.View> {
        void getFeeds();

        void subcribeToReadFeed();

        void readFeed(Feed feed);
    }
}

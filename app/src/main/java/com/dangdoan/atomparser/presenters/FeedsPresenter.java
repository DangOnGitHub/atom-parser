package com.dangdoan.atomparser.presenters;

import android.support.annotation.NonNull;

import com.dangdoan.atomparser.FeedsMvp;
import com.dangdoan.atomparser.models.Feed;
import com.dangdoan.atomparser.models.FeedsModel;
import com.dangdoan.atomparser.network.FirebaseApi;
import com.dangdoan.atomparser.utils.FeedParser;
import com.dangdoan.atomparser.utils.NetworkUtility;
import com.firebase.client.ChildEventListener;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dangdoan on 5/18/16.
 */
public class FeedsPresenter extends BasePresenter<FeedsMvp.View> implements FeedsMvp.Presenter {
    private Subscription mSubscription;
    private FeedsMvp.Model mModel;
    private ChildEventListener mListener;

    public FeedsPresenter() {
        mModel = new FeedsModel();
    }

    @Override
    public void detachView() {
        super.detachView();

        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        if (mListener != null) {
            FirebaseApi.unsubcribeToReadFeed(mListener);
        }
    }

    @Override
    public void getFeeds() {
        checkViewAttached();
        getView().showProgress();
        mSubscription = getStackOverflowFeeds()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(feeds -> {
                    getView().hideProgress();
                    getView().showFeeds(feeds);
                }, throwable -> {
                    getView().hideProgress();
                    getView().showError(throwable.getMessage());
                });
    }

    @Override
    public void subcribeToReadFeed() {
        mListener = FirebaseApi.subcribeToReadFeed(new FirebaseApi.OnFeedReadHandler() {
            @Override
            public void onFeedRead(Feed readFeed) {
                getView().markRead(readFeed);
            }

            @Override
            public void onError(Throwable throwable) {
                getView().showError(throwable.getMessage());
            }
        });
    }

    @NonNull
    private Observable<List<Feed>> getStackOverflowFeeds() {
        return Observable.create(subscriber -> {
            FeedParser parser = new FeedParser();
            InputStream inputStream = null;
            try {
                inputStream = NetworkUtility.downloadUrl("http://stackoverflow.com/feeds");
                List<Feed> feeds = parser.parse(inputStream);
                subscriber.onNext(feeds);
                subscriber.onCompleted();
            } catch (XmlPullParserException e) {
                subscriber.onError(e);
            } catch (IOException e) {
                subscriber.onError(e);
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public void readFeed(Feed feed) {
        if (!feed.isRead()) {
            mModel.markRead(feed);
            mModel.saveFeed(feed);
        }
    }
}

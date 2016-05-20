package com.dangdoan.atomparser.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dangdoan.atomparser.models.Feed;
import com.dangdoan.atomparser.views.FeedHolder;

import java.util.Collections;
import java.util.List;

/**
 * Created by dangdoan on 5/11/16.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedHolder> {
    private List<Feed> mFeeds;
    private FeedHolder.OnFeedClickListener mListener;

    public FeedAdapter(FeedHolder.OnFeedClickListener listener) {
        mFeeds = Collections.emptyList();
        mListener = listener;
    }

    @Override
    public FeedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
        return new FeedHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedHolder holder, int position) {
        Feed feed = mFeeds.get(position);
        holder.bindEntry(feed);
        holder.setListener(mListener);
    }

    @Override
    public int getItemCount() {
        return mFeeds.size();
    }

    public List<Feed> getFeeds() {
        return mFeeds;
    }

    public void setFeeds(List<Feed> feeds) {
        mFeeds = feeds;
    }
}

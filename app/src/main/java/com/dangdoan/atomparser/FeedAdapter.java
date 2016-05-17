package com.dangdoan.atomparser;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by dangdoan on 5/11/16.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedHolder> {
    private List<FeedEntry> mEntries;
    private FeedHolder.OnFeedClickListener mListener;

    public FeedAdapter(List<FeedEntry> entries, FeedHolder.OnFeedClickListener listener) {
        mEntries = entries;
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
        FeedEntry entry = mEntries.get(position);
        holder.bindEntry(entry);
        holder.setListener(mListener);
    }

    @Override
    public int getItemCount() {
        return mEntries.size();
    }
}

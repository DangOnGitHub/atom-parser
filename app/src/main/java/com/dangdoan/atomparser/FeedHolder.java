package com.dangdoan.atomparser;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by dangdoan on 5/11/16.
 */
public class FeedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(android.R.id.text1)
    TextView mTextView1;
    @BindView(android.R.id.text2)
    TextView mTextView2;
    private Feed mEntry;
    private OnFeedClickListener mListener;

    public FeedHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    public void bindEntry(Feed entry) {
        mEntry = entry;
        DateTime dateTime = new DateTime(entry.getPublished());
        mTextView1.setText(dateTime.toString(DateTimeFormat.shortDate()));
        mTextView2.setText(entry.getTitle());
        if (!entry.isRead()) {
            mTextView1.setTypeface(null, Typeface.BOLD);
            mTextView2.setTypeface(null, Typeface.BOLD);
        } else {
            mTextView1.setTypeface(null, Typeface.NORMAL);
            mTextView2.setTypeface(null, Typeface.NORMAL);
        }
    }

    public void setListener(OnFeedClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        mListener.onFeedClick(mEntry);
    }

    public interface OnFeedClickListener {
        void onFeedClick(Feed entry);
    }
}

package com.dangdoan.atomparser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by dangdoan on 5/11/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedEntry {
    @JsonProperty("id")
    private String mId;
    @JsonProperty("title")
    private String mTitle;
    @JsonProperty("summary")
    private String mSummary;
    @JsonProperty("link")
    private String mLink;
    @JsonProperty("published")
    private long mPublished;
    private boolean mIsRead = false;

    public FeedEntry() {

    }

    public FeedEntry(String id, String title, String summary, String link, long published) {
        mId = id;
        mTitle = title;
        mSummary = summary;
        mLink = link;
        mPublished = published;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSummary() {
        return mSummary;
    }

    public String getLink() {
        return mLink;
    }

    public long getPublished() {
        return mPublished;
    }

    public boolean isRead() {
        return mIsRead;
    }

    public void markRead() {
        mIsRead = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedEntry feedEntry = (FeedEntry) o;

        return mId.equals(feedEntry.mId);

    }

    @Override
    public int hashCode() {
        return mId.hashCode();
    }
}

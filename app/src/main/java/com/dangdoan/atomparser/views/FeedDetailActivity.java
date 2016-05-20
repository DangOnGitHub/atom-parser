package com.dangdoan.atomparser.views;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class FeedDetailActivity extends BaseActivity {
    public static Intent newIntent(Context context, String link) {
        Intent intent = new Intent(context, FeedDetailActivity.class);
        intent.putExtra(FeedDetailFragment.ARG_LINK, link);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return FeedDetailFragment.newInstance(getIntent().getStringExtra(FeedDetailFragment.ARG_LINK));
    }
}

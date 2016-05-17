package com.dangdoan.atomparser;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class DetailActivity extends SingleFragmentActivity {
    public static Intent newIntent(Context context, String link) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailFragment.ARG_LINK, link);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return DetailFragment.newInstance(getIntent().getStringExtra(DetailFragment.ARG_LINK));
    }
}

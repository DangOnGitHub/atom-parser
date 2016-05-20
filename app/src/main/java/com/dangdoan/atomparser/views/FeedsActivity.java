package com.dangdoan.atomparser.views;

import android.support.v4.app.Fragment;

public class FeedsActivity extends BaseActivity {
    @Override
    protected Fragment createFragment() {
        return FeedsFragment.newInstance();
    }
}
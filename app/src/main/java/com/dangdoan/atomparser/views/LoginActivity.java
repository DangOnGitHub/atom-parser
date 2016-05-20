package com.dangdoan.atomparser.views;

import android.support.v4.app.Fragment;

public class LoginActivity extends BaseActivity {
    @Override
    protected Fragment createFragment() {
        return LoginFragment.newInstance();
    }
}

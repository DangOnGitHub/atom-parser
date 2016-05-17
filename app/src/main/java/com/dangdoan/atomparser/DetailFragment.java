package com.dangdoan.atomparser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dangdoan on 5/12/16.
 */
public class DetailFragment extends Fragment {
    public static final String ARG_LINK = "arg_link";
    @BindView(R.id.web_view)
    WebView mWebView;
    private Unbinder mUnbinder;
    private String mLink;

    public DetailFragment() {

    }

    public static DetailFragment newInstance(String link) {
        Bundle args = new Bundle();
        args.putString(ARG_LINK, link);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLink = getArguments().getString(ARG_LINK);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mWebView.loadUrl(mLink);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mUnbinder.unbind();
    }
}

package com.dangdoan.atomparser.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.dangdoan.atomparser.R;

import butterknife.BindView;

/**
 * Created by dangdoan on 5/12/16.
 */
public class FeedDetailFragment extends BaseFragment {
    public static final String ARG_LINK = "arg_link";
    @BindView(R.id.web_view)
    WebView mWebView;
    private String mLink;

    public FeedDetailFragment() {

    }

    public static FeedDetailFragment newInstance(String link) {
        Bundle args = new Bundle();
        args.putString(ARG_LINK, link);
        FeedDetailFragment fragment = new FeedDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLink = getArguments().getString(ARG_LINK);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_feed_detail;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mWebView.loadUrl(mLink);
        return view;
    }
}

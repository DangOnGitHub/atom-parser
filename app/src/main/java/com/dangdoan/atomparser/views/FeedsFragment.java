package com.dangdoan.atomparser.views;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dangdoan.atomparser.FeedsMvp;
import com.dangdoan.atomparser.utils.LoginManager;
import com.dangdoan.atomparser.R;
import com.dangdoan.atomparser.adapters.FeedAdapter;
import com.dangdoan.atomparser.models.Feed;
import com.dangdoan.atomparser.network.FacebookApi;
import com.dangdoan.atomparser.presenters.FeedsPresenter;

import java.util.List;

import butterknife.BindView;


/**
 * Created by dangdoan on 5/18/16.
 */
public class FeedsFragment extends BaseFragment implements FeedsMvp.View, FeedHolder.OnFeedClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private FeedsMvp.Presenter mPresenter;
    private FeedAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    public FeedsFragment() {

    }

    public static FeedsFragment newInstance() {
        return new FeedsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (!FacebookApi.isLoggedIn()) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
            return;
        }

        mPresenter = new FeedsPresenter();
        mPresenter.attachView(this);
        mAdapter = new FeedAdapter(this);
        mPresenter.getFeeds();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_feeds;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        hideProgress();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                LogoutDialog dialog = new LogoutDialog();
                dialog.show(getFragmentManager(), "logout");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showFeeds(List<Feed> feeds) {
        mAdapter.setFeeds(feeds);
        mAdapter.notifyDataSetChanged();
        mPresenter.subcribeToReadFeed();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
        }
        mProgressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void markRead(Feed feed) {
        int position = mAdapter.getFeeds().indexOf(feed);
        if (position != -1) {
            mAdapter.getFeeds().get(position).markRead();
            mAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void onFeedClick(Feed feed) {
        mPresenter.readFeed(feed);
        Intent intent = FeedDetailActivity.newIntent(getActivity(), feed.getLink());
        startActivity(intent);
    }

    public static class LogoutDialog extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure you want to log out?")
                    .setPositiveButton(android.R.string.yes, (dialog, id) -> {
                        LoginManager.getInstance().logout();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    })
                    .setNegativeButton(android.R.string.cancel, null);
            return builder.create();
        }
    }
}

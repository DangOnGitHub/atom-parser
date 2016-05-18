package com.dangdoan.atomparser;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.ChildEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dangdoan on 5/11/16.
 */
public class MainFragment extends Fragment implements FeedHolder.OnFeedClickListener {
    private static final String LOG_TAG = MainFragment.class.getSimpleName();
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private Unbinder mUnbinder;
    private List<Feed> mEntries = new ArrayList<>();
    private Subscription mSubscription;
    private ChildEventListener mChildEventListener;
    private FeedAdapter mAdapter;

    public MainFragment() {

    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (!FirebaseApi.isLoggedIn()) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        FeedStore store = new FeedStore();
        mSubscription = store
                .getRecentQuestions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(feedEntries -> {
                    mEntries = feedEntries;
                    setupAdatper();
                    progressDialog.dismiss();
                    FirebaseApi.subcribeToReadFeed(new FirebaseApi.OnFeedReadHandler() {
                        @Override
                        public void onFeedRead(Feed readFeed) {
                            int location = mEntries.indexOf(readFeed);
                            if (location != -1) {
                                mEntries.get(location).markRead();
                                mAdapter.notifyItemChanged(location);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Log.d(LOG_TAG, throwable.getMessage());
                        }
                    });
                }, throwable -> {
                    Log.d(LOG_TAG, throwable.getMessage());
                    progressDialog.dismiss();
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdatper();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        if (mChildEventListener != null) {
            FirebaseApi.unsubcribeToReadFeed(mChildEventListener);
        }
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
    public void onFeedClick(Feed entry) {
        FirebaseApi.markFeedRead(entry);
        Intent intent = DetailActivity.newIntent(getActivity(), entry.getLink());
        startActivity(intent);
    }

    private void setupAdatper() {
        if (isAdded()) {
            mAdapter = new FeedAdapter(mEntries, this);
            mRecyclerView.setAdapter(mAdapter);
        }
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

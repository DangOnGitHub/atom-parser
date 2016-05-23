package com.dangdoan.atomparser.presenters;

import com.dangdoan.atomparser.BaseMvp;

/**
 * Created by dangdoan on 5/18/16.
 */
public class BasePresenter<V> implements BaseMvp.Presenter<V> {
    private V mView;

    @Override
    public void attachView(V view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    public V getView() {
        return mView;
    }

    public void checkViewAttached() {
        if (mView == null) throw new MvpViewNotAttachedException();
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(View) before" +
                    " requesting data to the Presenter");
        }
    }
}

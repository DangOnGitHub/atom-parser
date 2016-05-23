package com.dangdoan.atomparser;

/**
 * Created by dangdoan on 5/18/16.
 */
public interface BaseMvp {
    interface Presenter<V> {
        void attachView(V view);

        void detachView();
    }
}

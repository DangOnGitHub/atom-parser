package com.dangdoan.atomparser;

/**
 * Created by dangdoan on 5/18/16.
 */
public interface BaseMvp {
    interface View {

    }

    interface Presenter<V extends BaseMvp.View> {
        void attachView(V view);

        void detachView();
    }
}

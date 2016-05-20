package com.dangdoan.atomparser.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;

import com.dangdoan.atomparser.utils.LoginManager;
import com.dangdoan.atomparser.utils.OnLoginManagerResult;
import com.dangdoan.atomparser.R;
import com.facebook.CallbackManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dangdoan on 5/14/16.
 */
public class LoginFragment extends BaseFragment {
    private static final String LOG_TAG = LoginFragment.class.getSimpleName();
    @BindView(R.id.login_button)
    Button mLoginButton;
    private CallbackManager mCallbackManager;

    public LoginFragment() {

    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_login;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.login_button)
    public void login() {
        LoginManager.getInstance().login(this, mCallbackManager, new OnLoginManagerResult() {
            @Override
            public void onSuccess() {
                startActivity(new Intent(getActivity(), FeedsActivity.class));
                getActivity().finish();
            }

            @Override
            public void onFailure(String message) {
                Log.d(LOG_TAG, message);
            }
        });
    }
}

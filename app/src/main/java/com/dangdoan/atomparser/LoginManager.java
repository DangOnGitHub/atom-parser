package com.dangdoan.atomparser;

import android.support.v4.app.Fragment;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

interface OnLoginResult {
    void onSuccess();

    void onFailure(String message);
}

/**
 * Created by dangdoan on 5/14/16.
 */
public class LoginManager {
    private static LoginManager sInstance;

    public static LoginManager getInstance() {
        if (sInstance == null) {
            sInstance = new LoginManager();
        }
        return sInstance;
    }

    public void login(Fragment fragment, CallbackManager callbackManager, OnLoginResult result) {
        Firebase.AuthResultHandler handler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                FirebaseApi.storeUserInformation(authData);
                result.onSuccess();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                result.onFailure(firebaseError.getMessage());
            }
        };
        if (FacebookApi.isLoggedIn()) {
            FirebaseApi.login(FacebookApi.getAccessToken(), handler);
        } else {
            FacebookApi.login(fragment, callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    FirebaseApi.login(loginResult.getAccessToken().getToken(), handler);
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {
                    result.onFailure(error.getMessage());
                }
            });
        }
    }

    public void logout() {
        FacebookApi.logout();
        FirebaseApi.logout();
    }
}

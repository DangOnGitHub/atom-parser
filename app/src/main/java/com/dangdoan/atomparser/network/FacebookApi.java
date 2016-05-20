package com.dangdoan.atomparser.network;

import android.support.v4.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dangdoan on 5/14/16.
 */
public class FacebookApi {
    public static void login(Fragment fragment, CallbackManager callbackManager, FacebookCallback<LoginResult> callback) {
        List<String> permissions = new ArrayList<>();
        permissions.add("public_profile");
        permissions.add("user_friends");
        permissions.add("email");
        LoginManager.getInstance().logInWithReadPermissions(fragment, permissions);
        LoginManager.getInstance().registerCallback(callbackManager, callback);
    }

    public static void logout() {
        LoginManager.getInstance().logOut();
    }

    public static boolean isLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    public static String getAccessToken() {
        return AccessToken.getCurrentAccessToken().getToken();
    }
}

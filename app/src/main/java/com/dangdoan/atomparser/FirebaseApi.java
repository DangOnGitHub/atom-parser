package com.dangdoan.atomparser;

import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by dangdoan on 5/14/16.
 */
public class FirebaseApi {
    private static Firebase sRef = new Firebase("https://atom-parser.firebaseio.com/");

    public static void login(String token, Firebase.AuthResultHandler handler) {
        sRef.authWithOAuthToken("facebook", token, handler);
    }

    public static void logout() {
        sRef.unauth();
    }

    public static boolean isLoggedIn() {
        return sRef.getAuth() != null;
    }

    public static void storeUserInformation(AuthData authData) {
        sRef.child(authData.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    Map<String, Object> values = new HashMap<>();
                    values.put("displayName", authData.getProviderData().get("displayName").toString());
                    sRef.child(authData.getUid()).setValue(values);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void markFeedRead(FeedEntry entry) {
        if (entry.isRead()) {
            return;
        }
        entry.markRead();
        sRef.child(sRef.getAuth().getUid()).child("read").push().setValue(entry);
    }

    public static Observable<List<FeedEntry>> getReadFeeds() {
        return Observable.create(subscriber -> {
            Query readRef = sRef.child(sRef.getAuth().getUid()).child("read").orderByChild("published");
            readRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<FeedEntry> entries = new ArrayList<>();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            entries.add(snapshot.getValue(FeedEntry.class));
                        }
                    }
                    subscriber.onNext(entries);
                    subscriber.onCompleted();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    subscriber.onError(firebaseError.toException());
                }
            });
        });
    }

    public static void getReadFeedsIds(long afterPublishedDate) {
        List<String> ids = new ArrayList<>();
        Query readRef = sRef.child(sRef.getAuth().getUid()).child("read").orderByChild("published");
        readRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot feedSnapshot : dataSnapshot.getChildren()) {
                        FeedEntry feedEntry = feedSnapshot.getValue(FeedEntry.class);
                        Log.i("FirebaseApi", feedEntry.getTitle());
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static ChildEventListener subcribeToReadFeed(OnFeedReadHandler handler) {
        Firebase ref = sRef.child(sRef.getAuth().getUid()).child("read");
        return ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                handler.onFeedRead(dataSnapshot.getValue(FeedEntry.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                handler.onError(firebaseError.toException());
            }
        });
    }

    public static void unsubcribeToReadFeed(ChildEventListener listener) {
        sRef.child(sRef.getAuth().getUid()).child("read").removeEventListener(listener);
    }

    interface OnFeedReadHandler {
        void onFeedRead(FeedEntry readFeed);

        void onError(Throwable throwable);
    }
}

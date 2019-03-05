package com.example.android.androiduber.service;

import com.example.android.androiduber.common.Common;
import com.example.android.androiduber.model.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        updateTokenToServer(refreshToken);
    }

    private void updateTokenToServer(String refreshToken) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tokens = database.getReference(Common.token_tbl);

        Token token = new Token(refreshToken);
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            tokens.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(token);
    }
}

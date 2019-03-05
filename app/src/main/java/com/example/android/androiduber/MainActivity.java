package com.example.android.androiduber;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.android.androiduber.common.Common;
import com.example.android.androiduber.model.Usr;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    RelativeLayout rootLayout;


    Button btnSignIn, btnRegister;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference(Common.usr_driver_tbl);


        btnSignIn = (Button) findViewById(R.id.signin_btn);
        btnRegister = (Button) findViewById(R.id.register_btn);
        rootLayout = (RelativeLayout) findViewById(R.id.rootlayout);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginDialog();
            }
        });
    }

    private void showLoginDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("LOGIN");
        dialog.setMessage("please use email to Login");

        LayoutInflater inflater = LayoutInflater.from(this);
        View layoutLogin = inflater.inflate(R.layout.layout_login, null);

        final MaterialEditText emailEditText = layoutLogin.findViewById(R.id.email_edittext);
        final MaterialEditText passwordEditText = layoutLogin.findViewById(R.id.password_edittext);


        dialog.setView(layoutLogin);

        dialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                dialogInterface.dismiss();
                btnSignIn.setEnabled(false);

                if (TextUtils.isEmpty(emailEditText.getText().toString())) {
                    Snackbar.make(rootLayout, "please enter email address", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(passwordEditText.getText().toString())) {
                    Snackbar.make(rootLayout, "please enter your password", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (passwordEditText.getText().toString().length() <= 6) {
                    Snackbar.make(rootLayout, "password is to short !!!", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }


                final android.app.AlertDialog waitingDialog = new SpotsDialog(MainActivity.this);
                waitingDialog.show();


                auth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                waitingDialog.dismiss();
                                startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        waitingDialog.dismiss();
                        Snackbar.make(rootLayout, "Failure " + e.getMessage(), Snackbar.LENGTH_SHORT)
                                .show();
                        btnSignIn.setEnabled(true);
                    }
                });


            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }

    private void showRegisterDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("REGISTER");
        dialog.setMessage("please use email to register");

        LayoutInflater inflater = LayoutInflater.from(this);
        View layoutRegister = inflater.inflate(R.layout.layout_register, null);

        final MaterialEditText emailEditText = layoutRegister.findViewById(R.id.email_edittext);
        final MaterialEditText passwordEditText = layoutRegister.findViewById(R.id.password_edittext);
        final MaterialEditText nameEditText = layoutRegister.findViewById(R.id.name_edittext);
        final MaterialEditText phoneEditText = layoutRegister.findViewById(R.id.phone_edittext);

        dialog.setView(layoutRegister);

        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                if (TextUtils.isEmpty(emailEditText.getText().toString())) {
                    Snackbar.make(rootLayout, "please enter email address", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }


                if (TextUtils.isEmpty(phoneEditText.getText().toString())) {
                    Snackbar.make(rootLayout, "please enter your phone", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }


                if (TextUtils.isEmpty(passwordEditText.getText().toString())) {
                    Snackbar.make(rootLayout, "please enter your password", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (passwordEditText.getText().toString().length() <= 6) {
                    Snackbar.make(rootLayout, "password is to short !!!", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }


                auth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Usr usr = new Usr();
                                usr.setEmail(emailEditText.getText().toString());
                                usr.setName(nameEditText.getText().toString());
                                usr.setPassword(passwordEditText.getText().toString());
                                usr.setPhone(phoneEditText.getText().toString());

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(usr)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(rootLayout, "register successfully !!!", Snackbar.LENGTH_SHORT)
                                                        .show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(rootLayout, "register failure " + e.getMessage(), Snackbar.LENGTH_SHORT)
                                                        .show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(rootLayout, "register failure " + e.getMessage(), Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        });
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }
}

package com.example.alton.myapplication.LoginModule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alton.myapplication.Admin.AdminActivity;
import com.example.alton.myapplication.HomePageActivity;
import com.example.alton.myapplication.Models.User;
import com.example.alton.myapplication.R;
import com.facebook.FacebookSdk;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup,txtForgot;
    private ProgressDialog progressDialog;
    private LoginButton fbLoginButton;
     DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    CallbackManager mCallbackManager;
    String first_name,last_name,gender,profilePicUrl,fb_id;
ProgressDialog progressDialog1;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_login);
        progressDialog=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();

progressDialog1=new ProgressDialog(this);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("demo", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("demo", "onAuthStateChanged:signed_out");
                }
            }
        };

        if(firebaseAuth.getCurrentUser()!=null) {
            startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
        }





        editTextEmail=(EditText)findViewById(R.id.editTextEmail2);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword2);



        buttonSignin=(Button)findViewById(R.id.buttonSignin);

        fbLoginButton=(LoginButton)findViewById(R.id.face_button);

        textViewSignup=(TextView)findViewById(R.id.textViewSignup);
        txtForgot=(TextView)findViewById(R.id.textForgot);



        mCallbackManager = CallbackManager.Factory.create();
        fbLoginButton.setReadPermissions("email","public_profile");
        fbLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("demo", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("demo", "facebook:onCancel");
                Toast.makeText(getApplicationContext(),"Cancelled",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("demo", "facebook:onError", error);
                Toast.makeText(getApplicationContext(),"facebook login:error occrued",Toast.LENGTH_SHORT).show();
            }
        });

        buttonSignin.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
        txtForgot.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }}
    private void userLogin(){

        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            editTextEmail.setError("Enter email");
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setTitle("Logging in");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(),HomePageActivity.class));
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Please enter valid credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("demo", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("demo", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("demo", "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(LoginActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            GraphRequest request = GraphRequest.newMeRequest(
                                    AccessToken.getCurrentAccessToken() ,
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(
                                                JSONObject object,
                                                GraphResponse response) {
                                            try {
                                                Log.d("demo","fbjsonObj"+object.toString());
                                                fb_id=object.getString("id");
                                                first_name= (String) object.getString("first_name");
                                                last_name=object.getString("last_name");
                                                gender=object.getString("gender");
                                                profilePicUrl=object.getJSONObject("picture").getJSONObject("data").getString("url");
                                                Log.d("demo","fb user details"+first_name+last_name+gender+profilePicUrl);
                                                //create new User and save in firebase DB
                                                User newUser=new User();
                                                newUser.setUid(user.getUid());
                                                newUser.setFirstName(first_name);
                                                newUser.setLastName(last_name);
                                                newUser.setGender(gender);
                                                newUser.setUserPicUrl(profilePicUrl);
                                                mDatabase.child("users").child(user.getUid()).child("profile").setValue(newUser);
                                                first_name="";
                                                last_name="";
                                                gender="";
                                                profilePicUrl="";
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields","id,name,email,about,cover,birthday,first_name,gender,last_name,picture");
                            request.setParameters(parameters);
                            request.executeAsync();

                            Intent intent=new Intent(LoginActivity.this,HomePageActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            mCallbackManager.onActivityResult(requestCode, resultCode, data);

        }

    @Override
    public void onClick(View v) {
        if(v==findViewById(R.id.buttonSignin)){
            String email1=editTextEmail.getText().toString().trim();
            String password2=editTextPassword.getText().toString().trim();


            if(email1.contentEquals("john@gmail.com")&&password2.contentEquals("qwerty")){
        progressDialog1.setTitle("Welcome Admin");
        progressDialog1.setMessage("Loading...");
        progressDialog1.show();
                firebaseAuth.signInWithEmailAndPassword(email1,password2).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
progressDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(),AdminActivity.class));
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Please enter valid admin credentials", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
            else {
                userLogin();
            }

        }
        if(v==findViewById(R.id.textViewSignup)){
            Intent i=new Intent(getApplicationContext(),SignUpActivity.class);
            startActivity(i);
        }
        if(v==findViewById(R.id.textForgot)){
            startActivity(new Intent(getApplicationContext(),ResetActivity.class));
        }

    }
}


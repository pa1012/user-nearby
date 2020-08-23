package com.example.usernearby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

  // Constants
  public static final String USER_LIST = "user_list";

  //  Add member variables here:
  // UI references.
  private AutoCompleteTextView mEmailView;
  private AutoCompleteTextView mUsernameView;
  private EditText mPasswordView;
  private EditText mConfirmPasswordView;
  private int[] images= {R.drawable.user01,R.drawable.user02,R.drawable.user03,R.drawable.user04};
  private int iImage = 0;
  private SharedPreferences mPreferences;
  private SharedPreferences.Editor mEditor;
  UserList users;
  ArrayList<User> userArrayList;
  // Firebase instance variables


  private FirebaseAuth mAuth;
  private FirebaseDatabase mData;
  private DatabaseReference mRef;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    initComponents();

    // Keyboard sign in action
    mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == R.integer.register_form_finished || id == EditorInfo.IME_NULL) {
          attemptRegistration();
          return true;
        }
        return false;
      }
    });

    // Get hold of an instance of FirebaseAuth
    mAuth = FirebaseAuth.getInstance();
    mData = FirebaseDatabase.getInstance();
    mRef = mData.getReference("users");

  }

  private void initComponents() {
    mEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);
    mPasswordView = (EditText) findViewById(R.id.register_password);
    mConfirmPasswordView = (EditText) findViewById(R.id.register_confirm_password);
    mUsernameView = (AutoCompleteTextView) findViewById(R.id.register_username);

    /*mPreferences = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);
    mEditor = mPreferences.edit();

    Gson gson = new Gson();
    String json = mPreferences.getString(USER_LIST, "");
    users = gson.fromJson(json, UserList.class);
    userArrayList =  users.users;*/

  }

  // Executed when Sign Up button is pressed.
  public void signUp(View v) {
    attemptRegistration();
  }

  private void attemptRegistration() {

    // Reset errors displayed in the form.
    mEmailView.setError(null);
    mPasswordView.setError(null);

    // Store values at the time of the login attempt.
    String email = mEmailView.getText().toString();
    String password = mPasswordView.getText().toString();

    boolean cancel = false;
    View focusView = null;

    // Check for a valid password, if the user entered one.
    if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
      mPasswordView.setError(getString(R.string.error_invalid_password));
      focusView = mPasswordView;
      cancel = true;
    }

    // Check for a valid email address.
    if (TextUtils.isEmpty(email)) {
      mEmailView.setError(getString(R.string.error_field_required));
      focusView = mEmailView;
      cancel = true;
    } else if (!isEmailValid(email)) {
      mEmailView.setError(getString(R.string.error_invalid_email));
      focusView = mEmailView;
      cancel = true;
    }

    if (cancel) {
      // There was an error; don't attempt login and focus the first
      // form field with an error.
      focusView.requestFocus();
    } else {
      // Call create FirebaseUser() here
      createFirebaseUser();
    }
  }

  private boolean isEmailValid(String email) {
    // You can add more checking logic here.
    return email.contains("@");
  }

  private boolean isPasswordValid(String password) {
    String confirmPassword = mConfirmPasswordView.getText().toString();

    return confirmPassword.equals(password) && password.length() > 4;
  }

  private void createFirebaseUser(){
    String email = mEmailView.getText().toString();
    String password = mPasswordView.getText().toString();
    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
      @Override
      public void onComplete(@NonNull Task<AuthResult> task) {
        Log.d("User", "createUser onComplete:" + task.isSuccessful());

        if (!task.isSuccessful()){
          Log.d("User","user creation failed");
          showErrorDialog("Registration attempt failed");
        }
        else{
          String key = mAuth.getUid();
          User user =  saveDisplayName(key);
          Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
          intent.putExtra("user", user);
          finish();
          startActivity(intent);
        }
      }
    });
  }

  private User saveDisplayName(String key){
    LatLng latLng = new LatLng(0,0);
    User user = new User(mUsernameView.getText().toString(),mPasswordView.getText().toString(),mEmailView.getText().toString(),images[iImage % 4],0 ,0,true);

    iImage = (iImage + 1) % 4;
    mRef.child(key).setValue(user);

    return user;
  }
  private void showErrorDialog(String message){
    new AlertDialog.Builder(this)
            .setTitle("Oops")
            .setMessage(message)
            .setPositiveButton(android.R.string.ok,null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
  }
}
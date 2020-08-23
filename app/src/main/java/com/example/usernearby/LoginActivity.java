package com.example.usernearby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

  // UI references.
  private AutoCompleteTextView mEmailView;
  private EditText mPasswordView;
  private FirebaseAuth mAuth;
  private DatabaseReference mRef;

  User user;
  UserList users;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    initComponents();

    mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == R.integer.login || id == EditorInfo.IME_NULL) {
          attemptLogin();
          return true;
        }
        return false;
      }
    });



  }

  private void initComponents() {
    mEmailView = (AutoCompleteTextView) findViewById(R.id.login_email);
    mPasswordView = (EditText) findViewById(R.id.login_password);

    mAuth = FirebaseAuth.getInstance();




    Intent intentIn = getIntent();
    if (intentIn != null)
       user = (User) intentIn.getSerializableExtra("user");
  }

  // Executed when Sign in button pressed
  public void signInExistingUser(View v)   {
    attemptLogin();

  }

  // Executed when Register button pressed
  public void registerNewUser(View v) {
    Intent intent = new Intent(this, RegisterActivity.class);
    //finish();
    startActivity(intent);
  }

  private void attemptLogin() {
    final String email = mEmailView.getText().toString();
    String password = mPasswordView.getText().toString();

    if (email.equals("") || password.equals("")) return;
    Toast.makeText(this,"Login progress...", Toast.LENGTH_SHORT).show();

    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
      @Override
      public void onComplete(@NonNull Task<AuthResult> task) {
        Log.d("User","signInWithEmail() onComplete: "+ task.isSuccessful());

        if (!task.isSuccessful()){
          Log.d("User", "Problem sign in: "+ task.getException());
          showErrorDialog("There was a problem signing in");
        }
        else {

          Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
          intent.putExtra("user_email" ,  email);
          startActivity(intent);
        }
      }
    });
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
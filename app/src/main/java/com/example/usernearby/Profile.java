package com.example.usernearby;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {
  TextView UserName;
  TextView Email;
  ImageView imgImage;

  private User user;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    initComponents();
  }

  private void initComponents() {
    UserName = (TextView) findViewById(R.id.user_name);
    Email =(TextView) findViewById(R.id.email);
    imgImage = (ImageView) findViewById(R.id.avatar);

    Intent intent = getIntent();
    if (intent!= null)
      user =  (User) intent.getSerializableExtra("user_profile");

    UserName.setText(user.getUsername());
    Email.setText("Email: " + user.getEmail());
    imgImage.setImageResource(user.getImage());
  }
}

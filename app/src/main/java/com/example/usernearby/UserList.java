package com.example.usernearby;

import java.io.Serializable;
import java.util.ArrayList;

public class UserList implements Serializable {
  ArrayList<User> users;

  public UserList(ArrayList<User> users) {
    this.users = users;
  }



}

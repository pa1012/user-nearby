package com.example.usernearby;

import java.io.Serializable;

public class User implements Serializable {

  String username;
  String password;
  String email;
  int image;
  double lat,lng;

  boolean login;

  public boolean isLogin() {
    return login;
  }

  public void setLogin(boolean login) {
    this.login = login;
  }


  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLng() {
    return lng;
  }

  public void setLng(double lng) {
    this.lng = lng;
  }


  public User(String username, String password, String email, int image, double lat, double lng,boolean login) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.image = image;
    this.lat = lat;
    this.lng = lng;
    this.login = login;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int getImage() {
    return image;
  }

  public void setImage(int image) {
    this.image = image;
  }


}

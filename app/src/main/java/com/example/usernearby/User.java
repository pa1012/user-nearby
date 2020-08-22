package com.example.usernearby;

import java.io.Serializable;

public class User implements Serializable {

  String username;
  String password;
  String email;
  int image;
  float lat,lng;
  public float getLat() {
    return lat;
  }

  public void setLat(float lat) {
    this.lat = lat;
  }

  public float getLng() {
    return lng;
  }

  public void setLng(float lng) {
    this.lng = lng;
  }


  public User(String username, String password, String email, int image, float lat, float lng) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.image = image;
    this.lat = lat;
    this.lng = lng;

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

package com.example.demo.chat.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


@Getter
@Setter
public class ConfluenceModel {
  private String title;
  private ArrayList<ArrayList<String>> content;
  private String pageId;
  private String spaceKey;
}

package com.example.demo.chat.service;

import java.util.ArrayList;

public interface TableFormer {
  String formTableContent (ArrayList<ArrayList<String>> content);

  String AppendContent (String prev, String curr);
}

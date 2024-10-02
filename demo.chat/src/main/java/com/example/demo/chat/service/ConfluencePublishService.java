package com.example.demo.chat.service;

import com.example.demo.chat.model.ConfluenceModel;

import java.util.ArrayList;

public interface ConfluencePublishService {
  String publishCustomWorkflow(ConfluenceModel confluenceModel) throws Exception;

  String appendExistingPage(ConfluenceModel confluenceModel) throws Exception;
}

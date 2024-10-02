package com.example.demo.chat.service;

import com.example.demo.chat.model.ConfluenceModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static com.example.demo.chat.model.Constants.*;

public class ConfluencePublishServiceImpl implements ConfluencePublishService {


  @Autowired
  private TableFormer tableFormer;

  public ConfluencePublishServiceImpl(TableFormer tableFormer) { //will pass on the concrete implementation during run time
    this.tableFormer = tableFormer;
  }

  @Override
  public String publishCustomWorkflow(ConfluenceModel confluenceModel) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    CloseableHttpClient httpClient = HttpClients.createDefault();
    String response = createPage(httpClient, mapper, tableFormer.formTableContent(confluenceModel.getContent()), confluenceModel);
    httpClient.close();
    return response;
  }

  @Override
  public String appendExistingPage(ConfluenceModel confluenceModel) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    CloseableHttpClient httpClient = HttpClients.createDefault();
    String response = appendToPage(httpClient, mapper, confluenceModel.getPageId(),
            tableFormer.AppendContent
                    (getExistingPageContent(httpClient, confluenceModel.getPageId(), mapper),tableFormer.formTableContent(confluenceModel.getContent())));
    httpClient.close();
    return response ;
  }




  private static String appendToPage(CloseableHttpClient httpClient, ObjectMapper mapper, String pageId, String htmlRows) throws Exception {
    // Retrieve the existing page content



    // Prepare the update request
    HttpPut httpPut = new HttpPut(BASE_URL + "/" + pageId);
    httpPut.setHeader("Authorization", "Basic " + getAuthHeader());
    httpPut.setHeader("Content-Type", "application/json");
    httpPut.setHeader("X-Atlassian-Token", "no-check");





    // Create JSON object for the updated page content
    ObjectNode pageContent = mapper.createObjectNode();
    ObjectNode space = mapper.createObjectNode();
    space.put("key", SPACE_KEY);
    pageContent.set("space", space);

    ObjectNode versionNode = mapper.createObjectNode();
    versionNode.put("number", getNextVersion(httpClient, pageId, mapper));

    pageContent.put("version", versionNode); // Get next version number
    ObjectNode body = mapper.createObjectNode();
    ObjectNode storage = mapper.createObjectNode();
    storage.put("value", htmlRows);
    storage.put("representation", "storage");
    body.set("storage", storage);
    pageContent.set("body", body);

    // Send the PUT request
    StringEntity stringEntity = new StringEntity(pageContent.toString());
    httpPut.setEntity(stringEntity);



    try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
      HttpEntity entity = response.getEntity();
      return EntityUtils.toString(entity);
    }
  }

  // Helper method to retrieve existing page content
  private static String getExistingPageContent(CloseableHttpClient httpClient, String pageId, ObjectMapper mapper) throws Exception {
    HttpGet httpGet = new HttpGet(BASE_URL + "/" + pageId + "?expand=body.storage");
    httpGet.setHeader("Authorization", "Basic " + getAuthHeader());

    try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
      HttpEntity entity = response.getEntity();
      ObjectNode jsonResponse = mapper.readValue(EntityUtils.toString(entity), ObjectNode.class);
      return jsonResponse.get("body").get("storage").get("value").asText();
    }
  }

  // Helper method to get the next version number
  private static int getNextVersion(CloseableHttpClient httpClient, String pageId, ObjectMapper mapper) throws Exception {
    HttpGet httpGet = new HttpGet(BASE_URL + "/" + pageId);
    httpGet.setHeader("Authorization", "Basic " + getAuthHeader());

    try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
      HttpEntity entity = response.getEntity();
      ObjectNode jsonResponse = mapper.readValue(EntityUtils.toString(entity), ObjectNode.class);
      return jsonResponse.get("version").get("number").asInt() + 1; // Increment the version number
    }
  }


  // Create a Confluence page with the table content
  private String createPage(CloseableHttpClient httpClient, ObjectMapper mapper, String htmlRows, ConfluenceModel confluenceModel) throws Exception {
    HttpPost httpPost = new HttpPost(BASE_URL);
    httpPost.setHeader("Authorization", "Basic " + getAuthHeader());
    httpPost.setHeader("Content-Type", "application/json");
    httpPost.setHeader("X-Atlassian-Token", "no-check");

    // Create JSON object for the page content
    ObjectNode pageContent = mapper.createObjectNode();
    pageContent.put("type", "page");
    pageContent.put("title", confluenceModel.getTitle());
    ObjectNode space = mapper.createObjectNode();
    space.put("key", confluenceModel.getSpaceKey());
    pageContent.set("space", space);


    ObjectNode body = mapper.createObjectNode();
    ObjectNode storage = mapper.createObjectNode();
    storage.put("value", htmlRows);
    storage.put("representation", "storage");
    body.set("storage", storage);
    pageContent.set("body", body);

    // Send the POST request
    StringEntity stringEntity = new StringEntity(pageContent.toString());
    httpPost.setEntity(stringEntity);

    System.out.println("Request: " + pageContent.toString());


    try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
      HttpEntity entity = response.getEntity();
      return EntityUtils.toString(entity);
    }
  }

  // Generate HTML rows from JSON rows
  private static String getAuthHeader() {
    String auth = USERNAME + ":" + API_TOKEN;
    return java.util.Base64.getEncoder().encodeToString(auth.getBytes());
  }

}


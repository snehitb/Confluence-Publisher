package com.example.demo.chat.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class TableFormerImple implements  TableFormer {

  private static final String HTML_TEMPLATE =
          "<html>" +
                  "<head><style>table { width: 100%; border-collapse: collapse; } th, td { border: 1px solid black; padding: 8px; text-align: left; }</style></head>" +
                  "<body>" +
                  "<h1>My Custom Table</h1>" +
                  "<table>" +
                  "{ROWS}" +
                  "</table>" +
                  "</body>" +
                  "</html>";


  private static ArrayNode createTableRows(ObjectMapper mapper, ArrayList<ArrayList<String>> values) {
    ArrayNode rows = mapper.createArrayNode();
    for (ArrayList<String> valueList : values) {
      ObjectNode row = mapper.createObjectNode();
      ArrayNode columns = mapper.createArrayNode();
      for (String value : valueList) {
        columns.add(value);
      }
      row.set("cells", columns);
      rows.add(row);
    }
    return rows;
  }

  private static String tableToHtml(ArrayNode rows) {
    StringBuilder html = new StringBuilder();
    int index = 0;
    for (JsonNode row : rows) {
      html.append("<tr>");
      JsonNode cells = row.get("cells");
      for (JsonNode cell : cells) {
        if (index == 0)
          html.append("<th>").append(cell.asText()).append("</th>");
        else
          html.append("<td>").append(cell.asText()).append("</td>");
      }
      html.append("</tr>");
      index++;
    }
    return html.toString();
  }


  @Override
  public String formTableContent(ArrayList<ArrayList<String>> content) {
    ObjectMapper mapper = new ObjectMapper();
    ArrayNode rows = createTableRows(mapper, content);
    return HTML_TEMPLATE.replace("{ROWS}", tableToHtml(rows));
  }

  @Override
  public  String AppendContent(String existingTable, String newHeaders) {
    int insertPosition = existingTable.indexOf("</tbody>");


    if (insertPosition != -1) {
      return existingTable.substring(0, insertPosition) + newHeaders + existingTable.substring(insertPosition);
    } else {
      return existingTable; // If no </tbody> tag is found, return the original table
    }
  }
}

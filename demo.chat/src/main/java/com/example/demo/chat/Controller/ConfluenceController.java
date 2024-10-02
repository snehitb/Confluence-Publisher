package com.example.demo.chat.Controller;


import com.example.demo.chat.model.ConfluenceModel;
import com.example.demo.chat.service.ConfluencePublishService;
import com.example.demo.chat.service.ConfluencePublishServiceImpl;
import com.example.demo.chat.service.TableFormer;
import com.example.demo.chat.service.TableFormerImple;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/confluence")
public class ConfluenceController {


  private ConfluencePublishService confluencePublishService;

  private TableFormer tableFormer;
  public ConfluenceController() {
    tableFormer = new TableFormerImple();
    confluencePublishService = new ConfluencePublishServiceImpl(tableFormer);
  }

  @PostMapping("/publishCustom")
  public String publishCustomWorkflow(@RequestBody ConfluenceModel confluenceModel) throws Exception {
     return this.confluencePublishService.publishCustomWorkflow(confluenceModel);
  }

  @PostMapping("/editExisting")
  public String appendExistingWorkflow(@RequestBody ConfluenceModel confluenceModel) throws Exception {
     return this.confluencePublishService.appendExistingPage(confluenceModel);
  }


}

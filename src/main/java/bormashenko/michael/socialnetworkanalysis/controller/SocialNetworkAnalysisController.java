package bormashenko.michael.socialnetworkanalysis.controller;

import bormashenko.michael.socialnetworkanalysis.repo.SocialNetworkUser;
import bormashenko.michael.socialnetworkanalysis.service.SocialNetworkAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class SocialNetworkAnalysisController {

   @Autowired
   private SocialNetworkAnalysisService socialNetworkAnalysisService;

   @GetMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
   public List<SocialNetworkUser> getUsers() {
      return socialNetworkAnalysisService.getUsers();
   }

}

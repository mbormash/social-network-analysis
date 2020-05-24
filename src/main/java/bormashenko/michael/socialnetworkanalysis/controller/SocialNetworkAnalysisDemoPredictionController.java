package bormashenko.michael.socialnetworkanalysis.controller;

import bormashenko.michael.socialnetworkanalysis.repo.SocialNetworkUser;
import bormashenko.michael.socialnetworkanalysis.service.SocialNetworkAnalysisService;
import bormashenko.michael.socialnetworkanalysis.service.Util;
import bormashenko.michael.socialnetworkanalysis.service.prediction.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequestMapping("/social-analysis/prediction/demo")
@Slf4j
public class SocialNetworkAnalysisDemoPredictionController {

   @Autowired
   private SocialNetworkAnalysisService socialNetworkAnalysisService;

   private Integer[][] demoMatrix;

   @PostConstruct
   private void getDemoUsers() {
      List<SocialNetworkUser> socialNetworkUsers = socialNetworkAnalysisService.getUsers();
      demoMatrix = Util.Conversion.convertUserListToMatrix(socialNetworkUsers);
   }

   @GetMapping(value = "/common-friends")
   public Integer[][] predictByCommonFriends() {
      return socialNetworkAnalysisService.predictRelations(demoMatrix, new CommonFriendsPredictionService());
   }

   @GetMapping(value = "/zhakkar")
   public Integer[][] predictByZhakkar() {
      return socialNetworkAnalysisService.predictRelations(demoMatrix, new ZhakkarPredictionService());
   }

   @GetMapping(value = "/adamic-adar")
   public Integer[][] predictByAdamicAdar() {
      return socialNetworkAnalysisService.predictRelations(demoMatrix, new AdamicAdarPredictionService());
   }

   @GetMapping(value = "/min-path-dijkstra")
   public Integer[][] predictByMinPathDijkstra() {
      return socialNetworkAnalysisService.predictRelations(demoMatrix, new MinPathByDijkstraPredictionService());
   }

   @GetMapping(value = "/hit-probability")
   public Integer[][] predictByHitProbability() {
      return socialNetworkAnalysisService.predictRelations(demoMatrix, new HitProbabilityPredictionService());
   }
}

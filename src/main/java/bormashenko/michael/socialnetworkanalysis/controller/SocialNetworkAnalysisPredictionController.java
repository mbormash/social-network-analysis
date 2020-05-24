package bormashenko.michael.socialnetworkanalysis.controller;

import bormashenko.michael.socialnetworkanalysis.service.SocialNetworkAnalysisService;
import bormashenko.michael.socialnetworkanalysis.service.prediction.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/social-analysis/prediction")
@Slf4j
public class SocialNetworkAnalysisPredictionController {

   @Autowired
   private SocialNetworkAnalysisService socialNetworkAnalysisService;

   @PostMapping(value = "/common-friends", consumes = MediaType.APPLICATION_JSON_VALUE)
   public Integer[][] predictByCommonFriends(@RequestBody Integer[][] matrix) {
      return socialNetworkAnalysisService.predictRelations(matrix, new CommonFriendsPredictionService());
   }

   @PostMapping(value = "/zhakkar", consumes = MediaType.APPLICATION_JSON_VALUE)
   public Integer[][] predictByZhakkar(@RequestBody Integer[][] matrix) {
      return socialNetworkAnalysisService.predictRelations(matrix, new ZhakkarPredictionService());
   }

   @PostMapping(value = "/adamic-adar", consumes = MediaType.APPLICATION_JSON_VALUE)
   public Integer[][] predictByAdamicAdar(@RequestBody Integer[][] matrix) {
      return socialNetworkAnalysisService.predictRelations(matrix, new AdamicAdarPredictionService());
   }

   @PostMapping(value = "/min-path-dijkstra", consumes = MediaType.APPLICATION_JSON_VALUE)
   public Integer[][] predictByMinPathDijkstra(@RequestBody Integer[][] matrix) {
      return socialNetworkAnalysisService.predictRelations(matrix, new MinPathByDijkstraPredictionService());
   }

   @PostMapping(value = "/hit-probability", consumes = MediaType.APPLICATION_JSON_VALUE)
   public Integer[][] predictByHitProbability(@RequestBody Integer[][] matrix) {
      return socialNetworkAnalysisService.predictRelations(matrix, new HitProbabilityPredictionService());
   }
}

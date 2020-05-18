package bormashenko.michael.socialnetworkanalysis.controller;

import bormashenko.michael.socialnetworkanalysis.repo.SocialNetworkUser;
import bormashenko.michael.socialnetworkanalysis.service.SocialNetworkAnalysisService;
import bormashenko.michael.socialnetworkanalysis.service.prediction.CommonFriendsPredictionService;
import bormashenko.michael.socialnetworkanalysis.service.prediction.ZhakkarPredictionService;
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
   public Integer[][] predictByCommonFriends(@RequestParam Integer[][] matrix) {
      return socialNetworkAnalysisService.predictRelations(matrix, new CommonFriendsPredictionService());
   }

   @PostMapping(value = "/zhakkar", consumes = MediaType.APPLICATION_JSON_VALUE)
   public Integer[][] predictByZhakkar(@RequestParam Integer[][] matrix) {
      return socialNetworkAnalysisService.predictRelations(matrix, new ZhakkarPredictionService());
   }
}

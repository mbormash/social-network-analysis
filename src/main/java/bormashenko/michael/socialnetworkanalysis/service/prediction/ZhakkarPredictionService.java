package bormashenko.michael.socialnetworkanalysis.service.prediction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static bormashenko.michael.socialnetworkanalysis.service.Util.CalculationUtil.*;

@Slf4j
@Service
public class ZhakkarPredictionService extends CommonFriendsPredictionService {

   @Override
   public boolean shouldConnect(Integer[] xRelations, Integer[] yRelations) {
      double commonFriends = calculateCommonFriends(xRelations, yRelations);
      double totalFriends = calculateTotalFriends(xRelations, yRelations);

      return shouldConnect(commonFriends, totalFriends);
   }

   private double calculateTotalFriends(Integer[] xUserRelations, Integer[] yUserRelations) {
      int totalFriends = 0;
      for (int i = 0; i < xUserRelations.length; i++) {
         if (xUserRelations[i] != null && xUserRelations[i].equals(POSITIVE_RELATION)) {
            totalFriends++;
         }

         if (yUserRelations[i] != null && yUserRelations[i].equals(POSITIVE_RELATION)) {
            totalFriends++;
         }
      }

      return totalFriends - calculateCommonFriends(xUserRelations, yUserRelations);
   }

   private boolean shouldConnect(double commonFriends, double totalFriends) {
      return getRandomBooleanWithProbability(commonFriends / totalFriends);
   }
}

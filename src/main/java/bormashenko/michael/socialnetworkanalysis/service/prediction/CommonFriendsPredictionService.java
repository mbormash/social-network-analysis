package bormashenko.michael.socialnetworkanalysis.service.prediction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static bormashenko.michael.socialnetworkanalysis.service.Util.CalculationUtil.*;


@Slf4j
@Service
public class CommonFriendsPredictionService implements CommonFriendsPrediction {

   private static final long COMMON_FRIENDS_BOUND_MAX_PROBABILITY = 6L;

   private static final double EACH_COMMON_FRIEND_PROBABILITY = 1.0 / COMMON_FRIENDS_BOUND_MAX_PROBABILITY;

   private static final long SMALL_NETWORK_COMMON_FRIENDS_BOUND_MAX_PROBABILITY = 3L;

   private static final double SMALL_NETWORK_EACH_COMMON_FRIEND_PROBABILITY = 1.0 / SMALL_NETWORK_COMMON_FRIENDS_BOUND_MAX_PROBABILITY;

   protected static final int SMALL_NETWORK = 7;

   @Override
   public Integer[][] predict(Integer[][] relationMatrix) {
      Prediction.verifyMatrix(relationMatrix);

      Integer[][] predictedRelations = new Integer[relationMatrix.length][relationMatrix.length];

      for (int i = 0; i < relationMatrix.length; i++) {
         Integer[] xRelations = relationMatrix[i];
         Integer[] predictedXRelations = new Integer[xRelations.length];

         for (int j = 0; j < xRelations.length; j++) {
            if (i == j) {
               predictedXRelations[j] = null;
               continue;
            }

            if (xRelations[j].equals(POSITIVE_RELATION)) {
               predictedXRelations[j] = POSITIVE_RELATION;
               continue;
            }

            Integer[] yRelations = relationMatrix[j];

            predictedXRelations[j] = shouldConnect(xRelations, yRelations) ? POSITIVE_RELATION : NEUTRAL_RELATION;
         }

         predictedRelations[i] = predictedXRelations;
      }

      normalizeMatrixByX(predictedRelations);
      return predictedRelations;
   }

   @Override
   public boolean shouldConnect(Integer[] xRelations, Integer[] yRelations) {
      boolean smallNetwork = xRelations.length <= SMALL_NETWORK;
      double commonFriends = calculateCommonFriends(xRelations, yRelations);

      return shouldConnect(commonFriends, smallNetwork);
   }

   protected double calculateCommonFriends(Integer[] xUserRelations, Integer[] yUserRelations) {
      int commonFriends = 0;
      for (int i = 0; i < xUserRelations.length; i++) {
         if (xUserRelations[i] != null && xUserRelations[i].equals(POSITIVE_RELATION) &&
               yUserRelations[i] != null && yUserRelations[i].equals(POSITIVE_RELATION)) {
            commonFriends++;
         }
      }

      return commonFriends;
   }

   protected boolean shouldConnect(double commonFriends, boolean smallNetwork) {
      if (smallNetwork) {
         return shouldConnectInSmallNetwork(commonFriends);
      }

      if (commonFriends >= COMMON_FRIENDS_BOUND_MAX_PROBABILITY) {
         return true;
      }

      if (commonFriends == 0) {
         return false;
      }

      double probability = commonFriends * EACH_COMMON_FRIEND_PROBABILITY;
      return getRandomBooleanWithProbability(probability);
   }

   private boolean shouldConnectInSmallNetwork(double commonFriends) {
      if (commonFriends >= SMALL_NETWORK_COMMON_FRIENDS_BOUND_MAX_PROBABILITY) {
         return true;
      }

      if (commonFriends == 0) {
         return false;
      }

      double probability = commonFriends * SMALL_NETWORK_EACH_COMMON_FRIEND_PROBABILITY;
      return getRandomBooleanWithProbability(probability);
   }

}

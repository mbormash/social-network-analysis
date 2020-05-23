package bormashenko.michael.socialnetworkanalysis.service.prediction;

import bormashenko.michael.socialnetworkanalysis.exception.SNAnalysisException;

import java.util.Arrays;
import java.util.Random;

import static bormashenko.michael.socialnetworkanalysis.service.Util.CalculationUtil.getRandomBooleanWithProbability;
import static bormashenko.michael.socialnetworkanalysis.service.Util.CalculationUtil.normalizeMatrixByX;

public class HitProbabilityPredictionService implements Prediction {

   private static final double ALPHA = 0.1;

   private Integer[][] relationMatrix;

   @Override
   public Integer[][] predict(Integer[][] relationMatrix) {
      Prediction.verifyMatrix(relationMatrix);
      this.relationMatrix = relationMatrix;

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

            boolean shouldConnect = predictByHitProbability(xRelations, j);
            predictedXRelations[j] = shouldConnect ? POSITIVE_RELATION : NEUTRAL_RELATION;
         }

         predictedRelations[i] = predictedXRelations;
      }

      normalizeMatrixByX(predictedRelations);
      return predictedRelations;
   }

   private boolean predictByHitProbability(Integer[] userRelations, int anotherUserIndex) {
      int steps = 1;
      Integer[] currentUser = userRelations;
      while (true) {
         if (getRandomBooleanWithProbability(ALPHA)) {
            return false;
         }

         int nextUserIndex = chooseRandomConnectedUserIndex(currentUser);
         if (nextUserIndex == anotherUserIndex) {
            break;
         }

         currentUser = relationMatrix[nextUserIndex];
         steps++;
      }

      double probability = steps * (1 - ALPHA);
      return getRandomBooleanWithProbability(probability);
   }

   private int chooseRandomConnectedUserIndex(Integer[] userRelations) {
      int numberOfFriends = Prediction.calculateFriends(userRelations);
      int randomUser = new Random().nextInt(numberOfFriends) + 1;

      int skippedUsers = 0;
      for (int i = 0; i < userRelations.length; i++) {
         if (userRelations[i] != null && userRelations[i].equals(POSITIVE_RELATION)) {
            skippedUsers++;

            if (skippedUsers == randomUser) {
               return i;
            }
         }
      }

      throw new SNAnalysisException("Server error: unable to choose random friend. Random user index (from 1): " + randomUser +
            " relations: " + Arrays.toString(userRelations));
   }
}

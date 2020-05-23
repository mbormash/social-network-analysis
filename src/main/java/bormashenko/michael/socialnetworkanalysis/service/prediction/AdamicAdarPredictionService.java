package bormashenko.michael.socialnetworkanalysis.service.prediction;

public class AdamicAdarPredictionService extends CommonFriendsPredictionService {

   private Integer[][] relationMatrix;

   @Override
   public Integer[][] predict(Integer[][] relationMatrix) {
      this.relationMatrix = relationMatrix;

      return super.predict(relationMatrix);
   }

   @Override
   protected double calculateCommonFriends(Integer[] xUserRelations, Integer[] yUserRelations) {
      double commonFriends = 0.0;
      for (int i = 0; i < xUserRelations.length; i++) {
         if (xUserRelations[i] != null && xUserRelations[i].equals(POSITIVE_RELATION) &&
               yUserRelations[i] != null && yUserRelations[i].equals(POSITIVE_RELATION)) {
            commonFriends += calculateCommonFriendWeight(i);
         }
      }

      return commonFriends;
   }

   private double calculateCommonFriendWeight(int friendIndex) {
      Integer[] friendRelations = relationMatrix[friendIndex];
      int numberOfFriends = Prediction.calculateFriends(friendRelations);

      return 1.0 / Math.log10(numberOfFriends);
   }

}

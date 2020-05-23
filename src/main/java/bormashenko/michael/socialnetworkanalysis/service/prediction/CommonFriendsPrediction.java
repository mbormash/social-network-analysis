package bormashenko.michael.socialnetworkanalysis.service.prediction;

public interface CommonFriendsPrediction extends Prediction {

   boolean shouldConnect(Integer[] xRelations, Integer[] yRelations);

}

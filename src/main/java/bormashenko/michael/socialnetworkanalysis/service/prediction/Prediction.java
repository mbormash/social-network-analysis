package bormashenko.michael.socialnetworkanalysis.service.prediction;

import bormashenko.michael.socialnetworkanalysis.exception.SNAnalysisException;
import bormashenko.michael.socialnetworkanalysis.service.Relation;

import static bormashenko.michael.socialnetworkanalysis.service.Util.CalculationUtil.isRelationMatrixValid;

public interface Prediction {

   Integer POSITIVE_RELATION = Relation.convertRelationToInteger(Relation.POSITIVE);
   Integer NEUTRAL_RELATION = Relation.convertRelationToInteger(Relation.NEUTRAL);

   Integer[][] predict(Integer[][] relationMatrix);

   static void verifyMatrix(Integer[][] relationMatrix) {
      if (!isRelationMatrixValid(relationMatrix)) {
         throw new SNAnalysisException("Not a valid relation matrix! Should be symmetric and diagonal elements should be null.");
      }
   }

   static int calculateFriends(Integer[] userRelations) {
      int numberOfFriends = 0;
      for (Integer userRelation : userRelations) {
         if (userRelation != null && userRelation.equals(POSITIVE_RELATION)) {
            numberOfFriends++;
         }
      }

      return numberOfFriends;
   }

}

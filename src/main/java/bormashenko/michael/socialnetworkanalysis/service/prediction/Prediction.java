package bormashenko.michael.socialnetworkanalysis.service.prediction;

import bormashenko.michael.socialnetworkanalysis.service.Relation;

public interface Prediction {

   Integer POSITIVE_RELATION = Relation.convertRelationToInteger(Relation.POSITIVE);
   Integer NEUTRAL_RELATION = Relation.convertRelationToInteger(Relation.NEUTRAL);

   Integer[][] predict(Integer[][] relationMatrix);

}

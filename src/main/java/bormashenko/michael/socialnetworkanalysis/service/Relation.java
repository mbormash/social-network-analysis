package bormashenko.michael.socialnetworkanalysis.service;

import bormashenko.michael.socialnetworkanalysis.exception.SNAnalysisException;

public enum Relation {

   POSITIVE,

   NEUTRAL,

   MYSELF;

   public static Relation convertIntegerToRelation(Integer integer) {
      if (integer == null) {
         return MYSELF;
      }

      switch (integer) {
         case 1:
            return POSITIVE;
         case 0:
            return NEUTRAL;
         default:
            throw new SNAnalysisException("Cannot convert " + integer + " to relation! Number should be 1, 0 or null");
      }
   }

   public static Integer convertRelationToInteger(Relation relation) {
      if (relation == MYSELF) {
         return null;
      }

      switch (relation) {
         case POSITIVE:
            return 1;
         case NEUTRAL:
            return 0;
         default:
            throw new SNAnalysisException("Cannot convert " + relation + " to integer!");
      }
   }
}

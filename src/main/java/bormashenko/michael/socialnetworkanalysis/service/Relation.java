package bormashenko.michael.socialnetworkanalysis.service;

public enum Relation {

   POSITIVE,

   NEUTRAL,

   NEGATIVE,

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
         case -1:
            return NEGATIVE;
         default:
            throw new IllegalArgumentException("Can not convert " + integer + " to relation! Number should be 1, 0, -1 or null");
      }
   }
}

package bormashenko.michael.socialnetworkanalysis.service;

import bormashenko.michael.socialnetworkanalysis.exception.SNAnalysisException;
import bormashenko.michael.socialnetworkanalysis.repo.SocialNetworkUser;
import bormashenko.michael.socialnetworkanalysis.repo.UserRelation;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Util {

   public static class CalculationUtil {

      public static void normalizeMatrixByX(Integer[][] matrix) {
         for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
               if (matrix[i][j] != null && !matrix[i][j].equals(matrix[j][i])) {
                  matrix[j][i] = matrix[i][j];
               }
            }
         }
      }

      public static boolean isRelationMatrixValid(Integer[][] matrix) {
         return isQuadrantMatrix(matrix) && isMatrixDiagonalNull(matrix) && isSymmetricMatrix(matrix);
      }

      private static boolean isQuadrantMatrix(Integer[][] matrix) {
         int height = matrix.length;

         for (Integer[] row : matrix) {
            if (row.length != height) {
               log.warn("Not a quadrant matrix!");
               return false;
            }
         }

         return true;
      }

      private static boolean isMatrixDiagonalNull(Integer[][] matrix) {
         for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][i] != null) {
               log.warn("Not null diagonal element! Element index: [{}][{}]", i, i);
               return false;
            }
         }
         return true;
      }

      private static boolean isSymmetricMatrix(Integer[][] matrix) {
         for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
               if (matrix[i][j] != null && !matrix[i][j].equals(matrix[j][i])) {
                  log.warn("Not a symmetric matrix! Element index: [{}][{}]", i, j);
                  return false;
               }
            }
         }

         return true;
      }
   }

   public static class Conversion {

      public static Integer[][] convertUserListToMatrix(List<SocialNetworkUser> users) {
         Integer[][] matrix = new Integer[users.size()][users.size()];

         for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
               matrix[i][j] = Relation.convertRelationToInteger(users.get(i).getRelations().get(j).getRelation());
            }
         }

         return matrix;
      }

      public static List<SocialNetworkUser> convertMatrixToUserList(Integer[][] matrix) {
         if (!CalculationUtil.isRelationMatrixValid(matrix)) {
            throw new SNAnalysisException("Non valid relation matrix! Matrix should be quadrant, symmetric and diagonal elements should be null.");
         }

         List<SocialNetworkUser> users = new ArrayList<>();
         String codeName = "A";
         for (Integer[] relations : matrix) {
            SocialNetworkUser user = new SocialNetworkUser();
            user.setCodeName(codeName);

            List<UserRelation> userRelations = new ArrayList<>();
            String anotherUserCodeName = "A";
            for (Integer integerRelation : relations) {
               UserRelation userRelation = new UserRelation();
               userRelation.setUser(user);
               userRelation.setUserCodeName(codeName);
               userRelation.setAnotherUserCodeName(anotherUserCodeName);
               userRelation.setRelation(Relation.convertIntegerToRelation(integerRelation));

               userRelations.add(userRelation);
               anotherUserCodeName = generateNextCodeName(anotherUserCodeName);
            }

            user.setRelations(userRelations);
            users.add(user);
            codeName = generateNextCodeName(codeName);
         }

         return users;
      }

      private static String generateNextCodeName(String codeName) {
         if (codeName.endsWith("Z")) {
            return incrementCodeNameLength(codeName);
         } else {
            return incrementLastChar(codeName);
         }
      }

      private static String incrementCodeNameLength(String codeName) {
         int length = codeName.length();

         StringBuilder builder = new StringBuilder(length + 1);
         for (int i = 0; i < length + 1; i++) {
            builder.append("A");
         }

         return builder.toString();
      }

      private static String incrementLastChar(String codeName) {
         char lastChar = codeName.charAt(codeName.length() - 1);
         lastChar++;

         return codeName.substring(0, codeName.length() - 1) + lastChar;
      }
   }

}

package bormashenko.michael.socialnetworkanalysis.service;

import bormashenko.michael.socialnetworkanalysis.repo.SocialNetworkUser;
import bormashenko.michael.socialnetworkanalysis.repo.UserRepository;
import bormashenko.michael.socialnetworkanalysis.repo.UsersRelation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Component
@Slf4j
public class SocialNetworkAnalysisServiceImpl implements SocialNetworkAnalysisService {

   private static final Integer[][] DEFAULT_USERS_RELATIONS = new Integer[][]{
         {null, 1, 1, 0, 0, -1, 1, 1, -1, 0, 0, 0, -1, 1, 1, -1, 0, 0, -1, 0},
         {1, null, 1, 1, 1, 1, 1, 0, 0, 1, 0, 1, 1, -1, 1, 0, 1, 1, 0, 0},
         {1, 1, null, -1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, -1, -1, 1, 1},
         {0, 1, -1, null, -1, -1, 1, 0, 0, 1, 0, 0, 1, 1, -1, 1, 1, 0, 0, -1},
         {0, 1, 1, -1, null, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, -1, -1, 1, 1},
         {-1, 1, 0, -1, 1, null, 0, 0, 1, 0, -1, -1, 1, 0, 0, 0, 0, 0, 0, 0},
         {1, 1, 0, 1, 1, 0, null, -1, -1, 1, -1, 1, 1, 1, 1, 0, 0, 1, 1, 1},
         {1, 0, 0, 0, 0, 0, -1, null, 1, 1, -1, -1, 1, 1, 1, 0, 1, 1, 1, 1},
         {-1, 0, 0, 0, 0, 1, -1, 1, null, -1, 1, 1, -1, 1, 1, 0, 0, -1, -1, -1},
         {0, 1, 1, 1, 0, 0, 1, 1, -1, null, 0, 1, 0, 0, -1, -1, 1, 1, 0, 0},
         {0, 0, 1, 0, 0, -1, -1, -1, 1, 0, null, 0, 0, 1, 0, 1, 1, 1, 1, 1},
         {0, 1, 0, 0, 0, -1, 1, -1, 1, 1, 0, null, -1, -1, 1, 1, 1, -1, -1, 1},
         {-1, 1, 0, 1, 0, 1, 1, 1, -1, 0, 0, -1, null, 1, 1, -1, 1, 0, 0, 0},
         {1, -1, 0, 1, 0, 0, 1, 1, 1, 0, 1, -1, 1, null, 1, -1, 1, 0, 0, 0},
         {1, 1, 1, -1, 1, 0, 1, 1, 1, -1, 0, 1, 1, 1, null, -1, 1, 1, 1, 0},
         {-1, 0, 1, 1, 1, 0, 0, 0, 0, -1, 1, 1, -1, -1, -1, null, 1, 1, 1, 1}, //todo -1 null
         {0, 1, -1, 1, -1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, null, -1, 1, -1}, //todo 1 null
         {0, 1, -1, 0, -1, 0, 1, 1, -1, 1, 1, -1, 0, 0, 1, 1, -1, null, 0, 1}, //todo 1, -1, null
         {-1, 0, 1, 0, 1, 0, 1, 1, -1, 0, 1, -1, 0, 0, 1, 1, 1, 0, null, 1}, //todo 1, 1, 1, 0, null
         {0, 0, 1, -1, 1, 0, 1, 1, -1, 0, 1, 1, 0, 0, 0, 1, -1, 1, 1, null}
   };

   @Autowired
   private UserRepository userRepository;

   @PostConstruct
   public void initDb() {
      log.info("Deleting all users in the DB");
      userRepository.deleteAll();

      log.info("Initializing default users");
      userRepository.saveAll(createDefaultUsers());

      log.info("Initializing default users - DONE!");
   }

   @Override
   public void addUser(SocialNetworkUser user) {
      if (!userRepository.findByCodeName(user.getCodeName()).isPresent()) {
         log.debug("Adding a new user in the DB with codeName: {}.", user.getCodeName());

         userRepository.save(user);
      } else {
         throw new IllegalArgumentException("User with codeName" + user.getCodeName() + " already exists!");
      }
   }

   @Override
   public List<SocialNetworkUser> getUsers() {
      log.debug("Detected getUsers request");

      return userRepository.findAll();
   }

   @Override
   public List<SocialNetworkUser> createUsersByRelations(Integer[][] relationMatrix) {
      log.debug("Detected createUsersByRelations request");

      if (!isRelationsMatrixValid(relationMatrix)) {
         throw new IllegalArgumentException("Non valid relation matrix! Matrix should be quadrant, symmetric and diagonal elements should be null.");
      }

      List<SocialNetworkUser> users = new ArrayList<>();
      String codeName = "A";
      for (Integer[] relations : relationMatrix) {
         SocialNetworkUser user = new SocialNetworkUser();
         user.setCodeName(codeName);

         List<UsersRelation> usersRelations = new ArrayList<>();
         String anotherUserCodeName = "A";
         for (Integer integerRelation : relations) {
            UsersRelation usersRelation = new UsersRelation();
            usersRelation.setUser(user);
            usersRelation.setUserCodeName(codeName);
            usersRelation.setAnotherUserCodeName(anotherUserCodeName);
            usersRelation.setRelation(Relation.convertIntegerToRelation(integerRelation));

            usersRelations.add(usersRelation);
            anotherUserCodeName = generateNextCodeName(anotherUserCodeName);
         }

         user.setRelations(usersRelations);
         users.add(user);
         codeName = generateNextCodeName(codeName);
      }

      return users;
   }

   private List<SocialNetworkUser> createDefaultUsers() {
      return createUsersByRelations(DEFAULT_USERS_RELATIONS);
   }

   private boolean isRelationsMatrixValid(Integer[][] matrix) {
      return isQuadrantMatrix(matrix) && isMatrixDiagonalNull(matrix) && isSymmetricMatrix(matrix);
   }

   private boolean isQuadrantMatrix(Integer[][] matrix) {
      int height = matrix.length;

      for (Integer[] row : matrix) {
         if (row.length != height) {
            log.warn("Non quadrant matrix!");
            return false;
         }
      }

      return true;
   }

   private boolean isMatrixDiagonalNull(Integer[][] matrix) {
      for (int i = 0; i < matrix.length; i++) {
         if (matrix[i][i] != null) {
            log.warn("Non diagonal matrix! Element index: [{}][{}]", i, i);
            return false;
         }
      }
      return true;
   }

   private boolean isSymmetricMatrix(Integer[][] matrix) {
      for (int i = 0; i < matrix.length; i++) {
         for (int j = 0; j < matrix[i].length; j++) {
            if (matrix[i][j] != null && !matrix[i][j].equals(matrix[j][i])) {
               log.warn("Non symmetric matrix! Element index: [{}][{}]", i, j);
               return false;
            }
         }
      }

      return true;
   }

   private String generateNextCodeName(String codeName) {
      if (codeName.endsWith("Z")) {
         return incrementCodeNameLength(codeName);
      } else {
         return incrementLastChar(codeName);
      }
   }

   private String incrementCodeNameLength(String codeName) {
      int length = codeName.length();

      StringBuilder builder = new StringBuilder(length + 1);
      for (int i = 0; i < length + 1; i++) {
         builder.append("A");
      }

      return builder.toString();
   }

   private String incrementLastChar(String codeName) {
      char lastChar = codeName.charAt(codeName.length() - 1);
      lastChar++;

      return codeName.substring(0, codeName.length() - 1) + lastChar;
   }
}

package bormashenko.michael.socialnetworkanalysis.service;

import bormashenko.michael.socialnetworkanalysis.exception.SNAnalysisException;
import bormashenko.michael.socialnetworkanalysis.repo.SocialNetworkUser;
import bormashenko.michael.socialnetworkanalysis.repo.UserRelation;
import bormashenko.michael.socialnetworkanalysis.repo.UserRepository;
import bormashenko.michael.socialnetworkanalysis.service.prediction.Prediction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static bormashenko.michael.socialnetworkanalysis.service.Util.Conversion.convertMatrixToUserList;

@Transactional
@Component
@Slf4j
public class SocialNetworkAnalysisServiceImpl implements SocialNetworkAnalysisService {

   private static Integer[][] DEFAULT_USERS_RELATIONS = new Integer[][]{ //todo remove -1
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
         {-1, 0, 1, 1, 1, 0, 0, 0, 0, -1, 1, 1, -1, -1, -1, null, 1, 1, 1, 1},
         {0, 1, -1, 1, -1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, null, -1, 1, -1},
         {0, 1, -1, 0, -1, 0, 1, 1, -1, 1, 1, -1, 0, 0, 1, 1, -1, null, 0, 1},
         {-1, 0, 1, 0, 1, 0, 1, 1, -1, 0, 1, -1, 0, 0, 1, 1, 1, 0, null, 1},
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
      log.debug("Detected addUser request");

      if (!userRepository.findByCodeName(user.getCodeName()).isPresent()) {
         log.debug("Adding a new user in the DB with codeName: {}.", user.getCodeName());
         userRepository.save(user);
      } else {
         throw new SNAnalysisException("User with codeName" + user.getCodeName() + " already exists!");
      }
   }

   @Override
   public List<SocialNetworkUser> getUsers() {
      log.debug("Detected getUsers request");
      return userRepository.findAll();
   }

   @Override
   public List<SocialNetworkUser> createUsersByRelations(Integer[][] relationMatrix) {
      return convertMatrixToUserList(relationMatrix);
   }

   @Override
   public Integer[][] predictRelations(Integer[][] relationMatrix, Prediction prediction) {
      return prediction.predict(relationMatrix);
   }

   private List<SocialNetworkUser> createDefaultUsers() {
      return convertMatrixToUserList(DEFAULT_USERS_RELATIONS);
   }

}

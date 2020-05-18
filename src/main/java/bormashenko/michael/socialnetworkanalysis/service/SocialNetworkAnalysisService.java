package bormashenko.michael.socialnetworkanalysis.service;

import bormashenko.michael.socialnetworkanalysis.repo.SocialNetworkUser;
import bormashenko.michael.socialnetworkanalysis.service.prediction.Prediction;

import java.util.List;

public interface SocialNetworkAnalysisService {

   void addUser(SocialNetworkUser user);

   List<SocialNetworkUser> getUsers();

   List<SocialNetworkUser> createUsersByRelations(Integer[][] relationMatrix);

   Integer[][] predictRelations(Integer[][] relationMatrix, Prediction prediction);
}

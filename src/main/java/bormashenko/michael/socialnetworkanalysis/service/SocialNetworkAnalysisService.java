package bormashenko.michael.socialnetworkanalysis.service;

import bormashenko.michael.socialnetworkanalysis.repo.SocialNetworkUser;

import java.util.List;

public interface SocialNetworkAnalysisService {

   void addUser(SocialNetworkUser user);

   List<SocialNetworkUser> getUsers();

   List<SocialNetworkUser> createUsersByRelations(Integer[][] relationMatrix);
}

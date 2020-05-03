package bormashenko.michael.socialnetworkanalysis.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<SocialNetworkUser, Long> {

   Optional<SocialNetworkUser> findByCodeName(String codeName);
}

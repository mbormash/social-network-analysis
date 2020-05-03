package bormashenko.michael.socialnetworkanalysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("bormashenko.michael.socialnetworkanalysis")
@EnableJpaRepositories("bormashenko.michael.socialnetworkanalysis")
@SpringBootApplication
public class SocialNetworkAnalysisApplication {

   public static void main(String[] args) {
      SpringApplication.run(SocialNetworkAnalysisApplication.class, args);
   }

}

package bormashenko.michael.socialnetworkanalysis.repo;

import bormashenko.michael.socialnetworkanalysis.service.Relation;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserRelation implements Serializable {

   private static final long serialVersionUID = -8913192462710591280L;

   @Id
   @GeneratedValue
   private Long id;

   @ManyToOne(fetch = FetchType.EAGER)
   private SocialNetworkUser user;

   @Column
   private String userCodeName;

   @Column
   private String anotherUserCodeName;

   @Enumerated(EnumType.STRING)
   @Column
   private Relation relation;

   @Override
   public String toString() {
      return "UserRelation{" +
            "id=" + id +
            ", userCodeName='" + userCodeName + '\'' +
            ", anotherUserCodeName='" + anotherUserCodeName + '\'' +
            ", relation=" + relation +
            '}';
   }
}

package bormashenko.michael.socialnetworkanalysis.repo;

import bormashenko.michael.socialnetworkanalysis.service.Relation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersRelation implements Serializable {

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
}

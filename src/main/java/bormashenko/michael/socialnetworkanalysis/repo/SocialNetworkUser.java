package bormashenko.michael.socialnetworkanalysis.repo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocialNetworkUser implements Serializable {

   private static final long serialVersionUID = -7617708335749678715L;

   @Id
   @GeneratedValue
   private Long id;

   @Column(unique = true)
   private String codeName;

   @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   private List<UsersRelation> relations;

}

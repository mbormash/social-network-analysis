package bormashenko.michael.socialnetworkanalysis.service.prediction.dijkstra;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Vertex {

   private int index;

   private List<Integer> neighbors;

   private List<Integer> costs;

   public void addNeighbor(int index) {
      if (neighbors == null) {
         neighbors = new ArrayList<>();
      }

      neighbors.add(index);
   }

   public void addCost(int cost) {
      if (costs == null) {
         costs = new ArrayList<>();
      }

      costs.add(cost);
   }

}

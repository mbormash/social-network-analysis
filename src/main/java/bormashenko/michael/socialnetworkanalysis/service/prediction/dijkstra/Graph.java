package bormashenko.michael.socialnetworkanalysis.service.prediction.dijkstra;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class Graph {

   private List<Vertex> vertexes;

   public Integer calculatePathBetweenVertexes(int source, int destination) {
      int size = vertexes.size();

      int[] values = new int[size];
      boolean[] used = new boolean[size];
      for (int i = 0; i < size; i++) {
         values[i] = Integer.MAX_VALUE;
         used[i] = false;
      }

      values[source] = 0;
      while (!allUsed(used)) {
         int minIndex = findMin(values, used);
         if (minIndex == -1) {
            return null;
         }

         Vertex vertex = vertexes.get(minIndex);
         for (int i = 0; i < vertex.getNeighbors().size(); i++) {
            int value = values[vertex.getIndex()] + vertex.getCosts().get(i);
            if (value < values[vertex.getNeighbors().get(i)]) {
               values[vertex.getNeighbors().get(i)] = value;
            }
         }

         used[vertex.getIndex()] = true;
      }

      return values[destination];
   }

   private int findMin(int[] values, boolean[] used) {
      int minimum = Integer.MAX_VALUE;
      int index = -1;

      for (int i = 0; i < values.length; i++) {
         if (values[i] < minimum && !used[i]) {
            index = i;
            minimum = values[i];
         }
      }

      return index;
   }

   private boolean allUsed(boolean[] used) {
      for (boolean b : used) {
         if (!b) {
            return false;
         }
      }

      return true;
   }
}
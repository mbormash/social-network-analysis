package bormashenko.michael.socialnetworkanalysis.service.prediction.dijkstra;

import java.util.ArrayList;
import java.util.List;

import static bormashenko.michael.socialnetworkanalysis.service.prediction.Prediction.NEUTRAL_RELATION;

public class Converter {

   public static Graph convertMatrixToGraph(Integer[][] matrix) {
      List<Vertex> vertexList = new ArrayList<>();

      for (int i = 0; i < matrix.length; i++) {
         Integer[] matrixRow = matrix[i];
         Vertex vertex = new Vertex();
         vertex.setIndex(i);

         for (int j = 0; j < matrixRow.length; j++) {
            if (matrixRow[j] != null && !matrixRow[j].equals(NEUTRAL_RELATION)) {
               vertex.addNeighbor(j);
               vertex.addCost(j, matrixRow[j]);
            }
         }

         vertexList.add(vertex);
      }

      return new Graph(vertexList);
   }
}

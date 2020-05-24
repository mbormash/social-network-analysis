package bormashenko.michael.socialnetworkanalysis.service.prediction;

import bormashenko.michael.socialnetworkanalysis.service.prediction.dijkstra.Converter;
import bormashenko.michael.socialnetworkanalysis.service.prediction.dijkstra.Graph;

import static bormashenko.michael.socialnetworkanalysis.service.Util.CalculationUtil.getRandomBooleanWithProbability;
import static bormashenko.michael.socialnetworkanalysis.service.Util.CalculationUtil.normalizeMatrixByX;

public class MinPathByDijkstraPredictionService implements Prediction {

   private static final double ALPHA = 0.2;

   @Override
   public Integer[][] predict(Integer[][] relationMatrix) {
      Prediction.verifyMatrix(relationMatrix);

      Integer[][] predictedRelations = new Integer[relationMatrix.length][relationMatrix.length];
      Graph graph = Converter.convertMatrixToGraph(relationMatrix);

      for (int i = 0; i < relationMatrix.length; i++) {
         Integer[] xRelations = relationMatrix[i];
         Integer[] predictedXRelations = new Integer[xRelations.length];

         for (int j = 0; j < xRelations.length; j++) {
            if (i == j) {
               predictedXRelations[j] = null;
               continue;
            }

            if (xRelations[j].equals(POSITIVE_RELATION)) {
               predictedXRelations[j] = POSITIVE_RELATION;
               continue;
            }

            boolean shouldConnect = predictByDijkstra(graph, i, j);
            predictedXRelations[j] = shouldConnect ? POSITIVE_RELATION : NEUTRAL_RELATION;
         }

         predictedRelations[i] = predictedXRelations;
      }

      normalizeMatrixByX(predictedRelations);
      return predictedRelations;
   }

   private boolean predictByDijkstra(Graph graph, int source, int destination) {
      Integer minPath = graph.calculatePathBetweenVertexes(source, destination);

      if (minPath == null || minPath == 0 || minPath > 1.0 / ALPHA) {
         return false;
      }

      if (minPath == 1) {
         return true;
      }

      double probability = (1 - minPath * ALPHA);
      return getRandomBooleanWithProbability(probability);
   }

}

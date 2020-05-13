package bormashenko.michael.socialnetworkanalysis.exception;

public class SNAnalysisException extends RuntimeException {

   public SNAnalysisException(String message) {
      super(message);
   }

   public SNAnalysisException(String message, Throwable cause) {
      super(message, cause);
   }

   public SNAnalysisException(Throwable cause) {
      super(cause);
   }
}

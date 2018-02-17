package graph;

/**
 * Custom exception handling for the exceptions throwed by the graph.
 */
public class RenderException extends Exception {

    public RenderException(String message) {
        super(message);
    }

    public RenderException(String message, Throwable cause) {
        super(message, cause);
    }

}

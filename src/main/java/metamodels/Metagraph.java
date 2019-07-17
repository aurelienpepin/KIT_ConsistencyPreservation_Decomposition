package metamodels;

import java.util.function.Supplier;
import metamodels.vertices.ENamedElementVertex;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

/**
 *
 * @author Aurelien
 */
public class Metagraph extends DefaultUndirectedGraph<ENamedElementVertex, DefaultEdge> {
    
    public Metagraph() {
        super(DefaultEdge.class);
    }

    public Metagraph(Supplier<ENamedElementVertex> vertexSupplier, Supplier<DefaultEdge> edgeSupplier, boolean weighted) {
        super(vertexSupplier, edgeSupplier, weighted);
    }
}

package metamodels;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import metamodels.vertices.ENamedElementVertex;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

/**
 *
 * @author Aurelien
 */
public class Metagraph extends DefaultUndirectedGraph<ENamedElementVertex, DefaultEdge> {
    
    private final Map<String, ENamedElementVertex> elementsAsVertices;
    
    public Metagraph() {
        super(DefaultEdge.class);
        this.elementsAsVertices = new HashMap<>();
    }

    public Metagraph(Supplier<ENamedElementVertex> vertexSupplier, Supplier<DefaultEdge> edgeSupplier, boolean weighted) {
        super(vertexSupplier, edgeSupplier, weighted);
        this.elementsAsVertices = new HashMap<>();
    }

    @Override
    public boolean addVertex(ENamedElementVertex v) {
        this.elementsAsVertices.put(v.getFullName(), v);
        return super.addVertex(v);
    }
    
    public Set<String> getVertices() {
        return this.elementsAsVertices.keySet();
    }
}

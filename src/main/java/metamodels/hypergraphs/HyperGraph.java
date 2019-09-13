package metamodels.hypergraphs;

import java.util.Set;
import java.util.HashSet;

/**
 * Represents a hypergraph.
 * The class only includes the methods necessary to manipulate
 * a hypergraph in the context of the decomposition procedure.
 * 
 * @author  Aurélien Pepin
 * @param   <V> The vertex type, derived from HyperVertex
 * @param   <E> The edge type, derived from HyperEdge
 */
public class HyperGraph<V extends HyperVertex, E extends HyperEdge> {

    private final Set<V> vertices;
    private final Set<E> edges;
    
    public HyperGraph() {
        this.vertices = new HashSet<>();
        this.edges = new HashSet<>();
    }
    
    public boolean addEdge(E e) {
        if (e == null)
            throw new NullPointerException("Edge cannot be null");
        
        if (this.containsEdge(e))
            return false;
        
        this.edges.add(e);
        return true;        
    } 
   
    public boolean addVertex(V v) {
        if (v == null)
            throw new NullPointerException("Vertex cannot be null");
        
        if (this.containsVertex(v))
            return false;
        
        this.vertices.add(v);
        return true;
    }
    
    public boolean containsVertex(V v) {
        return vertices.contains(v);
    }
    
    public boolean containsEdge(E e) {
        return edges.contains(e);
    }

    public Set<E> edgeSet() {
        return edges;
    }

    public Set<V> vertexSet() {
        return vertices;
    }

    /**
     * Merge two hypergraphs by adding the elements of one into the other.
     * @param dest
     * @param source
     */
    public static void addHyperGraph(HyperGraph dest, HyperGraph source) {
        dest.vertexSet().addAll(source.vertexSet());
        dest.edgeSet().addAll(source.edgeSet());
    }
    
    @Override
    public String toString() {
        return "(" + vertices + " | " + edges + ")";
    }
}

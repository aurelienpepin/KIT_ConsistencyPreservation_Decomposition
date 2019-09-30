package metamodels.hypergraphs;

import java.io.Serializable;
import java.util.Set;

/**
 * Represents a hyperedge, i.e. an edge that can link two or more hypervertices.
 * 
 * @see     HyperGraph
 * @author  Aurélien Pepin
 * @param   <V> The vertex type, derived from HyperVertex
 */
public abstract class HyperEdge<V extends HyperVertex> implements Cloneable, Serializable {
    
    protected final Set<V> vertices;
    
    public HyperEdge(Set<V> vertices) {
        this.vertices = vertices;
    }
    
    @Override
    public String toString() {
        return "(" + vertices + ")";
    }

    public Set<V> getVertices() {
        return vertices;
    }
}

package metamodels.hypergraphs;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import org.jgrapht.Graph;

/**
 *
 * @author Aurélien Pepin
 */
public class HyperGraph<V extends HyperVertex, E extends HyperEdge> { // implements Graph<HyperVertex, HyperEdge> {

    private final Set<V> vertices;
    private final Set<E> edges;
    
    public HyperGraph() {
        this.vertices = new HashSet<>();
        this.edges = new HashSet<>();
    }

    // TODO
    // public void addEdge(Iterable<HyperVertex> vertices) {
    //     HyperEdge edge = new HyperEdge();
    //     throw new RuntimeException("allez fais ce hyperedge");
    // }
    
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
}

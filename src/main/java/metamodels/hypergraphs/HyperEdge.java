package metamodels.hypergraphs;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Aurélien Pepin
 */
public class HyperEdge implements Cloneable, Serializable {
    
    private final Set<HyperVertex> vertices;
    // TODO. Constraint
    
    public HyperEdge() {
        this.vertices = new HashSet<>();
        throw new RuntimeException("hyperedge constraint");
    }
    
    @Override
    public String toString() {
        return "(" + vertices + ")";
    }
}

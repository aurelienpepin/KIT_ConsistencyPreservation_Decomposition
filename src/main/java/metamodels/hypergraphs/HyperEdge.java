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
    
    public HyperEdge() {
        this.vertices = new HashSet<>();
    }
    
    @Override
    public String toString() {
        return "(" + vertices + ")";
    }
}

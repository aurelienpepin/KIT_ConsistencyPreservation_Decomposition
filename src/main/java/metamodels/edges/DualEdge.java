package metamodels.edges;

import java.util.Set;
import metamodels.vertices.MetaVertex;
import org.jgrapht.graph.DefaultEdge;

/**
 *
 * @author Aurélien Pepin
 */
public class DualEdge extends DefaultEdge {
    
    /**
     * Shared metamodel elements.
     */
    private final Set<MetaVertex> sharedElements;
    
    public DualEdge(Set<MetaVertex> sharedElements) {
        if (sharedElements == null || sharedElements.isEmpty())
            throw new NullPointerException("No null or empty intersection for dual edges");
        
        this.sharedElements = sharedElements;
    }

    @Override
    public String toString() {
        return "(" + sharedElements + ")";
    }

    public Set<MetaVertex> getSharedElements() {
        return sharedElements;
    }
}

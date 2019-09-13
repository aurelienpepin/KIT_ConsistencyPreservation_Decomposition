package metamodels.edges;

import java.util.Set;
import metamodels.vertices.MetaVertex;
import org.jgrapht.graph.DefaultEdge;

/**
 * Represents a dual edge, i.e. the set of shared metamodel elements between
 * two constraints. A DualEdge is part of a DualGraph.
 * 
 * @author Aurélien Pepin
 */
public class DualEdge extends DefaultEdge {
    
    /**
     * Shared metamodel elements.
     */
    private final Set<MetaVertex> sharedElements;
    
    /**
     * Creates a dual edge from a set of shared elements.
     * The set cannot be null.
     * 
     * @param sharedElements    
     */
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

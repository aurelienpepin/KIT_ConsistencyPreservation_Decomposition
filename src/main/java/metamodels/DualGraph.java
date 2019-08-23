package metamodels;

import metamodels.edges.DualEdge;
import metamodels.edges.MetaEdge;
import org.jgrapht.graph.SimpleGraph;

/**
 *
 * @author Aur�lien Pepin
 */
public class DualGraph extends SimpleGraph<MetaEdge, DualEdge> {
    
    public DualGraph() {
        super(DualEdge.class);
    }
}

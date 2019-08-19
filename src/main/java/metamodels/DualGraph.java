package metamodels;

import metamodels.hypergraphs.HyperEdge;
import metamodels.vertices.MetaVertex;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

/**
 *
 * @author Aurélien Pepin
 */
public class DualGraph extends SimpleGraph<HyperEdge, DefaultEdge> {
    
    public DualGraph() {
        super(DefaultEdge.class);
    }
}

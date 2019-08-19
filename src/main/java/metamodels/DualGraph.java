package metamodels;

import metamodels.hypergraphs.HyperVertex;
import metamodels.vertices.MetaVertex;
import org.jgrapht.graph.SimpleGraph;

/**
 *
 * @author Aurélien Pepin
 */
public class DualGraph extends SimpleGraph<HyperVertex, MetaVertex> {
    
    // TODO CHANGE METAVERTEX         ----v
    public DualGraph(Class<? extends MetaVertex> edgeClass) {
        super(edgeClass);
    }
}

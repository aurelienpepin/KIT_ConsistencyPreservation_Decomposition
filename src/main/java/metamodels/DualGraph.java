package metamodels;

import metamodels.edges.DualEdge;
import metamodels.edges.MetaEdge;
import metamodels.vertices.MetaVertex;
import org.jgrapht.graph.SimpleGraph;
import parsers.qvtr.QVTSpecification;

/**
 * Represents a dual graph, i.e. the dual of a metagraph.
 * In the dual graph, the metaedges are the vertices and the sets of
 * metavertices are the edges.
 * 
 * A metagraph and its dual graph are isomorphic. The advantage of the metagraph
 * over the dual graph is that it only has binary edges.
 * 
 * TODO: preconditions are not shared from metagraph to dual graph (!)
 * 
 * @author Aurélien Pepin
 */
public class DualGraph extends SimpleGraph<MetaEdge, DualEdge> {

    /**
     * An intermediate representation of the consistency specification.
     * 
     * A metagraph has access to the consistency
     * specification, and so does its dual graph.
     */
    private final QVTSpecification spec;
    
    public DualGraph() {
        super(DualEdge.class);
        this.spec = new QVTSpecification();
    }
    
    public DualGraph(QVTSpecification spec) {
        super(DualEdge.class);
        this.spec = spec;
    }
    
    /**
     * Transforms a dual graph into a metagraph.
     * 
     * @return The isomorphic metagraph.
     */
    public MetaGraph toMeta() {
        MetaGraph meta = new MetaGraph(spec);
        
        // Edge -> Vertex
        for (DualEdge edge : this.edgeSet()) {
            for (MetaVertex vertex : edge.getSharedElements()) {
                meta.addVertex(vertex);
            }
        }
        
        // Vertex -> Edge
        for (MetaEdge constraint : this.vertexSet()) {
            meta.addEdge(constraint);
        }
        
        return meta;
    }
}

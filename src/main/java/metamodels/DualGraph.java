package metamodels;

import metamodels.edges.DualEdge;
import metamodels.edges.MetaEdge;
import metamodels.vertices.MetaVertex;
import org.jgrapht.graph.SimpleGraph;
import parsers.qvtr.QVTSpecification;

/**
 *
 * @author Aurélien Pepin
 */
public class DualGraph extends SimpleGraph<MetaEdge, DualEdge> {
    
    private QVTSpecification spec;
    
    public DualGraph() {
        super(DualEdge.class);
        this.spec = new QVTSpecification();
    }
    
    public DualGraph(QVTSpecification spec) {
        super(DualEdge.class);
        this.spec = spec;
    }
    
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

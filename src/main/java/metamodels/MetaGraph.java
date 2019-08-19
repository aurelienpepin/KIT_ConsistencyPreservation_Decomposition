package metamodels;

import metamodels.edges.MetaEdge;
import metamodels.hypergraphs.HyperEdge;
import metamodels.hypergraphs.HyperGraph;
import metamodels.vertices.MetaVertex;
import parsers.qvtr.QVTSpecification;

public class MetaGraph extends HyperGraph<MetaVertex, MetaEdge> {
    
    private QVTSpecification spec;
    
    public MetaGraph() {
        this.spec = new QVTSpecification();
    }
    
    public MetaGraph(QVTSpecification spec) {
        if (spec == null)
            throw new NullPointerException("Specification cannot be null");
        
        this.spec = spec;
    }
    
    public void setSpecification(QVTSpecification spec) {
        if (spec == null)
            throw new NullPointerException("Specification cannot be null");
        
        this.spec = spec;
    }
    
    public DualGraph toDual() {
        DualGraph dual = new DualGraph();
        
        for (MetaEdge edge : this.edgeSet())
            dual.addVertex(edge);
        
        // Generate combinations of two.
        
        throw new UnsupportedOperationException("TODO");
    }
}

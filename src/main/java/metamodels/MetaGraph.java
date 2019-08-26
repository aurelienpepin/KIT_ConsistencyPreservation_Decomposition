package metamodels;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import metamodels.edges.DualEdge;
import metamodels.edges.MetaEdge;
import metamodels.hypergraphs.HyperEdge;
import metamodels.hypergraphs.HyperGraph;
import metamodels.hypergraphs.HyperVertex;
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
        DualGraph dual = new DualGraph(spec);
        
        for (MetaEdge edge : this.edgeSet())
            dual.addVertex(edge);
        
        List<MetaEdge> edgeList = new ArrayList<>(this.edgeSet());
        
        // Check common metamodel elements between constraints
        for (int i = 0; i < edgeList.size(); ++i) {
            for (int j = i + 1; j < edgeList.size(); ++j) {
                Set<MetaVertex> constraint1 = edgeList.get(i).getVertices();
                Set<MetaVertex> constraint2 = edgeList.get(j).getVertices();
                
                if (!Collections.disjoint(constraint1, constraint2)) {
                    Set<MetaVertex> intersection = new HashSet<>(constraint1);
                    intersection.retainAll(constraint2);
                    
                    DualEdge dualEdge = new DualEdge(intersection);
                    dual.addEdge(edgeList.get(i), edgeList.get(j), dualEdge);
                }
            }
        }
        
        return dual;
    }
}

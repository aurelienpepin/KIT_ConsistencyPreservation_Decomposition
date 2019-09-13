package metamodels;

import com.microsoft.z3.BoolExpr;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import metamodels.edges.DualEdge;
import metamodels.edges.MetaEdge;
import metamodels.hypergraphs.HyperGraph;
import metamodels.vertices.MetaVertex;
import parsers.qvtr.QVTSpecification;

/**
 * Represents a metagraph, i.e. a graph whose vertices are metamodel elements
 * and whose edges are constraints on subsets of metamodel elements.
 * 
 * Constraints can involve more than two elements, so the metagraph is a
 * hypergraph (and not only a simple graph).
 * 
 * @author Aurélien Pepin
 */
public class MetaGraph extends HyperGraph<MetaVertex, MetaEdge> {
    
    /**
     * The set of preconditions (`when` clauses in QVTR).
     */
    private Set<BoolExpr> preconditions;
    
    /**
     * An intermediate representation of the consistency specification.
     */
    private QVTSpecification spec;
    
    public MetaGraph() {
        this.spec = new QVTSpecification();
        this.preconditions = new HashSet<>();
    }
    
    public MetaGraph(QVTSpecification spec) {
        if (spec == null)
            throw new NullPointerException("Specification cannot be null");
        
        this.spec = spec;
        this.preconditions = new HashSet<>();
    }
    
    public void setSpecification(QVTSpecification spec) {
        if (spec == null)
            throw new NullPointerException("Specification cannot be null");
        
        this.spec = spec;
    }
    
    public void addPrecondition(BoolExpr precondition) {
        if (precondition == null)
            throw new NullPointerException("Precondition cannot be null");
        
        this.preconditions.add(precondition);
    }

    public Set<BoolExpr> getPreconditions() {
        return preconditions;
    }
    
    /**
     * Transforms a metagraph into a dual graph.
     * In the dual graph, constraints (edges) become vertices and vertices
     * are grouped into sets that represent edges (metamodel elements shared
     * between constraints).
     * 
     * @return The isomorphic dual graph.
     */
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

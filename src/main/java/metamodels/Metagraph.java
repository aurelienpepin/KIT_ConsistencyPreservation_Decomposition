package metamodels;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import metamodels.edges.PredicateEdge;
import metamodels.vertices.ENamedElementVertex;
import org.eclipse.emf.ecore.ENamedElement;
import org.jgrapht.graph.DefaultUndirectedGraph;

/**
 *
 * @author Aurelien
 */
public class Metagraph extends DefaultUndirectedGraph<ENamedElementVertex, PredicateEdge> {
    
    private final Map<ENamedElement, ENamedElementVertex> elementsAsVertices;
    
    public Metagraph() {
        super(PredicateEdge.class);
        this.elementsAsVertices = new HashMap<>();
    }

    // public Metagraph(Supplier<ENamedElementVertex> vertexSupplier, Supplier<PredicateEdge> edgeSupplier, boolean weighted) {
    //     super(vertexSupplier, edgeSupplier, weighted);
    //     this.elementsAsVertices = new HashMap<>();
    // }

    @Override
    public boolean addVertex(ENamedElementVertex v) {
        this.elementsAsVertices.put(v.getElement(), v);
        return super.addVertex(v);
    }
    
    public Set<ENamedElement> elementsInVertices() {
        Set<ENamedElement> elements = new HashSet<>();
        
        for (ENamedElementVertex vertex : this.vertexSet()) {
            elements.add(vertex.getElement());
        }
        
        return elements;
    }
    
    public ENamedElementVertex getVerticeFrom(ENamedElement elem) {
        return this.elementsAsVertices.get(elem);
    }
}

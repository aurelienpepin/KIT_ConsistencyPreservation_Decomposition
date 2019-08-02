package metamodels;

import java.util.HashMap;
import java.util.Map;
import metamodels.edges.PredicateEdge;
import metamodels.vertices.ENamedElementVertex;
import org.eclipse.emf.ecore.ENamedElement;
import org.jgrapht.graph.DefaultUndirectedGraph;
import parsers.relations.QVTSpecification;

/**
 *
 * @author Aurelien
 */
public class Metagraph extends DefaultUndirectedGraph<ENamedElementVertex, PredicateEdge> {

    private final Map<ENamedElement, ENamedElementVertex> elementsAsVertices;
    
    private QVTSpecification spec;
    
    public Metagraph() {
        super(PredicateEdge.class);
        this.elementsAsVertices = new HashMap<>();
        this.spec = new QVTSpecification();
    }
    
    public Metagraph(QVTSpecification spec) {
        super(PredicateEdge.class);
        this.elementsAsVertices = new HashMap<>();
        this.spec = spec;
    }

    public void setSpecification(QVTSpecification spec) {
        this.spec = spec;
    }
    
    public QVTSpecification getSpec() {
        return spec;
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
    
//    public Set<ENamedElement> elementsInVertices() {
//        Set<ENamedElement> elements = new HashSet<>();
//        
//        for (ENamedElementVertex vertex : this.vertexSet()) {
//            elements.add(vertex.getElement());
//        }
//        
//        return elements;
//    }
//    
//    public ENamedElementVertex getVerticeFrom(ENamedElement elem) {
//        return this.elementsAsVertices.get(elem);
//    }
}

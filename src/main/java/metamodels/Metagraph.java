package metamodels;

import java.util.HashMap;
import java.util.Map;
import metamodels.edges.PredicateEdge;
import metamodels.vertices.Metavertex;
import metamodels.vertices.ecore.ENamedElementVertex;
import org.eclipse.emf.ecore.ENamedElement;
import org.jgrapht.graph.DefaultUndirectedGraph;
import parsers.qvtr.QVTSpecification;

/**
 *
 * @author Aurelien
 */
public class Metagraph extends DefaultUndirectedGraph<Metavertex, PredicateEdge> {

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

    @Override
    public boolean addVertex(Metavertex v) {
        // this.elementsAsVertices.put(v.getElement(), v);
        return super.addVertex(v);
    }
}

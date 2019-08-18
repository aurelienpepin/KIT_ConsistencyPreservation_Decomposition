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
public class MetagraphOld extends DefaultUndirectedGraph<Metavertex, PredicateEdge> {

    private final Map<ENamedElement, ENamedElementVertex> elementsAsVertices;
    
    private QVTSpecification spec;
    
    public MetagraphOld() {
        super(PredicateEdge.class);
        this.elementsAsVertices = new HashMap<>();
        this.spec = new QVTSpecification();
    }
    
    public MetagraphOld(QVTSpecification spec) {
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

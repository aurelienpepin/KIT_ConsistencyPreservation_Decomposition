package metamodels;

import metamodels.hypergraphs.HyperEdge;
import metamodels.hypergraphs.HyperGraph;
import metamodels.vertices.Metavertex;
import parsers.qvtr.QVTSpecification;

public class Metagraph extends HyperGraph<Metavertex, HyperEdge> {
    
    private QVTSpecification spec;
    
    public Metagraph() {
        this.spec = new QVTSpecification();
    }
    
    public Metagraph(QVTSpecification spec) {
        if (spec == null)
            throw new NullPointerException("Specification cannot be null");
        
        this.spec = spec;
    }
    
    public void setSpecification(QVTSpecification spec) {
        if (spec == null)
            throw new NullPointerException("Specification cannot be null");
        
        this.spec = spec;
    }
}

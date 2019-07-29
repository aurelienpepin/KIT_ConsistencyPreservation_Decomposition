package metamodels.edges;

import com.microsoft.z3.BoolExpr;
import org.jgrapht.graph.DefaultEdge;

/**
 *
 * @author Aurélien Pepin
 */
public class PredicateEdge extends DefaultEdge {
    
    private final BoolExpr predicate;
    
    public PredicateEdge(BoolExpr predicate) {
        this.predicate = predicate;
    }

    @Override
    public String toString() {
        // return super.toString() + "!!" + predicate + "!!"; //To change body of generated methods, choose Tools | Templates.
        return "!!" + predicate + "!!";
    }
    
    public BoolExpr getPredicate() {
        return predicate;
    }
}

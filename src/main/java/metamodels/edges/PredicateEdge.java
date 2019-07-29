package metamodels.edges;

import com.microsoft.z3.BoolExpr;
import org.jgrapht.graph.DefaultEdge;

/**
 *
 * @author Aur�lien Pepin
 */
public class PredicateEdge extends DefaultEdge {
    
    private final BoolExpr predicate;
    
    public PredicateEdge(BoolExpr predicate) {
        this.predicate = predicate;
    }
}

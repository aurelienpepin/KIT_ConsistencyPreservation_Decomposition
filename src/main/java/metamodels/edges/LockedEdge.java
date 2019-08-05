package metamodels.edges;

import com.microsoft.z3.BoolExpr;

/**
 *
 * @author Aurélien Pepin
 */
public class LockedEdge extends PredicateEdge {
    
    public LockedEdge(BoolExpr predicate) {
        super(predicate);
    }

    @Override
    public boolean isLocked() {
        return true;
    }

    @Override
    public String toString() {
        return "[[LOCKED]] " + super.toString(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}

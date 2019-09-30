package metamodels.edges;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;
import java.util.HashSet;
import java.util.Set;
import metamodels.hypergraphs.HyperEdge;
import metamodels.vertices.MetaVertex;
import parsers.qvtr.QVTVariable;
import procedure.translators.TranslatorContext;

/**
 * Represents an edge in a metagraph, i.e. a constraint that involves at least
 * two metamodel elements (MetaVertex). The MetaEdge includes a set of
 * vertices, a set of Z3 expressions (formulas) and a set of free variables
 * that appear in formulas.
 * 
 * @author Aurélien Pepin
 */
public class MetaEdge extends HyperEdge<MetaVertex> {
    
    /**
     * A set of free variables.
     * 
     * They are used to bind elements from different metamodels.
     * They are stored with constraints because they appear in the logical formulas.
     */
    private final Set<QVTVariable> freeVariables;
    
    /**
     * A set of Z3 expressions, i.e. logical formulas which encode consistency
     * constraints.
     */
    private final Set<Expr> expressions;
    
    
    public MetaEdge(Set<MetaVertex> vertices, Set<Expr> expressions, Set<QVTVariable> freeVariables) {
        super(new HashSet<>());
        
        this.vertices.addAll(vertices);
        this.freeVariables = freeVariables;        
        this.expressions = expressions;
    }
    
    /**
     * Returns a predicate, i.e. the logical conjunction of all Z3 expressions.
     * @return A Boolean predicate
     */
    public BoolExpr getPredicate() {
        return TranslatorContext.getInstance().getZ3Ctx().mkAnd(expressions.toArray(new BoolExpr[expressions.size()])); 
    }
    
    public Set<Expr> getPredicateParts() {
        return expressions;
    }
    
    public Set<QVTVariable> getFreeVariables() {
        return freeVariables;
    }

    public Set<Expr> getExpressions() {
        return expressions;
    }
}

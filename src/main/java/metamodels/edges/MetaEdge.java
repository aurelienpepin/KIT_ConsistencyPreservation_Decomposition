package metamodels.edges;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;
import java.util.HashSet;
import java.util.Set;
import metamodels.hypergraphs.HyperEdge;
import metamodels.vertices.MetaVertex;
import org.eclipse.ocl.pivot.Variable;
import parsers.qvtr.QVTVariable;
import procedure.translators.TranslatorContext;

/**
 *
 * @author Aurelien
 */
public class MetaEdge extends HyperEdge<MetaVertex> {
    
    private final Set<QVTVariable> freeVariables;
    
    public MetaEdge(Set<MetaVertex> vertices, Set<Expr> expressions, Set<QVTVariable> freeVariables) {
        super(new HashSet<>(), expressions);
        this.vertices.addAll(vertices);
        this.freeVariables = freeVariables;
    }
    
    public Expr getPredicate() {
        return TranslatorContext.getInstance().getZ3Ctx().mkAnd(expressions.toArray(new BoolExpr[expressions.size()])); 
    }
    
    public Set<Expr> getPredicateParts() {
        return expressions;
    }

    public Set<QVTVariable> getFreeVariables() {
        return freeVariables;
    }
}

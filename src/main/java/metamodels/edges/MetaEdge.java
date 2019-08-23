package metamodels.edges;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;
import java.util.HashSet;
import java.util.Set;
import metamodels.hypergraphs.HyperEdge;
import metamodels.vertices.MetaVertex;
import procedure.translators.TranslatorContext;

/**
 *
 * @author Aurelien
 */
public class MetaEdge extends HyperEdge<MetaVertex> {
    
    public MetaEdge(Set<MetaVertex> vertices, Set<Expr> expressions) {
        super(new HashSet<>(), expressions);
        this.vertices.addAll(vertices);
    }
    
    public Expr getPredicate() {
        return TranslatorContext.getInstance().getZ3Ctx().mkAnd(expressions.toArray(new BoolExpr[expressions.size()])); 
   }
}

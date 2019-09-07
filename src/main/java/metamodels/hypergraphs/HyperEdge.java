package metamodels.hypergraphs;

import com.microsoft.z3.Expr;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Aurélien Pepin
 * @param <V>
 */
public abstract class HyperEdge<V extends HyperVertex> implements Cloneable, Serializable {
    
    protected final Set<V> vertices;
    
    protected final Set<Expr> expressions;
    
    public HyperEdge(Set<V> vertices, Set<Expr> expressions) {
        this.vertices = vertices;
        this.expressions = expressions;
    }
    
    @Override
    public String toString() {
        return "(" + expressions + ")";
    }

    public Set<Expr> getExpressions() {
        return expressions;
    }

    public Set<V> getVertices() {
        return vertices;
    }
}

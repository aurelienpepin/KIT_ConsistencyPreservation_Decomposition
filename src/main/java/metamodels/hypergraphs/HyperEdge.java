package metamodels.hypergraphs;

import com.microsoft.z3.Expr;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Aurélien Pepin
 */
public abstract class HyperEdge implements Cloneable, Serializable {
    
    protected final Set<HyperVertex> vertices;
    
    protected final Set<Expr> expressions;
    
    public HyperEdge() {
        this.vertices = new HashSet<>();
        this.expressions = new HashSet<>();
        throw new RuntimeException("hyperedge constraint");
    }
    
    public HyperEdge(Set<HyperVertex> vertices, Set<Expr> expressions) {
        this.vertices = vertices;
        this.expressions = expressions;
    }
    
    @Override
    public String toString() {
        return "(" + vertices + ")";
    }

    public Set<Expr> getExpressions() {
        return expressions;
    }

    public Set<HyperVertex> getVertices() {
        return vertices;
    }
}

package metamodels.edges;

import com.microsoft.z3.Expr;
import java.util.HashSet;
import java.util.Set;
import metamodels.vertices.MetaVertex;
import procedure.translators.VariableIndexer;

/**
 * Intermediate structure between QVT-R domain patterns and hyperedges.
 * An EdgeAssembler groups expressions and vertices. It is associated
 * with QVT-R variables and used during the merge algorithm of
 * VariableIndexer (construction of constraints).
 * 
 * @see     VariableIndexer
 * @author  Aurélien Pepin
 */
public class EdgeAssembler {
    
    private final Set<Expr> expressions;
    
    private final Set<MetaVertex> vertices;

    public EdgeAssembler() {
        this.expressions = new HashSet<>();
        this.vertices = new HashSet<>();
    }
    
    public void add(EdgeAssembler other) {
        this.expressions.addAll(other.getExpressions());
        this.vertices.addAll(other.getVertices());
    }
    
    public void addExpression(Expr expression) {
        if (expression == null)
            throw new NullPointerException("Null expression is forbidden");
        
        this.expressions.add(expression);
    }
    
    public void addVertex(MetaVertex vertex) {
        if (vertex == null)
            throw new NullPointerException("Null vertex is forbidden");
        
        this.vertices.add(vertex);
    }

    public Set<Expr> getExpressions() {
        return expressions;
    }

    public Set<MetaVertex> getVertices() {
        return vertices;
    }

    @Override
    public String toString() {
        return "EA-(" + expressions + ")-(" + vertices + ")";
    }
}

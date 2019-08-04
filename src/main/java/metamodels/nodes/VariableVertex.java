package metamodels.nodes;

import metamodels.vertices.ENamedElementVertex;
import org.eclipse.ocl.pivot.Variable;

/**
 *
 * @author Aurélien Pepin
 */
public class VariableVertex extends SimpleMetavertex {
    
    /**
     * The element to which the variable is bound.
     * Can be null.
     */
    private final ENamedElementVertex alias;
    
    /**
     * The OCL variable itself. TODO : variable factory
     */
    private final Variable variable;
    
    public VariableVertex(Variable variable, ENamedElementVertex element) {
        this.variable = variable;
        this.alias = element;
    }
    
    public VariableVertex(Variable variable) {
        this(variable, null);
    }
}

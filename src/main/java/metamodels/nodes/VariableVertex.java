package metamodels.nodes;

import metamodels.vertices.ENamedElementVertex;
import org.eclipse.ocl.pivot.Variable;

/**
 *
 * @author Aurélien Pepin
 */
public class VariableVertex extends SimpleMetavertex {

    /**
     * The OCL variable itself. TODO : variable factory
     */
    private final Variable variable;

    public VariableVertex(Variable variable) {
        this.variable = variable;
    }
}

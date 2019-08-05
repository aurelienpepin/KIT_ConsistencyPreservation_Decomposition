package metamodels.nodes;

import metamodels.vertices.ENamedElementVertex;
import org.eclipse.ocl.pivot.Variable;
import parsers.relations.QVTRelation;

/**
 *
 * @author Aurélien Pepin
 */
public class VariableVertex extends SimpleMetavertex {

    /**
     * The OCL variable itself.
     */
    private final Variable variable;
    
    /**
     * The QVT-R relation in which the variable is used.
     * Useful because two variables in different relations can have the same name.
     */
    private final QVTRelation relation;
    
    private final int id;

    public VariableVertex(Variable variable, QVTRelation relation, int id) {
        if (variable == null || relation == null)
            throw new RuntimeException("Unable to create a VariableVertex");
        
        this.variable = variable;
        this.relation = relation;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Var(" + this.relation.getName() + "){" + this.variable + " " + this.id + "}";
    }
}

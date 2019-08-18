package parsers.qvtr;

import org.eclipse.ocl.pivot.Variable;

/**
 *
 * @author Aurélien Pepin
 */
public class QVTVariable {
    
    /**
     * The OCL/QVT-R variable itself.
     */
    private final Variable variable;
    
    /**
     * The QVT-R relation in which the variable is used.
     * Useful because two variables in different relations can have the same name.
     */
    private final QVTRelation relation;
    
    /**
     * Represent the id^th occurrence of the variable.
     */
    private final int id;
    
    public QVTVariable(Variable variable, QVTRelation relation, int id) {
        if (variable == null || relation == null)
            throw new RuntimeException("Unable to create a QVTVariable");
        
        this.variable = variable;
        this.relation = relation;
        this.id = id;
    }
    
    @Override
    public String toString() {
        return "Var(" + this.relation.getName() + "){" + this.variable + " " + this.id + "}";
    }

    public String getFullName() {
        return "Var(" + this.relation.getName() + "){" + this.variable.getName() + " " + this.id + "}";
    }

    public Variable getVariable() {
        return variable;
    }
}

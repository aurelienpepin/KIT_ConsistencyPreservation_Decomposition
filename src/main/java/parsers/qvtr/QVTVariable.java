package parsers.qvtr;

import org.eclipse.ocl.pivot.Variable;

/**
 *
 * @author Aurélien Pepin
 */
public class QVTVariable {
    
    /**
     * The relation to which the variable belongs.
     */
    private final QVTRelation relation;
    
    private final Variable variable;
    
    public QVTVariable(Variable variable, QVTRelation relation) {
        if (variable == null || relation == null)
            throw new NullPointerException("Null instantiation of a QVT-R variable.");
        
        this.variable = variable;
        this.relation = relation;
    }
    
    public String getFullName() {
        return relation.getName() + "@" + variable.getName();
    }

    @Override
    public String toString() {
        return this.getFullName();
    }
}

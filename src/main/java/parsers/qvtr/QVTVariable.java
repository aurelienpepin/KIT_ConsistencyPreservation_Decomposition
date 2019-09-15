package parsers.qvtr;

import com.microsoft.z3.Expr;
import org.eclipse.ocl.pivot.Variable;

/**
 * Represents a variable in QVT-R.
 * Variables are used to bind metamodel elements.
 * 
 * @author Aurélien Pepin
 */
public class QVTVariable {
    
    /**
     * The relation to which the variable belongs.
     */
    private final QVTRelation relation;
    
    /**
     * Corresponding QVT-R variable element.
     */
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
    
    /**
     * Transform the QVT-R variable into a Z3 constant.
     * TODO: refactor with ConstraintFactory
     * 
     * @return The variable as a Z3 expression
     */
    public Expr getExpr() {
        return relation.getConstraintVisitor().getFactory().fromVariable(variable, relation);
    }
}

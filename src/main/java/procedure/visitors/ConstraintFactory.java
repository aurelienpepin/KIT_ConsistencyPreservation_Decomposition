package procedure.visitors;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.Expr;
import com.microsoft.z3.SeqExpr;
import com.microsoft.z3.Sort;
import java.util.List;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.ocl.pivot.OperationCallExp;
import org.eclipse.ocl.pivot.Variable;
import org.eclipse.ocl.pivot.VariableExp;
import parsers.qvtr.QVTRelation;
import procedure.translators.TranslatorContext;

/**
 * Transform complex OCL expressions into Z3 predicates.
 * @author Aurélien Pepin
 */
public class ConstraintFactory {

    private final TranslatorContext context;
    
    public ConstraintFactory(TranslatorContext context) {
        this.context = context;
    }
     
    public Expr fromOperationCall(OperationCallExp oce, List<Expr> operands) {
        // System.out.println("t: " + oce.getReferredOperation().getType());
        switch (oce.getReferredOperation().getOperationId().getName()) {
            case "+":
                if ("String".equals(oce.getReferredOperation().getType().toString())) // TODO custom function (for readability)
                    return context.getZ3Ctx().mkConcat(operands.toArray(new SeqExpr[operands.size()]));
                else
                    return context.getZ3Ctx().mkAdd(operands.toArray(new ArithExpr[operands.size()]));
            case "-":
                return context.getZ3Ctx().mkSub(operands.toArray(new ArithExpr[operands.size()]));
            case "*":
                return context.getZ3Ctx().mkMul(operands.toArray(new ArithExpr[operands.size()]));
            default:
                throw new UnsupportedOperationException("Unsupported operation in constraint translation: " + oce.getReferredOperation());
        } 
    }
    
    public Expr fromVariable(VariableExp ve, QVTRelation relation) {      
        Variable v = (Variable) ve.getReferredVariable();
        String distinctiveName = relation.getName() + "@" + v.getName();
        
        switch (ve.getReferredVariable().getType().getName()) {
            case "Integer":
                return context.getZ3Ctx().mkConst(distinctiveName, context.getZ3Ctx().mkIntSort());
            case "String":
                return context.getZ3Ctx().mkConst(distinctiveName, context.getZ3Ctx().mkStringSort());
            case "Boolean":
                return context.getZ3Ctx().mkConst(distinctiveName, context.getZ3Ctx().mkBoolSort());
            case "Real":
                return context.getZ3Ctx().mkConst(distinctiveName, context.getZ3Ctx().mkRealSort());
            default:
                throw new UnsupportedOperationException("Unsupported variable in constraint translation: " + ve.getReferredVariable().getType().getName());
        }
    }
    
    public Sort fromEcoreTypedElement(ETypedElement element) {
        switch (element.getEType().getName()) {
            case "EInt":
                return context.getZ3Ctx().mkIntSort();
            case "EString":
                return context.getZ3Ctx().mkStringSort();
            case "EBoolean":
                return context.getZ3Ctx().mkBoolSort();
            default:
                throw new UnsupportedOperationException("Unsupported sort in constraint translation: " + element.getEType());
        }
    }
}

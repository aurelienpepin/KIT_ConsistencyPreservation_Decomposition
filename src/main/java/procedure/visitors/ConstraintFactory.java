package procedure.visitors;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.RealExpr;
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
        Context c = context.getZ3Ctx();
        
        // TODO des opérations qui redéfinissent ces noms ?
        // System.out.println("t: " + oce.getReferredOperation().getType());
        switch (oce.getReferredOperation().getOperationId().getName()) {
            case "=":
                return c.mkEq(operands.get(0), operands.get(1));
            case "<>":
                return c.mkNot(c.mkEq(operands.get(0), operands.get(1)));
            // BASIC ARITHMETIC OPERATIONS
            case "+":
                if ("String".equals(oce.getReferredOperation().getType().toString())) // TODO custom function (for readability)
                    return c.mkConcat(operands.toArray(new SeqExpr[operands.size()]));
                else
                    return c.mkAdd(operands.toArray(new ArithExpr[operands.size()]));
            case "-":
                return c.mkSub(operands.toArray(new ArithExpr[operands.size()]));
            case "*":
                return c.mkMul(operands.toArray(new ArithExpr[operands.size()]));
            case "mod":
                return c.mkMod((IntExpr) operands.get(0), (IntExpr) operands.get(1));
            case "abs":
                ArithExpr operand = (ArithExpr) operands.get(0);
                return c.mkITE(c.mkLt(operand, c.mkInt(0)), c.mkUnaryMinus(operand), operand);
            // ORDERED SET OPERATIONS
            case "max":
                ArithExpr maxOper0 = (ArithExpr) operands.get(0);
                ArithExpr maxOper1 = (ArithExpr) operands.get(1);
                return c.mkITE(c.mkGt(maxOper0, maxOper1), maxOper0, maxOper1);
            case "min":
                ArithExpr minOper0 = (ArithExpr) operands.get(0);
                ArithExpr minOper1 = (ArithExpr) operands.get(1);
                return c.mkITE(c.mkGt(minOper0, minOper1), minOper0, minOper1);
            // FRACTIONALS TO INTEGRALS
            case "floor":
                return c.mkReal2Int((RealExpr) operands.get(0));
            // ORDER RELATIONS
            case "<":
                return c.mkLt((ArithExpr) operands.get(0), (ArithExpr) operands.get(1));
            case "<=":
                return c.mkLe((ArithExpr) operands.get(0), (ArithExpr) operands.get(1));
            case ">":
                return c.mkGt((ArithExpr) operands.get(0), (ArithExpr) operands.get(1));
            case ">=":
                return c.mkGe((ArithExpr) operands.get(0), (ArithExpr) operands.get(1));
            // BOOLEAN FUNCTIONS
            case "not":
                return c.mkNot((BoolExpr) operands.get(0));
            case "and":
                return c.mkAnd(operands.toArray(new BoolExpr[operands.size()]));
            case "or":
                return c.mkOr(operands.toArray(new BoolExpr[operands.size()]));
            case "xor":
                return c.mkXor((BoolExpr) operands.get(0), (BoolExpr) operands.get(1));
            case "implies":
                return c.mkImplies((BoolExpr) operands.get(0), (BoolExpr) operands.get(1));
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

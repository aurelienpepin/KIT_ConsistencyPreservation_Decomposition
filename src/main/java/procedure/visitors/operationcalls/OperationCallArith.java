package procedure.visitors.operationcalls;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.SeqExpr;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.eclipse.ocl.pivot.OperationCallExp;
import procedure.translators.TranslatorContext;
import static procedure.visitors.operationcalls.Translatable.typeEquals;

/**
 * Translate operation calls about arithmetics.
 * @author Aurélien Pepin
 */
public class OperationCallArith implements Translatable {

    /**
     * To add the support of an operation:
     *  - Add the operation name in the array below
     *  - Add a case in OperationCallArith::translate
     */
    private static final String[] NAMES = new String[] {
        "+", "-", "*", "/", "div", "mod", "abs"
    };
    
    private static final Set<String> OPERATIONS = new HashSet<>(Arrays.asList(NAMES));
    
    @Override
    public Expr translate(TranslatorContext ctx, OperationCallExp oce, List<Expr> operands) {
        Context c = ctx.getZ3Ctx();
        
        switch (oce.getReferredOperation().getOperationId().getName()) {
            case "+":
                if (typeEquals(oce, "String"))
                    return c.mkConcat(operands.toArray(new SeqExpr[operands.size()]));
                else
                    return c.mkAdd(operands.toArray(new ArithExpr[operands.size()]));
            case "-":
                return c.mkSub(operands.toArray(new ArithExpr[operands.size()]));
            case "*":
                return c.mkMul(operands.toArray(new ArithExpr[operands.size()]));
            case "/":
            case "div":
                return c.mkDiv((ArithExpr) operands.get(0), (ArithExpr) operands.get(1));
            case "mod":
                return c.mkMod((IntExpr) operands.get(0), (IntExpr) operands.get(1));
            case "abs":
                ArithExpr absOper = (ArithExpr) operands.get(0);
                return c.mkITE(c.mkLt(absOper, c.mkInt(0)), c.mkUnaryMinus(absOper), absOper);
            default: // defensive
                throw new UnsupportedOperationException("Unsupported operation in constraint translation: " + oce.getReferredOperation());
        }
    }

    @Override
    public boolean isResponsibleFor(String operation) {
        return OPERATIONS.contains(operation);
    }
}

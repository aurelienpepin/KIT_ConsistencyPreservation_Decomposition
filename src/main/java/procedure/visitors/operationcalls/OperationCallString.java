package procedure.visitors.operationcalls;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.SeqExpr;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.ocl.pivot.OperationCallExp;
import procedure.translators.TranslatorContext;
import static procedure.visitors.operationcalls.Translatable.typeEquals;

/**
 * Translate operation calls about strings.
 * @author Aurélien Pepin
 */
public class OperationCallString implements Translatable {

    /**
     * To add the support of an operation:
     *  - Add the operation name in the array below
     *  - Add a case in OperationCallArith::translate
     */
    private static final String[] NAMES = new String[] {
        "concat", "substring", "toInteger"
    };
    
    private static final Set<String> OPERATIONS = new HashSet<>(Arrays.asList(NAMES));
    
    @Override
    public Expr translate(TranslatorContext ctx, OperationCallExp oce, List<Expr> operands) {
        Context c = ctx.getZ3Ctx();
        
        switch (oce.getReferredOperation().getOperationId().getName()) {
            case "concat":
                if (typeEquals(oce, "String"))
                    return c.mkConcat(operands.toArray(new SeqExpr[operands.size()]));     
            case "substring":
                if (typeEquals(oce, "String")) {
                    IntExpr substringOper2 = (IntExpr) c.mkSub((IntExpr) operands.get(2), (IntExpr) operands.get(1));
                    substringOper2 = (IntExpr) c.mkAdd(substringOper2, c.mkInt(1));
                    
                    return c.mkExtract((SeqExpr) operands.get(0), (IntExpr) operands.get(1), substringOper2);
                }
            case "toInteger":
                return c.stringToInt(operands.get(0));
            default: // defensive
                throw new UnsupportedOperationException("Unsupported operation in constraint translation: " + oce.getReferredOperation());    
        }
    }

    @Override
    public boolean isResponsibleFor(String operation) {
        return OPERATIONS.contains(operation);
    }
}

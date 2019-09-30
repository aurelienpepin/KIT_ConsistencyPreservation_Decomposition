package procedure.visitors.operationcalls;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.RealExpr;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.ocl.pivot.OperationCallExp;
import procedure.translators.TranslatorContext;

/**
 * Translate operation calls about reals to integers.
 * @author Aurelien
 */
public class OperationCallCast implements Translatable {

    /**
     * To add the support of an operation:
     *  - Add the operation name in the array below
     *  - Add a case in OperationCallArith::translate
     */
    private static final String[] NAMES = new String[] {
        "floor", "round"
    };
    
    private static final Set<String> OPERATIONS = new HashSet<>(Arrays.asList(NAMES));
    @Override
    public Expr translate(TranslatorContext ctx, OperationCallExp oce, List<Expr> operands) {
        Context c = ctx.getZ3Ctx();
        
        switch (oce.getReferredOperation().getOperationId().getName()) {
            case "floor":
                return c.mkReal2Int((RealExpr) operands.get(0));
            case "round":
                return c.mkReal2Int((RealExpr) c.mkAdd(c.mkReal("0.5"), (RealExpr) operands.get(0)));
            default: // defensive
                throw new UnsupportedOperationException("Unsupported operation in constraint translation: " + oce.getReferredOperation());
        }
    }

    @Override
    public boolean isResponsibleFor(String operation) {
        return OPERATIONS.contains(operation);
    }
}

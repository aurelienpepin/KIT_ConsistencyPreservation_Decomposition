package procedure.visitors.operationcalls;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.RealExpr;
import java.util.List;
import org.eclipse.ocl.pivot.OperationCallExp;
import procedure.translators.TranslatorContext;

/**
 *
 * @author Aurelien
 */
public class OperationCallCast implements Translatable {

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
}

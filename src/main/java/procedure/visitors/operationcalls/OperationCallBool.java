package procedure.visitors.operationcalls;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import java.util.List;
import org.eclipse.ocl.pivot.OperationCallExp;
import procedure.translators.TranslatorContext;

/**
 *
 * @author Aurelien
 */
public class OperationCallBool implements Translatable {

    @Override
    public Expr translate(TranslatorContext ctx, OperationCallExp oce, List<Expr> operands) {
        Context c = ctx.getZ3Ctx();
        
        switch (oce.getReferredOperation().getOperationId().getName()) {
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
            default: // defensive
                throw new UnsupportedOperationException("Unsupported operation in constraint translation: " + oce.getReferredOperation());
        }        
    }
}

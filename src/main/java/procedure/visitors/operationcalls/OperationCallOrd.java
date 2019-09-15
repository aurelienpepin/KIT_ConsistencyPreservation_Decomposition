package procedure.visitors.operationcalls;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import java.util.List;
import org.eclipse.ocl.pivot.OperationCallExp;
import procedure.translators.TranslatorContext;

/**
 * Translate operation calls about order relations.
 * @author Aurélien Pepin
 */
public class OperationCallOrd implements Translatable {

    @Override
    public Expr translate(TranslatorContext ctx, OperationCallExp oce, List<Expr> operands) {
        Context c = ctx.getZ3Ctx();
        
        switch (oce.getReferredOperation().getOperationId().getName()) {
            case "<":
                return c.mkLt((ArithExpr) operands.get(0), (ArithExpr) operands.get(1));
            case "<=":
                return c.mkLe((ArithExpr) operands.get(0), (ArithExpr) operands.get(1));
            case ">":
                return c.mkGt((ArithExpr) operands.get(0), (ArithExpr) operands.get(1));
            case ">=":
                return c.mkGe((ArithExpr) operands.get(0), (ArithExpr) operands.get(1));
            default: // defensive
                throw new UnsupportedOperationException("Unsupported operation in constraint translation: " + oce.getReferredOperation());
        }
    }
}

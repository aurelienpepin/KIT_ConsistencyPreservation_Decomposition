package procedure.translators.operationcalls;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import java.util.List;
import org.eclipse.ocl.pivot.OperationCallExp;
import procedure.translators.TranslatorContext;

/**
 *
 * @author Aurelien
 */
public class OperationCallOrdSet implements Translatable {

    @Override
    public Expr translate(TranslatorContext ctx, OperationCallExp oce, List<Expr> operands) {
        Context c = ctx.getZ3Ctx();
        
        switch (oce.getReferredOperation().getOperationId().getName()) {
            case "max":
                ArithExpr maxOper0 = (ArithExpr) operands.get(0);
                ArithExpr maxOper1 = (ArithExpr) operands.get(1);
                return c.mkITE(c.mkGt(maxOper0, maxOper1), maxOper0, maxOper1);
            case "min":
                ArithExpr minOper0 = (ArithExpr) operands.get(0);
                ArithExpr minOper1 = (ArithExpr) operands.get(1);
                return c.mkITE(c.mkGt(minOper0, minOper1), minOper0, minOper1);
            default: // defensive
                throw new UnsupportedOperationException("Unsupported operation in constraint translation: " + oce.getReferredOperation());
        }
    }
}

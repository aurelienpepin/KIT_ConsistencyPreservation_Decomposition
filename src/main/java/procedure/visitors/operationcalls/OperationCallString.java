package procedure.visitors.operationcalls;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.SeqExpr;
import java.util.List;
import org.eclipse.ocl.pivot.OperationCallExp;
import procedure.translators.TranslatorContext;
import static procedure.visitors.operationcalls.Translatable.typeEquals;

/**
 *
 * @author Aurelien
 */
public class OperationCallString implements Translatable {

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
}

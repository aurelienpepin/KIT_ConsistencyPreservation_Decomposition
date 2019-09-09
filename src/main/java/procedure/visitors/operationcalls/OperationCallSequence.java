package procedure.visitors.operationcalls;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.DatatypeExpr;
import com.microsoft.z3.DatatypeSort;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.IntExpr;
import java.util.List;
import org.eclipse.ocl.pivot.OperationCallExp;
import procedure.translators.TranslatorContext;

/**
 *
 * @author Aurelien
 */
public class OperationCallSequence implements Translatable {

    @Override
    public Expr translate(TranslatorContext ctx, OperationCallExp oce, List<Expr> operands) {
        Context c = ctx.getZ3Ctx();
        
        DatatypeExpr seqExpr = (DatatypeExpr) operands.get(0);
        FuncDecl length = ((DatatypeSort) seqExpr.getSort()).getAccessors()[0][0];
        FuncDecl array = ((DatatypeSort) seqExpr.getSort()).getAccessors()[0][1];
        
        switch (oce.getReferredOperation().getOperationId().getName()) {
            case "first": // no verification on length!
                return c.mkSelect((ArrayExpr) c.mkApp(array, seqExpr), c.mkInt(0));
            case "last":  // no verification on length!
                return c.mkSelect((ArrayExpr) c.mkApp(array, seqExpr), c.mkSub((IntExpr) c.mkApp(length, seqExpr), c.mkInt(1)));
            case "at":    // no verification on length!
                return c.mkSelect((ArrayExpr) c.mkApp(array, seqExpr), c.mkSub((IntExpr) c.mkApp(length, seqExpr), (IntExpr) operands.get(1)));
            default: // defensive
                throw new UnsupportedOperationException("Unsupported operation in constraint translation: " + oce.getReferredOperation());   
        }
    }
}

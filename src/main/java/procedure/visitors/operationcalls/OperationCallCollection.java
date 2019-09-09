package procedure.visitors.operationcalls;

import com.microsoft.z3.Context;
import com.microsoft.z3.DatatypeExpr;
import com.microsoft.z3.DatatypeSort;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncDecl;
import java.util.List;
import org.eclipse.ocl.pivot.OperationCallExp;
import procedure.translators.TranslatorContext;
import static procedure.visitors.operationcalls.Translatable.datatypeEquals;

/**
 *
 * @author Aurelien
 */
public class OperationCallCollection implements Translatable {

    @Override
    public Expr translate(TranslatorContext ctx, OperationCallExp oce, List<Expr> operands) {
        Context c = ctx.getZ3Ctx();
        
        switch (oce.getReferredOperation().getOperationId().getName()) {
            case "isEmpty":
                DatatypeExpr dteExpr1 = (DatatypeExpr) operands.get(0);
                FuncDecl length = ((DatatypeSort) dteExpr1.getSort()).getAccessors()[0][0];
                
                if (datatypeEquals(dteExpr1, "Sequence")) {
                    return c.mkEq(c.mkInt(0), c.mkApp(length, dteExpr1));
                } else if (datatypeEquals(dteExpr1, "Sort")) {
                    return c.mkEq(c.mkInt(0), c.mkApp(length, dteExpr1));
                } else {
                    throw new UnsupportedOperationException("Unsupported collection: " + dteExpr1);
                }
            case "notEmpty":
                DatatypeExpr dteExpr2 = (DatatypeExpr) operands.get(0);
                FuncDecl length2 = ((DatatypeSort) dteExpr2.getSort()).getAccessors()[0][0];
                
                if (datatypeEquals(dteExpr2, "Sequence")) {
                    return c.mkNot(c.mkEq(c.mkInt(0), c.mkApp(length2, dteExpr2)));
                } else if (datatypeEquals(dteExpr2, "Sort")) {
                    return c.mkNot(c.mkEq(c.mkInt(0), c.mkApp(length2, dteExpr2)));
                } else {
                    throw new UnsupportedOperationException("Unsupported collection: " + dteExpr2);
                }
            default: // defensive
                throw new UnsupportedOperationException("Unsupported operation in constraint translation: " + oce.getReferredOperation());    
        }
    }
}

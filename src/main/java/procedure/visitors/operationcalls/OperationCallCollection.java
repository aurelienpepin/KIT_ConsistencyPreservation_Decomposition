package procedure.visitors.operationcalls;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.DatatypeExpr;
import com.microsoft.z3.DatatypeSort;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.IntExpr;
import java.util.List;
import org.eclipse.ocl.pivot.OperationCallExp;
import procedure.translators.TranslatorContext;
import static procedure.visitors.operationcalls.Translatable.datatypeEquals;

/**
 * Translate operation calls about collections.
 * @author Aurélien Pepin
 */
public class OperationCallCollection implements Translatable {

    @Override
    public Expr translate(TranslatorContext ctx, OperationCallExp oce, List<Expr> operands) {
        Context c = ctx.getZ3Ctx();
        
        switch (oce.getReferredOperation().getOperationId().getName()) {
            case "isEmpty":
                return isEmpty(ctx, oce, operands);
            case "notEmpty":
                return c.mkNot(isEmpty(ctx, oce, operands));
            case "includes":
                return includes(ctx, oce, operands);
            case "excludes":
                return c.mkNot(includes(ctx, oce, operands));
            default: // defensive
                throw new UnsupportedOperationException("Unsupported operation in constraint translation: " + oce.getReferredOperation());    
        }
    }

    private BoolExpr isEmpty(TranslatorContext ctx, OperationCallExp oce, List<Expr> operands) {
        Context c = ctx.getZ3Ctx();
        
        DatatypeExpr dteExpr = (DatatypeExpr) operands.get(0);
        FuncDecl length = ((DatatypeSort) dteExpr.getSort()).getAccessors()[0][0];

        if (datatypeEquals(dteExpr, "Sequence")) {
            return c.mkEq(c.mkInt(0), c.mkApp(length, dteExpr));
        }
        
        if (datatypeEquals(dteExpr, "Sort")) {
            return c.mkEq(c.mkInt(0), c.mkApp(length, dteExpr));
        }
        
        throw new UnsupportedOperationException("Unsupported collection: " + dteExpr);
    }
    
    private BoolExpr includes(TranslatorContext ctx, OperationCallExp oce, List<Expr> operands) {
        Context c = ctx.getZ3Ctx();
        
        DatatypeExpr dteExpr = (DatatypeExpr) operands.get(0);
        FuncDecl length = ((DatatypeSort) dteExpr.getSort()).getAccessors()[0][0];
        FuncDecl array = ((DatatypeSort) dteExpr.getSort()).getAccessors()[0][1];

        if (datatypeEquals(dteExpr, "Sequence")) {
            IntExpr i = c.mkIntConst("_i"); // TODO: dynamic name to avoid conflicts
            BoolExpr findElem = c.mkEq(operands.get(1), c.mkSelect((ArrayExpr) c.mkApp(array, dteExpr), i));
            BoolExpr search = c.mkImplies(c.mkAnd(c.mkGe(i, c.mkInt(0)), c.mkLt(i, (ArithExpr) c.mkApp(length, dteExpr))), findElem);
            
            return c.mkExists(new IntExpr[]{ i }, search, 0, null, null, null, null);
        }
        
        if (datatypeEquals(dteExpr, "Set")) {
            return c.mkSetMembership(operands.get(1), (ArrayExpr) c.mkApp(array, dteExpr));
        }
        
        throw new UnsupportedOperationException("Unsupported collection: " + dteExpr);
    }
}

package procedure.visitors.operationcalls;

import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Context;
import com.microsoft.z3.DatatypeExpr;
import com.microsoft.z3.DatatypeSort;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.IntExpr;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.ocl.pivot.OperationCallExp;
import procedure.translators.TranslatorContext;

/**
 * Translate operation calls about sets (specifically).
 * @author Aurélien Pepin
 */
public class OperationCallSet implements Translatable {
    
    /**
     * To add the support of an operation:
     *  - Add the operation name in the array below
     *  - Add a case in OperationCallArith::translate
     */
    private static final String[] NAMES = new String[] {
        "union", "intersection", "symmetricDifference"
    };
    
    private static final Set<String> OPERATIONS = new HashSet<>(Arrays.asList(NAMES));
    
    @Override
    public Expr translate(TranslatorContext ctx, OperationCallExp oce, List<Expr> operands) {
        Context c = ctx.getZ3Ctx();
        
        switch (oce.getReferredOperation().getOperationId().getName()) {
            case "union":
                return union(ctx, oce, operands);
            case "intersection":
                return intersection(ctx, oce, operands);
            case "symmetricDifference":
                return symmetricDifference(ctx, oce, operands);
            default: // defensive
                throw new UnsupportedOperationException("Unsupported operation in constraint translation: " + oce.getReferredOperation());
        }
    }
    
    private Expr union(TranslatorContext ctx, OperationCallExp oce, List<Expr> operands) {
        Context c = ctx.getZ3Ctx();
        
        // All this in a dedicated function
        DatatypeExpr set1 = (DatatypeExpr) operands.get(0);
        DatatypeExpr set2 = (DatatypeExpr) operands.get(1);
        
        FuncDecl length = ((DatatypeSort) set1.getSort()).getAccessors()[0][0];
        FuncDecl array = ((DatatypeSort) set2.getSort()).getAccessors()[0][1];
        FuncDecl makeSet = ((DatatypeSort) set1.getSort()).getConstructors()[0];
        
        ArrayExpr arraySet1 = (ArrayExpr) c.mkApp(array, set1);
        ArrayExpr arraySet2 = (ArrayExpr) c.mkApp(array, set2);
        
        // TEMP LENGTH: only an upper bound here (+)!
        Expr newLength = c.mkAdd((IntExpr) c.mkApp(length, set1), (IntExpr) c.mkApp(length, set2));
        Expr newSet = c.mkSetUnion(c.mkSetUnion(arraySet1, arraySet2), c.mkSetIntersection(arraySet1, arraySet2));
        return c.mkApp(makeSet, newLength, newSet);
    }
    
    private Expr intersection(TranslatorContext ctx, OperationCallExp oce, List<Expr> operands) {
        Context c = ctx.getZ3Ctx();
        
        // All this in a dedicated function
        DatatypeExpr set1 = (DatatypeExpr) operands.get(0);
        DatatypeExpr set2 = (DatatypeExpr) operands.get(1);
        
        FuncDecl length = ((DatatypeSort) set1.getSort()).getAccessors()[0][0];
        FuncDecl array = ((DatatypeSort) set2.getSort()).getAccessors()[0][1];
        FuncDecl makeSet = ((DatatypeSort) set1.getSort()).getConstructors()[0];
        
        ArrayExpr arraySet1 = (ArrayExpr) c.mkApp(array, set1);
        ArrayExpr arraySet2 = (ArrayExpr) c.mkApp(array, set2);
        IntExpr lengthSet1 = (IntExpr) c.mkApp(length, set1);
        IntExpr lengthSet2 = (IntExpr) c.mkApp(length, set1);
        
        // TEMP LENGTH: only an upper bound here (max)!
        Expr newLength = c.mkITE(c.mkLt(lengthSet1, lengthSet2), lengthSet1, lengthSet2);
        Expr newSet = c.mkSetIntersection(c.mkSetUnion(arraySet1, arraySet2), c.mkSetIntersection(arraySet1, arraySet2));
        return c.mkApp(makeSet, newLength, newSet);
    }
    
    private Expr symmetricDifference(TranslatorContext ctx, OperationCallExp oce, List<Expr> operands) {
        Context c = ctx.getZ3Ctx();
        
        // All this in a dedicated function
        DatatypeExpr set1 = (DatatypeExpr) operands.get(0);
        DatatypeExpr set2 = (DatatypeExpr) operands.get(1);
        
        FuncDecl length = ((DatatypeSort) set1.getSort()).getAccessors()[0][0];
        FuncDecl array = ((DatatypeSort) set2.getSort()).getAccessors()[0][1];
        FuncDecl makeSet = ((DatatypeSort) set1.getSort()).getConstructors()[0];
        
        ArrayExpr arraySet1 = (ArrayExpr) c.mkApp(array, set1);
        ArrayExpr arraySet2 = (ArrayExpr) c.mkApp(array, set2);

        Expr newLength = c.mkAdd((IntExpr) c.mkApp(length, set1), (IntExpr) c.mkApp(length, set2));
        Expr newSet = c.mkSetDifference(c.mkSetUnion(arraySet1, arraySet2), c.mkSetIntersection(arraySet1, arraySet2));
        return c.mkApp(makeSet, newLength, newSet);
    }

    @Override
    public boolean isResponsibleFor(String operation) {
        return OPERATIONS.contains(operation);
    }
}

package procedure.visitors.operationcalls;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.DatatypeExpr;
import com.microsoft.z3.DatatypeSort;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.IntExpr;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.ocl.pivot.OperationCallExp;
import procedure.translators.TranslatorContext;

/**
 * Translate operation calls about sequences (specifically).
 * @author Aurélien Pepin
 */
public class OperationCallSequence implements Translatable {

    /**
     * To add the support of an operation:
     *  - Add the operation name in the array below
     *  - Add a case in OperationCallArith::translate
     */
    private static final String[] NAMES = new String[] {
        "first", "last", "at"
    };
    
    private static final Set<String> OPERATIONS = new HashSet<>(Arrays.asList(NAMES));
    
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

    @Override
    public boolean isResponsibleFor(String operation) {
        return OPERATIONS.contains(operation);
    }
}

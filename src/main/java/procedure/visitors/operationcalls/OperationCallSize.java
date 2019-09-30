package procedure.visitors.operationcalls;

import com.microsoft.z3.Context;
import com.microsoft.z3.DatatypeExpr;
import com.microsoft.z3.DatatypeSort;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.SeqExpr;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.ocl.pivot.OperationCallExp;
import procedure.translators.TranslatorContext;
import static procedure.visitors.operationcalls.Translatable.datatypeEquals;
import static procedure.visitors.operationcalls.Translatable.typeEquals;

/**
 * Translate operation calls about size of data structures and strings.
 * The `size` operation call is handled in a different class because it is
 * both a function of String and Collection in OCL.
 * 
 * @author Aurélien Pepin
 */
public class OperationCallSize implements Translatable {

    /**
     * To add the support of an operation:
     *  - Add the operation name in the array below
     *  - Add a case in OperationCallArith::translate
     */
    private static final String[] NAMES = new String[] {
        "size"
    };
    
    private static final Set<String> OPERATIONS = new HashSet<>(Arrays.asList(NAMES));
    
    
    @Override
    public Expr translate(TranslatorContext ctx, OperationCallExp oce, List<Expr> operands) {
        Context c = ctx.getZ3Ctx();
        
        switch (oce.getReferredOperation().getOperationId().getName()) {
            case "size":
                if (typeEquals(oce, "String"))
                    return c.mkLength((SeqExpr) operands.get(0));
                
                DatatypeExpr dteExpr = (DatatypeExpr) operands.get(0);
                FuncDecl length = ((DatatypeSort) dteExpr.getSort()).getAccessors()[0][0];
                
                if (datatypeEquals(dteExpr, "Sequence")) {
                    return (IntExpr) c.mkApp(length, dteExpr);
                }
                
                if (datatypeEquals(dteExpr, "Set")) {
                    return (IntExpr) c.mkApp(length, dteExpr);
                }
                
                throw new UnsupportedOperationException("Unsupported collection: " + dteExpr);
            default: // defensive
                throw new UnsupportedOperationException("Unsupported operation in constraint translation: " + oce.getReferredOperation());   
        }
    }

    @Override
    public boolean isResponsibleFor(String operation) {
        return OPERATIONS.contains(operation);
    }
}

package procedure.visitors.operationcalls;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.ocl.pivot.OperationCallExp;
import procedure.translators.TranslatorContext;

/**
 * Translate operation calls about extrema.
 * @author Aurélien Pepin
 */
public class OperationCallOrdSet implements Translatable {

    /**
     * To add the support of an operation:
     *  - Add the operation name in the array below
     *  - Add a case in OperationCallArith::translate
     */
    private static final String[] NAMES = new String[] {
        "max", "min"
    };
    
    private static final Set<String> OPERATIONS = new HashSet<>(Arrays.asList(NAMES));
    
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

    @Override
    public boolean isResponsibleFor(String operation) {
        return OPERATIONS.contains(operation);
    }
}

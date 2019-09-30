package procedure.visitors.operationcalls;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.ocl.pivot.OperationCallExp;
import procedure.translators.TranslatorContext;

/**
 * Translate operation calls about equality.
 * @author Aurélien Pepin
 */
public class OperationCallEq implements Translatable {

    /**
     * To add the support of an operation:
     *  - Add the operation name in the array below
     *  - Add a case in OperationCallArith::translate
     */
    private static final String[] NAMES = new String[] {
        "=", "<>"
    };
    
    private static final Set<String> OPERATIONS = new HashSet<>(Arrays.asList(NAMES));
    @Override
    public Expr translate(TranslatorContext ctx, OperationCallExp oce, List<Expr> operands) {
        Context c = ctx.getZ3Ctx();
        
        switch (oce.getReferredOperation().getOperationId().getName()) {
            case "=":
                return c.mkEq(operands.get(0), operands.get(1));
            case "<>":
                return c.mkNot(c.mkEq(operands.get(0), operands.get(1)));
            default: // defensive
                throw new UnsupportedOperationException("Unsupported operation in constraint translation: " + oce.getReferredOperation());
        }
    }

    @Override
    public boolean isResponsibleFor(String operation) {
        return OPERATIONS.contains(operation);
    }
}

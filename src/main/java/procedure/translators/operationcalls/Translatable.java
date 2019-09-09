package procedure.translators.operationcalls;

import com.microsoft.z3.DatatypeExpr;
import com.microsoft.z3.Expr;
import java.util.List;
import org.eclipse.ocl.pivot.OperationCallExp;
import procedure.translators.TranslatorContext;

/**
 *
 * @author Aurelien
 */
public interface Translatable {
    
    /**
     * Translate an OCL OperationCallExp into a Z3 Expr
     * @param ctx   TranslatorContext
     * @param oce   OCL operation call expression
     * @param operands  List of already translated operands
     * @return 
     */
    public Expr translate(TranslatorContext ctx, OperationCallExp oce, List<Expr> operands);
    
    /**
     * Helper function for short type verification.
     * @param oce   OCL operation call expression
     * @param type  Type to compare
     * @return 
     */
    public static boolean typeEquals(OperationCallExp oce, String type) {
        return type.equals(oce.getReferredOperation().getOwningClass().toString());
    }
    
    public static boolean datatypeEquals(DatatypeExpr dte, String sort) {
        // System.out.println(dte.getSort().toString());
        // Ex: Sequence<Int> equivalent to Sequence
        return dte.getSort().toString().startsWith(sort);
    }
}

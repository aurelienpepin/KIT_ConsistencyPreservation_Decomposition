package procedure.visitors.operationcalls;

import com.microsoft.z3.DatatypeExpr;
import com.microsoft.z3.Expr;
import java.util.List;
import org.eclipse.ocl.pivot.OperationCallExp;
import procedure.translators.TranslatorContext;

/**
 * Common interface for OperationCallX classes.
 * Provides util functions to check type and datatype equality.
 * 
 * @author Aurélien Pepin
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
     * Find the good utility class to call to translate an operation.
     * @param operation     Name of the operation to translate
     * @return              True if the class can translate it, false otherwise
     */
    public boolean isResponsibleFor(String operation);
    
    /**
     * Helper function for short type verification.
     * @param oce   OCL operation call expression
     * @param type  Type to compare
     * @return 
     */
    public static boolean typeEquals(OperationCallExp oce, String type) {
        return type.equals(oce.getReferredOperation().getOwningClass().toString());
    }
    
    /**
     * Helper function for short datatype verification.
     * @param dte
     * @param sort
     * @return 
     */
    public static boolean datatypeEquals(DatatypeExpr dte, String sort) {
        // System.out.println(dte.getSort().toString());
        // Ex: Sequence<Int> equivalent to Sequence
        return dte.getSort().toString().startsWith(sort);
    }
}

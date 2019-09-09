package procedure.visitors;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Constructor;
import com.microsoft.z3.Context;
import com.microsoft.z3.DatatypeExpr;
import com.microsoft.z3.DatatypeSort;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.RealExpr;
import com.microsoft.z3.SeqExpr;
import com.microsoft.z3.Sort;
import java.util.List;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.ocl.pivot.CollectionLiteralExp;
import org.eclipse.ocl.pivot.OperationCallExp;
import org.eclipse.ocl.pivot.Variable;
import org.eclipse.ocl.pivot.VariableExp;
import org.eclipse.ocl.pivot.CollectionLiteralPart;
import org.eclipse.ocl.pivot.Type;
import parsers.qvtr.QVTRelation;
import parsers.qvtr.QVTVariable;
import procedure.translators.TranslatorContext;
import procedure.translators.operationcalls.OperationCallArith;
import procedure.translators.operationcalls.OperationCallBool;
import procedure.translators.operationcalls.OperationCallCast;
import procedure.translators.operationcalls.OperationCallCollection;
import procedure.translators.operationcalls.OperationCallEq;
import procedure.translators.operationcalls.OperationCallOrd;
import procedure.translators.operationcalls.OperationCallOrdSet;
import procedure.translators.operationcalls.OperationCallString;

/**
 * Transform complex OCL expressions into Z3 predicates.
 * @author Aurélien Pepin
 */
public class ConstraintFactory {

    private final TranslatorContext context;
    
    private final CollectionConstraintFactory collFactory;
    
    public ConstraintFactory(TranslatorContext context) {
        this.context = context;
        this.collFactory = new CollectionConstraintFactory(this, context);
    }
     
    public Expr fromOperationCall(OperationCallExp oce, List<Expr> operands) {
        Context c = context.getZ3Ctx();

        switch (oce.getReferredOperation().getOperationId().getName()) {
            case "=":
            case "<>":
                return (new OperationCallEq()).translate(context, oce, operands);
            // Arithmetic operations
            case "+":
            case "-":
            case "*":
            case "/":
            case "div":
            case "mod":
            case "abs":
                return (new OperationCallArith()).translate(context, oce, operands);
            // Ordered set operations
            case "max":
            case "min":
                return (new OperationCallOrdSet()).translate(context, oce, operands);
            // Fractionals to integrals
            case "floor":
            case "round":
                return (new OperationCallCast()).translate(context, oce, operands);
            // Order relations
            case "<":
            case "<=":
            case ">":
            case ">=":
                return (new OperationCallOrd()).translate(context, oce, operands);
            // Boolean functions
            case "not":
            case "and":
            case "or":
            case "xor":
            case "implies":
                return (new OperationCallBool()).translate(context, oce, operands);
            // String-related functions
            case "concat":
            case "size":    
            case "substring":
            case "toInteger":
                return (new OperationCallString()).translate(context, oce, operands);
            // Collection-related functions
            case "isEmpty":
            case "notEmpty":
                return (new OperationCallCollection()).translate(context, oce, operands);
            default:
                throw new UnsupportedOperationException("Unsupported operation in constraint translation: " + oce.getReferredOperation());
        }
    }
    
    public Expr fromCollectionLiteral(CollectionLiteralExp cle) {
        switch (cle.getKind()) {
            case SEQUENCE:
                return collFactory.createSequenceLiteral(cle);
            case SET:
                return collFactory.createSetLiteral(cle);
            case ORDERED_SET:
            case BAG:
            case COLLECTION:
            default: // defensive
                throw new UnsupportedOperationException("Unknown or unsupported collection type");
        }
    }
    
    public Expr fromVariableExp(VariableExp ve, QVTRelation relation) {
        return this.fromVariable((Variable) ve.getReferredVariable(), relation);
    }
    
    public Expr fromVariable(Variable v, QVTRelation relation) {
        String distinctiveName = (new QVTVariable(v, relation)).getFullName();
        
        switch (v.getType().getName()) {
            case "Integer":
                return context.getZ3Ctx().mkConst(distinctiveName, context.getZ3Ctx().mkIntSort());
            case "String":
                return context.getZ3Ctx().mkConst(distinctiveName, context.getZ3Ctx().mkStringSort());
            case "Boolean":
                return context.getZ3Ctx().mkConst(distinctiveName, context.getZ3Ctx().mkBoolSort());
            case "Real":
                return context.getZ3Ctx().mkConst(distinctiveName, context.getZ3Ctx().mkRealSort());
            default:
                throw new UnsupportedOperationException("Unsupported variable in constraint translation: " + v.getType().getName());
        }
    }
    
    public Sort fromEcoreTypedElement(ETypedElement element) {
        switch (element.getEType().getName()) {
            case "EInt":
                return context.getZ3Ctx().mkIntSort();
            case "EString":
                return context.getZ3Ctx().mkStringSort();
            case "EBoolean":
                return context.getZ3Ctx().mkBoolSort();
            default:
                throw new UnsupportedOperationException("Unsupported sort in constraint translation: " + element.getEType());
        }
    }
    
    public Sort fromType(Type type) {
        switch (type.toString()) {
            case "Integer":
                return context.getZ3Ctx().mkIntSort();
            case "String":
                return context.getZ3Ctx().mkStringSort();
            case "Boolean":
                return context.getZ3Ctx().mkBoolSort();
            case "Real":
                return context.getZ3Ctx().mkRealSort();
            case "OclVoid":
                throw new UnsupportedOperationException("OclVoid is not supported: empty collection literal?");
            default:
                throw new UnsupportedOperationException("Unsupported sort in constraint translation: " + type);
        }
    }
    
    public Expr fromValue(Sort sort, CollectionLiteralPart clp) {
        switch (sort.getSortKind()) {
            case Z3_INT_SORT:
                return context.getZ3Ctx().mkInt(clp.toString());
            case Z3_REAL_SORT:
                return context.getZ3Ctx().mkReal(clp.toString());
            case Z3_BOOL_SORT:
                return context.getZ3Ctx().mkBoolConst(clp.toString());
            default:
                throw new UnsupportedOperationException("Unsupported value translation: " + clp + " (" + sort + ")");
        }
    }
    
    /**
     * Helper function for short datatype verification.
     * @param dte
     * @param sort
     * @return 
     */
    private boolean datatypeEquals(DatatypeExpr dte, String sort) {
        // System.out.println(dte.getSort().toString());
        // Ex: Sequence<Int> equivalent to Sequence
        return dte.getSort().toString().startsWith(sort);
    }
}

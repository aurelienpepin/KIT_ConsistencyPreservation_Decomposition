package procedure.visitors;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
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
import procedure.visitors.operationcalls.OperationCallArith;
import procedure.visitors.operationcalls.OperationCallBool;
import procedure.visitors.operationcalls.OperationCallCast;
import procedure.visitors.operationcalls.OperationCallCollection;
import procedure.visitors.operationcalls.OperationCallEq;
import procedure.visitors.operationcalls.OperationCallOrd;
import procedure.visitors.operationcalls.OperationCallOrdSet;
import procedure.visitors.operationcalls.OperationCallSequence;
import procedure.visitors.operationcalls.OperationCallSet;
import procedure.visitors.operationcalls.OperationCallSize;
import procedure.visitors.operationcalls.OperationCallString;

/**
 * Transform complex OCL expressions into Z3 predicates.
 * Called by the ConstraintVisitor.
 * 
 * @author Aur�lien Pepin
 */
public class ConstraintFactory {

    private final TranslatorContext context;
    
    /**
     * A specialized factory to translate declarations of OCL collection literals.
     */
    private final CollectionConstraintFactory collFactory;
    
    
    public ConstraintFactory(TranslatorContext context) {
        this.context = context;
        this.collFactory = new CollectionConstraintFactory(this, context);
    }
     
    /**
     * Translate an OCL operation call (ex.: `5+2`, `a.method()`, etc.).
     * Given the large number of predefined operations in OCL, operations
     * are grouped by theme and translated by specialized
     * classes (OperationCall...) for better readability.
     * 
     * @param oce       Operation to translate
     * @param operands  Operands (the first one is the source)
     * @return          Translated operation
     */
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
            case "substring":
            case "toInteger":
                return (new OperationCallString()).translate(context, oce, operands);
            // Collection-related functions
            case "isEmpty":
            case "notEmpty":
            case "includes":
            case "excludes":
                return (new OperationCallCollection()).translate(context, oce, operands);
            // (Collection + String)-related functions
            case "size":
                return (new OperationCallSize()).translate(context, oce, operands);
            // Sequence-related functions
            case "at":
            case "last":
            case "first":
                return (new OperationCallSequence()).translate(context, oce, operands);
            case "symmetricDifference":
                return (new OperationCallSet()).translate(context, oce, operands);
            default:
                throw new UnsupportedOperationException("Unsupported operation in constraint translation: " + oce.getReferredOperation());
        }
    }
    
    /**
     * Translate an OCL collection literal (ex.: `Sequence{1, 5, 1}`, `Set{2}`).
     * 
     * @param cle   Collection literal to translate
     * @return      Translated collection literal
     */
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
    
    /**
     * Translate an OCL variable expression.
     * A variable expression is a part of the AST that contains a variable.
     * 
     * @param ve        Variable expression to translate
     * @param relation  Relation to avoid name conflicts
     * @return          Translated variable expression
     */
    public Expr fromVariableExp(VariableExp ve, QVTRelation relation) {
        return this.fromVariable((Variable) ve.getReferredVariable(), relation);
    }
    
    /**
     * Translate an OCL variable.
     * 
     * @param v         Variable to translate
     * @param relation  Relation to avoid name conflicts
     * @return          Translated variable
     */
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
    
    /**
     * Translate a primitive type in Ecore into a Z3 sort.
     * 
     * @param element   Element whose type must be translated
     * @return          Translated type (i.e. Z3 sort)
     */
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
    
    /**
     * Translate an OCL type.
     * 
     * @param type  OCL type
     * @return      Z3 sort corresponding to the OCL type
     */
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
    
    /**
     * Translate a literal (ex.: `1`, `"abc"`, `3.14`, etc.)
     * 
     * @param sort  Z3 type of the literal
     * @param clp   Literal
     * @return      Translated literal into a Z3 expression
     */
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
}

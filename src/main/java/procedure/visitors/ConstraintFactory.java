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
import procedure.translators.TranslatorContext;
import procedure.translators.VariableIndexer;

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
        
        // TODO des opérations qui redéfinissent ces noms ?
        // System.out.println("t: " + oce.getReferredOperation().getType());
        // System.out.println(oce.getReferredOperation().getOperationId().getName());
        switch (oce.getReferredOperation().getOperationId().getName()) {
            case "=":
                return c.mkEq(operands.get(0), operands.get(1));
            case "<>":
                return c.mkNot(c.mkEq(operands.get(0), operands.get(1)));
            // BASIC ARITHMETIC OPERATIONS
            case "+":
                if (this.typeEquals(oce, "String"))
                    return c.mkConcat(operands.toArray(new SeqExpr[operands.size()]));
                else
                    return c.mkAdd(operands.toArray(new ArithExpr[operands.size()]));
            case "-":
                return c.mkSub(operands.toArray(new ArithExpr[operands.size()]));
            case "*":
                return c.mkMul(operands.toArray(new ArithExpr[operands.size()]));
            case "/":
            case "div":
                return c.mkDiv((ArithExpr) operands.get(0), (ArithExpr) operands.get(1));
            case "mod":
                return c.mkMod((IntExpr) operands.get(0), (IntExpr) operands.get(1));
            case "abs":
                ArithExpr absOper = (ArithExpr) operands.get(0);
                return c.mkITE(c.mkLt(absOper, c.mkInt(0)), c.mkUnaryMinus(absOper), absOper);
            // ORDERED SET OPERATIONS
            case "max":
                ArithExpr maxOper0 = (ArithExpr) operands.get(0);
                ArithExpr maxOper1 = (ArithExpr) operands.get(1);
                return c.mkITE(c.mkGt(maxOper0, maxOper1), maxOper0, maxOper1);
            case "min":
                ArithExpr minOper0 = (ArithExpr) operands.get(0);
                ArithExpr minOper1 = (ArithExpr) operands.get(1);
                return c.mkITE(c.mkGt(minOper0, minOper1), minOper0, minOper1);
            // FRACTIONALS TO INTEGRALS
            case "floor":
                return c.mkReal2Int((RealExpr) operands.get(0));
            case "round":
                return c.mkReal2Int((RealExpr) c.mkAdd(c.mkReal("0.5"), (RealExpr) operands.get(0)));
            // ORDER RELATIONS
            case "<":
                return c.mkLt((ArithExpr) operands.get(0), (ArithExpr) operands.get(1));
            case "<=":
                return c.mkLe((ArithExpr) operands.get(0), (ArithExpr) operands.get(1));
            case ">":
                return c.mkGt((ArithExpr) operands.get(0), (ArithExpr) operands.get(1));
            case ">=":
                return c.mkGe((ArithExpr) operands.get(0), (ArithExpr) operands.get(1));
            // BOOLEAN FUNCTIONS
            case "not":
                return c.mkNot((BoolExpr) operands.get(0));
            case "and":
                return c.mkAnd(operands.toArray(new BoolExpr[operands.size()]));
            case "or":
                return c.mkOr(operands.toArray(new BoolExpr[operands.size()]));
            case "xor":
                return c.mkXor((BoolExpr) operands.get(0), (BoolExpr) operands.get(1));
            case "implies":
                return c.mkImplies((BoolExpr) operands.get(0), (BoolExpr) operands.get(1));
            // STRING-RELATED FUNCTIONS
            case "concat":
                if (this.typeEquals(oce, "String"))
                    return c.mkConcat(operands.toArray(new SeqExpr[operands.size()]));
            case "size":
                if (this.typeEquals(oce, "String"))
                    return c.mkLength((SeqExpr) operands.get(0));     
            case "substring":
                if (this.typeEquals(oce, "String")) {
                    IntExpr substringOper2 = (IntExpr) c.mkSub((IntExpr) operands.get(2), (IntExpr) operands.get(1));
                    substringOper2 = (IntExpr) c.mkAdd(substringOper2, c.mkInt(1));
                    return c.mkExtract((SeqExpr) operands.get(0), (IntExpr) operands.get(1), substringOper2);
                }
            case "toInteger":
                return c.stringToInt(operands.get(0));
            // COLLECTION-RELATED FUNCTIONS
            case "isEmpty":
                DatatypeExpr dteExpr1 = (DatatypeExpr) operands.get(0);
                FuncDecl length = ((DatatypeSort) dteExpr1.getSort()).getAccessors()[0][0];
                
                if (datatypeEquals(dteExpr1, "Sequence")) {
                    return c.mkEq(c.mkInt(0), c.mkApp(length, dteExpr1));
                } else if (datatypeEquals(dteExpr1, "Sort")) {
                    return c.mkEq(c.mkInt(0), c.mkApp(length, dteExpr1));
                }
            case "notEmpty":
                DatatypeExpr dteExpr2 = (DatatypeExpr) operands.get(0);
                FuncDecl length2 = ((DatatypeSort) dteExpr2.getSort()).getAccessors()[0][0];
                
                return c.mkNot(c.mkEq(c.mkInt(0), c.mkApp(length2, dteExpr2)));
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
                // return null;
            case BAG:
                // return null;
            case COLLECTION:
                // return null;
            default: // defensive
                throw new UnsupportedOperationException("Unknown or unsupported collection type");
        }
        
        // throw new UnsupportedOperationException("Unsupported collection literal translation: " + cle);
    }
    
    public Expr fromVariable(VariableExp ve, QVTRelation relation) {      
        Variable v = (Variable) ve.getReferredVariable();
        // String distinctiveName = relation.getName() + "@" + v.getName();
        String distinctiveName = VariableIndexer.uniqueNameGenerator(relation, v);
        
        switch (ve.getReferredVariable().getType().getName()) {
            case "Integer":
                return context.getZ3Ctx().mkConst(distinctiveName, context.getZ3Ctx().mkIntSort());
            case "String":
                return context.getZ3Ctx().mkConst(distinctiveName, context.getZ3Ctx().mkStringSort());
            case "Boolean":
                return context.getZ3Ctx().mkConst(distinctiveName, context.getZ3Ctx().mkBoolSort());
            case "Real":
                return context.getZ3Ctx().mkConst(distinctiveName, context.getZ3Ctx().mkRealSort());
            default:
                throw new UnsupportedOperationException("Unsupported variable in constraint translation: " + ve.getReferredVariable().getType().getName());
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
     * Helper function for short type verification.
     * @param oce
     * @param type
     * @return 
     */
    private boolean typeEquals(OperationCallExp oce, String type) {
        // System.out.println("inTypeEquals: ");
        // System.out.println("- " + oce.getReferredOperation().getOperationId().getName());
        // System.out.println("- " + oce.getReferredOperation().getOwningClass());
        return type.equals(oce.getReferredOperation().getOwningClass().toString());
    }
    
    /**
     * Helper function for short datatype verification.
     * @param dte
     * @param sort
     * @return 
     */
    private boolean datatypeEquals(DatatypeExpr dte, String sort) {
        return sort.equals(dte.getSort().toString());
    }
}

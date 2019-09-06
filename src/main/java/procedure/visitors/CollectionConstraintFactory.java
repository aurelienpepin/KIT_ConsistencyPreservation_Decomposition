package procedure.visitors;

import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.Constructor;
import com.microsoft.z3.Context;
import com.microsoft.z3.DatatypeSort;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Log;
import com.microsoft.z3.Native;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Sort;
import java.util.Arrays;
import org.eclipse.ocl.pivot.CollectionKind;
import org.eclipse.ocl.pivot.CollectionLiteralExp;
import procedure.translators.TranslatorContext;

/**
 * Transform complex OCL expressions involving collections into Z3 formulas.
 * @author Aurélien Pepin
 */
public class CollectionConstraintFactory {
    
    private final TranslatorContext context;

    private final ConstraintFactory parentFactory;
    
    public CollectionConstraintFactory(ConstraintFactory parentFactory, TranslatorContext context) {
        this.parentFactory = parentFactory;
        this.context = context;
    }
    
    public Constructor constructorFromCollection(CollectionKind kind, Sort sort) {
        Context c = context.getZ3Ctx();
        
        switch (kind) {
            case SEQUENCE:
                System.out.println("here!");
                Constructor seqCons = c.mkConstructor("mkSequence", "isSequence",       // Constructor, recognizer
                        new String[]{"length", "array"},                                // Store an array and its length
                        new Sort[]{c.getIntSort(), c.mkArraySort(c.mkIntSort(), sort)},  // Length: Int, Array: Int x sort
                        new int[]{0, 1});
                // Constructor seqAlt = c.mkConstructor("mk-pair", "is-pair", new String[]{"val"}, new Sort[]{c.mkIntSort()}, new int[]{0});
                // System.out.println("@@ interm. cons: " + );
                return seqCons;
            case SET:
                Constructor setCons = c.mkConstructor("mkSet", "isSet",
                        new String[]{"length", "set"},
                        new Sort[]{c.mkIntSort(), c.mkSetSort(sort)},
                        new int[]{0, 1});
                return setCons;
            default:
                throw new UnsupportedOperationException("Unknown or unsupported OCL collection: " + kind);
        }
    }
    
    public DatatypeSort sortFromCollection(CollectionKind kind, Sort sort) {
        Context c = context.getZ3Ctx();
        
        switch (kind) {
            case SEQUENCE:
                DatatypeSort seqDts = c.mkDatatypeSort("Sequence", new Constructor[] {constructorFromCollection(kind, sort)});
                return seqDts;
            case SET:
                DatatypeSort setDts = c.mkDatatypeSort("Set", new Constructor[] {constructorFromCollection(kind, sort)});
                return setDts;
            default:
                throw new UnsupportedOperationException("Unknown or unsupported OCL collection: " + kind);
        }
    }
    
    public Expr createSequenceLiteral(CollectionLiteralExp cle) {
        Context c = context.getZ3Ctx();
        Sort sort = parentFactory.fromType(cle.getType().flattenedType());
        String symbol = context.getCollIndexer().getSymbol(cle);
        
        DatatypeSort seqSort = sortFromCollection(CollectionKind.SEQUENCE, sort);
        FuncDecl makeSeq = seqSort.getConstructors()[0];
        FuncDecl lengthFunc = seqSort.getAccessors()[0][0];
        FuncDecl arrayFunc = seqSort.getAccessors()[0][1];
        
        //System.out.println("sfc: " + Arrays.toString(seqSort.getAccessors()[0]));
        // System.out.println("sfc: " + seqSort.getAccessors()[0][0].apply(c.mkConst("seqtest", seqSort)));
        
        IntExpr newLength = c.mkInt(cle.getOwnedParts().size());
        ArrayExpr newArray = c.mkArrayConst(symbol, c.getIntSort(), sort);        
        Expr newSeq = c.mkApp(makeSeq, newLength, newArray);
        
        for (int i = 0; i < cle.getOwnedParts().size(); ++i) {
            newArray = c.mkStore(newArray, c.mkInt(i), parentFactory.fromValue(sort, cle.getOwnedParts().get(i)));
            newSeq = c.mkApp(makeSeq, newLength, newArray);
        }
        
        // SOLVING TRIES
        // Solver s = c.mkSolver();
        // s.add(c.mkEq(c.mkInt(1), c.mkApp(seqSort.getAccessors()[0][0], newSeq)));
        // System.out.println(s.check());
        return newSeq;
        
//        Context c = context.getZ3Ctx();
//        Sort sort = parentFactory.fromType(cle.getType().flattenedType());
//        String symbol = context.getCollIndexer().getSymbol(cle);
//        // String lengthSymbol = context.getCollIndexer().getSymbolLength(cle);
//        
//        Constructor cons = c.mkConstructor("mk-sequence", "is-sequence", new String[]{"length", "array"}, new Sort[]{c.mkIntSort(), c.mkArraySort(c.mkIntSort(), sort)}, new int[]{0, 1});
//        DatatypeSort dts = c.mkDatatypeSort("Sequence", new Constructor[]{cons});
//        System.out.println("DTS: " + dts);
//        System.out.println("CONS: " + cons.ConstructorDecl());
//        System.out.println(dts.getSExpr());
//        
//        ArrayExpr arrayDef = c.mkArrayConst(symbol, context.getZ3Ctx().mkIntSort(), sort);
//        for (int i = 0; i < cle.getOwnedParts().size(); ++i) {
//            // System.out.println(cle.getOwnedParts().get(i));
//            // arrayDef = c.mkStore(arrayDef, c.mkInt(i), c.mkInt(Integer.parseInt(cle.getOwnedParts().get(i).toString())));
//            arrayDef = c.mkStore(arrayDef, c.mkInt(i), parentFactory.fromValue(sort, cle.getOwnedParts().get(i)));
//        }
//        
//        System.out.println("ARRAY: " + c.mkStore(arrayDef, c.mkInt(-1), c.mkInt(cle.getOwnedParts().size())));
//        return c.mkStore(arrayDef, c.mkInt(-1), c.mkInt(cle.getOwnedParts().size()));
    }
    
    public Expr createSetLiteral(CollectionLiteralExp cle) {
        Context c = context.getZ3Ctx();
        Sort sort = parentFactory.fromType(cle.getType().flattenedType());
        String symbol = context.getCollIndexer().getSymbol(cle);
        
        ArrayExpr setDef = c.mkEmptySet(sort);
        for (int i = 0; i < cle.getOwnedParts().size(); ++i) {
            setDef = c.mkSetAdd(setDef, parentFactory.fromValue(sort, cle.getOwnedParts().get(i)));
        }
        
        System.out.println("SET: " + setDef);
        return setDef;
    }
}

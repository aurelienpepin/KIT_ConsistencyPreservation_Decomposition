package procedure.visitors;

import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.Constructor;
import com.microsoft.z3.Context;
import com.microsoft.z3.DatatypeSort;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Sort;
import java.util.HashSet;
import org.eclipse.ocl.pivot.CollectionKind;
import org.eclipse.ocl.pivot.CollectionLiteralExp;
import procedure.translators.TranslatorContext;

/**
 * Transform complex OCL expressions involving collections into Z3 formulas.
 * Build datatype sorts in Z3.
 * 
 * A datatype (for collections) is made of:
 * - A constructor  (mkSet, mkSequence, etc.)
 * - Accessors      (length, array)
 * 
 * All OCL data structures are represented using Z3 arrays.
 * 
 * @author Aurélien Pepin
 */
public class CollectionConstraintFactory {
    
    private final TranslatorContext context;

    private final ConstraintFactory parentFactory;
    
    
    public CollectionConstraintFactory(ConstraintFactory parentFactory, TranslatorContext context) {
        this.parentFactory = parentFactory;
        this.context = context;
    }
    
    /**
     * Build the constructor for a given collection kind.
     * 
     * @param kind  Kind of OCL collection (Sequence, Set, etc.)
     * @param sort  Type of data that the data structure will contain
     * @return      Constructor for the data structure of kind `kind`
     */
    public Constructor constructorFromCollection(CollectionKind kind, Sort sort) {
        Context c = context.getZ3Ctx();
        
        switch (kind) {
            case SEQUENCE:
                Constructor seqCons = c.mkConstructor("mkSequence", "isSequence",       // Constructor, recognizer
                        new String[]{"length", "array"},                                // Store an array and its length
                        new Sort[]{c.getIntSort(), c.mkArraySort(c.mkIntSort(), sort)},  // Length: Int, Array: Int x sort
                        new int[]{0, 1});
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
    
    /**
     * Build the datatype sort for a given collection kind.
     * IMPORTANT: sorts are parametrized through their names using chevrons.
     * 
     * @param kind  Kind of OCL collection (Sequence, Set, etc.)
     * @param sort  Type of data that the data structure will contain
     * @return      Datatype sort for the data structure of kind `kind`
     */
    public DatatypeSort sortFromCollection(CollectionKind kind, Sort sort) {
        Context c = context.getZ3Ctx();
        
        switch (kind) {
            case SEQUENCE:
                DatatypeSort seqDts = c.mkDatatypeSort("Sequence<" + sort + ">", new Constructor[] {constructorFromCollection(kind, sort)});
                return seqDts;
            case SET:
                DatatypeSort setDts = c.mkDatatypeSort("Set<" + sort + ">", new Constructor[] {constructorFromCollection(kind, sort)});
                return setDts;
            default:
                throw new UnsupportedOperationException("Unknown or unsupported OCL collection: " + kind);
        }
    }
    
    /**
     * Create the datatype sort for a sequence and fill it will literals.
     * 
     * @param cle   Sequence and its literals
     * @return      The Z3 datatype filled with literals
     */
    public Expr createSequenceLiteral(CollectionLiteralExp cle) {
        Context c = context.getZ3Ctx();
        Sort sort = parentFactory.fromType(cle.getType().flattenedType());
        String symbol = context.getCollectionIndexer().getSymbol(cle);
        
        DatatypeSort seqSort = sortFromCollection(CollectionKind.SEQUENCE, sort);
        FuncDecl makeSeq = seqSort.getConstructors()[0];
        // FuncDecl lengthFunc = seqSort.getAccessors()[0][0];
        // FuncDecl arrayFunc = seqSort.getAccessors()[0][1];
        
        IntExpr newLength = c.mkInt(cle.getOwnedParts().size());
        // ArrayExpr newArray = c.mkConstArray(sort, c.mkCons);                 // REPLACED ARRAYCONST WITH CONSTARRAY (TODO: eval)
        ArrayExpr newArray = c.mkArrayConst(symbol, c.getIntSort(), sort);      // REPLACED ARRAYCONST WITH CONSTARRAY (TODO: eval)
        
        Expr newSeq = c.mkApp(makeSeq, newLength, newArray);
        
        for (int i = 0; i < cle.getOwnedParts().size(); ++i) {
            newArray = c.mkStore(newArray, c.mkInt(i), parentFactory.fromValue(sort, cle.getOwnedParts().get(i)));
            newSeq = c.mkApp(makeSeq, newLength, newArray);
        }
        
        return newSeq;
    }
    
    /**
     * Create the datatype sort for a set and fill it will literals.
     * 
     * @param cle   Set and its literals
     * @return      The Z3 datatype filled with literals
     */
    public Expr createSetLiteral(CollectionLiteralExp cle) {
        Context c = context.getZ3Ctx();
        Sort sort = parentFactory.fromType(cle.getType().flattenedType());
        // String symbol = context.getCollIndexer().getSymbol(cle);
        
        DatatypeSort setSort = sortFromCollection(CollectionKind.SET, sort);
        FuncDecl makeSet = setSort.getConstructors()[0];
        // FuncDecl lengthFunc = setSort.getAccessors()[0][0];
        // FuncDecl arrayFunc = setSort.getAccessors()[0][1];
        
        IntExpr newLength = c.mkInt(new HashSet<>(cle.getOwnedParts()).size());
        ArrayExpr newArray = c.mkEmptySet(sort);
        Expr newSet = c.mkApp(makeSet, newLength, newArray);
        
        for (int i = 0; i < cle.getOwnedParts().size(); ++i) {
            newArray = c.mkSetAdd(newArray, parentFactory.fromValue(sort, cle.getOwnedParts().get(i)));
            newSet = c.mkApp(makeSet, newLength, newArray);
        }
        
        return newSet;
    }
}

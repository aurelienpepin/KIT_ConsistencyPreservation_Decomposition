package procedure.translators;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.ocl.pivot.CollectionLiteralExp;

/**
 *
 * @author Aurélien Pepin
 */
public class OCLCollectionIndexer {
    
    /**
     * A mapping between collection literals in OCL and symbols for Z3.
     */
    private final Map<CollectionLiteralExp, String> symbols;
    
    /**
     * A counter to give distinctive symbols to literals.
     */
    private int counter = 1;
    
    private final String lengthSuffix = "len";

    public OCLCollectionIndexer() {
        this.symbols = new HashMap<>();
    }
    
    /**
     * Returns the name of the symbol that represents the collection literal.
     * Example: `sequence1`
     * @param cle
     * @return 
     */
    public String getSymbol(CollectionLiteralExp cle) {
        if (symbols.containsKey(cle))
            return symbols.get(cle);
        
        String newSymbol = cle.getKind().toString().toLowerCase();
        newSymbol += counter;
        counter++;
        
        symbols.put(cle, newSymbol);
        return newSymbol;
    }
    
    /**
     * Returns the name of the symbol that represents the length of the collection literal.
     * Example: `sequence1length`
     * @param cle
     * @return 
     */
    public String getSymbolLength(CollectionLiteralExp cle) {
        String symbol = getSymbol(cle);
        return symbol + lengthSuffix;
    }
}

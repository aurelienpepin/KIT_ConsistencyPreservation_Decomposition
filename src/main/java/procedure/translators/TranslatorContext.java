package procedure.translators;

/**
 * (Singleton). Provides a unique access to important translation classes.
 * 
 * @author Aurélien Pepin
 */
public class TranslatorContext {
    
    private static final TranslatorContext INSTANCE = new TranslatorContext();
    
    /**
     * The context for Z3 transformations.
     * @param graph 
     */
    private final com.microsoft.z3.Context z3Context;
    
    /**
     * The symbol provider for OCL collection literals.
     */
    private final OCLCollectionIndexer collectionIndexer;
    
    
    private TranslatorContext() {
        this.z3Context = new com.microsoft.z3.Context();
        this.collectionIndexer = new OCLCollectionIndexer();
    }
    
    /**
     * TranslatorContext Singleton.
     * @return 
     */
    public static final TranslatorContext getInstance() {
        return INSTANCE;
    }
    
    public com.microsoft.z3.Context getZ3Ctx() {
        return this.z3Context;
    }

    public OCLCollectionIndexer getCollectionIndexer() {
        return collectionIndexer;
    }
}
package procedure.translators;

import metamodels.Metagraph;

/**
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
    
    private TranslatorContext() {
        this.z3Context = new com.microsoft.z3.Context();
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
}
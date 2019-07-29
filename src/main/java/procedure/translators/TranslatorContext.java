package procedure.translators;

import metamodels.Metagraph;

/**
 *
 * @author Aurélien Pepin
 */
public class TranslatorContext {
    
    /**
     * The graph representing the QVT-R input files.
     */
    private final Metagraph graph;
    
    /**
     * The context for Z3 transformations.
     * @param graph 
     */
    private final com.microsoft.z3.Context z3Context;
    
    public TranslatorContext(Metagraph graph) {
        if (graph == null)
            throw new RuntimeException("The metagraph cannot be null");
        
        this.graph = graph;
        this.z3Context = new com.microsoft.z3.Context();
    }
    
    public com.microsoft.z3.Context getZ3Ctx() {
        return this.z3Context;
    }
}
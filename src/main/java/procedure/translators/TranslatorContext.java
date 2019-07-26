package procedure.translators;

import metamodels.Metagraph;

/**
 *
 * @author Aurélien Pepin
 */
public class TranslatorContext {
    
    private final Metagraph graph;
    
    public TranslatorContext(Metagraph graph) {
        if (graph == null)
            throw new RuntimeException("The metagraph cannot be null");
        
        this.graph = graph;
    }
}

package parsers.qvtr;

import metamodels.MetaGraph;

/**
 * Common interface for all QVT-R components that can be translated into
 * a metagraph.
 * 
 * @author Aurélien Pepin
 */
public interface QVTTranslatable {
    
    public void translate(MetaGraph graph);
}

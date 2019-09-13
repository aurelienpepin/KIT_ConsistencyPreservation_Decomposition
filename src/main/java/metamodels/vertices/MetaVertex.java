package metamodels.vertices;

import metamodels.hypergraphs.HyperVertex;

/**
 * Represents a metavertex, i.e. a vertex that represents a metamodel element
 * in the metagraph (consistency graph).
 * 
 * @author Aurélien Pepin
 */
public abstract class MetaVertex extends HyperVertex {
    
    /**
     * The full name of a metamodel element is the concatenation of its name
     * and the name of all its parents.
     * 
     * The full name is unique; it avoids conflicts when different metamodels
     * have elements with the same name.
     * 
     * @return  A full, unique name
     */
    public abstract String getFullName();
}

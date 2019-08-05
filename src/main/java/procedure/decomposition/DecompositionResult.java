package procedure.decomposition;

import java.util.ArrayList;
import java.util.List;
import metamodels.edges.PredicateEdge;
import metamodels.vertices.Metavertex;
import metamodels.vertices.ecore.ENamedElementVertex;
import org.jgrapht.graph.AsSubgraph;

/**
 *
 * @author Aurélien Pepin
 */
public class DecompositionResult {

    /**
     * Component (subgraph of the metagraph) in which edges are simulated.
     */
    private final AsSubgraph<Metavertex, PredicateEdge> component;

    /**
     * Edges that are simulable through a combination of other remaining edges.
     */
    private final List<PredicateEdge> removedEdges;

    /**
     * Edges that are NOT simulable through a combination of other remaining edges.
     */
    private final List<PredicateEdge> preservedEdges;

    /**
     * 
     * @param component
     * @param removedEdges
     * @param preservedEdges 
     */
    public DecompositionResult(AsSubgraph<Metavertex, PredicateEdge> component, List<PredicateEdge> removedEdges, List<PredicateEdge> preservedEdges) {
        this.component = component;
        this.removedEdges = new ArrayList<>(removedEdges);
        this.preservedEdges = new ArrayList<>(preservedEdges);
    }

    public List<PredicateEdge> getPreservedEdges() {
        return preservedEdges;
    }

    public List<PredicateEdge> getRemovedEdges() {
        return removedEdges;
    }
    
    /**
     * The result is considered positive if at least one edge in the component was removed.
     * 
     * @return 
     */
    public boolean isPositive() {
        return !removedEdges.isEmpty();
    }
}

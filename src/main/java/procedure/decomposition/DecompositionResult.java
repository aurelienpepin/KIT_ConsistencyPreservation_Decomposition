package procedure.decomposition;

import java.util.ArrayList;
import java.util.List;
import metamodels.edges.DualEdge;
import metamodels.edges.MetaEdge;
import metamodels.vertices.MetaVertex;
import org.jgrapht.graph.AsSubgraph;

/**
 *
 * @author Aurélien Pepin
 */
public class DecompositionResult {

    /**
     * Component (subgraph of the dual of the metagraph) in which constraints are simulated.
     */
    private final AsSubgraph<MetaEdge, DualEdge> component;

    /**
     * Constraints that are simulable through a combination of other remaining edges.
     */
    private final List<MetaEdge> removedEdges;

    /**
     * Constraints that are NOT simulable through a combination of other remaining edges.
     */
    // private final List<MetaEdge> preservedEdges;

    /**
     * 
     * @param component
     * @param removedEdges
     */
    public DecompositionResult(AsSubgraph<MetaEdge, DualEdge> component, List<MetaEdge> removedEdges /* , List<MetaEdge> preservedEdges */) {
        this.component = component;
        this.removedEdges = new ArrayList<>(removedEdges);
        // this.preservedEdges = new ArrayList<>(preservedEdges);
    }

    // public List<MetaEdge> getPreservedEdges() {
    //     return preservedEdges;
    // }

    public List<MetaEdge> getRemovedEdges() {
        return removedEdges;
    }
   
    /**
     * The result is considered positive if at least one constraint in the component was removed.
     * 
     * @return 
     */
    public boolean isPositive() {
        return !removedEdges.isEmpty();
    }

    @Override
    public String toString() {
        return "DecompositionResult{" + "component=" + component + ", removedEdges=" + removedEdges + '}';
    }
}

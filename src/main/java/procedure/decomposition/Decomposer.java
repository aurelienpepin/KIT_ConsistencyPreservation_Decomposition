package procedure.decomposition;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import metamodels.Metagraph;
import metamodels.edges.PredicateEdge;
import metamodels.vertices.ENamedElementVertex;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.BiconnectivityInspector;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.interfaces.KShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.YenKShortestPath;
import org.jgrapht.graph.AsSubgraph;
import procedure.translators.TranslatorContext;

/**
 *
 * @author Aurélien Pepin
 */
public class Decomposer {
    
    public static void decompose(Metagraph graph) {
        // Get connected components
        BiconnectivityInspector inspector = new BiconnectivityInspector(graph);
        Set<AsSubgraph<ENamedElementVertex, PredicateEdge>> connectedComponents = inspector.getConnectedComponents();
        // System.out.println("Connected components: " + connectedComponents);
        
        System.out.println("oui j'ai réussi le cast ??");
        System.out.println(connectedComponents);
        
        // For each component, see what you can remove thanks to simulation
        for (AsSubgraph<ENamedElementVertex, PredicateEdge> component : connectedComponents) {
            if (!component.edgeSet().isEmpty()) {
                System.out.println("RES: " + Decomposer.reverseDelete(component));
                // on reconstruit théoriquement ici le QVT-R file
            }
        }
        
//        connectedComponents.forEach((Metagraph component) -> {
//            System.out.println(Decomposer.reverseDelete(graph, component));
//        });
    }
    
    // TODO: Use the strategy pattern to give the choice of the algorithm
    private static List<PredicateEdge> reverseDelete(AsSubgraph<ENamedElementVertex, PredicateEdge> component) {
        List<PredicateEdge> edges = new ArrayList<>(component.edgeSet());
        int i = 0;
        
        while (i < edges.size()) {           
            PredicateEdge edge = edges.get(i);
            ENamedElementVertex s = component.getEdgeSource(edge);
            ENamedElementVertex t = component.getEdgeTarget(edge);
            
            // Delete the edge
            component.removeEdge(edge);
            edges.set(i, null);
            
            ConnectivityInspector inspector = new ConnectivityInspector(component);
            if (!inspector.isConnected() || !simulableThroughCombination(component, edge, s, t)) {	// Cancel deletion
                edges.set(i, edge);
                component.addEdge(s, t, edge);
            }

            i++;
        }
        
        return edges;
    }
    
    private static boolean simulableThroughCombination(AsSubgraph<ENamedElementVertex, PredicateEdge> graph, PredicateEdge edge, ENamedElementVertex source, ENamedElementVertex target) {
        KShortestPathAlgorithm algo = new YenKShortestPath(graph);
        // System.out.println("ARB: " + edge + " " + algo.getPaths(source, target, 100));	// arbitrary, for experimentation

        List<GraphPath> paths = algo.getPaths(source, target, Integer.MAX_VALUE);
        if (paths.stream().anyMatch((path) -> (pathToHornClause(path, edge)))) {
            return true;
        }

        return false;
    }
    
    private static boolean pathToHornClause(GraphPath<ENamedElementVertex, PredicateEdge> path, PredicateEdge edge) {
        Context ctx = TranslatorContext.getInstance().getZ3Ctx();
        Solver s = ctx.mkSolver();

        path.getEdgeList().forEach((pathEdge) -> {
            s.add(pathEdge.getPredicate());
        });

        s.add(ctx.mkNot(edge.getPredicate()));
        // System.out.println("assertions: " + Arrays.toString(s.getAssertions()));

        return Status.UNSATISFIABLE.equals(s.check());
    }
}

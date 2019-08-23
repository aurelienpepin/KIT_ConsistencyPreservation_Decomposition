package procedure.decomposition;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import edu.emory.mathcs.backport.java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import metamodels.DualGraph;
import metamodels.MetaGraph;
import metamodels.edges.DualEdge;
import metamodels.edges.MetaEdge;
import metamodels.vertices.MetaVertex;
import metamodels.vertices.ecore.ENamedElementVertex;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.connectivity.BiconnectivityInspector;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.alg.cycle.PatonCycleBase;
import org.jgrapht.alg.interfaces.KShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.BFSShortestPath;
import org.jgrapht.alg.shortestpath.YenKShortestPath;
import org.jgrapht.graph.AsSubgraph;
import procedure.translators.TranslatorContext;

/**
 *
 * @author Aurélien Pepin
 */
public class Decomposer {

    public static List<DecompositionResult> decompose(MetaGraph graph) {
        List<DecompositionResult> results = new ArrayList<>();
        
        // Get the dual graph
        DualGraph dual = graph.toDual();
        
        // Get connected components
        BiconnectivityInspector inspector = new BiconnectivityInspector(dual);
        Set<AsSubgraph<MetaEdge, DualEdge>> connectedComponents = inspector.getConnectedComponents();
        
        // For each component, see what you can remove thanks to simulation
        for (AsSubgraph<MetaEdge, DualEdge> component : connectedComponents) {
            DecompositionResult result = checkCycles2(component);
        }
        
        return null;
        // throw new UnsupportedOperationException("Not finished yet.");
    }
    
    public static DecompositionResult checkCycles2(AsSubgraph<MetaEdge, DualEdge> component) {
        List<MetaEdge> dualVertices = new ArrayList<>(component.vertexSet());
        List<MetaEdge> removedDualVertices = new ArrayList<>();
        
        Set<GraphPath<MetaEdge, DualEdge>> simpleCycles;
        PatonCycleBase paton;
        int i = 0;
        
        while (i < dualVertices.size()) {
            MetaEdge constraint = dualVertices.get(i);
            paton = new PatonCycleBase(component);
            simpleCycles = paton.getCycleBasis().getCyclesAsGraphPaths();
            
            for (GraphPath p : simpleCycles) {
                if (!p.getVertexList().contains(constraint))
                    continue;
                
                if (pathToHornClause(p, constraint)) {
                    component.removeVertex(constraint);
                    removedDualVertices.add(constraint);
                    System.out.println("REPLACED: " + constraint);
                    break;
                }
            }
            
            // Shorten the reduction procedure if there are no more cycles
            if (paton.getCycleBasis().getCycles().isEmpty()) {
                break;
            }
            
            i++;
        }
        
        return null;
    }
    
    public static DecompositionResult checkCycles(AsSubgraph<MetaEdge, DualEdge> component) {
        // CycleDetector detector = new CycleDetector(component); // Only for directed graphs ATM
        PatonCycleBase paton = new PatonCycleBase(component);
        Set<GraphPath<MetaEdge, DualEdge>> simpleCycles = paton.getCycleBasis().getCyclesAsGraphPaths();
        
        List<MetaEdge> dualVertices = new ArrayList<>(component.vertexSet());
        int i = 0;
        
        for (GraphPath p : simpleCycles) {
            if (p.getVertexList().contains(dualVertices.get(i))) {
                System.out.println("D: " + dualVertices.get(i));
                System.out.println("P: " + p);
                // System.out.println("R: " + pathToHornClause(p, dualVertices.get(i)));

                if (pathToHornClause(p, dualVertices.get(i))) {
                    System.out.println("Replaceable constraint:" + dualVertices.get(i));
                }
            }
        }
        
        return null;
        // throw new UnsupportedOperationException("Not finished yet.");
    }
    
    /*
    public static List<DecompositionResult> decompose(Metagraph graph) {
        List<DecompositionResult> results = new ArrayList<>();
        
        // Get connected components
        BiconnectivityInspector inspector = new BiconnectivityInspector(graph);
        Set<AsSubgraph<Metavertex, PredicateEdge>> connectedComponents = inspector.getConnectedComponents();
        
        // For each component, see what you can remove thanks to simulation
        for (AsSubgraph<Metavertex, PredicateEdge> component : connectedComponents) {
            if (!component.edgeSet().isEmpty()) {
                DecompositionResult result = Decomposer.reverseDelete(component);
                List<PredicateEdge> edges = result.getRemovedEdges();
                
                results.add(result);
                System.out.println("Result: " + edges);
            }
        }
        
        return results;
    }
    */
    
    /*
    // TODO: Use the strategy pattern to give the choice of the algorithm
    private static DecompositionResult reverseDelete(AsSubgraph<MetaVertex, PredicateEdge> component) {
        List<PredicateEdge> edges = new ArrayList<>(component.edgeSet());
        List<PredicateEdge> removedEdges = new ArrayList<>();
        int i = 0;
        
        while (i < edges.size()) {           
            PredicateEdge edge = edges.get(i);
            MetaVertex s = component.getEdgeSource(edge);
            MetaVertex t = component.getEdgeTarget(edge);
            
            // Delete the edge
            component.removeEdge(edge);
            edges.set(i, null);
            
            ConnectivityInspector inspector = new ConnectivityInspector(component);
            if (edge.isLocked() || !inspector.isConnected() || !simulableThroughCombination(component, edge, s, t)) {	// Cancel deletion
                edges.set(i, edge);
                component.addEdge(s, t, edge);
            } else {
                removedEdges.add(edge);
            }

            i++;
        }
        
        edges.removeIf(e -> Objects.isNull(e));
        return new DecompositionResult(component, removedEdges, edges);
    }
    
    private static boolean simulableThroughCombination(AsSubgraph<MetaVertex, PredicateEdge> graph, PredicateEdge edge, MetaVertex source, MetaVertex target) {
        KShortestPathAlgorithm algo = new YenKShortestPath(graph);
        // System.out.println("ARB: " + edge + " " + algo.getPaths(source, target, 100));	// arbitrary, for experimentation

        List<GraphPath> paths = algo.getPaths(source, target, Integer.MAX_VALUE);
        if (paths.stream().anyMatch((path) -> (pathToHornClause(path, edge)))) {
            return true;
        }

        return false;
    }
    */
    private static boolean pathToHornClause(GraphPath<MetaEdge, DualEdge> path, MetaEdge constraint) {
        Context ctx = TranslatorContext.getInstance().getZ3Ctx();
        Solver s = ctx.mkSolver();

        path.getVertexList().forEach((pathEdge) -> {
            s.add((BoolExpr) pathEdge.getPredicate());
        });

        s.add(ctx.mkNot((BoolExpr) constraint.getPredicate()));
        // System.out.println("assertions: " + Arrays.toString(s.getAssertions()));

        return Status.UNSATISFIABLE.equals(s.check());
    }
}
package main;

import java.util.HashMap;
import java.util.Map;
import metamodels.Metagraph;
import parsers.TransformationParser;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import procedure.decomposition.Decomposer;

public class Main {
    
    public static void main(String[] args) {
        System.out.println(">> DECOMPOSITION PROCEDURE <<");

        // 1. CREATE GRAPH
        String qvtr = "C:\\Users\\Aurelien\\Documents\\KIT\\Masterarbeit\\archive\\examples_qvtr\\simple\\simple.qvtr";
        Metagraph graph = TransformationParser.generateGraphFrom(qvtr);
        
        // Map<String, String> cfg = new HashMap<>();
        // cfg.put("model", "true");
        
        // Context context = new Context(cfg);
        // Solver s = context.mkSolver();
        
        // 2. DECOMPOSITION PROCEDURE
        Decomposer.decompose(graph);
        
        System.out.println(graph);
    }
}

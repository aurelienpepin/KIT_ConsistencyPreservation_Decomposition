package main;

import metamodels.Metagraph;
import parsers.TransformationParser;

import procedure.decomposition.Decomposer;

public class Main {
    
    public static void main(String[] args) {
        System.out.println(">> DECOMPOSITION PROCEDURE <<");

        // 1. CREATE GRAPH
        String qvtr = "C:\\Users\\Aurelien\\Documents\\KIT\\Masterarbeit\\archive\\examples_qvtr\\simple\\simple.qvtr";
        // String qvtr = "C:\\Logiciels\\eclipse_workspace\\decomposition\\src\\test\\resources\\specs\\simpleConcat\\simpleConcat.qvtr";
        // String qvtr = "C:\\Logiciels\\eclipse_workspace\\decomposition\\src\\test\\resources\\specs\\uml2rdbms_simple\\uml2rdbms_simple.qvtr";
        Metagraph graph = TransformationParser.generateGraphFrom(qvtr);
        
        // 2. PERFORM DECOMPOSITION
        Decomposer.decompose(graph);
        
        System.out.println(graph);
    }
}

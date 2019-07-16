package main;

import metamodels.vertices.ENamedElementVertex;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import parsers.MetamodelParser;

public class Main {
    
    public static void main(String[] args) {
        System.out.println(">> DECOMPOSITION PROCEDURE <<");
        
        String ecoreFile = "C:\\Users\\Aurelien\\Documents\\KIT\\Masterarbeit\\archive\\examples_to_ecore\\ecore\\student.ecore";
        Graph<ENamedElementVertex, DefaultEdge> g = MetamodelParser.generateGraphFrom(ecoreFile);
        
        System.out.println(g);
    }
}

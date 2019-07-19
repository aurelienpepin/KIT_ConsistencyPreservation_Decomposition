package main;

import metamodels.Metagraph;
import parsers.MetamodelParser;
import parsers.TransformationParser;

public class Main {
    
    public static void main(String[] args) {
        System.out.println(">> DECOMPOSITION PROCEDURE <<");
        
        String ecoreFile = "C:\\Users\\Aurelien\\Documents\\KIT\\Masterarbeit\\archive\\examples_to_ecore\\ecore\\student.ecore";
        String qvtrFile = "C:\\Users\\Aurelien\\Documents\\KIT\\Masterarbeit\\archive\\examples_qvtr\\some2some\\some2some.qvtr";
        
        Metagraph g = MetamodelParser.generateGraphFrom(ecoreFile);
        TransformationParser.fillGraphWith(g, qvtrFile);
        
        System.out.println(g);
    }
}

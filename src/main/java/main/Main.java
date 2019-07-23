package main;

import metamodels.Metagraph;
import parsers.MetamodelParser;
import parsers.TransformationParser;

public class Main {
    
    public static void main(String[] args) {
        System.out.println(">> DECOMPOSITION PROCEDURE <<");
        
        // String diplom = "C:\\Users\\Aurelien\\Documents\\KIT\\Masterarbeit\\archive\\examples_qvtr\\studenttobachelor\\diplom.ecore";
        // String bologna = "C:\\Users\\Aurelien\\Documents\\KIT\\Masterarbeit\\archive\\examples_qvtr\\studenttobachelor\\bologna.ecore";
        // String qvtrFile = "C:\\Users\\Aurelien\\Documents\\KIT\\Masterarbeit\\archive\\examples_qvtr\\studenttobachelor\\studenttobachelor.qvtr";
        
        String person = "C:\\Users\\Aurelien\\Documents\\KIT\\Masterarbeit\\archive\\examples_qvtr\\simple\\person.ecore";
        String resident = "C:\\Users\\Aurelien\\Documents\\KIT\\Masterarbeit\\archive\\examples_qvtr\\simple\\resident.ecore";
        String employee = "C:\\Users\\Aurelien\\Documents\\KIT\\Masterarbeit\\archive\\examples_qvtr\\simple\\employee.ecore";
        String qvtrFile = "C:\\Users\\Aurelien\\Documents\\KIT\\Masterarbeit\\archive\\examples_qvtr\\simple\\simple.qvtr";
        
        Metagraph g = MetamodelParser.generateGraphFrom(person, resident, employee);
        TransformationParser.fillGraphWith(g, qvtrFile);
        
        System.out.println(g);
    }
}

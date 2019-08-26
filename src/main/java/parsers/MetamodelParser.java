package parsers;

import metamodels.MetaGraph;
import metamodels.hypergraphs.HyperGraph;
import metamodels.vertices.ecore.EAttributeVertex;
import metamodels.vertices.ecore.ENamedElementVertex;
import metamodels.vertices.ecore.EPackageVertex;
import metamodels.vertices.ecore.EReferenceVertex;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.jgrapht.Graphs;

public class MetamodelParser {
    
    /**
     * Generate a graph from a metamodel
     * @param ecoreFilePath File path to the Ecore metamodel
     * @return A graph filled with elements of the metamodel
     */
    private static MetaGraph generateGraphFrom(String ecoreFilePath) {
        MetaGraph graph = new MetaGraph();
        
        ResourceSet resourceSet = new ResourceSetImpl();
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
        URI ecoreURI = URI.createFileURI(ecoreFilePath);
        
        Resource metamodelResource = resourceSet.getResource(ecoreURI, true);        
        MetamodelParser.parseFromRoot(metamodelResource, graph);
        return graph;
    }
    
    /**
     * Generate a graph from an arbitrary number of metamodels
     * @param ecoreFilePaths List of file paths to Ecore metamodels
     * @return A graph filled with elements of the metamodel
     */
    public static MetaGraph generateGraphFrom(String... ecoreFilePaths) {
        MetaGraph graph = new MetaGraph();
        
        for (String ecoreFilePath : ecoreFilePaths) {
            HyperGraph.addHyperGraph(graph, generateGraphFrom(ecoreFilePath));
        }
        
        return graph;
    }
    
    /**
     * Generate graph from the root EPackage of an Ecore metamodel.
     * @param rootPackage
     * @return 
     */
    private static MetaGraph generateGraphFrom(EPackage rootPackage) {
        if (rootPackage == null)
            throw new RuntimeException("Root package cannot be null");
        
        MetaGraph graph = new MetaGraph();
        graph.addVertex(new EPackageVertex(rootPackage));
        
        MetamodelParser.parseFromPackage(rootPackage, graph);
        return graph;
    }
    
    /**
     * Generate a graph from an arbitrary number of root packages.
     * @param rootPackages
     * @return 
     */
    public static MetaGraph generateGraphFrom(EPackage... rootPackages) {
        MetaGraph graph = new MetaGraph();
        
        for (EPackage rootPackage : rootPackages) {
            HyperGraph.addHyperGraph(graph, generateGraphFrom(rootPackage));
        }
        
        return graph;
    }
    
    /**
     * 
     * @param resource
     * @param graph 
     */
    private static void parseFromRoot(Resource resource, MetaGraph graph) {
        if (!beginsWithEPackage(resource))
            throw new RuntimeException("The following metamodel does not begin with an EPackage: " + resource.getURI());
        
        EPackage rootPackage = (EPackage) resource.getContents().get(0);
        graph.addVertex(new EPackageVertex(rootPackage));
        
        MetamodelParser.parseFromPackage(rootPackage, graph);
    }
    
    private static void parseFromPackage(EPackage ePackage, MetaGraph graph) {
        for (EPackage subPackage : ePackage.getESubpackages()) {
            graph.addVertex(new EPackageVertex(subPackage));
            MetamodelParser.parseFromPackage(subPackage, graph);
        }
        
        for (EClassifier classifier : ePackage.getEClassifiers()) {
            graph.addVertex(MetamodelVertexFactory.getClassifier(classifier));
            MetamodelParser.parseFromClassifier(classifier, graph);
        }
    }
    
    private static void parseFromClassifier(EClassifier eClassifier, MetaGraph graph) {
        switch (eClassifier.eClass().getName()) {
            case "EClass":
                MetamodelParser.parseFromClass((EClass) eClassifier, graph);
                return;
            default:
                throw new RuntimeException("Unknown classifier type: " + eClassifier.eClass().getName());
        }
    }
    
    private static void parseFromClass(EClass eClass, MetaGraph graph) {
        for (EAttribute attribute : eClass.getEAttributes()) {
            graph.addVertex(new EAttributeVertex(attribute));
        }
        
        for (EReference reference : eClass.getEReferences()) {
            graph.addVertex(new EReferenceVertex(reference));
        }
    }
    
    // TODO: Elements still missing:
    //      - EOperation, EParameter
    //      - EDataType, EEnum, EEnumLiteral
    
    /**
     * 
     * @param resource
     * @return 
     */
    private static boolean beginsWithEPackage(Resource resource) {
        if (resource.getContents().isEmpty())
            return false;
        
        return "EPackage".equals(resource.getContents().get(0).eClass().getName());
    }
}



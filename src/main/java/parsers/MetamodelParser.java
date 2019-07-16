package parsers;

import metamodels.vertices.EAttributeVertex;
import metamodels.vertices.ENamedElementVertex;
import metamodels.vertices.EPackageVertex;
import metamodels.vertices.EReferenceVertex;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.impl.EcorePackageImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;

public class MetamodelParser {
    
    /**
     * Generate a graph from a metamodel
     * @param ecoreFilePath File path to the Ecore metamodel
     * @return A graph filled with elements of the metamodel
     */
    public static Graph<ENamedElementVertex, DefaultEdge> generateGraphFrom(String ecoreFilePath) {
        Graph<ENamedElementVertex, DefaultEdge> graph = GraphTypeBuilder.<ENamedElementVertex, DefaultEdge> undirected().buildGraph();
        
        ResourceSet resourceSet = new ResourceSetImpl();
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
        URI ecoreURI = URI.createFileURI(ecoreFilePath);
        
        Resource metamodelResource = resourceSet.getResource(ecoreURI, true);        
        MetamodelParser.parseFromRoot(metamodelResource, graph);
        return graph;
    }
    
    /**
     * 
     * @param resource
     * @param graph 
     */
    private static void parseFromRoot(Resource resource, Graph<ENamedElementVertex, DefaultEdge> graph) {
        if (!beginsWithEPackage(resource))
            throw new RuntimeException("The following metamodel does not begin with an EPackage: " + resource.getURI());
        
        EPackage rootPackage = (EPackage) resource.getContents().get(0);
        graph.addVertex(new EPackageVertex(rootPackage));
        
        MetamodelParser.parseFromPackage(rootPackage, graph);
    }
    
    private static void parseFromPackage(EPackage ePackage, Graph<ENamedElementVertex, DefaultEdge> graph) {
        for (EPackage subPackage : ePackage.getESubpackages()) {
            graph.addVertex(new EPackageVertex(subPackage));
            MetamodelParser.parseFromPackage(subPackage, graph);
        }
        
        for (EClassifier classifier : ePackage.getEClassifiers()) {
            graph.addVertex(MetamodelVertexFactory.getClassifier(classifier));
            MetamodelParser.parseFromClassifier(classifier, graph);
        }
    }
    
    private static void parseFromClassifier(EClassifier eClassifier, Graph<ENamedElementVertex, DefaultEdge> graph) {
        switch (eClassifier.eClass().getName()) {
            case "EClass":
                MetamodelParser.parseFromClass((EClass) eClassifier, graph);
                return;
            default:
                throw new RuntimeException("Unknown classifier type: " + eClassifier.eClass().getName());
        }
    }
    
    private static void parseFromClass(EClass eClass, Graph<ENamedElementVertex, DefaultEdge> graph) {
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



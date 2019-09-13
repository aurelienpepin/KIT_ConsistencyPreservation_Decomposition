package parsers;

import metamodels.vertices.ecore.EClassVertex;
import metamodels.vertices.ecore.EClassifierVertex;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;

/**
 * Find the appropriate vertex to instantiate while parsing metamodels.
 * 
 * @author Aurélien Pepin
 */
public class MetamodelVertexFactory {
    
    /**
     * Find the appropriate vertex to instantiate if the element is a classifier.
     * 
     * @param classifier    The classifier to check
     * @return              The vertex to instantiate
     */
    public static EClassifierVertex getClassifier(EClassifier classifier) {
        switch (classifier.eClass().getName()) {
            case "EClass":
                return new EClassVertex((EClass) classifier);
            case "EDataType": // TODO
                throw new RuntimeException("TODO: implement EDataTypeVertex");
            default:
                throw new RuntimeException("Unknown classifier type: " + classifier.eClass().getName());
        }
    }
}

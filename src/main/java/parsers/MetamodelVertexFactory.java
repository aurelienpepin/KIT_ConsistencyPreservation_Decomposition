package parsers;

import metamodels.vertices.EClassVertex;
import metamodels.vertices.EClassifierVertex;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;

public class MetamodelVertexFactory {
    
    /**
     * 
     * @param classifier
     * @return 
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

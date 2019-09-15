package parsers.qvtr;

import org.eclipse.qvtd.pivot.qvtbase.TypedModel;

/**
 * Represents a model parameter, i.e. a set of typed models that participate in
 * the transformation.
 * 
 * @author Aurélien Pepin
 */
public class QVTModelParam {
    
    /**
     * Corresponding QVT-R typed model element.
     */
    private final TypedModel modelParam;
    
    
    public QVTModelParam(TypedModel modelParam) {
        this.modelParam = modelParam;
        
        // System.out.println(modelParam.getUsedPackage().get(0).getEPackage());
    }
    
    public TypedModel getModel() {
        return modelParam;
    }
}

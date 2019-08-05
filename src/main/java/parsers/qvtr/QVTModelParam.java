package parsers.qvtr;

import org.eclipse.qvtd.pivot.qvtbase.TypedModel;

/**
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

package metamodels.vertices.ecore;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.ENamedElement;

/**
 *
 * @author Aurelien
 */
public abstract class EClassifierVertex extends ENamedElementVertex {

    public EClassifierVertex(EClassifier element) {
        super(element);
    }

    @Override
    public String getFullName() {
        EClassifier classifElem = (EClassifier) element;
        StringBuilder sb = new StringBuilder();
        
        if (classifElem.getEPackage() != null) {
            sb.append(new EPackageVertex(classifElem.getEPackage()).getFullName()).append("::");
        }
        
        sb.append(element.getName());
        return sb.toString();
    }
    
    @Override
    public EClassifier getElement() {
        return (EClassifier) element;
    }
}

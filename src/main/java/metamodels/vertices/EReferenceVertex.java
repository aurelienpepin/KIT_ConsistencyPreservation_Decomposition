package metamodels.vertices;

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EReference;

/**
 *
 * @author Aurelien
 */
public class EReferenceVertex extends ENamedElementVertex {

    public EReferenceVertex(ENamedElement element) {
        super(element);
    }
    
    @Override
    public String getFullName() {
        EReference refElem = (EReference) element;
        StringBuilder sb = new StringBuilder();
        
        if (refElem.getEContainingClass() != null) {
            sb.append(new EClassVertex(refElem.getEContainingClass()).getFullName()).append("::");
        }
        
        sb.append(element.getName());
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Ref{" + this.getFullName() + "}";
    }
    
    @Override
    public EReference getElement() {
        return (EReference) element;
    }
    
}

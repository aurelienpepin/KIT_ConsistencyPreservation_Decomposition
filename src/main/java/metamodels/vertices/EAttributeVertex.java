package metamodels.vertices;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.ENamedElement;

public class EAttributeVertex extends ENamedElementVertex {

    public EAttributeVertex(ENamedElement element) {
        super(element);
    }

    @Override
    public String getFullName() {
        EAttribute attrElem = (EAttribute) element;
        StringBuilder sb = new StringBuilder();
        
        if (attrElem.getEContainingClass() != null) {
            sb.append(new EClassVertex(attrElem.getEContainingClass()).getFullName()).append("::");
        }
        
        sb.append(element.getName());
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return "Attr{" + element.getName() + "}";
    }
    
    @Override
    public EAttribute getElement() {
        return (EAttribute) element;
    }
    
}

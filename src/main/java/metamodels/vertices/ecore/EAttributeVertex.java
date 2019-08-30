package metamodels.vertices.ecore;

import org.eclipse.emf.ecore.EAttribute;

public class EAttributeVertex extends ENamedElementVertex {

    public EAttributeVertex(EAttribute element) {
        super(element);
    }

    @Override
    public String getFullName() {
        EAttribute attrElem = (EAttribute) element;
        // System.out.println("ATTRTYPE: " + attrElem.getEType());
        StringBuilder sb = new StringBuilder();
        
        if (attrElem.getEContainingClass() != null) {
            sb.append(new EClassVertex(attrElem.getEContainingClass()).getFullName()).append("::");
        }
        
        sb.append(element.getName());
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return "Attr{" + this.getFullName() + "}";
    }
    
    @Override
    public EAttribute getElement() {
        return (EAttribute) element;
    }
    
}

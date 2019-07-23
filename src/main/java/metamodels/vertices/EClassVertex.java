package metamodels.vertices;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;

public class EClassVertex extends EClassifierVertex {

    public EClassVertex(ENamedElement element) {
        super(element);
    }

    @Override
    public String toString() {
        // return "Class{" + element.getName() + "}";
        return element.toString();
    }
    
    @Override
    public EClass getElement() {
        return (EClass) element;
    }
}

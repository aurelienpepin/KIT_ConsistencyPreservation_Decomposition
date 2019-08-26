package metamodels.vertices.ecore;

import org.eclipse.emf.ecore.EClass;

public class EClassVertex extends EClassifierVertex {

    public EClassVertex(EClass element) {
        super(element);
    }

    @Override
    public String toString() {
        return "Class{" + this.getFullName() + "}";
    }
    
    @Override
    public EClass getElement() {
        return (EClass) element;
    }
}

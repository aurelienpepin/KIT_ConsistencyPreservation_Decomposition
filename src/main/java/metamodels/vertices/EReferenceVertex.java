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
    public String toString() {
        return "Ref{" + element.getName() + "}";
    }
    
    @Override
    public EReference getElement() {
        return (EReference) element;
    }
    
}

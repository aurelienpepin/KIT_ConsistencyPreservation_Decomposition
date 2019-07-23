package metamodels.vertices;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.ENamedElement;

/**
 *
 * @author Aurelien
 */
public abstract class EClassifierVertex extends ENamedElementVertex {

    public EClassifierVertex(ENamedElement element) {
        super(element);
    }

    @Override
    public EClassifier getElement() {
        return (EClassifier) element;
    }
}

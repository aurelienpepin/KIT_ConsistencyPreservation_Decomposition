package metamodels.vertices;

import metamodels.nodes.SimpleMetavertex;
import org.eclipse.emf.ecore.ENamedElement;

public abstract class ENamedElementVertex extends SimpleMetavertex {

    protected final ENamedElement element;

    public ENamedElementVertex(ENamedElement element) {
        this.element = element;
    }
    
    public abstract String getFullName();
    
    public abstract ENamedElement getElement();
    
    @Override
    public int hashCode() {
        return this.getFullName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final ENamedElementVertex other = (ENamedElementVertex) obj;
        return this.getFullName().equals(other.getFullName());
    }
}
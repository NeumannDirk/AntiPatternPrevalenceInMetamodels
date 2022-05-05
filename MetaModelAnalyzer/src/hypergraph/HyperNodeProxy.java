package hypergraph;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

public class HyperNodeProxy extends HyperNode {
	
	private String proxy;
	
	public HyperNodeProxy(EObject originalModelElement, URI eProxyURI) {
		super(originalModelElement);
		this.proxy = originalModelElement.toString();
		this.name = this.proxy;
	}
	
	@Override
	public boolean isNodeOf(EObject searchedFor) {
		return super.isNodeOf(searchedFor) || this.proxy.equals(searchedFor.toString());
	}

}

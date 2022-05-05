package analyzer;

import java.util.ArrayList;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EAttributeImpl;
import org.eclipse.emf.ecore.impl.ENamedElementImpl;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;

public class Unnamed_noPackage extends IAnalyzer {
	@Override
	public void printSummary() {
		this.ar.printGen("name_noP");
	}
	
	@Override
	public int analyze(TreeIterator<EObject> iterator,ArrayList<EClass> eclasses) {
		int numClasses = eclasses.size(); 
		if (!iterator.hasNext()) {
			this.ar.inc(0);
		} else {
			while (iterator.hasNext()) {
				EObject eo = iterator.next();
				if ((eo instanceof ENamedElementImpl) && !(eo instanceof EPackageImpl)) {
					String name = ((ENamedElementImpl) eo).getName();
					if(name == null || name.equals("")) {
						this.ar.inc(numClasses);
						return -1;						
					}
				}			
			}
			this.ar.inc(-1 * numClasses);
		}
		return -1;
	}
}

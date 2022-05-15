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

public class Unnamed_complete extends IAnalyzer {
	@Override
	public void printSummary() {
		this.ar.printGen("name_all");
	}

	@Override
	public int analyze(TreeIterator<EObject> iterator,ArrayList<EClass> eclasses) {
		int numClasses = eclasses.size();
		int erg = 0;
		if (!iterator.hasNext()) {
			this.ar.inc(0);
		} else {
			while (iterator.hasNext()) {
				EObject eo = iterator.next();
				if (eo instanceof ENamedElementImpl) {
					String name = ((ENamedElementImpl) eo).getName();
					if(name == null || name.equals("")) {
						this.ar.inc(numClasses);
						erg++;						
					}
				}			
			}
			this.ar.inc(-1 * numClasses);
		}
		return erg;
	}
}

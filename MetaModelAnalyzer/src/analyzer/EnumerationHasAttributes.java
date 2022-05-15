package analyzer;

import java.util.ArrayList;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EEnumImpl;
import org.eclipse.emf.ecore.impl.ENamedElementImpl;

public class EnumerationHasAttributes extends IAnalyzer {

	@Override
	public void printSummary() {
		// TODO Auto-generated method stub

	}

	@Override
	public int analyze(TreeIterator<EObject> iterator, ArrayList<EClass> eclasses) {		
		int erg = 0;
		if (!iterator.hasNext()) {
			this.ar.inc(0);
		} else {
			while (iterator.hasNext()) {
				EObject eo = iterator.next();
				if (eo instanceof EEnumImpl) {
					TreeIterator<EObject> iterator2 = eo.eAllContents();
					while (iterator2.hasNext()) {
						EObject enumContent = iterator2.next();
						if(enumContent instanceof EAttribute) {
							erg++;
						}
					}
				}			
			}
//			this.ar.inc(-1 * numClasses);
		}
		return erg;
	}

}

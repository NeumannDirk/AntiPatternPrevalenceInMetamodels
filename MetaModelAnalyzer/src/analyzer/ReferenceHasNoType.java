package analyzer;

import java.util.ArrayList;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;

public class ReferenceHasNoType extends IAnalyzer {

	@Override
	public void printSummary() {
		// TODO Auto-generated method stub

	}

	@Override
	public int analyze(TreeIterator<EObject> iterator, ArrayList<EClass> eclasses) {
		int erg = 0;
		for(EClass eclass : eclasses) {
			for(EReference eref : eclass.getEReferences()) {
				if(eref.getEType() == null) {
					erg++;
				}
			}
			for(EAttribute eatt : eclass.getEAttributes()) {
				if(eatt.getEType() == null) {
					erg++;
				}			
			}
//			for(EClass suTyp : eclass.getESuperTypes()) {
//				if(suTyp() == null) {
//					erg++;
//				}
//			}
//			for(EOperation eop : eclass.getEOperations()) {
//				if(eop.getEType() == null) {
//					erg++;
//				}
//			}
		}
		return erg;
	}

}

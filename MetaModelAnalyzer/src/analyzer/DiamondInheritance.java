package analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class DiamondInheritance extends IAnalyzer {

	@Override
	public void printSummary() {
		// TODO Auto-generated method stub

	}

	@Override
	public int analyze(TreeIterator<EObject> iterator, ArrayList<EClass> eclasses) {
		int erg = 0;
		for (EClass eclass : eclasses) {

			if (eclass.getESuperTypes().size() > 1 && eclass.getEAllSuperTypes().size() > 2 && hasDiamondInheritance(eclass)) {
				erg++;
			}
		}
		return erg;
	}

	private boolean hasDiamondInheritance(EClass eclass) {
		EList<EGenericType> directSuperTypes = eclass.getEGenericSuperTypes();
		EList<EGenericType> allSuperTypes = eclass.getEAllGenericSuperTypes();
		List<EGenericType> allIndirectSuperTypes = allSuperTypes.stream().filter(x -> !directSuperTypes.contains(x)).collect(Collectors.toList());
		
		if(allIndirectSuperTypes.size() > 0) {		
			for (EGenericType directSuperType1 : directSuperTypes) {
				for (EGenericType directSuperType2 : directSuperTypes) {
					if(directSuperType1 != directSuperType2) {
						if(directSuperType1.getEClassifier() instanceof EClass && directSuperType2.getEClassifier() instanceof EClass) {
							EList<EGenericType> indirectSuperTypes1 = ((EClass) directSuperType1.getEClassifier()).getEAllGenericSuperTypes();
							EList<EGenericType> indirectSuperTypes2 = ((EClass) directSuperType2.getEClassifier()).getEAllGenericSuperTypes();
							for (EGenericType indirectSuperType1 : indirectSuperTypes1) {
								for (EGenericType indirectSuperType2 : indirectSuperTypes2) {									
									if(indirectSuperType1.getEClassifier().equals(indirectSuperType2.getEClassifier())) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

}

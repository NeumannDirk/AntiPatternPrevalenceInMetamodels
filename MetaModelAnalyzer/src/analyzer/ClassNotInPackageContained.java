package analyzer;

import java.util.ArrayList;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

public class ClassNotInPackageContained extends IAnalyzer {

	@Override
	public void printSummary() {
		// TODO Auto-generated method stub

	}

	@Override
	public int analyze(TreeIterator<EObject> iterator, ArrayList<EClass> eclasses) {
		int numberOfAntipatterns = 0;
		if (eclasses.size() > 0) {
			for (EClass ec : eclasses) {
				EObject eContainer = ec.eContainer();
				String location = "";
				if (eContainer == null) {
					numberOfAntipatterns++;
				} else if (!(eContainer instanceof EPackage)) {
					System.out.println("Kein EContainer für eine Klasse!");
				}
			}			
		}
		return numberOfAntipatterns;
	}

}

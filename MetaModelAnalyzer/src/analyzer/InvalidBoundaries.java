package analyzer;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EAttributeImpl;

public class InvalidBoundaries extends IAnalyzer {

	@Override
	public void printSummary() {
		this.ar.printGen("att");
	}

	@Override
	public void analyze(ArrayList<EClass> eclasses) {	
		if (eclasses.size() == 0) {
			this.ar.inc(0);
			return;
		}
		for (EClass ec : eclasses) {		
			System.out.println("Class:"+ec.getName());	
//			Interesting part	
			for (EStructuralFeature esf : ec.getEAllStructuralFeatures()) {
				if(esf.getClass() != EAttributeImpl.class) {
					continue;
				}
				int lo = esf.getLowerBound();
				int up = esf.getUpperBound();	
				if(lo > up ){
					this.ar.inc(eclasses.size());
					System.out.println("\t"+esf.getName());
//					return;
				}
			}
		}
		this.ar.inc(-1 * eclasses.size());
	}
}

package analyzer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EAttributeImpl;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

public class MME_complete extends IAnalyzer {
	@Override
	public void printSummary() {
		this.ar.printGen("mme_com");	
	}

	@Override
	public int analyze(TreeIterator<EObject> iterator,ArrayList<EClass> eclasses) {
		if (eclasses.size() == 0) {
			this.ar.inc(0);
			return -1;
		}
		//[*,*] ==> ok
		//[x,*] ==> ok
		//[*,y] ==> nicht ok
		//[x,y] ==> nicht ok wenn x > y
		for (EClass ec : eclasses) {		
			//Interesting part	
			for (EStructuralFeature esf : ec.getEAllStructuralFeatures()) {
				int lo = esf.getLowerBound();
				int up = esf.getUpperBound();
				//[x,y] ==> nicht ok wenn x > y
				if((lo != -1)&&(up != -1)&&(lo > up)) {
					this.ar.inc(eclasses.size());
					return -1;					
				}
				//[*,y] ==> nicht ok
				if((lo == -1)&&(up != -1)){
					this.ar.inc(eclasses.size());
					return -1;
				}
			}
		}
		this.ar.inc(-1 * eclasses.size());
		return -1;
	}
}
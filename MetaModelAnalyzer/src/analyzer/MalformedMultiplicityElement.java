package analyzer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
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

public class MalformedMultiplicityElement extends IAnalyzer {

	private int malformed_multi_element = 0;

	@Override
	public void printSummary() {
		this.ar.printGen("mme2");	
	}

	@Override
	public void analyze(ArrayList<EClass> eclasses) {
		if (eclasses.size() == 0) {
			this.ar.inc(0);
			return;
		}
		for (EClass ec : eclasses) {		
//			System.out.println("Class:"+ec.getName());	
			//Interesting part	
			for (EStructuralFeature esf : ec.getEAllStructuralFeatures()) {
//				if(esf.getClass() == EAttributeImpl.class) {
//					continue;
//				}
				int lo = esf.getLowerBound();
				int up = esf.getUpperBound();	
				if((lo > up )){//||(lo == -1 && up != -1)
					this.ar.inc(eclasses.size());
//					System.out.println("\t"+esf.getName());
					return;
				}
			}
		}
		this.ar.inc(-1 * eclasses.size());
	}
}
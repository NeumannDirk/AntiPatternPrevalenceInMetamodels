package analyzer;

import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

public class MalformedMultiplicityElement implements IAnalyzer {

	private int malformed_multi_element = 0;

	@Override
	public void printSummary() {
		System.out.println("Models with a malformed multiplicity element: " + this.malformed_multi_element);
	}

	@Override
	public void analyze(EList<EObject> models) {
		for (EObject model : models) {
			EList<EObject> eobjects = model.eContents();
			for (EObject eo : eobjects) {
				if (eo.getClass() == EClassImpl.class) {
					EClass eclass = (EClass) eo;					
					System.out.println(eclass.getName());				
					//Interesting part	
					for (EStructuralFeature esf : eclass.getEAllStructuralFeatures()) {
						int lo = esf.getLowerBound();
						int up = esf.getUpperBound();
						String name = esf.getName();
//						if()
						String typeOfRef = esf.getEType().eClass().getName();	
						System.out.println("\t\"" + name + "\" ["+lo+","+up+"] : "  + typeOfRef);
						
						if((lo > up )||(lo == -1 && up != -1)){
							this.malformed_multi_element++;
						}
					}
				}
			}
		}
	}
}

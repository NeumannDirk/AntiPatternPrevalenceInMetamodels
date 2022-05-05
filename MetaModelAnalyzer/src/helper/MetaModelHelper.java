package helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EAnnotationImpl;
import org.eclipse.emf.ecore.impl.EAttributeImpl;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.impl.EDataTypeImpl;
import org.eclipse.emf.ecore.impl.EEnumImpl;
import org.eclipse.emf.ecore.impl.EEnumLiteralImpl;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.impl.EGenericTypeImpl;
import org.eclipse.emf.ecore.impl.EOperationImpl;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.impl.EParameterImpl;
import org.eclipse.emf.ecore.impl.EReferenceImpl;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.impl.ETypeParameterImpl;
import org.eclipse.emf.ecore.resource.Resource;

public class MetaModelHelper {
	public static ArrayList<EObject> getClasses (Resource myMetaModel) {
		ArrayList<EObject> eclasses = new ArrayList<EObject>();
		EList<EObject> emodels = myMetaModel.getContents();
		getAllClasses(emodels, eclasses, null);				
		return eclasses;
	}
	
	private static void getAllClasses(EList<EObject> contents, ArrayList<EObject> eclasses, Integer packageHash) {
		for (EObject content : contents) {
//			List<EClass> clazzes = Arrays.asList(				
////				EClass.class,
////				EEnumImpl.class,
////				EDataTypeImpl.class,
////				EGenericTypeImpl.class,
//				EClassifier.class
//			);
			
			if (content.getClass() == EPackageImpl.class) {
				int ph = content.hashCode();
				if(packageHash == null || ph != packageHash) {
					getAllClasses(((EPackageImpl)content).eContents(), eclasses, ph);					
				}						
			} 
			else if (content.getClass() == EClassImpl.class || content.getClass() == EEnumImpl.class || content.getClass() == EDataTypeImpl.class) {
				eclasses.add(content);
			} 
//			else if (content.getClass() == EEnumImpl.class) { }
//			else if (content.getClass() == EDataTypeImpl.class) { }
			else if (content.getClass() == EAnnotationImpl.class) { }	
//			else if (content.getClass() == EGenericTypeImpl.class) { }	
			else if (content.getClass() == EFactoryImpl.class) { }	
			else if (content.getClass() == ETypeParameterImpl.class) { }	
			else if (content.getClass() == EParameterImpl.class) { }	
			else if (content.getClass() == EAttributeImpl.class) { }	
			else if (content.getClass() == EOperationImpl.class) { }	
			else if (content.getClass() == EEnumLiteralImpl.class) { }	
			else if (content.getClass() == EReferenceImpl.class) { }	
			else if (content.getClass() == EStringToStringMapEntryImpl.class) { }				
			else {
				System.out.println("Unbekannte Klasse: " + content.getClass());
			}
		}
	}
}

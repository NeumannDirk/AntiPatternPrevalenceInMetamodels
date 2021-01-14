package analyzer;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

public class MainAnalyzer {

	public static void main(String[] args) {
		String dir = System.getProperty("user.dir") + "\\model";
		dir = "D:\\data\\ap_mm";
//		System.out.print(dir);
		ArrayList<String> ecoreFiles = new ArrayList<String>();
		if (true) {
			getAllEcores(new File(dir), ecoreFiles);
//			ecoreFiles = (ArrayList<String>) ecoreFiles.subList(0, 1000);
		} else {
			ecoreFiles = new ArrayList<String>();
			ecoreFiles.add("D:\\Repositories\\MyEcore\\model\\myEcore.ecore");
//			ecoreFiles.add("D:\\data\\own_examples\\myEcore2.ecore");
//			ecoreFiles.add("D:\\data\\own_examples\\myEcore3.ecore");
		}
		System.out.println("Number of meta-models: " + ecoreFiles.size());
		new MainAnalyzer().run(ecoreFiles);
	}

	private static void getAllEcores(File curDir, ArrayList<String> list) {
		File[] filesList = curDir.listFiles();
		for (File f : filesList) {
			if (f.isDirectory())
				getAllEcores(f, list);
			if (f.isFile() && f.getName().endsWith(".ecore")) {
				list.add(f.getAbsolutePath());
			}
		}

	}

	private int not_readable_file = 0;
	private int empty_file = 0;
	private ArrayList<IAnalyzer> analyzers = new ArrayList<IAnalyzer>();
	
	int[] lim = new int[] {0,10,20,30,40,50,60,70,80,90,100,200,300,400,500,600,700,800,900,1000};
	int[] noc = new int[lim.length-1];
	
	private void inc(int nc) {
		for(int i = 1; i < lim.length; i++) {
			if(nc < lim[i]) {
				noc[i-1] += 1;
				break;
			}
		}
	}
	
	public MainAnalyzer() {
//		this.analyzers.add(new ClassHasMoreThanOneID());
		this.analyzers.add(new MalformedMultiplicityElement());
//		this.analyzers.add(new InvalidBoundaries());
	}

	private void run(ArrayList<String> ecoreFiles) {

//		ClassHasMoreThanOneID id_analyzer = new ClassHasMoreThanOneID();
//		this.analyzers.add(id_analyzer);
		for (int i = 0; i < ecoreFiles.size(); i++) {
			if(i%10000==0) {
				System.out.println(i);
			}
			String s = ecoreFiles.get(i);
			EList<EObject> emodels = null;
			try {
				ResourceSet resourceSet = new ResourceSetImpl();
				resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
						new EcoreResourceFactoryImpl());
				Resource myMetaModel = resourceSet.getResource(URI.createFileURI(s), true);
				emodels = myMetaModel.getContents();
			} catch (Exception e) {
				this.not_readable_file++;
				continue;
			}
			if (emodels.size() == 0) {
				this.empty_file++;
				continue;
			}

			ArrayList<EClass> eclasses = new ArrayList<EClass>();
			IAnalyzer.getAllClasses(emodels, eclasses, null);
			this.inc(eclasses.size());
			
			for (IAnalyzer analyzer : this.analyzers) {
				analyzer.analyze(eclasses);
			}
		}
		printSummary();
//		for(int i = 0; i < lim.length-1; i++) {
//			System.out.print("("+lim[i]+","+noc[i]+") ");
//		}
	}

	private void printSummary() {
		for (IAnalyzer analyzer : this.analyzers) {
			analyzer.printSummary();
		}
		System.out.println("not_readable_file" + " = " + this.not_readable_file);
		System.out.println("empty_file" + " = " + this.empty_file);
	}

}

package analyzer;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

public class MainAnalyzer {

	public static void main(String[] args) {
		String dir = System.getProperty("user.dir") + "\\model";
		System.out.print(dir);
		ArrayList<String> ecoreFiles = new ArrayList<String>();
		if (false) {
			getAllEcores(new File(dir), ecoreFiles);
		} else {
			ecoreFiles = new ArrayList<String>();
			ecoreFiles.add("D:\\data\\ap_mm_model\\own_examples\\myEcore.ecore");
			ecoreFiles.add("D:\\data\\ap_mm_model\\own_examples\\myEcore2.ecore");
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
	private int more_than_one = 0;
	private ArrayList<IAnalyzer> analyzers = new ArrayList<IAnalyzer>();

	public MainAnalyzer() {
		this.analyzers.add(new ClassHasMoreThanOneID());
		this.analyzers.add(new MalformedMultiplicityElement());
	}

	private void run(ArrayList<String> ecoreFiles) {

//		ClassHasMoreThanOneID id_analyzer = new ClassHasMoreThanOneID();
//		this.analyzers.add(id_analyzer);
		
		for (int i = 0; i < ecoreFiles.size(); i++) {

			if (i > 0 && i % 1000 == 0) {
				System.out.println(i);
			}
			if (i > 0 && i % 10000 == 0) {
				printSummary();
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
			if (emodels.size() > 1) {
				this.more_than_one++;
			}
			
			for (IAnalyzer analyzer : this.analyzers) {
				analyzer.analyze(emodels);
			}
		}
		printSummary();
	}

	private void printSummary() {
		System.out.println("not_readable_file" + " = " + this.not_readable_file);
		System.out.println("empty_file" + " = " + this.empty_file);
		System.out.println("more_than_one" + " = " + this.more_than_one);
	}

}

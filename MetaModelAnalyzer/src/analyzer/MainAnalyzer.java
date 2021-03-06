package analyzer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

public class MainAnalyzer {

	StringBuilder sb = new StringBuilder();
	
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

	public MainAnalyzer() {
//		this.analyzers.add(new ClassHasMoreThanOneID());
		this.analyzers.add(new MME_complete());
		this.analyzers.add(new MME_restricted());
//		this.analyzers.add(new Unnamed_complete());
//		this.analyzers.add(new Unnamed_noPackage());
	}

	String badModels = "";

	private void run(ArrayList<String> ecoreFiles) {
		sb.append("Number of meta-models: " + ecoreFiles.size());

		ArrayList<Integer> badones = new ArrayList<Integer>();
		badones.add(44803);
		badones.add(44805);
		badones.add(44806);
		badones.add(44809);
		badones.add(44810);
		badones.add(44811);

		for (int i = 0; i < ecoreFiles.size(); i++) {
			if (i % 1000 == 0) {
				System.out.println(i);
			}
			String s = ecoreFiles.get(i);
			EList<EObject> emodels = null;
			Resource myMetaModel = null;
			try {
				ResourceSet resourceSet = new ResourceSetImpl();
				resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
						new EcoreResourceFactoryImpl());
				myMetaModel = resourceSet.getResource(URI.createFileURI(s), true);
				emodels = myMetaModel.getContents();
			} catch (Exception e) {
				this.not_readable_file++;
				continue;
			}
			if (emodels.size() == 0) {
				this.empty_file++;
				continue;
			}
			if (badones.contains(i)) {
				this.not_readable_file++;
				badModels += (ecoreFiles.get(i) + "\n");
				continue;
			}

			ArrayList<EClass> eclasses = new ArrayList<EClass>();
			IAnalyzer.getAllClasses(emodels, eclasses, null);
			
			for (IAnalyzer analyzer : this.analyzers) {
				analyzer.analyze(myMetaModel.getAllContents(),eclasses);
			}			
			
			myMetaModel = null;
			eclasses = null;
		}
		printSummary();
	}

	private void printSummary() {
		for (IAnalyzer analyzer : this.analyzers) {
			analyzer.printSummary();
		}
		System.out.println("not_readable_file" + " = " + this.not_readable_file);
		System.out.println("empty_file" + " = " + this.empty_file);
		System.out.println("bad models with tree-iterator problems:\n" + badModels);
		
		sb.append("\nNot Readable Files" + " = " + this.not_readable_file);
		sb.append("\nEmpty Files" + " = " + this.empty_file);
		sb.append("\nBad Models with tree-iterator problems:\n" + badModels);
		
		try {
			FileWriter console_writer = new FileWriter("results\\console.log");
			console_writer.write(sb.toString());
			console_writer.flush();
			console_writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}

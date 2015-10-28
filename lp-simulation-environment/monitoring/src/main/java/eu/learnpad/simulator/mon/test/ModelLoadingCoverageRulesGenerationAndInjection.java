package eu.learnpad.simulator.mon.test;

import org.w3c.dom.Document;

import eu.learnpad.simulator.mon.BPMNExplorer.BPMNPathExplorer;
import eu.learnpad.simulator.mon.coverage.ModelLoader;
import eu.learnpad.simulator.mon.impl.BPMNPathExplorerImpl;
import eu.learnpad.simulator.mon.impl.PathCrossingRulesGeneratorImpl;
import eu.learnpad.simulator.mon.rulesGenerator.PathCrossingRulesGenerator;

public class ModelLoadingCoverageRulesGenerationAndInjection {

	public static void main(String[] args) {

		//read the model from a URI and store it locally
		Document modelLoaded = ModelLoader.READMODEL(args[0]);
		
		//Creation of the BPMNExplorerEngine
		BPMNPathExplorer bpmnExplorer = new BPMNPathExplorerImpl();
		
		//Creation of the PathCrossingRulesGenerator object
		PathCrossingRulesGenerator crossRulesGenerator = new PathCrossingRulesGeneratorImpl();
		
		
		//here I create an xml (that is a ComplexEventRuleActionListDocument) 
		//containing all the rules generated by the PathCrossingRulesGenerator
				
		String theXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		
				//this is the most important row, generation of the rules
				//starting from the modelLoaded that will be explored through bpmnExplorer
				crossRulesGenerator.generateRules(bpmnExplorer.getUnfoldedBPMN(modelLoaded)).xmlText();
		
		System.out.println(theXml);
		
	}

}
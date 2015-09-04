package eu.learnpad.verification.plugin.bpmn.guideline.impl;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Process;

import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.ThrowEvent;


public class explicitGateways extends abstractGuideline {

	explicitGateways() {

	}

	public explicitGateways(List<RootElement> diagram) {
		super(diagram);
		this.id = 20;
		this.Description = "The modeler should not split or join flows using activities or events; the modeler should split or join sequence flows always using gateways. Moreover the modeler should not start conditional sequence flows from an activity; he should always make explicit use of gateways and start conditional sequence flows from them. This includes that an activity can have only one incoming sequence flow and only one outgoing sequence flow.";
		this.Name = "Explicit usage of gateways";
		this.Suggestion = "Well done!";

	}

	public void findGL(List<RootElement> diagram) {
		String ret = "";
		int i = 1;
		for (RootElement rootElement : diagram) {
			if (rootElement instanceof Process) {
				Process process = (Process) rootElement;
				System.out.format("Found a process: %s\n", process.getName());
				for (FlowElement fe : process.getFlowElements()) {
					if (fe instanceof Activity) {

						Activity act = (Activity) fe;
						System.out.println(fe.eClass().getName() + ": name="
								+ fe.getName() + " ID=" + fe.getId());

						if (act.getOutgoing().size() > 1
								| act.getIncoming().size() > 1) {
							elements.add(fe);
							setElements(fe.getId());
							ret += i++ +") name=" + fe.getName() + " ID=" + fe.getId()
									+ "\n";
						}
					} else if (fe instanceof CatchEvent
							| fe instanceof ThrowEvent) {
						Event event = (Event) fe;
						System.out.println(fe.eClass().getName() + ": name="
								+ fe.getName() + " ID=" + fe.getId());

						if (event.getOutgoing().size() > 1
								| event.getIncoming().size() > 1) {
							elements.add(fe);
							setElements(fe.getId());
							ret += i++ +") name=" + fe.getName() + " ID=" + fe.getId()
									+ "\n";
						}
					} else if (fe instanceof StartEvent) {
						Event event = (Event) fe;
						System.out.println(fe.eClass().getName() + ": name="
								+ fe.getName() + " ID=" + fe.getId());

						if (event.getOutgoing().size() > 1) {
							elements.add(fe);
							setElements(fe.getId());
							ret +=i++ +") name=" + fe.getName() + " ID=" + fe.getId()
									+ "\n";
						}
					} else if (fe instanceof EndEvent) {
						Event event = (Event) fe;
						System.out.println(fe.eClass().getName() + ": name="
								+ fe.getName() + " ID=" + fe.getId());

						if (event.getIncoming().size() > 1) {
							elements.add(fe);
							setElements(fe.getId());
							ret +=i++ +") name=" + fe.getName() + " ID=" + fe.getId()
									+ "\n";
						}
					}
				}

			}
		}
		if (!elements.isEmpty()) {
			this.Suggestion = "Add Gateway before or/and after of the elements: " + ret;
			this.status = false;
		}else
			this.status = true;
	}

}
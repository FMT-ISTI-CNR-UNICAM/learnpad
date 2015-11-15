package eu.learnpad.ca.analysis.contentclarity.plugin;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.languagetool.Language;

import eu.learnpad.ca.analysis.Plugin;
import eu.learnpad.ca.rest.data.Annotation;
import eu.learnpad.ca.rest.data.Node;
import gate.DocumentContent;
import gate.util.InvalidOffsetException;

public class UnclearAcronym extends Plugin{ 

	protected static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UnclearAcronym.class);


	public UnclearAcronym(Language lang,  DocumentContent docContent, List<Node> listnode){
		this.language=lang;
		this.docContent = docContent;
		this.listnode = listnode;
	}







	public List<Annotation> checkUnclearAcronym(Set<gate.Annotation> listsentence, Set<gate.Annotation> listSentenceDefected, List<Annotation> annotations) {

		int id = 100_000;

		for (gate.Annotation sentence_gate : listsentence) {


			gate.Node gatenodestart = sentence_gate.getStartNode();
			gate.Node gatenodeend =  sentence_gate.getEndNode();
			try{

				DocumentContent sentence = docContent.getContent(gatenodestart.getOffset(),gatenodeend.getOffset());

				int len = annotations.size();
				id = checkdefect(sentence.toString(),id,annotations,gatenodestart.getOffset().intValue());
				if(annotations.size()>len){
					if(!listSentenceDefected.contains(sentence_gate))
						listSentenceDefected.add(sentence_gate);
				}

			}catch(InvalidOffsetException e){
				log.debug(e);
			}

		}
		return annotations;


	}

	private int  checkdefect(String sentence, int id, List<Annotation> annotations, int offset) {

		List<String> acronymdefected = new ArrayList<String>();
		List<String> listOfStrings = new ArrayList<String>(Arrays.asList(sentence.split(" ")));
		Stopwords stopw = new Stopwords();

		listOfStrings.removeAll(stopw.getStopwords());
		String ContentCleaned = StringUtils.join(listOfStrings, " ");
		Map<String,String> acronym = new HashMap<String, String>();
/*
		JLanguageTool langTool = new JLanguageTool(language);
		List<String> listsentenceofContentCleaned = langTool.sentenceTokenize(ContentCleaned);
*/

		String regex = "[A-Z|\\.]{2,}";

		// Create a Pattern object 65,46
		Pattern r = Pattern.compile(regex);
	//	for (String sentencecleanend : listsentenceofContentCleaned) {

			// Now create matcher object.
			Matcher m = r.matcher(ContentCleaned);

			while (m.find()){
				String tmpcandidateAcronym = m.group();
				String candidateAcronym = tmpcandidateAcronym;
				if(candidateAcronym.length()<=1 | (candidateAcronym.contains(".")&candidateAcronym.length()==2 ) | (!candidateAcronym.contains(".")&candidateAcronym.length()>4 )){
					continue;
				}
				if(candidateAcronym.contains(".") ){
					candidateAcronym = candidateAcronym.replaceAll("\\.", "");

				}
				int lencandidateAcronym = candidateAcronym.length();
				String regex2 = "([A-Z]+\\w+([ ]|)){"+lencandidateAcronym+"}";

				// Create a Pattern object
				Pattern r2 = Pattern.compile(regex2);

				// Now create matcher object.
				Matcher m2 = r2.matcher(ContentCleaned);
				boolean flag = true;
				while(m2.find()){

					//ok trovato  acronimi estesi

					String candidateexAcr = m2.group();


					String [] splited = candidateexAcr.split("\\s");
					if(candidateAcronym.length()==splited.length){
						for (int i = 0; i < splited.length; i++) {
							if(!splited[i].startsWith(String.valueOf(candidateAcronym.charAt(i)))){
								flag = true;
								break;
							}else{
								flag = false;					
							}
						}

						if(!flag){
							//System.out.println("Acronym "+candidateAcronym+" "+candidateexAcr);
							//Acronym trovato
							acronym.put(candidateAcronym,candidateexAcr);
							break;
						}
					}
				}
				if(flag){
					//new defect
					if(!acronym.containsKey(candidateAcronym)){
						/*if(tmpcandidateAcronym.indexOf(".")==tmpcandidateAcronym.lastIndexOf(".") & tmpcandidateAcronym.contains(".")){
							tmpcandidateAcronym = tmpcandidateAcronym.replaceAll("\\.", "");
						}*/
						if(!acronymdefected.contains(tmpcandidateAcronym)){
							acronymdefected.add(tmpcandidateAcronym);
						}
					}
				}

			//}
		}

		if(acronym.size()>0)
			log.trace(acronym+"\nsize: "+acronym.size());
		if(acronymdefected.size()>0 )
			log.trace(acronymdefected+"\nsize: "+acronymdefected.size());
		

		return insertdefectannotationsentence(sentence,id,annotations, acronymdefected,offset);

	}





	private int insertdefectannotationsentence(String sentence,
			int nodeid, List<Annotation> annotations,
			List<String> acronymdefected, int offset) {
		//StringTokenizer tokenizer = new StringTokenizer(sentence," \u201c\u201d.,?!:;()<>[]\b\t\n\f\r\"\'\"");
		String [] spliter = sentence.split("[\\s]");
		Map<String, Integer> elementfinded = new HashMap<String, Integer>();
		int precedentposition=0;

		//while (tokenizer.hasMoreTokens()) {
		//String token = tokenizer.nextToken();
		//token =	token.trim().replace(".", "").replace(":", "").replace(";", "").replace("\"","");
		for (int i = 0; i < spliter.length; i++) {

			String token = spliter[i];
			if(acronymdefected.contains(token)){
				int initialpos = indexofElement(sentence,token,elementfinded,"[\\s]");
				int finalpos = initialpos+token.length();
				if(precedentposition>initialpos){
					//initialpos = sentence.lastIndexOf(token);
					//finalpos = initialpos+token.length();
					log.fatal("error jump position");
				}

				precedentposition=finalpos;
				nodeid++;
				Node init= new Node(nodeid,initialpos+offset);
				nodeid++;
				Node end= new Node(nodeid,finalpos+offset);
				listnode.add(init);
				listnode.add(end);

				Annotation a = new Annotation();
				a.setId(nodeid);
				a.setEndNode(end.getId());
				a.setStartNode(init.getId());
				a.setNodeEnd(end);
				a.setNodeStart(init);
				a.setType("Unclear Acronym");
				a.setRecommendation("Explicit Acronym "+token);
				annotations.add(a);

			}
		}

		return nodeid;

	}


}

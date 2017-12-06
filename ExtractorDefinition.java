package it.uniroma1.sapienza.project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExtractorDefinition {
	private Document d;
	private static BufferedWriter writer;
	private static LinkedList<String> langs;
	private static LinkedList<String> posLang;
	private static LinkedList<String> defLang;
	private static LinkedList<String> tablist;

	public ExtractorDefinition(Document d, BufferedWriter writer){
		this.d=d;
		this.writer=writer;
	}

	/*Metodo per estrarre la lingua e il tipo di parola*/
	public void extractInfo(Document doc, BufferedWriter writer) throws Exception {
		langs=new LinkedList<String>();
		extractLang(doc,writer,langs);//,readLang);
	}

	/*Estrae la lingua*/
	private static void extractLang(Document doc, BufferedWriter writer, LinkedList langs)throws Exception{//,BufferedReader readLang) throws Exception {
		Elements h2s=doc.getElementsByTag("h2");
		for(Element h2 : h2s){
			Elements elements=h2.getElementsByClass("mw-headline");
			for(Element ul: elements){
				langs.add(ul.html());
			}
		}
		extractPOS(doc, writer, langs);
	}

	private static void extractPOS(Document doc, BufferedWriter writer, LinkedList<String> langs)throws Exception{
		Element toc=doc.getElementsByClass("toc").first(); //controllare che la prima classe "toc" è quella con i toctexts
		System.out.println("Tot langs: "+langs.size());
		if(toc!=null){
			Elements toctexts=toc.getElementsByClass("toctext");
			int i=0;
			while(i+1<langs.size()){
				System.out.println(i);
				String curr= (String) langs.get(i);
				String next= (String) langs.get(i+1);
				int j=searchLang(toctexts,curr); //posizione Lingua nel toctext
				System.out.println("Curr: "+curr);
				posLang=new LinkedList(); //lista pos per Lingua curr
				int tab=0;
				while(j< searchLang(toctexts,next)){
					if(isPos(toctexts.get(j).html())){
						String pos=toctexts.get(j).html();
						if(checkTableToctext(toctexts,j,next)){
							tab++;
							pos=pos+"T";
							if(tab==1){
								tablist=new LinkedList();
								tablist=extractTable(doc,curr);
							}
						}
						posLang.add(pos);
					}
					j++;
				}
				if(!posLang.isEmpty()){
					extractDefinition(doc,writer,curr,posLang,tablist);
				}
				i++;
			}
			String curr=(String) langs.getLast();
			System.out.println("Last curr: "+curr);
			int j= searchLang(toctexts,curr);
			posLang=new LinkedList();
			int tab=0;
			while(j< toctexts.size()){
				if(isPos(toctexts.get(j).html())){
					String poss=toctexts.get(j).html();
					//	Sistemare la ricerca della tabella
					int ij=j+1;
					while(ij<toctexts.size() && !isPos(toctexts.get(ij).html())){
						if(toctexts.get(ij).html().equals("Inflection") || toctexts.get(ij).html().equals("Declension")||
								toctexts.get(ij).html().equals("Conjugation")){
							poss=poss+"T";
							tab++;
							if(tab==1){
								tablist=new LinkedList();
								tablist=extractTable(doc,curr);
							}
						}
						ij++;		
					}	
					posLang.add(poss);
				}
				j++;
			}
			if(!posLang.isEmpty())
				extractDefinition(doc,writer,curr,posLang,tablist);
		/*}
		else{
			Elements h3s=doc.getElementsByTag("h3");
			for(Element h3 : h3s){
				Elements elements=h3.getElementsByClass("mw-headline");
				for(Element ul: elements){
					if(isPos(ul.html())){
						String pos=ul.html();
						Elements h4s= doc.getElementsByTag("h4");
						for(Element h4 : h4s){
							Elements ul4=h3.getElementsByClass("mw-headline");
							for(Element u4 : ul4){
								if(u4.html().equals("Conjugation") ||u4.html().equals("Declension") ||u4.html().equals("Inflection")){
									pos=pos+"T";
									tablist=new LinkedList();
									tablist=extractTable(doc,langs.getFirst());
								}
							}
						}
					}
					langs.add(ul.html());
				}
			}
			extractPOS(doc, writer, langs);*/
		}
	}
	private static LinkedList<String> extractTable(Document doc, String currlanguage) throws Exception {
		//tablist=new LinkedList();
		String langCode=getLangCode(currlanguage);
		ExtractorTable et=new ExtractorTable();
		return et.extractTableFromClassToMatrix(doc,langCode);//Giusto extractTableFromClass(doc,langCode);
	}

	private static void extractDefinition(Document doc, BufferedWriter writer, String curr, LinkedList posLang, LinkedList tablist)throws Exception{//,BufferedReader readLang) throws Exception {

		defLang=new LinkedList();
		String langCode=getLangCode(curr);
		if(langCode!=null){
			System.out.println("LangCode: "+langCode);
			Elements ps= doc.getElementsByTag("p");
			for(Element p: ps){
				Elements Latns= p.getElementsByClass("Latn headword");
				if(Latns!=null){
					for(Element Latn : Latns){
						String lan=Latn.attr("lang");
						if(lan.equals(langCode)){
							getDefinition(p,defLang);
						}
					}
				}
				Elements Latinx=p.getElementsByClass("Latinx headword"); //esempio Old English
				if(Latinx!=null){
					for(Element Latn : Latinx){
						String lan=Latn.attr("lang");
						if(lan.equals(langCode)){
							getDefinition(p,defLang);
						}
					}
				}
				//	if(!defLang.isEmpty())
				//	printDefinition(doc,writer,curr,langCode,posLang,defLang);
			}
			if(!defLang.isEmpty()) //posLang is not empty for sure if we are here
				printDefinition(doc,writer,curr,langCode,posLang,defLang,tablist);
		}
		return;
	}

	private static void getDefinition(Element p, LinkedList defLang) {
		/*estrae elementi per la definizione*/
		String[] temp1=new String[20];
		String[] temp2=new String[20];
		int count=0;
		int j=0;
		String def="";
		Elements is=p.getElementsByTag("i");
		for(Element i : is){
			temp1[count]=i.html();
			if(i.getElementsByTag("a").size()>0){ //sistemare nel caso ci siano due o più forme per lo stesso tense(vedi augustus)
				temp1[count]=i.getElementsByTag("a").html();
			}
			count++;
		}
		Elements spans=p.getElementsByTag("span"); //nel caso di null è perchè non c'è span all'inizio
		if(spans.size()>0){
			for(Element span : spans){
				//non memorizzare quello che è fuori la parentesi -> memorizzare
				if(span.getElementsByTag("abbr").size()>0){
					Elements bs=p.getElementsByTag("b");
					for(Element b:bs){
						if(b.getElementsByTag("a").size()>0)
							temp2[j]=b.getElementsByTag("a").html();
						else if(b.getElementsByTag("strong").size()>0)
							temp2[j]=b.getElementsByTag("strong").html();
						else
							temp2[j]=b.html();
						j++;
					}	
				}
				else{
					temp2[j]=span.html();
					if(span.getElementsByTag("b").size()>0){
						Elements bs=span.getElementsByTag("b");
						for(Element b:bs){
							String stringb=b.html();			
							if(b.getElementsByTag("a").size()>0){
								//Prima	temp2[j]=b.getElementsByTag("a").html();
								//Per tener conto di more and most
								String good1=stringb.substring(stringb.indexOf("\">")+2, stringb.indexOf("</"));
								String good2=stringb.substring(stringb.indexOf("a>")+2,stringb.length());

								temp2[j]=good1+" "+good2;
							}else if(b.getElementsByTag("strong").size()>0)
								temp2[j]=b.getElementsByTag("strong").html();
							else
								temp2[j]=b.html();
						}				
					}
					if(span.getElementsByTag("a").size()>0){
						for(Element a:span.getElementsByTag("a")){
							temp2[j]=a.html();
						}
					}
					j++;
				}
			}
		}
		else{ //non c'è <span>
			Elements bs=p.getElementsByTag("b");
			for(Element b:bs){
				if(b.getElementsByTag("a").size()>0)
					temp2[j]=b.getElementsByTag("a").html();
				else
					temp2[j]=b.html();
				j++;
			}	
		}
		/*quello che estrae lo salva in una stringa*/ 
		int k=0;
		while(k<count){
			if(temp1[k+1]!=null && temp1[k+1].equals("or")){
				if(temp1[k+2]!=null && temp1[k+2].equals("or")){
					def=def+"\t"+temp2[k]+"\t"+temp1[k+1]+"\t"+temp2[k+1]+"\t"+temp1[k+2]+"\t"+temp2[k+2]+"\t"+temp1[k];
					k=k+3;
				}
				else{
					def=def +"\t"+temp2[k]+"\t"+temp1[k+1]+"\t"+temp2[k+1]+"\t"+temp1[k];
					k=k+2;
				}
			}
			else{
				def=def +"\t"+temp2[k]+"\t"+temp1[k];
				k++;
			}
		}
		/*for(int k=0;k<count;k++){
			def=def +"\t"+temp2[k]+"\t"+temp1[k];
		}*/
		/*lo memorizza in defLang*/
		defLang.add(def);
	}

	/*gestire diversi pos per la stessa lingua*/
	private static void printDefinition(Document doc, BufferedWriter writer, String curr, String langCode, LinkedList posLang, LinkedList defLang, LinkedList tablist) throws Exception {
		/*match tra le due liste e stamparlo sul file write*/
		//	TableEdit table=new TableEdit(doc,langCode);
		/*tablist=new LinkedList<String>(); table.extractTableFromClass(doc,langCode,tablist);*/
		//	tablist=table.extractTableFromClass(doc,langCode);
		System.out.println("Pos size: "+posLang.size());
		System.out.println("Def size: "+defLang.size());

		if(posLang.size()!=0 && defLang.size()!=0){
			int i=0;
			while(i<posLang.size()){
				if(posLang.get(i).toString().endsWith("T")){
					int endString=(posLang.get(i).toString().length())-1;
					String pos=posLang.get(i).toString().substring(0, endString);
					System.out.println("Tablist size: "+tablist.size());
					if(!tablist.isEmpty()){
						writer.write(curr+"#"+pos.toUpperCase()+"\t"+"Table"+"\t"+tablist.getFirst());//.getFirst(tablist));
						writer.newLine();
						tablist.removeFirst();
					}
					else{
						writer.write(curr+"#"+pos.toUpperCase()+"\t"+"Table"+"\t vuoto");
						if(!defLang.get(i).equals(""))
							writer.write("\t"+ defLang.get(i));
						writer.newLine();
					}
				}
				else{
					writer.write(curr+"#"+posLang.get(i).toString().toUpperCase()+"\t"+defLang.get(i));
					writer.newLine();
				}
				i++;
			}
		}
	}

	private static int searchLang(Elements toctexts, String lang) {
		int i=0;
		while(!toctexts.get(i).html().equals(lang))
			i++;
		return i;
	}

	private static boolean checkTableToctext(Elements toctexts, int j, String next) {
		int i=j+1;
		while(!toctexts.get(i).html().equals(next) && !isPos(toctexts.get(i).html())){
			if(toctexts.get(i).html().equals("Inflection") || toctexts.get(i).html().equals("Declension") || toctexts.get(i).html().equals("Conjugation"))
				return true;
			i++;		
		}
		return false;
	}



	private static boolean isPos(String s) {
		switch(s){
		case "Verb":
			return true;
		case "Adverb":
			return true;
		case "Adjective":
			return true;
		case "Noun":
			return true;
		case "Pronoun":
			return true;
			/*Altri incontrati?*/
		default:
			return false;
		}
	}
	private static String getLangCode(String curr)throws Exception{//, BufferedReader readLang) throws Exception {
		BufferedReader readLang=new BufferedReader(new FileReader("langC.txt"));
		//String line=readLang.readLine();
		int ikd=0;
		while(ikd<300){ 
			//while(line!=null){
			String line=readLang.readLine();
			if(line!=null && line.contains(curr)){
				//if(line.contains(curr)){
				String[] split=line.split("\t");
				return split[1];
			}
			//}
			ikd++;
		}
		return null;
	}

}


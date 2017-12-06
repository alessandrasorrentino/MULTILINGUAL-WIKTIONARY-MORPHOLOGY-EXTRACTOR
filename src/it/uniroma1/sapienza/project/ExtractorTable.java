package it.uniroma1.sapienza.project;

import java.util.LinkedList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExtractorTable {



	private boolean checkLang(Element lang, String langCode) {
		for(Element td : lang.getElementsByTag("td")){
			if(td.getElementsByClass("Latn").size()!=0
					&& td.getElementsByClass("Latn").first()!=null){
				Element Latn=td.getElementsByClass("Latn").first();
				String lan=Latn.attr("lang");
				if(langCode.equals(lan)){
					return true;
				}
			}
			if(td.getElementsByClass("Latinx").size()!=0
					&& td.getElementsByClass("Latinx").first()!=null){
				Element Latn=td.getElementsByClass("Latinx").first();
				String lan=Latn.attr("lang");
				if(langCode.equals(lan)){
					return true;
				}
			}
			if(td.getElementsByTag("span").size()!=0 &&
					td.getElementsByTag("span").first().hasAttr("lang")){
				String lan= td.getElementsByTag("span").first().attr("lang");
				if(langCode.equals(lan)){
					return true;
				}

			}
		}
		return false;
	}


	private static String extract(Element first) {
		if(first.getElementsByTag("a").size()!=0){
			String sentenceToSplit[]=first.html().split("<");
			return sentenceToSplit[0] + " "+deep2String(first.getElementsByTag("a"));
		}
		else if(first.getElementsByTag("strong").size()!=0){
			String sentenceToSplit[]=first.html().split("<");
			return sentenceToSplit[0] + " "+deep2String(first.getElementsByTag("strong"));
		}
		else if(first.getElementsByTag("span").size()!=0){
			String sentenceToSplit[]=first.html().split("<");
			return sentenceToSplit[0] + " "+deep2String(first.getElementsByTag("span"));
		}
		else if(first.getElementsByTag("font").size()!=0){
			String sentenceToSplit[]=first.html().split("<");
			return sentenceToSplit[0] + " "+deep2String(first.getElementsByTag("font"));
		}
		else
			return first.html();
	}

	private static String deep2String(Elements elementsByTag) {
		String s="";
		for(Element element : elementsByTag){
			if(checkTag(element,"strong")){
				for(Element st:element.getElementsByTag("strong"))
					s=st.html()+"\t";
			}
			else if(checkTag(element,"a")){
				for(Element a : element.getElementsByTag("a"))
					s=a.html()+"\t";
			}
			else if(checkTag(element,"font")){
				for(Element a : element.getElementsByTag("font"))
					s=a.html()+"\t";
			}
			else if(checkTag(element,"span")){
				for(Element a : element.getElementsByTag("span"))
					if(checkTag(a,"span")){
						//	for(Element sp : a.getElementsByTag("span"))
						s="";
					}
					else
						s=a.html()+"\t";
			}
			else if(checkTag(element,"font")){
				for(Element a : element.getElementsByTag("font"))
					s="";
			}
			else 
				s=element.html()+"\t";
		}
		if(s.contains("<")){
			String sentenceToSplit[]=s.split("<");
			s=sentenceToSplit[0]+"\t";
		}
		if(s.contains("<br />")){
			String sentenceToSplit[]=s.split("<");
			s=sentenceToSplit[0]+" "+sentenceToSplit[1]+" ";
		}
		
		return s;
	}

	private static boolean checkTag(Element el, String tag) {
		if(el.getElementsByTag(tag).size()!=0){
			return true;
		}
		return false;
	}


	public LinkedList<String> extractTableFromClassToMatrix(Document doc, String langCode) throws Exception {
		LinkedList<String> tablist=new LinkedList();
		//Assumo che sia solo inflection table

		tablist=extractFromItToMatrix(doc,langCode);
		return tablist;
		//return null;
	}

	private LinkedList<String> extractFromItToMatrix(Document doc, String langCode) throws Exception {

		LinkedList<String> tables=new LinkedList();
		if(isIts(langCode)){
			tables=extractFromIts(doc,langCode);
		}
		else if(isPit(langCode)){
			tables=extractFromPit(doc,langCode);
		}
		else{
		String it="inflection-table";
		for(Element tab : doc.getElementsByTag("table")){
			//	if(tab.hasClass(it)){
			if(checkLang(tab,langCode)){
				System.out.println("trovata tabella per: "+langCode);
				String s=printToMatrix(tab);
				tables.add(s);
			}
			//	}
		}
		}
		return tables;
	}

	private String printToMatrix(Element tab) {
		String s ="";
		String[][] matrix= buildMatrix(tab);
		s=readMatrix(matrix);
		return s;
	}

	/*lettura della matrice*/

	private String readMatrix(String[][] matrix) {
		String s=" "; 
		int out=0;
		for(int i=0;i< matrix.length;i++){
			for(int j=0;j< matrix[i].length;j++){
				if(matrix[i][j]!=null){
					System.out.println("matrix["+i+"]["+j+"]: "+matrix[i][j]);
					if(!isSpecialChar(matrix[i][j])){
						if(!isCapitalLetter(matrix[i][j])){
							out++;
							String temp= matrix[i][j]+" "+checkUp(matrix,i,j)+checkPerson(matrix,i,j)+chekLeft(matrix,i,j);
							if(!s.contains(temp)){
								s=s+temp;
							}
						}
					}

				}
				s=s+" ";
				//s=s+matrix[i][j]+" ";
			}
		}
		System.out.println("out:"+out);
		return s.toLowerCase();
	}

	private boolean isSpecialChar(String string) {
		switch(string){
		case "": return true;
		case " — ": return true;
		case "—": return true;
		default:break;
		}
		return false;
	}


	private String checkPerson(String[][] matrix, int i, int j) {
		int person=checkRowPers(matrix);
		if(i<person){
			return " ";
		}
		else
			return " "+matrix[person+1][j]+" "+matrix[person][j] +" "+ matrix[person][0]+ " ";
	}

	private boolean exists(String[][] matrix, String string) {
		for(int i=0;i<matrix.length;i++){
			if(matrix[i][0].equals("PERSON"))		
				return true;
		}
		return false;
	}


	private int checkRowPers(String[][] matrix) {
		for(int i=0; i<matrix.length;i++){
			if(matrix[i][0]!=null && matrix[i][0].equals("PERSON"))
				return i;
		}
		return 100;
	}

	private String checkUp(String[][] matrix, int i, int j) {
		while(i>0){
			if(isCapitalLetter(matrix[i-1][j]))
				return matrix[i-1][j]+" "+matrix[i-1][0];
			i--;
		}
		if(i==0)
			return " ";
		else
			return matrix[i][j];
	}

	private String chekLeft(String[][] matrix, int i, int j) {
		int maxLeft=matrix[i].length;
		while(j>0){
			if(isCapitalLetter(matrix[i][j-1])){
				/*if(isCapitalLetter(matrix[i][j-2]) && matrix[i][j-2] !=null && ! matrix[i][j-2].equals(matrix[i][j-1]))
					return matrix[i][j-1]+" "+matrix[i][j-2];
				else*/
				return matrix[i][j-1];
			}
			j--;
		}
		return " ";

	}

	private String[][] buildMatrix(Element tab) {
		int righe= tab.getElementsByTag("tr").size();
		int colonne=countColumns(tab); //check countcolumns
		System.out.println("righe "+righe+"colonne:"+colonne);
		String[][] result= new String[righe][colonne];
		fill(result,tab, righe, colonne);
		return result;
	}

	/*Riempio la matrice con tutte le informazioni contenute nella tabella*/
	private void fill(String[][] result, Element tab, int r, int c) {
		for(int i=0; i< r;i++){
			Element tr=tab.getElementsByTag("tr").get(i);
			Elements ths=tr.getElementsByTag("th"); //header della cella
			Elements tds=tr.getElementsByTag("td");
			if(ths.size()==c){ //Riga con forme non flesse
				for(int j=0;j<c;j++){
					checkRowColspan(ths.get(j),result, i,j,extract(ths.get(j)).toUpperCase());
				}
			}
			else{
				int indexEl=0;
				if(ths.size()==1){
					int startIndex=0;
					/*if(tds.size()==c-2){
						startIndex=indexEl+1;
					}*/
					checkRowColspan(ths.get(0),result, i,indexEl,extract(ths.get(0)).toUpperCase());
					if(ths.get(0).hasAttr("colspan")){
						int col= Integer.parseInt(ths.get(0).attr("colspan"));
						indexEl=indexEl+col;
					}
					else{
						indexEl++;
					}
					for(Element td : tds){
						checkRowColspan(td,result, i, indexEl,extract(td));
						if(td.hasAttr("colspan")){
							int coltd=Integer.parseInt(td.attr("colspan"));
							indexEl=indexEl+coltd;
						}
						else 
							indexEl++;
					}
				}
				else if(ths.size()==2 && (c-1)!=2){ //controllare
					Element th1=ths.first();
					/*					if(tds.isEmpty())
						indexEl++;*/
					checkRowColspan(th1,result, i, indexEl,extract(th1).toUpperCase());
					if(th1.hasAttr("colspan")){
						int col= Integer.parseInt(th1.attr("colspan"));
						indexEl=indexEl+col;
					}else 
						indexEl++;
					if(tds.size()==2){ //controllare che tds sia sempre uno, in caso modificare
						checkRowColspan(tds.first(),result,i, indexEl,extract(tds.first()));
						if(tds.first().hasAttr("colspan")){
							indexEl=indexEl+ Integer.parseInt(tds.first().attr("colspan"));
						}
					}
					Element th2=ths.last();
					checkRowColspan(th2,result, i, indexEl,extract(th2).toUpperCase());
					if(th2.hasAttr("colspan")){
						int col= Integer.parseInt(th2.attr("colspan"));
						indexEl=indexEl+col;
					}
					else indexEl++;
					if(tds.size()==2){ //controllare che tds sia sempre uno, in caso modificare
						checkRowColspan(tds.last(),result,i, indexEl,extract(tds.last()));
						if(tds.last().hasAttr("colspan")){
							indexEl=indexEl+ Integer.parseInt(tds.last().attr("colspan"));
						}
					}
					else{
						for(Element td: tds){
							checkRowColspan(td,result, i, indexEl, extract(td));
							if(td.hasAttr("colspan")){
								indexEl=indexEl+ Integer.parseInt(td.attr("colspan"));
							}
							else
								indexEl++;
						}
					}
				}
				else if(ths.size()==3){
					if(tds.size()==0){
						//assumo che non ci siano tds
						Element th1=ths.get(0);
						checkRowColspan(th1,result,i,indexEl,extract(th1).toUpperCase());
						if(th1.hasAttr("colspan")){
							indexEl=indexEl+Integer.parseInt(th1.attr("colspan"));		
						}
						else indexEl++;
						Element th2=ths.get(1);
						checkRowColspan(th2,result, i, indexEl, extract(th2).toUpperCase());
						if(th2.hasAttr("colspan")){
							indexEl=indexEl+Integer.parseInt(th2.attr("colspan"));
						}else indexEl++;
						Element th3=ths.last();
						checkRowColspan(th3,result, i,indexEl,extract(th3).toUpperCase());
						if(th3.hasAttr("colspan")){
							indexEl=indexEl+ Integer.parseInt(th3.attr("colspan"));	
						}else indexEl++;
					}
					else{
						if(tds.size()==1){
							Element th1=ths.get(0);
							checkRowColspan(th1,result,i,indexEl,extract(th1).toUpperCase());
							if(th1.hasAttr("colspan")){
								indexEl=indexEl+Integer.parseInt(th1.attr("colspan"));		
							}
							else indexEl++;
							checkRowColspan(tds.first(),result, i,indexEl,extract(tds.first()));
							if(tds.first().hasAttr("colspan")){
								indexEl=indexEl+Integer.parseInt(tds.first().attr("colspan"));		
							}
							else indexEl++;
							Element th2=ths.get(1);
							checkRowColspan(th2,result, i, indexEl, extract(th2).toUpperCase());
							if(th2.hasAttr("colspan")){
								indexEl=indexEl+Integer.parseInt(th2.attr("colspan"));
							}else indexEl++;
							Element th3=ths.last();
							checkRowColspan(th3,result, i,indexEl,extract(th3).toUpperCase());
							if(th3.hasAttr("colspan")){
								indexEl=indexEl+ Integer.parseInt(th3.attr("colspan"));	
							}else indexEl++;
						}
					}

				}
				else if(ths.size()==c-1){// && tds.isEmpty()){  
					if(tds.isEmpty()){//controllare senza row e col span
						if(noColspan(ths)){
							for(int j=0;j<ths.size();j++){
								writeInMatrix(result, i, j+1, ths.get(j).html().toUpperCase());
							}
						}
						else{
							for(Element e: ths){
								checkRowColspan(e, result, i, indexEl, extract(e).toUpperCase());
								if(e.hasAttr("colspan")){
									indexEl=indexEl+Integer.parseInt(e.attr("colspan"));
								}
								else
									indexEl++;
							}
						}
					}
					else if(tds.size()==1){
						writeInMatrix(result,i, indexEl, extract(tds.first()));
						indexEl++;
						for(Element e: ths){
							checkRowColspan(e, result, i, indexEl, extract(e).toUpperCase());
							if(e.hasAttr("colspan")){
								indexEl=indexEl+Integer.parseInt(e.attr("colspan"));
							}
							else
								indexEl++;
						}
					}
				}
				/*	else if(ths.size()==c-1 && tds.size()==1){
					writeInMatrix(result,i, 0, extract(tds.first()));
					for(Element e: ths){
						checkRowColspan(e, result, i, indexEl, extract(e).toUpperCase());
						if(e.hasAttr("colspan")){
							indexEl=indexEl+Integer.parseInt(e.attr("colspan"));
						}
						else
							indexEl++;
					}
				}*/
				else if(ths.size()==c-2 && tds.isEmpty()){
					if(noColspan(ths)){
						for(int j=0; j<ths.size(); j++){
							writeInMatrix(result, i, j+2, extract(ths.get(j)).toUpperCase());
						}
					}
				}
				else if(tds.size()==c-2 && ths.size()==0){
					if(noColspan(tds)){
						for(int j=0; j<tds.size(); j++){
							writeInMatrix(result, i, j+2, extract(tds.get(j)));
						}

					}
				}
				else if(ths.size()==0 && tds.size()==c-1){ //controllare senza row e col span
					if(noColspan(tds)){
						for(int j=0; j<tds.size(); j++){
							writeInMatrix(result, i, j+1, extract(tds.get(j)));
						}
					}
				}
			}
		}
	}


	private boolean noColspan(Elements ths) {
		for(Element th: ths){
			if(th.hasAttr("colspan"))
				return false;
		}
		return true;
	}


	private void checkRowColspan(Element element, String[][] result, int currentRow, int currentCol, String info) {
		if(element.hasAttr("rowspan") && element.hasAttr("colspan")){
			int rows=Integer.parseInt(element.attr("rowspan"));
			int cols= Integer.parseInt(element.attr("colspan"));
			int finr=currentRow+rows;
			int finc= currentCol+cols;
			for(int count=currentRow;count<finr;count++){
				for(int k=currentCol; k<finc; k++){
					writeInMatrix(result, count, k,info);
				}
			}
		}
		else if(element.hasAttr("rowspan")){
			int rows=Integer.parseInt(element.attr("rowspan"));
			int finr=currentRow+rows;
			for(int count=currentRow;count<finr;count++){
				writeInMatrix(result, count, currentCol,info);
			}
		}else if(element.hasAttr("colspan")){
			int cols=Integer.parseInt(element.attr("colspan"));
			int finc=currentCol+cols;
			for(int count=currentCol;count<finc;count++){
				writeInMatrix(result, currentRow, count,info);
			}
		}
		else 
			writeInMatrix(result, currentRow, currentCol,info);
	}


	private void writeInMatrix(String[][] result, int i, int j, String upperCase) {
		while(result[i][j]!=null && j< result[i].length){
			j++;
		}
		if(result[i][j]==null)
			result[i][j]=upperCase;
		/*else  //controllare
		result[i][j+1]=upperCase;*/
	}

	private int countColumns(Element tab){
		int max=0;
		for(Element tr : tab.getElementsByTag("tr")){
			if(tr.getElementsByTag("th").size()>0){
				int count=0;
				for(Element e : tr.getElementsByTag("th")){
					if(e.hasAttr("colspan")){
						count=count+ Integer.parseInt(e.attr("colspan"));
					}
					else
						count++;
				}
				if(count>max)
					max=count;
			}
		}
		return max;
	}


	private boolean isCapitalLetter(String s){
		if (s!=null && !isSpecialChar(s)){
			char i=s.charAt(0);
			if(Character.isUpperCase(i))
				/*	if(i=='A'|| i=='B'|| i=='C'|| i=='D'|| i=='E'|| i=='F'|| i=='G'|| i=='H'|| i=='J'||
					i=='K'|| i=='I'|| i=='L'|| i=='M'|| i=='N'|| i=='O'|| i=='P'|| i=='Q'|| i=='R'||
					i=='S'|| i=='T'|| i=='U'|| i=='V'|| i=='Z'|| i=='W' || i=='X'|| i=='Y')*/
				return true;
			else
				return false;
		}
		return false;
	}
	
	private LinkedList<String> extractFromPit(Document doc, String langCode) throws Exception {
		LinkedList<String> tables=new LinkedList();
		String pit="prettytable inflection-table";
		for(Element tab : doc.getElementsByTag("table")){
			if(tab.hasClass(pit)){
				if(checkLang(tab,langCode)){
					System.out.println("trovata tabella per: "+langCode);
					String s=printPretty(tab);
					tables.add(s);
				}
			}
		}
		return tables;
	}
	
	private LinkedList<String> extractFromIts(Document doc, String langCode) {
		LinkedList<String> tables=new LinkedList();
		String its="inflection-table vsSwitcher vsToggleCategory-inflection";
		for(Element tab : doc.getElementsByTag("table")){
			if(tab.hasClass(its)){
				if(checkLang(tab,langCode)){
					String s=printIts(tab);
					tables.add(s);
				}
			}
		}
		return tables;
	}
	
	private String printPretty(Element table){
		String s="";
		Elements tenses=table.getElementsByTag("tr");
		String gender[]=new String[3];
		Element genders=tenses.first();
		Elements ths=genders.getElementsByTag("th");
		//generalizzare in caso
		gender[1]=ths.get(1).html(); //Singular
		gender[2]=ths.get(2).html(); //Plural
		int i=1;
		//As in case of Augustus
		if(tenses.get(1).getElementsByTag("th").size()==7){
			//Salvo maschile-femm-neuter
			Elements ths2=tenses.get(1).getElementsByTag("th");
			String[] ths2words=new String[3];
			ths2words[0]=ths2.get(1).html();
			ths2words[1]=ths2.get(2).html();
			ths2words[2]=ths2.get(3).html();
			int ji=2;
			while(ji<tenses.size()){
				Element block=tenses.get(ji);
				String modo=deep2String(block.getElementsByTag("th")); //nominativo-accusativo-etc
				Elements tds=block.getElementsByTag("td");
				s=checkAndPrintRows(tds,ths2words,gender,s,modo); //prima tds come primo parametro
				ji++;
			}

		}
		else {//As in case of thesaurus
			while(i<tenses.size()){
				Element tense=tenses.get(i);
				String modo=deep2String(tense.getElementsByTag("th")); //nominativo-accusativo-etc
				Elements tds=tense.getElementsByTag("td");
				if(tds.size()!=0){
					Element singular=tds.first();
					//deep(singular.getElementsByTag("span"),s);
					s=s+"\t"+deep2String(singular.getElementsByTag("span"))+gender[1]+"\t"+modo;
					Element plural=tds.get(1);
					//	deep(plural.getElementsByTag("span"),s);
					s=s+"\t"+deep2String(plural.getElementsByTag("span"))+gender[2]+"\t"+modo;
				}
				i++;
			}
		}
		return s;
	}
	
	private String checkAndPrintRows(Elements tds, String[] ths2words, String[] gender, String s, String modo) {
		switch(tds.size()){
		case 3:
			s=s+"\t"+deep2String(tds.get(0).getElementsByTag("span"))+ths2words[0]+"\t"+gender[1]+"\t"+modo;
			s=s+"\t"+deep2String(tds.get(0).getElementsByTag("span"))+ths2words[1]+"\t"+gender[1]+"\t"+modo;
			s=s+"\t"+deep2String(tds.get(1).getElementsByTag("span"))+ths2words[2]+"\t"+gender[1]+"\t"+modo;
			if(tds.get(2).hasAttr("colspan")){
				String el=tds.get(2).attr("colspan");
				if(el.equals(3)){
					s=s+"\t"+deep2String(tds.get(2).getElementsByTag("span"))+ths2words[0]+"\t"+gender[2]+"\t"+modo;
					s=s+"\t"+deep2String(tds.get(2).getElementsByTag("span"))+ths2words[1]+"\t"+gender[2]+"\t"+modo;
					s=s+"\t"+deep2String(tds.get(2).getElementsByTag("span"))+ths2words[2]+"\t"+gender[2]+"\t"+modo;
				}
			}
			return s;
		case 4:
			s=s+"\t"+deep2String(tds.get(0).getElementsByTag("span"))+ths2words[0]+"\t"+gender[1]+"\t"+modo;
			s=s+"\t"+deep2String(tds.get(1).getElementsByTag("span"))+ths2words[1]+"\t"+gender[1]+"\t"+modo;
			s=s+"\t"+deep2String(tds.get(2).getElementsByTag("span"))+ths2words[2]+"\t"+gender[1]+"\t"+modo;
			if(tds.get(3).hasAttr("colspan")){
				String el=tds.get(3).attr("colspan");
				if(el.equals(3)){
					s=s+"\t"+deep2String(tds.get(3).getElementsByTag("span"))+ths2words[0]+"\t"+gender[2]+"\t"+modo;
					s=s+"\t"+deep2String(tds.get(3).getElementsByTag("span"))+ths2words[1]+"\t"+gender[2]+"\t"+modo;
					s=s+"\t"+deep2String(tds.get(3).getElementsByTag("span"))+ths2words[2]+"\t"+gender[2]+"\t"+modo;
				}
			}
			return s;
		case 6:
			s=s+"\t"+deep2String(tds.get(0).getElementsByTag("span"))+ths2words[0]+"\t"+gender[1]+"\t"+modo;
			s=s+"\t"+deep2String(tds.get(1).getElementsByTag("span"))+ths2words[1]+"\t"+gender[1]+"\t"+modo;
			s=s+"\t"+deep2String(tds.get(2).getElementsByTag("span"))+ths2words[2]+"\t"+gender[1]+"\t"+modo;
			s=s+"\t"+deep2String(tds.get(3).getElementsByTag("span"))+ths2words[0]+"\t"+gender[2]+"\t"+modo;
			s=s+"\t"+deep2String(tds.get(4).getElementsByTag("span"))+ths2words[1]+"\t"+gender[2]+"\t"+modo;
			s=s+"\t"+deep2String(tds.get(5).getElementsByTag("span"))+ths2words[2]+"\t"+gender[2]+"\t"+modo;				
			return s;
		case 7:
			s=s+"\t"+deep2String(tds.get(0).getElementsByTag("span"))+ths2words[0]+"\t"+gender[1]+"\t"+modo;
			s=s+"\t"+deep2String(tds.get(1).getElementsByTag("span"))+ths2words[1]+"\t"+gender[1]+"\t"+modo;
			s=s+"\t"+deep2String(tds.get(2).getElementsByTag("span"))+ths2words[2]+"\t"+gender[1]+"\t"+modo;
			s=s+"\t"+deep2String(tds.get(4).getElementsByTag("span"))+ths2words[0]+"\t"+gender[2]+"\t"+modo;
			s=s+"\t"+deep2String(tds.get(5).getElementsByTag("span"))+ths2words[1]+"\t"+gender[2]+"\t"+modo;
			s=s+"\t"+deep2String(tds.get(6).getElementsByTag("span"))+ths2words[2]+"\t"+gender[2]+"\t"+modo;	

			return s;
		default: return s;
		}
	}


	private boolean isIts(String langCode) {
		switch(langCode){
		case "nl":
			return true;
		case "vep":
			return true;
		default:
			return false;
		}
	}

	private boolean isPit(String langCode) {
		switch(langCode){
		case "la":
			return true;
		default:
			return false;
		}
	}
	
	private String printIts(Element table) {
		String s="";
		Elements tenses=table.getElementsByTag("tr");//.hasClass("vsHide");
		Element lang=tenses.get(3); //Già controllato ?
		for(Element tr : tenses){
			if(tr.hasClass("vsShow")){
				String modo=tr.getElementsByTag("th").first().html(); //se c'è solo un th (Gratis dutch)
				String word=extract(tr.getElementsByTag("td").first()); //se c'è solo un td (Gratis dutch)
				s= s+" "+word+" "+modo;
			}
			/*if(tr.hasClass("vsHide")){
				Elements ths=tr.getElementsByTag("th");
				Elements tds=tr.getElementsByTag("td");
				if(tds.size()==1 && tds.first().html().equals("")){
					gender=deep2String(tr.getElementsByTag("th"));//ths.first().html(); //positive nel caso di GRATIS in DUTCH
				}
				else{
					String thstring=deep2String(ths);
					Element toFind=tds.first().getElementsByTag("span").first();
					String word= toFind.getElementsByTag("strong").html();
					s=s+"\t"+word+"\t"+thstring+gender;
				}
			}*/
		}
		return s;
	}


}

package it.uniroma1.sapienza.project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainLangCode {
	static BufferedWriter langCode;
	
	public static void main(String[] args) throws Exception{
		langCode = new BufferedWriter(new FileWriter(new File("langCode.txt")));
		String path="https://en.wiktionary.org/wiki/Wiktionary:List_of_languages";
		Document doc = Jsoup.connect(path).get();
		extractList(doc,langCode);
		langCode.close();
	}

	private static void extractList(Document doc, BufferedWriter langCode) throws Exception {
		Elements tables=doc.getElementsByTag("table");
		System.out.println(tables.size()); //should be 28
		for(Element table : tables){
			Elements trs=table.getElementsByTag("tr");
			for(Element tr : trs){
				Elements tds=tr.getElementsByTag("td");
				/*estrae i langCode*/
				if(tds.size()>1){
				//	String td=tds.get(1).html();
					Element td=tds.get(1);
					Elements texts=td.getElementsByTag("a");
					for(Element text : texts)
					langCode.write(text.html());
					
					//System.out.println(tds.get(1));
				}
				if(tds.first()!=null){
					String td=tds.first().html();
					String[] split1=td.split("</");
					String tosplit=split1[0];
					String[] split2=tosplit.split(">");
					String langCod=split2[1];
					langCode.write("\t"+langCod);
				}
				langCode.newLine();

			}
			
			
		}
	}

}

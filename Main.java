package it.uniroma1.sapienza.project;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import it.uniroma1.lcl.wimmp.MorphoEntry;
import it.uniroma1.lcl.wimmp.MorphoEntryIterator;
import it.uniroma1.lcl.wimmp.MorphoForm;
import it.uniroma1.sapienza.project.parserxml.ParserXml;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class Main {

	static BufferedWriter writer;
	static BufferedReader input;
	static BufferedReader readlang;

	public static void main(String[] args) throws Exception{
		input=new BufferedReader(new FileReader("inputC.txt"));
		writer = new BufferedWriter(new FileWriter(new File("output.txt")));
		readlang=new BufferedReader(new FileReader("langC.txt"));
		HashSet<String> listWords= new HashSet<String>();
		//leggo una sola parola per vedere se funziona
		//parse("star",writer); //provare anche prendre
		int i=0;
		while(i<220){//Quando farò girare tuttowhile(input.readLine()!=null){
			String line=input.readLine();
			String[] wordsplit=line.split("#");
			String word=wordsplit[0];
			if(!listWords.contains(word)){
				if(!word.contains(":")){
					listWords.add(word);
					parse(word,writer);
					writer.newLine();
				}
			}
			i++;
		}
		writer.close();
	}
	/*Parser html*/
	private static void parse(String word, BufferedWriter writer) throws Exception {
		String path="https://en.wiktionary.org/wiki/"+word;
		Document doc = Jsoup.connect(path).get();
		System.out.println("Page "+word);
		String[] tosplit=doc.title().split("-");
		writer.write(tosplit[0].toUpperCase());
		writer.newLine();
		ExtractorDefinition ex=new ExtractorDefinition(doc,writer);
		ex.extractInfo(doc, writer);
	}



}

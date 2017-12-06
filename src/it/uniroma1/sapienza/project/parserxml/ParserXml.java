package it.uniroma1.sapienza.project.parserxml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import it.uniroma1.lcl.wimmp.MorphoEntry;
import it.uniroma1.lcl.wimmp.MorphoEntryIterator;
import it.uniroma1.lcl.wimmp.MorphoForm;

public class ParserXml {

	public static BufferedWriter input;

	public static void createInput() throws Exception{
	//public static void main(String[] args) throws Exception {
		String pathEnWiktionaryXML = "/Users/ale/Documents/Sapienza/Natural language/Hw1/enwiktionary-20160305-pages-articles.xml";
		String[] dumps = new String[]{pathEnWiktionaryXML}; 
		BufferedWriter input=new BufferedWriter(new FileWriter(new File("input.txt")));
		System.out.println("creato file di input");

		Class<?> c = Class.forName("it.uniroma1.sapienza.project.parserxml.GlobalMorphoEntryIterator");
		Class<? extends MorphoEntryIterator> m = c.asSubclass(MorphoEntryIterator.class);

		Constructor<? extends MorphoEntryIterator> constr = m.getConstructor(String[].class);
		MorphoEntryIterator iterator = constr.newInstance(new Object[] { dumps });

		System.out.println("creato iterator");
		int i=0;
		while(iterator.hasNext())
		{
			System.out.println("iterator non nullo");

			MorphoEntry morphologicalEntry = iterator.next();
			//			List<MorphoForm> forms = morphologicalEntry.getRule().getForms();

			input.write(morphologicalEntry.getLemma() + "#" + morphologicalEntry.getPOS());
			System.out.println("scritto");
			//		for (MorphoForm morphologicalForm : forms)
			//		input.write("\t" + morphologicalForm.getForm() + "\t" + morphologicalForm.getInfo());

			input.newLine();
			i++;
		}
		input.close();

	}

}

package it.uniroma1.sapienza.project.parserxml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.xml.sax.SAXException;

import it.uniroma1.lcl.wimmp.MorphoEntry;
import it.uniroma1.lcl.wimmp.MorphoEntryIterator;


public class GlobalMorphoEntryIterator extends MorphoEntryIterator {

		private List<MorphoEntry> myMorphoEntry;
		private XMLStreamReader reader;
		private int index;
		private String title;
		private String tagText;
		
		/**
		 * @param dumps
		 */
		public GlobalMorphoEntryIterator(String[] dump) 
		{
			super(dump);
			myMorphoEntry = new LinkedList<MorphoEntry>();
			index=0;
			try 
			{
				reader = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(dump[0]));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (XMLStreamException e) {
				e.printStackTrace();
			} catch (FactoryConfigurationError e) {
				e.printStackTrace();
			}
			
		}
		
		@Override
		public boolean hasNext()
		{
				
			try 
			{
				if ( index>0 ) return true;
				while((reader != null) && (reader.hasNext()))
				{
					
					switch(reader.next())
					{
					      case XMLStreamConstants.START_ELEMENT: 
					        	switch(reader.getLocalName())
					        	{
						            case "title":
						            	title = reader.getElementText();	
						            	break;
						            
						            case "text":
						            	tagText = reader.getElementText();
						            	EnglishPatterns myEnglishPattern = new EnglishPatterns(title, tagText);
						            	
						            //	if(myEnglishPattern.isEnglish())
						            	//{
						            		MorphoEntry appMorphoEntry;
						            	 	appMorphoEntry = myEnglishPattern.checkMorphoEntry();
						            	 	
						            	 	while(appMorphoEntry != null)
						            	 	{					            	 	
						            	 		myMorphoEntry.add(appMorphoEntry);
							            	 	index++ ;
							            	 	appMorphoEntry = myEnglishPattern.checkMorphoEntry();
						            	 	}
						            	 	if( index >0 ) return true;
						            	 	
						            	//}
						                break;
					        	}
					}
					
				      
				}
				
			} 
			catch (XMLStreamException e) 
			{
				e.printStackTrace();
			}
			return false;
		}
		
		@Override
		public MorphoEntry next()
		{
			index--;
			MorphoEntry m = myMorphoEntry.get(index);
			myMorphoEntry.remove(index);
			
			return m;
		}
		

/*	SAX parser
 * List<MorphoEntryFromXml> al;
	int index=0;
	
	*//**
	 * @param dumps
	 * @throws XMLStreamException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 *//*
	public GlobalMorphoEntryIterator(String[] dumps) throws XMLStreamException, SAXException, IOException, ParserConfigurationException {
		super(dumps);
		
	    SAXParserFactory parserFactor = SAXParserFactory.newInstance();
	    SAXParser parser = parserFactor.newSAXParser();
	    SAXHandler handler = new SAXHandler();
	    parser.parse(new File(super.getDumps()[0]),handler);
	     
	    al=handler.getMorphoEntrylist();
	    
	}

	@Override
	public boolean hasNext() {
		if(index<al.size())
			return true;
		return false;
	}

	@Override
	public MorphoEntry next() {
		MorphoEntryFromXml morpho=al.get(index);
		MorphoEntry me=MorphoEntryUtil.MorphoEntryResult(morpho);
		index++;
		return me;
	}
*/
}

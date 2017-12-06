package it.uniroma1.sapienza.project.parserxml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXHandler extends DefaultHandler {
	private List<MorphoEntryFromXml>  MorphoEntrylist=new ArrayList<MorphoEntryFromXml>();
	private MorphoEntryFromXml morphoEntry;
	private String content = "";

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
		if(qName.equals("page")){
			this.morphoEntry=new MorphoEntryFromXml();
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		/*if(qName.equals("page")){
			if(!this.morphoEntry.getTitle().contains(":")){
				if(this.morphoEntry.getText().contains("{{sl-noun|")){
					String newText=extractInformationSlNoun(this.morphoEntry.getText());
					this.MorphoEntrylist.add(new MorphoEntryFromXml(this.morphoEntry.getTitle(),newText));
				}
				if(this.morphoEntry.getText().contains("{{sl-verb|") 
						|| this.morphoEntry.getText().contains("{{sl-conj")){
					String newText=this.extractInformationSlVerb(this.morphoEntry.getText());
					this.MorphoEntrylist.add(new MorphoEntryFromXml(this.morphoEntry.getTitle(),newText));
				}
			}
		}*/
		if(qName.equals("title")){
			this.morphoEntry.setTitle(content);
			this.content="";
		}

	/*	if(qName.equals("text")){
			this.morphoEntry.setText(content);
			this.content="";
		}*/
		else
			this.content="";
	}

	@Override
	public void characters(char[] ch, int start, int length) 
			throws SAXException {
		content = this.content.concat(String.copyValueOf(ch, start, length).trim());
	}
	/**
	 * @return the morphoEntrylist
	 */
	public List<MorphoEntryFromXml> getMorphoEntrylist() {
		return MorphoEntrylist;
	}
	/**
	 * @param morphoEntrylist the morphoEntrylist to set
	 */
	public void setMorphoEntrylist(List<MorphoEntryFromXml> morphoEntrylist) {
		MorphoEntrylist = morphoEntrylist;
	}
}

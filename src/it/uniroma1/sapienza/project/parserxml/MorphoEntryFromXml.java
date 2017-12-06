package it.uniroma1.sapienza.project.parserxml;

public class MorphoEntryFromXml {
	private String title;
	private String text;
	
	public MorphoEntryFromXml(){
		this.title="";
		this.text="";
	}
	
	public MorphoEntryFromXml(String title,String text){
		this.setTitle(title);
		this.setText(text);
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
}

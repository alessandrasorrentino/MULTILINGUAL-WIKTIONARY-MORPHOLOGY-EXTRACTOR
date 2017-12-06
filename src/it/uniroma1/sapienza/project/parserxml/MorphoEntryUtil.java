package it.uniroma1.sapienza.project.parserxml;

import it.uniroma1.lcl.wimmp.MorphoEntry;
import it.uniroma1.lcl.wimmp.MorphoRule;
import it.uniroma1.lcl.wimmp.MorphoEntry.POS;


public class MorphoEntryUtil {
	public static MorphoEntry MorphoEntryResult(MorphoEntryFromXml morpho){

		POS pos=findEnWiktionaryPOS(morpho.getText());
		MorphoRule morphoRule= new GlobalMorphoRuleNotFind();

	/*	if(pos==POS.NOUN){
				morphoRule= new GlobalMorphoRuleNoun(morpho.getTitle(), morpho.getText());
		}

		if(pos==POS.VERB){
			if(morpho.getText().contains("{{sl-conj-ati-am|"))
				morphoRule= new SloveneMorphoRuleVerbConjAtiAm(morpho.getTitle(),morpho.getText());
			if(morpho.getText().contains("{{sl-conj-ati-em|"))
				morphoRule= new SloveneMorphoRuleVerbConjAtiEm(morpho.getTitle(),morpho.getText());
			if(morpho.getText().contains("{{sl-conj-áti-(j)em|"))
				morphoRule= new SloveneMorphoRuleVerbConjAatiJem(morpho.getTitle(),morpho.getText());
			if(morpho.getText().contains("{{sl-conj-áti-ím|"))
				morphoRule= new SloveneMorphoRuleVerbConjAatiIm(morpho.getTitle(),morpho.getText());
			if(morpho.getText().contains("{{sl-conj-áti-am|"))
				morphoRule= new SloveneMorphoRuleVerbConjAatiAm(morpho.getTitle(),morpho.getText());
			if(morpho.getText().contains("{{sl-conj-áti-ám") 
					|| morpho.getText().contains("{{sl-conj-'ati-'am|"))
				morphoRule= new SloveneMorphoRuleVerbConjAatiAam(morpho.getTitle(),morpho.getText());

			if(morpho.getText().contains("{{sl-conj-éti-(j)em|"))
				morphoRule= new SloveneMorphoRuleVerbConjEtiJem(morpho.getTitle(),morpho.getText());
			if(morpho.getText().contains("{{sl-conj-éti-em|"))
				morphoRule= new SloveneMorphoRuleVerbConjEtiEm(morpho.getTitle(),morpho.getText());
			if(morpho.getText().contains("{{sl-conj-éti-ím|"))
				morphoRule= new SloveneMorphoRuleVerbConjEtiIim(morpho.getTitle(),morpho.getText());
			if(morpho.getText().contains("{{sl-conj-eti-im|"))
				morphoRule= new SloveneMorphoRuleVerbConjEtiIm(morpho.getTitle(),morpho.getText());

			if(morpho.getText().contains("{{sl-conj-iti-im"))
				morphoRule= new SloveneMorphoRuleVerbConjItiIm(morpho.getTitle(),morpho.getText());
			if(morpho.getText().contains("{{sl-conj-íti-im"))
				morphoRule= new SloveneMorphoRuleVerbConjIitiIm(morpho.getTitle(),morpho.getText());
			if(morpho.getText().contains("{{sl-conj-íti-ím"))
				morphoRule= new SloveneMorphoRuleVerbConjIitiIim(morpho.getTitle(),morpho.getText());


			if(morpho.getText().contains("{{sl-conj-ti-em|"))
				morphoRule= new SloveneMorphoRuleVerbConjTiEm(morpho.getTitle(),morpho.getText());
			if(morpho.getText().contains("{{sl-conj-niti-nem|"))
				morphoRule= new SloveneMorphoRuleVerbConjNitiNem(morpho.getTitle(),morpho.getText());
			if(morpho.getText().contains("{{sl-conj-níti-nem|"))
				morphoRule= new SloveneMorphoRuleVerbConjNiitiNem(morpho.getTitle(),morpho.getText());


			if(morpho.getText().contains("{{sl-conj-vati"))
				morphoRule= new SloveneMorphoRuleVerbConjVati(morpho.getTitle(),morpho.getText());
			if(morpho.getText().contains("{{sl-conj-váti"))
				morphoRule= new SloveneMorphoRuleVerbConjVaati(morpho.getTitle(),morpho.getText());
			if(morpho.getText().contains("{{sl-conj-biti"))
				morphoRule= new SloveneMorphoRuleVerbConjBiti(morpho.getTitle(),morpho.getText());
			if(morpho.getText().contains("{{sl-conj-dati"))
				morphoRule= new SloveneMorphoRuleVerbConjDati(morpho.getTitle(),morpho.getText());
			if(morpho.getText().contains("{{sl-conj-jesti"))
				morphoRule= new SloveneMorphoRuleVerbConjJesti(morpho.getTitle(),morpho.getText());
			if(morpho.getText().contains("{{sl-conj-hoteti"))
				morphoRule= new SloveneMorphoRuleVerbConjHoteti(morpho.getTitle(),morpho.getText());
			if(morpho.getText().contains("{{sl-conj-imeti"))
				morphoRule= new SloveneMorphoRuleVerbConjImeti(morpho.getTitle(),morpho.getText());
			if(morpho.getText().contains("{{sl-conj-iti}}"))
				morphoRule= new SloveneMorphoRuleVerbConjIti(morpho.getTitle(),morpho.getText());
			if(morpho.getText().contains("{{sl-conj-vedeti"))
				morphoRule= new SloveneMorphoRuleVerbConjVedeti(morpho.getTitle(),morpho.getText());
			if(!morpho.getText().contains("{{sl-conj"))
				morphoRule=new SloveneMorphoRuleVerbVerb(morpho.getTitle(), morpho.getText());
		}
*/
		return new MorphoEntry(morpho.getTitle(),pos,morphoRule);
	}

	private static POS findEnWiktionaryPOS(String text){
		if(text.contains("{{sl-noun") || text.contains("{{sl-proper noun|"))
			return POS.NOUN;
		if(text.contains("{{sl-verb") || text.contains("{{head|sl|verb") || text.contains("{{sl-conj"))
			return POS.VERB;

		return null;

	}

}

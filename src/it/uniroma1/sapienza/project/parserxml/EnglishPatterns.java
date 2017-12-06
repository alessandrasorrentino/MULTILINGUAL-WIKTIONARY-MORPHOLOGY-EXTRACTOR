package it.uniroma1.sapienza.project.parserxml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.uniroma1.lcl.wimmp.MorphoEntry;
import it.uniroma1.lcl.wimmp.MorphoEntry.POS;
import it.uniroma1.lcl.wimmp.en.EnglishMorphoRuleAdjective;
import it.uniroma1.lcl.wimmp.en.EnglishMorphoRuleNoun;
import it.uniroma1.lcl.wimmp.en.EnglishMorphoRuleVerb;

public class EnglishPatterns {
	private Matcher matcher_line;
	private Matcher app;
	private String lemma;
	private String line;
	
	public EnglishPatterns(String lemma, String input)
	{
		this.lemma = lemma;
		this.matcher_line = Pattern.compile("^(.*)$",Pattern.MULTILINE).matcher(input);
	}

	public boolean isEnglish()
	{
		while(matcher_line.find())
		{
			if(Pattern.compile("^==English==$").matcher(matcher_line.group(1)).find())
				return true;
		}
		return false;
	}
	
	
	public MorphoEntry checkMorphoEntry()
	{	
			
		while(matcher_line.find())
		{	
			line = matcher_line.group(1);
			
			//Other languages
		//	if(Pattern.compile("^==[^=][\\w\\W]*[^=]==$").matcher(line).find())
			//	return null;
			
			
			app = Pattern.compile("(^=+Noun=+$|^=+Verb=+$|^=+Adjective=+$|^=+Adverb=+$)").matcher(line);
			if (app.find())
			{
				String pos=app.group(1);
				if(pos.contains("Noun"))
				{		
					return (new MorphoEntry(lemma, POS.NOUN, new EnglishMorphoRuleAdjective("more "+lemma,"most "+lemma)));

				}
				else if(pos.contains("Verb"))
				{
					return (new MorphoEntry(lemma, POS.VERB, null));
				}
				else if(pos.contains("Adjective"))
				{
					return (new MorphoEntry(lemma, POS.ADJECTIVE, null));
				}
				else if(pos.contains("Adjective"))
				{
					return (new MorphoEntry(lemma, POS.ADJECTIVE, null));
				}
				else if(pos.contains("Adjective"))
				{
					return (new MorphoEntry(lemma, POS.ADJECTIVE, null));
				}
				else if(pos.contains("Adverb"))
				{
					return (new MorphoEntry(lemma, POS.ADVERB, null));
				}
				else if(pos.contains("Pronoun"))
				{
					return (new MorphoEntry(lemma, POS.PRONOUN, null));
				}
				else if(pos.contains("Determiner"))
				{
					return (new MorphoEntry(lemma, POS.DETERMINER, null));
				}
				else if(pos.contains("Article"))
				{
					return (new MorphoEntry(lemma, POS.ARTICLE, null));
				}
				else if(pos.contains("Conjunction"))
				{
					return (new MorphoEntry(lemma, POS.CONJUNCTION, null));
				}
				else if(pos.contains("Interjection"))
				{
					return (new MorphoEntry(lemma, POS.INTERJECTION, null));
				}
				else if(pos.contains("Preposition"))
				{
					return (new MorphoEntry(lemma, POS.PREPOSITION, null));
				}
			}
			
		}
		
		return null;
	}
	
	
	
	public  MorphoEntry adj(String adj)
	{
		if(adj.equals("{{en-adj}}") || adj.equals("{{en-adj|more}}") || adj.equals("{{en-adj|}}"))
		{
			return (new MorphoEntry(lemma, POS.ADJECTIVE, new EnglishMorphoRuleAdjective("more "+lemma,"most "+lemma)));
		}
		return null;	
	}
	
	public  MorphoEntry noun(String noun)
	{
		
		if(noun.equals("{{en-noun}}") || noun.equals("{{en-noun|s}}"))
		{
			return (new MorphoEntry(lemma, POS.NOUN, new EnglishMorphoRuleNoun(lemma+"s")));
			
		}else if(noun.equals("{{en-noun|es}}")){
			return (new MorphoEntry(lemma, POS.NOUN, new EnglishMorphoRuleNoun(lemma+"es")));
			
		}
		return null;
	}
	
	public  MorphoEntry verb(String verb)
	{
		if(verb.equals("{{en-verb}}"))
		{
			return new MorphoEntry(lemma, POS.VERB, new EnglishMorphoRuleVerb(lemma+"s",lemma+"ing",lemma+"ed"));
		}
		
		return null;
	}
}

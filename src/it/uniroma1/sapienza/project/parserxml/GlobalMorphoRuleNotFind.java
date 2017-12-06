package it.uniroma1.sapienza.project.parserxml;

import java.util.ArrayList;
import java.util.List;

import it.uniroma1.lcl.wimmp.MorphoForm;
import it.uniroma1.lcl.wimmp.MorphoRule;

public class GlobalMorphoRuleNotFind implements MorphoRule {
	
	public GlobalMorphoRuleNotFind(){
	}
	
	@Override
	public List<MorphoForm> getForms() {
		// TODO Auto-generated method stub
		ArrayList<MorphoForm> al=new ArrayList<MorphoForm>();
		return al;
	}

}

package org.gk.TokenFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeSource;
import org.gk.Contenxt.SameWordContenxt;

import com.sun.corba.se.spi.orb.StringPair;





public class MyTokenFilter extends TokenFilter{

	private CharTermAttribute cta = null;
	private PositionIncrementAttribute pia = null;
	private AttributeSource.State currentStatue = null;
	private Stack<String> sameWordList= null;
	
	private SameWordContenxt context = null;
	
	public MyTokenFilter(TokenStream input,SameWordContenxt contenxt) {
		super(input);
		cta = this.addAttribute(CharTermAttribute.class);
		pia = this.addAttribute(PositionIncrementAttribute.class);
		sameWordList = new Stack<String>();
		this.context = contenxt;
	}

	@Override
	public boolean incrementToken() throws IOException {
		if(sameWordList.size() > 0){
			String str = sameWordList.pop();
			cta.setEmpty();
			cta.append(str);
			pia.setPositionIncrement(0);
			return true;
		}
		
		if(!this.input.incrementToken()) return false;
		if(addSameWords(cta.toString())){
			currentStatue = captureState();
		}
		return true;
	}
	
	private boolean addSameWords(String name){
		HashSet<String> sws = context.getSameWords(name);
		if(sws == null) return false;
		for(String str:sws){
			sameWordList.push(str);
		}
		return true;
	}

}

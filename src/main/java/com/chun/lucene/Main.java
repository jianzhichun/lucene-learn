package com.chun.lucene;


import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import com.chun.lucene.tokenizer.EnglishPosTagTokenizer;
import com.chun.lucene.tokenizer.EnglishPosTagTokenizerFactory;

public class Main {

	public static void main(String[] args) throws Exception {
		Analyzer ana = CustomAnalyzer.builder(Paths.get("."))
//				   .withTokenizer(StandardTokenizerFactory.class)
				   .withTokenizer(EnglishPosTagTokenizerFactory.class)
//				   .addTokenFilter(StandardFilterFactory.class)
//				   .addTokenFilter(LowerCaseFilterFactory.class)
				   .addTokenFilter(StopFilterFactory.class, "ignoreCase", "false", "words", "stopwords.txt")
				   .build();
		List<String> rs = new ArrayList<>();
		long t1 = System.currentTimeMillis();
		TokenStream ts = ana.tokenStream(null, "I like watching movies , !");
		CharTermAttribute termAttribute = ts.getAttribute(CharTermAttribute.class);   
		ts.reset();
        while (ts.incrementToken()) {   
        	rs.add(termAttribute.toString());                                            
        }
        System.out.println(System.currentTimeMillis()-t1);
        System.out.println(rs);
	}

}

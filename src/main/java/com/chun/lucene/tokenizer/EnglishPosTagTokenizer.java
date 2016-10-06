
package com.chun.lucene.tokenizer;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeFactory;

import com.google.common.collect.Iterables;

/**
 * 
 * @author chun
 *
 */
public class EnglishPosTagTokenizer extends Tokenizer {
	private Iterator<TaggedWord> tagged;
	private TaggedWord currentWord;
	private static MaxentTagger maxentTagger;
	private static Pattern pattern;
	private static boolean isWhiteList;
	private static boolean isStem;
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);

	public EnglishPosTagTokenizer(AttributeFactory factory) {
		super(factory);

	}

	public static void setStem(boolean isStem) {
		EnglishPosTagTokenizer.isStem = isStem;
	}

	public static void setWhiteList(boolean isWhiteList) {
		EnglishPosTagTokenizer.isWhiteList = isWhiteList;
	}


	public static void setPattern(Pattern pattern) {
		EnglishPosTagTokenizer.pattern = pattern;
	}

	public static void setMaxentTagger(MaxentTagger tagger) {
		EnglishPosTagTokenizer.maxentTagger = tagger;
	}

	protected static boolean filterTag(String tag) {
		boolean bool = pattern.matcher(tag).matches();
		return isWhiteList ? bool : !bool;
	}

	@Override
	public final boolean incrementToken() throws IOException {
		int increment = 0;
		for (;;) {
			if (!tagged.hasNext())
				return false;
			currentWord = tagged.next();
			if (filterTag(currentWord.tag()))
				break;
			increment++;
		}

		posIncrAtt.setPositionIncrement(increment);
		termAtt.setEmpty();
		termAtt.append(isStem?Morphology.stemStatic(currentWord.word(), currentWord.tag()).word():currentWord.word());
		return true;
	}

	@Override
	public void close() throws IOException {
		super.close();
		tagged = null;
	}

	@Override
	public void reset() throws IOException {
		super.reset();
		List<List<HasWord>> tokenized = MaxentTagger.tokenizeText(input);
		tagged = Iterables.concat(maxentTagger.process(tokenized)).iterator();

	}

}

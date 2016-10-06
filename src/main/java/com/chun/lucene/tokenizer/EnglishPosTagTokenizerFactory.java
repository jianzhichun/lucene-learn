package com.chun.lucene.tokenizer;

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.tagger.maxent.TaggerConfig;

public class EnglishPosTagTokenizerFactory extends TokenizerFactory {

	private Map<String, String> args;

	public EnglishPosTagTokenizerFactory(Map<String, String> args) throws Exception {
		super(args);
		this.args = args;

	}

	@Override
	public Tokenizer create(AttributeFactory factory) {
		EnglishPosTagTokenizer englishPosTagTokenizer = new EnglishPosTagTokenizer(factory);
		try {
			String modelpath = get(args, "modelfile", "src/main/resources/english-left3words-distsim.tagger");
			String whitelist = get(args, "whitelist");
			String blacklist = get(args, "blacklist");
			EnglishPosTagTokenizer.setStem(getBoolean(args, "isStem",false));
			Pattern pattern = Pattern.compile("^(CC|DT|[LR]RB|MD|POS|PRP|UH|WDT|WP|WP\\$|WRB|\\$|\\#|\\.|\\,|:)$");
			if (StringUtils.isNotBlank(blacklist))
				pattern = Pattern.compile(blacklist);
			if (StringUtils.isNotBlank(whitelist)) {
				pattern = Pattern.compile(whitelist);
				EnglishPosTagTokenizer.setWhiteList(true);
			}
			EnglishPosTagTokenizer.setPattern(pattern);
			EnglishPosTagTokenizer.setMaxentTagger(makeTagger(modelpath));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return englishPosTagTokenizer;
	}

	protected static MaxentTagger makeTagger(String modelFile) throws Exception {
		TaggerConfig config = new TaggerConfig("-model", modelFile);
		return new MaxentTagger(modelFile, config, false);
	}

}

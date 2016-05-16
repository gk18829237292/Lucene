package org.gk.Utils;
import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;


public class AnalyzerUtils {

	public static void displayToken(String str,Analyzer a) {
		try {
			TokenStream stream = a.tokenStream("content",new StringReader(str));
			//����һ�����ԣ�������Ի�������У��������TokenStream����
			CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
			while(stream.incrementToken()) {
				System.out.print("["+cta+"]");
			}
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void displayAllTokenInfo(String str,Analyzer a) {
		try {
			TokenStream stream = a.tokenStream("content",new StringReader(str));
			//λ�����������ԣ��洢��㵥Ԫ֮��ľ���
			PositionIncrementAttribute pia = 
						stream.addAttribute(PositionIncrementAttribute.class);
			//ÿ����㵥Ԫ��λ��ƫ����
			OffsetAttribute oa = 
						stream.addAttribute(OffsetAttribute.class);
			//�洢ÿһ����㵥Ԫ����Ϣ���ִʵ�Ԫ��Ϣ��
			CharTermAttribute cta = 
						stream.addAttribute(CharTermAttribute.class);
			//ʹ�õķִ�����������Ϣ
			TypeAttribute ta = 
						stream.addAttribute(TypeAttribute.class);
			for(;stream.incrementToken();) {
				System.out.print(pia.getPositionIncrement()+":");
				System.out.print(cta+"["+oa.startOffset()+"-"+oa.endOffset()+"]-->"+ta.type()+"\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

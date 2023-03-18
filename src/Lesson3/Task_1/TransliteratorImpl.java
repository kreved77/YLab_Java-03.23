package Lesson3.Task_1;

import java.util.HashMap;
import java.util.Map;

public class TransliteratorImpl implements Transliterator {

    private static final String alphabetRu = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЬЫЪЭЮЯ";
    private static final String alphabetRuToEn = "A,B,V,G,D,E,E,ZH,Z,I,I,K,L,M,N,O,P,R,S,T,U,F,KH,TS,CH,SH,SHCH,IE,Y,,E,IU,IA";
    private Map<Character, String> mapReplacement;

    public TransliteratorImpl() {
        this.mapReplacement = mapReplacementCreator();
    }

    private Map<Character, String> mapReplacementCreator() {
        mapReplacement = new HashMap<>();
        char[] alphabetOriginal = alphabetRu.toCharArray();
        String[] alphabetReplacement = alphabetRuToEn.split(",");
        for (int i = 0; i < alphabetOriginal.length; i++) {
            mapReplacement.put(alphabetOriginal[i], alphabetReplacement[i]);
        }
        return mapReplacement;
    }

    @Override
    public String transliterate(String source) {
        if (source.equals("")) return "";

        StringBuilder result = new StringBuilder();

        for (Character letter : source.toCharArray()){
            result.append(mapReplacement.getOrDefault(letter, String.valueOf(letter)));
        }
        return result.toString();
    }
}

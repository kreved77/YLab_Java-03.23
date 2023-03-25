package Lesson3.Task_1;

import java.util.HashMap;
import java.util.Map;

public class TransliteratorImpl implements Transliterator {

    private static final String alphabetRu = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЫЬЪЭЮЯ";
    private static final String alphabetRuToEn = "A,B,V,G,D,E,E,ZH,Z,I,I,K,L,M,N,O,P,R,S,T,U,F,KH,TS,CH,SH,SHCH,Y,,IE,E,IU,IA";
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

    /*
    or just hardcode instead of all above
     */
//    private Map<Character, String> mapReplacement = Map.of(
//            'А',"A",
//            'Б',"B",
//            'В',"V",
//            'Г',"G",
//            'Д',"D",
//            'Е',"E",
//            'Ё',"E",
//            'Ж',"ZH",
//            'З',"Z",
//            'И',"I",
//            'Й',"I",
//            'К',"K",
//            'Л',"L",
//            'М',"M",
//            'Н',"N",
//            'О',"O",
//            'П',"P",
//            'Р',"R",
//            'С',"S",
//            'Т',"T",
//            'У',"U,",
//            'Ф',"F,",
//            'Х',"KH",
//            'Ц',"TS",
//            'Ч',"CH",
//            'Ш',"SH",
//            'Щ',"SHCH",
//            'Ы',"Y",
//            'Ь',"",
//            'Ъ',"IE",
//            'Э',"ZH",
//            'Ю',"IU",
//            'Я',"IA"
//    );

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

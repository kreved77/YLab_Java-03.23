package Lesson3.Task_1;

public class TransliteratorTest {
    public static void main(String[] args) {
        Transliterator transliterator = new TransliteratorImpl();

        String res = transliterator
                .transliterate("HELLO! ПРИВЕТ! Go, boy!");  // HELLO! PRIVET! Go, boy!
        System.out.println(res);

        System.out.println(transliterator.transliterate(""));   // ""

        res = transliterator.transliterate("123! АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЬЫЪЭЮЯ! $#, {_+=}!");
        System.out.println(res);         // 123! ABVGDEEZHZIIKLMNOPRSTUFKHTSCHSHSHCHIEYEIUIA! $#, {_+=}!

        System.out.println(transliterator.transliterate("СЛУЧАЙНО НАЖАЛИ \"caps lock\" И ВВЕЛИ ТЕКСТ, НЕ ПРОБЛЕМА!"));
                                                      // SLUCHAINO NAZHALI "caps lock" I VVELI TEKST, NE PROBLEMA!
    }
}
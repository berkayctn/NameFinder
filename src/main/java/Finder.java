import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


public class Finder {
    public static void main(String[] args) throws IOException {
        //Reading body section(html) with Using Jsoup then casting String type by using given URL

        Document doc = Jsoup.connect(args[0]).get();
        Elements elements = doc.select("body");
        String list2 = doc.select("body").text();


        InputStream modelIn = new FileInputStream(new File("src\\main\\resources", "en-token.bin"));
        TokenizerModel model = new TokenizerModel(modelIn);
        Tokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(list2);

        // For tokenized items
//        for (int i = 0; i < tokens.length; i++) {
//            System.out.println(tokens[i]);
//        }

        InputStream modelIn2 = new FileInputStream(new File("src\\main\\resources", "en-sent.bin"));
        SentenceModel model2 = new SentenceModel(modelIn2);
        SentenceDetectorME sentenceDetector = new SentenceDetectorME(model2);
        String sentences[] = sentenceDetector.sentDetect(list2);

        // For all sentences
//        for (int i = 0; i < sentences.length; i++) {
//            System.out.println(sentences[i]);
//        }


        //For names-surnames only
        InputStream modelIn3 = new FileInputStream(new File("src\\main\\resources", "en-ner-person.bin"));
        TokenNameFinderModel model3 = new TokenNameFinderModel(modelIn3);
        NameFinderME nameFinder = new NameFinderME(model3);

        String sentence[] = tokenizer.tokenize(String.valueOf(elements));
        Span nameSpans[] = nameFinder.find(sentence);
        System.out.println(Arrays.toString(Span.spansToStrings(nameSpans,sentence)));

    }
}

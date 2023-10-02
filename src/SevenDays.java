import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SevenDays {

    public static void main(String[] args) throws Exception {

        System.out.println("Chamando API");
        String apiKey = "<APIKEY>";
        String json = new ImdbApiClient(apiKey).getBody();

        System.out.println("Parsing do Json");
        JsonParser jsonParser = new ImdbMovieJsonParser(json);
        List<? extends Content> contentList = new ImdbMovieJsonParser(json).parse();

        Collections.sort(contentList, Comparator.comparing(Content::year));

        System.out.println("gerando Html");
        PrintWriter writer = new PrintWriter("content.html");
        new HtmlGenerator(writer).generate(contentList);
        writer.close();
    }
}

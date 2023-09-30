import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
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
        List<Movie> movies  = new ImdbMovieJsonParser(json).parse();

        System.out.println("gerando Html");
        PrintWriter writer = new PrintWriter("content.html");
        new HtmlGenerator(writer).generate(movies);
        writer.close();
    }
}

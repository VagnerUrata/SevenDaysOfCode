import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MarvelSerieJsonParser implements JsonParser{

	private String json;

	public MarvelSerieJsonParser(String json) {
		this.json = json;
	}

	public List<Serie> parse() {
		String[] seriesArray = parseJsonSeries(this.json);
		
		List<Serie> series = new ArrayList<>();
		for (int i = 0; i < seriesArray.length; i++) {
			String titleValue = parseAttribute(seriesArray[i], "title");
			String yearValue = parseAttribute(seriesArray[i], "startYear");
			String ratingValue = parseAttribute(seriesArray[i], "rating");
			
			if(ratingValue.isEmpty()) {
				ratingValue = "Sem";
			}
			String thumbnailValue = parseThumbnailAttribute(seriesArray[i]);
			series.add(new Serie(titleValue, thumbnailValue, ratingValue, yearValue));
		}

		return series;
	}

	//"thumbnail":{"path":"http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available","extension":"jpg"},
	private String parseThumbnailAttribute(String jsonSerie) {
		
		Pattern pattern = Pattern.compile("\"thumbnail\":\\{\"path\":\"");
		Matcher matcher = pattern.matcher(jsonSerie);
		
		if(!matcher.find()) {
			throw new IllegalStateException("Thumbnail não encontrado");
		}
		
		int posIniAttribute = matcher.end();
		String thumbnail_ext = jsonSerie.substring(posIniAttribute);
		
		pattern = Pattern.compile("\",\"extension\":\"");
		matcher = pattern.matcher(thumbnail_ext);

		if(!matcher.find()) {
			throw new IllegalStateException("Thumbnail extension não encontrado");
		}
		
		posIniAttribute = matcher.start();
		String thumbnail = thumbnail_ext.substring(0 , posIniAttribute);
		
		String ext = thumbnail_ext.substring(matcher.end(), matcher.end() + 3);

		return cleanUp(thumbnail) + "." + ext;
	}

	private String parseAttribute(String jsonSerie, String atributeName) {
		
		int posIniAttribute = findInitialPositionOfAttribute(jsonSerie, atributeName);
		jsonSerie = jsonSerie.substring(posIniAttribute);
		
		int posEndAttribute = findFinalPositionOfAttribute(jsonSerie, atributeName);
		String attributeValue = jsonSerie.substring(0 , posEndAttribute);
		
		String value = cleanUp(attributeValue);
		
		return value;
	}

	private int findFinalPositionOfAttribute(String jsonSerie, String atributeName) {
		Pattern endPattern = Pattern.compile(",");
		Matcher endMatcher = endPattern.matcher(jsonSerie);
		
		if(!endMatcher.find()) {
			throw new IllegalStateException(atributeName + " não encontrado");
		}
		int posEndAttribute = endMatcher.start();
		return posEndAttribute;
	}

	private int findInitialPositionOfAttribute(String jsonSerie, String atributeName) {
		Pattern beginPattern = Pattern.compile("\"" + atributeName + "\":");
		
		Matcher beginMatcher = beginPattern.matcher(jsonSerie);
		if(!beginMatcher.find()) {
			throw new IllegalStateException(atributeName + " não encontrado");
		}
		
		int posIniAttribute = beginMatcher.end();
		return posIniAttribute;
	}

	private static String cleanUp(String attributeValue) {
		if(attributeValue.startsWith("\"")) {
			attributeValue = attributeValue.substring(1);
		}
		
		if(attributeValue.endsWith(",")) {
			attributeValue = attributeValue.substring(0, attributeValue.length() - 1);
		}
		
		if(attributeValue.endsWith("\"")) {
			attributeValue = attributeValue.substring(0, attributeValue.length() - 1);
		}
		
		return attributeValue.trim();
	}

	static Pattern BEGIN_ARRAY = Pattern.compile(".*\"results\":");
	static Pattern END_ARRAY = Pattern.compile(".*\\]}}");

	private String[] parseJsonSeries(String body) {
		
		Matcher matcher = BEGIN_ARRAY.matcher(body);
		matcher.find();
		int begin = matcher.end();
		
		matcher = END_ARRAY.matcher(body);
		matcher.find();
		int end = matcher.end();
		
		String jsonStringSeries = body.substring(begin, end);
		
		String[] jsonMovies = jsonStringSeries.split("\\},\\{\"id\"");
		return jsonMovies;
	}
}
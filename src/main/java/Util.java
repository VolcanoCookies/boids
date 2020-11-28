import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Util {
	
	public static Boolean[][] parseFileToMap(String relativePath) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(Util.class.getResourceAsStream(relativePath)));
		String line = reader.readLine();
		List<List<Boolean>> matrix = new ArrayList<>();
		while (line != null) {
			matrix.add(line.chars()
					.mapToObj(c -> c == 'x')
					.collect(Collectors.toList()));
			line = reader.readLine();
		}
		Boolean[][] booleans = new Boolean[matrix.size()][];
		for (int i = 0; i < matrix.size(); i++) {
			booleans[i] = matrix.get(i).toArray(new Boolean[0]);
		}
		return booleans;
	}
	
}

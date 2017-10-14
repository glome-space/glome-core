package space.glome.http.schema;

import java.io.IOException;
import java.util.Scanner;

public class TestMarshallingBase {

	 String readFile(String filePath) throws IOException {
		try (Scanner scanner = new Scanner(TestMarshallingRequestItem.class.getResourceAsStream(filePath), "UTF-8")) {
			return scanner.useDelimiter("\\A").next();
		}
	}
}

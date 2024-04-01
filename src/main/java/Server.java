import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Server {
	public static void main(String[] args) throws IOException {

		System.out.println("Sudoku Server Started");
		HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
		server.createContext("/api/generate", (exchange -> {

			//Generate Sudoku grid numbers
			if ("GET".equals(exchange.getRequestMethod())) {
				Sudoku sudokuObj = new Sudoku();

				int[][] inputBoard = { { 0, 0, 0, 0, 0, 0, 0, 0, 3 }, { 5, 7, 0, 0, 0, 9, 0, 2, 1 },
						{ 0, 0, 2, 0, 0, 0, 0, 7, 0 }, { 9, 0, 0, 0, 0, 4, 0, 0, 0 }, { 6, 0, 0, 7, 0, 0, 3, 5, 0 },
						{ 7, 0, 0, 0, 8, 1, 0, 0, 0 }, { 0, 6, 0, 0, 1, 0, 2, 0, 0 }, { 2, 0, 0, 0, 0, 0, 0, 4, 5 },
						{ 0, 0, 1, 4, 0, 5, 0, 0, 0 } };
				String responseText = sudokuObj.printBoard(inputBoard);
				exchange.sendResponseHeaders(200, responseText.getBytes().length);
				OutputStream output = exchange.getResponseBody();
				output.write(responseText.getBytes());
				output.flush();
			} else {
				exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
			}
			exchange.close();
		}));

		//Validate user entered sudoku numbers
		server.createContext("/api/validate", (exchange -> {

			if ("POST".equals(exchange.getRequestMethod())) {
				System.out.println(exchange.getRequestBody());

				InputStream ios = exchange.getRequestBody();
				Sudoku sudokuObj = new Sudoku();
				Sudoku sudokuCloneObj = new Sudoku();

				int[][] sudokuEntries = new int[9][9];

				StringBuilder sb = new StringBuilder();

				int i;
				while ((i = ios.read()) != -1) {
					sb.append((char) i);
				}
				System.out.println("hm: " + sb.toString());

				Object obj;
				try {
					obj = new JSONParser().parse(sb.toString());

					JSONObject jsonObject = (JSONObject) obj;
					JSONArray inputArray = (JSONArray) jsonObject.get("input");

					for (int ij = 0; ij < inputArray.size(); ij++) {
						String data = inputArray.get(ij).toString();
						data = data.replace("[", "");
						data = data.replace("]", "");

						String[] formatedArray = data.toString().split(",");
						int[] subEntry = new int[formatedArray.length];
						for (int k = 0; k < formatedArray.length; k++) {
							subEntry[k] = Integer.parseInt(formatedArray[k]);
						}
						sudokuEntries[ij] = subEntry;
					}

				} catch (ParseException e) {
					e.printStackTrace();
				}

				int[][] sudokuEntriesRequestData = Arrays.stream(sudokuEntries).map(int[]::clone).toArray(int[][]::new);

				sudokuObj.solveSudokuPuzzle(sudokuEntries);

				String responseText = sudokuCloneObj.printBoard(sudokuEntriesRequestData);

				System.out.println("\n responseText: " + responseText);
				if (responseText.contains("0"))
					responseText = "\n Valid Entry: " + sudokuObj.sudokuSolved;
				else
					responseText = "\n Sudoku Solved: " + sudokuObj.sudokuSolved;

				System.out.println("\n sudokuSolved: " + sudokuObj.sudokuSolved);

				exchange.sendResponseHeaders(200, responseText.getBytes().length);
				OutputStream output = exchange.getResponseBody();
				output.write(responseText.getBytes());
				output.flush();
			} else {
				exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
			}
			exchange.close();
		}));

		server.setExecutor(null); // creates a default executor
		server.start();

	}
}

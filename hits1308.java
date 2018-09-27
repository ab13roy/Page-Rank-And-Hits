import java.io.*;

public class hits1308{

	public static int findVertices(String filename){
		int vertices = 0;
		try{
			BufferedReader file = new BufferedReader(new FileReader(filename));
			String[] firstLine = file.readLine().split(" ");
			vertices = Integer.parseInt(firstLine[0]);
			file.close();
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return vertices;
	}


	public static void main(String... args){
		if (args.length == 3) {
			int iterations = Integer.parseInt(args[0]);
			int initialvalue = Integer.parseInt(args[1]);
			String filename = args[2];

			hitsMain1308 obj1 = new hitsMain1308(iterations, initialvalue, filename);

			obj1.setupVectors(findVertices(filename));
			obj1.initializeVectors(initialvalue);
			obj1.setAdmatrix();
			obj1.setErrorRate(iterations);
			obj1.hitsAlgorithm(iterations, findVertices(filename));
			System.exit(0);
		}
		else {
			System.out.println("Invalid command line arguments");
			System.out.println("Please run again");
			System.exit(0);
		}
	}
}
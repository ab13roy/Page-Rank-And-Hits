import java.io.*;
public class hitsMain1308{

	private int iterations;     //storing the number of iterarions
	private int initialvalue;   //storing the value of initialvalue
	private int vertices;      // number of vertices in the graph
	private int edges;      // number of edges in the graph
	private int [][] admatrix;  // adjacency matrix
	//private int [][] transAdmatrix;   // tanspose of the adjacency matrix
	private double[] authVector;    //storing the authority values for each vertices 
	private double[] authVector1;   //secondary vector used for computation and accuracy purpose
	private String filename;        //variable to hold the name of the text file
	private double [] hubVector;    //storing the hub values for each vertices
	private double [] hubVector1;   //secondary vector used for computation and accuracy purpose
	private double errorrate = 0.00001;  //default error rate
	private double aScale;
	private double hScale;
	private double aSumSqr;
	private double hSumSqr;

	hitsMain1308(int iterations, int initialvalue, String filename){
	  this.initialvalue = initialvalue;
	  this.iterations = iterations;
	  this.filename = filename;
	}

	public void setupVectors(int varg1){
		vertices = varg1;
		authVector = new double[vertices];
		authVector1 = new double[vertices];
		hubVector = new double[vertices];
		hubVector1 = new double[vertices];
		admatrix = new int [vertices][vertices];
	}

	public void initializeVectors(int initialvalue){
		switch (initialvalue) {
		  case 1:
		  for (int i = 0; i < vertices ; i++ ) {
		    hubVector[i] = 1.0;
		    authVector[i] = 1.0;
		  }
		  break;
		  case -1:
		  for (int i = 0; i < vertices ; i++ ) {
		    hubVector[i] = 1.0 / vertices;
		    authVector[i] = 1.0 / vertices;
		  }
		  break;
		  case -2:
		  for (int i = 0; i < vertices ; i++ ) {
		    hubVector[i] = 1.0 / Math.sqrt(vertices);
		    authVector[i] = 1.0 / Math.sqrt(vertices);
		  }
		  break;
		  default:
		  for (int i = 0; i < vertices ; i++ ) {
		    hubVector[i] = 0.0;
		    authVector[i] = 0.0;
		  }
		  break;
		}
	}

	public void printBaseCase(){
	  System.out.println();
	  System.out.print("Base    :0 :");
	  for (int i = 0; i < vertices ; i++ ) {
	    System.out.printf("    A/H[ %d] = %.7f/%.7f" , i, authVector[i], hubVector[i]);
	  }
	  System.out.println();
	}

	public boolean checkErrorRate(){
	  for (int i = 0; i < vertices ; i++ ) {
	    double temp;
	    double temp1;
	    temp = authVector[i] - authVector1[i];
	    temp1 = hubVector[i] - hubVector1[i];
	    if( (Math.abs(temp) > errorrate) && (Math.abs(temp1)) > errorrate ){
	      return false;
	    }
	    else {
	    	return true;
	    }
	  }
	  return true;
	}

	public void setAdmatrix(){
		int temp1, temp2;
		String line;
		try{
			BufferedReader file = new BufferedReader(new FileReader(filename));
			file.readLine();

			while((line = file.readLine()) != null){
				String[] linebreak = line.split(" ");
				//System.out.println(linebreak);
				temp1 = Integer.parseInt(linebreak[0]);
				temp2 = Integer.parseInt(linebreak[1]);
				//System.out.println(temp1 + "/" + temp2);
				admatrix[temp1][temp2] = 1;
				//System.out.println(admatrix);
			}
			file.close();
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			//System.out.println(e.printStackTrace());
			System.exit(0);
		}

		/*for (int i = 0; i < vertices ; i++ ) {
			for (int j = 0; j < vertices ; j++ ) {
				System.out.println(admatrix[i][j] + "/" + i + "/" + j);
			}
		}*/
	}

	public double setErrorRate(int iterations){
	  if (iterations == 0 || iterations == -5) {
	    errorrate = Math.pow(10, -5);
	    return errorrate;
	  }
	  else if (iterations == -1) {
	    errorrate = Math.pow(10, -1);
	    return errorrate;
	  }
	  else if (iterations == -2) {
	    errorrate = Math.pow(10, -2);
	    return errorrate;
	  }
	  else if (iterations == -3) {
	    errorrate = Math.pow(10, -3);
	    return errorrate;
	  }
	  else if (iterations == -4) {
	    errorrate = Math.pow(10, -4);
	    return errorrate;
	  }
	  else {
	    errorrate = Math.pow(10, -5);
	    return errorrate;
	  }
	}

	public void computeVariablesandVector(){
		for (int i = 0; i < vertices ; i++ ) {
		  aSumSqr += authVector[i] * authVector[i];
		  hSumSqr += hubVector[i] * hubVector[i];
		}
		aScale = Math.sqrt(aSumSqr);
		hScale = Math.sqrt(hSumSqr);
		for (int i = 0; i < vertices ; i++ ) {
		  authVector[i] = authVector[i] / aScale;
		  hubVector[i] = hubVector[i] / hScale;
		}
	}

	public void copyVectors(){
		for (int i = 0; i < vertices ; i++ ) {
		  authVector1[i] = authVector[i];
		  hubVector1[i] = hubVector[i];
		}
	}

	public void updateAuth(){
		for (int i = 0; i < vertices ; i++ ) {
		  for (int j = 0; j < vertices ; j++ ) {
		    if (admatrix[j][i] == 1) {
		      authVector[i] += hubVector[j];
		    }
		  }
		}
	}

	public void updateHub(){
		for (int i = 0; i < vertices ; i++ ) {
		  for (int j = 0; j < vertices ; j++ ) {
		    if (admatrix[i][j] == 1) {
		      hubVector[i] += authVector[j];
		    }
		  }
		}
	}

	public void resetAuth(){
		for (int i = 0; i < vertices ; i++ ) {
		 authVector[i] = 0; 
		}
	}

	public void resetHub(){
		for (int i = 0; i < vertices ; i++ ) {
		 hubVector[i] = 0; 
		}
	}

	public void resetVariables(){
		double aScale = 0.0;
		double hScale = 0.0;
		double aSumSqr = 0.0;
		double hSumSqr = 0.0;
	}

	public void printUntilNoError(){
	  int counter = 0;
	  boolean flag = false;
	  while(!flag){
	    resetVariables();

	    copyVectors();
	    resetAuth();
	    updateAuth();
	    resetHub();
	    updateHub();

	    computeVariablesandVector();
	    
	    counter++;
	    System.out.print("Iter    :" + (counter ) + " :");
	    
	    for (int i = 0; i < vertices ; i++ ) {
	      System.out.printf("    A/H[  %d] = %.7f/%.7f" , i, authVector[i], hubVector[i]);
	    }
	    System.out.println();
	    flag = checkErrorRate();
	  }
	}

	public void printIterationsForLarge(){
	  int counter = 0;
	  boolean flag = false;
	  while(!flag){
	    resetVariables();
	    
	    copyVectors();
	    resetAuth();
	    updateAuth();
	    resetHub();
	    updateHub();

	    computeVariablesandVector();

	    counter++;
	    flag = checkErrorRate();
	  }
	  System.out.print("Iter    :" + (counter ) + " :");
	  System.out.println();
	    for (int i = 0; i < vertices ; i++ ) {
	      System.out.printf("A/H[  %d] = %.7f/%.7f" , i, authVector[i], hubVector[i]);
	      System.out.println();
	    }
	}

	public void printForLimitedIterations(int iterations){	
		int counter = 0;
		boolean flag = false;
		while(counter < iterations){
		    resetVariables();

		    copyVectors();
		    resetAuth();
		    updateAuth();
		    resetHub();
		    updateHub();

		    computeVariablesandVector();

		    counter++;
		    System.out.print("Iter    :" + (counter ) + " :");
		    for (int i = 0; i < vertices ; i++ ){
		      System.out.printf("    A/H[  %d] = %.7f/%.7f" , i, authVector[i], hubVector[i]);
		    }
		    System.out.println();
		    flag = checkErrorRate();
		}
	}


	public void hitsAlgorithm(int iterations, int vertices){
	  if(vertices > 10){
	    printIterationsForLarge();
	    System.exit(0);
	  }

	  if (iterations > 0) {
	    printBaseCase();
	    printForLimitedIterations(iterations);
	    System.exit(0);
	  }
	  else {
	    printBaseCase();
	    printUntilNoError();
	    System.exit(0);
	  }
	}
}

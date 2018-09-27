//Abhishek Baidaroy CS610 1308 PRP
import java.util.*;
import java.io.*;
import java.lang.*;
import static java.lang.Math.*;

public class pageRank1308 {

        int iterations;     //storing the number of iterarions
        int initialvalue;   //storing the value of initalvalue
        int vertices;      // number of vertices in the graph
        int edges;      // number of edges in the graph
        int[][] admatrix;  // adjacency matrix
        double[] ranks;    //storing the ranks of the vertices 
        int[] outbounds; // array to contain number of outgoing links for page 'Ti'
        String filename;  //variable to hold the name of the text file
        double [] finalRanks; //secondary vector used for computation and accuracy purpose
        final double damp = 0.85;   //default damping value
        double errorrate = 0.00001;  //default error rate

        pageRank1308(int iterations, int initialvalue, String filename){
          this.initialvalue = initialvalue;
          this.iterations = iterations;
          this.filename = filename;

          try{
            Scanner scanfile = new Scanner(new File(filename));
            this.vertices = scanfile.nextInt();
            this.edges = scanfile.nextInt();

            //initalizing the matrix to 0s
            admatrix = new int[vertices][vertices];
            for(int i = 0; i < vertices; i++){
              for(int j = 0; j < vertices; j++){
                admatrix[i][j] = 0;
              }
            }

            //initializing the outbounds array to 0
            outbounds = new int[vertices];
            for(int i = 0; i < vertices; i++){
              outbounds[i] = 0;
            }

            //initialzing the matrix to the graph values
            do{
              int i = scanfile.nextInt();
              int j = scanfile.nextInt();

              admatrix[i][j] = 1;

            }while(scanfile.hasNextInt());
            //setting the outbound links for each vertices
            for(int i = 0; i < vertices; i++){
              for(int j = 0; j < vertices; j++){
                if(admatrix[i][j] == 1){
                  outbounds[i]++; 
                }
              }
            }
          }
          catch(FileNotFoundException e){
            System.out.println(e.getMessage());
          }

          ranks = new double[vertices];
          finalRanks = new double [vertices];
          //setting the initial ranks of the vertices
          if(initialvalue == 0){
            for(int i = 0; i < vertices;i++){
              ranks[i] = 0;
            }
          }
          else if(initialvalue == 1){
            for(int i = 0; i < vertices; i++){
              ranks[i] = 1;
            }
          }
          else if(initialvalue == -1){
            for(int i = 0; i < vertices; i++){
              ranks[i] = 1.0 / vertices;
            }
          }
          else{
            for (int i = 0; i < vertices ; i++ ) {
              ranks[i] = 1.0 / Math.sqrt(vertices);
            }
          }
        }

        //func that checks for accurate results
        public boolean checkErrorRate(int iterations, double [] finalRanks){
          if(iterations == 0 || iterations == -5){
            errorrate = 0.00001;
          }
          else if (iterations == -1) {
            errorrate = 0.1;
          }
          else if (iterations == -2) {
            errorrate = 0.01;
          }
          else if (iterations == -3) {
            errorrate = 0.001;
          }
          else if (iterations == -4) {
            errorrate = 0.0001;
          }
          else if (iterations == -6){
            errorrate = 0.000001;
          }
          else {
            errorrate = 0.00001;
          }

          for (int i = 0; i < vertices ; i++ ) {
            double temp;
            temp = ranks[i] - finalRanks[i];
            if(Math.abs(temp) > errorrate){
              return false;
            }
            else {
              return true;
            }
          }
          return false;
        }

        //func that computes page ranks
        public void computePageRanks(double [] finalRanks, int iterations){
          System.out.println();
          while (!(checkErrorRate(iterations, finalRanks))) {
            for (int i = 0; i < iterations ; i++){
              System.out.print("Iter    : " + (i+1));
              for(int l = 0; l < vertices; l++) {
                double temp;
                finalRanks[l] = (damp * finalRanks[l]) + (1-damp) / vertices;
                temp = (finalRanks[l] * 1000000.0) / 1000000.0;
                finalRanks[l] = temp;
                System.out.printf(" :P[" + " %d" + "] = %.7f" , l, temp);
                ranks[l] = finalRanks[l];
              }
              System.out.println();
            }
          }
        }

        //func that computes page ranks until no errors are recorded
        public void computePageRanksUntilNoError(double [] finalRanks, int iterations){
          boolean flag = false;
          int i = 0;
          System.out.println();
          while (!flag) {
            System.out.print("Iter    : " + (i+1));
            for(int l = 0; l < vertices; l++) {
              double temp;
              finalRanks[l] = (damp * finalRanks[l]) + (1-damp) / vertices;
              temp = (finalRanks[l] * 1000000.0) / 1000000.0;
              System.out.printf(" :P[" + " %d"+ "]= %.7f" ,l,temp);
             }
             i++;
             flag = checkErrorRate(iterations, finalRanks);
             for (int j = 0; j < vertices ; j++) {
               ranks[j] = finalRanks[j];
             }
             System.out.println();
           }
        }

        //func that operates only on large graphs ie for n > 10
        public void printIterationsForLarge(double [] finalRanks){
          boolean flag = false;
          int i = 0;
          while (!flag) {
            for(int l = 0; l < vertices; l++) {
              finalRanks[l] = (damp * finalRanks[l]) + (1-damp) / vertices;
             }
             i++;
             flag = checkErrorRate(iterations, finalRanks);
             for (int j = 0; j < vertices ; j++) {
               ranks[j] = finalRanks[j];
             }
          }
          System.out.println("Iter     :" + i);
          for (int j = 0; j < vertices ; j++ ) {
           double temp;
           temp = (finalRanks[j] * 1000000.0) / 1000000.0;
           System.out.printf("P[ " + " %d" + " ] = %.7f" , j, temp);
           System.out.println();
          }
        }

        //func that prints the base values        
        public void printBaseCase(double [] ranks){
          System.out.println();
          System.out.print("Base    : 0 :");
          for (int i = 0; i < vertices ; i++ ) {
            double temp;
            temp = (ranks[i] * 1000000.0) / 1000000.0;
            System.out.printf("P[  "+ " %d" + "] = %.7f", i, temp);
          }
        }

        //core func that invokes other funcs
        public void pageRankAlgorithm(int iterations){
          for (int i = 0; i < vertices ; i++) {
            finalRanks[i] = 0;
          }

          if(vertices > 10){
            iterations = 0;
            initialvalue = -1;
            for(int i = 0; i < vertices; i++){
              ranks[i] = 1.0 / vertices;
            }

            for (int i = 0; i < vertices ; i++ ) {
              for (int j = 0; j < vertices ; j++ ) {
                if (admatrix[i][j] == 1) {
                  finalRanks[j] += ranks[j] / outbounds[j];
                }
              }
            }

            for (int i = 0; i < vertices ; i++ ) {
              ranks[i] = finalRanks[i];
            }

            computePageRanks(finalRanks, iterations);

            for (int i = 0; i < vertices ; i++) {
              finalRanks[i] = ranks[i];
            }

            printIterationsForLarge(finalRanks);            
            System.exit(0);
          }

          if (iterations > 0) {
            printBaseCase(ranks);

            for (int i = 0; i < vertices ; i++ ) {
              for (int j = 0; j < vertices ; j++ ) {
                if (admatrix[i][j] == 1) {
                  finalRanks[j] += ranks[j] / outbounds[j];
                }
              }
            }

            computePageRanks(finalRanks, iterations);
            System.exit(0);
          }
          else {
            printBaseCase(ranks);

            for (int i = 0; i < vertices ; i++ ) {
              for (int j = 0; j < vertices ; j++ ) {
                if (admatrix[i][j] == 1) {
                  finalRanks[j] += ranks[j] / outbounds[j];
                }
              }
            }
            computePageRanksUntilNoError(finalRanks, iterations);
            System.exit(0);
          }
        }

        public static void main(String... args) throws IOException{
          if(args.length == 3){
            int iterations;
            int initialvalue;
            String filename;

            iterations = Integer.parseInt(args[0]);
            initialvalue = Integer.parseInt(args[1]);
            filename = args[2];

            pageRank1308 objc1 = new pageRank1308(iterations, initialvalue, filename);
            objc1.pageRankAlgorithm(iterations);

          }
          else{
            System.out.println("Invalid Usage of CommandLine Arguments");
            System.out.println("Please enter 3 Arguments: classFile interations initialvalue filename ");
            return;
          }
        }
 
}
import java.io.IOException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;
//export JVM_ARGS="-Xms1024m -Xmx1024m";

public class sequentialCloudData {

    
    Vector [][][] advection; // in-plane regular grid of wind vectors, that evolve over time
    float [][][] convection; // vertical air movement strength, that evolves over time
    int [][][] classification; // cloud type per grid point, evolving over time
    int dimx, dimy, dimt; // data dimensions
    float xAverage, yAverage;
    float xSum = 0;// sum of x values of wind    
    float ySum = 0;// sum of y values of wind
    float sumX = 0f;
    float sumY = 0f;
    int extension = 8;
    float xxAverage, yyAverage;
    // overall number of elements in the timeline grids
    int dim(){
        return dimt*dimx*dimy;
    }

    // convert linear position into 3D location in simulation grid
    void locate(int pos, int [] ind)
    {
        ind[0] = (int) pos / (dimx*dimy); // t
        ind[1] = (pos % (dimx*dimy)) / dimy; // x
        ind[2] = pos % (dimy); // y
    }

    // read cloud simulation data from file
    void readData(String fileName){
        try{
            Scanner sc = new Scanner(new File(fileName), "UTF-8");

            dimt = sc.nextInt();
            dimx = sc.nextInt();
            dimy = sc.nextInt();

            // initialize and load advection (wind direction and strength) and convection
            advection = new Vector[dimt][dimx][dimy];
            convection = new float[dimt][dimx][dimy];
            for(int t = 0; t < dimt; t++) {
                for (int x = 0; x < dimx; x++) {
                    for (int y = 0; y < dimy; y++) {
                        advection[t][x][y] = new Vector();
                        advection[t][x][y].setX(Float.parseFloat(sc.next().trim()));
                        advection[t][x][y].setY(Float.parseFloat(sc.next().trim()));
                        convection[t][x][y] = Float.parseFloat(sc.next().trim());
                    }
                }
                System.err.printf("\n");
            }

            classification = new int[dimt][dimx][dimy];
            cloudClassification();

            sc.close();
        }
        catch (IOException e){
            System.out.println("Unable to open input file "+fileName);
            e.printStackTrace();
        }
        catch (java.util.InputMismatchException e){
            System.out.println("Malformed input file "+fileName);
            e.printStackTrace();
        }
    }

    // write classification output to file
    void writeData(String fileName){
        try{
            FileWriter fileWriter = new FileWriter(fileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            //fileWriter.close();
            printWriter.printf("%d %d %d\n", dimt, dimx, dimy);

            windAverage();
            printWriter.printf("%f %f\n", xAverage, yAverage);

            for(int t = 0; t < dimt; t++){
                for(int x = 0; x < dimx; x++){
                    for(int y = 0; y < dimy; y++){
                        printWriter.printf("%d ", classification[t][x][y]);
                    }
                }
                printWriter.printf("\n");
            }

            printWriter.close();
        }
        catch (IOException e){
            System.out.println("Unable to open output file "+fileName);
            e.printStackTrace();
        }
    }

    // calculating wind average for x and y
    void windAverage() {
        for(int i = 0; i < dimt; i++){
            for(int j = 0; j < dimx; j++){
                for(int k = 0; k < dimy; k++){
                    xSum += advection[i][j][k].getX();
                    ySum += advection[i][j][k].getY();
                }
            }

        }

        xAverage = (float) xSum/dim();
        yAverage = (float) ySum/dim();
    }

   
    void cloudClassification () {
        for(int l = 0; l < dimt; l++) {
            for (int m = 0; m < dimx; m++) {
                for (int n = 0; n < dimy; n++) {

                    if(m==0 && n==0 ){
                        sumX = advection[l][m][n].getX() + advection[l][m+1][n].getX() + advection[l][m][n+1].getX() + advection[l][m+1][n+1].getX();
                        sumY = advection[l][m][n].getY() + advection[l][m+1][n].getY() + advection[l][m][n+1].getY() + advection[l][m+1][n+1].getY();

                    }
                    else if (m==(dimx-1) && n==0) {
                        sumX = advection[l][m][n].getX() + advection[l][m-1][n].getX() + advection[l][m-1][n+1].getX() + advection[l][m][n+1].getX();
                        sumY = advection[l][m][n].getY() + advection[l][m-1][n].getY() + advection[l][m-1][n+1].getY() + advection[l][m][n+1].getY();

                    }
                    else if (m==(dimx-1) && n==(dimy-1)) {
                        sumX = advection[l][m][n].getX() + advection[l][m-1][n-1].getX() + advection[l][m][n-1].getX() + advection[l][m-1][n].getX();
                        sumY = advection[l][m][n].getY() + advection[l][m-1][n-1].getY() + advection[l][m][n-1].getY() + advection[l][m-1][n].getY();
                    }
                    else if (m==0 && n==(dimy-1)) {
                        sumX = advection[l][m][n].getX() + advection[l][m][n-1].getX() + advection[l][m+1][n-1].getX() + advection[l][m+1][n].getX();
                        sumY = advection[l][m][n].getY() + advection[l][m][n-1].getY() + advection[l][m+1][n-1].getY() + advection[l][m+1][n].getY();
                    }
                    else if (m==0 && ((n > 0) || (n < (dimy -1)))) {
                        sumX = advection[l][m][n].getX() + advection[l][m][n-1].getX() + advection[l][m+1][n-1].getX() + advection[l][m+1][n].getX() + advection[l][m+1][n+1].getX() + advection[l][m][n+1].getX();
                        sumY = advection[l][m][n].getY() + advection[l][m][n-1].getY() + advection[l][m+1][n-1].getY() + advection[l][m+1][n].getY() + advection[l][m+1][n+1].getY() + advection[l][m][n+1].getY();
                    }
                    else if (((m > 0) || (m < (dimx -1))) && n==0) {
                        sumX = advection[l][m][n].getX() + advection[l][m-1][n].getX() + advection[l][m+1][n].getX() + advection[l][m-1][n+1].getX() + advection[l][m][n+1].getX() + advection[l][m+1][n+1].getX();
                        sumY = advection[l][m][n].getY() + advection[l][m-1][n].getY() + advection[l][m+1][n].getY() +
                                advection[l][m-1][n+1].getY() + advection[l][m][n+1].getY() + advection[l][m+1][n+1].getY();
                    }
                    else if (m==(dimx-1) && ((n > 0) || (n < (dimy -1)))) {
                        sumX = advection[l][m][n].getX() + advection[l][m][n-1].getX() + advection[l][m-1][n-1].getX() + advection[l][m-1][n].getX() + advection[l][m-1][n+1].getX() + advection[l][m][n+1].getX();
                        sumY = advection[l][m][n].getY() + advection[l][m][n-1].getY() + advection[l][m-1][n-1].getY() + advection[l][m-1][n].getY() + advection[l][m-1][n+1].getY() + advection[l][m][n+1].getY();
                    }
                    else {
                        sumX = advection[l][m-1][n-1].getX() + advection[l][m][n-1].getX() + advection[l][m+1][n-1].getX() +advection[l][m-1][n].getX() + advection[l][m][n].getX() + advection[l][m+1][n].getX() ;
                        sumY = advection[l][m-1][n-1].getY() + advection[l][m][n-1].getY() + advection[l][m+1][n-1].getY() +advection[l][m-1][n].getY() + advection[l][m][n].getY() + advection[l][m+1][n].getY() ;
                                
                                }

                    xxAverage = sumX/extension;
                     yyAverage = sumY/extension;
                     // calculating the magnitude of the wind vector
                    float windVectorValue = (float)Math.sqrt(xxAverage*xxAverage + yyAverage*yyAverage);
                    //calculate cloud type
                    if (Math.abs(convection[l][m][n]) > windVectorValue) {
                        // cumulus
                        classification[l][m][n] = 0;
                    } else if (windVectorValue > 0.2) {
                        //striated stratus
                        classification[l][m][n] = 1;

                    } else {
                        //armophous stratus
                        classification[l][m][n] = 2;

                    }               
                    
                     }
            }
        }
    } 
}
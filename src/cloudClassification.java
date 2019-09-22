import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class cloudClassification extends RecursiveAction {

    Vector[][][] advection;
    float[][][] convection;
    int[][][] classification;
    int begin, end;
    int startRow, stopRow, startColumn, stopColumn, dim;
    static final int SEQUENTIAL_CUTOFF = 2000000;
    double compareVal = 0.2;
    int centreRow, centreColumn;
    float xAverage, yAverage;
    
     public int[][][] cloudClassifications() {

        return classification;

    }

    public  cloudClassification(Vector [][][] advect, float [][][] convect,int [][][] classific , int startTime, int stopTime, int strtRw, int stpRw, int strtCol, int stpCol , int dimen){

            advection = advect;
            convection = convect;
            classification = classific;
            dim = dimen;
            begin = startTime;
            end = stopTime;
            startRow = strtRw;
            stopRow = stpRw;
            startColumn = strtCol;
            stopColumn = stpCol;



    }

    @Override
    protected  void compute() {

            if (dim < SEQUENTIAL_CUTOFF) {
                for (int i = begin; i < end; i++) {
                    for (int j = startRow; j < stopRow; j++) {
                        for (int k = startColumn; k < stopColumn; k++) {

                            float magnitudeOfWind = calculateLocalAverage(advection, i, j, k, stopRow, stopColumn);

                            if (magnitudeOfWind > compareVal && Math.abs(convection[i][j][k]) <= magnitudeOfWind) {
                                //Striated Stratus
                                classification[i][j][k] = 1;
                            } else if (Math.abs(convection[i][j][k]) > magnitudeOfWind) {
                                //Cumulus
                                classification[i][j][k] = 0;
                            } else {
                                //Amorphou stratus
                                classification[i][j][k] = 2;
                            }
                        }

                    }

                }

            } else {

                centreRow = startRow + (stopRow - startRow) / 2;
                centreColumn = startColumn + (stopColumn - stopColumn) / 2;
                dim = dim / 4;

                // dividing the cloud classification matrix into 4 quarters
                cloudClassification matrixOne = new cloudClassification(advection, convection, classification, begin, end, startRow, centreRow, startColumn, centreColumn, dim);
                cloudClassification matrixTwo = new cloudClassification(advection, convection, classification, begin, end, startRow, centreRow, centreColumn + 1, stopColumn, dim);
                cloudClassification matrixThree = new cloudClassification(advection, convection, classification, begin, end, centreRow + 1, stopRow, startColumn, centreColumn, dim);
                cloudClassification matrixFour = new cloudClassification(advection, convection, classification, begin, end, centreRow + 1, stopRow, centreColumn + 1, stopColumn, dim);

                //call to the invoke method which runs the task
                ForkJoinTask.invokeAll(matrixOne, matrixTwo, matrixThree, matrixFour);
-
            }

        }



    private float calculateLocalAverage(Vector[][][] windVector, int l, int m, int n, int x, int y){
        Vector[][][] advection = windVector;
        int dimx = x;
        int dimy = y;
        float xSum = 0f;
        float ySum= 0f;
        int extent = 0;

        if(m == 0  && n == 0){

            xSum = advection[l][m][n].getX() + advection[l][m+1][n].getX() + advection[l][m][n+1].getX() + advection[l][m+1][n+1].getX();
            ySum = advection[l][m][n].getY() + advection[l][m+1][n].getY() + advection[l][m][n+1].getY() + advection[l][m+1][n+1].getY();
            extent = 4;
        }
        else if( m ==(dimx-1) && n == 0){
            xSum = advection[l][m][n].getX() + advection[l][m-1][n].getX() + advection[l][m-1][n+1].getX() + advection[l][m][n+1].getX();
            ySum = advection[l][m][n].getY() + advection[l][m-1][n].getY() + advection[l][m-1][n+1].getY() + advection[l][m][n+1].getY();
            extent = 4;


        }else if (m==(dimx-1) && n==(dimy-1)) {
            xSum = advection[l][m][n].getX() + advection[l][m-1][n-1].getX() + advection[l][m][n-1].getX() + advection[l][m-1][n].getX();
            ySum = advection[l][m][n].getY() + advection[l][m-1][n-1].getY() + advection[l][m][n-1].getY() + advection[l][m-1][n].getY();
            extent = 4;
        }
        else if (m==0 && n==(dimy-1)) {
            xSum = advection[l][m][n].getX() + advection[l][m][n-1].getX() + advection[l][m+1][n-1].getX() +
                    advection[l][m+1][n].getX();
            ySum = advection[l][m][n].getY() + advection[l][m][n-1].getY() + advection[l][m+1][n-1].getY() +
                    advection[l][m+1][n].getY();
            extent = 4;
        }
        else if (m==0 && ((n > 0) || (n < (dimy-1)))) {
            xSum = advection[l][m][n].getX() + advection[l][m][n-1].getX() + advection[l][m+1][n-1].getX() + advection[l][m+1][n].getX() + advection[l][m+1][n+1].getX() + advection[l][m][n+1].getX();
            ySum = advection[l][m][n].getY() + advection[l][m][n-1].getY() + advection[l][m+1][n-1].getY() + advection[l][m+1][n].getY() + advection[l][m+1][n+1].getY() + advection[l][m][n+1].getY();
            extent = 6;
        }
        else if (((m > 0) || (m < (dimx-1))) && n==0) {
            xSum = advection[l][m][n].getX() + advection[l][m-1][n].getX() + advection[l][m+1][n].getX() + advection[l][m-1][n+1].getX() + advection[l][m][n+1].getX() + advection[l][m+1][n+1].getX();
            ySum = advection[l][m][n].getY() + advection[l][m-1][n].getY() + advection[l][m+1][n].getY() + advection[l][m-1][n+1].getY() + advection[l][m][n+1].getY() + advection[l][m+1][n+1].getY();
            extent = 6;
        }
        else if (m==(dimx-1) && ((n > 0) || (n < (dimy-1)))) {
            xSum = advection[l][m][n].getX() + advection[l][m][n-1].getX() + advection[l][m-1][n-1].getX() + advection[l][m-1][n].getX() + advection[l][m-1][n+1].getX() + advection[l][m][n+1].getX();
            ySum = advection[l][m][n].getY() + advection[l][m][n-1].getY() + advection[l][m-1][n-1].getY() + advection[l][m-1][n].getY() + advection[l][m-1][n+1].getY() + advection[l][m][n+1].getY();
            extent = 6;
        }
        else if (m+1 < dimx && n+1 < dimy) {
            xSum = advection[l][m-1][n-1].getX() + advection[l][m][n-1].getX() + advection[l][m+1][n-1].getX() + advection[l][m-1][n].getX() + advection[l][m][n].getX() + advection[l][m+1][m].getX() + advection[l][m-1][n+1].getX() + advection[l][m][n+1].getX() + advection[l][m+1][n+1].getX();
            ySum = advection[l][m-1][n-1].getY() + advection[l][m][n-1].getY() + advection[l][m+1][n-1].getY() + advection[l][m-1][n].getY() + advection[l][m][n].getY() + advection[l][m+1][n].getY() + advection[l][m-1][n+1].getY() + advection[l][m][n+1].getY() + advection[l][m+1][n+1].getY();
            extent = 9;
        }

        xAverage = xSum/extent;
        yAverage = ySum/extent;
        return (float)Math.sqrt(xAverage*xAverage + yAverage*yAverage);




    }


}

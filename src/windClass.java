import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class windClass extends RecursiveAction {

    int begin, end,startRow, startColumn, stopRow, stopColumn;
    Vector [][][] wind;
    int centerRow;
    int centerColumn;
    static final int SEQUENTIAL_CUTOFF = 2000000;
    int dimen;
    float xSum =0;
    float ySum =0;


    float xTotal(){
        return  xSum;
    }

    float yTotal(){
        return ySum;
    }

    public  windClass(Vector [][][] windvect, int begn , int ennd, int startRow, int stopRow, int startColumn, int stopColumn, int dimenn){
         wind = windvect;
         begin = begn;
         end = ennd;
         this.startRow = startRow;
         this.stopRow = stopRow;
         this.startColumn = startColumn;
         this.stopColumn = stopColumn;
         dimen = dimenn;

    }
    
   

    @Override
    protected void compute() {

            if (dimen < SEQUENTIAL_CUTOFF) {
                for (int i = begin; i < end; i++) {
                    for (int j = startRow; j < stopRow; j++) {
                        for (int k = startColumn; k < stopColumn; k++) {

                            xSum += wind[i][j][k].getX();
                            ySum += wind[i][j][k].getY();
                        }

                    }
                }

            } else {

                centerRow = startRow + (stopRow - startRow) / 2;
                centerColumn = startColumn + (stopColumn - startColumn) / 2;
                dimen = dimen / 4;
                windClass matrixOne = new windClass(wind, begin, end, startRow, centerRow, startColumn, centerColumn, dimen);
                windClass matrixTwo = new windClass(wind, begin, end, startRow, centerRow, centerColumn + 1, stopColumn, dimen);
                windClass matrixThree = new windClass(wind, begin, end, centerRow + 1, stopRow, startColumn, centerColumn, dimen);
                windClass matrixFour = new windClass(wind, begin, end, centerRow + 1, stopRow, centerColumn + 1, stopColumn, dimen);

                ForkJoinTask.invokeAll(matrixOne, matrixTwo, matrixThree, matrixFour);

                xSum = matrixOne.xSum + matrixTwo.xSum + matrixThree.xSum + matrixFour.xSum;
                ySum = matrixOne.ySum + matrixTwo.ySum + matrixThree.ySum + matrixFour.ySum;


            }
    }

}

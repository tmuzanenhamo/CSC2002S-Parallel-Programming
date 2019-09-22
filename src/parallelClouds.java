import java.util.concurrent.ForkJoinPool;

public class parallelClouds {

    //Measuring time
    static long startTime = 0;
    private static void tick(){
        startTime = System.currentTimeMillis();
    }
    private static float tock(){
        return (System.currentTimeMillis() - startTime) / 1000.0f;
    }
    static final ForkJoinPool fjPool = new ForkJoinPool();

    static void average(CloudData data){

        windClass windCalculation = new windClass(data.Advection(), 0, data.timeStep(),0,data.matrixWidth(),0,data.matrixHeight(),data.dim());
        fjPool.invoke(windCalculation);
        CloudData.averageOfx = windCalculation.xTotal()/data.dim();
        CloudData.averageOfy = windCalculation.yTotal()/data.dim();

    }

    static int[][][] clouds (CloudData data){

        cloudClassification cloudTypes = new cloudClassification(data.Advection(), data.Convection(), data.Classification(), 0, data.timeStep(), 0, data.matrixWidth(), 0, data.matrixHeight(), data.dim());
        fjPool.invoke(cloudTypes);
        return cloudTypes.cloudClassifications();
    }

    

    public static void main(String [] args){
    
    for(int i = 0; i<3; i++){
        System.out.println("Threads running \n");
        String inputFileName = "C:\\Users\\tmuza\\Downloads\\My Degree\\Second Semester\\CSC2002S\\CS Assignments\\CS Assignment 3\\inputs\\50x512x512.txt";
        String outputFileName = "C:\\Users\\tmuza\\Downloads\\My Degree\\Second Semester\\CSC2002S\\CS Assignments\\CS Assignment 3\\Outputs\\out.txt";
        CloudData data = new CloudData();
        data.readData(inputFileName);
       // System.out.println("Working on it....");
        tick();
        average(data);
        data.classificationValues(clouds(data));
        float endTime = tock();
        System.out.printf("The time to do wind calculations was: %f s\n" , endTime);
      //   tick();
//         
//         float runTime = tock();
//         System.out.printf("The time to classify the clouds was: %f s\n", runTime);
        data.writeData(outputFileName, data.averageOfx, data.averageOfy);
}

    }
}

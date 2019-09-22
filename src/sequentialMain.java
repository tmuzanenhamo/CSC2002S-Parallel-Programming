public class sequentialMain {

     static long startTime = 0;
    private static void tick(){
        startTime = System.currentTimeMillis();
    }
    private static float tock(){
        return (System.currentTimeMillis() - startTime) / 1000.0f;
    }


    public static void main(String [] args){
    
    for(int i =0; i<3; i++){
    
        String inputFileName = "C:\\Users\\tmuza\\Downloads\\My Degree\\Second Semester\\CSC2002S\\CS Assignments\\CS Assignment 3\\inputs\\20x256x512.txt";
         String outputFileName = "C:\\Users\\tmuza\\Downloads\\My Degree\\Second Semester\\CSC2002S\\CS Assignments\\CS Assignment 3\\Outputs\\20x256x512seq.txt";

        sequentialCloudData sq = new sequentialCloudData();
        sq.readData(inputFileName);
        sq.writeData(outputFileName);
        System.out.println("Sequential code executing......");
        tick();
        sq.windAverage();
        sq.cloudClassification ();
        float stopTime = tock();
        System.out.printf("The time to do wind calculations was: %f s\n" , stopTime);
        System.out.println(sq.xSum) ; 
        System.out.println(sq.ySum) ; 
        
        }

    }
}

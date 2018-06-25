package code2token;

public class MyTimer {

    static long startTime;
    static long endTime;

    protected MyTimer() {
        reset();
    }

    public static void reset(){
        startTime = System.nanoTime();
    }

    public static void getTimeInterval(){
        endTime = System.nanoTime();
        System.out.println("time interval is:" + (endTime - startTime)/1000000);
    }
}

package cube.generator.RANSAC;

public class Timer
{
    private static long startTime;
    private static long endTime;

    public static void start() {
        startTime = System.nanoTime();
    }

    public static void stop() {
        endTime = System.nanoTime();
    }

    /**
     * @return the milliseconds between the last call of start and stop
     */
    public static long getRuntime() {
        return (endTime - startTime) / 1000000;
    }
}
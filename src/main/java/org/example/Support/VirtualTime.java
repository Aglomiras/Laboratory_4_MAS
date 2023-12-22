package org.example.Support;

public class VirtualTime {
    private static VirtualTime INSTANCE;
    private long startTime;
    private int accelerationOfTime;

    private VirtualTime() {
        startTime = System.currentTimeMillis();
        accelerationOfTime = 48;
    }

    /**Создание нового таймера*/
    public synchronized static VirtualTime getInstance(){
        if (INSTANCE == null){
            INSTANCE = new VirtualTime();
        }
        return INSTANCE;
    }

    /**Возвращаем текущий час (ускоренного времени)*/
    public int getTimeHour() {
        int timeHour = (int) (System.currentTimeMillis() - startTime) * accelerationOfTime / 1000 / 3600;
        return timeHour % 24;
    }

    /**Возвращает количество миллисекунд (ускоренного времени)*/
    public long getTimeMils(double h){
        return (long) (h * 1000 * 3600 / accelerationOfTime);
    }
}

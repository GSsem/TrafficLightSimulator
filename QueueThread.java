package traffic;

import java.util.List;

public class QueueThread implements Runnable {

    private int time;
    private boolean state;
    private List<Road> roadQueue;
    private int openIndex = 1;

    public QueueThread(List<Road> roadQueue) {
        this.time = 0;
        this.roadQueue = roadQueue;
    }

    public void setState() {
        this.state = state ? false : true;
    }

    public boolean getState() { return state; }

    public int getOpenIndex(){ return openIndex; }

    public void incrementOpenIndex(){
        openIndex++;
        openIndex = openIndex % roadQueue.size() == 0 ? 0 : openIndex;
    }
    public void emptyOpenIndex(){ this.openIndex = -1; }

    @Override
    public void run() {

        while (Thread.currentThread().isAlive()) {
            time++;

            try {
                Thread.sleep(1000);
                if (state) {
                    printSystemInfo();
                    updateRoadStates();
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void printSystemInfo() {
        System.out.print("\r" + String.format("""
                ! %ds. have passed since system startup !
                ! Number of roads: %d !
                ! Interval: %d !
                ! Press "Enter" to open menu !
                """, time, roadQueue.size(), Main.getInterval()));
        if (roadQueue.isEmpty()) {
            System.out.println("   (No roads in the system)");
            System.out.println("----------------------------------------\n");
            System.out.println("Press \"Enter\" to open menu...");
            return;
        }
        roadQueue.forEach(x -> System.out.println(x.getStatus()));
    }

    public void updateRoadStates() {
        for (int i = 0; i < roadQueue.size(); i++) {

            roadQueue.get(i).decrementTimeLeft();
            if (roadQueue.get(i).getTimeLeft() == 0 && roadQueue.get(i).isOpen()) {
                roadQueue.get(i).setOpen(false);
                roadQueue.get(i).setTimeLeft(roadQueue.size() == 1 ? Main.getInterval() * roadQueue.size()
                        : Main.getInterval() * (roadQueue.size() - 1));
                openIndex++;
                if(openIndex % roadQueue.size() == 0)
                    openIndex = 0;

            } else if (roadQueue.get(i).getTimeLeft() == 0) {
                roadQueue.get(i).setOpen(true);
                roadQueue.get(i).setTimeLeft(Main.getInterval());
            }

        }

    }
}

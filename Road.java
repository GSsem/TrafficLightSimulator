package traffic;

public class Road {
    private String name;
    private boolean isOpen = false;    // Is this road currently open?
    private int timeLeft;

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";


    public Road(String name) {
        this.name = name;
    }
    public Road(String name, boolean isOpen, int timeLeft) {
        this.name = name;
        this.isOpen = isOpen;
        this.timeLeft = timeLeft;
    }

    public int getTimeLeft() {
        return timeLeft;
    }
    public String getName(){
        return name;
    }
    public synchronized void decrementTimeLeft() {
        this.timeLeft--;
    }
    public synchronized void setTimeLeft(int timeLeft){
        this.timeLeft = timeLeft;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getStatus(){
        return isOpen ? ANSI_GREEN +
                getClass().getSimpleName() +
                " \"" + name + '\"' +
                " will be open for " + timeLeft + "." +
                ANSI_RESET
                : ANSI_RED +
                getClass().getSimpleName() +
                " \"" + name + '\"' +
                " will be closed for " + timeLeft + "." +
                ANSI_RESET;
    }

    @Override
    public String toString() {
        return "Road{" +
                "name='" + name + '\'' +
                ", isOpen=" + isOpen +
                ", timeLeft=" + timeLeft +
                '}';
    }
}

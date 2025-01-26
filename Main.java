package traffic;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Integer numberOfRoads = 0;
    private static Integer interval = 0;
    private static Scanner scanner = new Scanner(System.in);
    private static List<Road> roadQueue = new ArrayList<>();
    private static QueueThread queueThread = new QueueThread(roadQueue);
    private static Thread thread = new Thread(queueThread);





    public static void main(String[] args) {

        System.out.println("Welcome to the traffic management system!");
        getInfo();

    }


    public static void printMenu(){
        System.out.print(""" 
                Menu:
                1. Add road
                2. Delete road
                3. Open system
                0. Quit
                """);
    }

    public static void getInfo(){

        System.out.print("Input the number of roads: > ");
        numberOfRoads = setRoadAndIntervals();
        System.out.print("Input the interval: > ");
        interval = setRoadAndIntervals();
        thread.start();
        printMenu();

        while(true){
            String option = scanner.nextLine();

            switch (option.matches("[0123]") ? Integer.parseInt(option) : -1){
                case 0 -> {
                    thread.interrupt();
                    scanner.close();
                    System.exit(0);
                }
                case 1 -> roadAdd();
                case 2 -> deleteRoad();
                case 3 -> openSystem();
                default -> System.out.println("Incorrect option");
            }

        }

    }

    public static void roadAdd(){

        System.out.print("Input road name: ");

        if(roadQueue.size() != numberOfRoads) {
            String[] t = new String[1];
            while (true){
                t[0] = scanner.nextLine();
                if(t[0].isBlank())
                    System.out.print("Please write correct name: ");
                else {
                    boolean checkUniqueName = roadQueue.stream()
                                                .noneMatch(x -> x.getName().equalsIgnoreCase(t[0]));
                    if(!checkUniqueName)
                        System.out.print("Please write unique name: ");
                    else
                        break;
                }

            }
            if(roadQueue.isEmpty()){
                roadQueue.add(new Road(t[0], true,interval));
                System.out.println("\rRoad added");
                queueThread.incrementOpenIndex();
                printMenu();
                return;
            }
            roadQueue.add(new Road(t[0]));
            onUpdateDeleteRoad();
        }
        else
            System.out.println("\rQueue is full");

        printMenu();
    }

    public static void deleteRoad(){

        if(roadQueue.isEmpty()){
            System.out.println("Queue is empty");
            printMenu();
            queueThread.emptyOpenIndex();
            return;
        }

        if(roadQueue.size() == 1){
            roadQueue.removeLast();
            queueThread.emptyOpenIndex();
            printMenu();
            return;
        }

        if(roadQueue.getLast().isOpen()){
            queueThread.incrementOpenIndex();
            roadQueue.removeLast();
        } else{
            roadQueue.removeLast();
            onUpdateDeleteRoad();
        }
        System.out.println("Road deleted");
        printMenu();
    }

    public static void onUpdateDeleteRoad(){
        int timeOpenRoad = roadQueue.get(queueThread.getOpenIndex()).getTimeLeft();
        for (int i = queueThread.getOpenIndex() + 1; i % roadQueue.size() != queueThread.getOpenIndex(); i++) {
            int distanceBetweenOpenIndexAndCurrent = Math.abs(i - queueThread.getOpenIndex()) - 1;
            roadQueue.get(i % roadQueue.size()).setTimeLeft(timeOpenRoad + interval * distanceBetweenOpenIndexAndCurrent);

        }
    }

    public static void openSystem(){
        queueThread.setState(); // switch on
        scanner.nextLine();
        queueThread.setState(); // switch off
        printMenu();
    }

    public static int setRoadAndIntervals(){

        while (true){
            String t = scanner.nextLine();
            try{
                int a = Integer.parseInt(t);
                if(a > 0)
                    return a;
            } catch (NumberFormatException e){ }
            System.out.println("Error! Incorrect Input. Try again: ");
        }
    }

    public static int getInterval(){
        return interval;
    }

}
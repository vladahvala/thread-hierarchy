package app.src.main;

public class Main {

    public void monitorThreadGroup(ThreadGroup myGroup) {
        Runnable monitorTask = () -> {
            while (myGroup.activeCount() > 0) { // executes while there`re active threads
                printGroup(myGroup, "");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("\n");
            }
            System.out.println("All threads have finished. Monitor thread exiting.");
        };

        Thread monitor = new Thread(monitorTask, "Monitor");
        monitor.start();
    }

    private void printGroup(ThreadGroup group, String indent) {
        // threads of the group
        Thread[] threads = new Thread[group.activeCount()];
        group.enumerate(threads, false);
        for (Thread t : threads) {
            if (t != null) {
                System.out.println(indent + t.getName());
            }

        }

        // threads of the subgroups of the group
        ThreadGroup[] subGroups = new ThreadGroup[group.activeGroupCount()];
        group.enumerate(subGroups, false);
        for (ThreadGroup g : subGroups) {
            if (g != null) {
                System.out.println(indent + "[Group] " + g.getName());
                printGroup(g, indent + "  "); // recursion to get threads of each subgroup
            }
        }
    }

    public static void main(String[] args) {
        ThreadGroup myGroup = new ThreadGroup("Group1");

        // giving each thread the same task to keep it simple
        Runnable task = () -> {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        // thread tree
        Thread t1 = new Thread(myGroup, task, "Thread-1");
        Thread t2 = new Thread(myGroup, task, "Thread-2");
        ThreadGroup SubGroupA = new ThreadGroup(myGroup, "SubGroupA");
        Thread t3 = new Thread(SubGroupA, task, "Thread-3");
        Thread t4 = new Thread(SubGroupA, task, "Thread-4");
        ThreadGroup SubGroupB = new ThreadGroup(myGroup, "SubGroupB");
        Thread t5 = new Thread(SubGroupB, task, "Thread-5");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        Main app = new Main();
        app.monitorThreadGroup(myGroup);
    }

}

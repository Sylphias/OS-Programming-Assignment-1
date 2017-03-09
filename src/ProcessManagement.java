import java.io.File;
import java.io.IOException;

public class ProcessManagement {

    //set the working directory
//    private static ArrayList<ProcessGraphNode>
    private static File currentDirectory = new File(System.getProperty("user.dir"));
    //set the instructions file
    private static File instructionSet = new File("graph-file2");
    public static Object lock=new Object();

    public static void main(String[] args) throws InterruptedException {

        //parse the instruction file and construct a data structure, stored inside ProcessGraph class
        ParseFile.generateGraph(new File(currentDirectory + "/"+instructionSet));

        // Print the graph information
	    ProcessGraph.printGraph();


        // Using index of ProcessGraph, loop through each ProcessGraphNode, to check whether it is ready to run

        // Continues running while not all the nodes have been executed.
        while(!allNodesExecuted()) {
            //mark all the runnable nodes
            markRunnable();
            //run the node if it is runnable
            runNodes();
        }

        System.out.println("All process finished successfully");
    }
//
    public static boolean allNodesExecuted(){
        for (ProcessGraphNode node : ProcessGraph.nodes) {
            if(!node.isExecuted())
                return false;
        }
        return true;
    }

    public static void markRunnable(){
        for (ProcessGraphNode node : ProcessGraph.nodes) {
            if(node.allParentsExecuted() && !node.isRunning()&& !node.isExecuted())
                node.setRunnable();
        }
    }

    public static void runNodes(){
        for (ProcessGraphNode node: ProcessGraph.nodes) {
                if (node.isRunnable() && !node.isRunning() && !node.isExecuted()) {
                    RunProcess rp = new RunProcess(node);
                    node.setRunning(true);
                    rp.start();
                }
        }
    }

}

class RunProcess extends Thread{
    ProcessGraphNode node;
    public RunProcess(ProcessGraphNode node) {
        this.node = node;
    }
    public void run(){
            ProcessBuilder pb = new ProcessBuilder();
            if (!node.getInputFile().getName().equals("stdin"))
                pb.redirectInput(node.getInputFile());
            if (!node.getOutputFile().getName().equals("stdout"))
                pb.redirectOutput(node.getOutputFile());
            pb.command(node.getCommand().split(" "));

            try {
                System.out.println("Process Starting " + node.getNodeId());
                Process p = pb.start();
                p.waitFor();
                node.setExecuted();
            } catch (InterruptedException ie) {
                node.setRunning(false);
                System.out.println(ie.getMessage());
            } catch (IOException ioe) {
                node.setRunning(false);
                System.out.println(ioe.getMessage());
            }

    }
}

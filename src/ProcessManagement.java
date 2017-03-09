import java.io.File;
import java.io.IOException;


/**
 * Programming Assignment 1
 * Done by:
 * Koh Kai Wei 1001471
 * Chan Wei Ren 1001459
 **/
public class ProcessManagement {

    //set the working directory
    private static File currentDirectory = new File(System.getProperty("user.dir"));
    //set the instructions file
    private static File instructionSet ;
    public static boolean graphError;
    public static Object lock=new Object();

    public static void main(String[] args) throws InterruptedException {
        graphError = false;
        instructionSet = new File(args[0]);
        //parse the instruction file and construct a data structure, stored inside ProcessGraph class
        ParseFile.generateGraph(new File(currentDirectory + "/"+instructionSet));

        // Print the graph information
	    ProcessGraph.printGraph();

        // Using index of ProcessGraph, loop through each ProcessGraphNode, to check whether it is ready to run
        // Continues running while not all the nodes have been executed.
        // graphError will change to true if any of the processes fail in running their processes.
        while(!allNodesExecuted() && !graphError) {
            //mark all the runnable nodes
            markRunnable();
            //run the node if it is runnable
            runNodes();
        }

        // This handles the error if the file the process is acting on does not exist.
        if(!graphError)
            System.out.println("All process finished successfully");
        else
            System.out.println("There was an error with the graph");
    }

    /**
     * Method Name: allNodesExecuted
     * Check if all nodes have been executed. If not all nodes have been executed, it will return false.
     * If not it will return true
     **/

    public static boolean allNodesExecuted(){
        for (ProcessGraphNode node : ProcessGraph.nodes) {
            if(!node.isExecuted())
                return false;
        }
        return true;
    }


    /**
     * Method Name: markRunnable
     * This method check if all of the node's parents have executed and that the node is not running or has not executed
     * If those conditions are fulfilled, then the node is marked as runnable
     **/
    public static void markRunnable(){
        for (ProcessGraphNode node : ProcessGraph.nodes) {
            if(node.allParentsExecuted() && !node.isRunning()&& !node.isExecuted())
                node.setRunnable();
        }
    }

    /**
     * Method Name: runNodes
     * This method checks if the node is runnable, running or has already executed.
     * If all are false, we will then spawn a thread to run the process.
    **/
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

/**
 * Class Name: RunProcess
 * This class will allow the process to spawn as a separate thread. It will also allow us to perform process specific adjustments in a separate thread.
 *
 * Class Methods:
 * RunProcess(ProcessGraphNode node) - Constructor that takes in a ProcessGraphNode by reference
 * Run() - Spawns the process that runs the command stored in the ProcessGraphNode.
 *         It waits for the process to finish executing before setting the the node's status to executed.
 **/
class RunProcess extends Thread{
    ProcessGraphNode node;
    public RunProcess(ProcessGraphNode node) {
        this.node = node;
    }
    public void run(){
            ProcessBuilder pb = new ProcessBuilder();

            // Handles the input if it expects an input from stdin and stdout
            if (!node.getInputFile().getName().equals("stdin"))
                pb.redirectInput(node.getInputFile());
            if (!node.getOutputFile().getName().equals("stdout"))
                pb.redirectOutput(node.getOutputFile());

            // Splits the command with spaces, this is to allow commands with multiple arguments to run properly
            pb.command(node.getCommand().split(" "));

            try {
                // Starts process and waits for process to finish. Once process has finished, it sets the node to execution
                System.out.println("Process Starting " + node.getNodeId());
                Process p = pb.start();
                p.waitFor();
                node.setExecuted();
            } catch (InterruptedException ie) {
                node.setRunning(false);
                System.out.println(ie.getMessage());
                ProcessManagement.graphError = true;
            } catch (IOException ioe) {
                node.setRunning(false);
                System.out.println(ioe.getMessage());
                ProcessManagement.graphError = true;
            }

    }
}

#Programming Assignment 1
Authors: Chan Wei Ren, Koh Kai Wei<br />
IDs: 1001459, 1001471<br />
Date: 09/03/2017

##Purpose

This program represents a process tree as a directed acyclic graph (DAG). It
analyses this graph to determine which processes are eligible to run, and 
then runs those processes. A process is eligible to run only when all of 
its parents have completed executing, after which it is forked and executed.
Each node can also have its input and output redirected through its 
definition in the graph file. Additionally, nodes which are runnable can be 
concurrently executed in their own threads.

##How to Compile

###Setting up through GitHub

```
git clone https://github.com/Sylphias/OS-Programming-Assignment-1.git
```

** Note that if you cannot clone the repo, that is because we have not set 
our repository to public. We will make it public after the submission deadline.


** If you cannot clone, you can use the zip file in the submission.

###Running on IDE

For this project we used Intellij to run our files. So for user.dir is 
the root folder of the project

###Running in Ubuntu/Mac Shell

```shell
javac ProcessManagement.java
java ProcessManagement graph-file
```

Ensure that the dependent files are in the same folder as where you run the 
command, if not the program will throw an error message saying there is 
something wrong with the graph.


##What this Program Does

The main method lies in ProcessManagement.java. It passes the graph file 
into ParseFile.java, which generates an ArrayList by reading in the nodes 
represented on each line in the following format:

```$xslt
<command>:<children>:<input file>:<output file>
```

Each element is represented as follows:

`command`: The program name, with its arguments (if any), separated with a 
space each

`children`: Pointers to the children nodes (if any) by index

`input file`: The input file to be used as standard input

`output file`: The output file to be used as standard output

Each line implicitly represents a node, with the first line being node 0, 
the second line being node 1 and so on.

Each node has a few important variables:

`ArrayList<ProcessGraphNode> parents`: An ArrayList containing all of the 
node's parents

`ArrayList<ProcessGraphNode> children`: An ArrayList containing all of the 
node's children

`int nodeId`: The ID of the node

`File inputFile`: The input file of the node

`File outputFile`: The output file of the node

`String command`: The command to be run by the node

`boolean runnable`: Whether or not the node is allowed to run, that is, if 
all of its parents have completed

`boolean executed`: Whether or not the node has executed and completed

`boolean running`: Whether or not the node is currently executing/running


To generate the ArrayList of nodes, for each line, a new node is created and
 added into the ArrayList at the respective index if it doesn't already 
 exist. If the node has children, a new node is created for each child and 
 added into the ArrayList at the respective index if each doesn't already 
 exist. The node's command, input file and output file are then set 
 according to the given parameters.
 
After all the nodes have been added, every node in the ArrayList has its 
children's parent variable set to itself, and every node that has no parents is
 set to runnable, indicating that it is ready to run.
 
The whole ArrayList is constantly looped through, and while there are still 
nodes to be executed, the program checks every node to see if it's runnable.
 If it's parents have all executed, the node is marked as runnable. The 
 program also checks all nodes and runs a node as new thread if it is 
 runnable, is not already executed, or does not already have an instance of 
 it running.

Once all nodes have been executed, the program ends.
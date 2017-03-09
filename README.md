##Purpose

This program represents a process tree as a directed acyclic graph (DAG). It
analyses this graph to determine which processes are eligible to run, and then
runs those processes. A process is eligible to run only when all of its parents
have completed executing. 


##How to Compile
###Setting up through GitHub
```
git clone https://github.com/Sylphias/OS-Programming-Assignment-1.git

```
** Note that if you cannot clone the repo, that is because we have not set 
our repository to public. We will make it public after the submission deadline.


** If you cannot clone, you can use the zip file in the submission.
###Running on IDE
For this project we used Intellij to run our files. So for user.dir is the root folder of the project

###Running in Ubuntu/Mac Shell
```shell
javac ProcessManagement.java
java ProcessManagement graph-file
```
Ensure that the dependent files are in the same folder as where you run the 
command, if not the program will throw an error message saying there is 
something wrong with the graph.

##What this Program Does

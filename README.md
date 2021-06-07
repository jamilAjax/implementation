# Package Challenge  
This API solves the following packaging problem:  
  
>You want to send your friend a package with different things.  
Each thing you put inside the package has such parameters as index number, weight and cost. The  
package has a weight limit. Your goal is to determine which things to put into the package so that the  
total weight is less than or equal to the package limit and the total cost is as large as possible.  
  
That problem matches with the well-known knapsack problem.  
   
  
### Requirements  
* JDK 11  
* Maven 3.6.3

## How to use 
Once you have cloned the repository, you can install it on your maven repository by maven command line
```bash
mvn install  
```
Include the maven dependency in your project ``pom.xml`` file
```xml
<dependency>
    <groupId>com.mobiquity</groupId>
    <artifactId>packer</artifactId>
    <version>1.0.0</version>  
</dependency>
```
Import Packer class
```java
import com.mobiquity.packer.Packer;
```
Use the library by calling pack method
```java
Packer.pack("/input/file/path");
```
## Unit tests execution 
This library has unit tests built with JUnit 5. They validate logic cases and also exception cases.
Unit tests can be run by the command
```bash
mvn test  
```
## Aproach 

After research how to solve the knapsack problem, I found one of the best ways is to use dynamic programming. It consists of making a data table where you put the gain of each possible solution. You iterate over the table increasing the knapsack capacity and looking for the best combination for each item of the list. See also [Wikipedia article](https://en.wikipedia.org/wiki/Knapsack_problem).

The dynamic programming solution algorithm was designed to work with integer values, thus I improved it to round floating-point values to BigDecimal values. 

There are two major passes in this algorithm:

- Forward pass: generating sets where sets contain cumulative (weight, cost) of item chosen. Forward pass include two operations:
    - Extend: in this step an item selected and at its cost and weight to all triplets result form merge operation in last round.
    - Merge: extended set will be merged with previous cumulative set with dominance pruning to reach all possible selecting items to find solution later on.
- Backward pass: Traverses cumulative sets back to source to find optimal solution.

Also, other algorithms can be implemented, the current structure allows new implementations.

### Source code structure :speech_balloon:
All packages and classes have been defined following *SOLID* principles and separated concerns.

Package list under ``com.mobiquity``

 - :file_folder:**exception** Exception classes
 - :file_folder:**service** Contains knapsack problem processors
 - :file_folder:**entities** Data objects
 - :file_folder:**packer** Main package. Contains Packer class
 - :file_folder:**util** Helper classes for parsing files and make a response

New knapsack solution algorithms can be added by a new class that implements the ``SolutionInterface`` interface.
### How it works :gear:
This diagram represents how the library works when is called.

![sequence](https://github.com/jamilAjax/implementation/blob/master/packageChallenge.png)

### Other decisions 

 - Weight and cost are stored as BigDecimal in order to obtain better precision.
 - An exception is thrown if any constraint is not met.
 - TDD practice was used.

## Key libraries and tools 

 - **Java** Programming language.
 - **Maven** Build automation tool.
 -  **JUnit** Unit testing framework.

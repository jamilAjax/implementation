# Package Challenging

PackerSolver is a Java API to solve packaging problems to achieve a sequence of items in which values are maximized and weights are minimized.

## Installation
Use the maven install to install PackerSolver.
```bash
mvn install
```

## Usage
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

## Aproach

After research how to solve the knapsack problem, I found one of the best ways is to use dynamic programming. It consists of making a data table where you put the gain of each possible solution. You iterate over the table increasing the knapsack capacity and looking for the best combination for each item of the list. See also [Wikipedia article](https://en.wikipedia.org/wiki/Knapsack_problem).

The dynamic programming solution algorithm was designed to work with integer values, thus I improved it to round floating-point values to BigDecimal values.

There are two major passes in this algorithm:

- Forward pass: generating sets where sets contain cumulative (weight, cost) of item chosen. Forward pass include two operations:
  - Extend: in this step an item selected and at its cost and weight to all triplets result form merge operation in last round.
  - Merge: extended set will be merged with previous cumulative set with dominance pruning to reach all possible selecting items to find solution later on.
- Backward pass: Traverses cumulative sets back to source to find optimal solution.

Also, other algorithms can be implemented, the current structure allows new implementations.

### How it works
This diagram represents how the library works when is called.

![sequence](https://github.com/jamilAjax/implementation/blob/master/packageChallenge.png)

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

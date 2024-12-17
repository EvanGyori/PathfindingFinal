A final project for school that I did not find out until later that my approach was flawed and it instead will sometimes find the longest path. I hope to remake the project sometime in the futures. Original README:

# A* Pathfinding Algorithm Final Project
The program takes in a file representing a maze to be solved and shows the shortest path between two points and knows when a maze cannot be solved. The program uses the A* algorithm and made use of lists, sets, maps, recursion, and stacks.

The path will only move in horizontal and vertical directions.

## Information and Software Design
The main class to run is `Pathfinding.java` but it uses the aditional classes to assist in finding the shortest path.
The Maze class is used to store the data of a maze text file in an easy to access class and allow for useful methods. The Path class provides a way to pass around the resulting path. AstarPathfinding does all the core work of the algorithm using Maze and Path to assist.

The use of concepts learned over the course came about very naturally and I didn't have to forcefully incorporate any of the data structures. The concepts made a lot of the program much easier to create and were very helpful and simplified multiple parts.

## Usage
Compile `Pathfinding.java` and execute the resulting class

#### linux
Compile:
```
javac Pathfinding.java
```

Execute:
```
java Pathfinding
```

The program will then prompt you to enter the filename of a maze. There are example mazes to input in the `exampleMazes/` directory. Type the path to the file and the file's name
Example input:
```
Enter maze filename: exampleMazes/validMaze1.txt
```

The output will display the map with plus signs `+` to indicate the shortest path from point `1` to point `2`. `*` character represents an empty spot and `#` represents a wall that cannot be passed through.
Example ouput from `exampleMazes/validMaze1.txt`:
```
*****
*1+**
**++*
***2*
*****
```

## Creating a Custom Map
Create a text file and follow the listed rules.

Rules for the maze's text file:
- Must only have specific characters (No Spaces!!!)
	- `*` empty spot
	- `#` wall, cannot be passed through
	- `1` starting position for path, file must contain one and only one
	- `2` ending position for path, file must constain one and only one
- Text must be in a **rectangular shape**
	Example:
```
1#2
*#*
*#*
***
```
more examples of valid and invalid mazes are in the `exampleMazes/` directory.

- Must have a starting and ending position
- Impossible mazes are allowed

Enter the maze's filename when running the program.

## Running Junit Tests
Compile `Tester.java` including the `junit.jar` library and all required classes. Execute the resulting program with the Junit library.

#### Linux
Compile:
```
javac -cp junit.jar Tester.java Maze.java Path.java AstarPathfinding.java
```

Execute:
```
java -jar junit.jar -cp . --scan-classpath
```

## Challenges
Implementing all of the classes and the whole maze and path system were relatively easy but required some planning and took time to implement. However, they were worth the effort as the program feels very structured and organized to me atleast.

The most challenging part was figuring out how the A* pathfinding algorithm works based off the info provided from the [website](https://theory.stanford.edu/~amitp/GameProgramming/AStarComparison.html) using only its basic algorithm rules **without** looking at any implementations of it. Some parts of the algorithm may not be exact but getting it exact was not my intention. My intention was to solve the problem using the algorithm's rules.

## Acknowledgements
[Introduction to A*](https://theory.stanford.edu/~amitp/GameProgramming/AStarComparison.html)
<p>Used to learn the A* pathfinding algorithm rules and specifics.

## Project Requirements (kept for my reference)

- A well documented and useful README.md including
  - A description of your project
  - Dependency and Installation instructions **excluding Geany, Git, Java, JavaFX, and ANSI/Unicode support**
  - Instructions on configuration and execution of your project
  - Sample output that may include images with captions and alt text
  - A description of your repository and overall software design 
  - Citations, Challenges, and anything else you feel is relevant
- A functioning project with a working interface (terminal or graphical) that uses many of the following
  - Lists, Sets, and/or Maps
  - Recursion
  - Stacks and/or Queues
  - Custom generic ADTs (array or node based) if the default ones are unsuitable
  - Heaps, Lambdas, and/or Streams
- Well documented and useful Javdoc including 
  - classes
  - methods and 
  - blocks of functionality
  - cryptic individual lines
- Thorough Testing via one of the following
  - Simulated keyboard input
  - JUnit test cases
  - Uploaded JaCoCo report

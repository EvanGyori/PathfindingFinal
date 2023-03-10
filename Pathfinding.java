import java.util.Scanner;
import java.io.File;
import java.awt.Point;
import java.util.Iterator;

import java.lang.IllegalArgumentException;
import java.io.FileNotFoundException;

/**
 * Includes the main method of the program.
 * Takes in a text file representing a maze from the user and outputs the shortest path to the console.
 */
public class Pathfinding
{
	/**
	 * Gets the maze filename from the user and uses AStarPathfinding class to get the shortest path.
	 * The path is then outputted to the console.
	 * @param args command line arguments not in use
	 */
	public static void main(String[] args)
		throws FileNotFoundException
	{
		// Get maze filename from user
		System.out.print("Enter maze filename: ");
		Scanner console = new Scanner(System.in);
		String filename = console.next();
	
		// Make sure file exists
		File mapFile = new File(filename);
		if (!mapFile.isFile()) {
			System.out.println("File '" + filename + "' not found!");
			return;
		}
		
		// Store file data into maze object
		Maze maze;
		try {
			maze = new Maze(mapFile);
		} catch (IllegalArgumentException err) {
			Maze.printUsage();
			throw err;
		}
		
		// Find path and output it
		Path astarPath = AstarPathfinding.findPath(maze);
		if (astarPath == null) {
			System.out.println("A* Pathfinding Algorithm found no path!");
		} else {
			System.out.println(maze.toString(astarPath));
		}
	}
}

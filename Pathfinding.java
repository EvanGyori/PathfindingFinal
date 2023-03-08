import java.util.Scanner;
import java.io.File;
import java.awt.Point;
import java.util.Iterator;

import java.lang.IllegalArgumentException;
import java.io.FileNotFoundException;

public class Pathfinding
{
	public static void main(String[] args)
		throws FileNotFoundException
	{
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
		
		Path astarPath = AstarPathfinding.findPath(maze);
		if (astarPath == null) {
			System.out.println("A* Pathfinding Algorithm found no path!");
		} else {
			System.out.println(maze.toString(astarPath));
		}
	}
}

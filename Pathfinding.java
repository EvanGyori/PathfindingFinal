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
		if (args.length >= 1 && (args[0].equals("-h") || args[0].equals("--help"))) {
			printUsage();
			return;
		}
		
		// Get filename either from command line argument or as run time input
		String filename;
		if (args.length == 1) {
			filename = args[0];
		} else {
			System.out.print("Enter maze filename: ");
			Scanner console = new Scanner(System.in);
			filename = console.next();
		}
	
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
		
		System.out.println(maze);
		
		Path astarPath = AstarPathfinding.findPath(maze);
		if (astarPath == null) {
			System.out.println("A* Pathfinding Algorithm found no path!");
		} else {
			Iterator<Point> itr = astarPath.iterator();
			while (itr.hasNext()) {
				Point point = itr.next();
				System.out.println(point);
			}
		}
	}
	
	public static void printUsage()
	{
		System.out.println("Pathfinding Flags:");
		
		System.out.println("\t-h --help\tprints this usage and rules for the maze text file");
		System.out.println("\t<filename>\tmaze file name to be used for pathfinding tests");
	}
}

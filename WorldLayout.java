import java.util.Scanner;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.io.File;

import java.lang.IllegalArgumentException;
import java.io.FileNotFoundException;

/**
 * Takes in a text file representing the layout of a maze to use for pathfinding algorithms.
 */
public class Maze
{	
	private static final Map<PointType, Character> pointChars = new HashMap<>(
		Map.of(
			PointType.EMPTY, '*',
			PointType.WALL, '#',
			PointType.START, '1',
			PointType.DESTINATION, '2')); 
	
	private PointType[][] points;
	
	/**
	 * Constuctor that creates the maze's data using the specified file.
	 * @param mapFile the file containing the maze's contents
	 */
	public Maze(File mapFile)
		throws FileNotFoundException
	{
		// First check if file contains the right characters and layout to be considered a maze
		// Also get the size of the maze
		int sizeX = 0, sizeY = 0;
		Scanner layoutInput = new Scanner(mapFile);
		while (layoutInput.hasNextLine()) {
			String lineInput = layoutInput.nextLine();
			sizeY++;
			
			// Check size
			if (sizeX == 0) {
				sizeX = lineInput.length();
			} else if (sizeX != lineInput.length()) {
				throw new IllegalArgumentException("Maze in file must be a rectangle shape");
			}
			
			// Check character
			for (int i = 0; i < lineInput.length(); i++) {
				if (getPointType(lineInput.charAt(i)) == null) {
					throw new IllegalArgumentException("File has improper characters");
				}
			}
		}
		
		// Now that we know the file is valid and we have the size of the maze, read in the data.
		layoutInput = new Scanner(mapFile);
		points = new PointType[sizeX][sizeY];
		int y = 0;
		while (layoutInput.hasNextLine()) {
			String lineInput = layoutInput.nextLine();
			for (int x = 0; x < lineInput.length(); x++) {
				points[x][y] = getPointType(lineInput.charAt(x));
			}
			
			y++;
		}
	}
	
	/**
	 * Default constructor
	 * <p>Creates an empty maze
	 */
	public Maze()
	{
		points = new PointType[0][0];
	}
	
	/**
	 * Returns the height of this maze
	 * @return maze height
	 */
	public int getHeight()
	{
		if (points.length == 0) return 0;
		return points[0].length;
	}
	
	/**
	 * Returns the width of this maze
	 * @return maze width
	 */
	public int getWidth()
	{
		return points.length;
	}
	
	/**
	 * Returns a string of the maze's layout
	 * @return string interpretation of the maze
	 */
	public String toString()
	{
		String result = "";
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				result += getPointChar(points[x][y]);
			}
			
			// Add endline for each row except for the last
			if (y != getHeight() - 1) {
				result += "\n";
			}
		}
		
		return result;
	}
	
	/**
	 * Prints help information for creating a maze text file to use for this Maze class
	 */
	public static void printUsage()
	{
		System.out.println("Maze Text File Usage:");
		
		System.out.println("- Text file must only have specified characters (Can't have any spaces!)");
		System.out.println("\t" + pointChars.get(PointType.EMPTY) + "\tEmpty spot");
		System.out.println("\t" + pointChars.get(PointType.WALL) + "\tWall");
		System.out.println("\t" + pointChars.get(PointType.START) + "\tStart pathfinding node (Must have one and only one)");
		System.out.println("\t" + pointChars.get(PointType.DESTINATION) + "\tFinish pathfinding node (Must have one and only one)");
		
		System.out.println("- Layout must be in a rectangle shape");
		System.out.println("\tExample:");
		System.out.println("\t1#2");
		System.out.println("\t*#*");
		System.out.println("\t*#*");
		System.out.println("\t***");
	}
	
	// Converts the point to a char and returns it
	// Returns a null byte char if point is invalid
	private static char getPointChar(PointType point)
	{
		if (pointChars.containsKey(point)) {
			return pointChars.get(point);
		} else {
			return '\0';
		}
	}
	
	// Converts a character to a PointType enum and returns it
	// Returns null if character is invalid
	private static PointType getPointType(char character)
	{
		Set<PointType> keySet = pointChars.keySet();
		for (PointType key : keySet) {
			if (pointChars.get(key).equals(character)) {
				return key;
			}
		}
		
		return null;
	}
	
	/**
	 * Represents each character in the maze
	 */
	public enum PointType
	{
		EMPTY,
		WALL,
		START,
		DESTINATION
	}
}

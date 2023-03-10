import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.awt.Point;
import java.util.Iterator;

import java.lang.IllegalArgumentException;
import java.io.FileNotFoundException;

/**
 * Takes in a text file representing the layout of a maze to use for pathfinding algorithms.
 * <p> Text file must only have specific characters (no spaces!!)
 * <p> * - empty spot
 * <p> # - wall
 * <p> 1 - start pathfinding node, must have one and only one
 * <p> 2 - end pathfinding node, must have one and only one
 *
 * <p> Text must be in a rectangular shape
 * <p> Example
 * <p> 1#2
 * <p> *#*
 * <p> *#*
 * <p> ***
 */
public class Maze
{
	/**
	 * Represents each type of tile in the maze
	 */
	public enum TileType
	{	
		/**
		 * An empty tile
		 */
		EMPTY,
		/**
		 * An unpassable tile
		 */
		WALL,
		/**
		 * Starting path tile
		 */
		START,
		/**
		 * Ending path tile
		 */
		END
	}
	
	// The char representations of each tile type
	private static final Map<TileType, Character> TILE_CHARACTERS = new HashMap<>(
		Map.of(
			TileType.EMPTY, '*',
			TileType.WALL, '#',
			TileType.START, '1',
			TileType.END, '2'));
	
	private TileType[][] tiles;
	private Point startTile, endTile;
	
	/**
	 * Constuctor that creates the maze's data using the specified file.
	 * @param mapFile the file containing the maze's contents
	 * @throws IllegalArgumentException if the text file is invalid
	 */
	public Maze(File mapFile)
		throws FileNotFoundException
	{
		if (mapFile == null) throw new IllegalArgumentException("null parameter");
	
		// First check if file contains the right characters and layout to be considered a maze
		// Also get the size of the maze
		int sizeX = 0, sizeY = 0;
		startTile = null;
		endTile = null;
		Scanner mazeInput = new Scanner(mapFile);
		while (mazeInput.hasNextLine()) {
			String lineInput = mazeInput.nextLine();
			sizeY++;
			
			// width must be the same on each row.
			if (sizeX == 0) {
				sizeX = lineInput.length();
			} else if (sizeX != lineInput.length()) {
				throw new IllegalArgumentException("Maze in file must be a rectangle shape");
			}
			
			// Must only contain specific characters. Get start and end tile.
			for (int i = 0; i < lineInput.length(); i++) {
				TileType type = characterToTileType(lineInput.charAt(i));
				if (type == null) {
					throw new IllegalArgumentException("File has improper characters");
				} else if (type == TileType.START) {
					startTile = new Point(i, sizeY - 1);
				} else if (type == TileType.END) {
					endTile = new Point(i, sizeY - 1);
				}
			}
		}
		
		// Maze must have a starting and ending tile
		if (startTile == null) {
			throw new IllegalArgumentException("Maze must have a starting point");
		}
		
		if (endTile == null) {
			throw new IllegalArgumentException("Maze must have an ending point");
		}
		
		// Now that we know the file is valid and we have the size of the maze, read in the data.
		mazeInput = new Scanner(mapFile);
		tiles = new TileType[sizeX][sizeY];
		for (int y = 0; y < sizeY; y++) {
			// Wont cause any error since we already know the number of lines in the file
			String lineInput = mazeInput.nextLine();
			for (int x = 0; x < lineInput.length(); x++) {
				tiles[x][y] = characterToTileType(lineInput.charAt(x));
			}
		}
	}
	
	/**
	 * Returns the width of this maze
	 * @return maze width
	 */
	public int getWidth()
	{
		return tiles.length;
	}
	
	/**
	 * Returns the height of this maze
	 * @return maze height
	 */
	public int getHeight()
	{
		if (tiles.length == 0) return 0;
		return tiles[0].length;
	}
	
	/**
	 * Returns TileType of a tile at the specified x and y position
	 * @param x the x-coordinate of the tile
	 * @param y the y-coordinate of the tile
	 * @return the tile's type. null if out of bounds.
	 */
	public TileType getTile(int x, int y)
	{
		if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) return null;
		return tiles[x][y];
	}
	
	/**
	 * Returns the start position of the maze
	 * @return start tile
	 */
	public Point getStart()
	{
		return new Point(startTile);
	}
	
	/**
	 * Returns the end position of the maze
	 * @return end tile
	 */
	public Point getEnd()
	{
		return new Point(endTile);
	}
	
	/**
	 * Returns a string of the maze
	 * @return string interpretation of the maze
	 */
	public String toString()
	{
		String result = "";
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				result += tileTypeToCharacter(tiles[x][y]);
			}
			
			// Add endline for each row except for the last
			if (y != getHeight() - 1) {
				result += "\n";
			}
		}
		
		return result;
	}
	
	/**
	 * Returns a String of the maze with the specified path traversing it
	 * @param path the path to draw out on the maze
	 * @return string interpretation of the maze with the path going through it. Returns just the maze string if path is null.
	 */
	public String toString(Path path)
	{
		if (path == null) return toString();
		
		Set<Point> moves = new HashSet<>();
		Iterator<Point> itr = path.iterator();
		while (itr.hasNext()) {
			moves.add(itr.next());
		}
		
		String result = "";
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (tiles[x][y] == TileType.EMPTY && moves.contains(new Point(x, y))) {
					result += "+";
				} else {
					result += tileTypeToCharacter(tiles[x][y]);
				}
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
		System.out.println("Maze txt file rules:");
		
		System.out.println("- Text file must only have specified characters (Can't have any spaces!)");
		System.out.println("\t" + TILE_CHARACTERS.get(TileType.EMPTY) + "\tEmpty spot");
		System.out.println("\t" + TILE_CHARACTERS.get(TileType.WALL) + "\tWall");
		System.out.println("\t" + TILE_CHARACTERS.get(TileType.START) + "\tStart pathfinding node (Must have one and only one)");
		System.out.println("\t" + TILE_CHARACTERS.get(TileType.END) + "\tFinish pathfinding node (Must have one and only one)");
		
		System.out.println("- Layout must be in a rectangle shape");
		System.out.println("\tExample:");
		System.out.println("\t1#2");
		System.out.println("\t*#*");
		System.out.println("\t*#*");
		System.out.println("\t***");
	}
	
	// Converts the TileType to a char and returns it
	// Returns a null byte char if the TileType doesn't represent any character
	private static char tileTypeToCharacter(TileType tile)
	{
		if (TILE_CHARACTERS.containsKey(tile)) {
			return TILE_CHARACTERS.get(tile);
		} else {
			return '\0';
		}
	}
	
	// Converts a char to a TileType enum and returns it
	// Returns null if char doesn't represent any TileType
	private static TileType characterToTileType(char character)
	{
		Set<TileType> keySet = TILE_CHARACTERS.keySet();
		for (TileType key : keySet) {
			if (TILE_CHARACTERS.get(key).equals(character)) {
				return key;
			}
		}
		
		return null;
	}
}

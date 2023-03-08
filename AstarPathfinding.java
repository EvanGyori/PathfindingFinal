import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.awt.Point;

/**
 * A pathfinding algorithm (A* pathfinding) to find the shortest path given a Maze.
 * <p> The algorithm uses the distance to the start and to the end and adds them together for each tile
 * to explore only the shortest possible path first but if there is a wall in the way, it will expand outwards
 * to longer paths until a path to the end is found.
 */
public class AstarPathfinding
{
	private Maze maze;
	private Path path;
	// Tiles that have already been checked for a path
	private Set<Point> calculatedTiles;
	// Comparator to sort points based on their F value
	private FSumComparator fComparator;
	// Used to stop recursion once end tile is found
	private boolean foundPath;

	/**
	 * Generates and returns a Path object giving the shortest path from one point to another using the specified maze
	 * 
	 * @param maze the Maze to be solved
	 * @return the closest path between the two points in the maze. Returns null if there is no possible path.
	 * @throws IllegalArgumentException if maze is null
	 */
	public static Path findPath(Maze maze)
	{
		if (maze == null) throw new IllegalArgumentException("null parameter");
		
		// Uses a private instance of this class to maintain the algorithm's information
		AstarPathfinding pathfinder = new AstarPathfinding(maze);
		if (pathfinder.foundPath) {
			return pathfinder.path;
		} else {
			return null;
		}
	}
	
	// Constructor that sets up the algorithm.
	// An instanced version is used because multiple variables need to be maintained.
	// It is private so users will only use the static findPath method which is all they need.
	private AstarPathfinding(Maze maze)
	{
		this.maze = maze;
		this.path = new Path();
		this.calculatedTiles = new HashSet<>();
		this.fComparator = new FSumComparator(maze);
		this.foundPath = false;
		
		// Add start to beginning of path and to calculatedTiles so it isn't checked for a path again.
		Point start = maze.getStart();
		path.push(start);
		calculatedTiles.add(start);
		
		// Begin recursion
		findPath((int)start.getX(), (int)start.getY());
	}
	
	// Helper method that uses memoization and recursion to explore shortest paths to farthest paths until a valid path is found
	private void findPath(int x, int y)
	{
		Point end = maze.getEnd();
		if (x == (int)end.getX() && y == (int)end.getY()) {
			foundPath = true;
			return;
		}
	
		// Stores the adjacent points and is sorted from lowest F value to highest.
		List<Point> adjacentTiles = getAdjacentTiles(x, y);
		// No need to re-test already calculated points
		calculatedTiles.addAll(adjacentTiles);
		
		// Recursively check each nearby tile for the shortest path. Goes in order for closest node to farthest node
		// Removes the tile from the path if it isn't valid
		for (Point tile : adjacentTiles) {
			path.push(tile);
			findPath((int)tile.getX(), (int)tile.getY());
			if (foundPath) {
				return;
			}
			path.pop();
		}
	}
	
	/*
	 * Returns a list containing the adjacent tiles of the center tile given x and y.
	 * Doesn't include tiles that have already been checked and generated via previous path checks
	 * (this memoized info is in calculatedPoints).
	 *
	 * @param x the x coordinate of the center point
	 * @param y the y coordinate of the center point
	 * @return the list of adjacent tiles to check for a path sorted based on their F values
	 */
	private List<Point> getAdjacentTiles(int x, int y)
	{
		List<Point> adjacentTiles = new ArrayList<>();
		// adds tiles east, sout, west, north of the given x and y point
		// Must be added in clockwise or counter clockwise order or else it will result in weird
		// patterns that aren't the shortest distance.
		addTile(adjacentTiles, x + 1, y);
		addTile(adjacentTiles, x, y + 1);
		addTile(adjacentTiles, x - 1, y);
		addTile(adjacentTiles, x, y - 1);
		
		// sort tiles based on f value
		adjacentTiles.sort(fComparator);
		
		return adjacentTiles;
	}
	
	/*
	 * Adds the point at the specified x and y to the adjacentPoints List if the point isn't contained in calculatedPoints and
	 * it is within the maze and isn't a wall.
	 *
	 * @param adjacentPoints the list to add the point to
	 * @param x the x coordinate of the point to check
	 * @param y the y coordinate of the point to check
	 */
	private void addTile(List<Point> adjacentTiles, int x, int y)
	{
		Point tile = new Point(x, y);
		Maze.TileType tileType = maze.getTile(x, y);
		// Adds tile if it hasn't already been tested and isn't out of bounds or a wall
		if (!calculatedTiles.contains(tile) && (tileType == Maze.TileType.EMPTY || tileType == Maze.TileType.END)) {
			adjacentTiles.add(tile);
		}
	}
	
	/*
	 * Returns the sum of the moving distance to the start and to the end.
	 * Used in A* pathfinding and is known as the character 'f'.
	 * f = g + h
	 *
	 * @param maze the maze to be solved
	 * @param x the x coordinate of the point to calculate
	 * @param y the y coordinate of the point to calculate
	 * @return f, g + h
	 */
	private static int calculateF(Maze maze, int x, int y)
	{
		return calculateG(maze, x, y) + calculateH(maze, x, y);		
	}
	
	/*
	 * Returns the distance from the current point specified by x and y to the start.
	 * Used in A* pathfinding and is known as the character 'g'.
	 *
	 * @param maze the maze to be solved
	 * @param x the x coordinate of the point to calculate
	 * @param y the y coordinate of the point to calculate
	 * @return g
	 */
	private static int calculateG(Maze maze, int x, int y)
	{
		Point start = maze.getStart();
		return calculateTileDistance(x, y, (int)start.getX(), (int)start.getY());
	}
	
	/*
	 * Returns the distance from the current point specified by x and y to the end.
	 * Used in A* pathfinding and is known as the character 'h'.
	 *
	 * @param maze the maze to be solved
	 * @param x the x coordinate of the point to calculate
	 * @param y the y coordinate of the point to calculate
	 * @return h
	 */
	private static int calculateH(Maze maze, int x, int y)
	{
		Point end = maze.getEnd();
		return calculateTileDistance(x, y, (int)end.getX(), (int)end.getY());
	}
	
	/*
	 * Returns the distance between two tiles.
	 * distance = maximum of (|y2 - y1|, |x2 - x1|)
	 *
	 * @param x1 the x coordinate of the first point
	 * @param y1 the y coordinate of the first point
	 * @param x2 the x coordinate of the second point
	 * @param y2 the y coordinate of the second point
	 * @return tile distance
	 */
	private static int calculateTileDistance(int x1, int y1, int x2, int y2)
	{
		int dx = Math.abs(x2 - x1);
		int dy = Math.abs(y2 - y1);
		return dy > dx ? dy : dx;
	}
	
	// Compares points using their F values.
	// Used to sort a list of points to explore shortest paths first (lowest F value points first)
	private class FSumComparator implements Comparator<Point>
	{
		Maze maze;
		
		public FSumComparator(Maze maze)
		{
			this.maze = maze;
		}
	
		// Compares two points using their F values
		public int compare(Point p1, Point p2)
		{
			int f1 = calculateF(maze, (int)p1.getX(), (int)p1.getY());
			int f2 = calculateF(maze, (int)p2.getX(), (int)p2.getY());
			if (f1 == f2) {
				return 0;
			} else if (f1 > f2) {
				return 1;
			} else {
				return -1;
			}
		}
	}
}

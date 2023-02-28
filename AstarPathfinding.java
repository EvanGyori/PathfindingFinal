import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.awt.Point;

import java.lang.Integer;

public class AstarPathfinding
{
	private Maze maze;
	private Path path;
	private Set<Point> calculatedPoints;
	private FSumComparator pointComparator;
	private boolean foundPath;

	/**
	 * Generates and returns a Path object giving the shortest path from one point to another using the specified maze
	 * 
	 * @param maze the maze to be solved
	 * @return the closest path between the two points in the maze. Returns null if there is no possible path.
	 * @throws IllegalArgumentException if maze is null
	 */
	public static Path findPath(Maze maze)
	{
		if (maze == null) throw new IllegalArgumentException("null parameter");
		
		AstarPathfinding pathfinder = new AstarPathfinding(maze);
		if (pathfinder.foundPath) {
			return pathfinder.path;
		} else {
			return null;
		}
	}
	
	private AstarPathfinding(Maze maze)
	{
		this.maze = maze;
		this.path = new Path();
		this.calculatedPoints = new HashSet<>();
		this.pointComparator = new FSumComparator(maze);
		this.foundPath = false;
		
		// TODO add start point to path and calculatedPoint
		Point start = maze.getStart();
		path.addMove((int)start.getX(), (int)start.getY());
		calculatedPoints.add(start);
		
		findPath((int)start.getX(), (int)start.getY());
	}
	
	// Helper method that uses memoization and recursion
	private void findPath(int x, int y)
	{
		Point end = maze.getDestination();
		if (x == (int)end.getX() && y == (int)end.getY()) {
			foundPath = true;
			return;
		}
	
		// Stores the adjacent points and is sorted from lowest F value to highest.
		List<Point> adjacentPoints = getAvailableAdjacentPoints(x, y);
		// No need to re-test already calculated points
		adjacentPoints.removeAll(calculatedPoints);
		calculatedPoints.addAll(adjacentPoints);
		
		for (Point point : adjacentPoints) {
			path.addMove((int)point.getX(), (int)point.getY());
			findPath((int)point.getX(), (int)point.getY());
			if (foundPath) {
				return;
			}
			path.removeLastMove();
		}
	}
	
	/**
	 * Returns a list containing the adjacent points of the current point given x and y.
	 * Doesn't include points that have already been checked and generated via previous path checks
	 * (this memoized info is in calculatedPoints).
	 *
	 * @param maze the maze being solved
	 * @param calculatedPoints points that have been checked for a path already and shouldn't be checked again (stores the index of the position)
	 * @param x the x coordinate of the center point
	 * @param y the y coordinate of the center point
	 */
	private List<Point> getAvailableAdjacentPoints(int x, int y)
	{
		List<Point> adjacentPoints = new ArrayList<>();
		// adds points north, south, east, and west of the given x and y point
		addPointIfAvailable(adjacentPoints, x, y + 1);
		addPointIfAvailable(adjacentPoints, x, y - 1);
		addPointIfAvailable(adjacentPoints, x + 1, y);
		addPointIfAvailable(adjacentPoints, x - 1, y);
		
		// sort points based on f value
		adjacentPoints.sort(pointComparator);
		
		return adjacentPoints;
	}
	
	/**
	 * Adds the point at the specified x and y to the adjacentPoints List if the point isn't contained in calculatedPoints and
	 * it is within the maze and isn't a wall.
	 *
	 * @param maze the maze to be solved
	 * @param calculatedPoints points that have been checked for a path already and shouldn't be checked again (stores the index of the position)
	 * @param adjacentPoints the list to add the point to
	 * @param x the x coordinate of the point to check
	 * @param y the y coordinate of the point to check
	 */
	private void addPointIfAvailable(List<Point> adjacentPoints, int x, int y)
	{
		Point point = new Point(x, y);
		Maze.PointType pointType = maze.getPoint(x, y);
		if (pointType == Maze.PointType.EMPTY || pointType == Maze.PointType.DESTINATION) {
			adjacentPoints.add(point);
		}
	}
	
	/**
	 * Used in A* pathfinding and is known as the character 'g'.
	 * f = g + h, the sum of the moving distance to the start and the end.
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
	
	/**
	 * Used in A* pathfinding and is known as the character 'g'.
	 * g = the distance from the current point to the start.
	 *
	 * @param maze the maze to be solved
	 * @param x the x coordinate of the point to calculate
	 * @param y the y coordinate of the point to calculate
	 * @return g
	 */
	private static int calculateG(Maze maze, int x, int y)
	{
		Point start = maze.getStart();
		return calculateMoveDistance(x, y, (int)start.getX(), (int)start.getY());
	}
	
	/**
	 * Used in A* pathfinding and is known as the character 'h'.
	 * h = the distance from the current point to the end.
	 *
	 * @param maze the maze to be solved
	 * @param x the x coordinate of the point to calculate
	 * @param y the y coordinate of the point to calculate
	 * @return h
	 */
	private static int calculateH(Maze maze, int x, int y)
	{
		Point end = maze.getDestination();
		return calculateMoveDistance(x, y, (int)end.getX(), (int)end.getY());
	}
	
	/**
	 * Returns the move distance which is the number of steps from one point to another.
	 * distance = |y2 - y1| + |x2 - x1|
	 *
	 * @param x1 the x coordinate of the first point
	 * @param y1 the y coordinate of the first point
	 * @param x2 the x coordinate of the second point
	 * @param y2 the y coordinate of the second point
	 * @return move distance
	 */
	private static int calculateMoveDistance(int x1, int y1, int x2, int y2)
	{
		return Math.abs(x2 - x1) + Math.abs(y2 - y1);
	}
	
	private class FSumComparator implements Comparator<Point>
	{
		Maze maze;
		
		public FSumComparator(Maze maze)
		{
			this.maze = maze;
		}
	
		public int compare(Point o1, Point o2)
		{
			int f1 = calculateF(maze, (int)o1.getX(), (int)o1.getY());
			int f2 = calculateF(maze, (int)o2.getX(), (int)o2.getY());
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

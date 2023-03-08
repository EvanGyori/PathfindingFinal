import java.util.Stack;
import java.util.Iterator;
import java.awt.Point;

/**
 * Contains moves that get from one point to another.
 * <p> Used in the pathfinding algorithm classes as an easy way to contain the path.
 * <p> Moves can be accessed using an iterator.
 */
public class Path
{
	Stack<Point> moves;
	
	/**
	 * Default constructor, initializes the path.
	 */
	public Path()
	{
		moves = new Stack<>();
	}
	
	/**
	 * Adds a new position to the path.
	 * <p> First move should be pushed first and remaining moves should be pushed in order after.
	 * @param x the x coordinate of the point
	 * @param y the y coordinate of the point
	 */
	public void push(int x, int y)
	{
		push(new Point(x, y));
	}
	
	/**
	 * Adds a new position to the path.
	 * <p> First move should be pushed first and remaining moves should be pushed in order after.
	 * @param point the position of the move to add
	 */
	public void push(Point position)
	{
		if (position == null) throw new IllegalArgumentException("null param");
		moves.push(position);
	}
	
	/**
	 * Removes the last added move.
	 */
	public void pop()
	{
		moves.pop();
	}
	
	/**
	 * Returns an iterator to traverse the moves from initial position to last.
	 */
	public Iterator<Point> iterator()
	{
		return moves.iterator();
	}
	
	/**
	 * Returns whether the specified other object is equal to this path
	 * @param other the Object to be compared for equality
	 * @return true if equal, false otherwise.
	 */
	public boolean equals(Object other)
	{
		if (other instanceof Path) {
			return this.moves.equals(((Path)other).moves);
		}
		return false;
	}
}

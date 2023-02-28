import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.awt.Point;

import java.lang.Integer;

public class Path
{
	List<Point> moves;
	
	public Path()
	{
		moves = new LinkedList<>();
	}
	
	public void addMove(int x, int y)
	{
		moves.add(new Point(x, y));
	}
	
	public void removeLastMove()
	{
		moves.remove(moves.size() - 1);
	}
	
	public Iterator<Point> iterator()
	{
		return moves.iterator();
	}
}

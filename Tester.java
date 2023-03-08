import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.*;
import java.awt.*;
import java.lang.*;
import java.io.*;
import java.util.function.*;

public class Tester
{
	private Path path, maze1Path, maze3Path, maze4Path;
	private File[] invalidMazeFiles, validMazeFiles;
	private File impossibleMazeFile;
	
	@BeforeEach
	void reset()
	{
		path = new Path();
		
		invalidMazeFiles = new File[6];
		for (int i = 0; i < invalidMazeFiles.length; i++) {
			invalidMazeFiles[i] = new File("exampleMazes/invalidMaze" + (i + 1) + ".txt");
		}
		
		validMazeFiles = new File[5];
		for (int i = 0; i < validMazeFiles.length; i++) {
			validMazeFiles[i] = new File("exampleMazes/validMaze" + (i + 1) + ".txt");
		}
		
		impossibleMazeFile = new File("exampleMazes/impossibleMaze.txt");
		
		maze3Path = new Path();
		for (int x = 0; x <= 6; x++) maze3Path.push(x, 0);
		for (int y = 1; y <= 5; y++) maze3Path.push(6, y);
		maze3Path.push(5, 5);
		maze3Path.push(4, 5);
		maze3Path.push(4, 4);
		maze3Path.push(3, 4);
		maze3Path.push(2, 4);
		maze3Path.push(2, 3);
		maze3Path.push(2, 2);
		
		maze1Path = new Path();
		maze1Path.push(1, 1);
		maze1Path.push(2, 1);
		maze1Path.push(2, 2);
		maze1Path.push(3, 2);
		maze1Path.push(3, 3);
		
		maze4Path = new Path();
		maze4Path.push(1, 3);
		maze4Path.push(2, 3);
		maze4Path.push(3, 3);
		maze4Path.push(3, 2);
		maze4Path.push(3, 1);
		maze4Path.push(4, 1);
		maze4Path.push(5, 1);
		maze4Path.push(5, 2);
		maze4Path.push(5, 3);
	}
	
	// Path class tests
	@Test
	void testPath()
	{
		path.push(3, 2);
		path.push(new Point(6, 6));
		assertThrows(IllegalArgumentException.class, ()->{ path.push(null); }, "pushing null param should throw error");
		
		path.pop();
		Iterator<Point> itr = path.iterator();
		assertEquals(new Point(3, 2), itr.next(), "top should be (3, 2)");
		assertEquals(false, itr.hasNext(), "path should only have one move");
		
		Path equalPath = new Path();
		equalPath.push(new Point(3, 2));
		assertEquals(path, equalPath, "paths should be equal");
	}
	
	// Maze class tests
	@Test
	void testMazeConstructor()
		throws FileNotFoundException
	{
		assertThrows(IllegalArgumentException.class, ()->{ new Maze(null); }, "null param");
		File fileThatDoesNotExist = new File("This h0pefu1ly Doesn't exist");
		assertThrows(FileNotFoundException.class, ()->{new Maze(fileThatDoesNotExist); }, "invalid file");
		
		// Test invalid mazes
		for (int i = 0; i < invalidMazeFiles.length; i++) {
			try {
				new Maze(invalidMazeFiles[i]);
				assert false : "invalid maze should throw error";
			} catch (IllegalArgumentException err) {}
		}
		
		// Test valid mazes
		for (int i = 0; i < validMazeFiles.length; i++) {
			// Shouldn't throw any errors
			new Maze(validMazeFiles[i]);
		}
		
		// impossible maze shouldn't throw error
		new Maze(impossibleMazeFile);
	}
	
	@Test
	void testMazeGetSize()
		throws FileNotFoundException
	{
		Maze maze5x5 = new Maze(validMazeFiles[0]);
		Maze maze24x16 = new Maze(validMazeFiles[1]);
		
		assertEquals(5, maze5x5.getWidth(), "5x5 maze should have width of 5");
		assertEquals(5, maze5x5.getHeight(), "5x5 maze should have height of 5");
		assertEquals(24, maze24x16.getWidth(), "24x16 maze should have width of 24");
		assertEquals(16, maze24x16.getHeight(), "24x16 maze should have height of 16");
	}
	
	@Test
	void testMazeGetTile()
		throws FileNotFoundException
	{
		Maze maze3 = new Maze(validMazeFiles[2]);
		Maze maze4 = new Maze(validMazeFiles[3]);
		
		assertEquals(null, maze3.getTile(-1, 3), "(-1, 3) is out of bounds");
		assertEquals(null, maze4.getTile(0, 5), "(0, 6) is out of bounds");
		assertEquals(null, maze4.getTile(100, -100), "(100, -100) is out of bounds");
		
		assertEquals(Maze.TileType.START, maze3.getTile(0, 0), "(0, 0) tile should be starting tile");
		assertEquals(Maze.TileType.EMPTY, maze3.getTile(6, 5), "(6, 5) tile should be empty");
		assertEquals(Maze.TileType.END, maze4.getTile(5, 3), "(5, 3) tile should be ending tile");
		assertEquals(Maze.TileType.WALL, maze4.getTile(4, 2), "(4, 2) tile should be a wall");
	}
	
	@Test
	void testMazeGetStart()
		throws FileNotFoundException
	{
		Maze maze1 = new Maze(validMazeFiles[0]);
		Maze maze5 = new Maze(validMazeFiles[4]);
		
		assertEquals(new Point(1, 1), maze1.getStart(), "starting tile should be at (1, 1)");
		
		Point maze5Start = maze5.getStart();
		assertEquals(new Point(2, 1), maze5Start, "starting tile should be at (2, 1)");
		maze5Start.setLocation(0, 0);
		assertEquals(new Point(2, 1), maze5.getStart(), "start should be unmodifiable");
	}
	
	@Test
	void testMazeGetEnd()
		throws FileNotFoundException
	{
		Maze maze2 = new Maze(validMazeFiles[1]);
		Maze maze3 = new Maze(validMazeFiles[2]);
		
		assertEquals(new Point(21, 5), maze2.getEnd(), "ending tile should be at (21, 5)");
		
		Point maze3End = maze3.getEnd();
		assertEquals(new Point(2, 2), maze3End, "ending tile should be at (2, 2)");
		maze3End.setLocation(0, 0);
		assertEquals(new Point(2, 2), maze3.getEnd(), "end should be unmodifiable");
	}
	
	private static final String validMaze1String =
"""
*****
*1***
*****
***2*
*****""";
	
	@Test
	void testMazeToString()
		throws FileNotFoundException
	{
		Maze maze1 = new Maze(validMazeFiles[0]);
		
		assertEquals(validMaze1String, maze1.toString(), "toString method should represent maze");
	}
	
	private static final String validMaze3StringWithPath =
"""
1++++++
*#####+
*#2#*#+
*#+#*#+
*#+++#+
*###+++""";
	
	@Test
	void testMazePathToString()
		throws FileNotFoundException
	{
		Maze maze3 = new Maze(validMazeFiles[2]);
		
		assertEquals(validMaze3StringWithPath, maze3.toString(maze3Path), "toString method with path should represent maze with the path");
	}
	
	// A* Pathfinding class tests
	@Test
	void testAstarPathfinding()
		throws FileNotFoundException
	{
		assertThrows(IllegalArgumentException.class, ()->{AstarPathfinding.findPath(null);}, "null param");
		
		Maze impossibleMaze = new Maze(impossibleMazeFile);
		assertEquals(null, AstarPathfinding.findPath(impossibleMaze), "impossible maze should return null path");
		
		Maze maze1 = new Maze(validMazeFiles[0]);
		assertEquals(maze1Path, AstarPathfinding.findPath(maze1), "must return shortest path for exampleMazes/validMaze1.txt");
		Maze maze3 = new Maze(validMazeFiles[2]);
		assertEquals(maze3Path, AstarPathfinding.findPath(maze3), "must return shortest path for exampleMazes/validMaze3.txt");
		Maze maze4 = new Maze(validMazeFiles[3]);
		assertEquals(maze4Path, AstarPathfinding.findPath(maze4), "must return shortest path for exampleMazes/validMaze4.txt");
	}
}

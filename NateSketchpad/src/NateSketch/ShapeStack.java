package NateSketch;

import java.util.ArrayList;
import java.util.Iterator;

public class ShapeStack implements Iterable<Shape>
{
	private ArrayList<Shape> shapes;
	
	public ShapeStack(int initCapacity)
	{
		shapes = new ArrayList<>(initCapacity);
	}
	
	public boolean isEmpty()
	{
		return shapes.isEmpty();
	}
	
	public void clear()
	{
		shapes.clear();
	}
	
	public Shape pop()
	{
		Shape value = shapes.get(shapes.size()-1);
		shapes.remove(shapes.size()-1);
		return value;
	}
	
	public void push(Shape shape)
	{
		shapes.add(shape);
	}

	@Override
	public Iterator<Shape> iterator()
	{
		return shapes.iterator();
	}
}

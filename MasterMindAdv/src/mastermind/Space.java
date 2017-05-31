package mastermind;

import java.awt.Color;
import java.awt.Graphics;

public class Space {

	private int xCoor;
	private int yCoor;
	private int dim;
	private Color color;

	public Space(int xCoor, int yCoor, int dim) {
		this.xCoor = xCoor;
		this.yCoor = yCoor;
		this.dim = dim;
		color = null;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public int getX() {
		return xCoor;
	}

	public int getY() {
		return yCoor;
	}

	public boolean draw(Graphics g) {
		if(color == null)
			return false;
		g.setColor(color);
		g.fillOval(xCoor, yCoor, dim, dim);
		return true;
	}
}
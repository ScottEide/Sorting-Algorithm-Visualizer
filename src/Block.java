import java.awt.Color;

public class Block {
	private double value;
	private Color color;
	
	public Block(double value) {
		this.value = value;
		this.color = Color.white;
	}
	
	public Block(double value, Color color) {
		this.value = value;
		this.color = color;
	}
	
	public double getValue() {
		return value;
	}

	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
}

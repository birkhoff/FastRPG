package engine;

public class Entity {
	
	private String name, type;
	private int x, y, height, width;
	private Property[] properties;
	private boolean propertyFlag = false;
	
	public Entity(String name, int x, int y){
		this.name = name;
		this.x = x;
		this.y = y;
	}
	
	public int getWidth(){
		return width;
	}
	
	public void setWidth(int width){
		this.width = width;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Property getPropertie(int i) {
		return properties[i];
	}

	public void setProperty(int i, Property property) {
		this.properties[i] = property;
	}
	
	public boolean hasProperties(){
		return propertyFlag;
	}
	
	public void newProperties(Property[] props){
		this.properties = props;
		propertyFlag = true;
	}

	
}

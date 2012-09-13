package engine;

public class Property {
	
	private String name, value;

	public Property(String name, String value){
		this.name = name;
		this.value = value;
	}
	
	public String getName(){
		return name;
	}
	
	public String getValue(){
		return value;
	}
	
}

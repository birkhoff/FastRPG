/**
 * Class representation of the TMX-File attribute Objectgroup
 */


package engine;

public class Objectgroup {
	
	private Entity[] objects;
	private String name;
	
	public Objectgroup(Entity[] obj, String name){
		this.objects = obj;
		this.name = name;
	}
	
	public Entity[] getObjetcs(){
		return this.objects;
	}
	
	public int getObjectCount(){
		return this.objects.length;
	}
	
	public String getName(){
		return name;
	}

}

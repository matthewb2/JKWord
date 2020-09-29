package oata;

public class ImageBucket {
	String file;
	String type;
	byte[] data;
	ImageBucket(String name, String type){
		this.file = name;
		this.type=type;
		this.data=null;
	}
}

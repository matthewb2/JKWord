package oata;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.Properties;
 
public class Config
{
   Properties configFile;
   public Config()
   {
	   configFile = new java.util.Properties();
	   
	   try {
		   FileInputStream reader= new FileInputStream(new File(getClass().getResource("res/config.txt").toURI()));
		   configFile.load(reader);
	   }catch(Exception eta){
		   eta.printStackTrace();
	   }
   }
 
   public String getProperty(String key)
   {
 String value = this.configFile.getProperty(key);
 return value;
   }
}
package nchu.dmlab.firemap.utils;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;

public class Conf {
  private static String GLOBAL_PATH = "/home/casper/firemap/";
	private static String LOCAL_WORKDIR="local_workdir";
	
	private static String DFS_HOST = "dfs_host";
	
	private static String confPath = "config/config.properties";
	private Properties prop = null;
	
	private String hdfs_ip = "HDFSTC";
	private String hdfs_port = "8020";
	
  private static String localWorkDir = "/home/casper/firemap/";
	
	public Conf(){
		try {
			prop = loadConfig(GLOBAL_PATH + confPath);
			System.out.println("Conf Path: " + GLOBAL_PATH + confPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String getLocalWorkDir(){
		return localWorkDir;
//		return prop.getProperty(localWorkDir);
	}

	public String getPhotoOutPath(){
		return "./photoDB";
	}
	public String getPhotoDBPath() {
		return "./photoDB";
	}
	public String getDFS_HOST() {
		return "hdfs://"+this.hdfs_ip+":"+this.hdfs_port;
	}
	public String getDFS_IP(){
//		return prop.getProperty(DFS_HOST);
		return this.hdfs_ip;
	}
	public String getDFS_PORT(){
		return this.hdfs_port;
	}
	private Properties loadConfig(String filename) throws Exception	{
		File confFile = null;
		Properties prop = null;
		URL url = ClassLoader.getSystemClassLoader().getResource(filename);
		try{
			confFile = new File(url.toURI());
			System.out.println(url.toURI());
			if(confFile.exists()){
				prop = new Properties();
				prop.load(new FileInputStream(filename));
			}else{
				throw new Exception("* Error in finding "+confFile.toString());
			}
			return prop;
		}catch(Exception e){
			throw new Exception("* Error in loading "+ confFile.toString());
		}
	}
}

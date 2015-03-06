package nchu.dmlab.firemap.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

import javax.imageio.ImageIO;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class DFSUtil{
  // private String dfsDesc = "firemap/output/";
  // private String localPath = "/home/casper/output";
	FileSystem dfs = null;
	Configuration conf = null;
	public DFSUtil(){
		Conf myConf = new Conf();
		
		conf = new Configuration();
		conf.set("fs.default.name", myConf.getDFS_HOST());
		try {
			dfs = FileSystem.get(conf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void fileExportLocal(String dfsPath, String localPath){
		Path src = new Path(dfs.getWorkingDirectory()+"/"+dfsPath);
		try {
			FileStatus[] flist = dfs.listStatus(src);
			Path descDir = new Path(localPath);
			FSDataInputStream in = null;
			String outputFile = localPath + "/mosaic.txt";
			FileOutputStream os = new FileOutputStream(outputFile);
			if( flist.length > 0 ){
				for( FileStatus f : flist ){
					if( !f.isDir() ){
	//				dfs.copyToLocalFile(f.getPath(), descDir);
						in= dfs.open(f.getPath());
						IOUtils.copyBytes(in, os, conf, false);
						System.out.println("Write " + f.getPath() + " in " + outputFile);
					}
				}
			}
			os.flush();
			os.close();
			IOUtils.closeStream(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void uploadFile(String src, String dest){
		try {
			dfs.copyFromLocalFile(new Path(src), new Path(dest));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Path getDFSFilePath(String src){
		return new Path(dfs.getWorkingDirectory()+"/"+src);
	}
	
	public FSDataInputStream getFSDataInputStream(String src) throws IOException{ 
		Path path = getDFSFilePath(src);
		return dfs.open(path);
	}
	
	public BufferedImage getBufferedImage(String src) throws IOException{
		FSDataInputStream instream = getFSDataInputStream(src);
		return ImageIO.read(instream);
	}
	
	public String showWorkingDirectory(){
		return dfs.getWorkingDirectory().toString();
	}
	
	public void closeDFS(){
		try {
			dfs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException{
		DFSUtil dfsUtil = new DFSUtil();
//		dfsUtil.fileExportLocal();
		
//		BufferedImage img = dfsUtil.getBufferedImage("mosaic/target/photo.jpg");
//		FileOutputStream os = new FileOutputStream("./photo_test.jpg");
//		ImageIO.write(img, "jpg", os);
		System.out.println(dfsUtil.showWorkingDirectory());
		dfsUtil.closeDFS();
	}

}
package nchu.dmlab.firemap;


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import nchu.dmlab.firemap.utils.DoublePolygon;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;

/**
 * immediate key : position of target village, immediate value : difference
 * 
 * @author Firemap
 */
public class FiremapMapper extends Mapper<Object, Text, Text, LongWritable>{
	private String inputFile;
  private Path targetVillagePath = null;
  HashMap<String, Latlngs> villageID_map_latlng = new HashMap<String, Latlngs>();
  
	private Text outputKey = new Text();
  private LongWritable outputValue = new LongWritable(1);
	@Override
  public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

		try {
      String[] inputItem = value.toString().split(" ");
      // StringTokenizer wordList = new StringTokenizer(value.toString());
      String id = inputItem[0].trim();
      String section = inputItem[1];
      double lat = Double.parseDouble(inputItem[2]);
      double lng = Double.parseDouble(inputItem[3]);
      
      for(String villageID: villageID_map_latlng.keySet() ){
        Latlngs villageLatlng = villageID_map_latlng.get(villageID);
        outputKey.set(villageID);
        if (isContain(lat, lng, villageLatlng)) {
          if (id.contains("firedepartment")) {
            outputValue.set(CalculateConfig.FIRE_DEPARTMENT_WEIGHT);
            context.write(outputKey, outputValue);
          }
          if (id.contains("illegal")) {
            outputValue.set(CalculateConfig.ILLEGAL_CONSTRUCTION_WEIGHT);
            context.write(outputKey, outputValue);
          }
          if (id.contains("narrow")) {
            outputValue.set(CalculateConfig.NARROW_ROADWAY_WEIGHT);
            context.write(outputKey, outputValue);
          }
          // if contains other risk conditions
        } else {
          double min_distance = minBoundDistance(lat, lng, villageLatlng);
          if (id.contains("firedepartment") && min_distance <= CalculateConfig.FIRE_DEPARTMENT_LENGTH * CalculateConfig.LENGTH_TOLERANT) {
            outputValue.set(CalculateConfig.FIRE_DEPARTMENT_WEIGHT);
            context.write(outputKey, outputValue);
          }
          if (id.contains("illegal") && min_distance <= CalculateConfig.ILLEGAL_CONSTRUCTION_LENGTH * CalculateConfig.LENGTH_TOLERANT) {
            outputValue.set(CalculateConfig.ILLEGAL_CONSTRUCTION_WEIGHT);
            context.write(outputKey, outputValue);
          }
          if (id.contains("narrow") && min_distance <= CalculateConfig.NARROW_ROADWAY_LENGTH * CalculateConfig.LENGTH_TOLERANT) {
            outputValue.set(CalculateConfig.NARROW_ROADWAY_WEIGHT);
            context.write(outputKey, outputValue);
          }
        }
      }
      // outputKey.set(id);
      // context.write(outputKey, outputValue);
		} catch (IOException ex) {
      Logger.getLogger(FiremapMapper.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	@Override
  protected void setup(Context context) throws IOException, InterruptedException {
    Configuration job = ((JobContext) context).getConfiguration();
    try {
      Path[] targetPhotoFiles;
      targetPhotoFiles = DistributedCache.getLocalCacheFiles(job);
      for (Path photoFile : targetPhotoFiles) {
        setTargetImagePath(photoFile);
      }
    } catch (IOException ioe) {
      System.err.println("Caught exception while getting cached files: " + StringUtils.stringifyException(ioe));
    }
  }
	
  private void setTargetImagePath(Path villageFile) {
    targetVillagePath = villageFile;
    try {
      readVillage(villageFile.toString());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
	}
  public void readVillage(String path) throws IOException{
    FileReader fr = new FileReader(path);
    BufferedReader br = new BufferedReader(fr);
    while (br.ready()) {
      String[] subString=br.readLine().split(" ");
      String id = subString[0];
      String section = subString[1];
      double[] lats = StringToArray(subString[2]);
      double[] lngs = StringToArray(subString[3]);
      villageID_map_latlng.put(id, new Latlngs(lats, lngs));
    }
    br.close();
    fr.close();

  }

  // 計算點和polygon邊上所有點距離
  public double minBoundDistance(double inputLat, double inputLng, Latlngs polygon_latlngs) {
    double distance = 0.0;
    double min_distance = 9999.0;
    int i = 0;

    while (i < polygon_latlngs.getLat().length) {
      distance = Math.sqrt(Math.pow(polygon_latlngs.getLat()[i] - inputLat, 2) + Math.pow(polygon_latlngs.getLng()[i] - inputLng, 2));
      min_distance = Math.min(min_distance, distance);
      // System.out.println(min_distance);
      i++;
    }
    return min_distance;
  }
  //檢查點是否落在polygon
  public boolean isContain(double inputLat,double inputLng,Latlngs polygon_latlngs){
    DoublePolygon polygon = new DoublePolygon(polygon_latlngs.getLat(), polygon_latlngs.getLng());
    if(polygon.contains(inputLat, inputLng))
      return true;
    else
      return false;
  }
  public double[] StringToArray(String str){
    String[] items = str.replaceAll("\\[", "").replaceAll("\\]", "").split(",");
    double[] results = new double[items.length];
    int i=0;
    
    for(String item:items){
      results[i]=Double.parseDouble(item.trim());
      i++;
    }
    return results;
  }
  public class Latlngs{
    double[] lats,lngs;
    public Latlngs(double[] lats,double[] lngs){
      this.lats=lats;
      this.lngs=lngs;
    }
    
    public double[] getLat(){ return this.lats;}
    public double[] getLng(){ return this.lngs;}

  }
}

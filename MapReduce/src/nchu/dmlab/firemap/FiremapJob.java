package nchu.dmlab.firemap;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class FiremapJob extends Configured implements Tool{

	// IMPORTANT : class main should be set as application entry point while archiving the JAR file
	public static void main(String[] args) {
		
		if( args.length <= 0 ){
      System.out.println("Main : [intput_path] [output_path] [-villageLocal] [-villageDFS]");
			return;
		}
		
		// mapred
		try {
//			runJob(args[0], args[1]);
      ToolRunner.run(new FiremapJob(), args);
		} catch (IOException ex) {
      Logger.getLogger(FiremapJob.class.getName()).log(Level.SEVERE, null, ex);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

  @Override
  public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String input = args[0];
		String output = args[1];
    String villageLocal = "";
    String villageDFS = "";
		System.out.println("Source : " + input + "\n" + "Target : " + output);
		Configuration conf = new Configuration();

		conf.set("io.serializations", "org.apache.hadoop.io.serializer.JavaSerialization,org.apache.hadoop.io.serializer.WritableSerialization");
		
		for (int i = 0; i < args.length; ++i) {
      if ("-villageDFS".equals(args[i])) {
        villageDFS = args[++i];
			}
      else if("-villageLocal".equals(args[i])){
        villageLocal = args[++i];
      }
		}
		
    // DFSUtil dfsUtil = new DFSUtil();
		
    // ****** upload target village *****//
    // dfsUtil.uploadFile(villageLocal, villageDFS);
    // System.out.println("Upload DFS " + villageLocal + " =>  " + villageDFS);

//		conf.set("mapred.tasktracker.map.tasks.maximum", "40");
//		conf.set("mapred.child.java.opts", "-Xmx512m");

    Job job = Job.getInstance(conf, "Firemap Job");

		FileInputFormat.setInputPaths(job, input);
    job.setJarByClass(FiremapJob.class);
    job.setMapperClass(FiremapMapper.class);
    job.setReducerClass(FiremapReducer.class);

    job.setInputFormatClass(TextInputFormat.class);
    
    // job.setNumReduceTasks(20);
		job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(LongWritable.class);

    // *********** DistributedCache ****************//
    if (!villageLocal.equals("")) {
      job.addCacheFile(new URI(villageDFS));
      System.out.println("ADD DistributedCache : " + villageDFS);
    }
		
		Path outPath = new Path(output);
		FileOutputFormat.setOutputPath(job, outPath);
    FileSystem dfs = FileSystem.get(outPath.toUri(), conf);
    if (dfs.exists(outPath)) {
      dfs.delete(outPath, true);
    }
		
		try {
			job.waitForCompletion(true);
		} catch (InterruptedException ex) {
      Logger.getLogger(FiremapJob.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
      Logger.getLogger(FiremapJob.class.getName()).log(Level.SEVERE, null, ex);
		}
		return 0;
	}

}
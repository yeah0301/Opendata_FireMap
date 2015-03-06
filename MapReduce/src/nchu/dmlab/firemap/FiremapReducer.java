package nchu.dmlab.firemap;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * @author Firemap
 *
 */
public class FiremapReducer extends Reducer<Text, LongWritable, Text, LongWritable>{
  private String SEPARATOR = ":";
  private LongWritable outputValue = new LongWritable();

  public void reduce(Text key, Iterator<LongWritable> values, Context context) throws IOException, InterruptedException {
    long sum = 0L;

    while (values.hasNext()) {
      sum += values.next().get();
    }
    outputValue.set(sum);;
    context.write(key, outputValue);
  }
}
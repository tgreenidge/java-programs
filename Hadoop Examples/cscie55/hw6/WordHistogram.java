package cscie55.hw6;
import java.io.IOException;
import java.util.*;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

public class WordHistogram extends Configured implements Tool {

    public static void main(String args[]) throws Exception {
        //I tried troubleshooting by trying new WordHistogram() as well to get class to run
        int res = ToolRunner.run(new cscie55.hw6.WordHistogram(), args);
        System.exit(res);
    }

    public int run(String[] args) throws Exception {
        Path inputPath = new Path(args[0]);
        Path outputPath = new Path(args[1]);

        Configuration conf = getConf();
        Job job = new Job(conf, this.getClass().toString());

        FileInputFormat.setInputPaths(job, inputPath);
        FileOutputFormat.setOutputPath(job, outputPath);

        job.setJobName("WordHistogram");
        //I tried troubleshooting by trying new WordHistogram() as well
        job.setJarByClass(cscie55.hw6.WordHistogram.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(Map.class);
        job.setCombinerClass(Reduce.class);
        job.setReducerClass(Reduce.class);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    //I made the keyout at LongWritable since we need to feed word lengths into the reducer
    public static class Map extends Mapper<LongWritable, Text, LongWritable, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        @Override
        public void map(LongWritable key, Text value,
            Mapper.Context context) throws IOException, InterruptedException {
            String line = value.toString();
            StringTokenizer tokenizer = new StringTokenizer(line);
            while (tokenizer.hasMoreTokens()) {
                word.set(tokenizer.nextToken());
                // modified this line from original WordCountfile
                // to meet the required output frequency of word-length
                context.write(new LongWritable(word.getLength()), one);
            }
        }
    }

    //keyin is LongWritable, value-in is IntWritable, keyout is LongWritable, value-out is IntWritable
    public static class Reduce extends Reducer<LongWritable, IntWritable, LongWritable, IntWritable> {

        @Override
        public void reduce(LongWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable value : values) {
                sum += value.get();
            }

            context.write(key, new IntWritable(sum));
        }
    }

}

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

public class WordCountByFile extends Configured implements Tool {

    public static void main(String args[]) throws Exception {
        int res = ToolRunner.run(new cscie55.hw6.WordCountByFile(), args);
        System.exit(res);
    }

    public int run(String[] args) throws Exception {
        Path inputPath = new Path(args[0]);
        Path outputPath = new Path(args[1]);

        Configuration conf = getConf();
        Job job = new Job(conf, this.getClass().toString());

        FileInputFormat.setInputPaths(job, inputPath);
        FileOutputFormat.setOutputPath(job, outputPath);

        job.setJobName("WordCountByFile");
        job.setJarByClass(cscie55.hw6.WordCountByFile.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        job.setMapOutputKeyClass(Text.class); //word
        job.setMapOutputValueClass(Text.class); //filename
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setMapperClass(Map.class);
        job.setCombinerClass(Reduce.class);
        job.setReducerClass(Reduce.class);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    //Since we are outputting the word with the respective filename, 
    // key-out and value-out are both text 
    public static class Map extends Mapper<LongWritable, Text, Text, Text> {
        private Text word = new Text();
        private Text fileName = new Text(); //name of each file 

        @Override
        public void map(LongWritable key, Text value,
            Mapper.Context context) throws IOException, InterruptedException {
            String line = value.toString();
            
            //get the file path (found using the Stackover flow post below)
            //https://stackoverflow.com/questions/19012482/how-to-get-the-input-file-name-in-the-mapper-in-a-hadoop-program
            fileName.set(((FileSplit) context.getInputSplit()).getPath().toString());
            StringTokenizer tokenizer = new StringTokenizer(line);
            while (tokenizer.hasMoreTokens()) {
                word.set(tokenizer.nextToken());
                context.write(word, fileName);
            }
        }
    }

    //Since we are outputting the word with the respective filenames and their counts as a string, 
    // key-out and value-out are both text 
    public static class Reduce extends Reducer<Text, Text, Text, Text> {
        //create a maooing for the filePaths
        TreeMap<String, Integer> fileCounts = new TreeMap<String, Integer>();


        @Override
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            //note key is a word!

            for (Text fileName: values) {
                String filePath = fileName.toString();

                //check to see if filePath is in TreeMap, if so, increase the count of its occurence
                if( fileCounts.get (filePath) != null) {
                    fileCounts.put (filePath, fileCounts.get (filePath) + 1 );
                } else {
                    fileCounts.put (filePath, 1);
                }
            }
        
            //Suppose the word appears different files
            // Then the output (output/part-r-00000) should contain this line:
            // word filePath1: timesAppearedInFile1 filePath2: timesAppearedInFile2
            // to achieve this, we need to aggregate all the filenames with the word

            StringBuilder allFiles = new StringBuilder();

            //unsure why the following block of code does not work
            // got an error on Map.Entry
            // for (Map.Entry <String, Integer> entry : fileCounts.entrySet()) {
            //     String file = entry.getKey();
            //     Integer count = entry.getValue();
            //     allFiles.append (file + ": ");
            //     allFiles.append (count);
            //     allFiles.append (" ");
            // }

            Set<String> set = fileCounts.keySet();
            for (String entry : set) {
                String file = entry;
                Integer count = fileCounts.get(file);
                allFiles.append (file + ": ");
                allFiles.append (count + " ");
            }

            // the final output of Map/Reduce is of <word, frequency_in_all_files>
            context.write(key, new Text( allFiles.toString() ));
        }
    }

}

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudera.utils.mapreduce;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cloudera.utils.mapreduce.MRPingTool.BATCH_ID;
import static com.cloudera.utils.mapreduce.MRPingTool.HOST_LIST_FILE;

public class MRCommandMapper extends Mapper<LongWritable, NullWritable, NullWritable, Text> {
    static private Logger LOG = Logger.getLogger(MRCommandMapper.class.getName());
    private final char DELIMITER = '\001';
//    private final String DELIMITER = ",";
    private final String SUCCESS = "1";
    private final String UNREACHABLE = "-1";
    private final String ERROR = "-2";
    private List<String> hostList = new ArrayList<String>();
//    private Pattern p = Pattern.compile("(Destination Host Unreachable)|(time=)");
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    protected void setup(Context context) {
        // Get the conf location from the job conf.
        String hlFile = context.getConfiguration().get(HOST_LIST_FILE);
        LOG.info("Host List File: " + hlFile);

        // Read the Host List from the path.
        FileSystem FS1 = null;
        FSDataInputStream dfsConfigInputStream = null;
        try {
            FS1 = FileSystem.get(context.getConfiguration());
            Path path = new Path(hlFile);
            dfsConfigInputStream = FS1.open(path);
            InputStreamReader isr = new InputStreamReader(dfsConfigInputStream,
                    StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            br.lines().forEach(line -> hostList.add(line));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeStream(dfsConfigInputStream);
        }
    }

    protected Boolean validLine(String line) {
        if (line.contains("time") || line.contains("Unreachable")) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    protected String parse(String line) {
        StringBuilder sb = new StringBuilder();
        String[] lineParts = line.split(" ");
        for (String part: lineParts) {
            if (part.startsWith("time")) {
                System.out.println("Time element: " + part);
                String[] time = part.split("=");
                try {
                    String letancy = time[1];
                    sb.append(SUCCESS).append(this.DELIMITER);
                    sb.append(letancy);
                } catch (Throwable t) {
                    System.out.println("Problem parsing " + part + " for time element.");
                    sb.append(ERROR).append(this.DELIMITER);
                    sb.append("0");
                }
            } else if (part.startsWith("Unreachable")) {
                sb.append(UNREACHABLE).append(this.DELIMITER); // Unreachable
                sb.append("0");
            }
        }
        // If there is a failure to parse, set status
        if (sb.toString().trim().length() == 0) {
            System.out.println("Could parse needed element from ping result: " + line);
            sb.append(ERROR).append(this.DELIMITER);
            // This creates a NULL for the LETANCY element
            sb.append("0");
        }
        return sb.toString();
    }

    public void runSystemCommand(String command, Context context, String... params) {

        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader inputStream = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            String s = "";
            StringBuilder sbFull = new StringBuilder();
            String timestamp = df.format(new Date());

            InetAddress localhost = InetAddress.getLocalHost();
            InetAddress realhost = InetAddress.getByName(localhost.getHostName());
            String realHostIp = realhost.getHostAddress();
            String realHostName = localhost.getHostName();

            Boolean foundValidLine = Boolean.FALSE;

            StringBuilder sb = new StringBuilder();
            sb.append(timestamp).append(this.DELIMITER);
            sb.append(realHostIp).append(this.DELIMITER);
            sb.append(realHostName).append(this.DELIMITER);
            // TODO: At this point, the only param is the target host.
            //       In the future, there may be more...  like the ping packet size.
            for (String param: params) {
                sb.append(param).append(this.DELIMITER);
            }

            while ((s = inputStream.readLine()) != null) {
                // Parse and write each line.
                System.out.println(s);
                sbFull.append(s).append("\n");
                if (validLine(s)) {
                    foundValidLine = Boolean.TRUE;
                    String parsedLine = parse(s);
                    StringBuilder sbEntry = new StringBuilder(sb.toString());
                    sbEntry.append(parsedLine);
                    Text lineText = new Text(sbEntry.toString());
                    System.out.println(sbEntry.toString());
                    context.write(NullWritable.get(), lineText);
                } else {
                    System.out.println("NO LINE: " + s);
                }
            }
            // Didn't find a matching/parsable line.
            if (!foundValidLine) {
                System.out.println("NO parsable line in command (" + command + "):\n" + sbFull.toString());
                StringBuilder sbNotFound = new StringBuilder(sb.toString());
                sbNotFound.append(ERROR).append(this.DELIMITER);
                sbNotFound.append("0");
                Text lineText = new Text(sbNotFound.toString());
                System.out.println(sbNotFound.toString());
                context.write(NullWritable.get(), lineText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void map(LongWritable key, NullWritable value, Context context) throws IOException, InterruptedException {
        /*
        - Loop through Host File List
        -
         */
        for (String host :
                hostList) {
            String command = "ping " + host + " -c 5";
            runSystemCommand(command, context, host);
        }
    }

}

/*
 * Copyright (c) 2022. Cloudera, Inc. All Rights Reserved
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.cloudera.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CmdRun {
    private static Pattern p = Pattern.compile("Destination\\sHost\\sUnreachable|time=");

    public static void runSystemCommand(String command) {

        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader inputStream = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            String s = "";
            // reading output stream of the command
            InetAddress localhost = InetAddress.getLocalHost();
            InetAddress realhost = InetAddress.getByName(localhost.getHostName());
            String hostIp = realhost.getHostAddress();
            String hostName = localhost.getHostName();
            String hostFullName = realhost.getCanonicalHostName();
            System.out.println("Host IP: " + hostIp);
            System.out.println("Hostname: " + hostName);
            System.out.println("Host Canonical Name: " + hostFullName);

            while ((s = inputStream.readLine()) != null) {
                if (validLine(s)) {
                    System.out.println("Parsed Line: " + parse(s));
                } else {
                    System.out.println("Non Target Line: " + s);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static Boolean validLine(String line) {
        if (line.contains("time") || line.contains("Unreachable")) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    protected static String parse(String line) {
        StringBuilder sb = new StringBuilder();
        String[] lineParts = line.split(" ");
        for (String part: lineParts) {
            if (part.startsWith("time")) {
                String[] time = part.split("=");
                sb.append(time[1]);
                break;
            } else if (part.startsWith("Unreachable")) {
                sb.append("-1"); // Unreachable
                break;
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        runSystemCommand("ping " + args[0] + " -c 1");
    }
}

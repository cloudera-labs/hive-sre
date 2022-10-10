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

package com.cloudera.utils.hive.sre;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Utils {

    public static String dirToPartitionSpec(String directoryPart) throws UnsupportedEncodingException {
        String[] directories = directoryPart.split("\\/");
        String[] partitionSpecs = new String[directories.length];
        int loc = 0;
        try {
            for (String directory : directories) {
                String[] specParts = directory.split("=");
                String partDir = null;
                partDir = URLDecoder.decode(specParts[1], StandardCharsets.UTF_8.toString());
                partitionSpecs[loc++] = specParts[0] + "=\"" + partDir + "\"";
            }
        } catch (Throwable t) {
            System.err.println("Issue with partition directory spec: " + directoryPart);
//            throw t;
        }
        StringBuilder rtn = new StringBuilder();
        rtn.append(StringUtils.join(partitionSpecs, ","));
        return rtn.toString();
    }
}

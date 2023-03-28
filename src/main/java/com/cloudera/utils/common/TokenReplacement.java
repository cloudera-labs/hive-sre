/*
 * Copyright (c) 2023. Cloudera, Inc. All Rights Reserved
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

package com.cloudera.utils.common;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenReplacement {
    private static Logger LOG = LogManager.getLogger(TokenReplacement.class);

    private Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
    private static TokenReplacement instance = new TokenReplacement();

    public static TokenReplacement getInstance() {
        return instance;
    }

    private TokenReplacement() {

    }

    /*
    Location the tokens in the list of strings and send them back.
     */
    public List<String> listTokens(Collection text) {
        List<String> rtn = new ArrayList<String>();
        for (Object line: text) {
            Matcher matcher = pattern.matcher(line.toString());
            while (matcher.find()) {
                rtn.add(matcher.group(1));
            }
        }
        return rtn;
    }

    public List<String> listTokens(Map<String, Object> textMap) {
        List<String> rtn = new ArrayList<String>();
        rtn.addAll(this.listTokens(textMap.values()));
        return rtn;
    }

    public List<String> replace(List<String> text, Map<String, Object> replacements) {
        return replace(text, replacements, Boolean.TRUE);
    }

    public List<String> replace(List<String> text, Map<String, Object> replacements, Boolean stripComments) {
        List<String> replacedText = new ArrayList<String>();
        Boolean inMultilineComment = Boolean.FALSE;
        for (String line : text) {
            if (StringUtils.isEmpty(line.trim())) {
                continue;
            }
            if (stripComments) {
                if (line.trim().startsWith("--") ) {
                    // Skip to next line
                    LOG.debug("Removed comment line: " + line);
                    continue;
                }
                if (line.trim().startsWith("/*")) {
                    inMultilineComment = Boolean.TRUE;
                    LOG.debug("Removed comment line: " + line);
                    continue;
                }
                if (inMultilineComment) {
                    if (line.trim().endsWith("*/")) {
                        inMultilineComment = Boolean.FALSE;
                        LOG.debug("Removed comment line: " + line);
                        continue;
                    } else {
                        LOG.debug("Removed comment line: " + line);
                        continue;
                    }
                }
            }

            Matcher matcher = pattern.matcher(line);
            StringBuilder builder = new StringBuilder();
            int i = 0;
            while (matcher.find()) {
                Object replacement = replacements.get(matcher.group(1));
                builder.append(line.substring(i, matcher.start()));
                if (replacement == null) {
//                    builder.append(matcher.group(0));
                    // Don't build back.  Remove the parameter and log it.
                    LOG.warn("Param Value not provided: " + matcher.group(0) + ". Will remove param.  This isn't " +
                            "necessarily a problem.");
                } else {
                    builder.append(replacement);
                }
                i = matcher.end();
            }
            builder.append(line.substring(i, line.length()));
            String lineFinal = builder.toString();
            if (!StringUtils.isEmpty(lineFinal.trim())) {
                replacedText.add(lineFinal);
            }
        }
        return replacedText;
    }

    public Map<String, Object> replaceInMap(Map<String, Object> paramMap, Map<String, Object> replacements) {
        // Create a copy we can manipulate.
        Map<String, Object> replacedParams = new HashMap<String, Object>(paramMap);

        for (String paramKey: replacedParams.keySet()) {
            String paramValue = (String)replacedParams.get(paramKey);
            Matcher matcher = pattern.matcher(paramValue.toString());
            StringBuilder builder = new StringBuilder();
            int i = 0;
            while (matcher.find()) {
                Object replacement = replacements.get(matcher.group(1));
                builder.append(paramValue.substring(i, matcher.start()));
                if (replacement == null)
                    builder.append(matcher.group(0));
                else
                    builder.append(replacement);
                i = matcher.end();
            }
            builder.append(paramValue.substring(i, paramValue.length()));
            replacedParams.put(paramKey, builder.toString());
        }
        return replacedParams;
    }

}

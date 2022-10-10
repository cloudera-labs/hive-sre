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

import com.cloudera.utils.hive.sre.MessageCode;

import java.text.MessageFormat;
import java.util.*;

public class Messages {

    private final BitSet bitSet;
    private final Map<Integer, Object[]> argMap = new TreeMap<Integer, Object[]>();

    public Messages(int size) {
        bitSet = new BitSet(size);
    }

    public void set(int bit, Object... args) {
        bitSet.set(bit);
        if (args != null) {
            argMap.put(bit, args);
        }
    }

    public void set(int bit) {
        bitSet.set(bit);
    }

    public long getReturnCode() {
        long rtn = 0;
        long[] messageSet = bitSet.toLongArray();
        for (long messageBit : messageSet) {
            rtn = rtn | messageBit;
        }
        return rtn;
    }

    public String getMessage(int bit) {
        String rtn = null;
        for (MessageCode messageCode : MessageCode.values()) {
            if (messageCode.getCode() == bit) {
                if (argMap.containsKey(bit)) {
                    Object[] args = argMap.get(messageCode.getCode());
                    rtn = MessageFormat.format(messageCode.getDesc(), args);
                } else {
                    rtn = messageCode.getDesc();
                }

            }
        }
        return rtn;
    }

    public String[] getMessages() {
        List<String> messageList = new ArrayList<String>();
        for (MessageCode messageCode : MessageCode.getCodes(bitSet)) {
            if (!argMap.containsKey(messageCode.getCode())) {
                messageList.add(messageCode.getCode()+":"+messageCode.getDesc());
            } else {
                messageList.add(messageCode.getCode()+":"+MessageFormat.format(messageCode.getDesc(), argMap.get(messageCode.getCode())));
            }
        }
        String[] rtn = messageList.toArray(new String[0]);

        return rtn;
    }

}

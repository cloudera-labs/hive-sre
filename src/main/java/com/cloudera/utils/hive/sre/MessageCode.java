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

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public enum MessageCode {
    // ERRORS
    ENCRYPT_PASSWORD(1, "Encrypted Password: {0}"),
    DECRYPT_PASSWORD(2, "Decrypted Password: {0}"),
    ENCRYPT_PASSWORD_ISSUE(3, "Issue Encrypting Password"),
    DECRYPTING_PASSWORD_ISSUE(4, "Issue Decrypting password"),
    PKEY_PASSWORD_CFG(5, "Need to include '-pkey' with '-p'.")
    ;


    private int code = 0;
    private String desc = null;

    MessageCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public long getLong() {
        double bitCode = Math.pow(2, code);
        return (long)bitCode;
    }

    public static List<MessageCode> getCodes(BitSet bitSet) {
        List<MessageCode> errors = new ArrayList<MessageCode>();
        for (int i=0;i<bitSet.size();i++) {
            if (bitSet.get(i)) {
                for (MessageCode error: MessageCode.values()) {
                    if (error.getCode() == i) {
                        errors.add(error);
                    }
                }
            }
        }
        return errors;
    }

}

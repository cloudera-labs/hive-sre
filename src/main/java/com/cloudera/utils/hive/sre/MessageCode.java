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

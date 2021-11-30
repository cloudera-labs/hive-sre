package com.cloudera.utils.hive.sre;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class UtilsTest {

    @Test
    public void dirToPartitionSpec_001() {
        String[] testSet = {"st=GA A/update_dt=2020-09-03"};

        for (String test: testSet) {
            String spec = null;
            try {
                spec = Utils.dirToPartitionSpec(test);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println(spec);
        }
    }

    @Test
    public void decodeTS_001() {
        String[] testSet = {"st=GA A/update_dt=2019-09-01 12%3A31%3A44.333"};

        for (String test: testSet) {
            String spec = null;
            try {
                spec = Utils.dirToPartitionSpec(test);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println(spec);
        }
    }
    @Test
    public void decodeTS_002() {
        String[] testSet = {"update_dt=2009-10-23/batch=test"};

        for (String test: testSet) {
            String spec = null;
            try {
                spec = Utils.dirToPartitionSpec(test);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println(spec);
        }
    }
}
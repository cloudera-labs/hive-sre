package com.cloudera.utils.hive.sre;

import java.io.FileNotFoundException;

public abstract class MetastoreProcess extends SreProcessBase {

    @Override
    public void init(ProcessContainer parent) throws FileNotFoundException {
        super.init(parent);
    }

    @Override
    public String toString() {
        return "MetastoreProcess{}";
    }
}

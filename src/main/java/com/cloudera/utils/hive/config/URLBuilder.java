package com.cloudera.utils.hive.config;

public class URLBuilder {

    public enum TransportMode {
        HTTP, BINARY;
    }

    private String knoxProxy = null;
    private Boolean kerberized = Boolean.FALSE;
    private Boolean ssl = Boolean.FALSE;
    private String trustStore = null;
    private String trustStorePassword = null;
    private String user = null;
    private String password = null;
    private TransportMode mode = TransportMode.BINARY;
    private Boolean legacyManaged = Boolean.FALSE;
    private String hivePrincipal = null;

}

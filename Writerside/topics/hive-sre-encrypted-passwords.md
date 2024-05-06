# Encrypted Passwords

Using Encrypted Password in the config 'yaml'

## Generate the Encrypted Password

Use the `-pkey <password-key>` and `-p <password-to-encrypt` options of `hive-sre`

`hive-sre u3 -pkey cloudera -p have-a-nice-day`

Will generate:
```
...
Encrypted password: HD1eNF8NMFahA2smLM9c4g==
```

Copy this encrypted password and place it in your configuration file for the connection `password`.  Repeat for the other passwords, if it's different, and paste it in the configuration as well.

## Running with Encrypted Passwords

Using the **same** `-pkey <password-key>` you used to generate the encrypted password, we'll run `hive-sre`

`hive-sre u3 -all|-hdp2|-hdp3|-cdh -pkey cloudera ...`

When the `-pkey` option is specified **WITHOUT** the `-p` option (used previously), `hive-sre` will understand to **decrypt** the configuration passwords before connecting to the resources.  If you receive jdbc connection exceptions, recheck the `-pkey` and encrypted password from before.

**NOTE**: The encrypted password process is shared by `u3`, `sre`, and `perf`.  It's not necessary to use different configs or password keys.

## Checking the 'encrypted' password with Key

If you're not sure the password is correct, copy the 'encrypted' password from the config file and run:

```
hive-sre u3 -pkey <password-key> -dp <encrypted_password>
```

For example:
```
# Encrypt
dstreev@e01 ~ $ hive-sre u3 -pkey cloudera -p have-a-nice-day
APP_DIR: /usr/local/hive-sre/bin
Running Host instance
Application JAVA_OPTS=-Djavax.net.ssl.trustStore=/home/dstreev/bin/certs/gateway-client-trust.jks -Djavax.net.ssl.trustStorePassword=changeit
PRG_ARGS= "u3" "-pkey" "cloudera" "-p" "have-a-nice-day"
openjdk version "1.8.0_272"
OpenJDK Runtime Environment (build 1.8.0_272-b10)
OpenJDK 64-Bit Server VM (build 25.272-b10, mixed mode)
Launching: u3
Using Config: /home/dstreev/.hive-sre/cfg/default.yaml
1:Encrypted Password: HD1eNF8NMFahA2smLM9c4g==

# Decrypt
dstreev@e01 ~ $ hive-sre u3 -pkey cloudera -dp HD1eNF8NMFahA2smLM9c4g==
APP_DIR: /usr/local/hive-sre/bin
Running Host instance
Application JAVA_OPTS=-Djavax.net.ssl.trustStore=/home/dstreev/bin/certs/gateway-client-trust.jks -Djavax.net.ssl.trustStorePassword=changeit
PRG_ARGS= "u3" "-pkey" "cloudera" "-dp" "HD1eNF8NMFahA2smLM9c4g=="
openjdk version "1.8.0_272"
OpenJDK Runtime Environment (build 1.8.0_272-b10)
OpenJDK 64-Bit Server VM (build 25.272-b10, mixed mode)
Launching: u3
Using Config: /home/dstreev/.hive-sre/cfg/default.yaml
2:Decrypted Password: have-a-nice-day
```

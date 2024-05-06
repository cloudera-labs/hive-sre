# Download and Install

## Get the Binary

USE THE PRE-BUILT BINARY!!!  You won't have the necessary dependencies to build this from scratch without downloading and building the 'Hadoop Cli'.

_**Don't Build, Download the LATEST binary here!!!**_

[![Download the LATEST Binary](images/download.png)](https://github.com/cloudera-labs/hive-sre/releases)

On the edgenode:
- Remove previous install directory `rm -rf hive-sre-install`
- Expand the tarball `tar zxvf hive-sre-dist.tar.gz`.
  > This produces a child `hive-sre-install` directory.
- Two options for installation:
    - As the root user (or `sudo`), run `hive-sre-install/setup.sh`. This will install the `hive-sre` packages in `/usr/local/hive-sre` and create symlinks for the executables in `/usr/local/bin`.  At this point, `hive-sre` should be available to all user and in the default path.
    - As the local user, run `hive-sre-install/setup.sh`.  This will install the `hive-sre` packages in `$HOME/.hive-sre` and create symlink in `$HOME/bin`.  Ensure `$HOME/bin` is in the users path and run `hive-sre`.

*DO NOT RUN `hive-sre` from the installation directory.*

If you install both options, your environment PATH will determine which one is run.  Make note of this because an upgrade may not be reachable.

This will create and install the `hive-sre` and `hive-sre-cli` applications to your path.

Try it out on a host with default configs (if kerberized, get a ticket first):

    hive-sre-cli
OR

    hive-sre


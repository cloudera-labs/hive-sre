#!/usr/bin/env sh

#
# Copyright (c) 2022. Cloudera, Inc. All Rights Reserved
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#        http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#
#

cd `dirname $0`

if (( $EUID != 0 )); then
  echo "Setting up as non-root user"
  BASE_DIR=$HOME/.hive-sre
else
  echo "Setting up as root user"
  BASE_DIR=/usr/local/hive-sre
fi

mkdir -p $BASE_DIR/bin
mkdir -p $BASE_DIR/lib

mkdir -p $HOME/.hive-sre/cfg
mkdir -p $HOME/.hive-sre/aux_libs

#echo "Make sure you copy your JDBC jars files to $HOME/.hive-sre/aux_libs"
#if [ ! -f $HOME/.hive-sre/cfg/default.yaml ]; then
#  cp default.template.yaml $HOME/.hive-sre/cfg/default.yaml
#  echo "A default.yaml template has been copied to $HOME/.hive-sre/cfg. Modify this for your environment."
#fi

# Cleanup previous installation
rm -f $BASE_DIR/lib/*.jar
rm -f $BASE_DIR/bin/*

cp -f hive-sre $BASE_DIR/bin
cp -f hive-sre-cli $BASE_DIR/bin

if [ -f hive-sre-shaded.jar ]; then
    cp -f hive-sre-shaded.jar $BASE_DIR/lib
fi

chmod -R +r $BASE_DIR
chmod +x $BASE_DIR/bin/hive-sre
chmod +x $BASE_DIR/bin/hive-sre-cli

if (( $EUID == 0 )); then
  echo "Setting up global links"
  ln -sf $BASE_DIR/bin/hive-sre /usr/local/bin/hive-sre
  ln -sf $BASE_DIR/bin/hive-sre-cli /usr/local/bin/hive-sre-cli
else
  mkdir -p $HOME/bin
  ln -sf $BASE_DIR/bin/hive-sre $HOME/bin/hive-sre
  ln -sf $BASE_DIR/bin/hive-sre-cli $HOME/bin/hive-sre-cli
  echo "Executable in $HOME/bin .  Add this to the environment path."
fi


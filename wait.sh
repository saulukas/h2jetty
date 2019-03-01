#!/bin/bash

#
#   Compiles and starts application, waits for SPACEBAR and repeats.
#

# prepare exiting while-cycle bellow via Ctrl-C
sigint_handler()
{
  kill $PID
  exit
}

trap sigint_handler SIGINT

SAGA="java -jar utils/saga.jar"
PATH_SEPARATOR=$($SAGA unix-win path-separator)

echo "---- setting classpath ----------------------------------------------------------------"

CLASS_PATH=$(mvn -q exec:exec -Dexec.executable=echo -Dexec.args="%classpath")
echo $CLASS_PATH | tr "$PATH_SEPARATOR" "\n"


while true; do

  mvn compile

  if [ $? -ne 0 ]; then 
     echo "Compilation failed. Press 'C' to recompile..."
     key=x
     while [ "$key" != 'c'  -a  "$key" != 'C' ]; do
       read -n1 -r key
     done
  else
    echo "---- starting Jetty -------------------------------------------------------------------"
    java -cp "$CLASS_PATH" h2jetty.H2JettyServer &
    PID=$!
    echo "Press 'R' to restart or Ctrl-C to stop..."
    key=x
    while [ "$key" != 'r'  -a  "$key" != 'R' ]; do
      read -n1 -r key
    done
    kill -9 $PID
  fi

done

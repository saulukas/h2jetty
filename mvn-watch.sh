#!/bin/bash

# prepare exiting while-cycle bellow via Ctrl-C
sigint_handler()
{
  kill $PID
  exit
}

trap sigint_handler SIGINT

SAGA="java -jar utils/saga.jar"
PATH_SEPARATOR=$($SAGA unix-win path-separator)

echo "----"
echo "---- setting classpath ----------------------------------------------------------------"
echo "----"

CLASS_PATH=$(mvn -q exec:exec -Dexec.executable=echo -Dexec.args="%classpath")
echo $CLASS_PATH | tr "$PATH_SEPARATOR" "\n"

while true; do
  echo "----"
  echo "---- starting Jetty -------------------------------------------------------------------"
  echo "----"
  java -cp $CLASS_PATH h2jetty.H2JettyServer &
  PID=$!
  echo $CLASS_PATH | tr ":" " "
  $SAGA watch $(echo $CLASS_PATH | tr "$PATH_SEPARATOR" " ")
  kill -9 $PID
done

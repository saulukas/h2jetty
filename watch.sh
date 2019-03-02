#!/bin/bash

#
#   Starts application and watches maven's CLASSPATH for changes.
#   Restarts application on any target/dependency-libs change.
#
#   Nice with "compile-on-save" IDE feature.
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
  echo "---- starting Jetty -------------------------------------------------------------------"
  java -cp "$CLASS_PATH" h2jetty.H2JettyServer &
  PID=$!
  $SAGA watch "$CLASS_PATH"
  echo " "
  echo "---- killing Jetty --------------------------------------------------------------------"
  kill -9 $PID
done

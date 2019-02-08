#!/bin/bash
#sudo apt-get install inotify-tools

sigint_handler()
{
  kill $PID
  exit
}

trap sigint_handler SIGINT

echo "----"
echo "---- setting classpath ----------------------------------------------------------------"
echo "----"
CLASSPATH=$(mvn -q exec:exec -Dexec.executable=echo -Dexec.args="%classpath")
echo $CLASSPATH | tr ":" "\n"

while true; do
  echo "----"
  echo "---- starting Jetty -------------------------------------------------------------------"
  echo "----"
  java -cp $CLASSPATH h2jetty.H2JettyServer &
  PID=$!
  inotifywait -e modify -e move -e create -e delete -e attrib -r $(echo $CLASSPATH | tr ":" " ")
  kill -9 $PID
done

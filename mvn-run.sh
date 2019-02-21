#/bin/bash

SAGA="java -jar utils/saga.jar"
PATH_SEPARATOR=$($SAGA unix-win path-separator)

echo "---- setting classpath ------------------------------------------------"
CLASS_PATH=$(mvn -q exec:exec -Dexec.executable=echo -Dexec.args="%classpath")
echo $CLASS_PATH | tr "$PATH_SEPARATOR" "\n"

echo "---- starting Jetty ---------------------------------------------------"
java -cp $CLASS_PATH h2jetty.H2JettyServer

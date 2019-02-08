#/bin/bash

echo "---- setting classpath ------------------------------------------------"
CLASSPATH=$(mvn -q exec:exec -Dexec.executable=echo -Dexec.args="%classpath")

echo "---- starting Jetty ---------------------------------------------------"
java -cp $CLASSPATH h2jetty.H2JettyServer

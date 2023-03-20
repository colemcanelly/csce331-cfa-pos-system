
# Depends on user
JAVAFX=/mnt/c/Java/javafx-sdk-19-linux

# Constants
CTRL_MOD=javafx.controls
FXML_MOD=javafx.fxml

PACKAGE=application
MAIN=Main

PQSQL_LOC=./lib
PGSQL=postgresql-42.2.8.jar
PGSQL_DRIVER=-Djdbc.drivers=org.postgresql.Driver

all : clean
	javac --module-path ${JAVAFX}:${JAVAFX}/lib --add-modules ${CTRL_MOD},${FXML_MOD} ./src/${PACKAGE}/${MAIN}.java ./src/${PACKAGE}/controllers/*.java
	java -cp ./src:${PQSQL_LOC}/${PGSQL} ${PGSQL_DRIVER} --module-path ${JAVAFX}:${JAVAFX}/lib --add-modules ${CTRL_MOD},${FXML_MOD} ${PACKAGE}.${MAIN}

clean :
	@rm -f ./src/${PACKAGE}/*.class
	@rm -f ./src/${PACKAGE}/controllers/*.class
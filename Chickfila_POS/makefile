
# Depends on user
JAVAFX=/mnt/c/Java/javafx-sdk-19-linux
JAVAFX_LIB=/mnt/c/Java/javafx-sdk-19-linux/lib


# Constants
CTRL_MOD=javafx.controls
FXML_MOD=javafx.fxml

PACKAGE=application
MAIN=Main

PQSQL_LOC=./lib
PGSQL=postgresql-42.2.8.jar
PGSQL_DRIVER=-Djdbc.drivers=org.postgresql.Driver

all : clean
	javac --module-path ${JAVAFX}:${JAVAFX_LIB} --add-modules ${CTRL_MOD},${FXML_MOD} ./${PACKAGE}/${MAIN}.java ./${PACKAGE}/controllers/*.java
	java -cp .:${PQSQL_LOC}/${PGSQL} ${PGSQL_DRIVER} --module-path ${JAVAFX}:${JAVAFX_LIB} --add-modules ${CTRL_MOD},${FXML_MOD} ${PACKAGE}.${MAIN}

clean :
	@rm -f ./${PACKAGE}/*.class
	@rm -f ./${PACKAGE}/controllers/*.class


# all :
# 	# java --module-path "C:\javafx-sdk-19.0.2.1\lib" --add-modules javafx.controls,javafx.fxml -Dfile.encoding=UTF-8 -classpath "C:\Users\westo\eclipse-workspace\phase3Chick\bin;C:\javafx-sdk-19.0.2.1\lib\javafx.base.jar;C:\javafx-sdk-19.0.2.1\lib\javafx.controls.jar;C:\javafx-sdk-19.0.2.1\lib\javafx.fxml.jar;C:\javafx-sdk-19.0.2.1\lib\javafx.graphics.jar;C:\javafx-sdk-19.0.2.1\lib\javafx.media.jar;C:\javafx-sdk-19.0.2.1\lib\javafx.swing.jar;C:\javafx-sdk-19.0.2.1\lib\javafx.web.jar;C:\javafx-sdk-19.0.2.1\lib\javafx-swt.jar;C:\Users\westo\OneDrive\Documents\CSCE331\csce331-cfa-pos-system\Chickfila_POS\lib\postgresql-42.2.8.jar" -XX:+ShowCodeDetailsInExceptionMessages application.Main
# 	java -cp "C:\javafx-sdk-19.0.2.1\lib\*" -Dfile.encoding=UTF-8 -classpath "C:\Users\westo\eclipse-workspace\phase3Chick\bin;C:\Users\westo\OneDrive\Documents\CSCE331\csce331-cfa-pos-system\Chickfila_POS\lib\postgresql-42.2.8.jar" -XX:+ShowCodeDetailsInExceptionMessages application.Main




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


docs:
	javadoc -cp ./src:${PQSQL_LOC}/${PGSQL} --module-path ${JAVAFX}:${JAVAFX}/lib --add-modules ${CTRL_MOD},${FXML_MOD} -d ./documentation application application.controllers

clean :
	@rm -f ./src/${PACKAGE}/*.class
	@rm -f ./src/${PACKAGE}/controllers/*.class
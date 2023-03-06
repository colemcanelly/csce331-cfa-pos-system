all : clean
	javac jdbcpostgreSQL.java dbSetup.java
	java -cp .:postgresql-42.2.8.jar jdbcpostgreSQL

clean :
	@rm -f *.class

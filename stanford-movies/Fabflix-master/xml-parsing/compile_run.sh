
echo "Building the tables and inserting the original data set."
mysql -uclassta -pclassta < add_movie.sql

echo "Compiling the data parser"
rm -rf ./classes && mkdir classes
javac -classpath .:lib/mysql-connector-java-5.0.8-bin.jar -Xlint -d classes src/*.java
cd classes
echo "Running the data parser. This should only take ~30 seconds or less..."
java -cp .:../lib/mysql-connector-java-5.0.8-bin.jar SAXParser
cd ..
echo "Done!"

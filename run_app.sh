# check you correctly set your PATH
echo "Java compiler: $(which javac)"
echo "Java runner: $(which java)"

# compile the classes
javac -d . src/main/java/com/minesweeper/*.java

# run the main class
java com.minesweeper.GamePlayer
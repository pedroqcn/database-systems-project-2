# Project 2

### Name: Pedro Nogueira
### Course: CS4432 - Database Systems II

## Section I: How to run the program

The project is written in Java and uses only the standard library.

1. Open the terminal and navigate to the root directory of the project.

2. Compile all source files:

    ```
    javac *.java
    ```

3. Run the program:

    ```
    java Main
    ```

4. Follow the prompts to interact with the program. To stop the program, press Ctrl+C.

## Section II: What is working

All sections of the spec are implemented and should be working.

## Section III: Design Decisions

The project is organized into four classes:

- `Main.java`: contains the command loop. It reads user input and dispatches commands to other classes.
- `FileReader.java`: reads data from a file and stores it in memory.
- `IndexBuilder.java`: builds an index from the data in memory.
- `QueryExecutor.java`: executes queries and prints the results.
# Java Collections – Performance Measurement

## Project Description

This project compares the performance of basic Java collections (`ArrayList`, `LinkedList`, `HashSet`, `TreeSet`) for different data types (including `Integer`, `Double`, `Person`, `Student`, `MyColor`, `Book`). The program allows testing operations such as:

- Reading by index
- Frequency of adding and removing elements
- Searching for an element
- Checking if an element exists in the collection

The project is designed following SOLID principles with separation into interfaces and implementing classes:

- `CollectionDataGenerator` – generates random data for collections
- `CollectionPerformanceTester` – performs performance tests
- `TestResultPrinter` – presents results (console output or CSV file)

## How it works

The user selects from the console:

- Data type (`INTEGER`, `DOUBLE`, `PERSON`, `STUDENT`, `MYCOLOR`, `BOOK`)
- Collection type (`ARRAYLIST`, `LINKEDLIST`, `HASHSET`, `TREESET`)
- Collection size
- Test type (`READBYINDEX`, `ADDREMOVEFREQUENCY`, `SEARCHOFELEMENT`, `ISINCOLLECTION`)
- Result presentation method (`CSV`, `CONSOLEPRINT`)

The program then:

- Generates a collection filled with random data
- Performs the selected test on that collection
- Saves or prints the result (execution time and optional additional information)

## Sample data

For the `STUDENT` or `BOOK` types, the program uses custom classes with implemented `equals`, `hashCode`, and `compareTo` methods, enabling their correct use in structures like `TreeSet`.

## Technologies

- Java 17+
- Standard API (`java.util`, `java.io`)
- Stream API

## How to run

1. Compile `S34366Project01.java`.
2. Run the program.
3. Follow the instructions displayed in the console.

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public
    class S34366Project01 {
    public static void main(String[] args) {
        S34366Project01 project = new S34366Project01();
        project.run();
    }

    //------------------------------------------
    public void run() {
        Scanner scanner = new Scanner(System.in);

        DataType selectedDataType = getEnumChoice(scanner, DataType.class, "Wybierz typ danych: ");
        CollectionType selectedColType = getEnumChoice(scanner, CollectionType.class, "Wybierz typ kolekcji: ");
        int size = sizeOfCollection(scanner);
        TestType selectedTestType = getEnumChoice(scanner, TestType.class, "Wybierz rodzaj testu: ");

        Object elementToSearch = null;
        if (selectedTestType == TestType.SEARCHOFELEMENT || selectedTestType == TestType.ISINCOLLECTION) {
            elementToSearch = getValueToSearch(scanner, selectedDataType);
        }

        SaveType selectedSaveType = getEnumChoice(scanner, SaveType.class, "Wybierz formę prezentacji wyników: ");

        CollectionDataGenerator generator = new RandomCollectionDataGenerator();
        Collection<?> collection = generator.generate(selectedDataType, size, selectedColType);

        CollectionPerformanceTester tester = new StdCollectionPerformanceTester();

        String result = tester.performTest(
                selectedTestType, collection,
                selectedTestType == TestType.ADDREMOVEFREQUENCY || selectedTestType == TestType.READBYINDEX ? size : elementToSearch, selectedDataType
        );

        TestResultPrinter printer = new ConsoleAndCSVResultPrinter();
        printer.print(result, selectedSaveType);

        scanner.close();
    }

    interface CollectionDataGenerator {
        Collection<?> generate(DataType type, int size, CollectionType colType);
    }
    class RandomCollectionDataGenerator implements CollectionDataGenerator {

        public Collection<?> generate(DataType type, int size, CollectionType colType) {
            Random rand = new Random();
            return switch (colType) {
                case ARRAYLIST -> generateStream(type, size, rand)
                        .collect(Collectors
                                .toCollection(ArrayList::new));
                case LINKEDLIST -> generateStream(type, size, rand)
                        .collect(Collectors
                                .toCollection(LinkedList::new));
                case HASHSET -> generateStream(type, size, rand)
                        .collect(Collectors
                                .toCollection(HashSet::new));
                case TREESET -> generateStream(type, size, rand)
                        .collect(Collectors
                                .toCollection(TreeSet::new));
            };
        }

        private Stream<Object> generateStream(DataType type, int size, Random rand) {
            return switch (type) {
                case INTEGER -> Stream.generate(() -> rand.nextInt(1000))
                        .limit(size).map((i) -> (Object) i);
                case DOUBLE -> Stream.generate(rand::nextDouble)
                        .limit(size).map((i) -> (Object) i);
                case PERSON ->
                        Stream.generate(() -> new Person(
                                        rand.nextInt(51) + 1975,
                                        "Person" + rand.nextInt(10000)))
                                .limit(size).map(i -> (Object) i);
                case STUDENT ->
                        Stream.generate(() -> new Student(
                                        rand.nextInt(51) + 1975,
                                        "Student" + rand.nextInt(10000),
                                        rand.nextInt(100000)))
                                .limit(size).map(i -> (Object) i);
                case MYCOLOR ->
                        Stream.generate(() -> new MyColor(
                                        rand.nextInt(256),
                                        rand.nextInt(256),
                                        rand.nextInt(256)))
                                .limit(size).map(i -> (Object) i);
                case BOOK ->
                        Stream.generate(() -> new Book(
                                        "Book" + rand.nextInt(100),
                                        rand.nextInt(2026),
                                        rand.nextInt(501)))
                                .limit(size).map(i -> (Object) i);
            };
        }
    }

    interface CollectionPerformanceTester {
        String performTest(TestType testType, Collection<?> collection, Object elementToSearch, DataType dataType);
    }
    class StdCollectionPerformanceTester implements CollectionPerformanceTester {
        public String performTest(TestType testType, Collection<?> collection, Object elementToSearch, DataType dataType) {
            return switch (testType) {
                case READBYINDEX -> testReadByIndex(collection);
                case ADDREMOVEFREQUENCY -> testAddRemoveFrequency(collection, (Integer) elementToSearch, dataType);
                case SEARCHOFELEMENT -> testSearchElement(collection, elementToSearch);
                case ISINCOLLECTION -> testIfExists(collection, elementToSearch);
            };
        }

        private String testReadByIndex(Collection<?> collection) {
            if (!(collection instanceof List<?> list)) {
                return "Test odczytu po indeksie nie jest obsługiwany dla tej kolekcji.\n";
            }
            long timeTaken = measureTime(() -> {
                for (Object element : list);
            });
            return "Czas odczytu po indeksie: " + timeTaken + " ns\n";
        }

        private String testAddRemoveFrequency(Collection<?> collection, int elementCount, DataType type) {
            long totalAddTime = 0;

            if (collection instanceof List<?> list) {
                List<Object> objectList = (List<Object>) list;
                for (int i = 0; i < elementCount; i++) {
                    Object element = generateRandomElement(type);
                    totalAddTime += measureTime(() -> objectList.add(element));
                }
            } else if (collection instanceof Set<?> set) {
                Set<Object> objectSet = (Set<Object>) set;
                for (int i = 0; i < elementCount; i++) {
                    Object element = generateRandomElement(type);
                    totalAddTime += measureTime(() -> objectSet.add(element));
                }
            }

            long totalRemoveTime = measureTime(collection::clear);
            long averageAddTime = elementCount > 0 ? totalAddTime / elementCount : 0;

            return "Całkowity czas dodawania: " + totalAddTime + " ns\n" + "Średni czas dodawania: " + averageAddTime + " ns\n" + "Całkowity czas usuwania: " + totalRemoveTime + " ns\n";
        }

        private String testSearchElement(Collection<?> collection, Object elementToSearch) {
            final String[] result = new String[1];

            long timeTaken = measureTime(() -> {
                if (collection instanceof List<?> list) {
                    int index = list.indexOf(elementToSearch);
                    result[0] = (index != -1 ? "Znaleziono na indeksie: " + index : "Nie znaleziono w liście") + "\n";
                } else {
                    boolean found = collection.contains(elementToSearch);
                    result[0] = (found ? "Znaleziono w zbiorze." : "Nie znaleziono w zbiorze.") + "\n";
                }
            });

            return result[0] + "Czas wyszukiwania: " + timeTaken + " ns\n";
        }

        private String testIfExists(Collection<?> collection, Object elementToSearch) {
            long start = System.nanoTime();
            boolean exists = collection.contains(elementToSearch);
            long end = System.nanoTime();
            return (exists ? "Element istnieje." : "Element nie istnieje.") + " Czas sprawdzania: " + (end - start) + " ns\n";
        }
    }

    interface TestResultPrinter {
        void print(String result, SaveType saveType);
    }
    class ConsoleAndCSVResultPrinter implements TestResultPrinter {
        public void print(String result, SaveType saveType) {
            switch (saveType) {
                case CONSOLEPRINT -> System.out.println("Wyniki testu:\n" + result);
                case CSV -> {
                    try (FileWriter writer = new FileWriter("results.csv")) {
                        writer.write(result);
                        System.out.println("Zapisano do results.csv");
                    } catch (IOException e) {
                        System.err.println("Błąd zapisu: " + e.getMessage());
                    }
                }
            }
        }
    }

    //------------------------------------------

    private int sizeOfCollection(Scanner scanner) {
        System.out.println("Wybierz rozmiar kolekcji: " + "\n1. 100" + "\n2. 500" + "\n3. 1000" + "\n4. 10000" + "\n5. Dowolny");
        int choice = scanner.nextInt();
        return switch (choice) {
            case 1 -> 100;
            case 2 -> 500;
            case 3 -> 1000;
            case 4 -> 10000;
            case 5 -> {
                System.out.println("Podaj wartość: ");
                yield scanner.nextInt();
            }
            default -> throw new IllegalArgumentException("Niepoprawny wybór! ");
        };
    }

    private Object getValueToSearch(Scanner scanner, DataType type) {
        System.out.println("Podaj wartość do wyszukania: ");
        return switch (type) {
            case INTEGER -> {
                System.out.println("Podaj liczbę całkowitą [0 - 999]: ");
                yield scanner.nextInt();
            }
            case DOUBLE -> {
                System.out.println("Podaj liczbe double: ");
                yield scanner.nextDouble();
            }
            case PERSON -> {
                System.out.println("Imie (Person[0 - 9999]): ");
                String name = scanner.next();
                System.out.println("Rok urodzenia [1975 - 2025]: ");
                int year = scanner.nextInt();

                yield new Person(year, name);
            }
            case MYCOLOR -> {
                System.out.println("Wartosci RGB [0 - 255 dla kazdej]: ");
                System.out.println("R:");
                int r = scanner.nextInt();
                System.out.println("G:");
                int g = scanner.nextInt();
                System.out.println("B:");
                int b = scanner.nextInt();
                yield new MyColor(r, g, b);
            }
            case STUDENT -> {
                System.out.println("Nazwa studenta (Student[0 - 9999]): ");
                String name = scanner.next();
                System.out.println("Rok urodzenia [1975 - 2025]: ");
                int year = scanner.nextInt();
                System.out.println("ID studenta [0 - 99999]: ");
                int studentId = scanner.nextInt();
                yield new Student(year, name, studentId);
            }
            case BOOK -> {
                System.out.println("Nazwa ksiazki (Book[0 - 99]): ");
                String bookName = scanner.next();
                System.out.println("Rok wydania [1975 - 2025]: ");
                int year = scanner.nextInt();
                System.out.println("Liczba stron [0 - 500]: ");
                int pages = scanner.nextInt();

                yield new Book(bookName, year, pages);
            }
        };
    }

    private long measureTime(Runnable method) {
        long start = System.nanoTime();
        method.run();
        long end = System.nanoTime();
        return end - start;
    }

    private Object generateRandomElement(DataType type) {
        Random random = new Random();
        return switch (type) {
            case INTEGER -> random.nextInt(1000);
            case DOUBLE -> random.nextDouble();
            case PERSON -> new Person(
                    random.nextInt(51) + 1975,
                        "Person" + random.nextInt(100));
            case MYCOLOR -> new MyColor(
                    random.nextInt(256),
                    random.nextInt(256),
                    random.nextInt(256));
            case STUDENT -> new Student(
                            random.nextInt(50) + 1975,
                                "Student" + random.nextInt(100),
                                        random.nextInt(1000));
            case BOOK -> new Book(
                    "Book" + random.nextInt(100),
                    random.nextInt(2026),
                    random.nextInt(501));
        };
    }

    private <T extends Enum<T>> T getEnumChoice(Scanner scanner, Class<T> enumClass, String message) {
        System.out.println(message);
        T[] values = enumClass.getEnumConstants();
        for (int i = 0; i < values.length; i++) {
            System.out.println((i + 1) + ". " + values[i]);
        }
        int choice = scanner.nextInt();
        return values[choice - 1];
    }

    //------------------------------------------

    class Person implements Comparable<Person> {
        private final int birthYear;
        private final String name;

        public Person(int birthYear, String name) {
            this.birthYear = birthYear;
            this.name = name;
        }

        @Override
        public int compareTo(Person other) {
            int yearComparison = Integer.compare(this.birthYear, other.birthYear);
            return (yearComparison != 0) ? yearComparison : this.name.compareTo(other.name);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return birthYear == person.birthYear && Objects.equals(name, person.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(birthYear, name );
        }
    }

    class MyColor implements Comparable<MyColor> {
        private final int red;
        private final int green;
        private final int blue;

        public MyColor(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        @Override
        public int compareTo(MyColor other) {
            int colorComparison = Integer.compare(this.red + this.green + this.blue, other.red + other.green + other.blue);
            if (colorComparison != 0) return colorComparison;
            colorComparison = Integer.compare(this.red, other.red);
            if (colorComparison != 0) return colorComparison;
            colorComparison = Integer.compare(this.green, other.green);
            return (colorComparison != 0) ? colorComparison : Integer.compare(this.blue, other.blue);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MyColor myColor = (MyColor) o;
            return red == myColor.red && green == myColor.green && blue == myColor.blue;
        }

        @Override
        public int hashCode() {
            return Objects.hash(red, green, blue);
        }
    }

    class Student extends Person {
        private final int studentId;

        public Student(int birthYear, String name, int studentId) {
            super(birthYear, name);
            this.studentId = studentId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!super.equals(o)) return false;
            if (getClass() != o.getClass()) return false;
            Student student = (Student) o;
            return studentId == student.studentId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), studentId);
        }
    }

    class Book implements Comparable<Book> {
        private final String bookName;
        private final int publicationYear;
        private final int pages;

        public Book(String bookName, int publicationYear, int pages) {
            this.bookName = bookName;
            this.publicationYear = publicationYear;
            this.pages = pages;
        }

        @Override
        public int compareTo(Book other) {
            int pageComparison = Integer.compare(this.pages, other.pages);
            return (pageComparison != 0) ? pageComparison : this.bookName.compareTo(other.bookName);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Book book = (Book) o;
            return publicationYear == book.publicationYear && pages == book.pages && Objects.equals(bookName, book.bookName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(bookName, publicationYear, pages);
        }
    }

    //--------------------------------

    enum CollectionType {ARRAYLIST, LINKEDLIST, HASHSET, TREESET}

    enum TestType {READBYINDEX, ADDREMOVEFREQUENCY, SEARCHOFELEMENT, ISINCOLLECTION}

    enum DataType {INTEGER, DOUBLE, PERSON, MYCOLOR, STUDENT, BOOK}

    enum SaveType {CSV, CONSOLEPRINT}
}
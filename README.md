# Kolekcje w Javie – Pomiar Wydajności

## Opis projektu

Ten projekt służy do **porównywania wydajności podstawowych kolekcji w Javie** (`ArrayList`, `LinkedList`, `HashSet`, `TreeSet`) dla różnych typów danych (m.in. `Integer`, `Double`, `Person`, `Student`, `MyColor`, `Book`). Program umożliwia testowanie operacji takich jak:

- odczyt po indeksie,
- częstotliwość dodawania i usuwania elementów,
- wyszukiwanie elementu,
- sprawdzenie, czy element istnieje w kolekcji.

Projekt został napisany z użyciem zasady SOLID i podziału na interfejsy i klasy implementujące:

- `CollectionDataGenerator` – generowanie danych losowych do kolekcji,
- `CollectionPerformanceTester` – przeprowadzanie testów wydajnościowych,
- `TestResultPrinter` – prezentacja wyników (konsola lub plik CSV).

## Jak to działa?

1. **Użytkownik wybiera z konsoli:**
   - typ danych (`INTEGER`, `DOUBLE`, `PERSON`, `STUDENT`, `MYCOLOR`, `BOOK`),
   - typ kolekcji (`ARRAYLIST`, `LINKEDLIST`, `HASHSET`, `TREESET`),
   - rozmiar kolekcji,
   - rodzaj testu (`READBYINDEX`, `ADDREMOVEFREQUENCY`, `SEARCHOFELEMENT`, `ISINCOLLECTION`),
   - sposób prezentacji wyników (`CSV`, `CONSOLEPRINT`).

2. **Program:**
   - generuje kolekcję z losowymi danymi,
   - wykonuje wybrany test na tej kolekcji,
   - zapisuje lub drukuje wynik (czas wykonania i ewentualne informacje pomocnicze).

## Przykład danych

Dla typu `STUDENT` lub `BOOK` program wykorzystuje własne klasy z zaimplementowanymi metodami `equals`, `hashCode` i `compareTo`, dzięki czemu możliwe jest ich poprawne użycie w strukturach takich jak `TreeSet`.

## Technologie

- Java 17+
- API standardowe (`java.util`, `java.io`)
- Obsługa strumieni (`Stream API`)

## Uruchamianie

1. Skompiluj plik `S34366Project01.java`.
2. Uruchom program.
3. Postępuj zgodnie z instrukcjami w konsoli.

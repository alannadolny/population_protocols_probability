# POPULATION PROTOCOLS PROBABILITY

## SPIS TREŚCI

- [UŻYTE TECHNOLOGIE / BIBLIOTEKI](#użyte-technologie-/-biblioteki)
- [KONCEPCJA PROJEKTU](#koncepcja-projektu)
- [CEL PROJEKTU](#cel-projektu)
- [STATUS PROJEKTU](#status-projektu)

## UŻYTE TECHNOLOGIE / BLIBLOTEKI

- junit
- javafx
- ejml

## KONCEPCJA PROJEKTU

Koncepcją projektu było obliczanie prawdopodobieństw w protokole populacyjnym z 3 stanami (Y - Yes, N - No, U - Unknow). Jest to problem głosowania większościowego (Majority/Consensus problem), z następującą funkcją przejścia:

```
(Y, N) -> U
(N, Y) -> U
(Y, Y) -> Y
(N, N) -> N
```

## CEL PROJEKTU

Główny celem projektu było porównanie macierzy rzadkich oraz macierzy gęstych. Dodatkowo porównywane były wyniki otrzymane za pomocą metod iteracyjnych (Gauss-Seidel, Gauss-Jacobi) oraz metod eliminacji Gaussa z częściowym wyborem. Wiarygodność metod iteracyjnych została sprawdzona na podstawie symulacji Monte Carlo. Porównania odbywały się pod względem wydajności oraz dokładności otrzymanych wyników (w pomiarach używany był typ Float, Double oraz własna klasa Fractions bazująca na BigInteger). Wszystkie otrzymane wyniki zostały podsumowane w pliku: [sprawozdanie](https://github.com/Alancioo/population_protocols_probability/blob/main/algorytmy_numeryczne_projekt_3.pdf)

## STATUS PROJEKTU

Projekt został zakończony (16.05.2022)

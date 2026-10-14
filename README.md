# Darwin Simulation - Projekt Ewolucja

## Opis projektu

Jest to symulacja świata, w którym samodzielnie żyją zwierzęta. Celem projektu jest przedstawienie środowiska i występujących w nim procesów, w którym zwierzęta przemieszczają się, poszukują pożywienia, rozmnażają się i dostosowują się do panujących warunków dzięki systemowi genów i doborowi naturalnemu. W trakcie symulacji dzięki wykresom dynamicznie generowanym, widać stan flory i fauny w zależności sytuacji.

## Mechanika Świata

Świat składa się z prostokątnej siatki pól, składającej się z dwóch biomów, gdzie każdego dnia losowo pojawiają się nowe rośliny, zgodnie z **zasadą Pareto**:

- **Stepy:** Obszar w którym jest 20% szans na wyrośnięcie rośliny (80% powierzchni mapy).
- **Dżungla:** Obszar w którym jest 80% szans na wyrośnięcie rośliny (20% powierzchni mapy).

### Zwierzęta

Każde zwierzę posiada:

- **Koordynaty (x, y):** Pozycja na mapie.
- **Energię:** Zasób niezbędny do przeżycia. Zużywa energię każdego dnia, a wzrasta po zjedzeniu rośliny.
- **Orientację:** Jeden z 8 kierunków świata (cyfry 0-7, gdzie 0 to północ).
- **Genotyp:** Ciąg genów (cyfry z zakresu 0-7), który steruje ruchem zwierzaka.

### Ruch i Geny

Zwierzęta poruszają się zgodnie ze swoim genomem w każdym dniu symulacji:

1. Zwierzę obraca się w kierunku określonym przez aktualnie aktywny gen.
2. Przemieszcza się o jedno pole w wyznaczonym kierunku.
3. Aktywuje kolejny gen w sekwencji.

### Topologia mapy

Mapa działa jak kula ziemska – wyjście poza lewą/prawą krawędź powoduje pojawienie się zwierzęcia z drugiej strony. Bieguny (północ/południe) odbijają zwierzę, zmieniając jego kierunek na przeciwny.

## Konsumpcja i Rozmnażanie

- **Jedzenie:** Zwierzę zjada roślinę po wejściu na jej pole, odzyskując określoną ilość energii.
- **Rozmnażanie:** Dwa najedzone zwierzaki na tym samym polu mogą się rozmnożyć. Potomek otrzymuje energię od rodziców oraz genotyp będący krzyżówką ich genów, zgodnie z ilością energii którą posiadają rodzice.
- **Mutacje:** Podczas narodzin potomka jego geny mogą ulec losowym zmianom.

## Przebieg Symulacji (Krok po Kroku)

Każdy dzień symulacji obejmuje:

1. Usunięcie martwych osobników.
2. Obrót i przemieszczenie zwierzaków.
3. Konsumpcję roślin.
4. Rozmnażanie się najedzonych par.
5. Wyrastanie nowych roślin.

## Efekt FOMO

Symulację można uruchomić z modyfikacją "FOMO", która polega na wywołaniu efektu 'fear of missing out', przez który zwierzęta skupiają się w dużych grupach. W efekcie wiele zwierząt znajduje się na małej powierzchni, zatem rozmnażalność jest duża, oraz ilość jedzenia jest mała. Z czasem wiele zwierząt umiera z głodu.

## Statystyki i Wykresy

W symulacji jest możliwość obserwowania co dzieje się w świecie, a także generowania statystyk oraz wykresów w czasie rzeczywistym, z których można wyciągnąć wnioski na temat związku ilości roślin na mapie z ilością zwierząt, a także jak geny wpływają na przetrwanie danych rodzin zwierząt.

## Parametry Konfiguracyjne

Symulacja pozwala na dostosowanie parametrów, takich jak:

- Wymiary mapy (szerokość i wysokość).
- Startowa liczba roślin i zwierząt.
- Energia startowa oraz energia z pożywienia.
- Energia potrzebna do przemieszczenia oraz rozmnażania.
- Ilość trawy rosnącej na mapie.
- Liczba mutacji i długość genomu.
- Aktywacja efektu FOMO oraz jego parametry.

## Technologie

- **Język:** Java 25
- **Interfejs graficzny:** JavaFX
- **System budowania:** Gradle
- **Architektura:** Model-View-Presenter (MVP)

## Uruchamianie Projektu

Aby uruchomić projekt należy wykonać:
- gradle sync
- run WorldGUI (java/agh/ics/oop/WorldGUI.java)
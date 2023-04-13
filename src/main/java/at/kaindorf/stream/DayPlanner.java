package at.kaindorf.stream;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Stream;

/**
 * <h3>Created by IntelliJ IDEA.</h3><br>
 * <b>Project:</b> Programming-Exercise - Map Collections<br>
 * <b>User:</b> Simon Schoeggler<br>
 * <b>Date:</b> 12. April 2023<br>
 * <b>Time:</b> 08:55<br>
 */

public class DayPlanner {
    private Map<LocalDate, ArrayList<Entry>> map = new LinkedHashMap<>();

    public static void main(String[] args) {
        DayPlanner dayPlanner = new DayPlanner();

        dayPlanner.addEntry(LocalDate.of(2019, 10, 19), new Entry(LocalTime.of(17, 0), "Party"));
        dayPlanner.addEntry(LocalDate.of(2019, 10, 19), new Entry(LocalTime.of(10, 30), "Fussball"));
        dayPlanner.addEntry(LocalDate.of(2019, 10, 18), new Entry(LocalTime.of(17, 0), "POS Aufgabe abgeben"));
        dayPlanner.addEntry(LocalDate.of(2019, 10, 18), new Entry(LocalTime.of(8, 0), "Pair Programming POS"));
        dayPlanner.addEntry(LocalDate.of(2019, 10, 18), new Entry(LocalTime.of(6, 30), "Frühstück"));

        File file = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "entries.csv").toFile();
        dayPlanner.loadEntries(file);
        dayPlanner.showAllDays();
        dayPlanner.showEntriesOfDay(LocalDate.of(2019, 10, 18));
        dayPlanner.showEntriesOfDay(LocalDate.of(2019, 10, 19));
        System.out.println("------------------");
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Enter a date (yyyy-MM-dd): ");
            String date = scanner.nextLine();
            LocalDate localDate = LocalDate.parse(date);
            dayPlanner.showEntriesOfDay(localDate);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date");
        }

        // grouped by years
        /*
        dayPlanner.map.keySet().stream().collect(Collectors.groupingBy(LocalDate::getYear)).forEach((year, dates) -> {
            System.out.println(year);
            dates.forEach(date -> {
                dayPlanner.map.get(date).forEach(System.out::println);
            });
        });
        */

    }

    public void loadEntries(File file) {
        try (Stream<String[]> stream = Files.lines(file.toPath()).skip(1).map(line -> line.split(","))) {
            stream.forEach(split -> {
                LocalDate date = LocalDate.parse(split[0]);
                LocalTime time = LocalTime.parse(split[2]);
                String text = split[1];
                addEntry(date, new Entry(time, text));
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addEntry(LocalDate date, Entry entry) {
        /*map.computeIfPresent(date, (key, value) -> {
            value.add(entry);
            return value;
        });

        map.computeIfAbsent(date, key -> {
            ArrayList<Entry> entries = new ArrayList<>();
            entries.add(entry);
            return entries;
        });*/
        map.merge(date, new ArrayList<>(List.of(entry)), (oldList, newList) -> {
            if (oldList
                    .stream()
                    .map(Entry::getTime)
                    .anyMatch(time -> newList
                            .stream()
                            .map(Entry::getTime)
                            .anyMatch(time::equals))) {
                System.out.println("Entry-Time already exists!");
            } else {
                oldList.addAll(newList);
                oldList.sort(Comparator.comparing(Entry::getTime));
            }
            return oldList;
        });
    }

    public void showEntriesOfDay(LocalDate date) {
        if (map.containsKey(date)) {
            System.out.println("<" + date + ">");
            map.get(date).forEach(System.out::println);
        } else {
            System.out.println("No entries for this day!");
        }
    }

    public void showAllDays() {
        System.out.println("Datum\t\tEinträge");
        System.out.println("------------------------------");
        map.forEach((date, entries) -> {
            System.out.println("<" + date + ">");
            entries.forEach(System.out::println);
        });
    }
}

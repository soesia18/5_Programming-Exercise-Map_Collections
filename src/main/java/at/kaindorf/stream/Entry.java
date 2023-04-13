package at.kaindorf.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * <h3>Created by IntelliJ IDEA.</h3><br>
 * <b>Project:</b> Programming-Exercise - Map Collections<br>
 * <b>User:</b> Simon Schoeggler<br>
 * <b>Date:</b> 12. April 2023<br>
 * <b>Time:</b> 08:53<br>
 */

@Data
@AllArgsConstructor
public class Entry {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

    private LocalTime time;
    private String text;

    @Override
    public String toString() {
        return "\t\t" + time.format(dtf) + " " + text;
    }
}

package Entities;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventEntry {
    private Integer id;
    private String participant;
    private House house;
    private String event;
    private LocalDate date;

    public EventEntry(Integer id, String participant, House house, String event, LocalDate date) {
        this.id = id;
        this.participant = participant;
        this.house = house;
        this.event = event;
        this.date = date;

    }

    public EventEntry() {

    }

    public Integer getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getParticipant() {
        return participant;
    }

    public House getHouse() {
        return house;
    }

    public String getEvent() {
        return event;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public  static List<EventEntry> parseEntries(String path) throws IOException {
        Path filePath = Path.of(path);

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            if (!reader.readLine().contains("[")) {
                throw new IOException("Invalid file");
            }

            ArrayList<EventEntry> entries = new ArrayList<>();
            String nextLine = reader.readLine();

            while (!nextLine.contains("]")) {
                entries.add(readEntry(reader, nextLine));

                nextLine = reader.readLine();
            }

            return entries;
        }
    }

    private static EventEntry readEntry(BufferedReader reader, String firstLine) throws IOException {
        if (!firstLine.contains("{")) {
            throw new IOException("Invalid file");
        }

        EventEntry entry = new EventEntry();

        for (int i = 0; i < 5; i++) {
            readField(reader, entry);
        }

        if (!reader.readLine().contains("}")) {
            throw new IOException("Invalid entry file");
        }

        return entry;
    }

    private static void readField(BufferedReader reader, EventEntry entry) throws IOException {
        String line2 = reader.readLine();
        String fieldName = line2.split(":")[0].replace("\"", "").trim();

        switch (fieldName) {
            case "Datum": {
                String[] split = line2.split(":");
                String s = (split[1] + ":" + split[2] + ":" + split[3]).replace("\"", "");
                entry.setDate(LocalDate.from(LocalDateTime.parse(s.substring(1, s.length() - 1))));
                break;
            }
            case "Mitgliedsname": {
                String s = line2.split(":")[1].replace("\"", "");
                entry.setParticipant(s.substring(1, s.length() - 1));
                break;
            }
            case "Haus": {
                if (!line2.split(":")[1].contains("{")) {
                    throw new IOException("Invalid file");
                }
                entry.setHouse(House.valueOf(line2.split(":")[1].replace("{", "")));
                break;
            }
            case "Id": {
                String s = line2.split(":")[1].replace("\"", "");
                s = s.substring(2, s.length() - 1);
                entry.setId(Integer.parseInt(s));
                break;
            }
        }
    }

}

import Entities.EventEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<EventEntry> events = EventEntry.parseEntries("src/Files/evenimente.json");
        for(EventEntry event : events){
            System.out.println(event);
        }
    }
}
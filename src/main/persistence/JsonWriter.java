package persistence;


import model.Kitchen;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

//JsonWriter represents a writer to write Json representation of Kitchen to file.
//JsonWriter is based on JsonWriter from  provided WorkroomApp example on edX.
public class JsonWriter {
    public static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    //EFFECTS: constructs writer for writing to destination
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    //MODIFIES: this
    //EFFECTS: opens writer for writing, throws FileNotFoundException if applicable.
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    //MODIFIES: this
    //EFFECTS: writes JSON representation of Kitchen to file
    public void write(Kitchen kitchen) {
        JSONObject json = kitchen.toJson();
        saveToFile(json.toString(TAB));
    }

    //MODIFIES: this
    //EFFECTS closes writer
    public void close() {
        writer.close();
    }

    //MODIFIES: this
    //EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}

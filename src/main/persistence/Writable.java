package persistence;

import org.json.JSONObject;

//Interface for toJson() methods for Kitchen and Recipe.
public interface Writable {
    JSONObject toJson();
}

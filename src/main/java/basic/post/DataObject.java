package basic.post;

public class DataObject {

    private final String data;

    private static int objectCounter = 0;

    public DataObject(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public static int getObjectCounter() {
        return objectCounter;
    }

    public static DataObject get(String data) {
        objectCounter++;
        return new DataObject(data);
    }
}
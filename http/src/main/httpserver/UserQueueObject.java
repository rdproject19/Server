package httpserver;

public class UserQueueObject<DataType> {

    private String type;
    private DataType data;

    public UserQueueObject(String type, DataType data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public DataType getData() {
        return data;
    }
}

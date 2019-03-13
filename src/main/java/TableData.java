import java.util.ArrayList;

public class TableData {
    String id;
    int type;
    boolean isInit;
    ArrayList<Integer> sizes;
    int countOfMeasur;
    int retType;
    int countOfParametrsFun;
    SemType typeData;

    public void changeType(int type) {
        typeData.type = type;
    }

    public TableData(String id, int type, SemType typeData) {
        this.id = id;
        this.type = type;
        isInit = false;
        sizes = new ArrayList<>();
        countOfMeasur = 1;
        retType = 0;
        countOfParametrsFun = 0;
        this.typeData = typeData;
        this.typeData.type = type;
    }
}

import java.util.Vector;

public class SemType {
    int type = 0;
    int typeData = 0;
    String val;
    Vector<Integer> positions = new Vector<>();

    public SemType(int type) {
        this.typeData = type;
    }

    public SemType(int type, int typeData) {
        this.type = type;
        this.typeData = typeData;
    }

    public SemType(SemType target) {
        if (target != null) {
            this.typeData = target.typeData;
            this.type = target.type;
        }
    }
}

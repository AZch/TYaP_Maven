import java.util.Vector;

public class VertTree {
    String id;
    Vector<String> val = new Vector<>();
    Vector<Integer> measure = new Vector<>();
    int type;
    VertTree left;
    VertTree right;
    VertTree parent;
    TableData tableData;
    boolean isSee = false;

    public VertTree(int type, String id, VertTree parent, TableData tableData) {
        this.parent = parent;
        this.id = id;
        this.type = type;
        this.tableData = tableData;
    }

    public VertTree(int type, String id) {
        this.parent = null;
        this.id = id;
        this.type = type;
        tableData = new TableData(id, type, new SemType(type));
    }

    public void fillVal() {
        int size = 1;
        for (int i = 0; i < measure.size(); i++) {
            size *= measure.get(i);
        }
        val = new Vector<>();
        for (int i = 0; i < size; i++)
            val.add("");
        int a;
        a = 6;
    }

    public String getVal() {
        String res = "";
        if (measure.size() > 0)
            res += "measure: " + String.valueOf(measure.size()) + "(";
        for (int i = 0; i < measure.size(); i++)
            res += String.valueOf(measure.get(i)) + " ";
        if (measure.size() > 0)
            res += ")\n";
        res += "value " + id + ":\n";
        for (int i = 0; i < val.size(); i++)
            if (val.get(i) == "")
                res += ". ";
            else
                res += val.get(i) + " ";
        return res + "\n";
    }

    public void addVal(String val, Vector<Integer> measureIndex, Scaner scaner) {
        int bias = 0;
        for (int k = 0; k < measureIndex.size(); k++) {
            int mult = 1;
            for (int l = k + 1; l < measureIndex.size(); l++)
                mult *= this.measure.get(l);
            bias += mult * measureIndex.get(k);
        }
        this.val.set(bias, val);
        int a;
        a = 6;
    }

}

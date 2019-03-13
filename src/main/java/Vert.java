import java.util.ArrayList;

public class Vert {
    Triad triads;
    String desc = "";
    Vert next;
    Vert trueBrace;
    Vert falseBrace;
    ArrayList<Vert> versionVert = new ArrayList<>();
    ArrayList<Vert> useVert = new ArrayList<>();
    Vert parent;
    int index = -1;

    public Vert(Triad triads, String desc, Vert parent, int index) {
        this.triads = triads;
        this.desc = desc;
        this.parent = parent;
        this.index = index;
    }

    public Vert() {}
}

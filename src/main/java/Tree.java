public class Tree {
    private VertTree root;
    private VertTree currVert;
    private VertTree linkVert;

    public Tree() {
        this.root = new VertTree(0, "root", null, null);
        this.currVert = root;
    }

    public void setLinkVert(VertTree linkVert) {
        this.linkVert = linkVert;
    }

    //    public VertTree goParent() {
//        if (currVert.parent != null) {
//            currVert = currVert.parent;
//            currId = currVert.id;
//        }
//        return currVert;
//    }
//
//    public VertTree goLeft() {
//        if (currVert.left != null) {
//            currVert = currVert.left;
//            currId = currVert.id;
//        }
//        return currVert;
//    }
//
//    public VertTree goRight() {
//        if (currVert.right != null) {
//            currVert = currVert.right;
//            currId = currVert.id;
//        }
//        return currVert;
//    }
    public VertTree getRoot() {
        return root;
    }

    public VertTree add(String id, int type, TableData tableData) {
        VertTree saveVert = null;
         if (type == Constants.CURLY_BRACE_OPEN) {
            currVert.left = new VertTree(Constants.CURLY_BRACE_OPEN, "", currVert, null);
            currVert = currVert.left;
            currVert.right = new VertTree(Constants.CURLY_BRACE_OPEN, "", currVert, null);
            currVert = currVert.right;
        } else {
            currVert.left = new VertTree(type, id, currVert, tableData);
            saveVert = currVert.left;
            currVert = currVert.left;
            if (linkVert != null) {
                currVert.right = linkVert.right;
            }
        }
        return saveVert;
    }

    private void upCurrToEmpty() {
        if (currVert.parent != null) {
            currVert = currVert.parent;
        }
        while (currVert.left != null) {
            if (currVert.parent != null) {
                currVert = currVert.parent;
            } else {
                return;
            }
        }
        //currVert = currVert.parent;
    }

    public void setCurrVert(VertTree vert) {
        currVert = vert;
    }

    public VertTree getCurrVert() {
        return currVert;
    }

    public VertTree addRight(VertTree vertTree) {
        currVert.right = vertTree;
        vertTree.parent = currVert;
        currVert = vertTree;
        return vertTree;
    }

    public VertTree findId(String id, Scaner scaner) {
        if (!findAllUp(id))
            scaner.PrintError("Идентификатор не обнаружен".toCharArray(), id.toCharArray());
        return findVertUp(id);
    }

    public void checkIndividualId(String id, Scaner scaner) {
        if (findThisBrace(id))
            scaner.PrintError("Идентификатор переопределён".toCharArray(), id.toCharArray());
    }

    public void checkWriteUp(String id, Scaner scaner) {
        if (!findThisBrace(id))
            scaner.PrintError("Идентификатор не определен".toCharArray(), id.toCharArray());
    }

    public void checkIndividualAllId(String id, Scaner scaner) {
        if (findAllUp(id))
            scaner.PrintError("Идентификатор переопределён".toCharArray(), id.toCharArray());
    }

    private boolean findAllUp(String id) {
        VertTree bufCurrVert = currVert;
        while (!bufCurrVert.id.equals(id)) {
            if (bufCurrVert.parent != null) {
                bufCurrVert = bufCurrVert.parent;
            } else
                return false;
        }
        return true;
    }

    public VertTree findVertUp(String id) {
        VertTree bufCurrVert = currVert;
        while (!bufCurrVert.id.equals(id)) {
            if (bufCurrVert.parent != null) {
                bufCurrVert = bufCurrVert.parent;
            } else
                return null;
        }
        return bufCurrVert;
    }

    public VertTree findThisBranch(String id, VertTree currVert) {
        VertTree bufCurrVert = currVert;
        while (!bufCurrVert.id.equals(id)) {
            if (bufCurrVert.left != null) {
                bufCurrVert = bufCurrVert.left;
            } else
                return null;
        }
        if (bufCurrVert.id.equals(id))
            return bufCurrVert;
        else
            return null;
    }

    private boolean findThisBrace(String id) {
        VertTree bufCurrVert = currVert;
        while (!bufCurrVert.id.equals(id)) {
            if (bufCurrVert.parent != null && (!bufCurrVert.id.equals(""))) {
                bufCurrVert = bufCurrVert.parent;
            } else
                return false;
        }
        return true;
    }
}
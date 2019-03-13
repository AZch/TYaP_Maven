import java.util.ArrayList;

public class LL1 {
    private int[] magazine = new int[5000];
    private int pointer = 0;

    public ArrayList<Triad> triads = new ArrayList<>();
    private int pointerTriad = 0;
    private boolean initPerem = false;
    private String countByte = "0";
    private String prefexPerem = "";
    private String prefixIf = "";
    private String lastId = "";
    private Triad assignTriad = null;
    private int lastCalcVarTriad1 = -1;
    private int lastCalcVarTriad2 = -1;
    private String lastCalcVarTriadOper = "";
    private boolean isMakeId = false;

    private int indexLastDoneVar = 0;

    private int getIdLastPerem() {
        for (Triad triad : triads) {
            if (triad.operand1.equals(lastId))
                return triads.indexOf(triad);
        }
        return -1;
    }

    private int getIdLastPeremWithPrefex(String prefex) {
        for (Triad triad : triads) {
            String check = prefex + "." + lastId;
            check = check.replaceAll("\0", "");
            if (triad.operand1.replaceAll("\0", "").equals(check))
                return triads.indexOf(triad);
        }
        return -1;
    }

    /*
    3 * ((4 + 5) - (7 / 9)) - (16 * 10) + 99
        0)                          = 0) 7)      = 0) 7)      = 0) 7)
        1) * 3 2)       * 3 2)      * 3 2)       * 3 2)       * 3 2)
        2) - 3) 4)      - 3) 4)     - 3) 4)      - 3) 4)      - 3) 4)
        3) + 4 5        + 4 5       + 4 5        + 4 5        + 4 5
        4) / 7 9        / 7 9       / 7 9        / 7 9        / 7 9
        5) - 1) 6)      - 1) 6)     - 1) 6)      - 1) 6)      - 1) 6)
        6) * 16 10      * 16 10     * 16 10      * 16 10      * 16 10
        7) = 23) 5)                 + 5) 99      + 5) 99      + 5) 99

     */

    private Triad getTriadByProcGoIf(String proc) {
        for (int i = pointerTriad; i >= 0; i--)
            if (triads.get(i).proc.equals(proc) && (
                    triads.get(i).operand1.equals("") ||
                            triads.get(i).operand2.equals("")
                    ))
                return triads.get(i);
        return null;
    }

    private Triad getTriad(String proc, String op1, String op2) {
        for (int i = pointerTriad; i >= 0; i--)
            if (triads.get(i).proc.equals(proc) &&
                    triads.get(i).operand1.equals(op1) &&
                    triads.get(i).operand2.equals(op2))
                return triads.get(i);
        return null;
    }

    private int getCountMemeryToTriad(Triad from, Triad to) {
        int indexStart = triads.indexOf(from),
            indexEnd = triads.indexOf(to),
            memory = 0;
        for (int i = indexStart; i < indexEnd; i++) {
            if (triads.get(i).proc.equals("int")) {
                memory += Constants.sizeInt;
            } else if (triads.get(i).proc.equals("char")) {
                memory += Constants.sizeChar;
            }
        }
        return memory;
    }

    private Triad getTriadWithoutOper2() {
        for (int i = pointerTriad; i >= 0; i--) {
            if (i < triads.size()) {
                if (triads.get(i).proc.equals("=")) {
                    return null;
                }
                if (triads.get(i).operand2.equals("") && !triads.get(i).proc.equals(""))
                    return triads.get(i);
            }
        }
        return null;
    }

    private Triad getTriadLastAssign() {
        for (int i = pointerTriad; i >= 0; i--) {
            if (i < triads.size() && triads.get(i).proc.equals("=") && triads.get(i).operand2.equals("")) {
                return triads.get(i);
            }
        }
        return null;
    }

    private Triad getEmptyTriad() {
        for (int i = pointerTriad; i >= 0; i--) {
            if (i < triads.size()) {
                if (triads.get(i).proc.equals("=")) {
                    return null;
                }
                if (triads.get(i).operand2.equals("") && triads.get(i).proc.equals("") && triads.get(i).operand1.equals(""))
                    return triads.get(i);
            }
        }
        return null;
    }

    private Triad getOnleOper1Triad() {
        for (int i = pointerTriad; i >= 0; i--) {
            if (i < triads.size()) {
                if (triads.get(i).proc.equals("=")) {
                    return null;
                }
                if (triads.get(i).operand2.equals("") && triads.get(i).proc.equals("") && !triads.get(i).operand1.equals(""))
                    return triads.get(i);
            }
        }
        return null;
    }

    private int getIndexTriadName(String prefix, String name) {
        for (Triad triad : triads)
            if (triad.operand1.equals(prefix + "." + name))
                return triads.indexOf(triad);
        return -1;
    }

    private ArrayList<Triad> getTriadForClass(String className) {
        ArrayList<Triad> resTriad = new ArrayList<>();
        for (Triad triad : triads) {
            if (triad.operand1.split("\\.")[0].equals(className)) {
                resTriad.add(triad);
            }
        }
        return resTriad;
    }

    private void epsilon() {
        pointer--;
    }

    private int getIndexTriadSplit() {
        try {
            int id = getIdLastPeremWithPrefex(prefexPerem);
            String pref = prefexPerem;
            while (id == -1) {
                String[] checkPref = pref.split("\\.");
                pref = checkPref[0];
                for (int i = 1; i < checkPref.length - 1; i++)
                    pref += "." + checkPref[i];
                id = getIdLastPeremWithPrefex(pref);
            }
            return id;
        } catch (Exception e) {
            return -1;
        }
    }

    public int LL1Analise(Scaner scaner) {
        int type, fl = 1, resCode = 1;

        char[] lex = new char[Constants.MAX_LEX], lll = new char[Constants.MAX_LEX];
        char[] sss = new char[Constants.MAX_LEX];
        char[] prevLex = new char[Constants.MAX_LEX];
        magazine[pointer] = Constants.netermPROG;

        //int pointerSave = scaner.getPointer();
        type = scaner.processScanner(lex);
        //scaner.setPointer(pointerSave);
        while (fl != 0) {
            if (magazine[pointer] <= Constants.MAX_TYPE_TERMINAL) {
                if (magazine[pointer] == type) {
                    // верхушка совпадает с отсканированным терминалом
                    if (type == Constants.END)
                        fl = 0;
                    else {
                        if (isMakeId)
                            lastId += new String(lex).replaceAll("\0", "");
                        type = scaner.processScanner(lex);




                        if (type == Constants.MAIN) {
                            prefexPerem = "main";
                            Triad triad = new Triad("fun", "main", "0");
                            triads.add(triad);
                            pointerTriad = triads.indexOf(triad) + 1;
                            triads.add(new Triad("fun", "main", "end"));
                        }
//                        else if (type == Constants.INT) {
//                            initPerem = true;
//                            countByte = "int";
//                        } else if (type == Constants.CHAR) {
//                            initPerem = true;
//                            countByte = "char";
//                        }
                        if (type == Constants.ASSIGN) {
                            if (!isMakeId)
                                triads.add(pointerTriad++, new Triad("=", String.valueOf(getIdLastPerem()) + ")", ""));
                            else {
                                int id = getIndexTriadSplit();

                                triads.add(pointerTriad++, new Triad("=", String.valueOf(id) + ")", ""));
                            }
                            triads.add(pointerTriad++, new Triad("", "", ""));
                            //assignTriad = new Triad("=", String.valueOf(getIdLastPerem()), "");
                            indexLastDoneVar = -1;
                        }
                        if (type == Constants.ID) {
                            if (!isMakeId)
                                lastId = prefexPerem + "." + new String(lex).replaceAll("\0", "");

                            if (initPerem) {
                                if (!countByte.equals("int") && !countByte.equals("char")) {
                                    String className = countByte;
                                    ArrayList<Triad> moreTriads = getTriadForClass(className);
                                    for (Triad triad : moreTriads)
                                        triads.add(pointerTriad++, new Triad(triad.proc, prefexPerem + "." + new String(lex).replaceAll("\0", "") + triad.operand1.substring(className.length()), ""));
                                }else {
                                    triads.add(pointerTriad, new Triad(countByte, prefexPerem + "." + new String(lex).replaceAll("\0", ""), ""));
                                    pointerTriad++;
                                }
                            }
                        }
                        if (type == Constants.COMMA && initPerem) {
                            initPerem = false;
                        }
                        if (type == Constants.VIRGULE)
                            initPerem = true;





                        pointer--;
                    }
                }
                else {
                    resCode = -1;
                    if (magazine[pointer] == Constants.CURLY_BRACE_OPEN) {
                        scaner.PrintError("Ожидалась открывающаяся фигурная скобка".toCharArray(), lex);
                        pointer--;
                        //while (type != Constants.CURLY_BRACE_OPEN) {
                        //    ttt = pointer;
                        //    type = scaner.processScanner(lex);
                        //}
                        //scaner.setPointer(pointer);
                    } else if (magazine[pointer] == Constants.ROUND_BRACE_OPEN) {
                        scaner.PrintError("Ожидалась открывающаяся фигурная скобка".toCharArray(), lex);
                        pointer--;
                        //while (type != Constants.ROUND_BRACE_OPEN) {
                        //    ttt = pointer;
                        //    type = scaner.processScanner(lex);
                        //}
                        //scaner.setPointer(pointer);
                    } else {
                        scaner.PrintError("Неизвестный символ".toCharArray(), lex);
                        while (type != Constants.COMMA && type != Constants.CURLY_BRACE_CLOSE && type != Constants.ROUND_BRACE_CLOSE)
                            type = scaner.processScanner(lex);



                        while (pointer > 0 && magazine[pointer] != type) {
                            pointer--;
                        }
                    }
                    //return -1; // ожидался символ типа magazine[pointer], а не lex
                }
                continue;
            } else {
                switch (magazine[pointer]) {
                    case Constants.netermPROG :
                        if (type == Constants.INT || type == Constants.CHAR ||
                            type == Constants.ID) {
                            magazine[pointer++] = Constants.netermPROG;
                            magazine[pointer++] = Constants.netermDATA;
                            prefexPerem = "global";
                        } else if (type == Constants.CLASS) {
                            magazine[pointer++] = Constants.netermPROG;

                            magazine[pointer++] = Constants.deltaClearPrefix;

                            magazine[pointer++] = Constants.COMMA;
                            magazine[pointer++] = Constants.CURLY_BRACE_CLOSE;
                            magazine[pointer++] = Constants.netermCLASS_DESC;
                            magazine[pointer++] = Constants.CURLY_BRACE_OPEN;
                            magazine[pointer++] = Constants.ID;
                            magazine[pointer++] = Constants.deltaInitClass;
                            magazine[pointer++] = Constants.CLASS;
                        } else if (type == Constants.CONST) {
                            magazine[pointer++] = Constants.netermPROG;
                            magazine[pointer++] = Constants.netermCONST;
                        } else if (type == Constants.VOID) {
                            magazine[pointer++] = Constants.netermPROG;
                            magazine[pointer++] = Constants.deltaEndMain;
                            magazine[pointer++] = Constants.netermDESC_FUN;
                            magazine[pointer++] = Constants.ROUND_BRACE_CLOSE;
                            magazine[pointer++] = Constants.ROUND_BRACE_OPEN;
                            magazine[pointer++] = Constants.MAIN;
                            magazine[pointer++] = Constants.VOID;
                        } else {
                            magazine[pointer++] = type;
                        }
                          //  epsilon();
                        break;
                    case Constants.netermCLASS_DESC:
                        if (type == Constants.INT || type == Constants.CHAR ||
                                type == Constants.ID) { // Ч П С Ч Я Д В Ч М
                            magazine[pointer++] = Constants.netermCLASS_DESC;
                            magazine[pointer++] = Constants.netermDATA;
                        } //else
                           // epsilon();
                        break;
                    case Constants.netermDATA:
                        if (type == Constants.INT || type == Constants.CHAR ||
                                type == Constants.ID) {
                            magazine[pointer++] = Constants.netermALL_DATA;
                            magazine[pointer++] = Constants.netermTYPE;
                        }
                        break;
                    case Constants.netermTYPE:
                        if (type == Constants.INT) {
                            initPerem = true;
                            countByte = "int";
                            magazine[pointer++] = Constants.INT;
                        } else if (type == Constants.CHAR) {
                            initPerem = true;
                            countByte = "char";
                            magazine[pointer++] = Constants.CHAR;
                        } else if (type == Constants.ID) {
                            initPerem = true;
                            countByte = new String(lex).replaceAll("\0", "");
                            magazine[pointer++] = Constants.ID;
                        }
                        break;
                    case Constants.netermALL_DATA:
                        if (type == Constants.ID) {
                            magazine[pointer++] = Constants.COMMA;
                            magazine[pointer++] = Constants.netermNEXT_DATA;
                            magazine[pointer++] = Constants.deltaEndAssign; // чек
                            magazine[pointer++] = Constants.netermVARIABLE;
                            magazine[pointer++] = Constants.ID;
                        }
                        break;
                    case Constants.netermNEXT_DATA:
                        if (type == Constants.VIRGULE) {
                            magazine[pointer++] = Constants.netermNEXT_DATA;
                            magazine[pointer++] = Constants.deltaEndAssign; // чек
                            magazine[pointer++] = Constants.netermVARIABLE;
                            magazine[pointer++] = Constants.ID;
                            magazine[pointer++] = Constants.VIRGULE;
                        } //else
                          //  epsilon();
                        break;
                    case Constants.netermCONST:
                        if (type == Constants.CONST) {
                            magazine[pointer++] = Constants.deltaEndAssign;
                            magazine[pointer++] = Constants.COMMA;
                            magazine[pointer++] = Constants.netermVARIABLE;
                            //magazine[pointer++] = Constants.ASSIGN;
                            magazine[pointer++] = Constants.ID;
                            magazine[pointer++] = Constants.netermTYPE;
                            magazine[pointer++] = Constants.CONST;
                        }
                        break;
                    case Constants.netermVARIABLE:
                        if (type == Constants.SQUARE_BRACE_OPEN) {
                            magazine[pointer++] = Constants.netermVARIABLE;
                            magazine[pointer++] = Constants.netermNEXT_SQUARE_DATA;
                            magazine[pointer++] = Constants.SQUARE_BRACE_CLOSE;
                            magazine[pointer++] = Constants.netermA1;
                            magazine[pointer++] = Constants.SQUARE_BRACE_OPEN;
                        } else if (type == Constants.ASSIGN) {
                            magazine[pointer++] = Constants.netermEXPRESSION;
                            magazine[pointer++] = Constants.ASSIGN;
                        }// else
                          //  epsilon();
                        break;
                    case Constants.netermNEXT_SQUARE_DATA:
                        if (type == Constants.SQUARE_BRACE_OPEN) {
                            magazine[pointer++] = Constants.netermNEXT_SQUARE_DATA;
                            magazine[pointer++] = Constants.SQUARE_BRACE_CLOSE;
                            magazine[pointer++] = Constants.netermA1;
                            magazine[pointer++] = Constants.SQUARE_BRACE_OPEN;
                        }// else
                          //  epsilon();
                        break;
                    case Constants.netermEXPRESSION:
                        if (type == Constants.ROUND_BRACE_OPEN ||
                            type == Constants.ID || type == Constants.TYPE_INT ||
                            type == Constants.TYPE_SINT || type == Constants.TYPE_CHAR) {
                                magazine[pointer++] = Constants.netermA1;
                        } else if (type == Constants.CURLY_BRACE_OPEN) {
                            magazine[pointer++] = Constants.CURLY_BRACE_CLOSE;
                            magazine[pointer++] = Constants.netermMANY_EXPRESSION;
                            magazine[pointer++] = Constants.CURLY_BRACE_OPEN;
                        }
                        break;
                    case Constants.netermMANY_EXPRESSION:
                        if (type == Constants.ROUND_BRACE_OPEN ||
                            type == Constants.CURLY_BRACE_OPEN ||
                            type == Constants.ID || type == Constants.TYPE_CHAR ||
                            type == Constants.TYPE_SINT || type == Constants.TYPE_INT) {
                                magazine[pointer++] = Constants.netermNEXT_MANY_EXPRESSION;
                                magazine[pointer++] = Constants.netermEXPRESSION;
                        }
                        break;
                    case Constants.netermNEXT_MANY_EXPRESSION:
                        if (type == Constants.VIRGULE) {
                            magazine[pointer++] = Constants.netermNEXT_MANY_EXPRESSION;
                            magazine[pointer++] = Constants.netermEXPRESSION;
                            magazine[pointer++] = Constants.VIRGULE;
                        } //else
                          //  epsilon();
                        break;
                    case Constants.netermOPERATOR:
                        if (type == Constants.CURLY_BRACE_OPEN) {
                            magazine[pointer++] = Constants.netermDESC_FUN;
                        } else if (type == Constants.IF) {
                            magazine[pointer++] = Constants.netermIF;
                        } else if (type == Constants.ID) {
                            magazine[pointer++] = Constants.deltaEndAssign;
                            magazine[pointer++] = Constants.COMMA;
                            magazine[pointer++] = Constants.netermEXPRESSION;
                            magazine[pointer++] = Constants.deltaUnMakeId;
                            magazine[pointer++] = Constants.ASSIGN;
                            magazine[pointer++] = Constants.netermELEM_ARRAY;
                            magazine[pointer++] = Constants.ID;
                            magazine[pointer++] = Constants.deltaMakeId;
                        }
                        break;
                    case Constants.netermIF:
                        if (type == Constants.IF) {
                            magazine[pointer++] = Constants.netermMAY_ELSE;

                            magazine[pointer++] = Constants.deltaRemovePrefixIf;

                            magazine[pointer++] = Constants.netermOPERATOR;

                            magazine[pointer++] = Constants.deltaAddPrefixIf;

                            magazine[pointer++] = Constants.deltaPlaceJmp;

                            magazine[pointer++] = Constants.deltaEndAssign;

                            magazine[pointer++] = Constants.ROUND_BRACE_CLOSE;
                            magazine[pointer++] = Constants.netermEXPRESSION;
                            magazine[pointer++] = Constants.ROUND_BRACE_OPEN;

                            triads.add(pointerTriad++, new Triad("=", "if", ""));
                            triads.add(pointerTriad++, new Triad("", "", ""));

                            magazine[pointer++] = Constants.IF;
                        }
                        break;
                    case Constants.netermMAY_ELSE:
                        if (type == Constants.ELSE) {
                            magazine[pointer++] = Constants.deltaRemoveElse;
                            magazine[pointer++] = Constants.deltaJumpAfterIfWithAddGo;
                            magazine[pointer++] = Constants.netermOPERATOR;
                            magazine[pointer++] = Constants.ELSE;
                            magazine[pointer++] = Constants.deltaJumpAfterIfWithElse;
                            magazine[pointer++] = Constants.deltaAddPrefixElse;
                        } //else
                        else
                            magazine[pointer++] = Constants.deltaJumpAfterIf;
                          //  epsilon();
                        break;
                    case Constants.netermDESC_FUN:
                        //if (type == Constants.CURLY_BRACE_OPEN) {
                            magazine[pointer++] = Constants.deltaRemoveDot;


                            magazine[pointer++] = Constants.CURLY_BRACE_CLOSE;
                            magazine[pointer++] = Constants.netermIN_DESC_FUN;

                            magazine[pointer++] = Constants.deltaAddPrefixDot;

                            magazine[pointer++] = Constants.CURLY_BRACE_OPEN;
                        //}
                        break;
                    case Constants.netermIN_DESC_FUN:
                        int pointerScanner = scaner.getPointer();
                        int nextType = scaner.processScanner(sss);
                        if (type == Constants.INT || type == Constants.CHAR ||
                                (type == Constants.ID && nextType == Constants.ID)) {
                                magazine[pointer++] = Constants.netermNEXT_DESC_FUN;
                                magazine[pointer++] = Constants.netermDATA;
                        } else if (type == Constants.CONST) {
                            magazine[pointer++] = Constants.netermNEXT_DESC_FUN;
                            magazine[pointer++] = Constants.netermCONST;
                        } else if (type == Constants.CURLY_BRACE_OPEN ||
                                type == Constants.IF || type == Constants.ID) {
                            magazine[pointer++] = Constants.netermNEXT_DESC_FUN;
                            magazine[pointer++] = Constants.netermOPERATOR;
                        }
                        scaner.setPointer(pointerScanner);
                        break;
                    case Constants.netermNEXT_DESC_FUN:
                        int pointerScannerNext = scaner.getPointer();
                        int nextTypeNext = scaner.processScanner(sss);
                        if (type == Constants.INT || type == Constants.CHAR ||
                                (type == Constants.ID && nextTypeNext == Constants.ID)) {
                            magazine[pointer++] = Constants.netermNEXT_DESC_FUN;
                            magazine[pointer++] = Constants.netermDATA;
                        } else if (type == Constants.CONST) {
                            magazine[pointer++] = Constants.netermNEXT_DESC_FUN;
                            magazine[pointer++] = Constants.netermCONST;
                        } else if (type == Constants.CURLY_BRACE_OPEN ||
                                type == Constants.IF || type == Constants.ID) {
                            magazine[pointer++] = Constants.netermNEXT_DESC_FUN;
                            magazine[pointer++] = Constants.netermOPERATOR;
                        } //else
                          //  epsilon();
                        scaner.setPointer(pointerScannerNext);
                        break;
                    case Constants.netermELEM_ARRAY:
                        if (type == Constants.SQUARE_BRACE_OPEN) {
                            magazine[pointer++] = Constants.netermELEM_ARRAY;
                            magazine[pointer++] = Constants.netermINSIDE_ELEM;
                        } else if (type == Constants.DOT) {
                            magazine[pointer++] = Constants.netermELEM_ARRAY;
                            magazine[pointer++] = Constants.netermINSIDE_ELEM;
                        } //else
                          //  epsilon();
                        break;
                    case Constants.netermINSIDE_ELEM:
                        if (type == Constants.SQUARE_BRACE_OPEN) {
                            magazine[pointer++] = Constants.netermMAY_SQUARE;
                        } else if (type == Constants.DOT) {
                            magazine[pointer++] = Constants.netermMAY_POINT;
                        }
                        break;
                    case Constants.netermMAY_SQUARE:
                        if (type == Constants.SQUARE_BRACE_OPEN) {
                            magazine[pointer++] = Constants.netermNEXT_MAY_SQUARE;
                            magazine[pointer++] = Constants.SQUARE_BRACE_CLOSE;
                            magazine[pointer++] = Constants.netermEXPRESSION;
                            magazine[pointer++] = Constants.SQUARE_BRACE_OPEN;
                        }
                        break;
                    case Constants.netermNEXT_MAY_SQUARE:
                        if (type == Constants.SQUARE_BRACE_OPEN) {
                            magazine[pointer++] = Constants.netermNEXT_MAY_SQUARE;
                            magazine[pointer++] = Constants.SQUARE_BRACE_CLOSE;
                            magazine[pointer++] = Constants.netermEXPRESSION;
                            magazine[pointer++] = Constants.SQUARE_BRACE_OPEN;
                        }// else
                          //  epsilon();
                        break;
                    case Constants.netermMAY_POINT:
                        if (type == Constants.DOT) {
                            magazine[pointer++] = Constants.netermNEXT_MAY_POINT;
                            magazine[pointer++] = Constants.ID;
                            magazine[pointer++] = Constants.DOT;
                        }
                        break;
                    case Constants.netermNEXT_MAY_POINT:
                        if (type == Constants.DOT) {
                            magazine[pointer++] = Constants.netermNEXT_MAY_POINT;
                            magazine[pointer++] = Constants.ID;
                            magazine[pointer++] = Constants.DOT;
                        }// else
                          //  epsilon();
                        break;
                    case Constants.netermA1:
                        if (type == Constants.ROUND_BRACE_OPEN ||
                            type == Constants.ID || type == Constants.TYPE_CHAR ||
                            type == Constants.TYPE_INT || type == Constants.TYPE_SINT) {
                                magazine[pointer++] = Constants.netermA1_;
                                magazine[pointer++] = Constants.netermA2;
                        }
                        break;
                    case Constants.netermA1_:
                        if (type == Constants.OR) {
                            magazine[pointer++] = Constants.netermA1_;
                            magazine[pointer++] = Constants.netermA2;
                            magazine[pointer++] = Constants.OR;
                            magazine[pointer++] = Constants.deltaOperation;
                        }// else
                           // epsilon();
                        break;
                    case Constants.netermA2:
                        if (type == Constants.ROUND_BRACE_OPEN ||
                                type == Constants.ID || type == Constants.TYPE_CHAR ||
                                type == Constants.TYPE_INT || type == Constants.TYPE_SINT) {
                            magazine[pointer++] = Constants.netermA2_;
                            magazine[pointer++] = Constants.netermA3;
                        }
                        break;
                    case Constants.netermA2_:
                        if (type == Constants.AND) {
                            magazine[pointer++] = Constants.netermA2_;
                            magazine[pointer++] = Constants.netermA3;
                            magazine[pointer++] = Constants.AND;
                            magazine[pointer++] = Constants.deltaOperation;
                        }// else
                          //  epsilon();
                        break;
                    case Constants.netermA3:
                        if (type == Constants.ROUND_BRACE_OPEN ||
                                type == Constants.ID || type == Constants.TYPE_CHAR ||
                                type == Constants.TYPE_INT || type == Constants.TYPE_SINT) {
                            magazine[pointer++] = Constants.netermA3_;
                            magazine[pointer++] = Constants.netermA4;
                        }
                        break;
                    case Constants.netermA3_:
                        if (type == Constants.EQUAL) {
                            magazine[pointer++] = Constants.netermA3_;
                            magazine[pointer++] = Constants.netermA4;
                            magazine[pointer++] = Constants.EQUAL;
                            magazine[pointer++] = Constants.deltaOperation;
                        } //else
                          //  epsilon();
                        break;
                    case Constants.netermA4:
                        if (type == Constants.ROUND_BRACE_OPEN ||
                                type == Constants.ID || type == Constants.TYPE_CHAR ||
                                type == Constants.TYPE_INT || type == Constants.TYPE_SINT) {
                            magazine[pointer++] = Constants.netermA4_;
                            magazine[pointer++] = Constants.netermA5;
                        }
                        break;
                    case Constants.netermA4_:
                        if (type >= Constants.MORE && type <= Constants.LESS_EQUAL ) {
                            magazine[pointer++] = Constants.netermA4_;
                            magazine[pointer++] = Constants.netermA5;
                            magazine[pointer++] = type;
                            magazine[pointer++] = Constants.deltaOperation;
                        } //else
                          //  epsilon();
                        break;
                    case Constants.netermA5:
                        if (type == Constants.ROUND_BRACE_OPEN ||
                                type == Constants.ID || type == Constants.TYPE_CHAR ||
                                type == Constants.TYPE_INT || type == Constants.TYPE_SINT) {
                            magazine[pointer++] = Constants.netermA5_;
                            magazine[pointer++] = Constants.netermA6;
                        }
                        break;
                    case Constants.netermA5_:
                        if (type == Constants.PLUS || type == Constants.MINUS) {
                            magazine[pointer++] = Constants.netermA5_;
                            magazine[pointer++] = Constants.netermA6;
                            magazine[pointer++] = type;
                            magazine[pointer++] = Constants.deltaOperation;
                        }// else
                          //  epsilon();
                        break;
                    case Constants.netermA6:
                        if (type == Constants.ROUND_BRACE_OPEN ||
                                type == Constants.ID || type == Constants.TYPE_CHAR ||
                                type == Constants.TYPE_INT || type == Constants.TYPE_SINT) {
                            magazine[pointer++] = Constants.netermA6_;
                            magazine[pointer++] = Constants.netermA7;
                        }
                        break;
                    case Constants.netermA6_:
                        if (type == Constants.STAR || type == Constants.SLASH ||
                                type == Constants.PERCENT) {
                            magazine[pointer++] = Constants.netermA6_;
                            magazine[pointer++] = Constants.netermA7;
                            magazine[pointer++] = type;
                            magazine[pointer++] = Constants.deltaOperation;
                        } //else
                           // epsilon();
                        break;
                    case Constants.netermA7:
                        initPerem = false;
                        if (type == Constants.ROUND_BRACE_OPEN) {
                            magazine[pointer++] = Constants.deltaRoundBraceClose;
                            magazine[pointer++] = Constants.ROUND_BRACE_CLOSE;
                            magazine[pointer++] = Constants.netermA1;
                            magazine[pointer++] = Constants.ROUND_BRACE_OPEN;
                            magazine[pointer++] = Constants.deltaRoundBraceOpen;
                        } else if (type == Constants.ID) {

                            magazine[pointer++] = Constants.deltaAnalysId;
                            magazine[pointer++] = Constants.netermELEM_ARRAY;
                            magazine[pointer++] = Constants.ID;
                            magazine[pointer++] = Constants.deltaMakeId;
                        } else if (type == Constants.TYPE_INT || type == Constants.TYPE_SINT ||
                                    type == Constants.TYPE_CHAR) {
                            magazine[pointer++] = type;
                            magazine[pointer++] = Constants.deltaNum;
                        }
                        break;

                    case Constants.deltaRoundBraceOpen:
                        triads.add(pointerTriad++, new Triad("", "", ""));
                        break;
                    case Constants.deltaNum:
                        if (triads.get(pointerTriad - 1).isOnleOperand2()) {
                            triads.get(pointerTriad - 1).operand2 = new String(lex).replaceAll("\0", "");
                            indexLastDoneVar = pointerTriad - 1;
                        } else {
                            Triad triadNum = getEmptyTriad();
                            if (triadNum == null) {
                                triadNum = getTriadWithoutOper2();
                                triadNum.operand2 = new String(lex).replaceAll("\0", "");
                                indexLastDoneVar = triads.indexOf(triadNum);
                            } else
                                triadNum.operand1 = new String(lex).replaceAll("\0", "");
                        }
                        break;

                    case Constants.deltaMakeId:
                        lastId = "";
                        isMakeId = true;
                        break;

                    case Constants.deltaUnMakeId:
                        lastId = "";
                        isMakeId = false;
                        break;

                    case Constants.deltaAnalysId:
                        isMakeId = false;
                        int indexTriad = getIndexTriadName(prefexPerem, lastId);
                        if (indexTriad == -1) {
                            indexTriad = getIndexTriadName("global", new String(lex).replaceAll("\0", ""));
                            if (indexTriad == -1)
                                indexTriad = getIndexTriadName("global", lastId);
                        }
                        if (indexTriad == -1) {
                            indexTriad = getIndexTriadSplit();
                        }

                        if (triads.get(pointerTriad - 1).isOnleOperand2()) {
                            triads.get(pointerTriad - 1).operand2 = String.valueOf(indexTriad) + ")";
                            indexLastDoneVar = pointerTriad - 1;
                        } else {
                            Triad triadNum = getEmptyTriad();
                            if (triadNum == null) {
                                triadNum = getTriadWithoutOper2();
                                triadNum.operand2 = String.valueOf(indexTriad) + ")";
                                indexLastDoneVar = triads.indexOf(triadNum);
                            } else
                                triadNum.operand1 = String.valueOf(indexTriad) + ")";
                        }
                        break;
                    case Constants.deltaOperation:
                        Triad triadOper1 = getOnleOper1Triad();
                        if (triadOper1 != null)
                            triadOper1.proc = new String(lex).replaceAll("\0", "");
                        else {
                            triads.add(pointerTriad++, new Triad(new String(lex).replaceAll("\0", ""), String.valueOf(indexLastDoneVar) + ")", ""));
                        }
                        break;
                    case Constants.deltaRoundBraceClose:
                        Triad triadOper2 = getTriadWithoutOper2();
                        Triad triadEmpty = getEmptyTriad();
                        if (triads.indexOf(triadEmpty) < triads.indexOf(triadOper2)) {
                            triadOper2.operand2 = String.valueOf(indexLastDoneVar) + ")";
                            indexLastDoneVar = triads.indexOf(triadOper2);
                        } else {
                            triadEmpty.operand1 = String.valueOf(indexLastDoneVar) + ")";
                        }
                        break;
                    case Constants.deltaEndAssign:
                        Triad triadLastAssign = getTriadLastAssign();
                        if (triadLastAssign != null) {
                            Triad triadOneOper1 = getOnleOper1Triad();
                            if (triadOneOper1 == null)
                                triadLastAssign.operand2 = String.valueOf(indexLastDoneVar) + ")";
                            else {
                                triadLastAssign.operand2 = triadOneOper1.operand1;
                                triads.remove(triadOneOper1);
                                pointerTriad--;
                            }
                        }
                        indexLastDoneVar = -1;
                        break;

                    case Constants.deltaEndMain:
                        Triad mainTriad = getTriad("fun", "main", "0");
                        int memory = getCountMemeryToTriad(mainTriad, getTriad("fun", "main", "end"));
                        mainTriad.operand2 = String.valueOf(memory);
                        prefexPerem = "global";
                        break;

                    case Constants.deltaInitClass:
                        prefexPerem = new String(lex).replaceAll("\0", "");
                        break;

                    case Constants.deltaClearPrefix:
                        prefexPerem = "global";
                        break;

                    case Constants.deltaResumInit:
                        initPerem = true;
                        break;

                    case Constants.deltaAddPrefixIf:
                        prefexPerem += ".if";
                        break;

                    case Constants.deltaRemovePrefixIf:
                        prefexPerem = prefexPerem.substring(0, prefexPerem.length() - ("if".length() + 1));
                        break;

                    case Constants.deltaAddPrefixDot:
                        prefexPerem += ".qb";
                        break;

                    case Constants.deltaRemoveDot:
                        prefexPerem = prefexPerem.substring(0, prefexPerem.length() - ("qb".length() + 1));
                        break;

                    case Constants.deltaAddPrefixElse:
                        prefexPerem += ".else";
                        break;

                    case Constants.deltaRemoveElse:
                        prefexPerem = prefexPerem.substring(0, prefexPerem.length() - ("else".length() + 1));
                        break;

                    case Constants.deltaPlaceJmp:
                        triads.add(pointerTriad, new Triad("if", String.valueOf(pointerTriad + 1) + ")", ""));
                        prefixIf += ".if";
                        pointerTriad++;
                        break;

                    case Constants.deltaJumpAfterIfWithAddGo:
                        Triad triad = getTriadByProcGoIf("go");
                        triad.operand1 = String.valueOf(pointerTriad) + ")";
                        break;

                    case Constants.deltaJumpAfterIfWithElse:
                        triads.add(pointerTriad++, new Triad("go", "", ""));
                        Triad triadJAWE = getTriadByProcGoIf("if");
                        triadJAWE.operand2 = String.valueOf(pointerTriad) + ")";
                        break;

                    case Constants.deltaJumpAfterIf:
                        Triad triadJAI = getTriadByProcGoIf("if");
                        triadJAI.operand2 = String.valueOf(pointerTriad) + ")";
                        break;

                } // end switch
            }
            pointer--;
        } // end cycle

        return resCode;
    }


}
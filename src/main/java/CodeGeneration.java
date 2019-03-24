import java.util.ArrayList;

public class CodeGeneration {
    public ArrayList<Triad> generate(ArrayList<Triad> triads) {
        regAvail.add("eax");
        regAvail.add("ebx");
        regAvail.add("ecx");
        regAvail.add("edx");
        ArrayList<Integer> goLinks = new ArrayList<>();
        boolean isFun = false;
        boolean isFindRes = false;
        int countJmp = 0;
        int result = 0;
        int sizeFun = 0;
        // 0 - no remove
        // 1 - remove true
        // -1 - remove false
        int removeInIf = 0;
        Triad startFun = null;
        String indexResult = "";
        ArrayList<Triad> codeTriad = new ArrayList<>();
        for (Triad triad : triads) {
            for (int oneTriad : goLinks) {
                if (triad.index == oneTriad) {
                    codeTriad.add(new Triad(".L" + (int) triad.index + ":", "", ""));
                    goLinks.remove(goLinks.indexOf(oneTriad));
                    break;
                }
            }
            if (triad.proc.equals("int") || triad.proc.equals("char")) {
                if (!isFun) {
                    codeTriad.add(new Triad("", ".global", triad.operand1));
                    codeTriad.add(new Triad("", ".data", ""));
                    int size = 0;
                    if (triad.proc.equals("int"))
                        size = Constants.sizeInt;
                    else if (triad.proc.equals("char"))
                        size = Constants.sizeChar;
                    codeTriad.add(new Triad("", ".align", String.valueOf(size)));
                    codeTriad.add(new Triad(".type", triad.operand1, "@object"));
                    codeTriad.add(new Triad(".size", triad.operand1, String.valueOf(size)));

                }
            }
            if (triad.proc.equals("=")) {
                isFindRes = true;
                if (!isFun) {
                    indexResult = triad.operand1;
                    codeTriad.add(new Triad(triads.get(Integer.valueOf(triad.operand1.split("\\)")[0])).operand1 + ":", "", ""));
                    codeTriad.add(new Triad(".long", String.valueOf(resultCalc(triads, triads.get(Integer.valueOf(triad.operand2.split("\\)")[0])), codeTriad, startFun)), ""));
                } else {
                    if (triad.operand2.indexOf(')') != -1) {
                        isNum = true;
                        int lastSizeCodeGen = codeTriad.size();
                        int resultCalc = resultCalc(triads, triads.get(Integer.valueOf(triad.operand2.split("\\)")[0])), codeTriad, startFun);
                        if (triad.operand1.equals("if")) {
                            if (isNum) {
                                if (resultCalc > 0) {
                                    removeInIf = -1;
                                } else {
                                    removeInIf = 1;
                                }
                                for (int i = codeTriad.size() - 1; codeTriad.size() != lastSizeCodeGen; i--) {
                                    codeTriad.remove(codeTriad.get(i));
                                }
                            } else {
                                if (cmpOper.equals("")) {
                                    codeTriad.add(new Triad("cmpl", "$0", "%" + regStack.get(regStack.size() - 1)));
                                    regAvail.add(regStack.get(regStack.size() - 1));
                                    regStack.remove(regStack.size() - 1);
                                    cmpOper = "je";
                                } else if (cmpOper.equals("=="))
                                    cmpOper = "je";
                                else if (cmpOper.equals(">"))
                                    cmpOper = "jm";
                                else if (cmpOper.equals("<"))
                                    cmpOper = "jl";
                                else if (cmpOper.equals(">="))
                                    cmpOper = "jme";
                                else if (cmpOper.equals("<="))
                                    cmpOper = "jle";
                                else if (cmpOper.equals("!="))
                                    cmpOper = "jne";
                                regAvail.add(regStack.get(regStack.size() - 1));
                                regStack.remove(regStack.size() - 1);
                            }
                        } else {
                            if (isNum)
                                codeTriad.add(new Triad("movl",
                                        "$" + String.valueOf(resultCalc),
                                        "-" + String.valueOf(bytePos(startFun, triads, triads.get(Integer.valueOf(triad.operand1.split("\\)")[0])).operand1)) + "(%rbp)"));
                            else {
                                codeTriad.add(new Triad("movl",
                                        "$" + String.valueOf(regStack.get(regStack.size() - 1)),
                                        "-" + String.valueOf(bytePos(startFun, triads, triads.get(Integer.valueOf(triad.operand1.split("\\)")[0])).operand1)) + "(%rbp)"));
                                regAvail.add(regStack.get(regStack.size() - 1));
                                regStack.remove(regStack.size() - 1);
                            }
                        }
                    } else
                        codeTriad.add(new Triad("movl",
                                "$" + triad.operand2,
                                "-" + String.valueOf(bytePos(startFun, triads, triads.get(Integer.valueOf(triad.operand1.split("\\)")[0])).operand1)) + "(%rbp)"));
                }
            }
            if (triad.proc.equals("go")) {
                codeTriad.add(new Triad("jmp", ".L" + triad.operand1.split("\\)")[0], ""));
                goLinks.add(Integer.valueOf(triad.operand1.split("\\)")[0]));
            }
            if (triad.proc.equals("if")) {
                if (isNum) {
                    if (removeInIf == 11) {
                        removeTriads(triads, Integer.valueOf(triad.operand1.split("\\)")[0]), Integer.valueOf(triad.operand2.split("\\)")[0]) - 1);
                    } else if (removeInIf == -11) {
                        if (triads.get(Integer.valueOf(triad.operand2.split("\\)")[0]) - 1).proc.equals("go"))
                            removeTriads(triads, Integer.valueOf(triad.operand2.split("\\)")[0]) - 1, Integer.valueOf(triads.get(Integer.valueOf(triad.operand2.split("\\)")[0]) - 1).operand1.split("\\)")[0]) - 1);
                    } else {
                        codeTriad.add(new Triad(cmpOper, ".L" + triad.operand2.split("\\)")[0], ""));
                        goLinks.add(Integer.valueOf(triad.operand2.split("\\)")[0]));
                    }
                } else {
                    codeTriad.add(new Triad(cmpOper, ".L" + triad.operand2.split("\\)")[0], ""));
                    goLinks.add(Integer.valueOf(triad.operand2.split("\\)")[0]));
                }
            }
            if (triad.proc.equals("fun") && triad.operand1.equals("main") && !triad.operand2.equals("end")) {
                codeTriad.add(new Triad(".text", "", ""));
                codeTriad.add(new Triad(".global", triad.operand1, ""));
                codeTriad.add(new Triad(".type", triad.operand1, "@function"));
                codeTriad.add(new Triad("main:", "", ""));
                codeTriad.add(new Triad(".LFB0:", "", ""));
                codeTriad.add(new Triad(".cfi_startproc", "", ""));
                codeTriad.add(new Triad("pushq", "%rbp", ""));
                codeTriad.add(new Triad(".cfi_def_cfa_offset", "16", ""));
                codeTriad.add(new Triad(".cfi_offset", "6", "-16"));
                codeTriad.add(new Triad("movq", "%rsp", "%rbp"));
                codeTriad.add(new Triad(".cfi_def_cfa_register", "6", ""));
                sizeFun = Integer.valueOf(triad.operand2);
                startFun = triad;
                isFun = true;
            }
            if (triad.proc.equals("fun") && triad.operand1.equals("main") && triad.operand2.equals("end")) {
                codeTriad.add(new Triad("nop", "", ""));
                codeTriad.add(new Triad("popq", "%rbp", ""));
                codeTriad.add(new Triad(".cfi_def_cfa", "7", "8"));
                codeTriad.add(new Triad("ret", "", ""));
                codeTriad.add(new Triad(".cfi_endproc", "", ""));
                codeTriad.add(new Triad(".LFE0:", "", ""));
                codeTriad.add(new Triad(".size", "main", ".-main"));
                codeTriad.add(new Triad(".ident", "\"GCC: (Ubuntu 7.3.0-27ubuntu1~18.04) 7.3.0\"", ""));
                codeTriad.add(new Triad(".section", ".note.GNU-stack,\"\"", "@progbits"));
            }
        }
        return codeTriad;
    }

    private int removeTriads(ArrayList<Triad> triads, int indexStart, int indexEnd) {
        int countBias = 0;
        for (int i = indexStart; i <= indexEnd; i++) {
            if (triads.get(i).proc.equals("int"))
                countBias += Constants.sizeInt;
            else if (triads.get(i).proc.equals("char")) {
                countBias += Constants.sizeChar;
            }
            triads.remove(triads.get(i));
        }
        return countBias;
    }

    private int bytePos(Triad funStart, ArrayList<Triad> triads, String name) {
        String nameFun = funStart.operand1;
        int size = Integer.valueOf(funStart.operand2);
        int bias = 0;
        for (int i = triads.indexOf(funStart); !(triads.get(i).proc.equals("fun") &&
                triads.get(i).operand1.equals(nameFun) && triads.get(i).operand2.equals("end")); i++) {
            if (triads.get(i).proc.equals("int")) {
                if (triads.get(i).operand1.equals(name))
                    return size - bias;
                else bias += Constants.sizeInt;
            } else if (triads.get(i).proc.equals("char")) {
                if (triads.get(i).operand1.equals(name))
                    return size - bias;
                else bias += Constants.sizeChar;
            }
        }
        return size - bias;
    }

    private int reg = -1;
    private String cmpOper = "";
    private boolean retReg = false;
    private boolean isNum = true;
    private ArrayList<String> regStack = new ArrayList<>();
    private ArrayList<String> regAvail = new ArrayList<>();

    /*
    + 3 a           + 4) 2          - 3 a
    mov eax, a      - a 5           mov eax, a
    add eax, 3      mov eax, a      sub eax, 3
    return eax      sub eax, 5      mul eax, -1
                    return eax
                    add eax, 2
     */

    private int resultCalc(ArrayList<Triad> triads, Triad thisTriad, ArrayList<Triad> codeTriad, Triad funStart) {
        int resultFirst = 0;
        String regFirst = "";
        if (thisTriad.operand1.indexOf(')') != -1) {
            resultFirst = resultCalc(triads, triads.get(Integer.valueOf(thisTriad.operand1.split("\\)")[0])), codeTriad, funStart);
            if (!isNum) {
                isNum = true;
                regFirst = regStack.get(regStack.size() - 1);
                regStack.remove(regStack.size() - 1);
            }
        } else {
            try {
                resultFirst = Integer.valueOf(thisTriad.operand1.split("\\)")[0]);
            } catch (Exception e) {
                isNum = false;
                regStack.add(regAvail.get(regAvail.size() - 1));
                codeTriad.add(new Triad("movl", bytePos(funStart, triads, thisTriad.operand1) + "(%rbp)", regAvail.get(regAvail.size() - 1)));
                regAvail.remove(regAvail.size() - 1);
                return Integer.MAX_VALUE;
            }
        }
        int resultSec = 0;
        String regSec = "";
        if (thisTriad.operand2.indexOf(')') != -1) {
            resultSec = resultCalc(triads, triads.get(Integer.valueOf(thisTriad.operand2.split("\\)")[0])), codeTriad, funStart);
            if (!isNum) {
                isNum = true;
                regSec = regStack.get(regStack.size() - 1);
                regStack.remove(regStack.size() - 1);
            }
        } else {
            try {
                resultSec = Integer.valueOf(thisTriad.operand2.split("\\)")[0]);
            } catch (Exception e) {
                isNum = false;
                regStack.add(regAvail.get(regAvail.size() - 1));
                codeTriad.add(new Triad("movl", bytePos(funStart, triads, thisTriad.operand1) + "(%rbp)", regAvail.get(regAvail.size() - 1)));
                regAvail.remove(regAvail.size() - 1);
                return Integer.MAX_VALUE;
            }
        }
        //triads.remove(thisTriad);
        if (!regFirst.equals("") && !regSec.equals("")) {
            isNum = false;
            regStack.add(regFirst);
            regAvail.add(regSec);
            //codeTriad.add(new Triad("movl", regAvail.get(regAvail.size() - 1), bytePos(funStart, triads, thisTriad.operand1) + "(%rbp)"));
            //codeTriad.add(new Triad("movl", regAvail.get(regAvail.size() - 1), bytePos(funStart, triads, thisTriad.operand1) + "(%rbp)"));
            if (thisTriad.proc.equals("+"))
                codeTriad.add(new Triad("addl", regSec, regFirst));
            if (thisTriad.proc.equals("-"))
                codeTriad.add(new Triad("subl", regSec, regFirst));
            if (thisTriad.proc.equals("*"))
                codeTriad.add(new Triad("multl", regSec, regFirst));
            if (thisTriad.proc.equals("/"))
                codeTriad.add(new Triad("divl", regSec, regFirst));
            if (thisTriad.proc.equals("==") || thisTriad.proc.equals(">") ||
                    thisTriad.proc.equals("<") || thisTriad.proc.equals(">=") ||
                    thisTriad.proc.equals("<=")) {
                codeTriad.add(new Triad("cmpl", regFirst, regSec));
                cmpOper = thisTriad.proc;
            }


            return Integer.MAX_VALUE;
        } else if (!regFirst.equals("")) {
            isNum = false;
            regStack.add(regFirst);
            if (thisTriad.proc.equals("+"))
                codeTriad.add(new Triad("addl", "$" + resultSec, regFirst));
            if (thisTriad.proc.equals("-"))
                codeTriad.add(new Triad("subl", "$" + resultSec, regFirst));
            if (thisTriad.proc.equals("*"))
                codeTriad.add(new Triad("multl", "$" + resultSec, regFirst));
            if (thisTriad.proc.equals("/"))
                codeTriad.add(new Triad("divl", "$" + resultSec, regFirst));
            if (thisTriad.proc.equals("==") || thisTriad.proc.equals(">") ||
                    thisTriad.proc.equals("<") || thisTriad.proc.equals(">=") ||
                    thisTriad.proc.equals("<=")) {
                codeTriad.add(new Triad("cmpl", regFirst, "$" + resultSec));
                cmpOper = thisTriad.proc;
            }
            return Integer.MAX_VALUE;

        } else if (!regSec.equals("")) {
            isNum = false;
            regStack.add(regSec);
            if (thisTriad.proc.equals("+"))
                codeTriad.add(new Triad("addl", "$" + resultFirst, regSec));
            if (thisTriad.proc.equals("-")) {
                codeTriad.add(new Triad("subl", "$" + regFirst, regSec));
                codeTriad.add(new Triad("multl", "$" + -1, regSec));
            }
            if (thisTriad.proc.equals("*"))
                codeTriad.add(new Triad("multl", "$" + resultFirst, regSec));
            if (thisTriad.proc.equals("/")) {
                codeTriad.add(new Triad("movl", "$" + resultFirst, regAvail.get(regAvail.size() - 1)));
                codeTriad.add(new Triad("divl", regSec, regAvail.get(regAvail.size() - 1)));
                codeTriad.add(new Triad("movl", regAvail.get(regAvail.size() - 1), regSec));
            }
            if (thisTriad.proc.equals("==") || thisTriad.proc.equals(">") ||
                    thisTriad.proc.equals("<") || thisTriad.proc.equals(">=") ||
                    thisTriad.proc.equals("<=")) {
                codeTriad.add(new Triad("cmpl", "$" + resultFirst, regSec));
                cmpOper = thisTriad.proc;
            }
            return Integer.MAX_VALUE;

        }
        if (thisTriad.proc.equals("+"))
            return resultFirst + resultSec;
        if (thisTriad.proc.equals("-"))
            return resultFirst - resultSec;
        if (thisTriad.proc.equals("*"))
            return resultFirst * resultSec;
        if (thisTriad.proc.equals("/"))
            return resultFirst / resultSec;
        if (thisTriad.proc.equals("%"))
            return resultFirst % resultSec;
        if (thisTriad.proc.equals("=="))
            if (resultFirst == resultSec)
                return 1;
            else
                return 0;
        if (thisTriad.proc.equals(">"))
            if (resultFirst > resultSec)
                return 1;
            else
                return 0;
        if (thisTriad.proc.equals("<"))
            if (resultFirst < resultSec)
                return 1;
            else
                return 0;
        if (thisTriad.proc.equals(">="))
            if (resultFirst >= resultSec)
                return 1;
            else
                return 0;
        if (thisTriad.proc.equals("<="))
            if (resultFirst <= resultSec)
                return 1;
            else
                return 0;
        return resultFirst + resultSec;

    }
}
import java.util.ArrayList;

public class CodeGeneration {
    public String generate(ArrayList<Triad> triads) {
        boolean isFun = false;
        boolean isFindRes = false;
        int result = 0;
        String indexResult = "";
        ArrayList<Triad> codeTriad = new ArrayList<>();
        for (Triad triad : triads) {
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
                    codeTriad.add(new Triad(".long", String.valueOf(resultCalc(triads, triads.get(Integer.valueOf(triad.operand2.split("\\)")[0])))), ""));
                }
            }

            if (triad.proc.equals("fun") && triad.operand1.equals("main")) {
                codeTriad.add(new Triad(".text", "", ""));
                codeTriad.add(new Triad(".global", triad.operand1, ""));
                codeTriad.add(new Triad(".type", triad.operand1, "@function"));
                codeTriad.add(new Triad("main:", "", ""));
                codeTriad.add(new Triad(".LFB0:", "", ""));
                isFun = true;
            }
        }
        return "";
    }

    private int resultCalc(ArrayList<Triad> triads, Triad thisTriad) {
        int resultFirst = 0;
        if (thisTriad.operand1.split("\\)").length > 1)
            resultFirst = resultCalc(triads, triads.get(Integer.valueOf(thisTriad.operand1.split("\\)")[0])));
        else
            resultFirst = Integer.valueOf(thisTriad.operand1.split("\\)")[0]);
        int resultSec = 0;
        if (thisTriad.operand2.split("\\)").length > 1)
            resultSec = resultCalc(triads, triads.get(Integer.valueOf(thisTriad.operand2.split("\\)")[0])));
        else
            resultSec = Integer.valueOf(thisTriad.operand2.split("\\)")[0]);
        triads.remove(thisTriad);
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

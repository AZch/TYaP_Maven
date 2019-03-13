import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ScanerPR {
    private char[] text;
    private int pointer;
    private int countStr = 0;
    private int pointerCountStr = -1;

    char[][] keyWords = new char[][] {
            {'i', 'n', 't'}, {'c', 'h', 'a', 'r'}, {'c', 'o', 'n', 's', 't'}, {'i', 'f'},
            {'e', 'l', 's', 'e'}, {'c', 'l', 'a', 's', 's'}, {'v', 'o', 'i', 'd'}, {'m', 'a', 'i', 'n'}
    };

    int[] indexKeyWords = new int[] {
            Constants.INT_PR, Constants.CHAR_PR, Constants.CONST_PR, Constants.IF_PR,
            Constants.ELSE_PR, Constants.CLASS_PR, Constants.VOID_PR, Constants.MAIN_PR
    };

    public int getPointer () {
        return pointer;
    }

    public void setPointer(int index) {
        pointer = index;
    }

    public void PrintError(char[] error, char[] nextLext) {
        if (nextLext[0] == '\0') {
            System.out.println("Ошибка: " + String.valueOf(error) + " " + String.valueOf(nextLext));
        } else {
            System.out.println("Ошибка: " + String.valueOf(error) + ". Неверный символ " + String.valueOf(nextLext) + "\n");
        }
        System.exit(0);
    }

    public int processScanner(char[] lex) {
        int indexLex;
        for (indexLex = 0; indexLex < Constants.MAX_LEX; indexLex++) lex[indexLex] = 0;
        indexLex = 0;
        while (text[pointer] != '\0') {
            // пропуск игнорируемых символов
            while (text[pointer] == ' ' || text[pointer] == '\n' || text[pointer] == '\t' || text[pointer] == '\r') {
                if (text[pointer] == '\n' && pointerCountStr != pointer) {
                    System.out.println("Строка: " + countStr++);
                    pointerCountStr = pointer;
                }
                pointer++;

            }

            if (text[pointer] == '/' && text[pointer + 1] == '/') {
                pointer += 2;
                while (text[pointer] != '\n')
                    pointer++;
                continue;
            }
            if (text[pointer] == '\0') {
                lex[0] = '#';
                return Constants.END_PR;
            }
            if (text[pointer] <= '9' && text[pointer] >= '0') { // цифра
                LexResInd inRes =  processNum(lex, indexLex);
                lex = inRes.getLex();
                indexLex = inRes.getIndexLex();
                return inRes.getCodeRes();
            } else if (text[pointer] >= 'a' && text[pointer] <= 'z' ||
                        (text[pointer] >= 'A' && text[pointer] <= 'Z')) { // идентификатор
                LexResInd inRes = processID(lex, indexLex);
                lex = inRes.getLex();
                indexLex = inRes.getIndexLex();
                return inRes.getCodeRes();

            } else if (text[pointer] == '.') {
                lex[indexLex++] = text[pointer++];
                return Constants.DOT_PR;
//                if (text[pointer] >= 'a' && text[pointer] <= 'z' ||
//                        (text[pointer] >= 'A' && text[pointer] <= 'Z')) {
//                    LexResInd inRes = processID(lex, indexLex);
//                    lex = inRes.getLex();
//                    indexLex = inRes.getIndexLex();
//                    return inRes.getCodeRes();
//                }

            } else if (text[pointer] == '\'') {
                pointer++; // '
                lex[indexLex++] = text[pointer++];
                if (text[pointer] != '\'') {
                    PrintError("Неверная char константа".toCharArray(), lex);
                    return Constants.ERROR;
                }
                pointer++; // '
                return Constants.NUM_PR;
            } else if (text[pointer] == ',') {
                lex[indexLex++] = text[pointer++];
                return Constants.VIRGULE_PR;

            } else if (text[pointer] == ')') {
                lex[indexLex++] = text[pointer++];
                return Constants.RBC_PR;

            } else if (text[pointer] == '(') {
                lex[indexLex++] = text[pointer++];
                return Constants.RBO_PR;

            } else if (text[pointer] == '+') {
                lex[indexLex++] = text[pointer++];
                return Constants.PLUS_MINUS_PR;

            } else if (text[pointer] == '-') {
                lex[indexLex++] = text[pointer++];
                return Constants.PLUS_MINUS_PR;

            } else if (text[pointer] == '*') {
                lex[indexLex++] = text[pointer++];
                return Constants.MULT_SLASH_PERCENT_PR;

            } else if (text[pointer] == '/') {
                lex[indexLex++] = text[pointer++];
                return Constants.MULT_SLASH_PERCENT_PR;

            } else if (text[pointer] == '%') {
                lex[indexLex++] = text[pointer++];
                return Constants.MULT_SLASH_PERCENT_PR;

            } else if (text[pointer] == '{') {
                lex[indexLex++] = text[pointer++];
                return Constants.CBO_PR;

            } else if (text[pointer] == '}') {
                lex[indexLex++] = text[pointer++];
                return Constants.CBC_PR;

            } else if (text[pointer] == '[') {
                lex[indexLex++] = text[pointer++];
                return Constants.SBO_PR;

            } else if (text[pointer] == ']') {
                lex[indexLex++] = text[pointer++];
                return Constants.SBC_PR;

            } else if (text[pointer] == ';') {
                lex[indexLex++] = text[pointer++];
                return Constants.COMMA_PR;

            } else if (text[pointer] == '<') {
                lex[indexLex++] = text[pointer++];
                if (text[pointer] == '=') {
                    lex[indexLex++] = text[pointer++];
                    return Constants.MORE_EQUAL_LESS_PR;
                }
                return Constants.MORE_EQUAL_LESS_PR;

            } else if (text[pointer] == '>') {
                lex[indexLex++] = text[pointer++];
                if (text[pointer] == '=') {
                    lex[indexLex++] = text[pointer++];
                    return Constants.MORE_EQUAL_LESS_PR;
                }
                return Constants.MORE_EQUAL_LESS_PR;
            } else if (text[pointer] == '=') {
                lex[indexLex++] = text[pointer++];
                if (text[pointer] == '=') {
                    lex[indexLex++] = text[pointer++];
                    return Constants.EQUAL_PR;
                }
                return Constants.ASSIGN_PR;
            } else if (text[pointer] == '|') {
                lex[indexLex++] = text[pointer++];
                if (text[pointer] == '|') {
                    lex[indexLex++] = text[pointer++];
                    return Constants.OR_PR;
                }
                return Constants.ERROR;
            } else if (text[pointer] == '&') {
                lex[indexLex++] = text[pointer++];
                if (text[pointer] == '&') {
                    lex[indexLex++] = text[pointer++];
                    return Constants.AND_PR;
                }
                return Constants.ERROR;
            }

            while (text[pointer] != ' ' && text[pointer] != '\n' && text[pointer] != '\t' && text[pointer] != '\r')
                lex[indexLex++] = text[pointer++];
            PrintError("i dont now what is it".toCharArray(), lex);
            return Constants.ERROR;
        }
        return Constants.END_PR;
    }

    private LexResInd processID(char[] lex, int indexLex) {
        LexResInd retData = new LexResInd();
        lex[indexLex++] = text[pointer++];
        while (checkDiapThreeSymb('9', '0', 'Z',
                'A', 'z', 'a'))
            if (indexLex < Constants.MAX_LEX - 1)
                lex[indexLex++] = text[pointer++];
            else // брезка длинного идентификатора
                pointer++;
        // проверка на ключевое слово
        for (int jKeyWord = 0; jKeyWord < Constants.MAX_KEYW; jKeyWord++)
            if (equalsChars(lex, keyWords[jKeyWord])) {
                retData.setCodeRes(indexKeyWords[jKeyWord]);
                retData.setIndexLex(indexLex);
                retData.setLex(lex);
                return retData;
            }
        retData.setIndexLex(indexLex);
        retData.setLex(lex);
        retData.setCodeRes(Constants.ID_PR);
        return retData;
    }

    private LexResInd processNum(char[] lex, int indexLex) {
        LexResInd retData = new LexResInd();
        if (text[pointer] == '0' && text[pointer + 1] == 'x') { // шестнадцетиричная цифра
            return processSNum(lex, indexLex);
        }
        lex[indexLex++] = text[pointer++];
        while (text[pointer] <= '9' && text[pointer] >= '0')
            if (indexLex < Constants.MAX_LEX - 1)
                lex[indexLex++] = text[pointer++];
            else
                pointer++;
        retData.setLex(lex);
        retData.setIndexLex(indexLex);
        retData.setCodeRes(Constants.NUM_PR);
        return retData;
    }

    private LexResInd processSNum(char[] lex, int indexLex) {
        LexResInd retData = new LexResInd();
        lex[indexLex++] = text[pointer++];
        lex[indexLex++] = text[pointer++];
        while (checkDiapThreeSymb('9', '0', 'F',
                'A', 'f', 'a'))
            if (indexLex < Constants.MAX_LEX)
                lex[indexLex++] = text[pointer++];
            else
                pointer++;
        retData.setLex(lex);
        retData.setIndexLex(indexLex);
        retData.setCodeRes(Constants.NUM_PR);
        return retData;
    }

    private boolean equalsChars(char[] first, char[] second) {
//        if (first.length != second.length)
//            return false;
        for (int i = 0; i < second.length; i++)
            if (first[i] != second[i])
                return false;
        return true;
    }

    private boolean checkDiapThreeSymb(char firstStart, char secondStart, char firstMid, char secondMid,
                                            char firstEnd, char secondEnd) {
        return (text[pointer] <= firstStart && text[pointer] >= secondStart) ||
                (text[pointer] <= firstMid && text[pointer] >= secondMid) ||
                (text[pointer] <= firstEnd && text[pointer] >= secondEnd);
    }

    public void GetData(String nameFile) throws IOException {
        //BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        File file = new File(nameFile);
        BufferedReader finalLoad = new BufferedReader(new FileReader(file));
        int symbol = finalLoad.read();
        int indexText = 0;
        while (symbol != -1) {
            text[indexText++] = (char) symbol;
            if (indexText >= Constants.MAX_TEXT - 1) {
                PrintError("to mach size of file".toCharArray(), "".toCharArray());
                break;
            }
            symbol = finalLoad.read();
        }
        text[indexText++] = '\n';
        text[indexText] = '\0';
        finalLoad.close();
        //System.exit(1);
    }

    ScanerPR(String nameFile) {
        text = new char[Constants.MAX_TEXT];
        pointer = 0;
        try {
            GetData(nameFile);
        } catch (IOException e) {
            PrintError("cant read file".toCharArray(), "".toCharArray());
        }
    }
}

import java.io.*;

public class Scaner {
    private char[] text;
    private int pointer;
    private int countStr = 0;
    private int pointerCountStr = -1;

    char[][] keyWords = new char[][] {
            {'i', 'n', 't'}, {'c', 'h', 'a', 'r'}, {'c', 'o', 'n', 's', 't'}, {'i', 'f'},
            {'e', 'l', 's', 'e'}, {'c', 'l', 'a', 's', 's'}, {'v', 'o', 'i', 'd'}, {'m', 'a', 'i', 'n'}
    };

    int[] indexKeyWords = new int[] {
            Constants.INT, Constants.CHAR, Constants.CONST, Constants.IF,
            Constants.ELSE, Constants.CLASS, Constants.VOID, Constants.MAIN
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
                return Constants.END;
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
                return Constants.DOT;
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
                return Constants.TYPE_CHAR;
            } else if (text[pointer] == ',') {
                lex[indexLex++] = text[pointer++];
                return Constants.VIRGULE;

            } else if (text[pointer] == ')') {
                lex[indexLex++] = text[pointer++];
                return Constants.ROUND_BRACE_CLOSE;

            } else if (text[pointer] == '(') {
                lex[indexLex++] = text[pointer++];
                return Constants.ROUND_BRACE_OPEN;

            } else if (text[pointer] == '+') {
                lex[indexLex++] = text[pointer++];
                return Constants.PLUS;

            } else if (text[pointer] == '-') {
                lex[indexLex++] = text[pointer++];
                return Constants.MINUS;

            } else if (text[pointer] == '*') {
                lex[indexLex++] = text[pointer++];
                return Constants.STAR;

            } else if (text[pointer] == '/') {
                lex[indexLex++] = text[pointer++];
                return Constants.SLASH;

            } else if (text[pointer] == '%') {
                lex[indexLex++] = text[pointer++];
                return Constants.PERCENT;

            } else if (text[pointer] == '{') {
                lex[indexLex++] = text[pointer++];
                return Constants.CURLY_BRACE_OPEN;

            } else if (text[pointer] == '}') {
                lex[indexLex++] = text[pointer++];
                return Constants.CURLY_BRACE_CLOSE;

            } else if (text[pointer] == '[') {
                lex[indexLex++] = text[pointer++];
                return Constants.SQUARE_BRACE_OPEN;

            } else if (text[pointer] == ']') {
                lex[indexLex++] = text[pointer++];
                return Constants.SQUARE_BRACE_CLOSE;

            } else if (text[pointer] == ';') {
                lex[indexLex++] = text[pointer++];
                return Constants.COMMA;

            } else if (text[pointer] == '<') {
                lex[indexLex++] = text[pointer++];
                if (text[pointer] == '=') {
                    lex[indexLex++] = text[pointer++];
                    return Constants.LESS_EQUAL;
                }
                return Constants.LESS;

            } else if (text[pointer] == '>') {
                lex[indexLex++] = text[pointer++];
                if (text[pointer] == '=') {
                    lex[indexLex++] = text[pointer++];
                    return Constants.MORE_EQUAL;
                }
                return Constants.MORE;
            } else if (text[pointer] == '=') {
                lex[indexLex++] = text[pointer++];
                if (text[pointer] == '=') {
                    lex[indexLex++] = text[pointer++];
                    return Constants.EQUAL;
                }
                return Constants.ASSIGN;
            } else if (text[pointer] == '|') {
                lex[indexLex++] = text[pointer++];
                if (text[pointer] == '|') {
                    lex[indexLex++] = text[pointer++];
                    return Constants.OR;
                }
                return Constants.ERROR;
            } else if (text[pointer] == '&') {
                lex[indexLex++] = text[pointer++];
                if (text[pointer] == '&') {
                    lex[indexLex++] = text[pointer++];
                    return Constants.AND;
                }
                return Constants.ERROR;
            }

            while (text[pointer] != ' ' && text[pointer] != '\n' && text[pointer] != '\t' && text[pointer] != '\r')
                lex[indexLex++] = text[pointer++];
            PrintError("i dont now what is it".toCharArray(), lex);
            return Constants.ERROR;
        }
        return Constants.END;
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
        retData.setCodeRes(Constants.ID);
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
        retData.setCodeRes(Constants.TYPE_INT);
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
        retData.setCodeRes(Constants.TYPE_SINT);
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
        BufferedReader finalLoad = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), "UTF-8"));
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
    }

    Scaner(String nameFile) {
        text = new char[Constants.MAX_TEXT];
        pointer = 0;
        try {
            GetData(nameFile);
        } catch (IOException e) {
            PrintError("cant read file".toCharArray(), "".toCharArray());
        }
    }
}

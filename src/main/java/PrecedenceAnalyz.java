public class PrecedenceAnalyz {
    private int[] magazine = new int[5000];
    private int pointer = 0;

    public int analyz(ScanerPR scaner) {
        String[][] PrecedentMatrix = {
                {"",   "<=", "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "" },
                {"",   "<=", "<=", "",   ">!=","",   "",   "",   ">",  "",   ">!=",">",  ">",  ">",  ">!=","<=", ">",  "",   ">",  ">",  ">",  ">",  ">",  ">",  "",   "",   "" },
                {"",   "<=", "<=", "<=", "",   "",   "",   "",   "",   "<=", "<=", "<=", "<=", "<=", "",   "",   "",   "<=", "",   "",   "",   "",   "",   "",   "<=", "",   "" },
                {"<=", ">",  ">",  ">",  ">!=","<=", "",   "",   ">",  "<=", "",   ">",  ">",  ">",  "",   "",   ">",  "",   "",   "",   "",   "",   "",   "",   "",   ">",  ">"},
                {"<=", "<=", ">",  ">",  "",   "<=", "",   "",   "",   "<=", "<=", "<=", "<=", "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   ">",  "",   ">"},
                {"",   "",   "",   "",   "",   "",   "<=", "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "" },
                {"",   "",   "",   "",   "",   "",   "",   "<=", "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "" },
                {"",   "<=", "<=", "",   "",   "",   "",   "<=", "<=", "",   "",   "",   "",   "",   "",   "",   "",   "<=", "<=", "<=", "<=", "<=", "<=", "<=", "",   "",   "" },
                {">",  ">",  "<=", ">",  "",   ">",  "",   "",   ">",  ">",  "",   ">",  ">",  "",   "",   "",   "",   ">",  ">",  ">",  ">",  ">",  ">",  ">",  "",   "<=", ">"},
                {"",   "<=", "<=", "",   "",   "",   "",   "",   "",   "<=", "",   "<=", "<=", "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "" },
                {"",   "<=", "<=", ">",  "<=", "",   "",   "",   "",   "",   "",   ">",  ">",  "",   "<=", "",   "",   "<=", "<=", "<=", "<=", "<=", "<=", "<=", ">",  "",   "" },
                {"",   "<=",  ">", "",   "<=", "",   "",   "",   "",   "",   "<=", ">",  ">",  "<=", "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "" },
                {"",   "<=",  "",  "",   "<=", "",   "",   "",   "",   "",   "<=", ">",  ">",  "<=", "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "" },
                {"",   "<=", "<=", ">",  ">",  "",   "",   "",   "",   "",   "",   "",   "",   ">",  "<=", "",   "",   "<=", "<=", "<=", "<=", "<=", "<=", "<=", "",   "",   "" },
                {"",   "<=", "",   "",   ">",  "",   "",   "",   "",   "",   ">",  "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "" },
                {"",   "<=", "<=", "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "<=", "<=", "",   "",   "",   "",   "",   "",   "",   "",   "" },
                {"",   "",   "",   "",   ">",  "",   "",   "",   "",   "",   ">",  "",   "",   "",   "",   ">",  ">",  "",   "",   "",   "",   "",   "",   "",   "",   "",   "" },
                {"",   "",   "",   ">",  ">",  "",   "",   "",   ">",  "",   "",   "",   "",   ">",  "",   "",   ">",  "",   ">",  ">",  ">",  ">",  ">",  ">",   "",   "",   "" },
                {"",   "<=", "",   "",   ">",  "",   "",   "<=", ">",  "",   "",   "",   "",   "",   "",   "",   "",   "<=", ">",  "<=", "<=", "<=", "<=", "<=", "",   "",   "" },
                {"",   "<=", "",   "",   ">",  "",   "",   "<=", ">",  "",   "",   "",   "",   "",   "",   "",   "",   "<=", ">",  ">",  "<=", "<=", "<=", "<=", "",   "",   "" },
                {"",   "<=", "",   "",   ">",  "",   "",   "<=", ">",  "",   "",   "",   "",   "",   "",   "",   "",   "<=", ">",  ">",  ">",  "<=", "<=", "<=", "",   "",   "" },
                {"",   "<=", "",   "",   ">",  "",   "",   "<=", ">",  "",   "",   "",   "",   "",   "",   "",   "",   "<=", ">",  ">",  ">",  ">",  "<=", "<=", "",   "",   "" },
                {"",   "<=", "",   "",   ">",  "",   "",   "<=", ">",  "",   "",   "",   "",   "",   "",   "",   "",   "<=", ">",  ">",  ">",  ">",  ">",  "<=", "",   "",   "" },
                {"",   "<=", "",   "",   ">",  "",   "",   "<=", ">",  "",   "",   "",   "",   "",   "",   "",   "",   "<=", ">",  ">",  ">",  ">",  ">",  ">",  "",   "",   "" },
                {"",   "",   "",   "",   "",   "",   "",   "<=", "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "" },
                {"",   "",   "<=", ">",  "",   "",   "",   "",   ">",  "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "<=", "",   "" },
                {"<=", "<=", "",   "",   "",   "<=", "",   "",   "",   "<=", "<=", "<=", "<=", "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "",   "" }
        } ;

        int type, resCode = 1;
        int flag = 1, AKS = 99;

        char[] lex = new char[Constants.MAX_LEX], lexSec = new char[Constants.MAX_LEX];
        magazine[pointer++] = Constants.END_PR;
        magazine[pointer++] = AKS;

        type = scaner.processScanner(lex);

        int last = Constants.END_PR, saveLast = 0;
        while (true) {
            String attitude = "";
            saveLast = last;
            if (magazine[pointer - 1] == AKS) {
                last = magazine[pointer - 2];
            } else
                last = magazine[pointer - 1];
            try {
                attitude = PrecedentMatrix[last][type];
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (type == Constants.END_PR) {
                if (magazine[pointer - 1] == AKS && magazine[pointer - 2] == Constants.END_PR) {
                    return resCode;
                }
            }
            if (attitude.equals("<=")) {
                magazine[pointer++] = type;
                type = scaner.processScanner(lex);
            } else if (attitude.equals(">") || attitude.equals(">!=")) {
                if (attitude.equals(">!=")) {
                    if (type == Constants.COMMA_PR && magazine[pointer - 1] == Constants.CBC_PR &&
                            ((magazine[pointer - 2] == AKS && magazine[pointer - 3] == Constants.CBO_PR && magazine[pointer - 4] == Constants.ID_PR && magazine[pointer - 5] == Constants.CLASS_PR) ||
                            (magazine[pointer - 2] == Constants.CBO_PR && magazine[pointer - 3] == Constants.ID_PR && magazine[pointer - 4] == Constants.CLASS_PR))) {
                        magazine[pointer++] = type;
                        type = scaner.processScanner(lex);
                        continue;
                    }
                    if (saveLast ==  Constants.INT_PR || saveLast == Constants.CHAR_PR || saveLast == Constants.ID_PR || saveLast == Constants.VIRGULE_PR) {
                        if (type == Constants.ASSIGN_PR) {
                            magazine[pointer++] = type;
                            type = scaner.processScanner(lex);
                            continue;
                        } else if (type == Constants.COMMA_PR && magazine[pointer - 1] == AKS && magazine[pointer - 2] == Constants.ID_PR && magazine[pointer - 3] == Constants.COMMA_PR) {
                            magazine[pointer++] = type;
                            type = scaner.processScanner(lex);
                            continue;
                        }
                    } else if ((saveLast == Constants.ASSIGN_PR || saveLast == Constants.DOT_PR) && type == Constants.DOT_PR) {
                        magazine[pointer++] = type;
                        type = scaner.processScanner(lex);
                        continue;
                    } else if (type == Constants.DOT_PR && magazine[pointer - 1] == Constants.ID_PR && magazine[pointer - 2] == AKS) {
                        magazine[pointer++] = type;
                        type = scaner.processScanner(lex);
                        continue;
                    }
                }
                pointer--;
                switch (last) {
                    case Constants.COMMA_PR:
                        if (pointer >= 6 && magazine[pointer] == Constants.COMMA_PR && // AKS -> AKS const (int, char, id) id = AKS ;
                            magazine[pointer - 1] == AKS && magazine[pointer - 2] == Constants.ASSIGN_PR &&
                            magazine[pointer - 3] == Constants.ID_PR && (magazine[pointer - 4] == Constants.INT_PR || magazine[pointer - 4] == Constants.CHAR_PR || magazine[pointer - 4] == Constants.ID_PR) &&
                            magazine[pointer - 5] == Constants.CONST_PR && magazine[pointer - 6] == AKS) {
                            pointer -= 6;
                        } else if (pointer >= 6 && magazine[pointer] == Constants.COMMA_PR && // AKS -> AKS class id { AKS } ;
                            magazine[pointer - 1] == Constants.CBC_PR && magazine[pointer - 2] == AKS &&
                            magazine[pointer - 3] == Constants.CBO_PR && magazine[pointer - 4] == Constants.ID_PR &&
                            magazine[pointer - 5] == Constants.CLASS_PR && magazine[pointer - 6] == AKS) {
                            pointer -= 6;
                        } else if (pointer >= 7 && magazine[pointer] == AKS && magazine[pointer - 1] == Constants.COMMA_PR && // AKS -> AKS class id { AKS } ; AKS
                                magazine[pointer - 2] == Constants.CBC_PR && magazine[pointer - 3] == AKS &&
                                magazine[pointer - 4] == Constants.CBO_PR && magazine[pointer - 5] == Constants.ID_PR &&
                                magazine[pointer - 6] == Constants.CLASS_PR && magazine[pointer - 7] == AKS) {
                            pointer -= 7;
                        } else if (pointer >= 5 && magazine[pointer] == Constants.COMMA_PR && // AKS -> AKS class id { } ;
                                magazine[pointer - 1] == Constants.CBC_PR &&
                                magazine[pointer - 2] == Constants.CBO_PR && magazine[pointer - 3] == Constants.ID_PR &&
                                magazine[pointer - 4] == Constants.CLASS_PR && magazine[pointer - 5] == AKS) {
                            pointer -= 5;
                        } else if (pointer >= 6 && magazine[pointer] == AKS && magazine[pointer - 1] == Constants.COMMA_PR && // AKS -> AKS class id { } ; AKS
                                magazine[pointer - 2] == Constants.CBC_PR &&
                                magazine[pointer - 3] == Constants.CBO_PR && magazine[pointer - 4] == Constants.ID_PR &&
                                magazine[pointer - 5] == Constants.CLASS_PR && magazine[pointer - 6] == AKS) {
                            pointer -= 6;
                        } else if (pointer >= 6 && magazine[pointer] == AKS && magazine[pointer - 1] == Constants.COMMA_PR && // AKS -> class id { AKS } ; AKS
                                magazine[pointer - 2] == Constants.CBC_PR && magazine[pointer - 3] == AKS &&
                                magazine[pointer - 4] == Constants.CBO_PR && magazine[pointer - 5] == Constants.ID_PR &&
                                magazine[pointer - 6] == Constants.CLASS_PR) {
                            pointer -= 6;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 5 && magazine[pointer] == AKS && magazine[pointer - 1] == Constants.COMMA_PR && // AKS -> class id { } ; AKS
                                magazine[pointer - 2] == Constants.CBC_PR &&
                                magazine[pointer - 3] == Constants.CBO_PR && magazine[pointer - 4] == Constants.ID_PR &&
                                magazine[pointer - 5] == Constants.CLASS_PR) {
                            pointer -= 5;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 6 && magazine[pointer] == AKS && magazine[pointer - 1] == Constants.COMMA_PR && // AKS -> const (id, char, int) id = AKS ; AKS
                                magazine[pointer - 2] == AKS && magazine[pointer - 3] == Constants.ASSIGN_PR &&
                                magazine[pointer - 4] == Constants.ID_PR && (magazine[pointer - 5] == Constants.INT_PR || magazine[pointer - 4] == Constants.CHAR_PR || magazine[pointer - 4] == Constants.ID_PR) &&
                                magazine[pointer - 6] == Constants.CONST_PR) {
                            pointer -= 6;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 5 && magazine[pointer] == Constants.COMMA_PR && // AKS -> const AKS id = num ;
                                magazine[pointer - 1] == AKS && magazine[pointer - 2] == Constants.ASSIGN_PR &&
                                magazine[pointer - 3] == Constants.ID_PR && (magazine[pointer - 4] == Constants.INT_PR || magazine[pointer - 4] == Constants.CHAR_PR || magazine[pointer - 4] == Constants.ID_PR) &&
                                magazine[pointer - 5] == Constants.CONST_PR) {
                            pointer -= 5;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 4 && magazine[pointer] == Constants.COMMA_PR && // AKS -> AKS id = AKS ;
                                magazine[pointer - 1] == AKS && magazine[pointer - 2] == Constants.ASSIGN_PR &&
                                magazine[pointer - 3] == Constants.ID_PR && magazine[pointer - 4] == AKS) {
                            pointer -= 4;
                        } else if (pointer >= 4 && magazine[pointer] == Constants.COMMA_PR && // AKS -> (int, char, id) id = AKS ;
                                magazine[pointer - 1] == AKS && magazine[pointer - 2] == Constants.ASSIGN_PR &&
                                magazine[pointer - 3] == Constants.ID_PR && (magazine[pointer - 4] == Constants.INT_PR || magazine[pointer - 4] == Constants.CHAR_PR || magazine[pointer - 4] == Constants.ID_PR)) {
                            pointer -= 4;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 3 && magazine[pointer] == Constants.COMMA_PR && // AKS -> AKS = AKS ;
                                magazine[pointer - 1] == AKS && magazine[pointer - 2] == Constants.ASSIGN_PR &&
                                magazine[pointer - 3] == AKS) {
                            pointer -= 3;
                        } else if (pointer >= 3 && // id = AKS ;
                                magazine[pointer - 1] == AKS && magazine[pointer - 2] == Constants.ASSIGN_PR &&
                                magazine[pointer - 3] == Constants.ID_PR) {
                            pointer -= 3;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 3 && magazine[pointer] == Constants.COMMA_PR && // AKS (int, char, id) AKS ;
                                magazine[pointer - 1] == AKS &&
                                (magazine[pointer - 2] == Constants.INT_PR || magazine[pointer - 2] == Constants.CHAR_PR || magazine[pointer - 2] == Constants.ID_PR) &&
                                magazine[pointer - 3] == AKS) {
                            pointer -= 3;
                        } else if (pointer >= 2 &&
                                magazine[pointer - 1] == AKS &&
                                (magazine[pointer - 2] == Constants.INT_PR || magazine[pointer - 2] == Constants.CHAR_PR || magazine[pointer - 2] == Constants.ID_PR)) {
                            pointer -= 2;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 5 && // AKS -> AKS (int, char, id) id = AKS ; // new
                                magazine[pointer - 1] == AKS && magazine[pointer - 2] == Constants.ASSIGN_PR &&
                                magazine[pointer - 3] == Constants.ID_PR && (magazine[pointer - 4] == Constants.INT_PR || magazine[pointer - 4] == Constants.CHAR_PR || magazine[pointer - 4] == Constants.ID_PR) &&
                                magazine[pointer - 5] == AKS) {
                            pointer -= 5;
                        } else if (pointer >= 4 && // AKS -> (int, char, id) id = AKS ; // new
                                magazine[pointer - 1] == AKS && magazine[pointer - 2] == Constants.ASSIGN_PR &&
                                magazine[pointer - 3] == Constants.ID_PR && (magazine[pointer - 4] == Constants.INT_PR || magazine[pointer - 4] == Constants.CHAR_PR || magazine[pointer - 4] == Constants.ID_PR) ) {
                            pointer -= 4;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 4 && // AKS -> AKS (int, char, id) id AKS;  // new
                                magazine[pointer - 1] == AKS &&
                                magazine[pointer - 2] == Constants.ID_PR && (magazine[pointer - 3] == Constants.INT_PR || magazine[pointer - 3] == Constants.CHAR_PR || magazine[pointer - 3] == Constants.ID_PR) &&
                                magazine[pointer - 4] == AKS) {
                            pointer -= 4;
                        } else if (pointer >= 5 &&
                                magazine[pointer] == AKS && magazine[pointer - 1] == Constants.COMMA_PR &&
                                magazine[pointer - 2] == AKS && magazine[pointer - 3] == Constants.ASSIGN_PR &&
                                magazine[pointer - 4] == AKS && (magazine[pointer - 5] == Constants.INT_PR || magazine[pointer - 5] == Constants.CHAR_PR || magazine[pointer - 5] == Constants.ID_PR)) {
                            pointer -= 5;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 4 &&
                                magazine[pointer] == AKS && magazine[pointer - 1] == Constants.COMMA_PR &&
                                magazine[pointer - 2] == AKS && magazine[pointer - 3] == Constants.ASSIGN_PR &&
                                magazine[pointer - 4] == AKS) {
                            pointer -=4;
                        } else if (pointer >= 3 && // AKS -> (int, char, id) id AKS;  // new
                                magazine[pointer - 1] == AKS &&
                                magazine[pointer - 2] == Constants.ID_PR && (magazine[pointer - 3] == Constants.INT_PR || magazine[pointer - 3] == Constants.CHAR_PR || magazine[pointer - 3] == Constants.ID_PR)) {
                            pointer -= 3;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 3 && // AKS -> AKS (int, char, id) id ; // new
                                magazine[pointer - 1] == Constants.ID_PR && (magazine[pointer - 2] == Constants.INT_PR || magazine[pointer - 2] == Constants.CHAR_PR || magazine[pointer - 2] == Constants.ID_PR) &&
                                magazine[pointer - 3] == AKS) {
                            pointer -= 3;
                        } else if (pointer >= 2 && // AKS -> (int, char, id) id ; // new
                                magazine[pointer - 1] == Constants.ID_PR && (magazine[pointer - 2] == Constants.INT_PR || magazine[pointer - 2] == Constants.CHAR_PR || magazine[pointer - 2] == Constants.ID_PR)) {
                            pointer -= 2;
                        } else if (pointer >= 5 &&
                                magazine[pointer] == AKS && magazine[pointer - 1] == Constants.COMMA_PR &&
                                magazine[pointer - 2] == AKS && magazine[pointer - 3] == Constants.ASSIGN_PR &&
                                magazine[pointer - 4] == Constants.ID_PR &&
                                (magazine[pointer - 5] == Constants.INT_PR || magazine[pointer - 5] == Constants.CHAR_PR || magazine[pointer - 5] == Constants.ID_PR)) {
                            pointer -= 5;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 4 &&
                                magazine[pointer] == AKS && magazine[pointer - 1] == Constants.COMMA_PR &&
                                magazine[pointer - 2] == AKS &&
                                (magazine[pointer - 3] == Constants.INT_PR || magazine[pointer - 3] == Constants.CHAR_PR || magazine[pointer - 3] == Constants.ID_PR) &&
                                magazine[pointer - 4] == AKS) {
                            pointer -= 4;
                        } else if (pointer >= 3 &&
                                magazine[pointer] == AKS && magazine[pointer - 1] == Constants.COMMA_PR &&
                                magazine[pointer - 2] == AKS &&
                                (magazine[pointer - 3] == Constants.INT_PR || magazine[pointer - 3] == Constants.CHAR_PR || magazine[pointer - 3] == Constants.ID_PR)) {
                            pointer -= 3;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 2 &&
                                magazine[pointer] == AKS && magazine[pointer - 1] == Constants.COMMA_PR &&
                                magazine[pointer - 2] == AKS) {
                            pointer -= 2;
                        } else if (pointer >= 3 && magazine[pointer] == AKS && magazine[pointer - 1] == Constants.COMMA_PR &&
                                magazine[pointer - 2] == Constants.ASSIGN_PR && magazine[pointer - 3] == AKS) {
                                pointer -= 3;
                        } else {
                            scaner.PrintError("Неверный символ".toCharArray(), lex);
                            resCode = 0;
                        }
                        break;
                    case Constants.CBC_PR:
                        if (pointer >= 3 && // AKS = AKS { AKS }
                                magazine[pointer - 1] == AKS && magazine[pointer - 2] == Constants.CBO_PR &&
                                magazine[pointer - 3] == AKS) {
                            pointer -= 3;
                        } else if (pointer >= 3 && // AKS = { AKS } AKS
                                magazine[pointer] == AKS && magazine[pointer - 1] == Constants.CBC_PR &&
                                magazine[pointer - 2] == AKS && magazine[pointer - 3] == Constants.CBO_PR) {
                            pointer -= 3;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 2 && // AKS -> { AKS }
                                magazine[pointer - 1] == AKS && magazine[pointer - 2] == Constants.CBO_PR) {
                            pointer -= 2;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 2 &&
                                magazine[pointer - 1] == Constants.CBO_PR && magazine[pointer - 2] == AKS) {
                            pointer -= 2;
                        } else if (magazine[pointer - 1] == Constants.CBO_PR) {
                            pointer -= 1;
                            magazine[pointer] = AKS;
                        }  else {
                            scaner.PrintError("Неверный символ".toCharArray(), lex);
                            resCode = 0;
                        }
                        break;
                    case Constants.RBC_PR: // где то здесь начал серъезно разбираться в том, что пишу
                        if (pointer >= 5 && // AKS -> AKS void main ( ) AKS
                                magazine[pointer] == AKS
                                 && magazine[pointer - 2] == Constants.RBO_PR &&
                                magazine[pointer - 3] == Constants.MAIN_PR && magazine[pointer - 4] == Constants.VOID_PR &&
                                magazine[pointer - 5] == AKS) {
                            pointer -= 5;
                        } else if (pointer >= 4 && // AKS -> void main ( ) AKS
                                magazine[pointer] == AKS
                                && magazine[pointer - 2] == Constants.RBO_PR &&
                                magazine[pointer - 3] == Constants.MAIN_PR && magazine[pointer - 4] == Constants.VOID_PR) {
                            pointer -= 4;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 5 && // AKS -> AKS if ( AKS ) AKS
                                magazine[pointer] == AKS && magazine[pointer - 1] == Constants.RBC_PR &&
                                magazine[pointer - 2] == AKS && magazine[pointer - 3] == Constants.RBO_PR &&
                                magazine[pointer - 4] == Constants.IF_PR && magazine[pointer - 5] == AKS) {
                            pointer -= 5;
                        } else if (pointer >= 5 && // AKS -> if ( AKS ) AKS
                                magazine[pointer] == AKS && magazine[pointer - 1] == Constants.RBC_PR &&
                                magazine[pointer - 2] == AKS && magazine[pointer - 3] == Constants.RBO_PR &&
                                magazine[pointer - 4] == Constants.IF_PR) {
                            pointer -= 4;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 3 && // AKS -> = ( AKS )
                                magazine[pointer - 1] == AKS && magazine[pointer - 2] == Constants.RBO_PR &&
                                magazine[pointer - 3] == Constants.ASSIGN_PR) {
                            pointer -= 3;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 2 &&
                                magazine[pointer - 1] == AKS && magazine[pointer - 2] == Constants.RBO_PR) {
                            pointer -= 2;
                            magazine[pointer] = AKS;
                        } else {
                            scaner.PrintError("Неверный символ".toCharArray(), lex);
                            resCode = 0;
                        }
                        break;
                    case Constants.SBC_PR:
                        if (pointer >= 3 && // AKS -> AKS [ AKS ]
                                magazine[pointer - 1] == AKS && magazine[pointer - 2] == Constants.SBO_PR &&
                                magazine[pointer - 3] == AKS) { // коричневое нужно ли его добавлять так
                            pointer -= 3;
                        } else if (pointer >= 2 && // AKS -> [ AKS ]
                                magazine[pointer - 1] == AKS && magazine[pointer - 2] == Constants.SBO_PR) {
                            pointer -= 2;
                            magazine[pointer] = AKS;
                        }else {
                            scaner.PrintError("Неверный символ".toCharArray(), lex);
                            resCode = 0;
                        }
                        break;
                    case Constants.VIRGULE_PR:
                        if (pointer >= 2 && // AKS -> AKS , AKS
                                magazine[pointer] == AKS && magazine[pointer - 1] == Constants.VIRGULE_PR &&
                                magazine[pointer - 2] == AKS) {
                            pointer -= 2;
                        } else {
                            scaner.PrintError("Неверный символ".toCharArray(), lex);
                            resCode = 0;
                        }
                        break;
                    default:
                        if (magazine[pointer] == Constants.ID_PR ||
                            magazine[pointer] == Constants.NUM_PR) {
                            magazine[pointer] = AKS;
                        } else if (magazine[pointer] == AKS && (magazine[pointer - 1] == Constants.INT_PR || magazine[pointer - 1] == Constants.CHAR_PR)) {
                            pointer -= 1;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 2 && // AKS .*.||.+. AKS
                                magazine[pointer] == AKS && (magazine[pointer - 1] == Constants.PLUS_MINUS_PR ||
                                    magazine[pointer - 1] == Constants.OR_PR || magazine[pointer - 1] == Constants.AND_PR ||
                                    magazine[pointer - 1] == Constants.EQUAL_PR || magazine[pointer - 1] == Constants.MORE_EQUAL_LESS_PR ||
                                    magazine[pointer - 1] == Constants.MULT_SLASH_PERCENT_PR) &&
                                (magazine[pointer - 2] == AKS)) {
                            pointer -= 2;
                        } else if (pointer >= 7 && // AKS -> if ( AKS ) AKS else AKS
                                magazine[pointer] == AKS && magazine[pointer - 1] == Constants.ELSE_PR &&
                                magazine[pointer - 2] == AKS && magazine[pointer - 3] == Constants.RBC_PR &&
                                magazine[pointer - 4] == AKS && magazine[pointer - 5] == Constants.RBO_PR &&
                                magazine[pointer - 6] == Constants.IF_PR && magazine[pointer - 7] == AKS) {
                            pointer -= 7;
                        } else if (pointer >= 6 && // AKS -> if ( AKS ) AKS else AKS
                                magazine[pointer] == AKS && magazine[pointer - 1] == Constants.ELSE_PR &&
                                magazine[pointer - 2] == AKS && magazine[pointer - 3] == Constants.RBC_PR &&
                                magazine[pointer - 4] == AKS && magazine[pointer - 5] == Constants.RBO_PR &&
                                magazine[pointer - 6] == Constants.IF_PR) {
                            pointer -= 6;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 3 && // AKS -> AKS id . AKS
                                magazine[pointer] == AKS && magazine[pointer - 1] == Constants.DOT_PR &&
                                magazine[pointer - 2] == Constants.ID_PR && magazine[pointer - 3] == AKS) {
                            pointer -= 3;
                        } else if (pointer >= 2 && // AKS -> id . AKS
                                magazine[pointer] == AKS && magazine[pointer - 1] == Constants.DOT_PR &&
                                magazine[pointer - 2] == Constants.ID_PR) {
                            pointer -= 2;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 2 && // AKS -> . id AKS
                                magazine[pointer] == AKS && magazine[pointer - 1] == Constants.ID_PR &&
                                magazine[pointer - 2] == Constants.DOT_PR) {
                            pointer -= 2;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 3 && // AKS -> id AKS = AKS
                                magazine[pointer] == AKS && magazine[pointer - 1] == Constants.ASSIGN_PR &&
                                magazine[pointer - 2] == AKS && magazine[pointer - 3] == Constants.ID_PR) {
                            pointer -= 3;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 1 && magazine[pointer] == AKS && magazine[pointer - 1] == Constants.ID_PR) { // AKS -> id AKS
                            pointer -= 1;
                            magazine[pointer] = AKS;
                        } else if (pointer >= 2 && magazine[pointer] == AKS && magazine[pointer - 1] == Constants.ASSIGN_PR && magazine[pointer - 2] == AKS) { // AKS -> AKS = AKS
                            pointer -= 2;
                        } else if (pointer >= 1 && magazine[pointer] == AKS && magazine[pointer - 1] == Constants.ASSIGN_PR) { // AKS -> = AKS
                            pointer -= 1;
                            magazine[pointer] = AKS;
                        } else {
                            scaner.PrintError("Неверный символ".toCharArray(), lex);
                            resCode = 0;
                        }
                        break;
                }
                pointer++;

            } else {
                scaner.PrintError("Неверный символ".toCharArray(), lex);
                resCode = 0;
                break;
            }
        }

        return resCode;
    }
}
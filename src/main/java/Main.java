import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.RankDir;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Link.to;

public class Main {
    public static void main(String[] argv) throws IOException {


        Scaner scaner = new Scaner("/home/az/IdeaProjects/TYaPMVN/src/main/java/inputTest.txt");
        LL1 ll1 = new LL1();
        int resAnalise = ll1.LL1Analise(scaner);
//        PrecedenceAnalyz precedenceAnalyz = new PrecedenceAnalyz();
//        int resAnalise = precedenceAnalyz.analyz(scaner);
        Optimization optimization = new Optimization();
        ArrayList<Triad> triads = optimization.make(ll1.triads);
        int count = 0;
        FileWriter writer = null;
        try {
            writer = new FileWriter("/home/az/IdeaProjects/TYaPMVN/src/main/java/output.txt");

            for (Triad triad : ll1.triads) {
                writer.write(String.valueOf(count) + ") " + triad.proc + " " + triad.operand1 + " " + triad.operand2 + "\n");
                count++;
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer = new FileWriter("/home/az/IdeaProjects/TYaPMVN/src/main/java/outputOpt.txt");

            for (Triad triad : triads) {
                writer.write(String.valueOf(triad.index) + ") " + triad.proc + " " + triad.operand1 + " " + triad.operand2 + "\n");
                count++;
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



        if (resAnalise == 1) {
            System.out.println("Синтаксических ошибок не обнаружено");
        } else {
            System.out.println("Синтаксические ошибки обнаружены");
        }
//        Scaner scaner = new Scaner("/home/az/IdeaProjects/TYaP/src/inputTest.txt");
//        Diagram diagram = new Diagram(scaner);
//        diagram.PROG();
//        System.out.println(diagram.makeReport());
//
//        char[] lex = new char[Constants.MAX_LEX];
        //int typeLex = scaner.processScanner(lex);
        //if (typeLex == Constants.END)
        //    System.out.println("Синтаксических ошибок не обнаружено");
       // else
       //     scaner.PrintError("Лишний текст в конце программы!".toCharArray(), "".toCharArray());

//        do {
//            typeLex = scaner.processScanner(lex);
//            System.out.println(String.valueOf(lex) + " type: " + typeLex);
//        } while (typeLex != Constants.END);
    }
}

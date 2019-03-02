package fractal.code.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Created by sorin.nica in February 2017
 */
public class AppShell {


    public static void main(String[] args) throws IOException, InterruptedException {
        Random r = new Random(System.currentTimeMillis());

        boolean readCommands;
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        do {
            System.out.print("Ce vrei sa afli Doina ?    ");
            String command = consoleReader.readLine();

            for (int i = 0; i < 20; i++) {
                System.out.print(".");
                Thread.sleep(280);
            }

            switch (r.nextInt(3)) {
                case 0:
                    System.out.println("    cu siguranta asa va fi");
                    break;
                case 1:
                    System.out.println("    indecis");
                    break;
                case 2:
                    System.out.println("    nu se va adeverii");
                    break;
                default:
                    break;
            }

//            if (r.nextInt(2) == 0) System.out.println("clar nu");
//            else System.out.println("cum sa nu Doina, sigur ca da !!!");

            readCommands = !command.equals("exit");
        } while (readCommands);

        consoleReader.close();
    }

}

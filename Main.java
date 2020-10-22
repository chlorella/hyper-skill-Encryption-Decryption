package encryptdecrypt;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

interface Algorithms {
    String encrypt(String phrase, int key);

    String decrypt(String phrase, int key);
}

class ShiftAlgorithms implements Algorithms {

    @Override
    public String encrypt(String phrase, int key) {
        StringBuilder stringBuilder = new StringBuilder();

        for (char c : phrase.toCharArray()) {
            if (Character.isLetter(c)) {
                int swift = c + key;
                if ((c < 91 && swift > 91) || (c > 97 && swift > 122)) {
                    swift = swift - 26;
                }
                stringBuilder.append((char) swift);
            } else {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public String decrypt(String phrase, int key) {
        StringBuilder stringBuilder = new StringBuilder();

        for (char c : phrase.toCharArray()) {
            if (Character.isLetter(c)) {
                int swift = c - key;
                if ((c < 65 && swift < 65) || (c > 97 && swift < 97)) {
                    swift = swift + 26;
                }
                stringBuilder.append((char) swift);
            } else {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }
}

class UnicodeAlgorithms implements Algorithms {

    @Override
    public String encrypt(String phrase, int key) {
        StringBuilder stringBuilder = new StringBuilder();

        for (char c : phrase.toCharArray()) {
            int swift = c + key;
            stringBuilder.append((char) swift);
        }
        return stringBuilder.toString();
    }

    @Override
    public String decrypt(String phrase, int key) {
        StringBuilder stringBuilder = new StringBuilder();

        for (char c : phrase.toCharArray()) {
            int swift = c - key;
            stringBuilder.append((char) swift);
        }
        return stringBuilder.toString();
    }
}

class Context {
    private final Algorithms algorithms;

    public Context(String algo) {
        switch (algo) {
            case "unicode":
                algorithms = new UnicodeAlgorithms();
                break;
            case "shift":
            default:
                algorithms = new ShiftAlgorithms();
                break;
        }
    }

    public String encrypt(String phrase, int key) {
        return algorithms.encrypt(phrase, key);
    }

    public String decrypt(String phrase, int key) {
        return algorithms.decrypt(phrase, key);
    }
}

public class Main {
    public static void main(String[] args) {
        String result = "";

        int key = 0;
        String method = "enc";
        String data = "";
        boolean isStdOut = true;
        String infileName = "";
        String outfileName = "";
        String algo = "";

        for (int i = 0; i < args.length; i = i + 2) {
            switch (args[i]) {
                case "-mode":
                    method = args[i + 1];
                    break;
                case "-key":
                    key = Integer.parseInt(args[i + 1]);
                    break;
                case "-data":
                    data = args[i + 1];
                    System.out.println(data);
                    break;
                case "-out":
                    isStdOut = false;
                    outfileName = args[i + 1];
                    break;
                case "-in":
                    infileName = args[i + 1];
                    break;
                case "-alg":
                    algo = args[i + 1];
                    break;
            }
        }

        if (!"".equals(infileName) && "".equals(data)) {
            data = getDataFromFile(infileName);
        }

        Context context = new Context(algo);

        if ("enc".equals(method)) {
            result = context.encrypt(data, key);
        } else if ("dec".equals(method)) {
            result = context.decrypt(data, key);
        }

        if (isStdOut) {
            System.out.println(result);
        } else {
            outResultToFile(outfileName, result);
        }

    }

    private static void outResultToFile(String outfileName, String result) {
        FileWriter writer = null;
        try {
            File file = new File(outfileName);
            writer = new FileWriter(file);

            writer.write(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getDataFromFile(String infileName) {
        String result = "";
        Scanner scanner = null;
        try {
            File file = new File(infileName);
            scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                result = scanner.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                try {
                    scanner.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}

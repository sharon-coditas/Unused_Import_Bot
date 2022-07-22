package com.coditas.unusedImportsBot;

import com.google.googlejavaformat.java.FormatterException;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaSource;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.googlejavaformat.java.UnusedImportScanners.listUnusedImports;


public class Bot {
    public static void main(String... args) {
        JFrame frame = new JFrame("Unused Import's Scanner Bot V1.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Coditas\\IdeaProjects\\Unused_Import_Bot\\src\\com\\coditas\\unusedImportsBot\\bot.png");
        frame.setIconImage(icon);
        Toolkit tk=Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        JButton stringButton = new JButton("ADD FOLDER FOR STRING");
        JButton readButton = new JButton("ADD FOLDER FOR UNUSED IMPORTS");
        JTextArea mainArea = new JTextArea(5, 5);
        JTextArea resultArea = new JTextArea(5, 5);

        readButton.setPreferredSize(new Dimension(624, 372));
        stringButton.setPreferredSize(new Dimension(624, 372));
        mainArea.setPreferredSize(new Dimension(5, 70));
        mainArea.setBackground(Color.black);
        resultArea.setPreferredSize(new Dimension(8, 600));
        Font boldFont = new Font(resultArea.getFont().getName(), Font.BOLD, resultArea.getFont().getSize());
        resultArea.setFont(boldFont);

        readButton.setBounds(500, 10, 280, 20);
        stringButton.setBounds(500, 40, 280, 20);

        frame.getContentPane().add(stringButton);
        frame.getContentPane().add(readButton);
        frame.getContentPane().add(mainArea);
        frame.getContentPane().add(resultArea, BorderLayout.AFTER_LAST_LINE, Font.BOLD);


        readButton.addActionListener(ev -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int returnVal = fc.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    List<String> listFiles = findFiles(Paths.get(fc.getSelectedFile().getAbsolutePath()), "java");
                    List<String> detailsImport = new ArrayList<>();
                    detailsImport.add("UNWANTED IMPORTS \n");
                    for (String filesContent : listFiles) {
                        JavaProjectBuilder builder = new JavaProjectBuilder();
                        builder.addSource(new File(filesContent));
                        detailsImport.add("File path: " + filesContent + "\n");
                        Collection<JavaSource> sources = builder.getSources();
                        String print;
                        for (JavaSource sourceString : sources) {
                            print = printUnusedImports(String.valueOf(sourceString));
                            detailsImport.add("Unused imports are: " + "\n" + print + "\n");
                        }
                        Reader inputString = new StringReader(String.valueOf(detailsImport));
                        resultArea.read(inputString, "READING FILE :-)");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Operation is CANCELLED :(");
            }
        });

        stringButton.addActionListener(ev -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                try {
                    List<String> listFiles = findFiles(Paths.get(fileChooser.getSelectedFile().getAbsolutePath()), "java");
                    List<String> detailsString = new ArrayList<>();
                    detailsString.add("UNWANTED STRING \n");
                    for (String filesContent : listFiles) {
                        JavaProjectBuilder builder = new JavaProjectBuilder();
                        builder.addSource(new File(filesContent));
                        detailsString.add("File path: " + filesContent + "\n");
                        Collection<JavaSource> sources = builder.getSources();
                            List<String> list = List.of(String.valueOf(sources).split("\""));
                            Set<String> uniqueWords = new HashSet<>(list);
                            for (String word : uniqueWords) {
                                if (Collections.frequency(list, word) > 1)
                                {
                                    if (!word.startsWith(")"))
                                        detailsString.add("word that is repeated: \"" + word + "\" : " + "number of times repeated: " + Collections.frequency(list, word) + "\n");
                                }
                            }

                        detailsString.add("Trying adding Constants \n");
                        Reader inputString = new StringReader(String.valueOf(detailsString));
                        resultArea.read(inputString, "READING FILE :-)");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Operation is CANCELLED :(");
            }
        });

        
        
        frame.pack();
        frame.setSize(screenSize.width,screenSize.height);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    public static List<String> findFiles(Path path, String fileExtension)
            throws IOException {

        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path must be a directory!");
        }

        List<String> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk
                    .filter(p -> !Files.isDirectory(p))
                    .map(p -> p.toString().toLowerCase())
                    .filter(f -> f.endsWith(fileExtension))
                    .collect(Collectors.toList());
        }
        return result;
    }

    public static String printUnusedImports(String source) throws FormatterException {
        return listUnusedImports(String.valueOf(source));

    }

}
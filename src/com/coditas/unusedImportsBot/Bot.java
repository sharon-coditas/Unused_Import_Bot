package com.coditas.unusedImportsBot;

import com.google.googlejavaformat.java.FormatterException;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaSource;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import static com.google.googlejavaformat.java.UnusedImportScanners.listUnusedImports;


public class Bot {
    public static void main(String... args) {
        JFrame frame = new JFrame("Unused Import's Scanner Bot V1.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image icon=Toolkit.getDefaultToolkit().getImage("D:\\Unused_Bot\\UnusedImportDetector\\Dependency\\bot.png");
        frame.setIconImage(icon);
        frame.setSize(500, 500);
        frame.setLocation(200, 200);
        frame.setVisible(true);
        JTextArea tarea = new JTextArea(30, 40);
        JButton readButton = new JButton("ADD FOLDER");

        readButton.addActionListener(ev -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int returnVal = fc.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    List<String> listFiles = findFiles(Paths.get(fc.getSelectedFile().getAbsolutePath()), "java");
                    List<String> details = new ArrayList<>();
                    for (String filesContent : listFiles) {
                        JavaProjectBuilder builder = new JavaProjectBuilder();
                        builder.addSource(new File(filesContent));
                        details.add("File path: " + filesContent + "\n");
                        Collection<JavaSource> sources = builder.getSources();
                        String print = "";
                        for (JavaSource sourceString : sources) {
                            print = printUnusedImports(String.valueOf(sourceString));
                            details.add("Unused imports are: " + "\n" + print + "\n");
                        }
                        Reader inputString = new StringReader(String.valueOf(details));
                        tarea.read(inputString, "READING FILE :-)");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Operation is CANCELLED :(");
            }
        });

        frame.getContentPane().add(tarea, BorderLayout.CENTER);
        frame.getContentPane().add(readButton, BorderLayout.PAGE_START);
        frame.pack();
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
        return  listUnusedImports(String.valueOf(source));

    }
}
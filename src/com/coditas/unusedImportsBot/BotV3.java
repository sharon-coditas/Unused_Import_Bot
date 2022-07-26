package com.coditas.unusedImportsBot;

import com.google.googlejavaformat.java.FormatterException;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaSource;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.Constants.Constants.*;
import static com.google.googlejavaformat.java.UnusedImportScanners.listUnusedImports;

/**
 * @author Coditas
 */
public class BotV3 extends javax.swing.JFrame {
    static List<String> listFiles = new ArrayList<>();
    private static javax.swing.JTextArea jTextArea1;
    private static javax.swing.JTextArea jTextArea2;
    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    /**
     * Creates new form swing_gui
     */
    public BotV3() {
        initComponents();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BotV3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BotV3().setVisible(true);
            }
        });
    }

    // End of variables declaration
    public static void unusedImports() {
        try {
            List<String> detailsImport = new ArrayList<>();
            detailsImport.add(DETAILS_IMPORTS);
            for (String filesContent : listFiles) {
                JavaProjectBuilder builder = new JavaProjectBuilder();
                builder.addSource(new File(filesContent));
                detailsImport.add(FILE_PATH + filesContent + "\n");
                Collection<JavaSource> sources = builder.getSources();
                String print;
                for (JavaSource sourceString : sources) {
                    print = printUnusedImports(String.valueOf(sourceString));
                    if (!print.isEmpty()) {
                        detailsImport.add(DETAILS_IMPORTS + "\n" + print + "\n");
                    } else {
                        detailsImport.add(DETAILS_IMPORTS + null + "\n");
                    }
                }
                Reader inputString = new StringReader(String.valueOf(detailsImport));
                jTextArea1.read(inputString, DESCRIPTION);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void unwantedString() {

        try {
            List<String> detailsString = new ArrayList<>();
            detailsString.add(DETAILS_STRING);
            for (String filesContent : listFiles) {
                JavaProjectBuilder builder = new JavaProjectBuilder();
                builder.addSource(new File(filesContent));
                detailsString.add(FILE_PATH + filesContent + "\n");
                Collection<JavaSource> sources = builder.getSources();
                //Take the list of string with the file content
                List<String> list = List.of(String.valueOf(sources).split("\""));
                Set<String> words = new HashSet<>(list);
                //for loop on every word and duplicate words are separated
                for (String duplicateWords : words) {
                    if (Collections.frequency(list, duplicateWords) > 1) {
                        if (!duplicateWords.startsWith(")")) {
                            // Collection.frequency would print the number of times the words is there.
                            detailsString.add(WORDS_REPEATED + "\"" + duplicateWords + "\" : " + NUMBERS_OF_TIME_REPEATED + Collections.frequency(list, duplicateWords) + "\n");
                            detailsString.add(ADD_CONSTANTS);
                        }
                    }
                }
                if (!words.isEmpty()) {
                    detailsString.add(NO_UNWANTED_STRING);
                }
                Reader inputString = new StringReader(String.valueOf(detailsString));
                jTextArea1.read(inputString, DESCRIPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ifElse() {
        try {
            List<String> detailsIfElse = new ArrayList<>();

            for (String filesContent : listFiles) {
                JavaProjectBuilder builder = new JavaProjectBuilder();
                builder.addSource(new File(filesContent));
                File file = new File(filesContent);
                String[] ifStatement = null;
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String str;
                // Word to search
                String searchIf = "if";
                String searchElse = "else";
                // Initializes the counter of word to zero
                int count = 0;
                // Read the contents of the file
                while ((str = bufferedReader.readLine()) != null) {
                    // Split the word using space
                    ifStatement = str.split(" ");
                    for (String word : ifStatement) {
                        //Searching for the word
                        if (word.equals(searchIf)) {
                            // If present, increment the counter
                            count++;
                        }
                    }
                }
                if (count != 0) {
                    detailsIfElse.add(FILE_PATH + filesContent + "\n");
                    detailsIfElse.add(IF_PRESENT + "\"" + count + "\" : " + NUMBER_TIMES_PRESENT + "\n");
                    detailsIfElse.add(USE_SWITCH);
                } else {
                    detailsIfElse.add(FILE_PATH + filesContent + "\n");
                    detailsIfElse.add(IF_NOT_PRESENT);
                    detailsIfElse.add("\n");
                }
                fileReader.close();
            }
            Reader inputString = new StringReader(String.valueOf(detailsIfElse));
            jTextArea1.read(inputString, DESCRIPTION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void fileChooser() throws IOException {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = fc.showOpenDialog(new JFrame());
        List<String> filepath = new ArrayList<>();
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                listFiles = findFiles(Paths.get(fc.getSelectedFile().getAbsolutePath()), "java");
                filepath.add(fc.getSelectedFile().getAbsolutePath());
                Reader inputString = new StringReader(String.valueOf(filepath));
                jTextArea2.read(inputString, DESCRIPTION);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            filepath.add(null);
            Reader inputString = new StringReader(String.valueOf(filepath));
            jTextArea2.read(inputString, DESCRIPTION);
        }
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton4 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText(SET_TEXT_UNUSED_IMPORTS);
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText(SET_TEXT_IF_ELSE);
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jButton3.setText(SET_TEXT_UNWANTED_STRING);
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton4.setText(SET_TEXT_FOLDER_SCANNING);
        jButton4.addActionListener(evt -> {
            try {
                jButton4ActionPerformed(evt);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);
        Image icon = Toolkit.getDefaultToolkit().getImage("src/com/img/bot.png");
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        setTitle("Unused Import's Scanner Bot V1.0");
        setIconImage(icon);
        setSize(screenSize.width, screenSize.height);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(235, 235, 235)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jScrollPane2)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jButton1)
                                                                .addGap(117, 117, 117)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jButton4)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(81, 81, 81)
                                                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(137, 137, 137)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1054, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(187, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton2)
                                        .addComponent(jButton3)
                                        .addComponent(jButton1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 678, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        unusedImports();
    }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) { ifElse();}
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        unwantedString();
    }
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) throws IOException { fileChooser(); }
}

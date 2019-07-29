package NoteTaker;

//import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Scanner;

import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import static javax.swing.UIManager.setLookAndFeel;

public class NoteTaker extends JFrame {

    private final JTextArea jTextArea1;
    private final JTextArea jTextArea2;
    private final UndoManager undoManager;
    private final JButton noteButton;
    private final JButton open;
    private final JButton undo;
    private final JButton redo;
    private final JButton save;
    private final Box horizontalBox;
    private final UndoManager manager = new UndoManager();
    NoteTaker(){

        //initializing Variables
           jTextArea1 = new JTextArea("Welcome, " + System.getProperty("user.name"),10,15);
           jTextArea2 = new JTextArea(10,15);
          undoManager = new UndoManager();
           noteButton = new JButton("Take Note");
                 open = new JButton("open");
                 undo = new JButton(UndoManagerHelper.getUndoAction(undoManager));
                 redo = new JButton(UndoManagerHelper.getRedoAction(undoManager));
                 save = new JButton("Save");
        horizontalBox = Box.createHorizontalBox();
        Box verticalBox = Box.createVerticalBox();


        //setting properties of jTextArea1
        jTextArea1.setBackground(Color.CYAN);
        //jTextArea1.setLineWrap(true);
        //jTextArea1.setWrapStyleWord(true);
        jTextArea1.getDocument().addUndoableEditListener(undoManager);
        jTextArea1.setToolTipText("open document here");

        //setting properties of jTextArea2
        jTextArea2.setEditable(false);
        jTextArea2.setDisabledTextColor(Color.BLUE);
        jTextArea2.setSelectedTextColor(Color.RED);
        jTextArea2.setToolTipText("Notes will we displayed here");


        //Adding functionality for opening file form the file selector window.
        open.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jFileChooser.setFileFilter(new FileNameExtensionFilter("source file (.java or .cpp)","java","cpp"));
            jFileChooser.setFileFilter(new FileNameExtensionFilter("Text file(.txt)","txt"));
            jFileChooser.setFileFilter(new FileNameExtensionFilter("web-page(.html)","html"));
            jFileChooser.setFileFilter(new FileNameExtensionFilter("Display All supported Files","cpp","java","txt","html","css","js","php"));
            jFileChooser.setAcceptAllFileFilterUsed(false);

            if(jFileChooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
                try {
                    Scanner diskScanner = new Scanner(jFileChooser.getSelectedFile());
                    jTextArea1.setText("");

                    while (diskScanner.hasNext())
                        jTextArea1.append(diskScanner.nextLine()+"\n");

                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }

        });

        save.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileFilter(new FileNameExtensionFilter("source file (.java or .cpp)","java","cpp"));
            jFileChooser.setFileFilter(new FileNameExtensionFilter("Text file(.txt)","txt"));
            jFileChooser.setFileFilter(new FileNameExtensionFilter("web-page(.html)","html"));
            jFileChooser.setFileFilter(new FileNameExtensionFilter("Display all supported files","cpp","java","txt","html","css","js","php"));
            jFileChooser.setAcceptAllFileFilterUsed(false);
            int option = jFileChooser.showSaveDialog(this);

            if (option == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooser.getSelectedFile();
                if(!file.exists()){
                    try {
                        if(!file.createNewFile()){
                            JOptionPane.showMessageDialog(this,"Can't Create File","Error",JOptionPane.INFORMATION_MESSAGE);
                        }
                        else{
                            String extension = "";

                            if(jFileChooser.getFileFilter().getDescription().equals("Display all supported files")){
                                extension = ".txt";
                            }
                            else{
                                //TODO : add if file name is specified by the user then don't need to getDescription();
                                String temp = jFileChooser.getFileFilter().getDescription();
                                //FilenameUtils.getExtension return only extension without "." prefixed so concatenating "."
                                extension = temp.substring(temp.lastIndexOf("."),temp.length()-1);
                                System.out.println("USed Extension : " + extension);
                            }
                            //TODO: Fix 2 times file creation (oldFile and file)
                            File oldFile = file;
                            file = new File(file.getAbsolutePath() + extension);
                            if(oldFile.exists())
                                oldFile.delete();
                                JOptionPane.showMessageDialog(this,"File Created and Saved.","Info",JOptionPane.INFORMATION_MESSAGE);

                            System.out.println(extension + " <- HEre ");
                            System.out.println(file.getAbsolutePath()+"  < ----Path");
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

                    try {
                        jTextArea2.write(new BufferedWriter(new FileWriter(file,true)));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
            }
        });

        //appending selected text when Take Note Button gets clicked.
        noteButton.addActionListener(e -> {jTextArea2.append("->" + jTextArea1.getSelectedText() + "\n");});

        jTextArea2.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z,KeyEvent.CTRL_DOWN_MASK),"actionMapKeyUndo");
        jTextArea2.getActionMap().put("actionMapKeyUndo",UndoManagerHelper.getUndoAction(undoManager));

        jTextArea2.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y,KeyEvent.CTRL_DOWN_MASK),"actionMapKeyRedo");
        jTextArea2.getActionMap().put("actionMapKeyRedo",UndoManagerHelper.getRedoAction(undoManager));


        verticalBox.add(noteButton);
        verticalBox.add(open);
        verticalBox.add(undo);
        verticalBox.add(redo);
        verticalBox.add(save);
        horizontalBox.add(new JScrollPane(jTextArea1));
        horizontalBox.add(verticalBox);
        horizontalBox.add(new JScrollPane(jTextArea2));
        add(horizontalBox);
        add(new JLabel("Developed by Tanay Shah"),BorderLayout.PAGE_END);
        pack();
    }

    public static void main(String[] Args){

        try {
            setLookAndFeel(getSystemLookAndFeelClassName());
        } catch (Exception e) { }
        NoteTaker noteTaker = new NoteTaker();
        noteTaker.setTitle("Note Taker");
        noteTaker.setSize(600,400);
        noteTaker.setDefaultCloseOperation(EXIT_ON_CLOSE);
        noteTaker.setLocationRelativeTo(null);
        noteTaker.setVisible(true);
    }

}

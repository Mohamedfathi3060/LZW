import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileWriter;
import java.util.HashMap;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {

    public static void comp(File file) throws FileNotFoundException{
        String result = "";
        int T = 128 ;
        HashMap<String,Integer> MAP = new HashMap<>();
        try{
            Scanner read  = new Scanner(file);
            String data ="";
            while (read.hasNextLine()){
                data = data.concat(read.nextLine());
            }
            // ABCDEF   DEF
            // ABAABABBAABAABAAAABABBBBBBBBX
            int  i = 0 ;
            int  j = i ;
            String s = "";

            while( j  <= data.length() ){

                // PB : when j == data.len
                if(j < data.length()){
                    s = data.substring(i,j+1);
                    if(s.length()==1 || MAP.get(s) != null )j++;
                    if(j == data.length())s+="$";
                }
                if((s.length()!=1 && MAP.get(s) == null) || j == data.length() ){
                    if(s.length() == 2){
                        // one char
                        int x = (int) s.charAt(0);
                        MAP.put(s , T);
                        T++;
                        result += x +"\n";
                    }
                    else{
                        // more than one char
                        MAP.put(s , T);
                        T++;
                        System.out.println(i+"->"+j);
                        result +=  MAP.get(data.substring(i,j)).toString()  +"\n";
                    }
                    i=j;

                    if(data.length() == j)j++;


                    }

            }


            System.out.println(result);
        }
        catch (FileNotFoundException e){
            System.out.println("File Not Found");
        }





        // writing to File
//        try{
//            FileWriter f = new FileWriter("Compress_Result.txt",false);
//            f.write(result);
//            f.flush();
//        }
//        catch (IOException e){
//            System.out.println("File Not Found");
//        }
        try(DataOutputStream dos  = new DataOutputStream(new FileOutputStream("comp.lzw"))){
            String[] arr = result.split("\n");
            for (int i = 0; i < arr.length; i++) {
                dos.write(Integer.parseInt(arr[i]));
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void deComp(FileInputStream file) throws IOException {
        String data ="";
        DataInputStream r  = new DataInputStream(file);
        HashMap<Integer,String> MAP = new HashMap<>();
        int T = 127 ;
        String prev ="";
        String got ="";
        boolean error = false;
        int symbol = 0;
        while ((symbol = r.read()) != -1){
            prev = got ;
            System.out.println("symb =>" +symbol);
            if(symbol < 128){
                got ="";
                got += (char) symbol ;
                data += got;
            }
            else{
                got = MAP.get(symbol);
                if(got != null){
                    data += got;
                }
                else{
                    // error happened
                    error = true;
                    got ="?";
                }

            }
            System.out.println(got);
            MAP.put(T, prev + got.charAt(0));
            if(error){
                // catch error
                got = MAP.get(symbol);
                MAP.put(T, prev + got.charAt(0));
                got = MAP.get(symbol);
                data += got;
                error =false;
            }
            T++;
        }
        System.out.println("data->" +data);


        try{
            FileWriter f = new FileWriter("decomp.txt",false);
            f.write(data);
            f.flush();
        }
        catch (IOException e){
            System.out.println("File Not Found");
        }



    }

    public  static void main(String[] args) {





        SwingUtilities.invokeLater(Main::createAndShowGUI);


//        String answer = "exit",fileName = "test.txt";
//        System.out.println("☼ Welcome:) ☼");
//        System.out.println("☼ What you want to do ? ☼");
//        while (true) {
//            System.out.println("☼ 1-Compression");
//            System.out.println("☼ 2-Decompression");
//            System.out.println("☼ 0-exit");
//            System.out.print(">>>>>>>>>>  ");
//            Scanner read = new Scanner(System.in);
//            answer = read.nextLine();
//            if (answer.equalsIgnoreCase("1") || answer.equalsIgnoreCase("Compression")) {
//                System.out.print("☼ Please Enter File Name You Want to Compress: ");
//                fileName = read.nextLine();
//                File file = new File(fileName);
//                comp(file);
//            } else if (answer.equalsIgnoreCase("2") || answer.equalsIgnoreCase("Decompression")) {
//                System.out.print("☼ Please Enter File Name You Want to Decompress: ");
//                fileName = read.nextLine();
//                try(FileInputStream file = new FileInputStream(fileName)){
//                    deComp(file);
//                }
//                catch (IOException e){e.printStackTrace();}
//            } else {
//                System.out.println("☼ Exiting ☼ ");
//                break;
//            }
//        }
        //  comp or deComp ?
        // take file name
        // validate file name
        // pass to comp or deComp
        // output result
        // Repeat


    }


    private static void createAndShowGUI() {
        JFrame frame = new JFrame("File Compression App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(700, 500));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1, 10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel row1 = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea(10, 40);
        textArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        row1.add(textArea, BorderLayout.CENTER);

        JButton browseButton = new JButton("Browse");
        row1.add(browseButton, BorderLayout.EAST);

        JPanel row2 = new JPanel();
        row2.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton compressButton = new JButton("Compress");
        JButton decompressButton = new JButton("Decompress");

        row2.add(compressButton);
        row2.add(decompressButton);

        mainPanel.add(row1);
        mainPanel.add(row2);

        frame.add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser("M:\\java library\\LZW\\LZW_implementation");
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    textArea.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        compressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement compression logic here
                File file = new File(textArea.getText());

                try {
                    comp(file);
                    JOptionPane.showMessageDialog(frame, "Operation was successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(frame, "Operation failed.", "Failure", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }
        });

        decompressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement decompression logic here
                FileInputStream file = null;
                try {
                    file = new FileInputStream(textArea.getText());
                    deComp(file);
                    JOptionPane.showMessageDialog(frame, "Operation was successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Operation failed.", "Failure", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);

                }
            }
        });

    }
}
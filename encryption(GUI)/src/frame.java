
import java.awt.Color;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import javax.swing.*;

public class frame extends JFrame implements ActionListener {
    JLabel lab;
    JTextField text;
    JButton encryptButton;
    JButton decryptButton;
    JButton generateKeyButton;
    JButton saveKeyButton;
    JButton loadKeyButton;
    char[] letters;
    ArrayList<Character> list = new ArrayList<>();
    ArrayList<Character> shuffledList = new ArrayList<>();

    frame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(800, 800);
        this.getContentPane().setBackground(new Color(245, 245, 220));

        lab = new JLabel("Enter your text here: ");
        lab.setBounds(300, 50, 150, 50);

        text = new JTextField();
        text.setBounds(265, 100, 200, 30);

        encryptButton = new JButton("Encrypt");
        encryptButton.setBounds(100, 200, 100, 30);
        encryptButton.setFocusable(false);
        encryptButton.setFocusPainted(false);
        encryptButton.addActionListener(this);

        decryptButton = new JButton("Decrypt");
        decryptButton.setBounds(300, 200, 100, 30);
        decryptButton.setFocusable(false);
        decryptButton.setFocusPainted(false);
        decryptButton.addActionListener(this);

        generateKeyButton = new JButton("Generate");
        generateKeyButton.setBounds(500, 200, 100, 30);
        generateKeyButton.setFocusable(false);
        generateKeyButton.setFocusPainted(false);
        generateKeyButton.addActionListener(this);

        saveKeyButton = new JButton("Save");
        saveKeyButton.setBounds(200, 350, 100, 30);
        saveKeyButton.setFocusable(false);
        saveKeyButton.setFocusPainted(false);
        saveKeyButton.addActionListener(this);

        loadKeyButton = new JButton("Load");
        loadKeyButton.setBounds(400, 350, 100, 30);
        loadKeyButton.setFocusable(false);
        loadKeyButton.setFocusPainted(false);
        loadKeyButton.addActionListener(this);

        this.add(lab);
        this.add(text);
        this.add(encryptButton);
        this.add(decryptButton);
        this.add(generateKeyButton);
        this.add(saveKeyButton);
        this.add(loadKeyButton);
        this.setResizable(false);
        this.setVisible(true);
    }

    public void keyGen() {
        list.clear();
        shuffledList.clear();

        for (int i = 32; i < 127; i++) {
            list.add((char) i);
        }
        shuffledList.addAll(list);
        Collections.shuffle(shuffledList);

        JOptionPane.showMessageDialog(null, "Key is generated");
    }

    public void encrypt() {
        if (list.isEmpty() || shuffledList.isEmpty() || text.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Cannot encrypt. Key or input text is missing.");
            return;
        }
        letters = text.getText().toCharArray();
        for (int i = 0; i < letters.length; i++) {
            for (int j = 0; j < list.size(); j++) {
                if (letters[i] == list.get(j)) {
                    letters[i] = shuffledList.get(j);
                    break;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (char c : letters) {
            sb.append(c);
        }
        String encryptedText = sb.toString();
        JTextArea textArea = new JTextArea(encryptedText);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane, "Encrypted Message", JOptionPane.INFORMATION_MESSAGE);
    }

    public void decrypt() {
        if (list.isEmpty() || shuffledList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No key loaded or generated");
            return;
        }
        letters = text.getText().toCharArray();
        for (int i = 0; i < letters.length; i++) {
            for (int j = 0; j < shuffledList.size(); j++) {
                if (letters[i] == shuffledList.get(j)) {
                    letters[i] = list.get(j);
                    break;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (char c : letters) {
            sb.append(c);
        }
        String decryptedText = sb.toString();
        JOptionPane.showMessageDialog(null, "Decrypted: " + decryptedText);
    }

    public void saveKey() {
        if (shuffledList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Cannot save. No key generated.");
            return;
        }
        try (FileWriter writer = new FileWriter("Encryption_key.txt")) {
            for (char c : shuffledList) {
                writer.write(c);
            }
            JOptionPane.showMessageDialog(null, "Encryption key is saved");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occurred while saving key: " + e.getMessage());
        }
    }

    public void loadKey() {
        shuffledList.clear();
        try (Scanner sc = new Scanner(new FileReader("Encryption_key.txt"))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                for (char c : line.toCharArray()) {
                    shuffledList.add(c);
                }
            }
            JOptionPane.showMessageDialog(null, "Key loaded successfully");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error loading key: " + e.getMessage());
        }
        list.clear();
        for (int i = 32; i < 127; i++) {
            list.add((char) i);
        }
        System.out.println("Key loaded successfully.");

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generateKeyButton) {
            keyGen();
        } else if (e.getSource() == encryptButton) {
            encrypt();
        } else if (e.getSource() == decryptButton) {
            decrypt();
        } else if (e.getSource() == saveKeyButton) {
            saveKey();
        } else if (e.getSource() == loadKeyButton) {
            loadKey();
        }
    }
}
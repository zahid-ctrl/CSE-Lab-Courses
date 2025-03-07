package Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;

public class passwordChange extends JFrame implements ActionListener {

    private JLabel l2, l3, l4, l5, l6, l7;
    private JButton b1, b2, b3;
    private JPanel P2, P3;
    private JTextField t1;
    private JPasswordField pa1, pa2, pa3;
    private String line1, line2;
    private JCheckBox c1;

    public passwordChange() {
        super("Password Change");
        setSize(750, 510);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true); // Set undecorated

        P2 = new JPanel();
        P2.setBounds(0, 70, 750, 440);
        P2.setBackground(new Color(248,0,0));
        P2.setBorder(BorderFactory.createLineBorder(Color.black));
        P2.setLayout(null);

        P3 = new JPanel();
        P3.setBounds(0, 70, 750, 440);
        P3.setBackground(new Color(248,0,0));
        P3.setBorder(BorderFactory.createLineBorder(Color.black));
        P3.setLayout(null);

        b1 = new JButton("Back");
        b1.setFont(new Font("Serif", Font.BOLD, 20));
        b1.setForeground(new Color(204,0,0));
		b1.setBackground(Color.white);
        b1.setBounds(40, 420, 100, 35);
        b1.addActionListener(this);
        b1.setFocusable(false);
        b1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P2.add(b1);

        b2 = new JButton("Continue");
        b2.setFont(new Font("Serif", Font.BOLD, 20));
        b2.setForeground(new Color(204,0,0));
		b2.setBackground(Color.white);
        b2.setBounds(570, 420, 130, 35);
        b2.addActionListener(this);
        b2.setFocusable(false);
        b2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P2.add(b2);

        l2 = new JLabel("Please verify yourself : ");
        l2.setFont(new Font("Georgia", Font.ITALIC | Font.BOLD, 23));
        l2.setForeground(Color.white);
        l2.setBounds(65, 130, 480, 35);
        P2.add(l2);

        l3 = new JLabel("User name : ");
        l3.setFont(new Font("Serif", Font.PLAIN, 20));
        l3.setForeground(Color.white);
        l3.setBounds(155, 210, 200, 35);
        P2.add(l3);

        t1 = new JTextField();
        t1.setFont(new Font("Serif", Font.PLAIN, 18));
        t1.setBounds(260, 215, 270, 27);
        P2.add(t1);

        l4 = new JLabel("Password : ");
        l4.setFont(new Font("Serif", Font.PLAIN, 20));
        l4.setForeground(Color.white);
        l4.setBounds(155, 280, 200, 35);
        P2.add(l4);

        pa1 = new JPasswordField();
        pa1.setEchoChar('*');
        pa1.setFont(new Font("Serif", Font.PLAIN, 19));
        pa1.setBounds(260, 285, 270, 27);
        P2.add(pa1);

        l5 = new JLabel("Enter your new password : ");
        l5.setFont(new Font("Georgia", Font.ITALIC | Font.BOLD, 23));
        l5.setForeground(Color.white);
        l5.setBounds(65, 130, 480, 35);
        P3.add(l5);

        l6 = new JLabel("New password  ");
        l6.setFont(new Font("Georgia", Font.PLAIN, 20));
        l6.setForeground(Color.white);
        l6.setBounds(125, 210, 200, 35);
        P3.add(l6);

        pa2 = new JPasswordField();
        pa2.setEchoChar('*');
        pa2.setFont(new Font("Georgia", Font.PLAIN, 20));
        pa2.setBounds(260, 215, 270, 27);
        P3.add(pa2);

        l7 = new JLabel("Retype password  ");
        l7.setFont(new Font("Georgia", Font.PLAIN, 20));
        l7.setForeground(Color.white);
        l7.setBounds(105, 280, 200, 35);
        P3.add(l7);

        pa3 = new JPasswordField();
        pa3.setEchoChar('*');
        pa3.setFont(new Font("Serif", Font.PLAIN, 20));
        pa3.setBounds(260, 285, 270, 27);
        P3.add(pa3);

        b3 = new JButton("Confirm");
        b3.setFont(new Font("Serif", Font.BOLD, 20));
        b3.setForeground(new Color(204,0,0));
		b3.setBackground(Color.white);
        b3.setBounds(570, 420, 130, 35);
        b3.addActionListener(this);
        b3.setFocusable(false);
        b3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P3.add(b3);

        c1 = new JCheckBox("Show password");
        c1.setFont(new Font("Serif", Font.PLAIN, 16));
        c1.setForeground(Color.white);
        c1.setBackground(new Color(248, 0, 0));
        c1.setBounds(260, 320, 270, 38);
        c1.addActionListener(this);
        c1.setFocusable(false);
        c1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P2.add(c1);

        add(P2);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == b1) {
            userinfo u = new userinfo();
            this.setVisible(false);
            u.setVisible(true);
        } else if (ae.getSource() == b2) {
            if (t1.getText().length() <= 0 || pa1.getPassword().length <= 0) {
                JOptionPane.showMessageDialog(this, "Fill all information");
            } else {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader("data\\login data.txt"));
                    line1 = Files.readAllLines(Paths.get("data\\login data.txt")).get(0);
                    line2 = Files.readAllLines(Paths.get("data\\login data.txt")).get(1);
                    reader.close();
                    String userNameS = "Name        :  " + t1.getText();
                    String passwordS = "Password  :  " + String.valueOf(pa1.getPassword());
                    if (line1.equals(userNameS) && line2.equals(passwordS)) {
                        this.add(P3);
                        P3.add(c1);
                        P2.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(this, "Wrong User name or password");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (ae.getSource() == b3) {
            if (pa2.getPassword().length <= 0 || pa3.getPassword().length <= 0) {
                JOptionPane.showMessageDialog(this, "Fill all information");
            } else if (!String.valueOf(pa2.getPassword()).equals(String.valueOf(pa3.getPassword()))) {
                JOptionPane.showMessageDialog(this, "Password doesn't match");
            } else {
                try {
                    BufferedReader readers = new BufferedReader(new FileReader("data\\login data.txt"));
                    line1 = Files.readAllLines(Paths.get("data\\login data.txt")).get(0);
                    line2 = Files.readAllLines(Paths.get("data\\login data.txt")).get(1);
                    readers.close();
                    BufferedReader reader = new BufferedReader(new FileReader("data\\data.txt"));
                    int totalLines = 0;
                    while (reader.readLine() != null) {
                        totalLines++;
                    }
                    reader.close();
                    for (int i = 0; i <= totalLines; i++) {
                        String data1 = Files.readAllLines(Paths.get("data\\data.txt")).get(i);
                        BufferedWriter writer = new BufferedWriter(new FileWriter("data\\new data.txt", true));
                        writer.write(data1 + "\n");
                        if (data1.equals(line1)) {
                            String data2 = Files.readAllLines(Paths.get("data\\data.txt")).get(i + 1);
                            if (data2.equals(line2)) {
                                writer.write("Password  :  " + String.valueOf(pa3.getPassword()) + "\n");
                                i = i + 1;
                            }
                        }
                        writer.close(); // Close the writer after writing is done for each iteration
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "Password changed successfully!");
                JOptionPane.showMessageDialog(null, "Please log in again");
                Login l = new Login();
                this.setVisible(false);
                l.setVisible(true);
                File file1 = new File("data\\data.txt");
                file1.delete();
                File file2 = new File("data\\new data.txt");
                File file3 = new File("data\\data.txt");
                file2.renameTo(file3);
            }
        } else if (ae.getSource() == c1) {
            if (c1.isSelected()) {
                pa1.setEchoChar((char) 0);
                pa2.setEchoChar((char) 0);
                pa3.setEchoChar((char) 0);
            } else {
                pa1.setEchoChar('*');
                pa2.setEchoChar('*');
                pa3.setEchoChar('*');
            }
        }
    }
}

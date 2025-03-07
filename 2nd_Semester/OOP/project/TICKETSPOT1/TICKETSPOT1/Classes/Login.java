package Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Login extends JFrame implements ActionListener {

    // Components for the custom title bar
    private JPanel titleBar;
    private JLabel titleLabel;
    private JButton closeButton;
    private JButton minimizeButton;

    // Other components for the login interface
    private JLabel l1, l2, l3, l4, l5;
    private JTextField t1;
    private JButton b1, b2, b3;
    private JPasswordField pa1;
    private JPanel P1, P2;
    private JCheckBox c1;
    // Class-level variables for mouse position
    private int mouseX, mouseY;

    public Login() {
        super("Log in");
        setSize(889, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Set undecorated

        createTitleBar(); // Create the custom title bar
        createLoginInterface(); // Create the login interface

        setVisible(true);
    }

    // Method to create the custom title bar
    private void createTitleBar() {
        titleBar = new JPanel();
        titleBar.setBounds(0, 0, getWidth(), 30);
        titleBar.setLayout(null);
        titleBar.setBackground(new Color(51, 51, 51));

        // Make the title bar draggable
        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                setLocation(x - mouseX, y - mouseY);
            }
        });

        titleLabel = new JLabel("Login");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setBounds(10, 0, 200, 30);

        closeButton = new JButton("X");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(51, 51, 51));
        closeButton.setBorder(null);
        closeButton.setFocusPainted(false);
        closeButton.setBounds(getWidth() - 45, 0, 45, 30);
        closeButton.addActionListener(this);
        closeButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                closeButton.setBackground(Color.RED);
            }

            public void mouseExited(MouseEvent e) {
                closeButton.setBackground(new Color(51, 51, 51));
            }
        });

        minimizeButton = new JButton("-");
        minimizeButton.setForeground(Color.WHITE);
        minimizeButton.setBackground(new Color(51, 51, 51));
        minimizeButton.setBorder(null);
        minimizeButton.setFocusPainted(false);
        minimizeButton.setBounds(getWidth() - 90, 0, 45, 30);
        minimizeButton.addActionListener(this);
        minimizeButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                minimizeButton.setBackground(Color.GRAY);
            }

            public void mouseExited(MouseEvent e) {
                minimizeButton.setBackground(new Color(51, 51, 51));
            }
        });

        titleBar.add(titleLabel);
        titleBar.add(closeButton);
        titleBar.add(minimizeButton);

        add(titleBar);
    }


    // Method to create the login interface
    private void createLoginInterface() {
        P1 = new JPanel();
        P1.setBounds(0, 30, 889, 500);
        P1.setLayout(null);

        // Labels
        l1 = new JLabel("Don't have an account?");
        l1.setFont(new Font("Serif", Font.PLAIN, 14));
        l1.setForeground(Color.black);
        l1.setBounds(545, 385, 150, 15);
        P1.add(l1);

        l2 = new JLabel("Account log in");
        l2.setFont(new Font("Serif", Font.PLAIN, 24));
        l2.setForeground(new Color(248, 0, 0));
        l2.setBounds(595, 80, 500, 28);
        P1.add(l2);

        // User name
        l3 = new JLabel("User name ");
        l3.setFont(new Font("Serif", Font.PLAIN, 17));
        l3.setForeground(Color.black);
        l3.setBounds(525, 140, 250, 20);
        P1.add(l3);

        t1 = new JTextField();
        t1.setFont(new Font("Serif", Font.PLAIN, 19));
        t1.setBounds(525, 170, 270, 27);
        P1.add(t1);

        // Password
        l4 = new JLabel("Password ");
        l4.setFont(new Font("Serif", Font.PLAIN, 18));
        l4.setForeground(Color.black);
        l4.setBounds(525, 215, 250, 20);
        P1.add(l4);

        pa1 = new JPasswordField();
        pa1.setEchoChar('*');
        pa1.setFont(new Font("Serif", Font.PLAIN, 19));
        pa1.setBounds(525, 245, 270, 27);
        P1.add(pa1);

        // Button
        b1 = new JButton("Register");
        b1.setFont(new Font("Serif", Font.BOLD, 14));
        b1.setForeground(new Color(248, 0, 0));
        b1.setBackground(new Color(255, 255, 255));
        b1.setBorder(null);
        b1.setBounds(680, 381, 100, 20);
        b1.addActionListener(this);
        b1.setFocusable(false);
        b1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P1.add(b1);
		
		b3 = new JButton("Back");
        b3.setFont(new Font("Serif", Font.BOLD, 14));
        b3.setForeground(new Color(240, 0, 0));
        b3.setBackground(new Color(255, 255, 255));
        b3.setBorder(null);
        b3.setBounds(620, 430, 100, 25);
        b3.addActionListener(this);
        b3.setFocusable(false);
        b3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P1.add(b3);
		

        b2 = new JButton("Login");
        b2.setFont(new Font("Serif", Font.BOLD, 22));
        b2.setForeground(Color.white);
        b2.setBackground(new Color(248, 0, 0));
        b2.setBounds(525, 340, 270, 38);
        b2.addActionListener(this);
        b2.setFocusable(false);
        b2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P1.add(b2);
		b2.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add white border to the button

        c1 = new JCheckBox("Show password");
        c1.setFont(new Font("Serif", Font.PLAIN, 16));
        c1.setForeground(Color.black);
        c1.setBackground(new Color(255, 255, 255));
        c1.setBounds(525, 280, 270, 38);
        c1.addActionListener(this);
        c1.setFocusable(false);
        c1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P1.add(c1);

        //image
        l5 = new JLabel(new ImageIcon(getClass().getResource("/image/Ticketspot(3).jpg")));
        l5.setBounds(0, 0, 889, 500);
        P1.add(l5);

        add(P1);
    }


    public void actionPerformed(ActionEvent ae) {

        //register button
        if (ae.getSource() == b1) {
            Registration r = new Registration();
            this.setVisible(false);
            r.setVisible(true);
        }
		
		else if (ae.getSource() == b3) {
			new Homepage(); // Open the frame
        dispose(); // Close the login frame
		}

        //login button
        else if (ae.getSource() == b2) {
            if (t1.getText().isEmpty() || String.valueOf(pa1.getPassword()).isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all information");
            } else {
                try {
                    String userNameS = "Name        :  " + t1.getText();
                    String passwordS = "Password  :  " + String.valueOf(pa1.getPassword());

                    BufferedReader reader = new BufferedReader(new FileReader("data\\data.txt"));
                    int totalLines = 0;
                    while (reader.readLine() != null)
                        totalLines++;
                    reader.close();

                    for (int i = 0; i <= totalLines; i++) {
                        String line = Files.readAllLines(Paths.get("data\\data.txt")).get(i);

                        if (line.equals(userNameS)) {
                            String line2 = Files.readAllLines(Paths.get("data\\data.txt")).get((i + 1));

                            if (line2.equals(passwordS)) {
                                movies m = new movies();
                                this.setVisible(false);
                                m.setVisible(true);

                                String line3 = Files.readAllLines(Paths.get("data\\data.txt")).get((i + 2));
                                String line4 = Files.readAllLines(Paths.get("data\\data.txt")).get((i + 3));
                                String line5 = Files.readAllLines(Paths.get("data\\data.txt")).get((i + 4));

                                BufferedWriter writer = new BufferedWriter(new FileWriter("data\\login data.txt"));
                                writer.write("" + line);
                                writer.write("\n" + line2);
                                writer.write("\n" + line3);
                                writer.write("\n" + line4);
                                writer.write("\n" + line5);
                                writer.close();

                                break;
                            }
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Wrong User Name or Password!");
                }
            }
        }

        //show password checkbox
        else if (ae.getSource() == c1) {
            if (c1.isSelected()) {
                pa1.setEchoChar((char) 0);
            } else {
                pa1.setEchoChar('*');
            }
        }

        // Close button
        else if (ae.getSource() == closeButton) {
            dispose();
        }

        // Minimize button
        else if (ae.getSource() == minimizeButton) {
            setState(JFrame.ICONIFIED);
        }
    }
}

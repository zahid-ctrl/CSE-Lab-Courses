package Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Registration extends JFrame implements ActionListener {

    JPanel titleBar;
    JLabel titleLabel;
    JButton closeButton;
    JButton minimizeButton;

    JLabel l1, l2, l3, l4, l5, l6, l7, l8, l9;
    JTextField t1, t2, t3;
    JButton b1, b2;
    JPasswordField pa1, pa2;
    JComboBox<String> c1; // Specify the type parameter for JComboBox
    JCheckBox ch1;
    JPanel P1, P2;

    private int mouseX, mouseY;

    public Registration() {
        super("Registration");
        setSize(978, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Set undecorated

        createTitleBar(); // Create the custom title bar
        createRegistrationInterface(); // Create the registration interface

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

        titleLabel = new JLabel("Registration");
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

    // Method to create the registration interface
    private void createRegistrationInterface() {
        P1 = new JPanel();
        P1.setBounds(0, 30, 978, 550);
        P1.setLayout(null);

        // Labels
        l1 = new JLabel("Already have an account?");
        l1.setFont(new Font("Serif", Font.PLAIN, 14));
        l1.setForeground(Color.black);
        l1.setBounds(610, 480, 150, 15);
        P1.add(l1);

        l2 = new JLabel("Create your free account");
        l2.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 22));
        l2.setForeground(new Color(248,0,0));
        l2.setBounds(610, 55, 500, 28);
        P1.add(l2);

        // Full name
        l3 = new JLabel("Full Name :");
        l3.setFont(new Font("Serif", Font.PLAIN, 16));
        l3.setForeground(Color.black);
        l3.setBounds(520, 110, 200, 20);
        P1.add(l3);

        t1 = new JTextField();
        t1.setFont(new Font("Serif", Font.PLAIN, 17));
        t1.setBounds(650, 110, 235, 25);
        t1.addActionListener(this);
        P1.add(t1);

        // Mobile number
        l4 = new JLabel("Mobile Number :");
        l4.setFont(new Font("Serif", Font.PLAIN, 16));
        l4.setForeground(Color.black);
        l4.setBounds(520, 160, 250, 25);
        P1.add(l4);

        t2 = new JTextField();
        t2.setFont(new Font("Serif", Font.PLAIN, 16));
        t2.setBounds(650, 160, 235, 25);
        t2.addActionListener(this);
        P1.add(t2);

        // Email
        l5 = new JLabel("Email :");
        l5.setFont(new Font("Serif", Font.PLAIN, 16));
        l5.setForeground(Color.black);
        l5.setBounds(520, 210, 250, 25);
        P1.add(l5);

        t3 = new JTextField();
        t3.setFont(new Font("Serif", Font.PLAIN, 16));
        t3.setBounds(650, 210, 235, 25);
        t3.addActionListener(this);
        P1.add(t3);

        // Gender
        l6 = new JLabel("Gender :");
        l6.setFont(new Font("Serif", Font.PLAIN, 16));
        l6.setForeground(Color.black);
        l6.setBounds(520, 260, 250, 22);
        P1.add(l6);

        String[] gender = { "Male", "Female" };
        c1 = new JComboBox<>(gender);
        c1.setFont(new Font("Serif", Font.PLAIN, 16));
        c1.setBounds(650, 260, 235, 25);
        P1.add(c1);

        // Password
        l7 = new JLabel("Password : ");
        l7.setFont(new Font("Serif", Font.PLAIN, 16));
        l7.setForeground(Color.black);
        l7.setBounds(520, 310, 250, 25);
        P1.add(l7);

        pa1 = new JPasswordField();
        pa1.setFont(new Font("Serif", Font.PLAIN, 16));
        pa1.setBounds(650, 310, 235, 25);
        pa1.setEchoChar('*');
        pa1.addActionListener(this);
        P1.add(pa1);

        // Retype password
        l8 = new JLabel("Retype Password : ");
        l8.setFont(new Font("Serif", Font.PLAIN, 16));
        l8.setForeground(Color.black);
        l8.setBounds(520, 360, 250, 25);
        P1.add(l8);

        pa2 = new JPasswordField();
        pa2.setFont(new Font("Serif", Font.PLAIN, 16));
        pa2.setBounds(650, 360, 235, 25);
        pa2.setEchoChar('*');
        pa2.addActionListener(this);
        P1.add(pa2);

        // Buttons
        b1 = new JButton("Login");
        b1.setFont(new Font("Serif", Font.BOLD, 14));
        b1.setForeground(Color.red);
        b1.setBackground(new Color(255, 255, 255));
        b1.setBorder(null);
        b1.setBounds(770, 475, 38, 20);
        b1.addActionListener(this);
        b1.setFocusable(false);
        b1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P1.add(b1);

        b2 = new JButton("Create Account");
        b2.setFont(new Font("Serif", Font.PLAIN, 25));
        b2.setForeground(Color.white);
        b2.setBackground(new Color(248,0,0));
        b2.setBounds(580, 430, 270, 38);
        b2.addActionListener(this);
        b2.setFocusable(false);
        b2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P1.add(b2);
		b2.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add white border to the button

        ch1 = new JCheckBox("Show password");
        ch1.setFont(new Font("Serif", Font.PLAIN, 14));
        ch1.setForeground(Color.black);
        ch1.setBackground(new Color(255, 255, 255));
        ch1.setBounds(645, 390, 270, 20);
        ch1.addActionListener(this);
        ch1.setFocusable(false);
        ch1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P1.add(ch1);

        // Image
        l9 = new JLabel(new ImageIcon(getClass().getResource("/image/Ticketspot (2).png")));
        l9.setBounds(0, 0, 978, 550);
        P1.add(l9);

        add(P1);
    }

    public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == b1) {
        Login l = new Login();
        this.setVisible(false);
        l.setVisible(true);
    } else if (ae.getSource() == b2) {
        if (t1.getText().isEmpty() || t2.getText().isEmpty() || t3.getText().isEmpty() || pa1.getPassword().length == 0 || pa2.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Fill all information");
        } else if (!Arrays.equals(pa1.getPassword(), pa2.getPassword())) {
            JOptionPane.showMessageDialog(this, "Password doesn't match");
        } else {
            JOptionPane.showMessageDialog(this, "Account created successfully!");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("data\\data.txt", true))) {
                writer.write("Name        :  " + t1.getText());
                writer.newLine();
                writer.write("Password  :  " + new String(pa1.getPassword()));
                writer.newLine();
                writer.write("Mobile       :  " + t2.getText());
                writer.newLine();
                writer.write("Email         :  " + t3.getText());
                writer.newLine();
                writer.write("Gender      :  " + c1.getSelectedItem() + "\n\n=================\n");
            } catch (IOException e) {
                System.out.println("Exception handled");
            }

            // Clearing the text fields
            t1.setText(null);
            t2.setText(null);
            t3.setText(null);
            pa1.setText(null);
            pa2.setText(null);
        }
    }// Close button
        else if (ae.getSource() == closeButton) {
            dispose();
        }

        // Minimize button
        else if (ae.getSource() == minimizeButton) {
            setState(JFrame.ICONIFIED);
        }
	else if (ch1.isSelected()) {
        pa1.setEchoChar((char) 0);
        pa2.setEchoChar((char) 0);
    } else {
        pa1.setEchoChar('*');
        pa2.setEchoChar('*');
    }
	
}
}

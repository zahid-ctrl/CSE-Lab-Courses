package Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class payment extends JFrame implements ActionListener {
    
    JLabel l1, l2, l3, l4, l5, l6, l7, l8, l9, l10;
    JButton b1, b2;
    JRadioButton b3, b4, b5;
    JPanel P1, P2,P3;
    JTextField t1;
    JPasswordField p1;
    JCheckBox c1;
    private JPanel titleBar;
    private JLabel titleLabel;
    private JButton minimizeButton, closeButton;
    private int mouseX, mouseY;

    public payment() {
        super("payment");
        this.setSize(750, 510);
        setLocationRelativeTo(null);
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Set undecorated
        
        // Initialize custom title bar
        createTitleBar();
        P2 = new JPanel();
        P2.setBounds(0, 0, 750, 460);
        P2.setBackground(new Color(255,255,255));
        P2.setBorder(BorderFactory.createLineBorder(Color.white));
        P2.setLayout(null);
		
		P3 = new JPanel();
        P3.setBounds(0, 250, 750, 260);
        P3.setBackground(new Color(204,0,0));
        P3.setBorder(BorderFactory.createLineBorder(Color.white));
        P3.setLayout(null);
		

        // Panel 2 components
        b1 = new JButton("Back");
        b1.setFont(new Font("Serif", Font.PLAIN, 22));
		b1.setForeground(new Color(204,0,0));
		b1.setBackground(Color.white);        
		b1.setBounds(40, 410, 90, 35);
        b1.addActionListener(this);
        b1.setFocusable(false);
        b1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P2.add(b1);

        b2 = new JButton("Confirm");
        b2.setFont(new Font("Serif", Font.PLAIN, 24));
        b2.setForeground(new Color(204,0,0));
		b2.setBackground(Color.white);
        b2.setBounds(585, 410, 120, 40);
        b2.addActionListener(this);
        b2.setFocusable(false);
        b2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P2.add(b2);

        l3 = new JLabel("Complete your payment");
        l3.setFont(new Font("Serif", Font.BOLD, 32));
        l3.setOpaque(true);
        l3.setBackground(new Color(204,0,0));
		l3.setForeground(Color.white);
        l3.setBounds(0, 30, 750, 40);
		l3.setHorizontalAlignment(SwingConstants.CENTER);
        P2.add(l3);
		
		l10 = new JLabel();
        l10.setOpaque(true);
        l10.setBackground(new Color(204,0,0));
        l10.setBounds(0, 130, 750, 70);
		l10.setLayout(null);
        P2.add(l10);
		

        l4 = new JLabel("We accept : ");
        l4.setFont(new Font("Serif", Font.BOLD, 20));
        l4.setOpaque(true);
		l4.setBackground(new Color(204,0,0));
		l4.setForeground(Color.white);
        l4.setBounds(50, 20, 110, 30);
        l10.add(l4);
		
		

        // Radio buttons
        b3 = new JRadioButton();
        b3.setBounds(175, 25, 20, 20);
        b3.setBackground(new Color(204,0,0));
        b3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b3.setFocusable(false);
        l10.add(b3);

        l5 = new JLabel(new ImageIcon(getClass().getResource("/image/bkash.jpg")));
        l5.setOpaque(true);
        l5.setBackground(new Color(218, 227, 243)); // Changed background color
        l5.setBounds(200, 0, 110, 70);
        l10.add(l5);

        b4 = new JRadioButton();
        b4.setBounds(340, 25, 20, 20);
        b4.setBackground(new Color(204,0,0));
        b4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b4.setFocusable(false);
        l10.add(b4);

        l6 = new JLabel(new ImageIcon(getClass().getResource("/image/rocket.png")));
        l6.setOpaque(true);
        l6.setBackground(new Color(218, 227, 243)); // Changed background color
        l6.setBounds(365, 0, 110, 70);
        l10.add(l6);

        b5 = new JRadioButton();
        b5.setBounds(500, 25, 20, 20);
        b5.setBackground(new Color(204,0,0));
        b5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b5.setFocusable(false);
        l10.add(b5);

        l7 = new JLabel(new ImageIcon(getClass().getResource("/image/nogod.jpg")));
        l7.setOpaque(true);
        l7.setBackground(new Color(218, 227, 243)); // Changed background color
        l7.setBounds(525, 0, 110, 70);
        l10.add(l7);

        ButtonGroup group = new ButtonGroup();
        group.add(b3);
        group.add(b4);
        group.add(b5);

        l8 = new JLabel("Mobile number : ");
        l8.setFont(new Font("Serif", Font.BOLD, 21));
        l8.setOpaque(true);
        l8.setBackground(new Color(204, 0, 0));
		l8.setForeground(new Color(255, 255, 255));
        l8.setBounds(200, 10, 160, 30);
        P3.add(l8);

        t1 = new JTextField();
        t1.setFont(new Font("Serif", Font.PLAIN, 21));
        t1.setBounds(360, 10, 220, 28);
        P3.add(t1);

        l9 = new JLabel("Password : ");
        l9.setFont(new Font("Serif", Font.BOLD, 21));
        l9.setOpaque(true);
        l9.setBackground(new Color(204, 0, 0));
		l9.setForeground(new Color(255, 255, 255));
        l9.setBounds(200, 50, 160, 30);
        P3.add(l9);

        p1 = new JPasswordField();
        p1.setFont(new Font("Serif", Font.PLAIN, 17));
        p1.setEchoChar('*');
        p1.setBounds(360, 50, 220, 28);
        P3.add(p1);

        c1 = new JCheckBox("Show password");
        c1.setFont(new Font("Serif", Font.PLAIN, 16));
        c1.setForeground(Color.black);
        c1.setBackground(new Color(204, 0, 0));
		c1.setForeground(new Color(255, 255, 255));
        c1.setBounds(355, 90, 270, 38);
        c1.addActionListener(this);
        c1.setFocusable(false);
        c1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P3.add(c1);

        P2.add(P3);
        this.add(P2);
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

        titleLabel = new JLabel("Payment");
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

    public void actionPerformed(ActionEvent ae) {
        // For back button
        if (ae.getSource() == b1) {
            buyTicket u = new buyTicket();
            this.setVisible(false);
            u.setVisible(true);
        } else if (ae.getSource() == b2) {
            if (!(b3.isSelected() || b4.isSelected() || b5.isSelected())) {
                JOptionPane.showMessageDialog(this, "Please select a payment method");
            } else if (t1.getText().length() <= 0 || p1.getPassword().length <= 0) {
                JOptionPane.showMessageDialog(this, "Fill all information");
            } else {
                JOptionPane.showMessageDialog(this, "payment COMPLETE !!");
                JOptionPane.showMessageDialog(this,
                        "You can collect your ticket from counter by your mobile number");
                movies s = new movies();
                this.setVisible(false);
                s.setVisible(true);
            }
        } else if (ae.getSource() == c1) {
            p1.setEchoChar(c1.isSelected() ? '\0' : '*');
        } else if (ae.getSource() == closeButton) {
            System.exit(0);
        } else if (ae.getSource() == minimizeButton) {
            this.setState(Frame.ICONIFIED);
        }
    }
}

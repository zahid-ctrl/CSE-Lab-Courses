package Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Homepage extends JFrame implements ActionListener {

    JLabel l1;
    JButton b1, b2, b3;
    JPanel P1;
    int mouseX, mouseY;

    public Homepage() {
        super();
        // Hide the title bar
        setUndecorated(true);

        this.setSize(889, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // panel
        P1 = new JPanel();
        P1.setBounds(0, 0, 889, 500);
        P1.setLayout(null);
        P1.setBackground(Color.white); // Change the background color of the panel

        // button
        b1 = new JButton("Exit");
        b1.setFont(new Font("Serif", Font.BOLD, 23));
        b1.setForeground(Color.white);
        b1.setBackground(new Color(196, 12, 12));
        b1.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // Add white border to the button
        b1.setBounds(150, 390, 88, 36);
        b1.addActionListener(this);
        b1.setFocusable(false);
        b1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P1.add(b1);

        b2 = new JButton("Get started");
        b2.setFont(new Font("Serif", Font.BOLD, 23));
        b2.setBackground(Color.white);
        b2.setForeground(new Color(248, 0, 0));
        b2.setBorder(BorderFactory.createLineBorder(new Color(248, 0, 0))); // Add white border to the button
        b2.setBounds(500, 390, 220, 40);
        b2.addActionListener(this);
        b2.setFocusable(false);
        b2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P1.add(b2);
		
		b3 = new JButton("Admin Login");
        b3.setFont(new Font("Serif", Font.BOLD, 13));
        b3.setBackground(Color.white);
        b3.setForeground(new Color(204, 0, 0));
        b3.setBorder(BorderFactory.createLineBorder(Color.RED)); // Add white border to the button
        b3.setBounds(770, 20, 80, 36);
        b3.addActionListener(this);
        b3.setFocusable(false);
        b3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P1.add(b3);

        // image
        l1 = new JLabel(new ImageIcon(getClass().getResource("/image/home(1).jpg")));
        l1.setBounds(0, 0, 889, 500);
        P1.add(l1);

        // Add a white border around the panel
        P1.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        // Add mouse listener to make the frame draggable
        P1.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });

        P1.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                int newX = e.getXOnScreen() - mouseX;
                int newY = e.getYOnScreen() - mouseY;
                setLocation(newX, newY);
            }
        });

        this.add(P1);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == b1) {
            this.dispose();
		} else if (ae.getSource() == b3) {
			new LoginFrame(); // Open the frame
			dispose(); // Close the login frame
        } else if (ae.getSource() == b2) {
            Login r = new Login();
            this.setVisible(false);
            r.setVisible(true);
        }
    }
}

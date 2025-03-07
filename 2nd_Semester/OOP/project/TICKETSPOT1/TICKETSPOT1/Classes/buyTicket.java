package Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class buyTicket extends JFrame implements ActionListener {

    // Declare components
    JLabel l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12, l13, l14, l15, l16, l17, l18, l19, l23, l24, l25, l26;
    JButton b1, b2, b3, b4, b5, b6;
    JPanel P2, P3, P4;
    JComboBox<String> c1, c2, c3, c4, c5, c6;
    private JPanel titleBar;
    private JLabel titleLabel;
    private JButton minimizeButton, closeButton;
    private int mouseX, mouseY;

    public buyTicket() {
        super("buyTicket");
        this.setSize(750, 510);
        setLocationRelativeTo(null);
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Set undecorated

        // Initialize custom title bar
        createTitleBar();

        P2 = new JPanel();
        P2.setBounds(0,30,750,480);
        P2.setBackground(new Color(248,248,248));
        P2.setBorder(BorderFactory.createLineBorder(Color.black));
        P2.setLayout(null);

        P3 = new JPanel();
        P3.setBounds(0,50,750,460);
        P3.setBackground(new Color(204,0,0));
        P3.setBorder(BorderFactory.createLineBorder(Color.black));
        P3.setLayout(null);

        // panel 2 components
        // buttons
        b1 = new JButton("Continue");
        b1.setFont(new Font("Serif",Font.BOLD,26));
        b1.setBounds(550,410,160,38);
        b1.setBackground(new Color(204,0,0));
		b1.setForeground(Color.white);
        b1.addActionListener(this);
        b1.setFocusable(false);
        b1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P2.add(b1);

        b2 = new JButton("Back");
        b2.setFont(new Font("Serif",Font.BOLD,22));
        b2.setBounds(30,415,100,34);
        b2.setBackground(new Color(204,0,0));
		b2.setForeground(Color.white);
        b2.addActionListener(this);
        b2.setFocusable(false);
        b2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P2.add(b2);

        // labels
        l3 = new JLabel("    Grab your ticket now !");
        l3.setFont(new Font("Serif",Font.BOLD|Font.ITALIC,28));
        l3.setForeground(Color.white);
        l3.setOpaque(true);
        l3.setBackground(new Color(204,0,0));
        l3.setBorder(BorderFactory.createLineBorder(Color.black));
        l3.setBounds(0,0,750,37);
		l3.setHorizontalAlignment(SwingConstants.CENTER);
        P2.add(l3);

        // movie selection
        l4 = new JLabel("Select movie : ");
        l4.setFont(new Font("Serif",Font.BOLD,18));
        l4.setForeground(Color.black);
        l4.setBounds(35,120,125,20);
        P2.add(l4);

        String[] movie = {"Stree 2", "Bhul Bhlaiyaa 3", "Deyaler Desh", "Pushpa 2","Gladiator 2", "Wicked","Kacher Manush Dure Thuiya","Toofan","The Family Star","Godzilla x Kong:The New Empire","Teri Baaton Mein Aisa Uiljha Jiya","Hubba"};
        c1 = new JComboBox<>(movie);
        c1.addActionListener(this);
        c1.setBounds(150,120,200,28);
        P2.add(c1);

        // hall selection
        l5 = new JLabel("Select Hall : ");
        l5.setFont(new Font("Serif",Font.BOLD,18));
        l5.setForeground(Color.black);
        l5.setBounds(35,200,125,20);
        P2.add(l5);

        String[] hall = {"Bashundhara City Complex", "Jamuna Future Park", "Shimanto Square", "SKS Shoping Mall"};
        c2 = new JComboBox<>(hall);
        c2.addActionListener(this);
        c2.setBounds(150,200,200,28);
        P2.add(c2);

        // showtime selection
        l6 = new JLabel("Show time : ");
        l6.setFont(new Font("Serif",Font.BOLD,18));
        l6.setForeground(Color.black);
        l6.setBounds(390,120,125,20);
        P2.add(l6);

        String[] showtime = {"11.00 am", "01.00 pm","01.30 pm", "03.00 pm", "06.00 pm", "06.30 pm", "07.00 pm", "9.00 pm", "10.00 pm"};
        c3 = new JComboBox<>(showtime);
        c3.addActionListener(this);
        c3.setBounds(510,120,200,28);
        P2.add(c3);

        // seat type selection
        l7 = new JLabel("Seat type : ");
        l7.setFont(new Font("Serif",Font.BOLD,18));
        l7.setForeground(Color.black);
        l7.setBounds(390,200,125,20);
        P2.add(l7);

        String[] seattype = {"Regular seat", "Premium seat", "VIP seat"};
        c4 = new JComboBox<>(seattype);
        c4.addActionListener(this);
        c4.setBounds(510,200,200,28);
        P2.add(c4);

        // seat number selection
        l8 = new JLabel("Number of seat : ");
        l8.setFont(new Font("Serif",Font.BOLD,17));
        l8.setForeground(Color.black);
        l8.setBounds(390,290,125,20);
        P2.add(l8);

        String[] seatno = {"01", "02", "03", "04", "05"};
        c5 = new JComboBox<>(seatno);
        c5.addActionListener(this);
        c5.setBounds(510,290,200,28);
        P2.add(c5);

        // date selection
        l9 = new JLabel("Date : ");
        l9.setFont(new Font("Serif",Font.BOLD,17));
        l9.setForeground(Color.black);
        l9.setBounds(35,290,125,20);
        P2.add(l9);

        String[] date = {"25 November, 2024", "26 November, 2024", "27 November, 2024", "28 November, 2024", "29 November, 2024", "30 November, 2024", "1 December, 2024"};
        c6 = new JComboBox<>(date);
        c6.addActionListener(this);
        c6.setBounds(150,290,200,28);
        P2.add(c6);

        // total cost
        l10 = new JLabel("Total cost : ");
        l10.setFont(new Font("Serif",Font.BOLD,18));
        l10.setForeground(Color.black);
        l10.setBounds(240,355,125,20);
        P2.add(l10);

        l11 = new JLabel();
        l11.setFont(new Font("Serif",Font.BOLD,19));
        l11.setOpaque(true);
        l11.setBackground(new Color(204,0,0));
        l11.setForeground(Color.white);
        l11.setBounds(350,355,75,28);
        P2.add(l11);

        // panel 3 components
        // buttons
        b3 = new JButton("Back");
        b3.setFont(new Font("Serif",Font.BOLD,22));
        b3.setBounds(30,415,100,34);
        b3.setForeground(new Color(204,0,0));
		b3.setBackground(Color.white);
        b3.addActionListener(this);
        b3.setFocusable(false);
        b3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P3.add(b3);

        b4 = new JButton("Confirm");
        b4.setFont(new Font("Serif",Font.BOLD,26));
        b4.setBounds(560,410,140,38);
        b4.setForeground(new Color(204,0,0));
		b4.setBackground(Color.white);
        b4.addActionListener(this);
        b4.setFocusable(false);
        b4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P3.add(b4);

        // panel 4 components
        P4 = new JPanel();
        P4.setBounds(75, 158, 600, 194);
        P4.setBackground(Color.PINK);
        P4.setLayout(null);

        // Load the image
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/t1.png"));
        // Resize the image to fit the label
        Image image = imageIcon.getImage().getScaledInstance(600, 194, Image.SCALE_DEFAULT);
        // Create a label to display the image
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        imageLabel.setBounds(0, 0, 600, 194); // Adjust the bounds as needed to position the image
        P4.add(imageLabel);

        // Add text labels over the image
        l12 = new JLabel();
        l12.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 19));
        l12.setForeground(Color.WHITE);
        l12.setBounds(80, 60, 350, 20); // Adjust the bounds as needed to position the text
        imageLabel.add(l12);

        l19 = new JLabel("Ticket Information :");
        l19.setFont(new Font("Serif",Font.BOLD | Font.ITALIC,36));
		l19.setForeground(Color.WHITE);
        l19.setBounds(220,70,350,30);
        P3.add(l19);

        l13 = new JLabel();
        l13.setFont(new Font("Serif",Font.BOLD,15));
		l13.setForeground(Color.WHITE);
        l13.setBounds(80,95,350,20);
        imageLabel.add(l13);

        l14 = new JLabel();
        l14.setFont(new Font("Serif",Font.BOLD,14));
		l14.setForeground(Color.BLACK);
        l14.setBounds(520,150,300,20);
        imageLabel.add(l14);

        l15 = new JLabel();
        l15.setFont(new Font("Serif",Font.BOLD,15));
		l15.setForeground(Color.WHITE);
        l15.setBounds(80,120,300,22);
        imageLabel.add(l15);

        l16 = new JLabel();
        l16.setFont(new Font("Serif",Font.BOLD,11));
		l16.setForeground(Color.WHITE);
        l16.setBounds(295,23,300,20);
        imageLabel.add(l16);

        l17 = new JLabel();
        l17.setFont(new Font("Serif",Font.BOLD,13));
		l17.setForeground(Color.BLACK);
        l17.setBounds(487,95,300,20);
        imageLabel.add(l17);

        l18 = new JLabel();
        l18.setFont(new Font("Serif",Font.BOLD,13));
		l18.setForeground(Color.BLACK);
        l18.setBounds(530,43,300,20);
        imageLabel.add(l18);

        //this.add(P1);
        this.add(P2);
        this.add(P3);
		P3.add(P4);
        P3.setVisible(false); // Initially set Panel 3 invisible
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

        titleLabel = new JLabel("Buy Ticket");
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
        // Calculate total cost based on seat type
        int seatCost;
        switch (c4.getSelectedIndex()) {
            case 0:
                seatCost = 300;
                break;
            case 1:
                seatCost = 500;
                break;
            case 2:
                seatCost = 700;
                break;
            default:
                seatCost = 0;
        }
        int numSeats = c5.getSelectedIndex() + 1;
        int totalCost = seatCost * numSeats;
        l11.setText(" " + totalCost + " Tk");

        // Handle button actions
        if (ae.getSource() == b1) {
            add(P4);
            add(P3);
            P2.setVisible(false);
            P3.setVisible(true);
        } else if (ae.getSource() == b2) {
            movies s = new movies();
            setVisible(false);
            s.setVisible(true);
        } else if (ae.getSource() == b3) {
            P2.setVisible(true);
            P3.setVisible(false);
        } else if (ae.getSource() == b4) {
            payment s = new payment();
            setVisible(false);
            s.setVisible(true);
        } else if (ae.getSource() == closeButton) {
            System.exit(0);
        } else if (ae.getSource() == minimizeButton) {
            setState(JFrame.ICONIFIED);
        }

        // Display selected options
        l12.setText("Movie  :   " + c1.getSelectedItem());
        l13.setText("Hall   :   " + c2.getSelectedItem());
        l14.setText("" + c3.getSelectedItem());
        l15.setText("Seat type   :   " + c4.getSelectedItem());
        l16.setText("" + c5.getSelectedItem());
        l17.setText("" + c6.getSelectedItem());
        l18.setText("" + l11.getText());
    }
}

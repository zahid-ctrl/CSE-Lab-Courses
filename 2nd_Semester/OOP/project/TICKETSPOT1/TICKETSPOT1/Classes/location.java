package Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class location extends JFrame implements ActionListener {
    
    JLabel l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l13, l14, l15, l16, l23, l24, l25, l26;
    JButton b1, b2, b3, b4, b5, b6;
    JPanel P1, P2, P3, P4, P5, P6, P7;
	
    private JPanel titleBar;
    private JLabel titleLabel;
    private JButton minimizeButton, closeButton;
    private int mouseX, mouseY;

    public location() {
        super("location");
        this.setSize(750, 510);
        setLocationRelativeTo(null);
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Set undecorated
        
        // Initialize custom title bar
        createTitleBar();

        // Panels
        P1 = new JPanel();
        P1.setBounds(0,30,750,50);
        P1.setBackground(new Color(204,0,0));
        P1.setBorder(BorderFactory.createLineBorder(Color.white));
        P1.setLayout(null);

        P2 = new JPanel();
        P2.setBounds(0,70,750,50);
        P2.setBackground(new Color(219,219,219));
        P2.setBorder(BorderFactory.createLineBorder(Color.white));
        P2.setLayout(null);

        // 4 boxes
        P3 = new JPanel();
        P3.setBackground(new Color(248,186,186));
        P3.setLayout(new GridLayout(0, 1)); // Using GridLayout for Panel 3
        
        P4 = new JPanel();
        P4.setBackground(new Color(248,124,124));
        P4.setLayout(new GridLayout(0, 1));
        
        P5 = new JPanel();
        P5.setBackground(new Color(248,62,62));
        P5.setLayout(new GridLayout(0, 1));
		
		P7 = new JPanel();
        P7.setBackground(new Color(248,0,0));
        P7.setLayout(new GridLayout(0, 1));

        // Container panel to hold P3, P4, and P5
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.add(P3);
        containerPanel.add(P4);
        containerPanel.add(P5);
		containerPanel.add(P7);

        //JScrollPane scrollPane = new JScrollPane(containerPanel); // Adding the container panel to the scroll pane
		JScrollPane scrollPane = new JScrollPane(containerPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(25,130,700,350); // Setting bounds for scroll pane
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.black)); // Adding border to the scroll pane

        // Background panel
        P6 = new JPanel();
        P6.setBounds(0,100,750,410);
        P6.setBackground(new Color(248, 248, 248));
        P6.setBorder(BorderFactory.createLineBorder(Color.white));
        P6.setLayout(null);

        // Panel 1 components
        l1 = new JLabel("TICKETSPOT");
        l1.setFont(new Font("Serif", Font.ITALIC | Font.BOLD, 36));
        l1.setForeground(Color.white);
        l1.setBounds(20,6,250,35);
        P1.add(l1);

        l2 = new JLabel(". . . Sit back,relax and enjoy the show");
        l2.setFont(new Font("Serif", Font.ITALIC | Font.BOLD, 12));
        l2.setForeground(Color.white);
        l2.setBounds(250,27,300,15);
        P1.add(l2);

        b1 = new JButton(new ImageIcon(getClass().getResource("/image/ulogo.png")));
        b1.setBorder(null);
        b1.setBounds(685,5,40,40);
		b1.setBackground(new Color(204,0,0));
        b1.addActionListener(this);
        b1.setFocusable(false);
        b1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P1.add(b1);   

        // Panel 2 components
        b2 = new JButton("Movies");
        b2.setFont(new Font("Serif", Font.PLAIN, 18));
        b2.setForeground(Color.black);
        b2.setBackground(new Color(219,219,219));
        b2.setBorder(null);
        b2.setBounds(65,20,60,18);
        b2.addActionListener(this);
        b2.setFocusable(false);
        b2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P2.add(b2);

        b3 = new JButton("Showtime");
        b3.setFont(new Font("Serif", Font.PLAIN, 18));
        b3.setForeground(Color.black);
        b3.setBackground(new Color(219,219,219));
        b3.setBorder(null);
        b3.setBounds(230,22,75,15);
        b3.addActionListener(this);
        b3.setFocusable(false);
        b3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P2.add(b3);

        b4 = new JButton("Ticket Price");
        b4.setFont(new Font("Serif", Font.PLAIN, 18));
        b4.setForeground(Color.black);
        b4.setBackground(new Color(219,219,219));
        b4.setBorder(null);
        b4.setBounds(405,22,100,15);
        b4.addActionListener(this);
        b4.setFocusable(false);
        b4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P2.add(b4);

        b5 = new JButton("Location");
        b5.setFont(new Font("Serif", Font.BOLD, 18));
        b5.setForeground(new Color(204,0,0));
        b5.setBackground(new Color(209,209,209));
        b5.setBorder(null);
        b5.setBounds(600,10,140,38);
        b5.addActionListener(this);
        b5.setFocusable(false);
        b5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        P2.add(b5);

        // Panel 3 components
        l2 = new JLabel("<html><b> Jamuna Future Park <b><br/> Level - 7 <br/> KA-244, Kuril, Progoti Shoroni, Dhaka </html>");
        l2.setFont(new Font("Serif", Font.ITALIC, 20));
        l2.setForeground(Color.black);
        P3.add(l2);
		P3.setPreferredSize(new Dimension(600, 100));

        l3 = new JLabel("<html><b> Bashundhara City Shopping Complex <b><br/> Level - 6 <br/> 3 No Tejturi Bazar West, Panthapath, Dhaka 1215 </html>");
        l3.setFont(new Font("Serif", Font.ITALIC, 20));
        l3.setForeground(Color.black);
        P4.add(l3);
		P4.setPreferredSize(new Dimension(600, 100));

        l4 = new JLabel("<html><b> Shimanto Square <b><br/> Level - 1 <br/> 2 Road No. 2, Dhaka 1205 </html>");
        l4.setFont(new Font("Serif", Font.ITALIC, 20));
        l4.setForeground(Color.black);
        P5.add(l4);
		P5.setPreferredSize(new Dimension(600, 100));
		
		l10 = new JLabel("<html><b> SKS Shopping Mall <b><br/> Level - 3 <br/>  Bir Uttam Ziaur Rahman Rd, Mohakhali, Dhaka </html>");
        l10.setFont(new Font("Serif", Font.ITALIC, 20));
        l10.setForeground(Color.black);
        P7.add(l10);
		P7.setPreferredSize(new Dimension(600, 100));

        // Adding panels to frame
        this.add(P1);
        this.add(P2);
        this.add(scrollPane); // Adding the scroll pane instead of Panel 3 directly
        this.add(P6);
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

        titleLabel = new JLabel("Location");
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

        public void actionPerformed(ActionEvent ae){

        if(ae.getSource()==b1)
		{
			userinfo u = new userinfo();
			this.setVisible(false);
		    u.setVisible(true);
		}

		else if(ae.getSource()==b2)
		{
			movies s = new movies();
			this.setVisible(false);
		    s.setVisible(true);
		}


        else if(ae.getSource()==b3)
		{
			showtime t = new showtime();
			this.setVisible(false);
		    t.setVisible(true);
		}


        else if(ae.getSource()==b4)
		{
			ticketPrice l = new ticketPrice();
			this.setVisible(false);
		    l.setVisible(true);
		}


        else if(ae.getSource()==b6)
		{
			buyTicket b = new buyTicket();
			this.setVisible(false);
		    b.setVisible(true);
		}
		
		
		else if (ae.getSource() == closeButton) {
            System.exit(0); // Close the application when close button is clicked
        } else if (ae.getSource() == minimizeButton) {
            this.setState(JFrame.ICONIFIED); // Minimize the window when minimize button is clicked
        }
    }

}

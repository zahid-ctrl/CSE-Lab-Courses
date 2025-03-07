package Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ticketPrice extends JFrame implements ActionListener {
    
    JLabel l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l13, l14, l15, l16, l23, l24, l25, l26;
    JButton b1, b2, b3, b4, b5, b6;
    JPanel P1, P2, P3, P4, P5, P6;
    private JPanel titleBar;
    private JLabel titleLabel;
    private JButton minimizeButton, closeButton;
    private int mouseX, mouseY;

    public ticketPrice() {
        super("ticketPrice");
        this.setSize(750, 510);
        setLocationRelativeTo(null);
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Set undecorated
        
        // Initialize custom title bar
        createTitleBar();

        // panels
       
        P1 = new JPanel();
        P1.setBounds(0, 30, 750, 50);
        P1.setBackground(new Color(204,0,0));
        P1.setBorder(BorderFactory.createLineBorder(Color.white));
        P1.setLayout(null);

        P2 = new JPanel();
        P2.setBounds(0, 80, 750, 50);
        P2.setBackground(new Color(219,219,219));
        P2.setBorder(BorderFactory.createLineBorder(Color.white));
        P2.setLayout(new GridLayout(0, 4));

        P3 = new JPanel();
        P3.setBounds(0,120,245,410);
        P3.setBackground(new Color(205,54,62));
        P3.setBorder(BorderFactory.createLineBorder(Color.white));
        P3.setLayout(null);

        P4 = new JPanel();
        P4.setBounds(245,120,245,410);
        P4.setBackground(new Color(18, 30, 75));
        P4.setBorder(BorderFactory.createLineBorder(Color.white));
        P4.setLayout(null);

        P5 = new JPanel();
        P5.setBounds(490,120,260,410);
        P5.setBackground(new Color(157,195,230));
        P5.setBorder(BorderFactory.createLineBorder(Color.white));
        P5.setLayout(null);
		
		P6 = new JPanel();
        P6.setBounds(490,130,260,410);
        P6.setBackground(new Color(231, 120, 37));
        P6.setBorder(BorderFactory.createLineBorder(Color.white));
        P6.setLayout(null);
		
		this.add(P6);
		
		
		    //panel 1 component

       l1 = new JLabel("TICKETSPOT");
       l1.setFont(new Font("Serif",Font.ITALIC | Font.BOLD,36));
       l1.setForeground(Color.white);
       l1.setBounds(20,6,250,35);
       P1.add(l1);

       l2 = new JLabel(". . . Sit back,relax and enjoy the show");
       l2.setFont(new Font("Serif",Font.ITALIC | Font.BOLD,12));
       l2.setForeground(Color.white);
       l2.setBounds(250,27,300,15);
       P1.add(l2);


       b1 = new JButton(new ImageIcon(getClass().getResource("/image/ulogo.png")));
	   b1.setBounds(685,5,40,40);
       b1.setBackground(new Color(204,0,0));
       b1.setBorder(null);
	   b1.addActionListener(this);
       b1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	   P1.add(b1);   

   
     

    //panel 2 component

       b2 = new JButton("Movies");
	   b2.setFont(new Font("Serif",Font.PLAIN,18));
	   b2.setForeground(Color.black);
	   b2.setBackground(new Color(219,219,219));
	   b2.setBorder(null);
	   b2.setBounds(65,18,60,25);
	   b2.addActionListener(this);
	   b2.setFocusable(false);
       b2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	   P2.add(b2);



       b3 = new JButton("Show time");
	   b3.setFont(new Font("Serif",Font.PLAIN,18));
	   b3.setForeground(Color.black);
	   b3.setBackground(new Color(219,219,219));
	   b3.setBorder(null);
	   b3.setBounds(220,10,150,38);
	   b3.addActionListener(this);
	   b3.setFocusable(false);
       b3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	   P2.add(b3);
    


       b4 = new JButton("Ticket Price");
	   b4.setFont(new Font("Serif",Font.BOLD,18));
	   b4.setForeground(new Color(204,0,0));
	   b4.setBackground(new Color(209,209,209));
	   b4.setBorder(null);
	   b4.setBounds(405,22,100,15);
	   b4.addActionListener(this);
	   b4.setFocusable(false);
       b4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	   P2.add(b4);



       b5 = new JButton("Location");
	   b5.setFont(new Font("Serif",Font.PLAIN,18));
	   b5.setForeground(Color.black);
	   b5.setBackground(new Color(219,219,219));
	   b5.setBorder(null);
	   b5.setBounds(600,22,80,15);
	   b5.addActionListener(this);
	   b5.setFocusable(false);
       b5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	   P2.add(b5);
	   
	   
	   //panel 3 content

       l2 = new JLabel(new ImageIcon(getClass().getResource("/image/r1.jpg")));
	   l2.setBounds(16,25,210,140);
	   P3.add(l2); 
       
      
       l3 = new JLabel(new ImageIcon(getClass().getResource("/image/p1.jpg")));
	   l3.setBounds(16,25,210,140);
	   P4.add(l3);

    
       l4 = new JLabel(new ImageIcon(getClass().getResource("/image/v1.jpg")));
	   l4.setBounds(20, 22, 215, 130);
	   P6.add(l4);
       
 
       l5 = new JLabel("Regular seat");
       l5.setFont(new Font("Serif",Font.PLAIN,22));
	   l5.setForeground(Color.WHITE);
	   l5.setBounds(60,120,210,140);
	   P3.add(l5); 
       
      
       l6 = new JLabel("Premium seat");
       l6.setFont(new Font("Serif",Font.PLAIN,22));
	   l6.setForeground(Color.WHITE);
	   l6.setBounds(60,120,210,140);
	   P4.add(l6);

    
       l7 = new JLabel("VIP seat");
       l7.setFont(new Font("Serif",Font.PLAIN,22));
	   l7.setForeground(Color.WHITE);
	   l7.setBounds(75,110,210,140);
	   P6.add(l7);


       l8 = new JLabel("Price : 300 Tk / each");
       l8.setFont(new Font("Serif",Font.BOLD,22));
	   l8.setForeground(Color.WHITE);
	   l8.setBounds(30,220,210,140);
	   P3.add(l8); 
       
      
       l9 = new JLabel("Price : 500 Tk / each");
       l9.setFont(new Font("Serif",Font.BOLD,22));
	   l9.setForeground(Color.WHITE);
	   l9.setBounds(30,220,210,140);
	   P4.add(l9);

    
       l10 = new JLabel("Price : 700 Tk / each");
       l10.setFont(new Font("Serif",Font.BOLD,22));
	   l10.setForeground(Color.WHITE);
	   l10.setBounds(30, 212, 210, 140);
	   P6.add(l10);

        
        this.add(P1);
        this.add(P2);
        this.add(P3);
        this.add(P4);
        this.add(P5);
		
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

        titleLabel = new JLabel("Ticket Price");
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


        else if(ae.getSource()==b5)
		{
			location l = new location();
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

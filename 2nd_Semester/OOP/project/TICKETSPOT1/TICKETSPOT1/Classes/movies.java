package Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class movies extends JFrame implements ActionListener {
    
    JLabel l1,l2,l3,l4,l5,l6,l7,l8,l9,l10,l13,l14,l15,l16,l23,l24,l25,l26,l27;
	   
	JButton b1,b2,b3,b4,b5,b6;

    JPanel P1,P2,P3,P4;
	
	private JPanel titleBar;
    private JLabel titleLabel;
    private JButton minimizeButton, closeButton;
	private int mouseX, mouseY;





    public movies()
    {
        super("movies");
        this.setSize(750,510);
        setLocationRelativeTo(null);
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true); // Set undecorated
		
        // Initialize custom title bar
        createTitleBar();
		
		





    // panels
       
        P1 = new JPanel();
        P1.setBounds(0,30,750,50);
        P1.setBackground(new Color(204,0,0));
        P1.setBorder(BorderFactory.createLineBorder(Color.white));
        P1.setLayout(null);

        P2 = new JPanel();
        P2.setBounds(0,80,750,50);
        P2.setBackground(new Color(248, 248, 248));
        P2.setBorder(BorderFactory.createLineBorder(Color.white));
        //P2.setLayout(null);
		P2.setLayout(new GridLayout(0, 4));
		
		P3 = new JPanel();
        P3.setLayout(new GridLayout(0, 3)); // Set GridLayout to arrange components in a grid
		
		P4 = new JPanel();
        P4.setBounds(0,100,750,50);
        P4.setBackground(new Color(248, 248, 248));
        P4.setBorder(BorderFactory.createLineBorder(Color.white));
        P4.setLayout(null);






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
	   b1.setBorder(null);
	   b1.setBounds(685,5,40,40);
	   b1.addActionListener(this);
	   b1.setBackground(new Color(204,0,0));
	   b1.setFocusable(false);
       b1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	   P1.add(b1);   

   
     




    //panel 2 component

       b2 = new JButton("Movies");
	   b2.setFont(new Font("Serif",Font.BOLD,18));
	   b2.setForeground(new Color(204,0,0));
	   b2.setBackground(new Color(209,209,209));
	   b2.setBorder(null);
	   //b2.setBounds(25,80,150,35);
	   b2.addActionListener(this);
	   b2.setFocusable(false);
       b2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	   P2.add(b2);



       b3 = new JButton("Showtime");
	   b3.setFont(new Font("Serif",Font.PLAIN,18));
	   b3.setForeground(Color.black);
	   b3.setBackground(new Color(219,219,219));
	   b3.setBorder(null);
	   b3.setBounds(230,90,75,25);
	   b3.addActionListener(this);
	   b3.setFocusable(false);
       b3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	   P2.add(b3);
    


       b4 = new JButton("Ticket Price");
	   b4.setFont(new Font("Serif",Font.PLAIN,18));
	   b4.setForeground(Color.black);
	   b4.setBackground(new Color(219,219,219));
	   b4.setBorder(null);
	   b4.setBounds(405,90,100,25);
	   b4.addActionListener(this);
	   b4.setFocusable(false);
       b4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	   P2.add(b4);



       b5 = new JButton("Location");
	   b5.setFont(new Font("Serif",Font.PLAIN,18));
	   b5.setForeground(Color.black);
	   b5.setBackground(new Color(219,219,219));
	   b5.setBorder(null);
	   b5.setBounds(600,90,80,25);
	   b5.addActionListener(this);
	   b5.setFocusable(false);
       b5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	   P2.add(b5);







    //panel 3 component 
	   
	   
	   l2 = new JLabel("  Now Showing :");
       l2.setFont(new Font("Serif",Font.BOLD | Font.ITALIC ,22));
       l2.setOpaque(true);
       l2.setBackground(new Color(204,0,0));
	   l2.setForeground(Color.white);
       l2.setBounds(0, 130, 750, 40);
	   l2.setHorizontalAlignment(SwingConstants.LEFT);
       P4.add(l2);
	   
	   

       b6 = new JButton("Buy Ticket !!");
	   b6.setFont(new Font("Serif",Font.PLAIN,25));
	   b6.setForeground(Color.white);
	   b6.setBackground(new Color(204,0,0));
	   b6.setBounds(250,452,250,38);
	   b6.addActionListener(this);
	   b6.setFocusable(false);
       b6.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	   P4.add(b6);

    




   //panel 3 movie name
JPanel moviePanel1 = new JPanel(new BorderLayout());
l3 = new JLabel(new ImageIcon(getClass().getResource("/image/1.jpg")));
moviePanel1.add(l3, BorderLayout.CENTER);
JLabel movieTitle1 = new JLabel("Stree 2", SwingConstants.CENTER);
movieTitle1.setFont(new Font("Serif", Font.BOLD, 14));
moviePanel1.add(movieTitle1, BorderLayout.SOUTH);
P3.add(moviePanel1);

JPanel moviePanel2 = new JPanel(new BorderLayout());
l4 = new JLabel(new ImageIcon(getClass().getResource("/image/2.jpg")));
moviePanel2.add(l4, BorderLayout.CENTER);
JLabel movieTitle2 = new JLabel("Bhul Bhulaiyaa 3", SwingConstants.CENTER);
movieTitle2.setFont(new Font("Serif", Font.BOLD, 14));
moviePanel2.add(movieTitle2, BorderLayout.SOUTH);
P3.add(moviePanel2);

JPanel moviePanel3 = new JPanel(new BorderLayout()); // Corrected variable name
l5 = new JLabel(new ImageIcon(getClass().getResource("/image/3.jpg")));
moviePanel3.add(l5, BorderLayout.CENTER);
JLabel movieTitle3 = new JLabel("Deyaler Desh", SwingConstants.CENTER);
movieTitle3.setFont(new Font("Serif", Font.BOLD, 14));
moviePanel3.add(movieTitle3, BorderLayout.SOUTH); // Corrected variable name
P3.add(moviePanel3);

JPanel moviePanel4 = new JPanel(new BorderLayout());
l6 = new JLabel(new ImageIcon(getClass().getResource("/image/4.jpg")));
moviePanel4.add(l6, BorderLayout.CENTER);
JLabel movieTitle4 = new JLabel("Pushpa 2", SwingConstants.CENTER);
movieTitle4.setFont(new Font("Serif", Font.BOLD, 14));
moviePanel4.add(movieTitle4, BorderLayout.SOUTH);
P3.add(moviePanel4);

//panel 3 movie name
JPanel moviePanel5 = new JPanel(new BorderLayout());
l13 = new JLabel(new ImageIcon(getClass().getResource("/image/5.jpg")));
moviePanel5.add(l13, BorderLayout.CENTER);
JLabel movieTitle5 = new JLabel("Gladitor 2", SwingConstants.CENTER);
movieTitle5.setFont(new Font("Serif", Font.BOLD, 14));
moviePanel5.add(movieTitle5, BorderLayout.SOUTH);
P3.add(moviePanel5);

JPanel moviePanel6 = new JPanel(new BorderLayout());
l14 = new JLabel(new ImageIcon(getClass().getResource("/image/6.jpg")));
moviePanel6.add(l14, BorderLayout.CENTER);
JLabel movieTitle6 = new JLabel("Wicked", SwingConstants.CENTER);
movieTitle6.setFont(new Font("Serif", Font.BOLD, 14));
moviePanel6.add(movieTitle6, BorderLayout.SOUTH);
P3.add(moviePanel6);

JPanel moviePanel7 = new JPanel(new BorderLayout());
l15 = new JLabel(new ImageIcon(getClass().getResource("/image/7.jpg")));
moviePanel7.add(l15, BorderLayout.CENTER);
JLabel movieTitle7 = new JLabel("Kacher Manush Dure Thuiya", SwingConstants.CENTER);
movieTitle7.setFont(new Font("Serif", Font.BOLD, 14));
moviePanel7.add(movieTitle7, BorderLayout.SOUTH);
P3.add(moviePanel7);

JPanel moviePanel8 = new JPanel(new BorderLayout());
l16 = new JLabel(new ImageIcon(getClass().getResource("/image/8.jpg")));
moviePanel8.add(l16, BorderLayout.CENTER);
JLabel movieTitle8 = new JLabel("Toofan", SwingConstants.CENTER);
movieTitle8.setFont(new Font("Serif", Font.BOLD, 14));
moviePanel8.add(movieTitle8, BorderLayout.SOUTH);
P3.add(moviePanel8);

// Panel 3 movie name
JPanel moviePanel9 = new JPanel(new BorderLayout());
l23 = new JLabel(new ImageIcon(getClass().getResource("/image/9.jpg")));
moviePanel9.add(l23, BorderLayout.CENTER);
JLabel movieTitle9 = new JLabel("The Family Star", SwingConstants.CENTER);
movieTitle9.setFont(new Font("Serif", Font.BOLD, 14));
moviePanel9.add(movieTitle9, BorderLayout.SOUTH);
P3.add(moviePanel9);

JPanel moviePanel10 = new JPanel(new BorderLayout());
l24 = new JLabel(new ImageIcon(getClass().getResource("/image/10.jpg")));
moviePanel10.add(l24, BorderLayout.CENTER);
JLabel movieTitle10 = new JLabel("Godzilla x Kong: The New Empire", SwingConstants.CENTER);
movieTitle10.setFont(new Font("Serif", Font.BOLD, 14));
moviePanel10.add(movieTitle10, BorderLayout.SOUTH);
P3.add(moviePanel10);

JPanel moviePanel12 = new JPanel(new BorderLayout());
l26 = new JLabel(new ImageIcon(getClass().getResource("/image/11.jpg")));
moviePanel12.add(l26, BorderLayout.CENTER);
JLabel movieTitle12 = new JLabel("Teri Baaton Mein Aisa Uiljha Jiya", SwingConstants.CENTER);
movieTitle12.setFont(new Font("Serif", Font.BOLD, 14));
moviePanel12.add(movieTitle12, BorderLayout.SOUTH);
P3.add(moviePanel12);

JPanel moviePanel13 = new JPanel(new BorderLayout());
l27 = new JLabel(new ImageIcon(getClass().getResource("/image/12.jpg")));
moviePanel13.add(l27, BorderLayout.CENTER);
JLabel movieTitle13 = new JLabel("Hubba", SwingConstants.CENTER);
movieTitle13.setFont(new Font("Serif", Font.BOLD, 14));
moviePanel13.add(movieTitle13, BorderLayout.SOUTH);
P3.add(moviePanel13);



	   
	   
	   // Wrap panel 3 inside a JScrollPane
        JScrollPane scrollPane = new JScrollPane(P3, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(20, 175, 715, 270);
		
		this.add(scrollPane); // Add the scroll pane instead of plain panel 3



        this.add(P1);
this.add(P2);
this.add(P4);

// Set the frame visible
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

        titleLabel = new JLabel("Movies");
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

		else if(ae.getSource()==b3)
		{
			showtime s = new showtime();
			this.setVisible(false);
		    s.setVisible(true);
		}


        else if(ae.getSource()==b4)
		{
			ticketPrice t = new ticketPrice();
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

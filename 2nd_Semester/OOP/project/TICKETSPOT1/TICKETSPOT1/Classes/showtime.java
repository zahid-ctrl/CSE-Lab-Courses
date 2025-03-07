package Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class showtime extends JFrame implements ActionListener {
    
    JLabel l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l13, l14, l15, l16, l23, l24, l25, l26;
    JButton b1, b2, b3, b4, b5, b6;
    JPanel P1, P2, P3, P4;
    private JPanel titleBar;
    private JLabel titleLabel;
    private JButton minimizeButton, closeButton;
    private int mouseX, mouseY;

    public showtime() {
        super("showtime");
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
        P2.setBackground(new Color(219, 219, 219));
        P2.setBorder(BorderFactory.createLineBorder(Color.white));
        P2.setLayout(new GridLayout(0, 4));

        P3 = new JPanel();
        P3.setLayout(new GridLayout(0, 1, 10, 10)); // Set GridLayout to arrange components in a grid
        P3.setBackground(new Color(216, 228, 250));

        P4 = new JPanel();
        P4.setBounds(0, 100, 750, 50);
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



       b3 = new JButton("Showtime");
	   b3.setFont(new Font("Serif",Font.BOLD,18));
	   b3.setForeground(new Color(204,0,0));
	   b3.setBackground(new Color(209,209,209));
	   b3.setBorder(null);
	   b3.setBounds(220,10,150,38);
	   b3.addActionListener(this);
	   b3.setFocusable(false);
       b3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	   P2.add(b3);
    


       b4 = new JButton("Ticket Price");
	   b4.setFont(new Font("Serif",Font.PLAIN,18));
	   b4.setForeground(Color.black);
	   b4.setBackground(new Color(219,219,219));
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
		
		
		
		

        // Add movies to P3 panel
        addMovie("Stree 2", "/image/1.jpg", new String[]{"12:00 am", "02:00 pm", "05:00 pm", "08:00 pm"});
		addMovie("Bhulbhulaiyaa 3", "/image/2.jpg", new String[]{"1:00 pm", "02:00 pm", "05:30 pm", "07:00 pm"});
		addMovie("Deyaler Desh", "/image/3.jpg", new String[]{"12:30 am", "02:30 pm", "06:00 pm", "07:00 pm"});
		addMovie("Pushpa 2", "/image/4.jpg", new String[]{"12:00 am", "02:00 pm", "05:00 pm", "08:00 pm"});
		addMovie("Gladiator 2", "/image/5.jpg", new String[]{"12:00 am", "02:00 pm", "05:00 pm", "08:00 pm"});
		addMovie("Wicked", "/image/6.jpg", new String[]{"12:00 am", "02:00 pm", "05:00 pm", "08:00 pm"});
		addMovie("Kacher Manush Dure Thuiya", "/image/7.jpg", new String[]{"12:00 am", "02:00 pm", "05:00 pm", "08:00 pm"});
		addMovie("Toofan", "/image/8.jpg", new String[]{"12:00 pm", "02:00 am", "05:00 am", "08:00 am"});
		addMovie("The Family Star", "/image/9.jpg", new String[]{"12:00 am", "02:00 pm", "05:00 pm", "08:00 pm"});
		addMovie("Godzilla x Kong: The New Empire", "/image/10.jpg", new String[]{"12:00 am", "02:00 pm", "05:00 pm", "08:00 pm"});
		addMovie("Teri Batoon Mein Aisa Uiljha Jiya", "/image/11.jpg", new String[]{"1:00 am", "02:00 am", "05:00 am", "08:00 am"});
		addMovie("Hubba", "/image/12.jpg", new String[]{"12:00 am", "02:00 pm", "05:00 pm", "08:00 pm"});

        // Wrap panel 3 inside a JScrollPane
        JScrollPane scrollPane = new JScrollPane(P3, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(20, 132, 715, 350);

        this.add(scrollPane); // Add the scroll pane instead of plain panel 3
        this.add(P1);
        this.add(P2);
        this.add(P4);

        // Set the frame visible
        setVisible(true);
    }

private void addMovie(String title, String imagePath, String[] screeningTimes) {
    JPanel moviePanel = new JPanel(new GridLayout(1, 2)); // Create a 1x2 grid layout panel for each movie
    moviePanel.setBackground(Color.WHITE);

    // Add movie poster to the left
    JLabel movieImageLabel = new JLabel(new ImageIcon(getClass().getResource(imagePath)));
    moviePanel.add(movieImageLabel);

    // Create a panel for movie title and screening times
    JPanel infoPanel = new JPanel(new GridLayout(0, 1)); // Use GridLayout with one column
    infoPanel.setBackground(Color.WHITE);

    // Add movie title label to infoPanel
    JLabel movieTitleLabel = new JLabel(title, SwingConstants.CENTER);
    movieTitleLabel.setFont(new Font("Serif", Font.ITALIC | Font.BOLD, 17));
    movieTitleLabel.setForeground(Color.black);
    infoPanel.add(movieTitleLabel);

    // Add screening times below the movie title
    for (String time : screeningTimes) {
        JLabel screeningTimeLabel = new JLabel(time, SwingConstants.CENTER);
        screeningTimeLabel.setFont(new Font("Serif", Font.PLAIN, 12)); // smaller font size
        screeningTimeLabel.setForeground(Color.black);
        infoPanel.add(screeningTimeLabel);
    }

    // Add the infoPanel to the right side of the moviePanel
    moviePanel.add(infoPanel);

    // Add the moviePanel to the main panel (P3)
    P3.add(moviePanel);
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

        titleLabel = new JLabel("Show Time");
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

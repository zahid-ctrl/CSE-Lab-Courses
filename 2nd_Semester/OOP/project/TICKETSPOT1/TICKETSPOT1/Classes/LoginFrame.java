package Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import Interface.AdminLogin;

public class LoginFrame extends JFrame implements ActionListener {
    private JPanel titleBar;
    private JLabel titleLabel;
    private JButton closeButton, minimizeButton;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, backButton;
    private int mouseX, mouseY;

    public LoginFrame() {
        setSize(750, 510);
        setLayout(null);
		setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true); // Set undecorated
		getContentPane().setBackground(new Color(204, 0, 0));


        createTitleBar();
        createLoginForm();

        setVisible(true);
    }

    private void createTitleBar() {
        titleBar = new JPanel();
        titleBar.setBounds(0, 0, getWidth(), 30);
        titleBar.setLayout(null);
        titleBar.setBackground(new Color(51, 51, 51));

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

        titleLabel = new JLabel("Admin Login");
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
	
	private void customizeButton(JButton button) {
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(204,0,0));
        button.setFont(new Font("Arial", Font.PLAIN, 16));

        Border roundedBorder = new LineBorder(Color.WHITE, 2, true);
        button.setBorder(roundedBorder);
    }

    private void createLoginForm() {
        JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setForeground(Color.white);
        usernameLabel.setBounds(210, 200, 100, 30);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(320, 200, 200, 30);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setForeground(Color.white);
        passwordLabel.setBounds(210, 250, 100, 30);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(320, 250, 200, 30);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(300, 300, 100, 30);
		loginButton.setForeground(Color.white);
	    loginButton.setBackground(new Color(204,0,0));
        loginButton.addActionListener(this);
		customizeButton(loginButton);
		loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(loginButton);
		
		backButton = new JButton("Back");
        backButton.setBounds(20, 50, 100, 30);
		backButton.setForeground(Color.white);
	    backButton.setBackground(new Color(204,0,0));
        backButton.addActionListener(this);
		customizeButton(backButton);
		backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(backButton);
    }

    public void actionPerformed(ActionEvent e) {
    if (e.getSource() == closeButton) {
        System.exit(0);
	} else if (e.getSource() == backButton) {
        new Homepage(); // Open the frame
        dispose(); // Close the login frame
    } else if (e.getSource() == minimizeButton) {
        this.setState(Frame.ICONIFIED);
    } else if (e.getSource() == loginButton) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (username.equals("admin") && password.equals("admin")) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            new Admin(); // Open the Admin frame
            dispose(); // Close the login frame
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password");
        }
    }
}
}

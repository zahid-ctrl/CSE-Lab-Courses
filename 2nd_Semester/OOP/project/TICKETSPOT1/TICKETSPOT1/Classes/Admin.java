package Classes;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.File;

public class Admin extends JFrame {

    private JPanel leftPanel;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField textField1, textField2, textField3, searchTextField;
    private JComboBox<String> comboBox1;
    private JDialog editDialog; // Declare editDialog as a class-level instance variable
	private JLabel l1, l2, l3;

    public Admin() {
        setTitle("Admin");
        setSize(1080, 720);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        leftPanel = new JPanel();
        leftPanel.setLayout(null);
        leftPanel.setBackground(new Color(204,0,0));
        getContentPane().add(leftPanel);

        JButton editButton = new JButton("Edit");
        customizeButton(editButton);
        editButton.setBounds(240, 415, 150, 50);
        leftPanel.add(editButton);

        JButton deleteButton = new JButton("Delete");
        customizeButton(deleteButton);
        deleteButton.setBounds(60, 415, 150, 50);
        leftPanel.add(deleteButton);

        searchTextField = new JTextField();
        searchTextField.setBounds(250, 100, 500, 25);
        leftPanel.add(searchTextField);

        JButton searchButton = new JButton("Search");
        customizeButton(searchButton);
        searchButton.setBounds(800, 100, 80, 25);
        leftPanel.add(searchButton);

        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        leftPanel.add(separator);
		
		l1 = new JLabel("Users Information");
        l1.setFont(new Font("Serif", Font.BOLD, 32));
        l1.setOpaque(true);
        l1.setBackground(new Color(204,0,0));
		l1.setForeground(Color.white);
        l1.setBounds(100, 30, 980, 40);
		l1.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.add(l1);
		
		l2 = new JLabel(new ImageIcon(getClass().getResource("/image/admin.png")));
        //l2.setFont(new Font("Serif", Font.BOLD, 32));
        l2.setOpaque(true);
        l2.setBackground(new Color(204,0,0));
		l2.setForeground(Color.white);
        l2.setBounds(10, 20, 50, 50);
		//l1.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.add(l2);
		
		l3 = new JLabel("Admin");
        l3.setFont(new Font("Serif", Font.BOLD, 12));
        l3.setOpaque(true);
        l3.setBackground(new Color(204,0,0));
		l3.setForeground(Color.white);
        l3.setBounds(0, 70, 70, 20);
		l3.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.add(l3);
		

        String[] columnNames = {"Name", "Password", "Mobile", "Email", "Gender"};
        tableModel = new DefaultTableModel(null, columnNames);
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBounds(450, 150, 600, 200);
        leftPanel.add(tableScrollPane);

        JButton backButton = new JButton("Back");
        customizeButton(backButton);
        backButton.setBounds(60, 475, 330, 50);
        leftPanel.add(backButton);

        textField1 = new JTextField();
        textField2 = new JTextField();
        textField3 = new JTextField();
        comboBox1 = new JComboBox<>(new String[]{"Male", "Female", "Other"});

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String name = (String) table.getValueAt(selectedRow, 0);
                    String password = (String) table.getValueAt(selectedRow, 1);
                    String mobile = (String) table.getValueAt(selectedRow, 2);
                    String email = (String) table.getValueAt(selectedRow, 3);
                    String gender = (String) table.getValueAt(selectedRow, 4);

                    textField1.setText(name);
                    textField2.setText(mobile);
                    textField3.setText(email);
                    comboBox1.setSelectedItem(gender);

                    JButton saveButton = new JButton("Save");
                    saveButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            tableModel.setValueAt(textField1.getText(), selectedRow, 0);
                            tableModel.setValueAt(textField2.getText(), selectedRow, 2);
                            tableModel.setValueAt(textField3.getText(), selectedRow, 3);
                            tableModel.setValueAt(comboBox1.getSelectedItem(), selectedRow, 4);
                            saveDataToFile();
                            editDialog.dispose();
                        }
                    });

                    editDialog = new JDialog(Admin.this, "Edit Details", true);
                    editDialog.setLayout(new GridLayout(0, 2));
                    editDialog.add(new JLabel("Name:"));
                    editDialog.add(textField1);
                    editDialog.add(new JLabel("Mobile:"));
                    editDialog.add(textField2);
                    editDialog.add(new JLabel("Email:"));
                    editDialog.add(textField3);
                    editDialog.add(new JLabel("Gender:"));
                    editDialog.add(comboBox1);
                    editDialog.add(saveButton);
                    editDialog.pack();
                    editDialog.setLocationRelativeTo(null);
                    editDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(Admin.this, "Please select a row to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
		
		
		// Add the action listener for the "Back" button
backButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Close the Admin frame
        dispose();
        // Open the LoginFrame
        new LoginFrame();
    }
});

		
		

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchTextField.getText().trim();

                if (!searchText.isEmpty()) {
                    for (int i = 0; i < table.getRowCount(); i++) {
                        for (int j = 0; j < table.getColumnCount(); j++) {
                            String cellValue = String.valueOf(table.getValueAt(i, j));
                            if (cellValue.toLowerCase().contains(searchText.toLowerCase())) {
                                table.changeSelection(i, j, false, false);
                                table.scrollRectToVisible(table.getCellRect(i, j, true));
                                return;
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(Admin.this, "No matching record found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(Admin.this, "Please enter a search keyword.", "Search Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
		
		       // Add the action listener for the "Delete" button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row index
                int selectedRow = table.getSelectedRow();
                // Check if a row is selected
                if (selectedRow != -1) {
                    // Remove the selected row from the table
                    tableModel.removeRow(selectedRow);
                    // Save the updated data to the file
                    saveDataToFile();
                } else {
                    // If no row is selected, display a message
                    JOptionPane.showMessageDialog(Admin.this, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
		
		
		

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                leftPanel.setBounds(0, 0, getWidth(), getHeight());
                separator.setBounds(432, 150, 2, getHeight() - 200);
                tableScrollPane.setBounds(450, 150, getWidth() * 3 / 5 - 50, getHeight() - 200);
            }
        });

        setVisible(true);
        loadDataFromFile();
    }

    private void customizeButton(JButton button) {
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(204,0,0));
        button.setFont(new Font("Arial", Font.PLAIN, 16));

        Border roundedBorder = new LineBorder(Color.WHITE, 2, true);
        button.setBorder(roundedBorder);
    }

    private void saveDataToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data" + File.separator + "data.txt", false))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                writer.write("Name        :  " + tableModel.getValueAt(i, 0));
                writer.newLine();
                writer.write("Password  :  " + tableModel.getValueAt(i, 1));
                writer.newLine();
                writer.write("Mobile       :  " + tableModel.getValueAt(i, 2));
                writer.newLine();
                writer.write("Email         :  " + tableModel.getValueAt(i, 3));
                writer.newLine();
                writer.write("Gender      :  " + tableModel.getValueAt(i, 4));
                writer.newLine();
                writer.write("=================");
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDataFromFile() {
        try {
            FileReader reader = new FileReader("data" + File.separator + "data.txt");
            BufferedReader br = new BufferedReader(reader);
            String line;
            List<String> rowData = new ArrayList<>();
            tableModel.setRowCount(0);
            while ((line = br.readLine()) != null) {
                if (line.contains(":")) {
                    String[] parts = line.split(":");
                    if (parts.length > 1) {
                        String value = parts[1].trim();
                        rowData.add(value);
                    }
                } else if (line.equals("=================")) {
                    String[] rowDataArray = rowData.toArray(new String[0]);
                    tableModel.addRow(rowDataArray);
                    hideTableColumn(1);
                    rowData.clear();
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void hideTableColumn(int columnIndex) {
        table.getColumnModel().getColumn(columnIndex).setMinWidth(0);
        table.getColumnModel().getColumn(columnIndex).setMaxWidth(0);
        table.getColumnModel().getColumn(columnIndex).setWidth(0);
    }
}

package text;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TextEditor extends JFrame implements ActionListener {
	
	JTextArea textArea;
	JScrollPane scrollPane;
	JLabel fontLabel;
	JSpinner fontSizeSpinner;
	JButton fontColorButton;
	JComboBox fontBox;
	
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenuItem openItem;
	JMenuItem saveItem;
	JMenuItem exitItem;
	
	TextEditor() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Text Editor");
		this.setSize(500, 500);
		this.setLayout(new FlowLayout());
		this.setLocationRelativeTo(null);    //run edende ekran ortasinda baslayir
		
		//text area
		textArea = new JTextArea();
		textArea.setLineWrap(true);          //diger setre kecirdir
		textArea.setWrapStyleWord(true);     //diger setre sozu kecirir
		textArea.setFont(new Font("Arial", Font.PLAIN, 20));
		
		//scroll pane
		scrollPane = new JScrollPane(textArea);              
		scrollPane.setPreferredSize(new Dimension(450, 450));  //bunu scrollPane'de etdik deye textArea'dakini sildik
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		//font label
		fontLabel = new JLabel("Font");
		
		//font size spinner
		fontSizeSpinner = new JSpinner();
		fontSizeSpinner.setPreferredSize(new Dimension(50, 25));
		fontSizeSpinner.setValue(20);
		fontSizeSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, (int) fontSizeSpinner.getValue()));				
			}
			
		});
		
		//font color chooser button		
		fontColorButton = new JButton("Color");
		fontColorButton.addActionListener(this);
		
		//get available fonts
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		
		//font chooser combo box
		fontBox = new JComboBox(fonts);
		fontBox.addActionListener(this);    //yuxarida implement oluubdur deye
		fontBox.setSelectedItem("Arial");   //default font
		
		//menu bar
		menuBar = new JMenuBar();        //menu bar yaradilir
		fileMenu = new JMenu("File");    //icine file adinda menu atilir
		openItem = new JMenuItem("Open");
		saveItem = new JMenuItem("Save");   //menyu icindeki item'lari yaratdiq
		exitItem = new JMenuItem("Exit");
		
		openItem.addActionListener(this);
		saveItem.addActionListener(this);     //actionListener add edib asagida yaziriq
		exitItem.addActionListener(this);
		
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(exitItem);
		menuBar.add(fileMenu);
		
		////////////////////
		
		
		//frame adding	
		this.setJMenuBar(menuBar);
		this.add(fontLabel);
		this.add(fontSizeSpinner);
		this.add(fontColorButton);
		this.add(fontBox);
		this.add(scrollPane);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//color picker
		if (e.getSource() == fontColorButton) {
			JColorChooser colorChooser = new JColorChooser();
			
			Color color = colorChooser.showDialog(colorChooser, "Choose a color", Color.black);
			
			textArea.setForeground(color);
		}
		
		//font chooser
		if (e.getSource() == fontBox) {
			textArea.setFont(new Font((String)fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
		}
		///////////////////////////////////////////////////////////////////////
		//openItem
		if (e.getSource() == openItem) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("."));
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
			fileChooser.setFileFilter(filter);
			
			int response = fileChooser.showOpenDialog(null);
			
			if (response == JFileChooser.APPROVE_OPTION) {     //yes kimi				
				File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
				Scanner fileIn = null;
				
				try {
					fileIn = new Scanner(file);
					if (file.isFile()) {
						while (fileIn.hasNextLine()) {
							String line = fileIn.nextLine() + "\n";
							textArea.append(line);
						}
					}
				} catch (FileNotFoundException e1) {
				
					e1.printStackTrace();
				} finally {
					fileIn.close();
				}
			}
		}
		
		//saveItem
		if (e.getSource() == saveItem) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("."));   //. proj foldere atir save edende
			
			int response = fileChooser.showSaveDialog(null);
			
			if (response == JFileChooser.APPROVE_OPTION) {    //yes kimi
				File file;
				PrintWriter fileOut = null;                          //text formatina cevirir
				
				file = new File(fileChooser.getSelectedFile().getAbsolutePath());
				try {
					fileOut = new PrintWriter(file);
					fileOut.println(textArea.getText());
				} catch (FileNotFoundException e1) {
					
					e1.printStackTrace();
				} finally {
					fileOut.close();
				}
			}
		}
		
		//exitItem
		if (e.getSource() == exitItem) {
			System.exit(0);
		}
		
	}

}

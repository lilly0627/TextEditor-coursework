package labs.lab9;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Stack;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class TextEditor extends JFrame {
	private JTextArea editor;
	private JMenuBar menuBar;
	private JMenu fileM,editM;
	private JScrollPane scpane;
	private JMenuItem exitI,redoI, undoI;
	
	private JButton clear; // button
	private JCheckBox italicCheckBox;
	private JCheckBox boldCheckBox;
	private JRadioButton Button8;
	private JRadioButton Button16;
	private JRadioButton Button24;
	private JRadioButton Button32;
	private JRadioButton Button40;
	private JComboBox<String> facenameCombo;
	private ButtonGroup group;
	
	static Stack<CurrentAction> undoStack = new Stack<CurrentAction>();
	static Stack<CurrentAction> redoStack = new Stack<CurrentAction>();
	private boolean redo = false;
	private boolean undo = false;

	private CurrentAction myAction;
	private boolean setDefaultFont = true;
	private boolean fromEmpty = false;
	
	public TextEditor() {
		// set menu bar
	    menuBar = new JMenuBar();
	    fileM = new JMenu("File");
	    editM = new JMenu("Edit");
	    exitI = new JMenuItem("Exit");
	    undoI = new JMenuItem("Undo");
	    redoI = new JMenuItem("Redo");
	    setJMenuBar(menuBar);
	    menuBar.add(fileM);
	    menuBar.add(editM);
	    fileM.add(exitI);
	    editM.add(undoI);
	    editM.add(redoI);

		//create font panel: font, style, and size;
		JPanel facenamePanel = createComboBox();
		JPanel styleGroupPanel = createCheckBoxes();
		JPanel sizeGroupPanel = createRadioButtons();
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(3, 1));
		controlPanel.add(facenamePanel);
		controlPanel.add(styleGroupPanel);
		controlPanel.add(sizeGroupPanel);
		add(controlPanel, BorderLayout.NORTH);
		
		// create text area
		editor = new JTextArea();
		editor.setEditable(true);
		scpane = new JScrollPane(editor);
		scpane.setHorizontalScrollBar(null);
	    editor.setLineWrap(true);
	    editor.setWrapStyleWord(true);
		add(scpane, BorderLayout.CENTER);		
		setSize(900, 700);
		setLocation(150, 80);

		//create clear button
		clear = new JButton("clear");
		clear.addActionListener(new clearListener());
		JPanel panel = new JPanel();
		panel.add(clear);
		add(panel, BorderLayout.SOUTH);
		
		// built logic of exit
		ActionListener exitL = new ExitListener();
	    exitI.addActionListener(exitL);
	    // build logic of undo
	    undoI.addActionListener(new undoListener());
	    // redo 
	    redoI.addActionListener(new redoListener());
		// set default font
		String facename = (String) facenameCombo.getSelectedItem();
		int style = 0;
		if (italicCheckBox.isSelected()) {
			style = style + Font.ITALIC;
		}
		if (boldCheckBox.isSelected()) {
			style = style + Font.BOLD;
		}
		// Get font size
		int size = 24;
		final int f8 = 8;
		final int f16 = 16;
		final int f24 = 24;
		final int f32 = 32;
		final int f40 = 40;

		if (Button8.isSelected()) {
			size = f8;
		} else if (Button16.isSelected()) {
			size = f16;
		} else if (Button24.isSelected()) {
			size = f24;
		}else if (Button32.isSelected()) {
			size = f32;
		}else if (Button40.isSelected()) {
			size = f40;
		}

		editor.setFont(new Font(facename, style, size));
		editor.repaint();
	    
	}
	
	class ChoiceListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			//setLabelFont();
			if (undo || redo) {
				undo = false;
				redo = false;
			}else {
				if (setDefaultFont) {
					getCurrentAction("24");
					setDefaultFont = false;
				}
				else if (fromEmpty) {
					undoStack.push(myAction);
					getCurrentActionFromEmpty();
					fromEmpty = false;
				}
				undoStack.push(myAction);
			}
			
			// Get font name
			String facename = (String) facenameCombo.getSelectedItem();

			// Get font style
			int style = Font.PLAIN;
			if (italicCheckBox.isSelected()) {
				style = style + Font.ITALIC;
			}
			if (boldCheckBox.isSelected()) {
				style = style + Font.BOLD;
			}

			// Get font size
			int size = 24;

			final int f8 = 8;
			final int f16 = 16;
			final int f24 = 24;
			final int f32 = 32;
			final int f40 = 40;

			if (Button8.isSelected()) {
				size = f8;
			} else if (Button16.isSelected()) {
				size = f16;
			} else if (Button24.isSelected()) {
				size = f24;
			}else if (Button32.isSelected()) {
				size = f32;
			}else if (Button40.isSelected()) {
				size = f40;
			}

			editor.setFont(new Font(facename, style, size));	
			getCurrentAction(group.getSelection().getActionCommand());
		}
	}
	
	class ExitListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.exit(0);
		}
	}
	class undoListener implements ActionListener { // incompleted
		public void actionPerformed(ActionEvent event) {
			if (! undoStack.isEmpty()) {
				CurrentAction lastAction = undoStack.pop();
				undo = true;
				getCurrentAction(group.getSelection().getActionCommand());
				redoStack.push(myAction);
				undoRedo(lastAction);
				undo = false;
			}
		}
	}
	class redoListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (! redoStack.isEmpty()) {
				CurrentAction lastAction = redoStack.pop();
				redo = true;
				getCurrentAction(group.getSelection().getActionCommand());
				undoStack.push(myAction);
				undoRedo(lastAction);
				redo = false;
			}
		}
	}
	
	class clearListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String text = editor.getText();
			if ("".equals(text)) {
				return;
			}
			
			getCurrentAction(group.getSelection().getActionCommand());
			undoStack.add(myAction);
			editor.setText("");
			getCurrentAction(group.getSelection().getActionCommand());	
			//undoStack.add(myAction);	
			fromEmpty = true;
		}
		}

	public JPanel createComboBox() {
		facenameCombo = new JComboBox<String>();
		facenameCombo.addItem("Serif");
		facenameCombo.addItem("SansSerif");
		facenameCombo.addItem("Monospaced");
		facenameCombo.setEditable(true);
		facenameCombo.addActionListener(new ChoiceListener());

		JPanel panel = new JPanel();
		panel.add(facenameCombo);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Font"));
		return panel;
	}
	
	public JPanel createCheckBoxes() {
		italicCheckBox = new JCheckBox("Italic");
		italicCheckBox.addActionListener(new ChoiceListener());
		boldCheckBox = new JCheckBox("Bold");
		boldCheckBox.addActionListener(new ChoiceListener());

		JPanel panel = new JPanel();
		panel.add(italicCheckBox);
		panel.add(boldCheckBox);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Style"));
		return panel;
	}
	
	public JPanel createRadioButtons() {
		Button8 = new JRadioButton("8 pt.");
		Button8.setActionCommand("8");
		Button8.addActionListener(new ChoiceListener());
		
		Button16 = new JRadioButton("16 pt.");
		Button16.setActionCommand("16");
		Button16.addActionListener(new ChoiceListener());

		
		Button24 = new JRadioButton("24 pt.");
		Button24.setActionCommand("24");
		Button24.addActionListener(new ChoiceListener());

		
		Button32 = new JRadioButton("32 pt.");
		Button32.setActionCommand("32");
		Button32.addActionListener(new ChoiceListener());

		
		Button40 = new JRadioButton("40 pt.");
		Button40.setActionCommand("40");
		Button40.addActionListener(new ChoiceListener());

		Button24.setSelected(true);
		
		group = new ButtonGroup();
		group.add(Button8);
		group.add(Button16);
		group.add(Button24);
		group.add(Button32);
		group.add(Button40);

		JPanel panel = new JPanel();
		panel.add(Button8);
		panel.add(Button16);
		panel.add(Button24);
		panel.add(Button32);
		panel.add(Button40);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Size"));

		return panel;
	}
	
    static void UndoFunc(Stack<CurrentAction> undoStack, Stack<CurrentAction> redoStack)
    {
    	if(! undoStack.isEmpty()) {
    	CurrentAction X = (CurrentAction)undoStack.pop();
    	undoStack.pop();
    	redoStack.push(X);
    	}
    }
   
    static void RedoFunc(Stack<CurrentAction> undoStack, Stack<CurrentAction> redoStack)
    {
    	if(! redoStack.isEmpty()) {
    	CurrentAction X = (CurrentAction)redoStack.pop();
    	redoStack.pop();
    	redoStack.push(X);
    	}
    }
    
	private void getCurrentAction(String num) {		
		myAction = new CurrentAction();
		myAction.setFace((String)facenameCombo.getSelectedItem());
		myAction.setItalicCheck(italicCheckBox.isSelected());
		myAction.setBoldCheck(boldCheckBox.isSelected());
		myAction.setSize(num);
		myAction.setText(editor.getText());
	}
	
	private void getCurrentActionFromEmpty() {
		Font currentFont = editor.getFont();
		String face = currentFont.getFamily();	
		boolean italicCheck = false, boldCheck = false;
		
		if (currentFont.isItalic()) {
			italicCheck = true;
		}
		if (currentFont.isBold()) {
			boldCheck = true;
		}	
		String size = group.getSelection().getActionCommand();
		String txt = editor.getText();
		
		myAction = new CurrentAction();
		myAction.setFace(face);
		myAction.setItalicCheck(italicCheck);
		myAction.setBoldCheck(boldCheck);
		myAction.setSize(size);
		myAction.setText(txt);
	}

	/*
	 * For implementing the logic of Undo and Redo function, I actually was
	 * struggling with it for a long time. I did not know what the most proper way is
	 * so the tutor reminded me to add stacks, change the stack and its current action,
	 *  and finally I completed it.
	 */
	private void undoRedo(CurrentAction myAction) {
		if (myAction != null) {
		String font = (String)facenameCombo.getSelectedItem(); // check the font of the current action
		if (!font.equals(myAction.getFont())) {
			facenameCombo.setSelectedItem(myAction.getFont());
			font = (String)facenameCombo.getSelectedItem();
		}
		
		boolean italicCheck = italicCheckBox.isSelected();	// check the style of the current action
		if (italicCheck != myAction.getItalicCheck()) {
			italicCheckBox.setSelected(myAction.getItalicCheck());
			italicCheck = italicCheckBox.isSelected();
		}
		
		boolean boldCheck = boldCheckBox.isSelected(); //// check the bold of the current action
		if (boldCheck != myAction.isBoldCheck()) {
			boldCheckBox.setSelected(myAction.isBoldCheck());
			boldCheck = boldCheckBox.isSelected();
		}
		
		String size = group.getSelection().getActionCommand();	// check the size of the current action
		if (!size.equals(myAction.getSize())) {	
			Enumeration<AbstractButton> elements = group.getElements();
		
			while (elements.hasMoreElements()) {
				AbstractButton next = elements.nextElement();
				String actionCommand = next.getActionCommand();
				if (actionCommand.equals(myAction.getSize())) {	
					next.setSelected(true);	
					break;
				}
			}
		}
		
		String txt = editor.getText();
		if (!txt.equals(myAction.getText())) {
			editor.setText(myAction.getText()); // check the text of the current action
		}
		int style = Font.PLAIN;
		if (italicCheck) {
			style = style + Font.ITALIC;
		}	
		if (boldCheck) {
			style = style + Font.BOLD;
		}
		editor.setFont(new Font(font, style, Integer.parseInt(myAction.getSize())));
		if (undo) {
			undo = false;
		}
		if (redo) {
			redo = false;
		}	
		}
	}

	public static void main(String[] args) {
		JFrame frame = new TextEditor();
		frame.setTitle("Hanting Li-23253132");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
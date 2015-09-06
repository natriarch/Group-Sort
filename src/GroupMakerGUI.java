import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

//NOV 2014 - Sorts a list of students into groups.
public class GroupMakerGUI extends JPanel implements ActionListener{
	
	private String[] columnNames = {"Last Name", "First Name", "Sex", "Class Avg"};
	private ArrayList<Student> studentList;
	private int classSize;
	private int classAvg;
	private int numGroups;
	private ArrayList<ArrayList<Student>> groupList;
	private int groupSelection;
	private JPanel groupPane;
	private JScrollPane groupScrollPane;
	private JButton groupMakerButton;
	
	public GroupMakerGUI(int numStuds, int Grps) {
//		groupSelection = 0;
		classSize = numStuds;
		classAvg = 0;
		studentList = new ArrayList<Student>(numStuds);
		System.out.println(studentList.size());
		for (int i = 0; i < classSize; i ++){
			studentList.add(new Student()); 
			classAvg += (int) studentList.get(i).getData()[Student.AVG];
		}
		
		Collections.sort(studentList);
		classAvg = classAvg/numStuds;
		numGroups = Grps;
		groupList = new ArrayList<ArrayList<Student>>(numGroups);
		for (int i = 0; i < numGroups; i ++){
			ArrayList<Student> s = new ArrayList<Student>();
			groupList.add(s);
		}
		//GUI stuff
		groupMakerButton = new JButton(new AbstractAction("Group 'em") {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				makeGroups();				
			}
			
		});
	
		this.add(groupMakerButton);
		JTable table = new JTable(new StudTableModel());
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Student List"));
		JPanel studPane = new JPanel();
		studPane.add(scrollPane);
		this.setLayout(new FlowLayout());
		this.add(studPane);
		
		
		groupPane = new JPanel();
		groupPane.setLayout(new BoxLayout(groupPane, BoxLayout.Y_AXIS));
	}
	
	public void makeDumbGroups() {
		int isPerfect = 0;
		int runCount = 0;
		groupList.clear();
		for (int i = 0; i < numGroups; i ++) {
			ArrayList<Student> s = new ArrayList<Student>(); 
		}
		if (studentList.size() % groupList.size() != 0){
			isPerfect = 1;
		}
		while (runCount < studentList.size()/groupList.size() + isPerfect) {
			
		}
		
	}
	
	
	public void makeGroups() {
		
		groupList.clear();
		for (int i = 0; i < numGroups; i ++){
			ArrayList<Student> s = new ArrayList<Student>();
			groupList.add(s);
		}
		
		int studListFrame = 0;   
		int runCount = 0;
		int isPerfect = 0;
		
		// becomes 1 if an extra run is needed due to classSize % groupSize != 0. This value later added to for loop to achieve the correct loop range.
		if (studentList.size() % groupList.size() != 0){
			isPerfect = 1;
		}
		
		boolean remainderRun = false;
		
		//right side of expression - must add one run for remaining students if class size is not perfectly divisible
		while (runCount < studentList.size()/groupList.size() + isPerfect) {
			
			//alternate between this if/else to get students from different ends of the classList 
			//ie good avg, bad avg if the list is sorted
			//every even run gets students from front to back of classList
			if (runCount % 2 == 0) {
				
				//perform one more run to account for remainder students if necessary
				if(remainderRun) {
					for (int i = 0; i < studentList.size() % groupList.size(); i ++){
						groupList.get(i).add(studentList.get(i + studListFrame));
					}
				}
				else {
					for (int i = 0; i < groupList.size(); i ++){
						groupList.get(i).add(studentList.get(i + studListFrame));
					}
				}
			}
			//use this loop if on an odd numbered run 
			//gets students from back to front
			else {
				if(remainderRun) {
					for (int i = 0; i < studentList.size() % groupList.size(); i ++){
						groupList.get(i).add(studentList.get(studentList.size() - i - studListFrame - 1));
					}
				}
				else {
					for (int i = 0; i < groupList.size(); i++) {
					groupList.get(i).add(studentList.get(studentList.size() - i - studListFrame - 1));
					}
				}
				if (runCount == studentList.size() / groupList.size() && studentList.size() % groupList.size() != 0) {
					studListFrame += (studentList.size() % groupList.size());
				}
				else {
					studListFrame += groupList.size();
				}
			}
			runCount++;
			
			//if next run is the last run AND that run is for excess students - force method into modified for loop using remainderRun to add these students
			if ((runCount == studentList.size()/groupList.size() + isPerfect - 1) && isPerfect == 1) {
				remainderRun = true;
			}
			
		}
		runCount = 0;
		studListFrame = 0;
		isPerfect = 0; 
		
		//add group tables to the window
		groupSelection = 0;
		while (groupSelection < groupList.size()){
			JTable gTable = new JTable(new GroupTableModel(groupSelection));
			JPanel gTablePane = new JPanel();
			gTablePane.add(gTable);
			gTablePane.setBorder(BorderFactory.createTitledBorder("Group" + (groupSelection + 1)));
			groupPane.add(gTablePane);
			groupPane.add(Box.createVerticalStrut(10));
			groupSelection++;
		}
		//groupPane.setPreferredSize(getPreferredSize());
		groupScrollPane = new JScrollPane(groupPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(groupScrollPane);
		this.validate(); 
		this.repaint(); 
	}
	
	public void printGroups() {
		for (int i = 0; i < groupList.size(); i++){
			for (int j = 0; j < groupList.get(i).size(); j++) {
				groupList.get(i).get(j).printInfo();
			}
			System.out.println("--------------------------");
		}
	}
	
	public void printGroupAverages(){
		
		for (int i = 0; i < groupList.size(); i++) {
			int avg = 0;
			for (int j = 0; j < groupList.get(i).size(); j++) {
				avg += groupList.get(i).get(j).getAverage();
			}
			avg = avg / groupList.get(i).size();
			System.out.println(avg);
		}
	}

	class StudTableModel extends AbstractTableModel {
		
		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return studentList.size();
		}
		
		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return columnNames.length;
		}
		
		@Override
		public String getColumnName(int col) {
			return columnNames[col];
		}
		
		@Override
		public Object getValueAt(int row, int col) {
			// TODO Auto-generated method stub
			return studentList.get(row).getData()[col];
		}
		
		@Override
		public void setValueAt(Object value, int row, int col) {
			studentList.get(row).getData()[col] = value; 			
		}
		
		@Override
		public boolean isCellEditable(int row, int col){
			return true;
		}
		
//		public Class getColumnClass(int c) {
//			return getValueAt(0, c).getClass();
//		}
	}
	
	class GroupTableModel extends AbstractTableModel {
		
		private int gSelection;
		public GroupTableModel(int gSelect){
			super(); 
			gSelection = gSelect;
		}
		@Override
		public int getRowCount() {
			return groupList.get(gSelection).size();
		}

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return columnNames.length;
		}
		@Override
		public String getColumnName(int col) {
			return columnNames[col];			
		}

		@Override
		public Object getValueAt(int row, int col) {
			return groupList.get(gSelection).get(row).getData()[col];
		}
	
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GroupMakerGUI content = new GroupMakerGUI(21, 8);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(new JMenu("hello"));
		menuBar.setVisible(true);
		
		content.setSize(1024,768);
		content.add(menuBar);
		content.setOpaque(true);
		JFrame frame2 = new JFrame();
		JFrame frame = new JFrame();
		frame2.pack();
		frame2.setVisible(true);
		frame.setLayout(new FlowLayout());
		frame.setSize(1024,768);
		frame.setContentPane(content);
		frame.pack();
		frame.setTitle("GroupMaker!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}

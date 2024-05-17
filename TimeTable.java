import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TimeTable extends JFrame implements ActionListener {

	private JPanel screen = new JPanel(), tools = new JPanel();
	private JButton tool[];
	private JTextField field[];
	private CourseArray courses;
	private Color CRScolor[] = {Color.RED, Color.GREEN, Color.BLACK};
	private Autoassociator autoassociator;
	
	public TimeTable() {
		super("Dynamic Time Table");
		setSize(800, 800);
		setLayout(new FlowLayout());
		
		screen.setPreferredSize(new Dimension(400, 800));
		add(screen);
		
		setTools();
		add(tools);
		
		setVisible(true);
	}
	
	public void setTools() {
		String capField[] = {"Slots:", "Courses:", "Clash File:", "Iters:", "Shift:"};
		field = new JTextField[capField.length];
		
		String capButton[] = {"Load", "Start", "Step", "Print", "Exit", "Continue"};
		tool = new JButton[capButton.length];
		
		tools.setLayout(new GridLayout(2 * capField.length + capButton.length, 1));
		
		for (int i = 0; i < field.length; i++) {
			tools.add(new JLabel(capField[i]));
			field[i] = new JTextField(5);
			tools.add(field[i]);
		}
		
		for (int i = 0; i < tool.length; i++) {
			tool[i] = new JButton(capButton[i]);
			tool[i].addActionListener(this);
			tools.add(tool[i]);
		}
		
		field[0].setText("17");
		field[1].setText("381");
		field[2].setText("lse-f-91.stu");
		field[3].setText("1");
	}
	
	public void draw() {
		Graphics g = screen.getGraphics();
		int width = Integer.parseInt(field[0].getText()) * 20;
		for (int courseIndex = 1; courseIndex < courses.length(); courseIndex++) {
			g.setColor(CRScolor[courses.status(courseIndex) > 0 ? 0 : 1]);
			g.drawLine(0, courseIndex, width, courseIndex);
			g.setColor(CRScolor[CRScolor.length - 1]);
			g.drawLine(20 * courses.slot(courseIndex), courseIndex, 20 * courses.slot(courseIndex) + 20, courseIndex);
		}
	}
	
	private int getButtonIndex(JButton source) {
		int result = 0;
		while (source != tool[result]) result++;
		return result;
	}
	
	public void actionPerformed(ActionEvent click) {
		int min = Integer.MAX_VALUE;
		int step = 0;
		int clashes;
		
		switch (getButtonIndex((JButton) click.getSource())) {
		case 0:
			int slots = Integer.parseInt(field[0].getText());
			courses = new CourseArray(Integer.parseInt(field[1].getText()) + 1, slots);
			courses.readClashes(field[2].getText());

			// custom code alert! adding associator and training it
			this.autoassociator = new Autoassociator(courses);
			//train();
	
			draw();
			break;
		case 1:
			step = 0;
			if (field[4].getText().isEmpty() == false) {
				for(int p = 1; p < courses.length(); p++) {courses.setSlot(p, 0);}
				for(int q = 1; q <= Integer.parseInt(field[3].getText()); q++) {
				makeAStep(min, step, field[3].getText(), field[4].getText());
				

				
				
				for (int i = 1; i<courses.length(); i++) {
					int[] currentTimeslot = courses.getTimeSlot(courses.slot(i));
					
					int newNode = autoassociator.unitUpdate(currentTimeslot);
					
					if (newNode != courses.slot(i)) courses.setSlot(i, newNode);
					
				}
				draw();	
				
                clashes = courses.clashesLeft();
                if (min > clashes) {
					step = q;
                    min = clashes;
                    }
				
				}
			}	else {
				System.out.println("Empty");
			}
			break;
		case 2:
			courses.iterate(Integer.parseInt(field[4].getText()));
			draw();
			break;
		case 3:
			System.out.println("Exam\tSlot\tClashes");
			for (int i = 1; i < courses.length(); i++)
				System.out.println(i + "\t" + courses.slot(i) + "\t" + courses.status(i));
			break;
		case 4:
			System.exit(0);
			break;
		case 5:
		if (field[4].getText().isEmpty() == false) 
			makeAStep(min, step, field[3].getText(), field[4].getText());
		else 
			System.out.println("Empty.");
		}
		
	}

	public static void main(String[] args) {
		new TimeTable();
	}

	public void train(){
		for(int p =0; p<Integer.parseInt(field[0].getText()); p++) {
			int[] dataToTrainWith =courses.getTimeSlot(p);
			// HERE we check if there are any clashes
			boolean noClashes = true;
			
			for(int q = 0; q<dataToTrainWith.length;q++)
				if(dataToTrainWith[q]==1)if(courses.maxClashSize(q)>0) noClashes = false;
			if(noClashes == true) autoassociator.training(dataToTrainWith);


		}
	}

	private void makeAStep(int minimalSteps, int currentStep, String iterationCount, String shiftCount) {
		int numberOfClashes;
		
		for (int p = 1; p <= Integer.parseInt(iterationCount); p++) {
			courses.iterate(Integer.parseInt(shiftCount));
		
			for (int i = 1; i<courses.length(); i++) {
				int[] currentTimeslot = courses.getTimeSlot(courses.slot(i));
				
				int newNode = autoassociator.unitUpdate(currentTimeslot);
				
				if (newNode != courses.slot(i)) courses.setSlot(i, newNode);
				
			}
			draw();	
			draw();
			numberOfClashes = courses.clashesLeft();
			if(minimalSteps>numberOfClashes) {
				currentStep = p;
				minimalSteps = numberOfClashes;
			}
		}
		
		
		System.out.println("Shift = " + shiftCount + "\tMin clashes = " + minimalSteps + "\tat step " + currentStep%17);
		setVisible(true);
	}

	
}

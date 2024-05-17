public class Autoassociator {
	private int weights[][];
	private int trainingCapacity;
	private int courseCount;
	
	public Autoassociator(CourseArray courses) {
		trainingCapacity = 0;
		courseCount = courses.length();

		// initializaing the weight matrix
		weights = new int[courseCount][courseCount];
		
		// making all the initial weights zero cuz thats how they should be
		for(int i=0;i<courseCount;i++)
			for(int j=0;j<courseCount;j++)
				weights[i][j]=0;
	}
	
	public int getTrainingCapacity() {

		return trainingCapacity;
	}
	
	public void training(int pattern[]) {
		for(int i =0; i<courseCount; i++)
		for(int j = 0;j<courseCount;j++)
			if(i == j) continue;
			else weights[i][j] += (int)(-0.5+pattern[j])*2*(-0.5+pattern[i]);
		trainingCapacity = trainingCapacity + 1;
	}
	
	public int unitUpdate(int neurons[]) {
		int randomlySelectedNeuron = (int)(courseCount*Math.random());

		int count =0;
		for(int p = 0; p<courseCount;p++)
			if(p == randomlySelectedNeuron) continue;
			else count+=neurons[p]*weights[randomlySelectedNeuron][p];
		
		if(count>0) neurons[randomlySelectedNeuron]=1;
		else neurons[randomlySelectedNeuron]=-1;
		return randomlySelectedNeuron;
	}
	
	public void unitUpdate(int neurons[], int index) {
		int count =0;
		for(int p = 0; p<courseCount;p++)
			if(p == index) continue;
			else count+=neurons[p]*weights[index][p];
		
		if(count>0) neurons[index]=1;
		else neurons[index]=-1;
	}
	
	public void chainUpdate(int neurons[], int steps) {
		for(int p=0; p<courseCount;p++)
			unitUpdate(neurons);
	}
	
	public void fullUpdate(int neurons[]) {
		boolean isItStable;

		isItStable=true;
		for(int p=0;p<courseCount;p++){
			int previous = neurons[p];
			unitUpdate(neurons,p);
			if(previous != neurons[p]) {
				isItStable = false;
			}
		}

		while(isItStable == false) {
			isItStable=true;
			for(int p=0;p<courseCount;p++){
				int previous = neurons[p];
				unitUpdate(neurons,p);
				if(previous != neurons[p]) {
					isItStable = false;
				}
			}
		}

	}
}

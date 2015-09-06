import java.util.Random;

public class Student implements Comparable<Student> {
	private Object[] studData;
	static final int LAST_NAME = 0;
	static final int FIRST_NAME = 1;
	static final int GENDER = 2;
	static final int AVG = 3;
	private final char[] chars = {'A','B','C','D','E','F','G','H','I','J','K','L','M',
								  'N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	private final char[] sexList = {'f', 'm'};
	private final int randomStringLength = 6;
	
	public Student(String firstname, String lastname, String sex, int avg) {
		studData = new Object[] {lastname, firstname, sex, avg}; 
	}
	
	//Debug constructor
	public Student() {
		studData = new Object[4];
		Random r = new Random();
		int average = r.nextInt((100-30) + 1) + 30;
		char sex = sexList[r.nextInt(2)];
		studData[FIRST_NAME] = randomName();
		studData[LAST_NAME] = randomName();
		studData[GENDER] = sex;
		studData[AVG] = average;
	}
	
	
	
	public Object[] getData() {
		return studData;
	}
	
	public String randomName() {
		String randomName = "";
		Random r = new Random(); 
		for (int i = 0; i < randomStringLength; i ++){
			randomName += chars[r.nextInt(26)];
		}
		return randomName;
	}
	
	public int getAverage(){
		return (int)studData[AVG];
	}

	public int compareTo(Student compareStudent) {
		int compareAvg = compareStudent.getAverage();
		return compareAvg - this.getAverage();
	}
	
	public void printInfo() {
		System.out.println(studData[AVG]);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

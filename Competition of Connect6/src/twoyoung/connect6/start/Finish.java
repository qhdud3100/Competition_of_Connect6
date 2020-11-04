// 대국이 승부조건을 충족하여 완료된 경우 출력되는 페이지
// 이것도 기능의 동작과 관련된 부분에 있어서는 우선순위가 낮은 파트임

package twoyoung.connect6.start;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;
import java.awt.event.ActionEvent;

public class Finish extends JFrame implements KeyListener{

	private static final boolean append = false;
	public String blackName;
	public String whiteName;
	private JPanel contentPane;
	private JTextField whiteField;
	private JTextField blackField;
	private int winnerColor;
	private JLabel winnerLabel;
	public final int WHITE=3;
	public final int BLACK=4;
	private JButton checkButton;
	public Vector<Record> recordVector= new Vector<Record>();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Finish frame = new Finish();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public class Record{
		String name;
		int winCount=0;
		int loseCount=0;
	}
	
	
	public void nameEntered(){
		
		blackName=blackField.getText();
		whiteName=whiteField.getText();
		
		/*
		 * 
		 * 읽어오기 
		 * 
		 */
		Scanner inputStream = null; 
		try {
			inputStream = new Scanner(new File("record.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("파일을 찾을 수 없습니다. ");
		}
		
		
		while(inputStream.hasNextLine()) { // 벡터에 전부 옮겨 넣는다.
			String [] word = inputStream.nextLine().split(" ");
			
			Record tmp = new Record();
			
			tmp.name=word[0];
			tmp.winCount=Integer.parseInt(word[1]);
			tmp.loseCount=Integer.parseInt(word[2]);
			
			recordVector.add(tmp);
		}

		
//		System.out.println("winner color is "+ winnerColor);
		
		
		if(countingBlack()==0) { // 흑돌 이름 못찾았을때
//			System.out.println("흑돌 플레이어의 이름을 못찾았습니다. 새로 한 줄을 만듭니다.");
			Record tmp = new Record();
			tmp.name=blackName;
			
			if(winnerColor==BLACK) {
				tmp.winCount=1;
			}else if(winnerColor==WHITE) {
				tmp.loseCount=1;
			}
			recordVector.add(tmp);
//			printVector();
		}
		
		if(countingWhite()==0) { // 백돌 이름 못찾았을때
//			System.out.println(" 백돌 플레이어의 이름을 못찾았습니다. 새로 한줄을 만듭니다. ");
			
			Record tmp = new Record();
			tmp.name=whiteName;
			
			if(winnerColor==BLACK) {
				tmp.loseCount=1;
			}else if(winnerColor==WHITE) {
				tmp.winCount=1;
			}
			recordVector.add(tmp);
//			printVector();
		}
		
	
		
		/*
		 * 
		 * 파일에 기록하기
		 * 
		 */
		PrintWriter outputStream=null;
		try {
			outputStream = new PrintWriter(new FileOutputStream("record.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("파일을 찾을 수 없습니다. ");
		}
		
		for(int i=0;i<recordVector.size();i++) {
			Record tmp = recordVector.get(i);
			outputStream.printf("%s %d %d\n",tmp.name, tmp.winCount, tmp.loseCount);
		}
		
		
		outputStream.close();
		

		JOptionPane.showMessageDialog(null, "기록되었습니다.", "안내", JOptionPane.WARNING_MESSAGE);
		new Start();
		dispose();
	}
	
	
	
	
	public int countingBlack() { // 흑돌, 백돌 이름 찾아서 승, 패 카운팅++

		for(int i=0;i<recordVector.size();i++) { 
			
			Record tmp =recordVector.get(i);
			
			if(blackName.equals(tmp.name)) {
				
				if(winnerColor==BLACK) {
					tmp.winCount++;
				}else if(winnerColor==WHITE) {
					tmp.loseCount++;
				}
				return 1;
			}
		}
		return 0;
	}
	
	
	public int countingWhite() {
		
		for(int i=0;i<recordVector.size();i++) { 
			Record tmp =recordVector.get(i);
			if(whiteName.equals(tmp.name)) {
				if(winnerColor==WHITE) {
					tmp.winCount++;
				}else if(winnerColor==BLACK) {
					tmp.loseCount++;
				}
				return 1;
			}
		}
		return 0;
	}
	
	
	
	public Finish(int winnerColor) { // 승리자 라벨 setText 
		this();
		this.winnerColor=winnerColor;

		if(winnerColor==BLACK) winnerLabel.setText("흑돌 승리 !");
		else if(winnerColor==WHITE) winnerLabel.setText("백돌 승리 !");
	}

	
	public void printVector() { //벡터를 뽑아보자
		for(int i=0;i<recordVector.size();i++) {
			Record tmp = recordVector.get(i);
			System.out.printf("name : %s winCount: %d loseCount: %d", tmp.name, tmp.winCount, tmp.loseCount);
		}
	}
	
	
	/**
	 * 프레임
	 */
	public Finish() {
	

		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 300);
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("Button.select"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null);
		setResizable(false);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		//승리자 라벨 
		winnerLabel = new JLabel("");
		winnerLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 30));
		winnerLabel.setBounds(133, 32, 137, 36);
		contentPane.add(winnerLabel);
		

		JLabel whiteLabel = new JLabel("백돌:");
		whiteLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		whiteLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		whiteLabel.setBounds(102, 145, 57, 30);
		contentPane.add(whiteLabel);
		
		whiteField = new JTextField();
		whiteField.setForeground(UIManager.getColor("Button.disabledText"));
		whiteField.setText("홍길동");
		whiteField.setBounds(171, 146, 130, 31);
		contentPane.add(whiteField);
		whiteField.setColumns(10);
		whiteField.addKeyListener(this);
		
		JLabel blackLabel = new JLabel("흑돌:");
		blackLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		blackLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		blackLabel.setBounds(102, 103, 57, 30);
		contentPane.add(blackLabel);
		
		blackField = new JTextField();
		blackField.setText("윤보영");
		blackField.setForeground(UIManager.getColor("Button.disabledText"));
		blackField.setColumns(10);
		blackField.setBounds(171, 103, 130, 31);
		contentPane.add(blackField);
		blackField.addKeyListener(this);
		
		JButton recordButton = new JButton("기록하기");
		recordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nameEntered();
			}
		});
		recordButton.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		recordButton.setBounds(82, 218, 117, 36);
		contentPane.add(recordButton);
		
		checkButton = new JButton("검토하기");
		checkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		checkButton.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		checkButton.setBounds(204, 218, 117, 36);
		contentPane.add(checkButton);
		

		
		
		setVisible(true);
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	     int key = e.getKeyCode();
	     
	     if (key == KeyEvent.VK_ENTER) {

	    	 nameEntered();
	    	 
	        }
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}

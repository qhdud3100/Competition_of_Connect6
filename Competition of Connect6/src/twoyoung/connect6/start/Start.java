package twoyoung.connect6.start;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Start extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JFrame mainFrame;
	Play frame;
	public final int WHITE=3; // 백돌을 의미하는 상수
	public final int BLACK=4; // 흑돌을 의미하는 상수
	
	// 승부 기록에 대한 값을 저장하는 내부 클랙스
	public class Record {
		String name;
		int winCount=0;
		int loseCount=0;
	}
	
	// 프로그램 실행
	// 스레드를 같이 실행함
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Start frame = new Start(); // 생성자를 실행하기 위한 부분
					frame.startGame();
				} catch (Exception e) {
					e.printStackTrace(); // 정상적으로 프로그램이 실행되지 않은 경우에 대한 예외처리
				}
			}
		});
	}
	
	// 경기시작을 위한 페이지를 생성하는 부분
	// 1. 흑 시작하기
	// 2. 백 시작하기
	// 3. 랭킹보기
	// 4. 종료하기
	// -> 다음 4가지 옵션을 띄워주는 창으로 시작함
	// constructor Start 선언
	public void startGame()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 윈도우 창 종료시 프로세스까지 종료시킴
		setBounds(100, 100, 1200, 700); // 애플리케이션 윈도우를 생성할 위치와 그 크기를 결정
		contentPane = new JPanel(); // 내용을 담기 위한 컨테이너 생성
		contentPane.setBackground(Color.PINK); // 내용을 담는 패널의 배경색상을 Pink로 설정
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // 평면효과를 사용한 EmptyBorder사용
		setContentPane(contentPane);
		contentPane.setLayout(null); // 레이아웃을 설정
		
		JButton startBtn_1 = new JButton("흑 시작하기");
		startBtn_1.setFont(new Font("Lucida Grande", Font.PLAIN, 40)); // 버튼에 대한 디자인 설정
		startBtn_1.setBounds(400, 104, 400, 70); // 버튼의 위치지정
		contentPane.add(startBtn_1);
		startBtn_1.addActionListener(this); // 액션 리스너 지정
		
		JButton startBtn_2 = new JButton("백 시작하기");
		startBtn_2.setFont(new Font("Lucida Grande", Font.PLAIN, 40)); // 버튼에 대한 디자인 설정
		startBtn_2.setBounds(400, 200, 400, 70); // 버튼의 위치지정
		contentPane.add(startBtn_2);
		startBtn_2.addActionListener(this); // 액션 리스너 지정
		
		JButton showBtn = new JButton("랭킹보기");
		showBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 40)); // 버튼에 대한 디자인 설정
		showBtn.setBounds(400, 300, 400, 70); // 버튼의 위치지정
		contentPane.add(showBtn);
		showBtn.addActionListener(this); // 액션 리스너 지정
		
		JButton exitBtn = new JButton("종료하기");
		exitBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 40)); // 버튼에 대한 디자인 설정
		exitBtn.setBounds(400, 400, 400, 70); // 버튼의 위치 지정
		contentPane.add(exitBtn);
		exitBtn.addActionListener(this); // 액션리스너 지정
		
		setLocationRelativeTo(null); // 해상도에 지장을 받지않고 애플리케이션 윈두우가 화면의 중앙에 나타날 수 있도록 함
		setResizable(false); // 애플리케이션 윈도우의 사이즈를 사용자가 임의로 조정할 수 없도록 함
		setVisible(true);
	}
	
	// 액션 리스너
	@Override
	public void actionPerformed(ActionEvent e)
	{
		String button = e.getActionCommand(); // 컴포넌트의 텍스트값을 가져옴
		
		if(button.equals("흑 시작하기")) {
			mainFrame = new JFrame();
			frame = new Play(BLACK, WHITE);
			mainFrame.getContentPane().add(frame);
			mainFrame.setVisible(true);

			mainFrame.setResizable(false);
			mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			mainFrame.setContentPane(frame);
			mainFrame.setBounds(100, 100, 1200, 700);
			mainFrame.setLocationRelativeTo(null);
			dispose(); // Start를 위한 윈도우 종료
		}
		else if(button.equals("백 시작하기")) {
			mainFrame = new JFrame();
			frame = new Play(WHITE, BLACK);
			mainFrame.getContentPane().add(frame);
			mainFrame.setVisible(true);

			mainFrame.setResizable(false);
			mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			mainFrame.setContentPane(frame);
			mainFrame.setBounds(100, 100, 1200, 700);
			mainFrame.setLocationRelativeTo(null);
			dispose(); // Start를 위한 윈도우 종료
		}
		else if(button.equals("랭킹보기")) {
			new Rank(); // Rank를 위한 윈도우 실행
			dispose(); // Start를 위한 윈도우 종료
		}
		else if(button.equals("종료하기")) {
			System.exit(-1); // 프로세스 종료
		}
		else {
			System.out.println("Maching 되는 값이 없음. 실행할 수 없음");
		}
	}
}
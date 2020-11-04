// 경기를 시작하는 페이지를 출력하는 부분

package twoyoung.connect6.start;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Stack;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;


public class Play extends JPanel implements ActionListener, MouseListener{
	
	private final int startX=580; // 바둑판 좌측상단의 x좌표
	private final int startY=80;  // 바둑판 좌측상단의 y좌표
	private final int endX=1120;  // 바둑판 우측하단의 x좌표 
	private final int endY=620;	  // 바둑판 우측하단의 y좌표
	public final int NONE=0;  // 착수된 돌이 없음을 의미하는 상수
	public final int RED=1;   // 적돌이 착수되었음을 의미하는 상수
	public final int WHITE=3; // 백돌이 착수되었음을 의미하는 상수
	public final int BLACK=4; // 흑돌이 착수되었음을 의미하는 상수
	private final int PREV=5; // 착수 이전임을 의미하는 상수
	private final int SET=6;  // 착수를 의미하는 상수
	private final int AI=7;   // AI에 의한 착수를 의미하는 상수
	private final int TIME_LIMIT=60; // 돌을 착수하기까지 부여되는 제한시간 - 일단 60초로 설정한 상황

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;	
	private JButton giveupBtn; // 기권버튼
	private JButton setBtn;    // 착수버튼
	
	private int pointX; // 접근하는 지점의 x좌표
	private int pointY; // 접근하는 지점의 y좌표
	private int arrayX; // 2차원 배열 상에서 착수정보를 저장할 인덱스의 x좌표
	private int arrayY; // 2차원 배열 상에서 착수정보를 저장할 인덱스의 y좌표
	// private int redCount; // 착수된 적돌의 갯수
	private boolean first = true; // 현재 순서가 맨 처음인지에 대한 참, 거짓을 나타냄

	public int myColor;    // 내가 플레이하는 돌의 색상
	public int enemyColor; // 상대편 돌의 색상
	
	private int clickOption;
	private int colorOption=RED; // 현재 순서에 해당하는 돌의 색상
	private boolean count=false; // 2개의 돌이 착수되었는 지에 대한 참, 거짓을 나타냄
	
	private Timer timer; // 제한시간 값을 설정하기 위한 타이머
	private int time_limit; // 착수하기까지 걸려있는 제한시간
	
	private int prepointX=0 ; // 더블클릭을 위한 부분 - 1
	private int prepointY=0 ; // 더블클릭을 위한 부분 - 2
	
	Stack <Point> pointStack= new Stack<Point>(); // 착수정보를 저장하는 stack -> 페이지를 다시 그리기 위한 부분
	public int [][] colorArray = new int [19][19]; // 대국을 진행하는 바둑판에 대한 착수정보
	private JButton homeButton; // 진행중인 대국을 취소하고 대국을 종료하는 버튼
	
	private static JFrame mainFrame;
	
	// 각 좌표에 대한 착수정보를 표현하는 내부클래스
	public class Point
	{ 
		public int beforeX; // 착수하는 지점의 x좌표
		public int beforeY; // 착수하는 지점의 y좌표
		public int beforecolorOption; // 착수하는 돌의 색상
		
		// 전달받은 3개의 인자값으로 각 data field를 초기화하는 constructor
		public Point(int x, int y, int c) {
			this.beforeX=x;
			this.beforeY=y;
			this.beforecolorOption=c;
		}
	}
	
	// play 파트가 실행되는 부분
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainFrame = new JFrame();
					Play frame = new Play(); // play를 위한 객체를 생성
					mainFrame.getContentPane().add(frame);
					mainFrame.setVisible(true);
					mainFrame.setResizable(false); // 사용자가 윈도우의 사이즈를 임의로 조정할 수 없도록 함
					mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					mainFrame.setContentPane(frame);
					mainFrame.setBounds(100, 100, 1200, 700); // 출력할 윈도우의 사이즈를 지정
					mainFrame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// 대국페이지에 대한 constructor - 1
	// 사용자에게 보여지는 대국과 관련된 페이지를 생성하는 부분(프론트엔드)
	public Play() {
		addMouseListener(this);		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setBackground(Color.PINK); // 배경색상을 pink로 설정
		this.setLayout(null); // 레이아웃을 설정
			
		//대국을 취소하고 다시 시작화면으로 돌아가기 위한 부분
		homeButton = new JButton("Home");
		homeButton.setBounds(6, 6, 67, 29); // home 버튼의 위치를 좌표로 지정
		this.add(homeButton); // home 버튼을 패널에 추가
		homeButton.addActionListener(this); // 액션리스너 할당
			
		// 대국화면 상단에 메시지를 표시하는 부분
		JLabel timerLabel = new JLabel("화이팅~");
		timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		timerLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 30));
		timerLabel.setBounds(692, 6, 328, 36); // 버튼의 위치와 크기를 지정
		add(timerLabel);
			
		// AI가 판단하여 돌을 착수하는 부분
		JButton AIBtn = new JButton("AI 착수");
		AIBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 40));
		AIBtn.setBounds(81, 100, 400, 70); // 버튼의 위치와 크기를 지정
		this.add(AIBtn);
		AIBtn.addActionListener(this);
			
		// 적돌의 착수를 마친 후 경기를 시작하는 버튼
		JButton startBtn = new JButton("시작하기");
		startBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 40));
		startBtn.setBounds(81, 190, 400, 70); // 버튼의 위치와 크기를 지정
		this.add(startBtn);
		startBtn.addActionListener(this);
			
		// 현 대국을 포기하고 기권하는 버튼
		giveupBtn = new JButton("기권하기");
		giveupBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 40));
		giveupBtn.setBounds(81, 600, 400, 50); // 버튼의 위치와 크기를 지정
		this.add(giveupBtn);
		giveupBtn.addActionListener(this);
			
		// 현재 두는 수를 버리는 버튼
		JButton trashBtn = new JButton("버리기");
		trashBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 40));
		trashBtn.setBounds(81, 300, 400, 70); // 버튼의 위치와 크기를 지정
		this.add(trashBtn);
		trashBtn.addActionListener(this);
			
		// 수를 결정하고 착수를 진행하는 버튼
		setBtn = new JButton("착수하기");
		setBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 40));
		setBtn.setBounds(81, 518, 400, 70); // 버튼의 위치와 크기를 지정
		this.add(setBtn);
		setBtn.addActionListener(this);

		// 타이머 -> 착수시간을 제한하기 위한 부분
		time_limit=TIME_LIMIT; // 착수 제한시간 설정
		// 1초 간격으로 타이머를 설정함
		timer = new Timer(1000, new ActionListener(){   
			public void actionPerformed (ActionEvent e){
				setBackground(Color.PINK); // 사용할 배경색상을 설정
				// 남은 제한시간을 레이블에 출력
				if(colorOption==BLACK)
					timerLabel.setText("흑돌 남은 시간: " + time_limit + " 초");
				else if(colorOption==WHITE)
					timerLabel.setText("백돌 남은 시간: " + time_limit + " 초");
				time_limit--; // 시간값을 1차감

				// 타이머 초기화 - 정해진 착수 제한시간이 모두 소진된 경우
				if(time_limit==0) {
					JOptionPane.showMessageDialog(mainFrame, "시간초과, 순서가 전환됩니다.");
					time_limit=TIME_LIMIT; // 제한시간을 60초로 설정
					// 순서를 넘김, 흑돌 -> 백돌, 백돌 -> 흑돌
					if(colorOption==BLACK)
						colorOption=WHITE;
					else if(colorOption==WHITE)
						colorOption=BLACK;
					count=false;
					repaint(); // 프레임을 업데이트
				}
			}
		});
		setVisible(true); // 현재까지 설정한 내용을 frame에 출력
	}
		
	// 대국페이지에 대한 constructor - 2
	public Play(int myColor, int enemyColor) {
		this();                     // constructor Play()를 호출
		this.myColor=myColor;       // 나의 색상값을 업데이트
		this.enemyColor=enemyColor; // 상대방의 색상값을 업데이트
	}

	// 사용자에게 표시해야하는 요소들을 그려내는 method
	public void paint (Graphics g0)
	{
		Graphics2D g = (Graphics2D)g0;
		super.paint(g);
		
		// 대국이 이루어지는 바둑판을 그리는 부분
		g.setColor(new Color(240,170,40)); // 바둑판의 배경색상을 설정
		g.fillRect(startX-30, startY-30, 600, 600); // 판의 큰 틀을 그리는 부분
		g.setColor(Color.black); // 그리는 직선의 색상을 black으로 설정
		// 바둑판을 구성하는 선 그리기
		for(int x=startX; x<= endX; x+=30)
			g.drawLine(x,startY,x,endY);
		for(int y=startY; y<=endY;y+=30)
			g.drawLine(startX,y,endX,y);
		// 바둑판의 중간 격자들을 표현하기 위한 점을 그리는 부분
		for(int x=startX+90-3;x<1200-50;x+=180) {
			for(int y=startY+90-3;y<700-50;y+=180) {
				g.fillOval(x, y, 6, 6);
			}
		}

		// 스택에 저장한 착수정보들을 바탕으로 새로운 프레임을 그려냄
		// 업데이트된 프레임을 그려내기 위한 부분
		for(int i=0;i<pointStack.size();i++) {
			Point tmp= pointStack.get(i);
			// 가장 최근에 착수된 돌을 표시하기 위한 부분 -> 경기진행의 편의성을 위해서 따로 삽입한 부분
			// -> AI에 의해 가장 최근에 착수된 돌 2개를 녹색으로 라운드 처리함
			if(tmp.beforecolorOption==myColor
					&&(i==pointStack.size()-1||i==pointStack.size()-2||i==pointStack.size()-3||i==pointStack.size()-4)) {
				g.setColor(Color.green);
				g.fillOval(tmp.beforeX-18, tmp.beforeY-18, 36, 36);
			}
			switch(tmp.beforecolorOption) {
			// 적돌을 그려냄
			case RED:
				g.setColor(Color.red);
				g.fillOval(tmp.beforeX-16, tmp.beforeY-16, 32, 32);
				break;
			// 흑돌을 그려냄
			case BLACK:
				g.setColor(Color.black);
				g.fillOval(tmp.beforeX-16, tmp.beforeY-16, 32, 32); 
				break;
			// 백돌을 그려냄
			case WHITE:
				g.setColor(Color.white);
				g.fillOval(tmp.beforeX-16, tmp.beforeY-16, 32, 32);
				break;
			}
		}
		// 범위 안에 있어야되고 이미 놓인 돌이 없어야함. 
		if((startX<=pointX) && (pointX<=endX) && (startY<=pointY) && (pointY<=endY)
				&& (colorArray[arrayX][arrayY]==0)) {
			// 적돌
			if(colorOption==RED) {
				g.setColor(Color.red);
				g.fillOval(pointX-16,pointY-16, 32, 32);
				Point tmp= new Point(pointX, pointY,RED); // 적돌을 착수한 정보를 stack에 저장
				pointStack.add(tmp); // 착수한 좌표, 색상값을 담은 Point 객체를 stack에 저장
				colorArray[arrayX][arrayY]=colorOption; // 적돌을 착수한 정보를 2차원 배열에 저장
			}
			// 흑돌, 백돌
			else { 
				switch(clickOption) {
				// 미리두기(네모 틀) - 착수를 확정짓기 전에 바둑판에 표시하는 사각형
				case PREV:
					if(colorOption==BLACK) {
						g.setColor(Color.black); // 사각형의 색상을 검은색으로
						g.setStroke(new BasicStroke(4));
						g.drawRect(pointX-14, pointY-14, 28, 28); // 사각형을 그림
					}
					else if(colorOption==WHITE) {
						g.setColor(Color.white); // 사각형의 색상을 흰색으로
						g.setStroke(new BasicStroke(4));
						g.drawRect(pointX-14, pointY-14, 28, 28); // 사각형을 그림
					}
					break;
					
				// 착수지점 확정 및 돌을 착수하는 부분
				// 매번 착수가 이루어질 때마다 승부를 결정지을 조건을 충족하였는 지 확인
				case SET:
					System.out.println("SET쪽으로 프로그램 흐름의 분기가 넘어갔습니다.");
					// 흑돌
					if(colorOption==BLACK) {
						g.setColor(Color.black);
						g.setStroke(new BasicStroke(4));
						g.setColor(Color.red);
						g.fillOval(pointX-14,pointY-14, 32, 32);
						colorArray[arrayX][arrayY]=BLACK; // 2차원 배열에 착수정보를 저장
					}
					// 백돌
					else if(colorOption==WHITE) {
						g.setColor(Color.white);
						g.setStroke(new BasicStroke(4));
						g.setColor(Color.red);
						g.fillOval(pointX-14,pointY-14, 32, 32);
						colorArray[arrayX][arrayY]=WHITE; // 2차원 배열에 착수정보를 저장
					}
					// 착수정보를 stack에 저장함
					Point tmp = new Point(pointX, pointY,colorOption);
					pointStack.add(tmp);
					
					// 착수의 순서를 넘김(흑->백 or 백->흑)
					colorChange();
					
					int winner=finishCheck(); // 승부가 났는 지를 확인하는 부분
					// 흑돌이 승리한 경우
					if(winner==BLACK) {
						timer.stop(); // 타이머를 정지시킴
						new Finish(BLACK); // 흑돌승리 페이지를 출력
					}
					// 백돌이 승리한 경우
					else if(winner==WHITE) {
						timer.stop(); // 타이머를 정지시킴
						new Finish(WHITE); // 백돌승리 페이지를 출력
					}
					break;
				
				// AI에 대한 부분
				// 지금 이쪽으로 흐름의 분기가 넘어가지 않는 상황 
				case AI:
					colorChange(); // 착수정보를 변경
					int winner1=finishCheck(); // 승부가 났는 지를 확인하는 부분
					// 흑돌이 승리한 경우
					if(winner1==BLACK) {
						timer.stop(); // 타이머 종료
						new Finish(BLACK); // 흑돌승리 페이지 출력
					}
					// 백돌이 승리한 경우
					else if(winner1==WHITE) {
						timer.stop(); // 타이머 종료
						new Finish(WHITE); // 백돌승리 페이지 출력
					}
					break;
				}
			}
		}
	}

	// 사용자가 착수한 지점에 바둑돌을 그리는 메소드
	public void set() {
		clickOption=SET; // Click Option에 SET 상수값을 할당
		repaint(); // 착수정보를 업데이트 하기위해서 frame을 다시 그림
	}
	
	// 돌의 색상을 변경하는 method
	// 착수한 돌의 갯수나 순서와 관련된 조건들을 고려하여 돌의 색상을 변경함
	public void colorChange() {
		// 순서가 맨 처음인 경우
		// -> 이제 막 경기를 시작한 경우
		if(first) {
			colorOption=WHITE; // 돌의 색상을 백돌로 변경
			first=false; // first의 값을 false로 변경 -> 이제 더 이상 최초시작 순서가 아니므로
			time_limit = TIME_LIMIT; // 타이머 초기화
		}
		// 그 이외의 경우
		else {
			// 2개의 돌을 착수한 경우
			if(count==true) {
				// 현재 순서가 백돌 -> 흑돌로
				if(colorOption==WHITE) {
					colorOption=BLACK;
					System.out.println("순서전환 : 백돌 -> 흑돌");
				}
				// 현재 순서가 흑돌 -> 백돌로
				else if(colorOption==BLACK) {
					colorOption=WHITE;
					System.out.println("순서전환 : 흑돌 -> 백돌");
				}
				count=false; // 착수한 돌의 수에 대한 정보값을 초기화
				time_limit = TIME_LIMIT; // 타이머 초기화
			}
			// 1개의 돌만 착수된 경우
			else
				count=true;
		}
		repaint(); // 프레임을 업데이트
	}
	
	// AI의 동작을 제어하는 부분
	public void aiPlay() {
		// 현재 AI 자신의 순서이며, 2개의 돌을 모두 놓지 않은 상황인 경우
		System.out.println("AI method로 분기가 넘어 옵니다.");
		if((colorOption==myColor) && (count==false)) {
			// 주어진 대국정보를 주입한 AI생성
			AI ai = new AI(colorArray, myColor, enemyColor);
			
			// 첫번째 돌을 착수함
			colorArray[ai.x1][ai.y1]=myColor;
			Point tmp = new Point(ai.x1*30+startX,ai.y1*30+startY,myColor);
			pointStack.add(tmp); // AI에 의해서 착수된 정보를 stack에 저장
			
			// AI의 돌이 백돌이거나
			// 흑돌이지만 맨 처음에 두는 수가 아닌 경우
			// 총 2개의 돌을 착수함
			if(myColor==WHITE || first==false) {
				colorArray[ai.x2][ai.y2]=myColor;
				tmp = new Point(ai.x2*30+startX,ai.y2*30+startY,myColor);
				pointStack.add(tmp); // AI에 의해서 착수된 정보를 stack에 저장
			}
			clickOption=AI;
			repaint();
		}
		// AI의 순서가 아닌 경우에 AI착수 버튼을 클릭한 경우
		else {
			System.out.println("AI 차례가 아닙니다 ! ");
		}
	}

	// 승패가 났는 지를 판단하는 method
	// 승리조건을 만족한 돌이 발견된 경우 해당 돌의 색상을 의미하는 상수값을 반환함
	// -> AI에 의한 착수가 이뤄지는 경우 정상적인 탐색이 이뤄지지 않음 -> 디버깅 해야함
	public int finishCheck()
	{
		
		int findCount; // 연속으로 배열되는 돌의 갯수
		int findColor = 0; // 돌의 색상값을 상수로 표현한 것
		
		findColor = colorArray[arrayX][arrayY];
		System.out.println("탐색대상인 색상 : " + findColor);
		
		// 탐색지점으로부터 위쪽 방향으로 연속되는 동일한 색의 돌수를 카운트
		findCount=1;
		for(int y=arrayY+1;y<=arrayY+5;y++) {
			// 탐색범위를 벗어난 경우
			if(y>18) break;
			// 동일한 색의 돌이 착수되어 있는 경우 -> 카운트 값을 1증가
			else if(colorArray[arrayX][y]==findColor) findCount++;
			// 서로 색상이 다르거나 빈공간을 만난 경우
			else break;
		}
		// 6개 이상의 돌이 위쪽방향으로 연속되는 경우
		System.out.println("위쪽 방향 : " + findCount);
		if(findCount>=6) return findColor; // 해당돌의 색상값을 반환
		
		
		// 아랫쪽 판단
		findCount = 1;
		for(int y=arrayY-1;arrayY-5<=y;y--) {
			// 주어진 탐색범위를 넘어선 경우
			if(y<0) break;
			// 동일한 색의 돌이 착수되어 있는 경우 -> 카운트 값을 1증가
			else if(colorArray[arrayX][y]==findColor) findCount++;
			// 서로 색상이 다르거나 빈공간을 만난 경우
			else break;
		}
		// 6개 이상의 돌이 아래쪽방향으로 연속되는 경우
		System.out.println("아래쪽 방향 : " + findCount);
		if(findCount>=6) return findColor; // 해당 돌의 색상값을 반환
		
		
		// 오른쪽 판단
		findCount=1;
		for(int x=arrayX+1;x<=arrayX+5;x++) {
			// 주어진 탐색의 범위를 넘어서는 경우
			if(x>18) break;
			// 동일한 색의 돌이 착수되어 있는 경우 -> 카운트 값을 1증가
			else if(colorArray[x][arrayY]==findColor) findCount++;
			// 서로 색상이 다르거나 빈공간을 만난 경우
			else break;
		}
		// 6개 이상의 돌이 오른쪽 방향으로 연속되는 경우
		System.out.println("오른쪽 방향 : " + findCount);
		if(findCount>=6) return findColor; // 해당 돌의 색상값을 반환
		
		
		//왼쪽 판단 
		findCount=1;
		for(int x=arrayX-1;arrayX-5<=x;x--) {
			// 주어진 탐색의 범위를 넘어서는 경우
			if(x<0) break;
			// 동일한 색의 돌들이 착수되어 있는 경우 -> 카운트의 값을 1증가
			else if(colorArray[x][arrayY]==findColor) findCount++;
			// 서로 색상이 다르거나 빈공간을 만난 경우
			else break;
		}
		// 6개 이상의 돌이 왼쪽 방향으로 연속되는 경우
		System.out.println("왼쪽 방향 : " + findCount);
		if(findCount>=6) return findColor;
		
		
		// 오른쪽 아래 대각선
		findCount=1;
		for(int x=arrayX+1, y=arrayY+1 ;x<=arrayX+5 && y<=arrayY+5 ;x++, y++) {
			// 주어진 탐색의 범위를 넘어서는 경우
			if(x>18||y>18) break;
			// 동일한 색의 돌들이 착수되어 있는 경우 -> 카운트의 값을 1증가
			else if(colorArray[x][y]==findColor) findCount++;
			// 서로 색상이 다르거나 빈공간을 만난 경우 
			else break;
		}
		// 6개 이상의 돌이 오른쪽 아래 대각선 방향으로 연속되는 경우
		System.out.println("오른쪽 아래 대각선 : " + findCount);
		if(findCount>=6) return findColor;
		
		
		//왼쪽 위 대각선
		for(int x=arrayX-1, y=arrayY-1 ; arrayX-5<=x && arrayY-5<=y ;x--, y--) {
			// 주어진 탐색의 범위를 넘어서는 경우
			if(x<0||y<0) break;
			// 동일한 색의 돌들이 착수되어 있는 경우 -> 카운트의 값을 1증가
			else if(colorArray[x][y]==findColor) findCount++;
			// 서로 색상이 다르거나 빈공간을 만난 경우
			else break;
		}
		// 6개 이상의 돌이 왼쪽 위 대각선 방향으로 연속되는 경우
		System.out.println("왼쪽 위 대각선 : " + findCount);
		if(findCount>=6) return findColor;
		
		
		// 오른쪽 위 대각선
		findCount=1;
		for(int x=arrayX+1, y=arrayY-1 ;x<=arrayX+5 && y>=arrayY-5 ;x++, y--) {
			// 주어진 탐색의 범위를 넘어서는 경우
			if(x>18||y<0) break;
			// 동일한 색의 돌들이 착수되어 있는 경우 -> 카운트의 값을 1증가
			else if(colorArray[x][y]==findColor) findCount++;
			// 서로 색상이 다르거나 빈공간을 만난 경우
			else break;
		}
		// 6개 이상의 돌이 오른쪽 위 대각선 방향으로 연속되는 경우
		System.out.println("오른쪽 위 대각선 : " + findCount);
		if(findCount>=6) return findColor;
		
		
		//왼쪽 아래 대각선
		for(int x=arrayX-1, y=arrayY+1 ; arrayX-5<=x && arrayY+5>=y ;x--, y++) {
			// 주어진 탐색의 범위를 넘어서는 경우
			if(x<0||y<0) break;
			// 동일한 색의 돌들이 착수되어 있는 경우 -> 카운트의 값을 1증가
			else if(colorArray[x][y]==findColor) findCount++;
			// 서로 색상이 다르거나 빈공간을 만난 경우
			else break;
		}
		// 6개 이상의 돌이 왼쪽 아래 대각선 방향으로 연속되는 경우
		System.out.println("왼쪽 아래 대각선 : " + findCount);
		if(findCount>=6) return findColor;
		
		return 0;
	}

	// 액션 리스너에 대한 정의
	@Override
	public void actionPerformed(ActionEvent e) {
		String button=e.getActionCommand();
		
		// "착수하기" 버튼을 클릭한 경우
		if(button.equals("착수하기")) {
			set();
		}
		// "기권하기" 버튼을 클릭한 경우
		else if(button.equals("기권하기")) {
			AI ai = new AI(colorArray, myColor, enemyColor);
			// System.out.println("------array------");
			// ai.printArray();
			// System.out.println("------Weight-----");
			// ai.printWeight();
			if(colorOption==BLACK)
				new Finish(WHITE); // 백돌의 승리를 위한 페이지 생성 -> 흑돌이 기권하였으므로
			else if(colorOption==WHITE)
				new Finish(BLACK); // 흑돌의 승리를 위한 페이지 생성 -> 백돌이 기권하였으므로
			timer.stop(); // 타이머 정지
		}
		// "Home" 버튼을 클릭한 경우
		else if(button.equals("Home")) {
			Start new_frame = new Start();
			new_frame.startGame();
			mainFrame.dispose();
		}
		// "AI 착수" 버튼을 클릭한 경우
		else if(button.equals("AI 착수")) {
			this.aiPlay(); // AI를 동작시키는 method 실행
			// finishCheck(); // 경기종료조건을 만족하였는 지 확인
			colorOption=enemyColor; // 상대편에서 순서를 넘겨줌
		}
		// "시작하기" 버튼을 클릭한 경우
		else if(button.equals("시작하기")) {
			int result = JOptionPane.showConfirmDialog(null, "대국을 시작합니다.", "대국시작", JOptionPane.YES_NO_OPTION);
			// 사용자가 예, 아니오의 선택없이 팝업창을 닫은 경우
			if(result == JOptionPane.CLOSED_OPTION) {
				System.out.println("대국 시작 취소");
			}
			// 사용자가 "예"를 선택한 경우
			else if(result == JOptionPane.YES_OPTION) {
				timer.start();
				colorOption=BLACK; // 초기 돌의 색상값을 흑돌로 설정
				System.out.println("시작하기");
			}
			// 사용자가 "아니오"를 선택한 경우
			else {
				System.out.println("대국 시작 취소");
			}
		}
		// "버리기" 버튼을 클릭한 경우
		else if(button.equals("버리기")) {
			if(colorOption==BLACK)
				colorOption=WHITE; // 착수를 포기하고 순서를 넘김
			else if(colorOption==WHITE)
				colorOption=BLACK; // 착수를 포기하고 순서를 넘김
			count=false; // count값을 false로 다시 초기화
			repaint();
		}
	}
	
	// 마우스 리스너 정의
	// -> 사용자가 마우스를 클릭한 위치에 대해서 돌을 둘 수 있도록 하는 부분
	@Override
	public void mousePressed(MouseEvent e) {
		if(startX<=e.getX() && e.getX()<=endX && startY<=e.getY() && e.getY()<=endY) {
			arrayX=(e.getX()-startX+15)/30;
			arrayY=(e.getY()-startY+15)/30;
			pointX= arrayX*30+startX;
			pointY= arrayY*30+startY;
			
			// 사용자 입력이 더블클릭인 경우
			// -> 돌을 착수함
			if(clickOption==PREV&& Math.abs(e.getX()-prepointX)<15 && Math.abs(e.getY()-prepointY)<15) {	
				set();
			}
			// 사용자 입력이 일반클릭인 경우
			// -> 착수될 위치를 표시하는 사각형을 출력함
			else {
				prepointX=pointX;
				prepointY=pointY;
				clickOption=PREV; 
				repaint();
			}
		}
	}
	
	// // 현재 처리되는 내용을 판단하기 위한 백엔드 단 출력
	// public void printArray() { //2차원 배열 출력
	// 	for(int x=0;x<19;x++) {
	// 		for(int y=0;y<19;y++) {
	// 			// 이 부분이 구체적으로 수행하는 역할은 무엇인가?
	// 			if(colorArray[x][y]==BLACK)
	// 				System.out.println("x: "+ x +", y : "+ y +" 번째 돌은 검은돌 입니다. ");
	// 			else if(colorArray[x][y]==WHITE)
	// 				System.out.println("x: "+ x +", y: "+ y +" 번째 돌은 흰돌 입니다. ");
	// 			else if(colorArray[x][y]==RED)
	// 				System.out.println("x: "+ x +", y: "+ y +" 번째 돌은 빨간 돌 입니다. ");
	// 		}
	// 	}
	// }

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
}
// AI에 대한 코드를 다루는 부분

package twoyoung.connect6.start;

public class AI {
	
	public int [][] colorArray = new int [19][19];  // 색상값에 대한 정보를 저장하는 2차원 배열
	public int [][] weightTable = new int [19][19]; // 착수를 위한 가중치 정보를 저장하는 2차원 배열
	
	Point firstPoint = new Point();  // 첫번째 돌의 착수지점에 대한 정보
	Point secondPoint = new Point(); // 두번째 돌의 착수지점에 대한 정보
	
	public int x1=3; // 선택된 좌표1의 x좌표
	public int y1=6; // 선택된 좌표1의 y좌표
	public int x2=1; // 선택된 좌표2의 x좌표
	public int y2=1; // 선택된 좌표2의 y좌표
	
	public int firstWeightX;
	public int firstWeightY;
	public int secondWeighX;
	public int secondWeighY;
	
	public final int weightA=500; //한꺼번에 제어하기 위한 가중치 변수 
	public final int weightB=400;
	public final int weightC=300;
	public final int weightD=200;
	
	public final int weightE=35;
	public final int weightF=30;
	public final int weightG=25;
	public final int weightH=20;
	
	public final int RED=1; 
	public final int WHITE=3;
	public final int BLACK=4;
	
	public int myColor;
	public int enemyColor;
	public boolean stop=false;
	
	
	public class Point
	{
		int weight;
		int x;
		int y;
	}
	
	
//	public void printArray(){
//		
//		for(int y=0;y<19;y++) {
//			for(int x=0;x<19;x++) {
//				System.out.printf("%4d",colorArray[x][y]);
//			}
//			System.out.print("\n");
//		}
//	}

	
//	public void printWeight(){
//		
//		for(int y=0;y<19;y++) {
//			for(int x=0;x<19;x++) {
//				System.out.printf("%4d",weightTable[x][y]);
//			}
//			System.out.print("\n");
//		}
//	}
	
	// Class AI에 대한 constructor 정의
	public AI(int [][] colorArray, int myColor, int enemyColor) {
		this();
		this.myColor=myColor;
		this.enemyColor=enemyColor;
		this.colorArray=colorArray;
//		System.out.println(myColor);
		
		for(int y=0;y<19;y++) {
			for(int x=0;x<19;x++) {

				if(colorArray[x][y]!=0) {
					weightTable[x][y]= -1; //뭐가 놓여있을때는 1 놓기
				}
				else if(colorArray[x][y]==0) {//빈공간일때만 보내기

					//당장 공격, 당장 수비
					weightTable[x][y]=setWeight_1(x, y);
					weightTable[x][y]=Math.max(setWeight_2(x,y), weightTable[x][y]);
					weightTable[x][y]=Math.max(setWeight_3(x,y), weightTable[x][y]);
					weightTable[x][y]=Math.max(setWeight_4(x,y), weightTable[x][y]); // 우선순위 중 가장 큰 값으로 갱신하기 위해 
					
					//길목이 많이 생기도록
					weightTable[x][y]+=makeLoad(x,y, myColor); // 내 길목 만들기
					weightTable[x][y]+=makeLoad(x,y, enemyColor); // 상대방 길목 차단시키기
				}

			}
		}
		
		for(int y=9;y<13;y++) {//처음에 정 중앙에 놓기 위해, 적돌이 중앙에 놓아질 것을 대비해서 4개
			if(weightTable[9][y]==0) {
				weightTable[9][y]=2;
			}
		}


		
		// printWeight();
		
		int maxWeight = 0; // 가장 큰 가중치의 값을 저장함

		int maxX=0;
		int maxY=0;

		
		for(int i=0 ; i<19 ; i++) {
			for(int j=0 ; j<19 ; j++) {
				if(maxWeight < weightTable[i][j]&&colorArray[i][j]==0) {
					

					maxWeight = weightTable[i][j];

					maxX = i;
					maxY = j;
					
				}
//				else if(maxWeight == weightTable[i][j]&&colorArray[i][j]==0) {
//					Random random = new Random();
//					if(random.nextBoolean()) {
//						
//						maxWeight = weightTable[i][j];
//
//						maxX = i;
//						maxY = j;
//						
//					}
//					
//				}
				
			}
		}
		
		this.x1=maxX;
		this.y1=maxY;

		weightTable[maxX][maxY] = -1; // 두번째 점의 착수를 위해 가중치가 가장 높은 점에 대한 정보를 초기화

		
		maxWeight = 0; // 가장 큰 가중치의 값을 저장함
		for(int i=0 ; i<19 ; i++) {
			for(int j=0 ; j<19 ; j++) {
				if(maxWeight < weightTable[i][j]&&colorArray[i][j]==0) {
					maxWeight = weightTable[i][j];
					maxX = i;
					maxY = j;
				}
//				else if(maxWeight == weightTable[i][j]&&colorArray[i][j]==0) {
//					Random random = new Random();
//					if(random.nextBoolean()) {
//						
//						maxWeight = weightTable[i][j];
//
//						maxX = i;
//						maxY = j;
//						
//					}
//				}
			}
		}
		
		this.x2=maxX;
		this.y2=maxY;

		// System.out.println(this.x1+" , "+this.y1+"에 돌을 놓으세요");
		// System.out.println(this.x2+" , "+this.y2+"에 돌을 놓으세요");
	}
	
	
	
	public int setWeight_1(int curX, int curY) { // x 축 검색 -> 6목 수비, 공격 찾기

		int myCount=0; 
		int enemyCount=0; 
		int emptyCount=0;
		int redCount=0;
		
		String set6 = ""; // 돌 6개씩 묶어 한 세트를 String 으로 표현한 것
		int weight=0;
		int length=0;
		
		// x 축 판단
		for(int x=curX-5;x<=curX;x++) { //6번 
			for(int x1=x;x1<=x+5;x1++) { //6개씩
				if(x1>18||x1<0) { //범위 넘었을 때
					set6+=0;
//					myCount++;
				}else if(colorArray[x1][curY]==myColor) { //우리돌일때
					myCount++;
					length++;
					set6+=1;
				}else if(colorArray[x1][curY]==0) { //빈곳을 만났을때 
					emptyCount++;
					length++;
					set6+=0;
				}else if(colorArray[x1][curY]==enemyColor){ // 상대방 돌일때
					enemyCount++;
					length++;
					set6+=2;
				}else {
					set6+=3;
					redCount++;
					length++;
				}
			}
			
			/*
			 * 위급한 상황
			 */
			if(set6.equals("011110")||set6.equals("011111")||set6.equals("111110")) { //공격도 붙어서 하는게 더 좋음
				
				return weightA;
			}else if(myCount==5&&emptyCount==1 || myCount==4 && emptyCount==2) { //아무데나 공격 ~
				
				try {
					if((colorArray[curX+6][curY]==myColor&&curX==x&&set6.equals("001111"))) {
						System.out.println("오목의 왼쪽에는 놓지 않겠다."+curX+" , "+curY);
						return 1;
					}else {
						return weightA;
					}
				}catch(Exception e) {
					weight=weightA-1;
				}
				
				try {
					if((colorArray[curX-6][curY]==myColor&&curX==x+5&&set6.equals("111100"))) { //돌이 5개 놓여있을때는 1개만 놓고 4개 놓여있을때는 2개 놓기 위해  
						System.out.println("오목의 오른쪽에는 놓지 않겠다."+curX+" , "+curY);
						weight=1;
					}else {
						weight= weightA;
					}
					
				}catch(Exception e) { //예외가 발생하면 (육목 길이까지 있지 않으면) 
					weight=weightA-1;
				}
				


			}else if(set6.equals("022220")||set6.equals("022222")||set6.equals("222220")) { //양쪽 막아야되는 경우 : 돌 2개 필요
				System.out.println("양 옆을 막아야하는 경우 " +curX+" , "+curY);
				return weightC;
			}else if(enemyCount==5&&emptyCount==1||enemyCount==4 && emptyCount==2){
				if(curX==x||curX==x+5) weight=0; // 양 옆을 막아야하는 경우가 아니라면 가운데 막는데만 돌을 써도 된다. -> 빈 공간이 끝에 있을 경우는 가중치를 낮춘다. : 돌 1개
				else{
					System.out.print(set6);
					System.out.println("바로 수비할 것을 찾음"+curX + " , "+curY);
					weight=weightD;
				}
			}
			if(weight!=0) {
				return weight;
			}
			

			/*
			 * 일반적 상황 : 공격 (핵심: 3이 두개 있을때 하나씩 공격해서 44를 만들기 위함)
			 */
			else if(length!=6||redCount>=1) weight=0; // 어차피 6목이 안되는 자리는 포기
			else {
				if(enemyCount==0&&set6.contains("01110")) { // 뚫린3 -> 뚫린4
					try { //예외 발생 -> 가중치 0
						if(colorArray[curX+1][curY]==myColor) {
							return weightE;
						}
						else {
							return 0;
						}
					}catch(Exception e) {
						weight=0;
					}
				}else if(enemyCount==0&&set6.contains("111")) { // 막힌3 -> 막힌 4
					try { //범위 밖에 있으면 그냥 검사 하지 말고 가중치 주기
//						if(colorArray[curX+1][curY]==myColor||colorArray[curX-1][curY]==myColor)weight= weightF; //바로 옆에 수 연결하기(평범한 공격)
						if(curX==x||curX==x+5) weight=weightF; //두번 띈 4 공격(막힌4로도 상대의 돌 2개를 낭비하도록 하기 위해, 대비되지 않은 팀 상대로)
						else weight=0;
					}catch(Exception e) {
						weight=0;
					}
					
				}else if(enemyCount==0 && redCount==0 && myCount==3) { //띈 3 -> 4
					try {
						if(colorArray[curX-1][curY]==myColor&&colorArray[curX+1][curY]==myColor)// 양 옆으로 둘러쌓여있을때만
							weight=weightE;
						else weight=0;
					}catch(Exception e) {
						weight=0;
					}
					
					/*
					 * 일반적 상황: 수비
					 */
					
				}else if(myCount==0&&set6.contains("02220")) { // 뚫린 3 방어하기
					try { //예외 발생 -> 가중치 0
						if(colorArray[curX+1][curY]==enemyColor) {
							System.out.println(" 뚫린 3 하나만 방어하기");
							return weightG;
						}
						else {
							return 0;
						}
					}catch(Exception e) {
						weight= 0;
						
					}
					
					
				}else if(myCount==0&&redCount==0&&enemyCount==3){ //띈 3 방어하기 
					
					try {
					if(colorArray[curX-1][curY]==enemyColor&&colorArray[curX+1][curY]==enemyColor)
						weight=weightH;
					else weight=0;
					}catch(Exception e) {
						weight=0;
					}
				}
			}
			

			myCount=0;
			enemyCount=0;
			emptyCount=0;
			redCount=0;
			length=0;
			set6=""; 
			
		}
		return weight;
	}
	
	public int setWeight_2(int curX , int curY) { // y 축 검색 -> 6목 공격, 수비 잡기
		
		
		int myCount=0; 
		int enemyCount=0; 
		int emptyCount=0; 
		int redCount=0;
		
		
		String set6 = ""; // 돌 6개씩 묶어 한 세트를 String 으로 표현한 것
		int weight=0;
		int length=0;
		
		
		
		
		// x 축 판단
		for(int y=curY-5;y<=curY;y++) { //6번 
			for(int y1=y;y1<=y+5;y1++) { //6개씩
				if(y1>18||y1<0) { //범위 넘었을 때
					set6+=0;
				}else if(colorArray[curX][y1]==myColor) { //우리돌일때
					myCount++;
					length++;
					set6+=1;
				}else if(colorArray[curX][y1]==0) { //빈곳을 만났을때 
					emptyCount++;
					length++;
					set6+=0;
				}else if(colorArray[curX][y1]==enemyColor){ // 상대방 돌일때
					enemyCount++;
					length++;
					set6+=2;
				}else {
					set6+=3;
					length++;
					redCount++;
				}
				
			}
			
			
			
			/*
			 * 위급한 상황
			 */
			
			
			if(set6.equals("011110")||set6.equals("011111")||set6.equals("111110")) { //공격도 붙어서 하는게 더 좋음
				System.out.println("공격도 기존의 돌과 붙으면 붙을수록 좋음" +curX+" , "+curY);
				return weightA;
			}else if(myCount==5&&emptyCount==1 || myCount==4 && emptyCount==2) { //아무데나 공격 ~
				try {
					if((colorArray[curX][curY+6]==myColor&&curY==y&&set6.equals("001111"))) {
						System.out.println("오목의 왼쪽에는 놓지 않겠다."+curX+" , "+curY);
						return 1;
					}else {
						return weightA;
					}
				}catch(Exception e) {
					weight=weightA-1;
				}
				
				try {
					if((colorArray[curX][curY-6]==myColor&&curY==y+5&&set6.equals("111100"))) { //돌이 5개 놓여있을때는 1개만 놓고 4개 놓여있을때는 2개 놓기 위해  
						System.out.println("오목의 오른쪽에는 놓지 않겠다."+curX+" , "+curY);
						weight=1;
					}else {
						weight= weightA;
					}
					
				}catch(Exception e) { //예외가 발생하면 (육목 길이까지 있지 않으면) 
					weight=weightA-1;
				}
				

			}else if(set6.equals("022220")||set6.equals("022222")||set6.equals("222220")) { //양쪽 막아야되는 경우 : 돌 2개 필요
				System.out.println("양 옆을 막아야하는 경우 " +curX+" , "+curY);
				return weightC;
			}else if(enemyCount==5&&emptyCount==1||enemyCount==4 && emptyCount==2){
				if(curY==y||curY==y+5) weight=0; // 양 옆을 막아야하는 경우가 아니라면 가운데 막는데만 돌을 써도 된다. -> 빈 공간이 끝에 있을 경우는 가중치를 낮춘다. : 돌 1개
				else{
					System.out.print(set6);
					System.out.println("바로 수비할 것을 찾음"+curX + " , "+curY);
					weight=weightD;
				}
			}
			if(weight!=0) {
				return weight;
			}

			/*
			 * 일반적 상황 : 공격 (핵심: 3이 두개 있을때 하나씩 공격해서 44를 만들기 위해)
			 */
			
			
			else if(length!=6||redCount!=0) weight=0; // 어차피 6목이 안되는 자리는 포기
			else {
				if(enemyCount==0&&set6.contains("01110")) { // 뚫린3 -> 뚫린4
					try { //예외 발생 -> 가중치 0
						if(colorArray[curX][curY+1]==myColor) {
							return weightE;
						}
						else {
							return 0;
						}
					}catch(Exception e) {
						weight=0;
						
					}
				}else if(enemyCount==0&&set6.contains("111")) { // 막힌3 -> 막힌 4
					try { //범위 밖에 있으면 그냥 검사 하지 말고 가중치 주기
//						if(colorArray[curX][curY+1]==myColor||colorArray[curX][curY-1]==myColor)weight= weightF;
						if(curY==y||curY==y+5) weight=weightF; //두번 띈 4 공격(막힌4로도 상대의 돌 2개를 낭비하도록 하기 위해, 대비되지 않은 팀 상대로)
						else weight=0;
					}catch(Exception e) {
						weight=0;
					}
					
				}else if(enemyCount==0 && redCount==0 && myCount==3) { //띈 3 -> 4
					try {
						if(colorArray[curX][curY-1]==myColor&&colorArray[curX][curY+1]==myColor)// 양 옆으로 둘러쌓여있을때만
							weight=weightE;
						else weight=0;
					}catch(Exception e) {
						weight=0;
					}
					
					/*
					 * 일반적 상황: 수비
					 */
					
				}else if(myCount==0&&set6.contains("02220")) { // 뚫린 3 방어하기
					try { //예외 발생 -> 가중치 0
						if(colorArray[curX][curY+1]==enemyColor) {
							return weightG;
						}
						else {
							return 0;
						}
					}catch(Exception e) {
						weight= 0;
						
					}
					
					
				}else if(myCount==0&&redCount==0&&enemyCount==3){ //띈 3 방어하기 
					
					try {
					if(colorArray[curX][curY-1]==enemyColor&&colorArray[curX][curY+1]==enemyColor)
						weight=weightH;
					else weight=0;
					}catch(Exception e) {
						weight=0;
					}
				}
			}
			

			myCount=0;
			enemyCount=0;
			emptyCount=0;
			redCount=0;
			length=0;
			set6=""; 
			
		}
		return weight;
	}
	
	public int setWeight_3(int curX , int curY) { // 오른쪽 아래로 검색 -> 6목 공격, 수비 잡기
		
		
		int myCount=0; 
		int enemyCount=0; 
		int emptyCount=0; 
		int redCount=0;
		
		
		String set6 = ""; // 돌 6개씩 묶어 한 세트를 String 으로 표현한 것
		int weight=0;
		int length=0;
		
		

		// x 축 판단
		for(int x=curX-5,y=curY-5;x<=curX&&y<=curY;x++,y++) { //6번 
			for(int x1=x,y1=y;x1<=x+5&&y1<=y+5;x1++,y1++) { //6개씩
				if(x1>18||x1<0||y1>18||y1<0) { //범위 넘었을 때
					set6+=0;
//					myCount++;
				}else if(colorArray[x1][y1]==myColor) { //우리돌일때
					myCount++;
					length++;
					set6+=1;
				}else if(colorArray[x1][y1]==0) { //빈곳을 만났을때 
					emptyCount++;
					length++;
					set6+=0;
				}else if(colorArray[x1][y1]==enemyColor){ // 상대방 돌일때
					enemyCount++;
					length++;
					set6+=2;
				}else {
					set6+=3;
					redCount++;
					length++;
				}
			}
			
			
			
			/*
			 * 위급한 상황
			 */
			
			
			if(set6.equals("011110")||set6.equals("011111")||set6.equals("111110")) { //공격도 붙어서 하는게 더 좋음
				System.out.println("공격도 기존의 돌과 붙으면 붙을수록 좋음" +curX+" , "+curY);
				return weightA;
			}else if(myCount==5&&emptyCount==1 || myCount==4 && emptyCount==2) { //아무데나 공격 ~
		
				try {
					if((colorArray[curX+6][curY+6]==myColor&&curX==x&&curY==y&&set6.equals("001111"))) {
						System.out.println("오목의 왼쪽에는 놓지 않겠다."+curX+" , "+curY);
						return 1;
					}else {
						return weightA;
					}
				}catch(Exception e) {
					weight=weightA-1;
				}
				
				try {
					if((colorArray[curX-6][curY-6]==myColor&&curX==x+5&&curY==y+5&&set6.equals("111100"))) { //돌이 5개 놓여있을때는 1개만 놓고 4개 놓여있을때는 2개 놓기 위해  
						System.out.println("오목의 오른쪽에는 놓지 않겠다."+curX+" , "+curY);
						weight=1;
					}else {
						weight= weightA;
					}
					
				}catch(Exception e) { //예외가 발생하면 (육목 길이까지 있지 않으면) 
					weight=weightA-1;
				}

			}else if(set6.equals("022220")||set6.equals("022222")||set6.equals("222220")) { //양쪽 막아야되는 경우 : 돌 2개 필요
				System.out.println("양 옆을 막아야하는 경우 " +curX+" , "+curY);
				return weightC;
			}else if(enemyCount==5&&emptyCount==1||enemyCount==4 && emptyCount==2){
				if((curX==x&&curY==y)||(curX==x+5&&curY==y+5)) weight=0; // 양 옆을 막아야하는 경우가 아니라면 가운데 막는데만 돌을 써도 된다. -> 빈 공간이 끝에 있을 경우는 가중치를 낮춘다. : 돌 1개
				else{
					System.out.print(set6);
					System.out.println("바로 수비할 것을 찾음"+curX + " , "+curY);
					weight=weightD;
				}
			}
			if(weight!=0) {
				return weight;
			}

			
			/*
			 * 일반적 상황 : 공격 (핵심: 3이 두개 있을때 하나씩 공격해서 44를 만들기 위해)
			 */
			
			else if(length!=6||redCount!=0) weight=0; // 어차피 6목이 안되는 자리는 포기
			else {
				if(enemyCount==0&&set6.contains("01110")) { // 뚫린3 -> 뚫린4
					try { //예외 발생 -> 가중치 0
						if(colorArray[curX+1][curY+1]==myColor) {
							return weightE;
						}
						else {
							return 0;
						}
					}catch(Exception e) {
						weight=0;
						
					}
				}else if(enemyCount==0&&set6.contains("111")) { // 막힌3 -> 막힌 4
					try { //범위 밖에 있으면 그냥 검사 하지 말고 가중치 주기
//						if(colorArray[curX+1][curY+1]==myColor||colorArray[curX-1][curY-1]==myColor)weight= weightF;
						if((curX==x&&curY==y)||(curX==x+5&&curY==y+5)) weight=weightF; //두번 띈 4 공격(막힌4로도 상대의 돌 2개를 낭비하도록 하기 위해, 대비되지 않은 팀 상대로)
						else weight=0;
					}catch(Exception e) {
						weight=0;
					}
					
				}else if(enemyCount==0 && redCount==0 && myCount==3) { //띈 3 -> 4
					try {
						if(colorArray[curX-1][curY-1]==myColor&&colorArray[curX+1][curY+1]==myColor)// 양 옆으로 둘러쌓여있을때만
							weight=weightE;
						else weight=0;
					}catch(Exception e) {
						weight=0;
					}

					
					/*
					 * 일반적 상황: 수비
					 */
					
				}else if(myCount==0&&set6.contains("02220")) { // 뚫린 3 방어하기
					try { //예외 발생 -> 가중치 0
						if(colorArray[curX+1][curY+1]==enemyColor) {
							return weightG;
						}
						else {
							
							return 0;
						}
					}catch(Exception e) {
						weight= 0;
						
					}
					
					
				}else if(myCount==0&&redCount==0&&enemyCount==3){ //띈 3 방어하기 
					
					try {
					if(colorArray[curX-1][curY-1]==enemyColor&&colorArray[curX+1][curY+1]==enemyColor)
						weight=weightH;
					else weight=0;
					}catch(Exception e) {
						weight=0;
					}
				}
			}
			

			myCount=0;
			enemyCount=0;
			emptyCount=0;
			redCount=0;
			length=0;
			set6=""; 
			
		}
		return weight;
	}
	
	public int setWeight_4(int curX , int curY) { // 오른쪽 위로 검색 -> 6목 공격, 수비 잡기
		
		
		int myCount=0; 
		int enemyCount=0; 
		int emptyCount=0; 
		int redCount=0;
		
		
		String set6 = ""; // 돌 6개씩 묶어 한 세트를 String 으로 표현한 것
		int weight=0;
		int length=0;
		
		

		// x 축 판단
		for(int x=curX-5,y=curY+5;x<=curX&&y>=curY;x++,y--) { //6번 
			for(int x1=x,y1=y;x1<=x+5&&y1>=y-5;x1++,y1--) { //6개씩
				if(x1>18||x1<0||y1>18||y1<0) { //범위 넘었을 때
					set6+=0;
				}else if(colorArray[x1][y1]==myColor) { //우리돌일때
					myCount++;
					set6+=1;
					length++;
				}else if(colorArray[x1][y1]==0) { //빈곳을 만났을때 
					emptyCount++;
					length++;
					set6+=0;
				}else if(colorArray[x1][y1]==enemyColor){ // 상대방 돌일때
					enemyCount++;
					length++;
					set6+=2;
				}else {
					set6+=3;
					redCount++;
					length++;
				}
			}
			
			
			
			/*
			 * 위급한 상황
			 */
			
			
			if(set6.equals("011110")||set6.equals("011111")||set6.equals("111110")) { //공격도 붙어서 하는게 더 좋음
				System.out.println("공격도 기존의 돌과 붙으면 붙을수록 좋음" +curX+" , "+curY);
				return weightA;
			}else if(myCount==5&&emptyCount==1 || myCount==4 && emptyCount==2) { //아무데나 공격 ~

				try {
					if((colorArray[curX+6][curY-6]==myColor&&curX==x&&curY==y&&set6.equals("001111"))) {
						System.out.println("오목의 왼쪽에는 놓지 않겠다."+curX+" , "+curY);
						return 1;
					}else {
						return weightA;
					}
				}catch(Exception e) {
					weight=weightA-1;
				}
				
				try {
					if((colorArray[curX-6][curY+6]==myColor&&curX==x+5&&curY==y-5&&set6.equals("111100"))) { //돌이 5개 놓여있을때는 1개만 놓고 4개 놓여있을때는 2개 놓기 위해  
						System.out.println("오목의 오른쪽에는 놓지 않겠다."+curX+" , "+curY);
						weight=1;
					}else {
						weight= weightA;
					}
					
				}catch(Exception e) { //예외가 발생하면 (육목 길이까지 있지 않으면) 
					weight=weightA-1;
				}
				

			}else if(set6.equals("022220")||set6.equals("022222")||set6.equals("222220")) { //양쪽 막아야되는 경우 : 돌 2개 필요
				System.out.println("양 옆을 막아야하는 경우 " +curX+" , "+curY);
				return weightC;
			}else if(enemyCount==5&&emptyCount==1||enemyCount==4 && emptyCount==2){
				if((curX==x&&curY==y)||(curX==x+5&&curY==y-5)) weight=0; // 양 옆을 막아야하는 경우가 아니라면 가운데 막는데만 돌을 써도 된다. -> 빈 공간이 끝에 있을 경우는 가중치를 낮춘다. : 돌 1개
				else{
					System.out.print(set6);
					System.out.println("바로 수비할 것을 찾음"+curX + " , "+curY);
					weight=weightD;
				}
			}
			if(weight!=0) {
				return weight;
			}

			/*
			 * 일반적 상황 : 공격 (핵심: 3이 두개 있을때 하나씩 공격해서 44를 만들기 위해)
			 */
			
			
			else if(length!=6||redCount!=0) weight=0; // 어차피 6목이 안되는 자리는 포기
			else {
				if(enemyCount==0&&set6.contains("01110")) { // 뚫린3 -> 뚫린4
					try { //예외 발생 -> 가중치 0
						if(colorArray[curX+1][curY-1]==myColor) {
							return weightE;
						}
						else {
							return 0;
						}
					}catch(Exception e) {
						weight=0;
						
					}
				}else if(enemyCount==0&&set6.contains("111")) { // 막힌3 -> 막힌 4
					try { //범위 밖에 있으면 그냥 검사 하지 말고 가중치 주기
//						if(colorArray[curX+1][curY-1]==myColor||colorArray[curX-1][curY+1]==myColor)weight= weightF;
						if((curX==x&&curY==y)||(curX==x+5&&curY==y-5)) weight=weightF;
						else weight=0;
					}catch(Exception e) {
						weight=0;
					}
					
				}else if(enemyCount==0 && redCount==0 && myCount==3) { //띈 3 -> 4
					try {
						if(colorArray[curX+1][curY-1]==myColor&&colorArray[curX-1][curY+1]==myColor)// 양 옆으로 둘러쌓여있을때만
							weight=weightE;
						else weight=0;
					}catch(Exception e) {
						weight=0;
					}
					
					/*
					 * 일반적 상황: 수비
					 */
					
				}else if(myCount==0&&set6.contains("02220")) { // 뚫린 3 방어하기
					try { //예외 발생 -> 가중치 0
						if(colorArray[curX+1][curY-1]==enemyColor) {
							return weightG;
						}
						else {
							return 0;
						}
					}catch(Exception e) {
						weight= 0;
						
					}
					
					
				}else if(myCount==0&&redCount==0&&enemyCount==3){ //띈 3 방어하기 
					
					try {
					if(colorArray[curX+1][curY-1]==enemyColor&&colorArray[curX-1][curY+1]==enemyColor)
						weight=weightH;
					else weight=0;
					}catch(Exception e) {
						weight=0;
					}
				}
			}
			

			myCount=0;
			enemyCount=0;
			emptyCount=0;
			redCount=0;
			length=0;
			set6=""; 
			
		}
		return weight;
	}
	
	
	
	public int makeLoad(int curX, int curY, int findColor){ 
		
		int findCount=0;
		int sum=0;
		int scope=3;
		
		
		
		//아래쪽 판단
		for(int y=curY+1;y<=curY+scope;y++) {
			if(y>18||(colorArray[curX][y]!=findColor&&colorArray[curX][y]!=0)) { //범위 넘거나 원하지 않는 돌을 마주쳤을때
				findCount=0; //초기화 
				break;
			}else if(colorArray[curX][y]==findColor) { //찾았을때 
				findCount++;

			}
		}
		
		
		//윗쪽 판단 
		for(int y=curY-1;curY-scope<=y;y--) {
			if(y<0||(colorArray[curX][y]!=findColor&&colorArray[curX][y]!=0)) { //범위 넘었을 때
				findCount=0; //초기화 
				break;
			}else if(colorArray[curX][y]==findColor) { //찾았을때 
				findCount++;

			}
		}
		sum+=findCount;
		findCount=0;
		
		
		
		//오른쪽 판단
		for(int x=curX+1;x<=curX+scope;x++) {
			if(x>18||(colorArray[x][curY]!=findColor&&colorArray[x][curY]!=0)) { //범위 넘었을 때
				findCount=0; //초기화 
				break;
			}else if(colorArray[x][curY]==findColor) { //찾았을때 
				findCount++;

			}
		}
		//왼쪽 판단 
		for(int x=curX-1;curX-scope<=x;x--) {
			if(x<0||(colorArray[x][curY]!=findColor&&colorArray[x][curY]!=0)) { //범위 넘었을 때
				findCount=0; //초기화 
				break;
			}else if(colorArray[x][curY]==findColor) { //찾았을때 
				findCount++;

			}
		}

		sum+=findCount;
		findCount=0;
		
		
		
		// 오른쪽 아래 대각선
		for(int x=curX+1, y=curY+1 ;x<=curX+scope && y<=curY+scope ;x++, y++) {
			if(x>18||y>18||(colorArray[x][y]!=findColor&&colorArray[x][y]!=0)) { //범위 넘었을 때
				findCount=0; //초기화 
				break;
			}else if(colorArray[x][y]==findColor) { //찾았을때 
				findCount++;

			}
		}
		
		//왼쪽 위 대각선
		for(int x=curX-1, y=curY-1 ; curX-scope<=x && curY-scope<=y ;x--, y--) {
			if(x<0||y<0||(colorArray[x][y]!=findColor&&colorArray[x][y]!=0)) { //범위 넘었을 때
				break;
			}else if(colorArray[x][y]==findColor) { //찾았을때 
				findCount++;
			}
		}

		sum+=findCount;
		findCount=0;
		
		
		// 오른쪽 위 대각선
		for(int x=curX+1, y=curY-1 ;x<=curX+scope && y>=curY-scope ;x++, y--) {
			if(x>18||y<0||(colorArray[x][y]!=findColor&&colorArray[x][y]!=0)) { //범위 넘었을 때
				findCount=0; //초기화 
				break;
			}else if(colorArray[x][y]==findColor) { //찾았을때 
				findCount++;
			}
		}
		
		//왼쪽 아래 대각선
		for(int x=curX-1, y=curY+1 ; curX-scope<=x && y<=curY+scope ;x--, y++) {
			if(x<0||y>18||(colorArray[x][y]!=findColor&&colorArray[x][y]!=0)) { //범위 넘었을 때
				findCount=0; //초기화 
				break;
			}else if(colorArray[x][y]==findColor) { //찾았을때 
				findCount++;
			}
		}
		sum+=findCount;
		findCount=0;
		
		return sum;
	}
	
	
	
	public AI(){
		
	}
}

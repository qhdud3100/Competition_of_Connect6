// 현재까지 진행된 대국에 대한 정보를 다루는 페이지
// -> 일단 당장의 기능에 영향을 주는 요소가 아니므로 우선순위에서 제외

package twoyoung.connect6.start;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import twoyoung.connect6.start.Finish.Record;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Rank extends JFrame {
	
	
	private DefaultTableModel model;
	private JPanel contentPane;
	private JTable table;
	public final int WHITE=3;
	public final int BLACK=4;
	public int winCount=0;
	public int loseCount=0;
	
	
	public class Record implements Comparable<Record>{
		String name;
		int winCount=0;
		int loseCount=0;
		
//		@Override
//		public int compareTo(Record o) {
//			// TODO Auto-generated method stub
//			
//			if(o.loseCount==0) return 1;
//			else if(this.loseCount==0) return -1;
//			if(o.winCount/o.loseCount>this.winCount/this.loseCount) return 1;
//			else if(o.winCount/o.loseCount<this.winCount/this.loseCount) return -1;
//			else return 0;
//		}


		@Override
		public int compareTo(Record o) {
			// TODO Auto-generated method stub
			if(o.winCount>this.winCount) return 1;
			else if(o.winCount<this.winCount) return -1;
			else if(o.winCount==this.winCount) {
				if(o.loseCount>this.loseCount) return -1;
				else return 1;
			}else return 0;
		}
		
		
	}
	
	
	/**
	 * 메인 
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Rank frame = new Rank();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 *  생성자 
	 */
	public Rank() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1200, 700);
		contentPane = new JPanel();
		contentPane.setBackground(Color.PINK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		setLocationRelativeTo(null);
		setResizable(false);
		
		
        JLabel lblNewLabel = new JLabel("랭킹 보기");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 40));
        lblNewLabel.setBounds(400, 20, 400, 50);
        contentPane.add(lblNewLabel);
		
		
		/*
		 * 
		 * 파일에서 읽어오기
		 * 
		 */
		Scanner inputStream = null; 
		try {
			inputStream = new Scanner(new File("record.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("파일을 찾을 수 없습니다. ");
		}

		
		
		
		Vector<String> column1 = new Vector<String>(); //맨 첫줄
		column1.addElement("등수");
		column1.addElement("이름");
		column1.addElement("승");
		column1.addElement("패");  

		model= new DefaultTableModel(column1,0);
//		model.addRow(column1);
		
		table = new JTable(model);
		table.setEnabled(false);
		table.setRowSelectionAllowed(false);
		table.setShowGrid(false);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		table.setBounds(348, 99, 600, 500);
		contentPane.add(table);
		table.setFont(new Font("Lucida Grande", Font.PLAIN, 30));
		table.setBackground(Color.PINK);
		
		
		
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        
        table.setRowHeight(40);
        
		List <Record> list = new ArrayList<>();
        
        int n=1;
		while(inputStream.hasNextLine()) { //한줄씩 작성한다.
			Record tmp = new Record();
			String [] word = inputStream.nextLine().split(" ");
			tmp.name =word[0];	
			tmp.winCount =Integer.parseInt(word[1]);
			tmp.loseCount =Integer.parseInt(word[2]);
				
			list.add(tmp);
			
		}
		
		Collections.sort(list); //승률에 따라 sorting
		
		for(int i=0;i<list.size();i++) {
			Vector <String> row = new Vector<String>();

			Record tmp = list.get(i);
			
			row.addElement(Integer.toString(n)+" 위:  ");
			row.addElement(tmp.name);
			row.addElement(tmp.winCount+"  승");
			row.addElement(tmp.loseCount+" 패");
			
			model.addRow(row);    
			n++;
		}

        
//        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
//        table.setRowSorter(sorter);
//
//        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
//        sortKeys.add(new RowSorter.SortKey(4, SortOrder.ASCENDING));
//        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
//        sorter.setSortKeys(sortKeys);
        
        
        
        

		JButton homeButton = new JButton("Home");
		homeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Start();
				dispose();
			}
		});
		homeButton.setBounds(6, 6, 67, 29);
		getContentPane().add(homeButton);
        
        
		setVisible(true);
		
		inputStream.close();
		
	}


}
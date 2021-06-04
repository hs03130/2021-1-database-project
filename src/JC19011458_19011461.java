import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class JC19011458_19011461 extends JFrame implements ActionListener {
	
	/* ���� ���� */
	static Connection con;
	Statement stmt;
	ResultSet rs, rs1;
	String Driver = "";
	String url = "jdbc:mysql://localhost:3306/madang?&serverTimezone=Asia/Seoul&useSSL=false";
	String userid = "madang";
	String pwd = "madang";
	
	// common
	Container c = getContentPane();
	JPanel pnNorth = new JPanel();
	JPanel pnWest = new JPanel();
	JPanel pnCenter = new JPanel();
	JPanel pnEast = new JPanel();
	JPanel pnSouth = new JPanel();
	
	String userInfo = null;
	String todayYear = "2020", todaySemester="1";
	ImageIcon logoutIcon = new ImageIcon("images/exit-logout.png");
	ImageIcon leftArrowIcon = new ImageIcon("images/left-arrow.png");
	
	public JC19011458_19011461() {
		super("JC19011458_19011461");
		conDB();
		layInit();
		setVisible(true);
		setSize(1920, 1040);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/* ���� ���� */
	public void conDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("����̹� �ε� ����");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try { /* �����ͺ��̽��� �����ϴ� ���� */
			System.out.println("�����ͺ��̽� ���� �غ�...");
			con = DriverManager.getConnection(url, userid, pwd);
			System.out.println("�����ͺ��̽� ���� ����");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public void layInit() {
		c.removeAll();

		JPanel pnBtn = new JPanel();
		pnBtn.setLayout(new GridLayout(3,1,10,10));
		
		JButton btnAdmin, btnProfessor, btnStudent;
		btnAdmin = new JButton("������");
		btnAdmin.setPreferredSize(new Dimension(400, 60));
		btnAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userInfo = "ADMIN";
				adminMain();
			}
		});
		
		btnProfessor = new JButton("����");
		btnProfessor.setPreferredSize(new Dimension(400, 60));
		btnProfessor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				professorLogin();
			}
		});
		
		btnStudent = new JButton("�л�");
		btnStudent.setPreferredSize(new Dimension(400, 60));
		btnStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				studentLogin();
			}
		});
		
		pnBtn.add(btnAdmin);
		pnBtn.add(btnProfessor);
		pnBtn.add(btnStudent);
		
		pnCenter.removeAll();
		pnCenter.add(pnBtn);
		
		c.add("Center", pnCenter);
		c.revalidate();
		//c.repaint();
	}
	
	/* ������ */
	public void adminMain() {
		
	}
	
	/* ���� */
	public void professorLogin() {
		c.removeAll();
		
		JPanel pnLogin = new JPanel();
		pnLogin.setLayout(new GridLayout(3,1,10,10));
		
		JTextField professorNo = new JTextField();
		JButton btnLogin = new JButton("�α���");
		JLabel lbLoginFail = new JLabel("");
		lbLoginFail.setHorizontalAlignment(JLabel.CENTER);

		professorNo.setText("����");
		professorNo.setPreferredSize(new Dimension(400, 60));
		professorNo.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if (!((JTextField)e.getSource()).getText().equals("")) {
					((JTextField)e.getSource()).setText("");
				}
			}
		});
		professorNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnLogin.doClick();
			}
		});
		
		btnLogin.setPreferredSize(new Dimension(400, 60));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (professorNo.getText().equals("")) {
					lbLoginFail.setText("������ �Է����ּ���.");
					return;
				}
				try {
					Integer.parseInt(professorNo.getText());
					
					try {
						stmt = con.createStatement();
						String query = "SELECT COUNT(*) AS RESULT FROM professor WHERE professor_no = " + professorNo.getText();
						rs = stmt.executeQuery(query);
						rs.next();
						if (rs.getInt(1) == 1) {
							userInfo = professorNo.getText();
							professorMain();
						} else if (rs.getInt(1) == 0) {
							lbLoginFail.setText("�������� �ʴ� �����Դϴ�.");
						}
					} catch (SQLException e2) {
						System.out.println("���� �б� ���� :" + e2);
					}
					
				} catch(NumberFormatException e1) {
					// ���� �̿ܿ� �ٸ� ���� �ԷµǸ� ����
					lbLoginFail.setText("������ �ùٸ��� �ʽ��ϴ�.");
				}
			}
		});
		
		pnLogin.add(professorNo);
		pnLogin.add(btnLogin);
		pnLogin.add(lbLoginFail);
		
		pnCenter.removeAll();
		pnCenter.add(pnLogin);
		
		c.add("Center", pnCenter);
		c.revalidate();
		c.repaint();
	}
	
	public void professorMain() {
		c.removeAll();
		
		// ���� �̸� select
		String professorName = "";
		try {
			stmt = con.createStatement();
			String query = "SELECT professor_name FROM professor WHERE professor_no = " + userInfo;
			rs = stmt.executeQuery(query);
			rs.next();
			professorName = rs.getString(1);
		} catch (SQLException e) {
			System.out.println("���� �б� ���� :" + e);
		}
		JPanel pnUser = new JPanel();
		JLabel lbProfessor = new JLabel(userInfo + " " + professorName);
		JLabel lbLogout = initBtnLogout();
		pnUser.add(lbProfessor);
		pnUser.add(lbLogout);
	
		pnNorth.removeAll();
		pnNorth.setLayout(new BorderLayout());
		pnNorth.add("East", pnUser);
		
		JPanel pnBtn = new JPanel();
		pnBtn.setLayout(new GridLayout(4,1,5,5));
		pnBtn.setPreferredSize(new Dimension(90, 240));
		
		JButton btnLecture = new JButton("����");
		btnLecture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				professorLecture("2021", "1");				
			}
		});
		
		JButton btnTutor = new JButton("����");
		btnTutor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JButton btnDepartment = new JButton("�Ҽ�");
		btnDepartment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JButton btnTimetable = new JButton("�ð�ǥ");
		btnTimetable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		pnBtn.add(btnLecture);
		pnBtn.add(btnTutor);
		pnBtn.add(btnDepartment);
		pnBtn.add(btnTimetable);
		pnWest.removeAll();
		pnWest.add(pnBtn);
		
		pnCenter.removeAll();
		pnCenter.setBackground(Color.GRAY);
		
		c.add("North",pnNorth);
		c.add("West", pnWest);
		c.add("Center", pnCenter);
		c.revalidate();
		c.repaint();
	}
	
	public void professorLecture(String lectureYearValue, String lectureSemesterValue) {
		c.remove(pnCenter);
		pnCenter.removeAll();
		pnCenter.setLayout(new BorderLayout());
		
		JPanel pnHeader = new JPanel();
		//JPanel pnContent = new JPanel();
		
		pnHeader.setLayout(new BorderLayout());
		pnHeader.setBackground(Color.GRAY);
		
		JLabel lbTitle = new JLabel("���� ��ȸ");
		lbTitle.setHorizontalAlignment(JLabel.LEFT);
		
		JPanel pnCondition = new JPanel();
		
		JTextField lectureYear = new JTextField();
		lectureYear.setText(lectureYearValue);
		lectureYear.setPreferredSize(new Dimension(100, 30));
		lectureYear.setHorizontalAlignment(JTextField.RIGHT);
		
		JTextField lectureSemester = new JTextField();
		lectureSemester.setText(lectureSemesterValue);
		lectureSemester.setPreferredSize(new Dimension(100, 30));
		lectureSemester.setHorizontalAlignment(JTextField.RIGHT);
		
		JButton btnInquire = new JButton("��ȸ");
		btnInquire.setHorizontalAlignment(JButton.RIGHT);
		btnInquire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(pnCenter.getComponentCount());
				if (pnCenter.getComponentCount() > 1) { // �̹� scrollContent �����ϸ� ����
					pnCenter.remove(1);
				}
				JScrollPane scrollContent = new JScrollPane(findLectureByProfessor(lectureYear.getText(), lectureSemester.getText()));
				//scrollContent.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				scrollContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				pnCenter.add("Center",scrollContent);
				c.revalidate();
				c.repaint();
			}
		});
		pnCondition.add(lectureYear);
		pnCondition.add(lectureSemester);
		pnCondition.add(btnInquire);
		
		pnHeader.add("Center", lbTitle);
		pnHeader.add("East", pnCondition);
		
		pnCenter.add("North", pnHeader);
		c.add("Center", pnCenter);
		c.revalidate();
		c.repaint();
	}
	
	public JPanel findLectureByProfessor(String lectureYear, String lectureSemester) {
		JPanel pnContent = new JPanel();
//		pnContent.setLayout(new GridLayout(5, 2, 10, 10));
		pnContent.setBackground(Color.DARK_GRAY);
		
		try {
			stmt = con.createStatement();
			String query =  "SELECT l.lecture_no, l.lecture_class_no, l.lecture_name, p.professor_name, l.lecture_day1, l.lecture_period1, l.lecture_day2, l.lecture_period2, l.lecture_credit, l.lecture_time, l.lecture_room, d.department_name\r\n" + 
							"FROM lecture l\r\n" + 
							"LEFT JOIN professor p ON l.professor_no = p.professor_no\r\n" + 
							"LEFT JOIN department d ON l.department_no = d.department_no\r\n" + 
							"WHERE p.professor_no = " + userInfo + "\r\n" + 
							"AND l.lecture_year = " + lectureYear + "\r\n" + 
							"AND l.lecture_semester = " + lectureSemester;
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				int lectureNo = rs.getInt(1);
				String str = "���ǹ�ȣ\t" + rs.getInt(1) + " - " + rs.getInt(2) + 
						   "\n�����̸�\t" + rs.getString(3) + 
						   "\n��米��\t" + rs.getString(4);
				if (!rs.getString(7).equals("")) {
					str += "\n�ð�ǥ\t" + rs.getString(5) + " : " + rs.getString(6) + " / " +  rs.getString(7) + " : " + rs.getString(8);
				} else {
					str += "\n�ð�ǥ\t" + rs.getString(5) + " : " + rs.getString(6) + " ";
				}   
				str += "\n�������\t" + rs.getInt(9) + "����" + 
						    "\n���½ð�\t" + rs.getString(10) + 
						    "\n���ǽ�\t" + rs.getString(11) + 
						    "\n�����а�\t" + rs.getString(12);
						
				JTextArea lectureInfo = new JTextArea(str);
				lectureInfo.setEditable(false);
				//lectureInfo.setPreferredSize(new Dimension(450, 350));
				lectureInfo.setSize(450, 350);
				lectureInfo.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						professorLectureStudents(lectureNo, lectureYear, lectureSemester);
					}
					public void mouseEntered(MouseEvent e) {
						lectureInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
					}
				});
				// lectureInfo.setOpaque(true); // Opaque���� true�� �̸� ������ �־�� ������ ����ȴ�.
				// lectureInfo.setBackground(Color.WHITE);
				pnContent.add(lectureInfo);
			}
		} catch (SQLException e) {
			System.out.println("���� �б� ���� :" + e);
		}
		
		return pnContent;
	}
	
	public void professorLectureStudents(int lectureNo, String lectureYear, String lectureSemester) {
		c.remove(pnCenter);
		pnCenter.removeAll();
		pnCenter.setLayout(new BorderLayout());
		
		JPanel pnHeader = new JPanel();
		pnHeader.setLayout(new BorderLayout());
		pnHeader.setBackground(Color.GRAY);
		
		JLabel lbBackTracking = new JLabel(leftArrowIcon);
		lbBackTracking.setPreferredSize(new Dimension(40,40));
		lbBackTracking.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				c.remove(pnCenter);
				professorLecture(lectureYear, lectureSemester);
			}
			public void mouseEntered(MouseEvent e) {
				lbBackTracking.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});
		
		JLabel lbTitle = new JLabel("������ ��ȸ");
		lbTitle.setHorizontalAlignment(JLabel.LEFT);
		
		pnHeader.add("West", lbBackTracking);
		pnHeader.add("Center", lbTitle);
		
		String[] tableHeader = { "�й�", "�̸�", "�߰�", "�⸻", "��Ÿ", "�⼮", "����", "����" };
		String[][] tableContents = null;
		ArrayList<String[]> strList = new ArrayList<String[]>();
		try {
			stmt = con.createStatement();
			String query = "SELECT s.student_no, s.student_name, c.midterm_score, c.finals_score, c.other_score, c.attendance_score, c.total_score, c.grade\r\n" +
						   "FROM course c\r\n" + 
						   "LEFT JOIN lecture l ON l.lecture_no = c.lecture_no\r\n" + 
						   "LEFT JOIN student s ON s.student_no = c.student_no\r\n" + 
						   "WHERE c.lecture_no = " + lectureNo;
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				String[] str = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8) };
				strList.add(str);
			}
			tableContents = new String[strList.size()][];
			strList.toArray(tableContents);
		} catch (SQLException e) {
			System.out.println("���� �б� ���� :" + e);
		}
		
		DefaultTableModel model = new DefaultTableModel(tableContents, tableHeader) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		JTable studentTable = new JTable(model);
		studentTable.getTableHeader().setReorderingAllowed(false); // �÷��� �̵� �Ұ�
		//studentTable.getTableHeader().setResizingAllowed(false); // �÷� ũ�� ���� �Ұ�
		// ���� �������� ������ ��� �� ����Ŭ���Ͽ� �����Է� ����
		if (lectureYear.equals(todayYear) && lectureSemester.equals(todaySemester)) {
			studentTable.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						int row = studentTable.getSelectedRow();
						modifyGrade(lectureNo, lectureYear, lectureSemester, model, row);
					}
				}
				public void mouseEntered(MouseEvent e) {
					studentTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			});
		}
		studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 	//���� �� ���� �Ұ�
		
		JScrollPane scrollTable = new JScrollPane(studentTable);
		//scrollTable.setSize(1695, 775);
		//scrollTable.setLocation(5, 75);
		// scrollContent.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		pnCenter.add("North", pnHeader);
		pnCenter.add("Center", scrollTable);
		c.add("Center", pnCenter);
		c.revalidate();
		c.repaint();
	}
	
	public void modifyGrade(int lectureNo, String lectureYear, String lectureSemester, DefaultTableModel model, int row) {
		String studentNo = (String)model.getValueAt(row, 0);
		String studentName = (String)model.getValueAt(row, 1);
		String midtermScore = (String)model.getValueAt(row, 2);
		String finalsScore = (String)model.getValueAt(row, 3);
		String otherScore = (String)model.getValueAt(row, 4);
		String attendanceScore = (String)model.getValueAt(row, 5);
		String totalScore = (String)model.getValueAt(row, 6);
		String grade = (String)model.getValueAt(row, 7);
		
		c.remove(pnCenter);
		pnCenter.removeAll();
		pnCenter.setLayout(new BorderLayout());
		
		JPanel pnHeader = new JPanel();
		pnHeader.setLayout(new BorderLayout());
		pnHeader.setBackground(Color.GRAY);
		
		JLabel lbTitle = new JLabel("������ ��ȸ");
		lbTitle.setHorizontalAlignment(JLabel.LEFT);
		
		pnHeader.add("Center", lbTitle);
		
		JPanel pnContent = new JPanel();
		pnContent.setBackground(Color.WHITE);
		
		JPanel pnForm = new JPanel();
		pnForm.setLayout(new GridLayout(9,3,30,10));
		
		JLabel lbStudentNo = new JLabel("�й�");
		lbStudentNo.setHorizontalAlignment(JLabel.RIGHT);
		JLabel lbStudentName = new JLabel("�̸�");
		lbStudentName.setHorizontalAlignment(JLabel.RIGHT);
		JLabel lbMidtermScore = new JLabel("�߰����");
		lbMidtermScore.setHorizontalAlignment(JLabel.RIGHT);
		JLabel lbFinalScore = new JLabel("�⸻���");
		lbFinalScore.setHorizontalAlignment(JLabel.RIGHT);
		JLabel lbOtherScore = new JLabel("��Ÿ");
		lbOtherScore.setHorizontalAlignment(JLabel.RIGHT);
		JLabel lbAttandence = new JLabel("�⼮");
		lbAttandence.setHorizontalAlignment(JLabel.RIGHT);
		JLabel lbTotalScore = new JLabel("����");
		lbTotalScore.setHorizontalAlignment(JLabel.RIGHT);
		JLabel lbGrade = new JLabel("����");
		lbGrade.setHorizontalAlignment(JLabel.RIGHT);
		
		JTextField tfStudentNo = new JTextField();
		tfStudentNo.setText(studentNo);
		tfStudentNo.setEditable(false);
		JTextField tfStudentName = new JTextField();
		tfStudentName.setText(studentName);
		tfStudentName.setEditable(false);
		JTextField tfMidtermScore = new JTextField();
		tfMidtermScore.setText(midtermScore);
		JTextField tfFinalsScore = new JTextField();
		tfFinalsScore.setText(finalsScore);
		JTextField tfOtherScore = new JTextField();
		tfOtherScore.setText(otherScore);
		JTextField tfAttandence = new JTextField();
		tfAttandence.setText(attendanceScore);
		JTextField tfTotalScore = new JTextField();
		tfTotalScore.setText(totalScore);
		JTextField tfGrade = new JTextField();
		tfGrade.setText(grade);
		
		JPanel pnBtn = new JPanel();
		pnBtn.setPreferredSize(new Dimension(430,70));
		JButton btnInput = new JButton("�Է�");
		btnInput.setPreferredSize(new Dimension(200,50));
		btnInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "������ �����Ͻðڽ��ϱ�?", "", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					try {
						stmt = con.createStatement();
						String query = String.format("UPDATE course \nSET midterm_score = %s, finals_score = %s, other_score = %s, attendance_score = %s, total_score = %s, grade = '%s' \nWHERE lecture_no = %s AND student_no = %s", 
								tfMidtermScore.getText(), tfFinalsScore.getText(), tfOtherScore.getText(), tfAttandence.getText(), tfTotalScore.getText(), tfGrade.getText(), lectureNo, tfStudentNo.getText());
						System.out.println(query);
						stmt.execute(query);
						professorLectureStudents(lectureNo, lectureYear, lectureSemester);
					} catch (SQLException e1) {
						System.out.println("������Ʈ ���� :" + e1);
					}
				} else if (result == JOptionPane.CANCEL_OPTION) {
					
				}
			}
		});
		JButton btnCancel = new JButton("���");
		btnCancel.setPreferredSize(new Dimension(200,50));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				professorLectureStudents(lectureNo, lectureYear, lectureSemester);
			}
		});
		pnBtn.add(btnInput);
		pnBtn.add(btnCancel);
		
		pnForm.add(lbStudentNo);
		pnForm.add(tfStudentNo);
		pnForm.add(new JLabel(" "));
		pnForm.add(lbStudentName);
		pnForm.add(tfStudentName);
		pnForm.add(new JLabel(" "));
		pnForm.add(lbMidtermScore);
		pnForm.add(tfMidtermScore);
		pnForm.add(new JLabel(" "));
		pnForm.add(lbFinalScore);
		pnForm.add(tfFinalsScore);
		pnForm.add(new JLabel(" "));
		pnForm.add(lbOtherScore);
		pnForm.add(tfOtherScore);
		pnForm.add(new JLabel(" "));
		pnForm.add(lbAttandence);
		pnForm.add(tfAttandence);
		pnForm.add(new JLabel(" "));
		pnForm.add(lbTotalScore);
		pnForm.add(tfTotalScore);
		pnForm.add(new JLabel(" "));
		pnForm.add(lbGrade);
		pnForm.add(tfGrade);
		pnForm.add(new JLabel(" "));
		pnForm.add(new JLabel(" "));
		pnForm.add(pnBtn);
		//pnForm.add(btnInput);
		//pnForm.add(btnCancel);
		
		pnContent.add(pnForm);
		
		pnCenter.add("North", pnHeader);
		pnCenter.add("Center", pnContent);
		
		c.add("Center", pnCenter);
		c.revalidate();
		c.repaint();
	}
	
	public void professorTutoring() {
		c.remove(pnCenter);
		pnCenter.removeAll();
		pnCenter.setLayout(new BorderLayout());
		
		/* ��� */
		JPanel pnHeader = new JPanel();
		pnHeader.setLayout(new BorderLayout());
		pnHeader.setBackground(Color.GRAY);
		JLabel lbTitle = new JLabel(todayYear + "��" + todaySemester + "�б� �����л� ���");
		lbTitle.setHorizontalAlignment(JLabel.LEFT);
		pnHeader.add("Center", lbTitle);
		
		/* ���� */
		JPanel pnContent = new JPanel();
		String[] tableHeader = { "�й�", "�̸�", "�г�", "��ȭ��ȣ", "�̸���", "�ּ�", "����", "������", "����" };
		String[][] tableContents = null;
		ArrayList<String[]> strList = new ArrayList<String[]>();
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			
		}
		
		
		JTextField lectureYear = new JTextField();
		lectureYear.setText(lectureYearValue);
		lectureYear.setPreferredSize(new Dimension(100, 30));
		lectureYear.setHorizontalAlignment(JTextField.RIGHT);
		
		JTextField lectureSemester = new JTextField();
		lectureSemester.setText(lectureSemesterValue);
		lectureSemester.setPreferredSize(new Dimension(100, 30));
		lectureSemester.setHorizontalAlignment(JTextField.RIGHT);
		
		JButton btnInquire = new JButton("��ȸ");
		btnInquire.setHorizontalAlignment(JButton.RIGHT);
		btnInquire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(pnCenter.getComponentCount());
				if (pnCenter.getComponentCount() > 1) { // �̹� scrollContent �����ϸ� ����
					pnCenter.remove(1);
				}
				JScrollPane scrollContent = new JScrollPane(findLectureByProfessor(lectureYear.getText(), lectureSemester.getText()));
				//scrollContent.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				scrollContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				pnCenter.add("Center",scrollContent);
				c.revalidate();
				c.repaint();
			}
		});
		pnCondition.add(lectureYear);
		pnCondition.add(lectureSemester);
		pnCondition.add(btnInquire);
		
		pnHeader.add("Center", lbTitle);
		pnHeader.add("East", pnCondition);
		
		pnCenter.add("North", pnHeader);
		c.add("Center", pnCenter);
		c.revalidate();
		c.repaint();
	}
	
	/* �л� */
	public void studentLogin() {
		c.removeAll();
		
		JPanel pnLogin = new JPanel();
		JTextField studentNo = new JTextField();
		JButton btnLogin = new JButton("�α���");
		JLabel lbLoginFail = new JLabel("");
		lbLoginFail.setHorizontalAlignment(JLabel.CENTER);
		
		pnLogin.setLayout(new GridLayout(3,1,10,10));

		studentNo.setText("�й�");
		studentNo.setPreferredSize(new Dimension(400, 60));
		studentNo.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if (!((JTextField)e.getSource()).getText().equals("")) {
					((JTextField)e.getSource()).setText("");
				}
			}
		});
		studentNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnLogin.doClick();
			}
		});
		
		btnLogin.setPreferredSize(new Dimension(400, 60));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Integer.parseInt(studentNo.getText());
					
					try {
						stmt = con.createStatement();
						String query = "SELECT COUNT(*) AS RESULT FROM student WHERE student_no = " + studentNo.getText();
						rs = stmt.executeQuery(query);
						rs.next();
						if (rs.getInt(1) == 1) {
							userInfo = studentNo.getText();
							studentMain();
						} else if (rs.getInt(1) == 0) {
							lbLoginFail.setText("�������� �ʴ� �й��Դϴ�.");
						}
					} catch (SQLException e2) {
						System.out.println("���� �б� ���� :" + e2);
					}
					
				} catch(NumberFormatException e1) {
					// ���� �̿ܿ� �ٸ� ���� �ԷµǸ� ����
					lbLoginFail.setText("������ �ùٸ��� �ʽ��ϴ�.");
				}
			}
		});
		
		pnLogin.add(studentNo);
		pnLogin.add(btnLogin);
		pnLogin.add(lbLoginFail);
		
		pnCenter.removeAll();
		pnCenter.add(pnLogin);
		
		c.add("Center", pnCenter);
		c.revalidate();
		c.repaint();
	}
	
	public void studentMain() {
	
	}

	/* ���� */
	public JLabel initBtnLogout() {
		JLabel btnLogout = new JLabel(logoutIcon);
		btnLogout.setHorizontalAlignment(JLabel.RIGHT);
		btnLogout.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				layInit();
			}
			
			public void mouseEntered(MouseEvent e) {
				btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});
		return btnLogout;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
//		try {
//			stmt = con.createStatement();
//			String query = "SELECT * FROM Book ";
//			if (e.getSource() == btnAdmin) {
//
//			} else if (e.getSource() == btnProfessor) {
//				loginProfessor();
//			} else if (e.getSource() == btnStudent) {
//				loginStudent();
//			}
//
//	         if (e.getSource() == btnOk) {
//	            txtResult.setText("");
//	            txtResult.setText("BOOKNO           BOOK NAME       PUBLISHER      PRICE\n");
//	            rs = stmt.executeQuery(query);
//	            while (rs.next()) {
//	               String str = rs.getInt(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getInt(4)
//	                     + "\n";
//	               txtResult.append(str);
//	            }
//	         } else if (e.getSource() == btnReset) {
//	            txtResult.setText("");
//	         }
//		} catch (Exception e2) {
//			System.out.println("���� �б� ���� :" + e2);
//			/*
//			 * } finally { try { if (rs != null) rs.close(); if (stmt != null) stmt.close();
//			 * if (con != null) con.close(); } catch (Exception e3) { // TODO: handle
//			 * exception }
//			 */
//		}
	}
	
	public static void main(String[] args) {
		JC19011458_19011461 BLS = new JC19011458_19011461();
		
		// BLS.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// BLS.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		BLS.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				try {
					con.close();
				} catch (Exception e4) {
				}
				System.out.println("���α׷� ���� ����!");
				System.exit(0);
			}
		});
		
	}

}

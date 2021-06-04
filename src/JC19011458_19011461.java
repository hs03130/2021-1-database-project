import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

public class JC19011458_19011461 extends JFrame implements ActionListener {

	/* 수정 금지 */
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
	String todayYear = "2020", todaySemester = "1";
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

	/* 수정 금지 */
	public void conDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("드라이버 로드 성공");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try { /* 데이터베이스를 연결하는 과정 */
			System.out.println("데이터베이스 연결 준비...");
			con = DriverManager.getConnection(url, userid, pwd);
			System.out.println("데이터베이스 연결 성공");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public void layInit() {
		c.removeAll();
		pnCenter.removeAll();
		pnCenter.setLayout(new BorderLayout());
		
		JPanel pnBtn = new JPanel();
		pnBtn.setLayout(new GridLayout(3, 1, 10, 10));
		
		JButton btnAdmin, btnProfessor, btnStudent;
		btnAdmin = new JButton("관리자");
		btnAdmin.setPreferredSize(new Dimension(400, 60));
		btnAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userInfo = "ADMIN";
				adminMain();
			}
		});

		btnProfessor = new JButton("교수");
		btnProfessor.setPreferredSize(new Dimension(400, 60));
		btnProfessor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				professorLogin();
			}
		});

		btnStudent = new JButton("학생");
		btnStudent.setPreferredSize(new Dimension(400, 60));
		btnStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				studentLogin();
			}
		});

		pnBtn.add(btnAdmin);
		pnBtn.add(btnProfessor);
		pnBtn.add(btnStudent);
		
		JPanel pnGrid = new JPanel();
		pnGrid.setLayout(new GridLayout(3,3,10,10));
		pnGrid.add(new JLabel(""));
		pnGrid.add(new JLabel(""));
		pnGrid.add(new JLabel(""));
		pnGrid.add(new JLabel(""));
		pnGrid.add(pnBtn);
		pnGrid.add(new JLabel(""));
		pnGrid.add(new JLabel(""));
		pnGrid.add(new JLabel(""));
		pnGrid.add(new JLabel(""));
		
		pnCenter.add("Center", pnGrid);

		c.add("Center", pnCenter);
		c.revalidate();
		// c.repaint();
	}

	/* 관리자 */
	public void adminMain() {
		c.removeAll();

		// 학생 이름 select
		JPanel pnUser = new JPanel();
		JLabel lbStudent = new JLabel("관리자");
		JLabel lbLogout = initBtnLogout();
		pnUser.add(lbStudent);
		pnUser.add(lbLogout);

		pnNorth.removeAll();
		pnNorth.setLayout(new BorderLayout());
		pnNorth.add("East", pnUser);

		JPanel pnBtn = new JPanel();
		pnBtn.setLayout(new GridLayout(3, 1, 5, 5));
		pnBtn.setPreferredSize(new Dimension(90, 180));

		JButton btnInitDB = new JButton("초기화");
		btnInitDB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adminInitDB();
			}
		});

		JButton btnAdministration = new JButton("관리");
		btnAdministration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adminAdministration();
			}
		});

		JButton btnEntire = new JButton("전체");
		btnEntire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adminEntire();
			}
		});

		pnBtn.add(btnInitDB);
		pnBtn.add(btnAdministration);
		pnBtn.add(btnEntire);

		pnWest.removeAll();
		pnWest.add(pnBtn);

		pnCenter.removeAll();
		pnCenter.setBackground(Color.GRAY);

		c.add("North", pnNorth);
		c.add("West", pnWest);
		c.add("Center", pnCenter);
		c.revalidate();
		c.repaint();
	}

	public void adminInitDB() {}
	public void adminAdministration() {}
	public void adminEntire() {}
	
	/* 교수 */
	public void professorLogin() {
		c.removeAll();
		pnCenter.removeAll();
		pnCenter.setLayout(new BorderLayout());

		JPanel pnLogin = new JPanel();
		pnLogin.setLayout(new GridLayout(3, 1, 10, 10));

		JTextField professorNo = new JTextField();
		JButton btnLogin = new JButton("로그인");
		JLabel lbLoginFail = new JLabel("");
		lbLoginFail.setHorizontalAlignment(JLabel.CENTER);

		professorNo.setText("직번");
		professorNo.setPreferredSize(new Dimension(400, 60));
		professorNo.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if (!((JTextField) e.getSource()).getText().equals("")) {
					((JTextField) e.getSource()).setText("");
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
					lbLoginFail.setText("직번을 입력해주세요.");
					return;
				}
				try {
					Integer.parseInt(professorNo.getText());

					try {
						stmt = con.createStatement();
						String query = "SELECT COUNT(*) AS RESULT FROM professor WHERE professor_no = "
								+ professorNo.getText();
						rs = stmt.executeQuery(query);
						rs.next();
						if (rs.getInt(1) == 1) {
							userInfo = professorNo.getText();
							professorMain();
						} else if (rs.getInt(1) == 0) {
							lbLoginFail.setText("존재하지 않는 직번입니다.");
						}
					} catch (SQLException e2) {
						System.out.println("쿼리 읽기 실패 :" + e2);
					}

				} catch (NumberFormatException e1) {
					// 숫자 이외에 다른 것이 입력되면 실패
					lbLoginFail.setText("직번 형식이 올바르지 않습니다.");
				}
			}
		});

		pnLogin.add(professorNo);
		pnLogin.add(btnLogin);
		pnLogin.add(lbLoginFail);

		JPanel pnGrid = new JPanel();
		pnGrid.setLayout(new GridLayout(3,3,10,10));
		pnGrid.add(new JLabel(""));
		pnGrid.add(new JLabel(""));
		pnGrid.add(new JLabel(""));
		pnGrid.add(new JLabel(""));
		pnGrid.add(pnLogin);
		pnGrid.add(new JLabel(""));
		pnGrid.add(new JLabel(""));
		pnGrid.add(new JLabel(""));
		pnGrid.add(new JLabel(""));
		
		pnCenter.add("Center", pnGrid);

		c.add("Center", pnCenter);
		c.revalidate();
		c.repaint();
	}

	public void professorMain() {
		c.removeAll();

		// 교수 이름 select
		String professorName = "";
		try {
			stmt = con.createStatement();
			String query = "SELECT professor_name FROM professor WHERE professor_no = " + userInfo;
			rs = stmt.executeQuery(query);
			rs.next();
			professorName = rs.getString(1);
		} catch (SQLException e) {
			System.out.println("쿼리 읽기 실패 :" + e);
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
		pnBtn.setLayout(new GridLayout(4, 1, 5, 5));
		pnBtn.setPreferredSize(new Dimension(90, 240));

		JButton btnLecture = new JButton("강의");
		btnLecture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				professorLecture("2021", "1");
			}
		});

		JButton btnTutor = new JButton("지도");
		btnTutor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				professorTutoring();
			}
		});

		JButton btnDepartment = new JButton("소속");
		btnDepartment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				professorDepartment();
			}
		});

		JButton btnTimetable = new JButton("시간표");
		btnTimetable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				professorTimetable();
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

		c.add("North", pnNorth);
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
		// JPanel pnContent = new JPanel();

		pnHeader.setLayout(new BorderLayout());
		pnHeader.setBackground(Color.GRAY);

		JLabel lbTitle = new JLabel("강의 조회");
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

		JButton btnInquire = new JButton("조회");
		btnInquire.setHorizontalAlignment(JButton.RIGHT);
		btnInquire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pnCenter.getComponentCount() > 1) { // 이미 scrollContent 존재하면 제거
					pnCenter.remove(1);
				}
				JScrollPane scrollContent = new JScrollPane(
						findLectureByProfessor(lectureYear.getText(), lectureSemester.getText()));
				// scrollContent.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				scrollContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				pnCenter.add("Center", scrollContent);
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
			String query = "SELECT l.lecture_no, l.lecture_class_no, l.lecture_name, p.professor_name, l.lecture_day1, l.lecture_period1, l.lecture_day2, l.lecture_period2, l.lecture_credit, l.lecture_time, l.lecture_room, d.department_name\r\n"
					+ "FROM lecture l\r\n" + "LEFT JOIN professor p ON l.professor_no = p.professor_no\r\n"
					+ "LEFT JOIN department d ON l.department_no = d.department_no\r\n" + "WHERE p.professor_no = "
					+ userInfo + "\r\n" + "AND l.lecture_year = " + lectureYear + "\r\n" + "AND l.lecture_semester = "
					+ lectureSemester;
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				int lectureNo = rs.getInt(1);
				String str = "강의번호\t" + rs.getInt(1) + " - " + rs.getInt(2) + "\n강좌이름\t" + rs.getString(3) + "\n담당교수\t"
						+ rs.getString(4);
				if (!rs.getString(7).equals("")) {
					str += "\n시간표\t" + rs.getString(5) + " : " + rs.getString(6) + " / " + rs.getString(7) + " : "
							+ rs.getString(8);
				} else {
					str += "\n시간표\t" + rs.getString(5) + " : " + rs.getString(6) + " ";
				}
				str += "\n취득학점\t" + rs.getInt(9) + "학점" + "\n강좌시간\t" + rs.getString(10) + "\n강의실\t" + rs.getString(11)
						+ "\n개설학과\t" + rs.getString(12);

				JTextArea lectureInfo = new JTextArea(str);
				lectureInfo.setEditable(false);
				// lectureInfo.setPreferredSize(new Dimension(450, 350));
				lectureInfo.setSize(450, 350);
				lectureInfo.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						professorLectureStudents(lectureNo, lectureYear, lectureSemester);
					}

					public void mouseEntered(MouseEvent e) {
						lectureInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
					}
				});
				// lectureInfo.setOpaque(true); // Opaque값을 true로 미리 설정해 주어야 배경색이 적용된다.
				// lectureInfo.setBackground(Color.WHITE);
				pnContent.add(lectureInfo);
			}
		} catch (SQLException e) {
			System.out.println("쿼리 읽기 실패 :" + e);
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
		lbBackTracking.setPreferredSize(new Dimension(40, 40));
		lbBackTracking.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				c.remove(pnCenter);
				professorLecture(lectureYear, lectureSemester);
			}

			public void mouseEntered(MouseEvent e) {
				lbBackTracking.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});

		JLabel lbTitle = new JLabel("수강생 조회");
		lbTitle.setHorizontalAlignment(JLabel.LEFT);

		pnHeader.add("West", lbBackTracking);
		pnHeader.add("Center", lbTitle);

		String[] tableHeader = { "학번", "이름", "중간", "기말", "기타", "출석", "총점", "평점" };
		String[][] tableContents = null;
		ArrayList<String[]> strList = new ArrayList<String[]>();
		try {
			stmt = con.createStatement();
			String query = "SELECT s.student_no, s.student_name, c.midterm_score, c.finals_score, c.other_score, c.attendance_score, c.total_score, c.grade\r\n"
					+ "FROM course c\r\n" + "LEFT JOIN lecture l ON l.lecture_no = c.lecture_no\r\n"
					+ "LEFT JOIN student s ON s.student_no = c.student_no\r\n" + "WHERE c.lecture_no = " + lectureNo;
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				String[] str = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7), rs.getString(8) };
				strList.add(str);
			}
			tableContents = new String[strList.size()][];
			strList.toArray(tableContents);
		} catch (SQLException e) {
			System.out.println("쿼리 읽기 실패 :" + e);
		}

		DefaultTableModel model = new DefaultTableModel(tableContents, tableHeader) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		JTable studentTable = new JTable(model);
		studentTable.getTableHeader().setReorderingAllowed(false); // 컬럼들 이동 불가
		// studentTable.getTableHeader().setResizingAllowed(false); // 컬럼 크기 조절 불가
		// 현재 강의중인 강의의 경우 셀 더블클릭하여 성적입력 가능
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
		studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 여러 행 선택 불가

		JScrollPane scrollTable = new JScrollPane(studentTable);
		// scrollTable.setSize(1695, 775);
		// scrollTable.setLocation(5, 75);
		// scrollContent.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		pnCenter.add("North", pnHeader);
		pnCenter.add("Center", scrollTable);
		c.add("Center", pnCenter);
		c.revalidate();
		c.repaint();
	}

	public void modifyGrade(int lectureNo, String lectureYear, String lectureSemester, DefaultTableModel model,
			int row) {
		String studentNo = (String) model.getValueAt(row, 0);
		String studentName = (String) model.getValueAt(row, 1);
		String midtermScore = (String) model.getValueAt(row, 2);
		String finalsScore = (String) model.getValueAt(row, 3);
		String otherScore = (String) model.getValueAt(row, 4);
		String attendanceScore = (String) model.getValueAt(row, 5);
		String totalScore = (String) model.getValueAt(row, 6);
		String grade = (String) model.getValueAt(row, 7);

		c.remove(pnCenter);
		pnCenter.removeAll();
		pnCenter.setLayout(new BorderLayout());

		JPanel pnHeader = new JPanel();
		pnHeader.setLayout(new BorderLayout());
		pnHeader.setBackground(Color.GRAY);

		JLabel lbTitle = new JLabel("수강생 조회");
		lbTitle.setHorizontalAlignment(JLabel.LEFT);

		pnHeader.add("Center", lbTitle);

		JPanel pnContent = new JPanel();
		pnContent.setBackground(Color.WHITE);

		JPanel pnForm = new JPanel();
		pnForm.setLayout(new GridLayout(9, 3, 30, 10));

		JLabel lbStudentNo = new JLabel("학번");
		lbStudentNo.setHorizontalAlignment(JLabel.RIGHT);
		JLabel lbStudentName = new JLabel("이름");
		lbStudentName.setHorizontalAlignment(JLabel.RIGHT);
		JLabel lbMidtermScore = new JLabel("중간고사");
		lbMidtermScore.setHorizontalAlignment(JLabel.RIGHT);
		JLabel lbFinalScore = new JLabel("기말고사");
		lbFinalScore.setHorizontalAlignment(JLabel.RIGHT);
		JLabel lbOtherScore = new JLabel("기타");
		lbOtherScore.setHorizontalAlignment(JLabel.RIGHT);
		JLabel lbAttandence = new JLabel("출석");
		lbAttandence.setHorizontalAlignment(JLabel.RIGHT);
		JLabel lbTotalScore = new JLabel("총점");
		lbTotalScore.setHorizontalAlignment(JLabel.RIGHT);
		JLabel lbGrade = new JLabel("평점");
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
		pnBtn.setPreferredSize(new Dimension(430, 70));
		JButton btnInput = new JButton("입력");
		btnInput.setPreferredSize(new Dimension(200, 50));
		btnInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "성적을 수정하시겠습니까?", "", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					try {
						stmt = con.createStatement();
						String query = String.format(
								"UPDATE course \nSET midterm_score = %s, finals_score = %s, other_score = %s, attendance_score = %s, total_score = %s, grade = '%s' \nWHERE lecture_no = %s AND student_no = %s",
								tfMidtermScore.getText(), tfFinalsScore.getText(), tfOtherScore.getText(),
								tfAttandence.getText(), tfTotalScore.getText(), tfGrade.getText(), lectureNo,
								tfStudentNo.getText());
						stmt.execute(query);
						professorLectureStudents(lectureNo, lectureYear, lectureSemester);
					} catch (SQLException e1) {
						System.out.println("업데이트 실패 :" + e1);
					}
				} else if (result == JOptionPane.CANCEL_OPTION) {

				}
			}
		});
		JButton btnCancel = new JButton("취소");
		btnCancel.setPreferredSize(new Dimension(200, 50));
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
		// pnForm.add(btnInput);
		// pnForm.add(btnCancel);

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

		/* 헤더 */
		JPanel pnHeader = new JPanel();
		pnHeader.setLayout(new BorderLayout());
		pnHeader.setBackground(Color.GRAY);
		JLabel lbTitle = new JLabel(todayYear + "년" + todaySemester + "학기 지도학생 목록");
		lbTitle.setHorizontalAlignment(JLabel.LEFT);
		pnHeader.add("Center", lbTitle);

		/* 내용 */
		String[] tableHeader = { "학번", "이름", "학년", "이메일", "전화번호", "주소", "계좌", "전공", "부전공" };
		String[][] tableContents = null;
		ArrayList<String[]> strList = new ArrayList<String[]>();
		try {
			stmt = con.createStatement();
			String query = "SELECT s.student_no, s.student_name, t.grade_semester, s.student_email, s.student_phone, s.student_address, s.student_account, major.department_name AS major, minor.department_name AS minor\r\n"
					+ "FROM student s\r\n" + "LEFT JOIN department major ON major.department_no = s.major_no\r\n"
					+ "LEFT JOIN department minor ON minor.department_no = s.minor_no\r\n"
					+ "LEFT JOIN tuition t ON t.student_no = s.student_no\r\n"
					+ "WHERE s.student_no IN (SELECT student_no FROM tutoring WHERE professor_no = " + userInfo
					+ ")\r\n"
					+ "AND t.grade_semester = (SELECT grade_semester FROM tuition WHERE student_no = s.student_no ORDER BY grade_semester DESC LIMIT 1)";
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				String[] str = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9) };
				strList.add(str);
			}
			tableContents = new String[strList.size()][];
			strList.toArray(tableContents);
		} catch (SQLException e) {
			System.out.println("쿼리 읽기 실패 :" + e);
		}

		DefaultTableModel model = new DefaultTableModel(tableContents, tableHeader) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};

		JTable studentTable = new JTable(model);
		studentTable.getTableHeader().setReorderingAllowed(false); // 컬럼들 이동 불가
		// studentTable.getTableHeader().setResizingAllowed(false); // 컬럼 크기 조절 불가
		studentTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					findCourseByStudentNo((String) model.getValueAt(studentTable.getSelectedRow(), 0));
				}
			}

			public void mouseEntered(MouseEvent e) {
				studentTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});

		studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 여러 행 선택 불가

		JScrollPane pnContent = new JScrollPane(studentTable);
		pnContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		pnCenter.add("North", pnHeader);
		pnCenter.add("Center", pnContent);
		c.add("Center", pnCenter);
		c.revalidate();
		c.repaint();
	}

	public void findCourseByStudentNo(String studentNo) {
		c.remove(pnCenter);
		pnCenter.removeAll();
		pnCenter.setLayout(new BorderLayout());

		/* 헤더 */
		JPanel pnHeader = new JPanel();
		pnHeader.setLayout(new BorderLayout());
		pnHeader.setBackground(Color.GRAY);

		JLabel lbTitle = new JLabel(todayYear + "년" + todaySemester + "학기 지도학생 목록");
		lbTitle.setHorizontalAlignment(JLabel.LEFT);

		JLabel lbBackTracking = new JLabel(leftArrowIcon);
		lbBackTracking.setPreferredSize(new Dimension(40, 40));
		lbBackTracking.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				c.remove(pnCenter);
				professorTutoring();
			}

			public void mouseEntered(MouseEvent e) {
				lbBackTracking.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});
		pnHeader.add("Center", lbTitle);
		pnHeader.add("West", lbBackTracking);

		/* 내용 */
		JPanel pnTables = new JPanel();
		pnTables.setLayout(new GridLayout(8, 1, 10, 10));
		try {
			// 등록한 연도/학기 조회
			stmt = con.createStatement();
			String query = "SELECT tuition_year, tuition_semester FROM tuition WHERE student_no = " + studentNo;
			rs = stmt.executeQuery(query);
			ResultSet rs1;
			while (rs.next()) {
				String[] tableHeader = { "강의번호", "강의명", "중간고사", "기말고사", "기타점수", "출석", "총점", "평점" };
				String[][] tableContents = null;

				/* 테이블 내용 - 해당학기 성적 */
				Statement stmt1;
				ArrayList<String[]> strList = new ArrayList<String[]>();
				try {
					stmt1 = con.createStatement();
					String query1 = String.format(
							"SELECT c.lecture_no, l.lecture_name, c.midterm_score, c.finals_score, c.other_score, c.attendance_score, c.total_score, c.grade\r\n"
									+ "FROM course c\r\n" + "LEFT JOIN lecture l ON c.lecture_no = l.lecture_no\r\n"
									+ "WHERE c.student_no = %s\r\n" + "AND l.lecture_year = %s\r\n"
									+ "AND l.lecture_semester = %s",
							studentNo, rs.getString(1), rs.getString(2));
					rs1 = stmt1.executeQuery(query1);
					while (rs1.next()) {
						String[] str = { rs1.getString(1), rs1.getString(2), rs1.getString(3), rs1.getString(4),
								rs1.getString(5), rs1.getString(6), rs1.getString(7), rs1.getString(8) };
						strList.add(str);
					}
				} catch (SQLException e1) {
					System.out.println("쿼리 읽기 실패 :" + e1);
				}

				tableContents = new String[strList.size()][];
				strList.toArray(tableContents);
				DefaultTableModel model = new DefaultTableModel(tableContents, tableHeader) { // 표 수정불가
					public boolean isCellEditable(int rowIndex, int mColIndex) {
						return false;
					}
				};
				JTable table = new JTable(model);
				table.getTableHeader().setReorderingAllowed(false); // 컬럼들 이동 불가
				table.getTableHeader().setResizingAllowed(false); // 컬럼 크기 조절 불가

				table.setPreferredScrollableViewportSize(
						new Dimension(table.getPreferredSize().width, table.getRowHeight() * 8));

				// 캡션 (연도 - 학기)
				JScrollPane pnTable = new JScrollPane(table);
				pnTable.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
						rs.getString(1) + "년도" + rs.getString(2) + "학기", TitledBorder.LEFT, TitledBorder.TOP));
				pnTables.add(pnTable);
			}
		} catch (SQLException e) {
			System.out.println("쿼리 읽기 실패 :" + e);
		}

		JScrollPane pnContent = new JScrollPane(pnTables);
		pnContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		pnCenter.add("North", pnHeader);
		pnCenter.add("Center", pnContent);
		c.add("Center", pnCenter);
		c.revalidate();
		c.repaint();
	}

	public void professorDepartment() {
		c.remove(pnCenter);
		pnCenter.removeAll();
		pnCenter.setLayout(new BorderLayout());

		/* 헤더 */
		JPanel pnHeader = new JPanel();
		pnHeader.setLayout(new BorderLayout());
		pnHeader.setBackground(Color.GRAY);
		JLabel lbTitle = new JLabel("소속학과 목록");
		lbTitle.setHorizontalAlignment(JLabel.LEFT);
		pnHeader.add("Center", lbTitle);

		/* 내용 */
		String[] tableHeader = { "학과번호", "학과이름", "연락처", "사무실", "학과장 직번", "학과장 이름", "학과장 사무실", "학과장 연락처", "학과장 이메일" };
		String[][] tableContents = null;
		ArrayList<String[]> strList = new ArrayList<String[]>();
		try {
			stmt = con.createStatement();
			String query = "SELECT d.department_no, d.department_name, d.department_contact, d.department_office, p.professor_no, p.professor_name, p.professor_address, p.professor_phone, p.professor_email\r\n"
					+ "FROM department d\r\n" + "LEFT JOIN professor p ON d.professor_no = p.professor_no\r\n"
					+ "WHERE d.department_no IN (SELECT department_no FROM affiliated_professor WHERE professor_no = "
					+ userInfo + ")";
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				String[] str = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9) };
				strList.add(str);
			}
			tableContents = new String[strList.size()][];
			strList.toArray(tableContents);
		} catch (SQLException e) {
			System.out.println("쿼리 읽기 실패 :" + e);
		}

		DefaultTableModel model = new DefaultTableModel(tableContents, tableHeader) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};

		JTable departmentTable = new JTable(model);
		departmentTable.getTableHeader().setReorderingAllowed(false); // 컬럼들 이동 불가
		// departmentTable.getTableHeader().setResizingAllowed(false); // 컬럼 크기 조절 불가
		// departmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 여러
		// 행 선택 불가

		JScrollPane pnContent = new JScrollPane(departmentTable);
		pnContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		pnCenter.add("North", pnHeader);
		pnCenter.add("Center", pnContent);
		c.add("Center", pnCenter);
		c.revalidate();
		c.repaint();
	}

	public void professorTimetable() {
		c.remove(pnCenter);
		pnCenter.removeAll();
		pnCenter.setLayout(new BorderLayout());

		/* 헤더 */
		JPanel pnHeader = new JPanel();
		pnHeader.setLayout(new BorderLayout());
		pnHeader.setBackground(Color.GRAY);
		JLabel lbTitle = new JLabel(todayYear + "년 " + todaySemester +  "학기 시간표");
		lbTitle.setHorizontalAlignment(JLabel.LEFT);
		pnHeader.add("Center", lbTitle);

		Vector<String> day_map = new Vector<String>();
		day_map.add("월");
		day_map.add("화");
		day_map.add("수");
		day_map.add("목");
		day_map.add("금");
		day_map.add("토");

		String[] period_map = { "8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30",
				"13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00",
				"18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30",
				"24:00" };

		String[] columns = { "교시", "월", "화", "수", "목", "금", "토" };
		String[][] contents = new String[32][7]; // uis와 마찬가지로 32교시까지로 설정

		for (int i = 0; i < 32; i++) {
			contents[i][0] = "<HTML><div style=\"text-align:center\">" + Integer.toString(i + 1) + " 교시<br />"
					+ period_map[i] + "~" + period_map[i + 1] + "</div></HTML>"; // \n으로는 줄바꿈이 안돼서 HTML 사용
		}

		JLabel online = new JLabel("온라인 강의 : ");
		online.setPreferredSize(new Dimension(500, 100));
		online.setBackground(Color.WHITE);
		online.setOpaque(true);

		try {
			stmt = con.createStatement();
			String query = String.format("SELECT lecture_name, lecture_day1, lecture_period1, lecture_day2, lecture_period2, lecture_room FROM lecture WHERE professor_no = %s AND lecture_year = %s AND lecture_semester = %s", userInfo, todayYear, todaySemester);
			rs = stmt.executeQuery(query);

			String name, day, room, subject;
			String[] period;

			while (rs.next()) {
				name = rs.getString(1);

				if (rs.getString(2).equals("") && rs.getString(4).equals("")) {
					// 온라인 강의 추가
					online.setText(online.getText() + name);
					continue;
				}

				day = rs.getString(2);
				period = rs.getString(3).split(",");
				room = rs.getString(6);

				int i = day_map.indexOf(day);
				for (int j = 0; j < period.length; j++) {
					subject = "<HTML><div style=\"text-align:center\">" + name + "<br />" + room + "</div></HTML>";
					contents[Integer.parseInt(period[j]) - 1][i + 1] = subject;
				}

				day = rs.getString(4);
				
				if (!day.equals("")) {
					period = rs.getString(5).split(",");
					i = day_map.indexOf(day);
					for (int j = 0; j < period.length; j++) {
						subject = "<HTML><div style=\"text-align:center\">" + name + "<br />" + room + "</div></HTML>";
						contents[Integer.parseInt(period[j]) - 1][i + 1] = subject;
					}
				}

			}

			DefaultTableModel model = new DefaultTableModel(contents, columns) { // 표 수정불가
				public boolean isCellEditable(int rowIndex, int mColIndex) {
					return false;
				}
			};
			
			JTable timetable = new JTable(model);
			timetable.setRowHeight(50);

			JScrollPane scrollpane = new JScrollPane(timetable);
			JPanel pnContent = new JPanel();
			pnContent.setLayout(new BorderLayout());
			pnContent.add("North", online);
			pnContent.add("Center", scrollpane);
			
			pnCenter.add("North", pnHeader);
			pnCenter.add("Center", pnContent);
			c.add("Center", pnCenter);
			c.revalidate();
			c.repaint();

		} catch (SQLException e) {
			System.out.println("쿼리읽기실패:" + e);
		}
	}

	/* 학생 */
	public void studentLogin() {
		c.removeAll();
		pnCenter.removeAll();
		pnCenter.setLayout(new BorderLayout());

		JPanel pnLogin = new JPanel();
		JTextField studentNo = new JTextField();
		JButton btnLogin = new JButton("로그인");
		JLabel lbLoginFail = new JLabel("");
		lbLoginFail.setHorizontalAlignment(JLabel.CENTER);

		pnLogin.setLayout(new GridLayout(3, 1, 10, 10));

		studentNo.setText("학번");
		studentNo.setPreferredSize(new Dimension(400, 60));
		studentNo.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if (!((JTextField) e.getSource()).getText().equals("")) {
					((JTextField) e.getSource()).setText("");
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
				if (studentNo.getText().equals("")) {
					lbLoginFail.setText("학번을 입력해주세요.");
					return;
				}
				try {
					Integer.parseInt(studentNo.getText());

					try {
						stmt = con.createStatement();
						String query = "SELECT COUNT(*) AS RESULT FROM student WHERE student_no = "
								+ studentNo.getText();
						rs = stmt.executeQuery(query);
						rs.next();
						if (rs.getInt(1) == 1) {
							userInfo = studentNo.getText();
							studentMain();
						} else if (rs.getInt(1) == 0) {
							lbLoginFail.setText("존재하지 않는 학번입니다.");
						}
					} catch (SQLException e2) {
						System.out.println("쿼리 읽기 실패 :" + e2);
					}

				} catch (NumberFormatException e1) {
					// 숫자 이외에 다른 것이 입력되면 실패
					lbLoginFail.setText("학번 형식이 올바르지 않습니다.");
				}
			}
		});

		pnLogin.add(studentNo);
		pnLogin.add(btnLogin);
		pnLogin.add(lbLoginFail);
		
		JPanel pnGrid = new JPanel();
		pnGrid.setLayout(new GridLayout(3,3,10,10));
		pnGrid.add(new JLabel(""));
		pnGrid.add(new JLabel(""));
		pnGrid.add(new JLabel(""));
		pnGrid.add(new JLabel(""));
		pnGrid.add(pnLogin);
		pnGrid.add(new JLabel(""));
		pnGrid.add(new JLabel(""));
		pnGrid.add(new JLabel(""));
		pnGrid.add(new JLabel(""));
		
		pnCenter.add("Center", pnGrid);
		c.add("Center", pnCenter);
		c.revalidate();
		c.repaint();
	}

	public void studentMain() {
		c.removeAll();

		// 학생 이름 select
		String studentName = "";
		try {
			stmt = con.createStatement();
			String query = "SELECT student_name FROM student WHERE student_no = " + userInfo;
			rs = stmt.executeQuery(query);
			rs.next();
			studentName = rs.getString(1);
		} catch (SQLException e) {
			System.out.println("쿼리 읽기 실패 :" + e);
		}
		JPanel pnUser = new JPanel();
		JLabel lbStudent = new JLabel(userInfo + " " + studentName);
		JLabel lbLogout = initBtnLogout();
		pnUser.add(lbStudent);
		pnUser.add(lbLogout);

		pnNorth.removeAll();
		pnNorth.setLayout(new BorderLayout());
		pnNorth.add("East", pnUser);

		JPanel pnBtn = new JPanel();
		pnBtn.setLayout(new GridLayout(4, 1, 5, 5));
		pnBtn.setPreferredSize(new Dimension(90, 240));

		JButton btnCourse = new JButton("수강");
		btnCourse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				studentCourse();
			}
		});

		JButton btnTimetable = new JButton("시간표");
		btnTimetable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				studentTimetable();
			}
		});

		JButton btnClub = new JButton("동아리");
		btnClub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				studentClub();
			}
		});

		JButton btnGrade = new JButton("성적");
		btnGrade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				studentGrade();
			}
		});
		pnBtn.add(btnCourse);
		pnBtn.add(btnTimetable);
		pnBtn.add(btnClub);
		pnBtn.add(btnGrade);
		pnWest.removeAll();
		pnWest.add(pnBtn);

		pnCenter.removeAll();
		pnCenter.setBackground(Color.GRAY);

		c.add("North", pnNorth);
		c.add("West", pnWest);
		c.add("Center", pnCenter);
		c.revalidate();
		c.repaint();
	}

	public void studentCourse() {}
	public void studentTimetable() {}
	public void studentClub() {}
	public void studentGrade() {}
	
	/* 공통 */
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
//			System.out.println("쿼리 읽기 실패 :" + e2);
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
				System.out.println("프로그램 완전 종료!");
				System.exit(0);
			}
		});

	}

}

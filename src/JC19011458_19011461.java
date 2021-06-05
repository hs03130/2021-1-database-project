import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import java.text.SimpleDateFormat;
import java.util.Date;
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
	String todayYear = "2021", todaySemester = "1";
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
		pnGrid.setLayout(new GridLayout(3, 3, 10, 10));
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
		pnBtn.setLayout(new GridLayout(4, 1, 5, 5));
		pnBtn.setPreferredSize(new Dimension(90, 180));

		JButton btnEntire = new JButton("전체");
		btnEntire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adminEntire();
			}
		});
		JButton btnAdministration = new JButton("관리");
		btnAdministration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adminAdministration();
			}
		});
		JButton btnInitDB = new JButton("초기화");
		btnInitDB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "DB를 초기화 하시겠습니까?", "", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					InitDataBase init = new InitDataBase(con, stmt);
					// 초기화 확인
					JOptionPane.showMessageDialog(null, "DB가 초기화 되었습니다.", "", JOptionPane.PLAIN_MESSAGE);
				} else if (result == JOptionPane.CANCEL_OPTION) {

				}
			}
		});
		JButton btnOthers = new JButton("기타");
		btnOthers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adminOthers();
			}
		});
		pnBtn.add(btnEntire);
		pnBtn.add(btnAdministration);
		pnBtn.add(btnInitDB);
		pnBtn.add(btnOthers);

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

	public void adminEntire() {
		// 교수, 학과, 교수 소속, 강좌, 학생, 등록, 지도관계, 수강, 동아리, 동아리원
		c.remove(pnCenter);
		pnCenter.removeAll();
		pnCenter.setLayout(new BorderLayout());

		/* 헤더 */
		JPanel pnHeader = new JPanel();
		pnHeader.setLayout(new BorderLayout());
		pnHeader.setBackground(Color.GRAY);
		JLabel lbTitle = new JLabel("전체 테이블");
		lbTitle.setHorizontalAlignment(JLabel.LEFT);

		JPanel pnBtn = new JPanel();
		JButton btnProfessor = new JButton("교수");
		JButton btnDepartment = new JButton("학과");
		JButton btnAffiliatedProfessor = new JButton("교수 소속");
		JButton btnLecture = new JButton("강의");
		JButton btnStudent = new JButton("학생");
		JButton btnTuition = new JButton("등록");
		JButton btnTutoring = new JButton("지도관계");
		JButton btnCourse = new JButton("수강");
		JButton btnClub = new JButton("동아리");
		JButton btnClubJoin = new JButton("동아리원");

		btnProfessor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnCenter.removeAll();
				pnCenter.add("North", pnHeader);
				pnCenter.add("Center", showTableProfessor());
				c.remove(pnCenter);
				c.add("Center", pnCenter);
				c.revalidate();
				// c.repaint();
			}
		});
		btnDepartment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnCenter.removeAll();
				pnCenter.add("North", pnHeader);
				pnCenter.add("Center", showTableDepartment());
				c.remove(pnCenter);
				c.add("Center", pnCenter);
				c.revalidate();
				// c.repaint();
			}
		});
		btnAffiliatedProfessor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnCenter.removeAll();
				pnCenter.add("North", pnHeader);
				pnCenter.add("Center", showTableAffiliatedProfessor());
				c.remove(pnCenter);
				c.add("Center", pnCenter);
				c.revalidate();
				// c.repaint();
			}
		});
		btnLecture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnCenter.removeAll();
				pnCenter.add("North", pnHeader);
				pnCenter.add("Center", showTableLecture());
				c.remove(pnCenter);
				c.add("Center", pnCenter);
				c.revalidate();
				// c.repaint();
			}
		});
		btnStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnCenter.removeAll();
				pnCenter.add("North", pnHeader);
				pnCenter.add("Center", showTableStudent());
				c.remove(pnCenter);
				c.add("Center", pnCenter);
				c.revalidate();
				// c.repaint();
			}
		});
		btnTuition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnCenter.removeAll();
				pnCenter.add("North", pnHeader);
				pnCenter.add("Center", showTableTuition());
				c.remove(pnCenter);
				c.add("Center", pnCenter);
				c.revalidate();
				// c.repaint();
			}
		});
		btnTutoring.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnCenter.removeAll();
				pnCenter.add("North", pnHeader);
				pnCenter.add("Center", showTableTutoring());
				c.remove(pnCenter);
				c.add("Center", pnCenter);
				c.revalidate();
				// c.repaint();
			}
		});
		btnCourse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnCenter.removeAll();
				pnCenter.add("North", pnHeader);
				pnCenter.add("Center", showTableCourse());
				c.remove(pnCenter);
				c.add("Center", pnCenter);
				c.revalidate();
				// c.repaint();
			}
		});
		btnClub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnCenter.removeAll();
				pnCenter.add("North", pnHeader);
				pnCenter.add("Center", showTableClub());
				c.remove(pnCenter);
				c.add("Center", pnCenter);
				c.revalidate();
				// c.repaint();
			}
		});
		btnClubJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnCenter.removeAll();
				pnCenter.add("North", pnHeader);
				pnCenter.add("Center", showTableClubJoin());
				c.remove(pnCenter);
				c.add("Center", pnCenter);
				c.revalidate();
				// c.repaint();
			}
		});

		pnBtn.add(btnProfessor);
		pnBtn.add(btnDepartment);
		pnBtn.add(btnAffiliatedProfessor);
		pnBtn.add(btnLecture);
		pnBtn.add(btnStudent);
		pnBtn.add(btnTuition);
		pnBtn.add(btnTutoring);
		pnBtn.add(btnCourse);
		pnBtn.add(btnClub);
		pnBtn.add(btnClubJoin);

		pnHeader.add("Center", lbTitle);
		pnHeader.add("East", pnBtn);

		pnCenter.add("North", pnHeader);
//		pnCenter.add("Center", pnContent);
		c.add("Center", pnCenter);
		c.revalidate();
		c.repaint();
	}

	public void adminAdministration() {

	}

	public void adminInitDB() {
		c.remove(pnCenter);
		pnCenter.removeAll();
		pnCenter.setLayout(new BorderLayout());

		JPanel pnHeader = new JPanel();
		pnHeader.setLayout(new BorderLayout());
		pnHeader.setBackground(Color.GRAY);
		JLabel lbTitle = new JLabel("데이터베이스 초기화");
		lbTitle.setHorizontalAlignment(JLabel.LEFT);
		pnHeader.add("Center", lbTitle);

		JPanel pnContent = new JPanel();
		JButton btnInitDB = new JButton("초기화");
		btnInitDB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InitDataBase init = new InitDataBase(con, stmt);
			}
		});
		pnContent.add(btnInitDB);

		pnCenter.add("North", pnHeader);
		pnCenter.add("Center", btnInitDB);
		c.add("Center", pnCenter);
		c.revalidate();
		c.repaint();
	}

	public void adminOthers() {
		c.remove(pnCenter);
		pnCenter.removeAll();
		pnCenter.setLayout(new BorderLayout());

		/* 헤더 */
		JPanel pnHeader = new JPanel();
		pnHeader.setLayout(new BorderLayout());
		pnHeader.setBackground(Color.GRAY);
		JLabel lbTitle = new JLabel("기타 기능들");
		lbTitle.setHorizontalAlignment(JLabel.LEFT);

		JPanel pnBtn = new JPanel();
		JButton btnSetYearSemester = new JButton("학년/학기 설정");

		btnSetYearSemester.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnCenter.removeAll();
				pnCenter.add("North", pnHeader);

				/* 내용 */
				JLabel lbYear = new JLabel("연도");
				JTextField year = new JTextField();
				JLabel lbSemester = new JLabel("학기");
				JTextField semester = new JTextField();
				JButton btn = new JButton("변경");

				lbYear.setHorizontalAlignment(JLabel.CENTER);
				year.setPreferredSize(new Dimension(200, 40));
				year.setText(todayYear);
				year.addKeyListener(new KeyListener() {
					public void keyTyped(KeyEvent e) {
					}

					public void keyPressed(KeyEvent e) {
					}

					public void keyReleased(KeyEvent e) {
						if (!year.getText().equals(todayYear)) {
							btn.setEnabled(true);
						}
					}
				});

				lbSemester.setHorizontalAlignment(JLabel.CENTER);
				semester.setPreferredSize(new Dimension(200, 40));
				semester.setText(todaySemester);
				semester.addKeyListener(new KeyListener() {
					public void keyTyped(KeyEvent e) {
					}

					public void keyPressed(KeyEvent e) {
					}

					public void keyReleased(KeyEvent e) {
						if (!semester.getText().equals(todaySemester)) {
							btn.setEnabled(true);
						}
					}
				});

				btn.setPreferredSize(new Dimension(60, 40));
				btn.setEnabled(false);
				btn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							Integer.parseInt(year.getText());
						} catch (NumberFormatException ex) {
							JOptionPane.showMessageDialog(null, "숫자만 입력할 수 있습니다..", "", JOptionPane.PLAIN_MESSAGE);
							year.setText("");
							year.requestFocus();
							return;
						}
						try {
							Integer.parseInt(semester.getText());
						} catch (NumberFormatException ex) {
							JOptionPane.showMessageDialog(null, "숫자만 입력할 수 있습니다..", "", JOptionPane.PLAIN_MESSAGE);
							semester.setText("");
							semester.requestFocus();
							return;
						}
						if (year.getText().length() != 4) {
							JOptionPane.showMessageDialog(null, "연도가 올바르지 않습니다.", "", JOptionPane.PLAIN_MESSAGE);
							year.requestFocus();
							return;
						}
						if (!semester.getText().equals("1") && !semester.getText().equals("2")) {
							JOptionPane.showMessageDialog(null, "학기는 '1' 또는 '2'만 입력 가능합니다.", "",
									JOptionPane.PLAIN_MESSAGE);
							semester.requestFocus();
							return;
						}

						int result = JOptionPane.showConfirmDialog(null, "연도/학기를 수정하시겠습니까?", "",
								JOptionPane.OK_CANCEL_OPTION);
						if (result == JOptionPane.OK_OPTION) {
							todayYear = year.getText().replace(" ", "");
							todaySemester = semester.getText();
							JOptionPane.showMessageDialog(null, "수정되었습니다.", "", JOptionPane.PLAIN_MESSAGE);
							btnSetYearSemester.doClick();
						} else if (result == JOptionPane.CANCEL_OPTION) {

						}
					}
				});

				JPanel pnForm = new JPanel();
				pnForm.add(lbYear);
				pnForm.add(year);
				pnForm.add(lbSemester);
				pnForm.add(semester);
				pnForm.add(new JLabel(""));
				pnForm.add(btn);

				JPanel pnContent = new JPanel();
				JLabel notice = new JLabel("변경된 값은 프로그램이 종료되면 초기화됩니다.");
				notice.setHorizontalAlignment(JLabel.CENTER);
				pnContent.setLayout(new GridLayout(3, 1, 10, 10));
				pnContent.add(notice);
				pnContent.add(pnForm);
				pnContent.add(new JLabel(""));

				pnCenter.add("Center", pnContent);
				c.remove(pnCenter);
				c.add("Center", pnCenter);
				c.revalidate();
				c.repaint();
			}
		});

		pnBtn.add(btnSetYearSemester);
		pnHeader.add("Center", lbTitle);
		pnHeader.add("East", pnBtn);
		pnCenter.add("North", pnHeader);
		c.add("Center", pnCenter);
		c.revalidate();
		c.repaint();
	}

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
		pnGrid.setLayout(new GridLayout(3, 3, 10, 10));
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
				professorLecture(todayYear, todaySemester);
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
		lectureYear.setText(todayYear);
		lectureYear.setPreferredSize(new Dimension(100, 30));
		lectureYear.setHorizontalAlignment(JTextField.RIGHT);

		JTextField lectureSemester = new JTextField();
		lectureSemester.setText(todaySemester);
		lectureSemester.setPreferredSize(new Dimension(100, 30));
		lectureSemester.setHorizontalAlignment(JTextField.RIGHT);

		JButton btnInquire = new JButton("조회");
		btnInquire.setHorizontalAlignment(JButton.RIGHT);
		btnInquire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pnCenter.getComponentCount() > 1) { // 이미 scrollContent 존재하면 제거
					pnCenter.remove(1);
				}
				JScrollPane pnContent = new JScrollPane(
						findLectureByProfessor(lectureYear.getText(), lectureSemester.getText()));
				// scrollContent.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				pnContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				pnCenter.add("Center", pnContent);
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

	public JScrollPane findLectureByProfessor(String lectureYear, String lectureSemester) {

		String[] tableHeader = { "강의번호", "강좌이름", "담당교수", "시간표", "취득학점", "강좌시간", "강의실", "개설학과" };
		String[][] tableContents = null;
		ArrayList<String[]> strList = new ArrayList<String[]>();
		try {
			stmt = con.createStatement();
			String query = "SELECT l.lecture_no, l.lecture_class_no, l.lecture_name, p.professor_name, l.lecture_day1, l.lecture_period1, l.lecture_day2, l.lecture_period2, l.lecture_credit, l.lecture_time, l.lecture_room, d.department_name\r\n"
					+ "FROM lecture l\r\n" + "LEFT JOIN professor p ON l.professor_no = p.professor_no\r\n"
					+ "LEFT JOIN department d ON l.department_no = d.department_no\r\n" + "WHERE p.professor_no = "
					+ userInfo + "\r\n" + "AND l.lecture_year = " + lectureYear + "\r\n" + "AND l.lecture_semester = "
					+ lectureSemester;
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				String time = "";
				if(!rs.getString(5).equals("")) {
					if(rs.getString(5).equals(rs.getString(7))) {
						time += rs.getString(5) + rs.getString(7) + mapTime(rs.getString(6));
					}
					else {
						time = rs.getString(5) + mapTime(rs.getString(6));
						if(!rs.getString(7).equals(("")))
							time += "," + rs.getString(7) + mapTime(rs.getString(8));
				 	}
				}
				String[] str = { rs.getInt(1) + " - " + rs.getInt(2), rs.getString(3), rs.getString(4), time, rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12) };
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
		JTable lectureInfo = new JTable(model);
		lectureInfo.getTableHeader().setReorderingAllowed(false); // 컬럼들 이동 불가
		// studentTable.getTableHeader().setResizingAllowed(false); // 컬럼 크기 조절 불가
		lectureInfo.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String[] lectureNo = ((String) model.getValueAt(lectureInfo.getSelectedRow(), 0)).trim().split("-");
				professorLectureStudents(lectureNo[0], lectureYear, lectureSemester);
			}

			public void mouseEntered(MouseEvent e) {
				lectureInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});
		lectureInfo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 여러 행 선택 불가

		JScrollPane pnContent = new JScrollPane(lectureInfo);
		pnContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		return pnContent;
	}

	public void professorLectureStudents(String lectureNo, String lectureYear, String lectureSemester) {
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
	public void modifyGrade(String lectureNo, String lectureYear, String lectureSemester, DefaultTableModel model,
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
			System.out.println(query);

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
		JLabel lbTitle = new JLabel(todayYear + "년 " + todaySemester + "학기 시간표");
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
			String query = String.format(
					"SELECT lecture_name, lecture_day1, lecture_period1, lecture_day2, lecture_period2, lecture_room FROM lecture WHERE professor_no = %s AND lecture_year = %s AND lecture_semester = %s",
					userInfo, todayYear, todaySemester);
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
		pnGrid.setLayout(new GridLayout(3, 3, 10, 10));
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

	public void studentCourse() {
		c.remove(pnCenter);
		pnCenter.removeAll();
		pnCenter.setLayout(new BorderLayout());

		JPanel pnHeader = new JPanel();
		pnHeader.setLayout(new BorderLayout());
		pnHeader.setBackground(Color.GRAY);

		JLabel lbTitle = new JLabel("수강 내역");
		lbTitle.setHorizontalAlignment(JLabel.LEFT);

		JPanel pnCondition = new JPanel();
		JTextField lectureYear = new JTextField();
		lectureYear.setText(todayYear);
		lectureYear.setPreferredSize(new Dimension(100, 30));

		JTextField lectureSemester = new JTextField();
		lectureSemester.setText(todaySemester);
		lectureSemester.setPreferredSize(new Dimension(100, 30));

		JButton btnInquire = new JButton("조회");
		btnInquire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pnCenter.getComponentCount() > 1) { // 이미 scrollContent 존재하면 제거
					pnCenter.remove(1);
				}
				pnCenter.add("Center", findCourseByStudent(lectureYear.getText(), lectureSemester.getText()));
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

	public JScrollPane findCourseByStudent(String lectureYear, String lectureSemester) {
		JScrollPane pnContent = null;
		try {
			stmt = con.createStatement();
			String query = "select l.lecture_no,  l.lecture_class_no,  l.lecture_name, l.lecture_credit, l.lecture_day1, l.lecture_period1, l.lecture_day2, l.lecture_period2, p.professor_name , l.lecture_room\r\n"
					+ "from lecture l, course c, professor p\r\n"
					+ "where c.lecture_no = l.lecture_no and l.professor_no = p.professor_no "
					+ " and l.lecture_year = " + lectureYear + " and l.lecture_semester = " + lectureSemester
					+ " and c.student_no = " + userInfo;

			rs = stmt.executeQuery(query);

			String[] columns_list = { "강의번호", "분반", "교과목명", "학점", "시간표" };
			Vector<String> columns = new Vector<String>();
			Vector<Vector<String>> contents = new Vector<Vector<String>>();

			for (int i = 0; i < 5; i++)
				columns.add(columns_list[i]);

			while (rs.next()) {
				Vector<String> tmp = new Vector<String>();
				tmp.add(rs.getString(1));
				tmp.add(rs.getString(2));
				tmp.add(rs.getString(3));
				tmp.add(rs.getString(4));

				String timeinfo;

				String day1 = rs.getString(5);
				String period1 = rs.getString(6);
				String day2 = rs.getString(7);
				String period2 = rs.getString(8);

				if (period1.equals("")) {
					timeinfo = "(" + rs.getString(9) + ")";
				} else {
					if (period1.equals(period2)) {
						timeinfo = day1 + day2 + mapTime(period1); // 화목 10:00-12:00
					} else {
						timeinfo = day1 + mapTime(period1);
						if (!period2.equals(""))
							timeinfo += "," + day2 + mapTime(period2);
					}
					timeinfo += "(" + rs.getString(9) + "/" + rs.getString(10) + ")";

				}

				tmp.add(timeinfo);
				contents.add(tmp);
			}

			JTable coursetable = new JTable(contents, columns);
			coursetable.setEnabled(false);
			coursetable.getTableHeader().setReorderingAllowed(false); // 컬럼들 이동 불가
			pnContent = new JScrollPane(coursetable);
			pnContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		} catch (SQLException e) {
			System.out.println("쿼리 읽기 실패 :" + e);
		}
		return pnContent;
	}

	public void studentTimetable() {
		c.remove(pnCenter);
		pnCenter.removeAll();
		pnCenter.setLayout(new BorderLayout());

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
			String query = "select l.lecture_name, l.lecture_day1, l.lecture_period1, l.lecture_day2, l.lecture_period2, l.lecture_room\r\n"
					+ "from lecture l, course c\r\n" + "where l.lecture_no = c.lecture_no and c.student_no = "
					+ userInfo;
			query += " and l.lecture_year = " + todayYear + " and l.lecture_semester = " + todaySemester;
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
				System.out.println(day);
				if (!day.equals("")) {
					period = rs.getString(5).split(",");
					i = day_map.indexOf(day);
					for (int j = 0; j < period.length; j++) {
						subject = "<HTML><div style=\"text-align:center\">" + name + "<br />" + room + "</div></HTML>";
						contents[Integer.parseInt(period[j]) - 1][i + 1] = subject;
					}
				}

			}

			JTable timetable = new JTable(contents, columns);
			timetable.setRowHeight(50);
			timetable.setEnabled(false);
			timetable.getTableHeader().setReorderingAllowed(false);

			JScrollPane scrollpane = new JScrollPane(timetable);

			pnCenter.add("North", online);
			pnCenter.add("Center", scrollpane);

			c.add("Center", pnCenter);
			c.revalidate();
			c.repaint();

		} catch (SQLException e) {
			System.out.println("쿼리읽기실패:" + e);
			showMessageDialog(null, "쿼리읽기실패:");
		}
	}

	public void studentClub() {
		c.remove(pnCenter);
		pnCenter.removeAll();
		pnCenter.setLayout(new BorderLayout());

		JPanel pnHeader = new JPanel();
		pnHeader.setLayout(new BorderLayout());
		pnHeader.setBackground(Color.GRAY);

		JLabel lbTitle = new JLabel("동아리");
		lbTitle.setHorizontalAlignment(JLabel.LEFT);

		try {
			stmt = con.createStatement();
			String query = "select club.*, s.student_name, p.professor_name\r\n"
					+ "from club, club_join, student s, professor p\r\n"
					+ "where club.club_no = club_join.club_no and club.student_no = s.student_no and club.professor_no = p.professor_no \r\n"
					+ "and  club_join.student_no = " + userInfo;
			rs = stmt.executeQuery(query);

			String[] columns_list = { "동아리번호", "이름", "회장", "회원 수", "지도교수", "동아리방" };
			Vector<String> columns = new Vector<String>();
			Vector<Vector<String>> contents = new Vector<Vector<String>>();

			for (int i = 0; i < columns_list.length; i++)
				columns.add(columns_list[i]);

			Vector<Integer> president = new Vector<Integer>();
			int r = 0;
			while (rs.next()) {
				Vector<String> tmp = new Vector<String>();
				tmp.add(rs.getString(1));
				tmp.add(rs.getString(2));
				if (rs.getString(6).equals(userInfo)) {
					president.add(r);
					tmp.add("<HTML>" + rs.getString(7) + "<br />" + "[회원정보확인]" + "</HTML>");
					System.out.println("동아리회장");
				} else
					tmp.add(rs.getString(7));
				tmp.add(rs.getString(3));
				tmp.add(rs.getString(8));
				tmp.add(rs.getString(4));

				contents.add(tmp);
				r++;
			}

			DefaultTableModel model = new DefaultTableModel(contents, columns) {
				public boolean isCellEditable(int i, int c) {
					return false;
				}
			};

			JTable clubtable = new JTable(model);
			clubtable.getTableHeader().setReorderingAllowed(false); // 컬럼들 이동 불가
			clubtable.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					int row = clubtable.getSelectedRow();
					if (president.contains(row)) {
						showClubStudent(clubtable.getValueAt(row, 0));
					}
				}

				private void showClubStudent(Object valueAt) {
					c.remove(pnCenter);
					pnCenter.removeAll();
					pnCenter.setLayout(new BorderLayout());

					JPanel pnHeader = new JPanel();
					pnHeader.setLayout(new BorderLayout());
					pnHeader.setBackground(Color.GRAY);

					JLabel title = new JLabel("동아리");

					// TODO Auto-generated method stub
					try {
						// System.out.println(valueAt);
						stmt = con.createStatement();
						String query = "select s.*, major.department_name, minor.department_name\r\n"
								+ "from club_join c\r\n" + "left join student s on c.student_no = s.student_no\r\n"
								+ "left join department major on s.major_no = major.department_no\r\n"
								+ "left join department minor on s.minor_no = minor.department_no\r\n"
								+ "where c.club_no = " + valueAt;
						rs = stmt.executeQuery(query);

						String[] columns_list = { "학번", "이름", "주소", "번호", "이메일", "계좌", "전공", "부전공" };
						Vector<String> columns = new Vector<String>();
						Vector<Vector<String>> contents = new Vector<Vector<String>>();

						for (int i = 0; i < columns_list.length; i++)
							columns.add(columns_list[i]);

						while (rs.next()) {
							Vector<String> tmp = new Vector<String>();
							tmp.add(rs.getString(1));
							tmp.add(rs.getString(2));
							tmp.add(rs.getString(3));
							tmp.add(rs.getString(4));
							tmp.add(rs.getString(5));
							tmp.add(rs.getString(6));
							tmp.add(rs.getString(9));
							tmp.add(rs.getString(10));

							contents.add(tmp);
						}

						JTable lecturetable = new JTable(contents, columns);
						lecturetable.setEnabled(false);
						lecturetable.getTableHeader().setReorderingAllowed(false); // 컬럼들 이동 불가
						JScrollPane scrollpane = new JScrollPane(lecturetable);

						pnCenter.add("Center", scrollpane);
						pnCenter.revalidate();
						pnCenter.repaint();

					} catch (SQLException e1) {
						// TODO: handle exception
						System.out.println("쿼리 읽기 실패 :" + e1);

					}

					pnHeader.add("Center", title);
					pnCenter.add("North", pnHeader);
					c.add("Center", pnCenter);
					c.revalidate();
					c.repaint();
				}

			});
			clubtable.setRowHeight(50);

			JScrollPane scrollpane = new JScrollPane(clubtable);
			pnCenter.add("Center", scrollpane);
			pnCenter.revalidate();
			pnCenter.repaint();

		} catch (SQLException e) {
			System.out.println("쿼리 읽기 실패 :" + e);
		}

		pnHeader.add("Center", lbTitle);
		pnCenter.add("North", pnHeader);

		c.add("Center", pnCenter);
		c.revalidate();
		c.repaint();

	}

	public void studentGrade() {
		c.remove(pnCenter);
		pnCenter.removeAll();
		pnCenter.setLayout(new BorderLayout());

		JPanel pnHeader = new JPanel();
		pnHeader.setLayout(new BorderLayout());
		pnHeader.setBackground(Color.GRAY);
		JLabel lbTitle = new JLabel("성적");
		lbTitle.setHorizontalAlignment(JLabel.LEFT);
		pnHeader.add("Center", lbTitle);
				
		String[] grade_list = { "A+", "A0", "B+", "B0", "C+", "C0", "D+", "D0", "FA", "F", "P", "NP" };
		Double[] gradepoints = { 4.5, 4.0, 3.5, 3.0, 2.5, 2.0, 1.5, 1.0, 0.0, 0.0 };
		Vector<String> grade = new Vector<String>();
		for (int i = 0; i < grade_list.length; i++) {
			grade.add(grade_list[i]);
		}

		try {
			stmt = con.createStatement();
			String query = "select l.lecture_no, l.lecture_name, l.lecture_credit, c.grade\r\n"
					+ "from lecture l, course c\r\n"
					+ "where l.lecture_no = c.lecture_no and not(l.lecture_year >= " + todayYear + " and l.lecture_semester >= " + todaySemester + " )\r\n"
					+ "and c.student_no = " + userInfo;
			rs = stmt.executeQuery(query);

			String[] columns_list = { "과목번호", "교과목명", "학점", "등급", "평점" };
			Vector<String> columns = new Vector<String>();
			Vector<Vector<String>> contents = new Vector<Vector<String>>();

			for (int i = 0; i < columns_list.length; i++)
				columns.add(columns_list[i]);
			Double GPA = 0.0;
			
			Integer getCredit = 0; //취득학점 <8, ==10
			Integer gradeCredit = 0; //GPA반영학점 < 10
			Integer totalCredit = 0; //신청학점
		
			while (rs.next()) {
				Vector<String> tmp = new Vector<String>();
				tmp.add(rs.getString(1));
				tmp.add(rs.getString(2));
				tmp.add(rs.getString(3));
				tmp.add(rs.getString(4));

				int credit = Integer.parseInt(rs.getString(3)); // 학점
				int idx = grade.indexOf(rs.getString(4)); // 등급
				
				totalCredit += credit;
				if (idx < 10) { 
					gradeCredit += credit;
					tmp.add(Double.toString(gradepoints[idx]));
					GPA += gradepoints[idx] * (1.0 * credit);
					
					if(idx < 8) {
						getCredit += credit;
					}
					
				} else if (idx == 10) {// p
					getCredit += credit;
				}
				

				contents.add(tmp);
			}

			GPA /= 1.0 * gradeCredit;
			GPA = Math.round(GPA * 100) / 100.0;
			JLabel gradeinfo = new JLabel("<HTML><div style=\\\"text-align:right\\\">취득학점 : " + totalCredit
					+ "<br /><br />GPA :" + GPA + "</div></HTML>");
			gradeinfo.setHorizontalAlignment(JLabel.LEFT);
			
			JTable gradetable = new JTable(contents, columns);
			gradetable.setEnabled(false);
			
			JScrollPane scrollpane = new JScrollPane(gradetable);
			scrollpane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "<HTML><div style=\\\"text-align:left\\\">신청학점 : " + totalCredit +  "<br />취득학점 : " + getCredit + "<br />GPA : " + GPA + "</div></HTML>", TitledBorder.LEFT, TitledBorder.TOP, new Font("times new roman", Font.PLAIN, 15), Color.RED));
			pnCenter.add("Center", scrollpane);
			
			//pnHeader.add("South", gradeinfo);

			pnCenter.revalidate();
			pnCenter.repaint();

		} catch (SQLException e) {
			System.out.println("쿼리 읽기 실패 :" + e);
		}
		pnCenter.add("North", pnHeader);
		c.add("Center", pnCenter);
		c.revalidate();
		c.repaint();

	}

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

	private String mapTime(String period1) {
		String result = "";
		int start, end;

		String[] period_map = { "8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30",
				"13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00",
				"18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30",
				"24:00" };

		String[] period = period1.split(",");
		end = Integer.parseInt(period[0]); // 1교시면 index = 0 이므로
		start = end - 1;

		for (int i = 0; i < period.length; i++) {
			if (end != Integer.parseInt(period[i])) {
				result += period_map[start] + "-" + period_map[end] + ",";
				end = Integer.parseInt(period[i]);
				start = end - 1;
				i--;
			} else
				end++;
		}
		result += period_map[start] + "-" + period_map[--end];

		return result;
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

	// DB 테이블 반환
	public JScrollPane showTableProfessor() {
		/* 내용 */
		String[] tableHeader = { "professor_no", "professor_name", "professor_address", "professor_phone",
				"professor_email" };
		String[][] tableContents = null;
		ArrayList<String[]> strList = new ArrayList<String[]>();
		try {
			stmt = con.createStatement();
			String query = "SELECT professor_no, professor_name, professor_address, professor_phone, professor_email FROM professor";
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				String[] str = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5) };
				strList.add(str);
			}
			tableContents = new String[strList.size()][];
			strList.toArray(tableContents);
		} catch (SQLException e) {
			System.out.println("쿼리 읽기 실패 showTableProfessor :" + e);
		}

		DefaultTableModel model = new DefaultTableModel(tableContents, tableHeader) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};

		JTable table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false); // 컬럼들 이동 불가
		// departmentTable.getTableHeader().setResizingAllowed(false); // 컬럼 크기 조절 불가
		// departmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 여러
		// 행 선택 불가

		JScrollPane pnContent = new JScrollPane(table);
		pnContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		return pnContent;
	}

	public JScrollPane showTableDepartment() {
		/* 내용 */
		String[] tableHeader = { "department_no", "department_name", "department_contact", "department_office",
				"professor_no" };
		String[][] tableContents = null;
		ArrayList<String[]> strList = new ArrayList<String[]>();
		try {
			stmt = con.createStatement();
			String query = "SELECT department_no, department_name, department_contact, department_office, professor_no FROM department";
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				String[] str = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5) };
				strList.add(str);
			}
			tableContents = new String[strList.size()][];
			strList.toArray(tableContents);
		} catch (SQLException e) {
			System.out.println("쿼리 읽기 실패 showTableDepartment :" + e);
		}

		DefaultTableModel model = new DefaultTableModel(tableContents, tableHeader) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};

		JTable table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false); // 컬럼들 이동 불가
		// departmentTable.getTableHeader().setResizingAllowed(false); // 컬럼 크기 조절 불가
		// departmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 여러
		// 행 선택 불가

		JScrollPane pnContent = new JScrollPane(table);
		pnContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return pnContent;
	}

	public JScrollPane showTableAffiliatedProfessor() {
		/* 내용 */
		String[] tableHeader = { "professor_no", "department_no" };
		String[][] tableContents = null;
		ArrayList<String[]> strList = new ArrayList<String[]>();
		try {
			stmt = con.createStatement();
			String query = "SELECT professor_no, department_no FROM affiliated_professor";
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				String[] str = { rs.getString(1), rs.getString(2) };
				strList.add(str);
			}
			tableContents = new String[strList.size()][];
			strList.toArray(tableContents);
		} catch (SQLException e) {
			System.out.println("쿼리 읽기 실패 showTableAffiliatedProfessor :" + e);
		}

		DefaultTableModel model = new DefaultTableModel(tableContents, tableHeader) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};

		JTable table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false); // 컬럼들 이동 불가
		// departmentTable.getTableHeader().setResizingAllowed(false); // 컬럼 크기 조절 불가
		// departmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 여러
		// 행 선택 불가

		JScrollPane pnContent = new JScrollPane(table);
		pnContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return pnContent;
	}

	public JScrollPane showTableLecture() {
		/* 내용 */
		String[] tableHeader = { "lecture_no", "lecture_class_no", "lecture_name", "lecture_day1", "lecture_period1",
				"lecture_day2", "lecture_period2", "lecture_credit", "lecture_time", "lecture_room", "department_no",
				"professor_no", "lecture_year", "lecture_semester" };
		String[][] tableContents = null;
		ArrayList<String[]> strList = new ArrayList<String[]>();
		try {
			stmt = con.createStatement();
			String query = "SELECT lecture_no, lecture_class_no, lecture_name, lecture_day1, lecture_period1, lecture_day2, lecture_period2, lecture_credit, lecture_time, lecture_room, department_no, professor_no, lecture_year, lecture_semester FROM lecture";
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				String[] str = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10),
						rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14) };
				strList.add(str);
			}
			tableContents = new String[strList.size()][];
			strList.toArray(tableContents);
		} catch (SQLException e) {
			System.out.println("쿼리 읽기 실패 showTableLecture :" + e);
		}

		DefaultTableModel model = new DefaultTableModel(tableContents, tableHeader) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};

		JTable table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false); // 컬럼들 이동 불가
		// departmentTable.getTableHeader().setResizingAllowed(false); // 컬럼 크기 조절 불가
		// departmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 여러
		// 행 선택 불가

		JScrollPane pnContent = new JScrollPane(table);
		pnContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return pnContent;
	}

	public JScrollPane showTableStudent() {
		/* 내용 */
		String[] tableHeader = { "student_no", "student_name", "student_address", "student_phone", "student_email",
				"student_account", "major_no", "minor_no" };
		String[][] tableContents = null;
		ArrayList<String[]> strList = new ArrayList<String[]>();
		try {
			stmt = con.createStatement();
			String query = "SELECT student_no, student_name, student_address, student_phone, student_email, student_account, major_no, minor_no FROM student";
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				String[] str = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7), rs.getString(8) };
				strList.add(str);
			}
			tableContents = new String[strList.size()][];
			strList.toArray(tableContents);
		} catch (SQLException e) {
			System.out.println("쿼리 읽기 실패 showTableStudent :" + e);
		}

		DefaultTableModel model = new DefaultTableModel(tableContents, tableHeader) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};

		JTable table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false); // 컬럼들 이동 불가
		// departmentTable.getTableHeader().setResizingAllowed(false); // 컬럼 크기 조절 불가
		// departmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 여러
		// 행 선택 불가

		JScrollPane pnContent = new JScrollPane(table);
		pnContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return pnContent;
	}

	public JScrollPane showTableTuition() {
		/* 내용 */
		String[] tableHeader = { "student_no", "tuition_year", "tuition_semester", "tuition_fee", "tuition_payment",
				"last_payment_date", "grade_semester" };
		String[][] tableContents = null;
		ArrayList<String[]> strList = new ArrayList<String[]>();
		try {
			stmt = con.createStatement();
			String query = "SELECT student_no, tuition_year, tuition_semester, tuition_fee, tuition_payment, last_payment_date, grade_semester FROM tuition ORDER BY tuition_year DESC, tuition_semester DESC, student_no";
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				String[] str = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7) };
				strList.add(str);
			}
			tableContents = new String[strList.size()][];
			strList.toArray(tableContents);
		} catch (SQLException e) {
			System.out.println("쿼리 읽기 실패 showTableTuition :" + e);
		}

		DefaultTableModel model = new DefaultTableModel(tableContents, tableHeader) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};

		JTable table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false); // 컬럼들 이동 불가
		// departmentTable.getTableHeader().setResizingAllowed(false); // 컬럼 크기 조절 불가
		// departmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 여러
		// 행 선택 불가

		JScrollPane pnContent = new JScrollPane(table);
		pnContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return pnContent;
	}

	public JScrollPane showTableTutoring() {
		/* 내용 */
		String[] tableHeader = { "student_no", "professor_no", "grade_semester" };
		String[][] tableContents = null;
		ArrayList<String[]> strList = new ArrayList<String[]>();
		try {
			stmt = con.createStatement();
			String query = "SELECT student_no, professor_no, grade_semester FROM tutoring";
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				String[] str = { rs.getString(1), rs.getString(2), rs.getString(3) };
				strList.add(str);
			}
			tableContents = new String[strList.size()][];
			strList.toArray(tableContents);
		} catch (SQLException e) {
			System.out.println("쿼리 읽기 실패 showTableTutoring :" + e);
		}

		DefaultTableModel model = new DefaultTableModel(tableContents, tableHeader) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};

		JTable table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false); // 컬럼들 이동 불가
		// departmentTable.getTableHeader().setResizingAllowed(false); // 컬럼 크기 조절 불가
		// departmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 여러
		// 행 선택 불가

		JScrollPane pnContent = new JScrollPane(table);
		pnContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return pnContent;
	}

	public JScrollPane showTableCourse() {
		/* 내용 */
		String[] tableHeader = { "lecture_no", "student_no", "midterm_score", "finals_score", "other_score",
				"attendance_score", "total_score", "grade" };
		String[][] tableContents = null;
		ArrayList<String[]> strList = new ArrayList<String[]>();
		try {
			stmt = con.createStatement();
			String query = "SELECT lecture_no, student_no, midterm_score, finals_score, other_score, attendance_score, total_score, grade FROM course";
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				String[] str = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7), rs.getString(8) };
				strList.add(str);
			}
			tableContents = new String[strList.size()][];
			strList.toArray(tableContents);
		} catch (SQLException e) {
			System.out.println("쿼리 읽기 실패 showTableCourse :" + e);
		}

		DefaultTableModel model = new DefaultTableModel(tableContents, tableHeader) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};

		JTable table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false); // 컬럼들 이동 불가
		// departmentTable.getTableHeader().setResizingAllowed(false); // 컬럼 크기 조절 불가
		// departmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 여러
		// 행 선택 불가

		JScrollPane pnContent = new JScrollPane(table);
		pnContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return pnContent;
	}

	public JScrollPane showTableClub() {
		/* 내용 */
		String[] tableHeader = { "club_no", "club_name", "club_room", "professor_no", "student_no",
				"club_total_member" };
		String[][] tableContents = null;
		ArrayList<String[]> strList = new ArrayList<String[]>();
		try {
			stmt = con.createStatement();
			String query = "SELECT club_no, club_name, club_room, professor_no, student_no, club_total_member FROM club";
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				String[] str = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6) };
				strList.add(str);
			}
			tableContents = new String[strList.size()][];
			strList.toArray(tableContents);
		} catch (SQLException e) {
			System.out.println("쿼리 읽기 실패 showTableClub :" + e);
		}

		DefaultTableModel model = new DefaultTableModel(tableContents, tableHeader) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};

		JTable table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false); // 컬럼들 이동 불가
		// departmentTable.getTableHeader().setResizingAllowed(false); // 컬럼 크기 조절 불가
		// departmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 여러
		// 행 선택 불가

		JScrollPane pnContent = new JScrollPane(table);
		pnContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return pnContent;
	}

	public JScrollPane showTableClubJoin() {
		/* 내용 */
		String[] tableHeader = { "club_no", "student_no" };
		String[][] tableContents = null;
		ArrayList<String[]> strList = new ArrayList<String[]>();
		try {
			stmt = con.createStatement();
			String query = "SELECT club_no, student_no FROM club_join";
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				String[] str = { rs.getString(1), rs.getString(2) };
				strList.add(str);
			}
			tableContents = new String[strList.size()][];
			strList.toArray(tableContents);
		} catch (SQLException e) {
			System.out.println("쿼리 읽기 실패 showTableClubJoin :" + e);
		}

		DefaultTableModel model = new DefaultTableModel(tableContents, tableHeader) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};

		JTable table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false); // 컬럼들 이동 불가
		// departmentTable.getTableHeader().setResizingAllowed(false); // 컬럼 크기 조절 불가
		// departmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 여러
		// 행 선택 불가

		JScrollPane pnContent = new JScrollPane(table);
		pnContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return pnContent;
	}
}

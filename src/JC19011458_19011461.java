import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JC19011458_19011461 extends JFrame {

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
    String todayYear = "", todaySemester = "";
    ImageIcon logoutIcon = new ImageIcon("images/exit-logout.png");
    ImageIcon leftArrowIcon = new ImageIcon("images/left-arrow.png");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
    String regExp = "^[0-9]+$"; // 숫자 정규식

    public JC19011458_19011461() {
        super("JC19011458_19011461");
        conDB();
        layInit();
        initYearAndSemester();
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

        JPanel pnUser = new JPanel();
        JLabel lbUser = new JLabel("관리자");
        JLabel lbLogout = initBtnLogout();
        pnUser.add(lbUser);
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
        c.remove(pnCenter);
        pnCenter.removeAll();
        pnCenter.setLayout(new BorderLayout());

        JPanel pnHeader = new JPanel();
        pnHeader.setLayout(new BorderLayout());
        // pnHeader.setBackground(Color.GRAY);

        JLabel lbTitle = new JLabel("관리");
        lbTitle.setHorizontalAlignment(JLabel.LEFT);
        pnHeader.add("Center", lbTitle);

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
                c.add("Center", adminAdministrationProfessor(pnHeader));
                c.revalidate();
                c.repaint();
            }
        });
        btnDepartment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c.add("Center", adminAdministrationDepartment(pnHeader));
                c.revalidate();
                c.repaint();
            }
        });
        btnAffiliatedProfessor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c.add("Center", adminAdministrationAffiliatedProfessor(pnHeader));
                c.revalidate();
                c.repaint();
            }
        });
        btnLecture.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //c.add("Center", adminAdministrationLecture(pnHeader));
                c.revalidate();
                c.repaint();
            }
        });
        btnStudent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c.add("Center", adminAdministrationStudent(pnHeader));
                c.revalidate();
                c.repaint();
            }
        });
        btnTuition.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c.add("Center", adminAdministrationTuition(pnHeader));
                c.revalidate();
                c.repaint();
            }
        });
        btnTutoring.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c.add("Center", adminAdministrationTutoring(pnHeader));
                c.revalidate();
                c.repaint();
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
                c.add("Center", adminAdministrationClub(pnHeader));
                c.revalidate();
                c.repaint();
            }
        });
        btnClubJoin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c.add("Center", adminAdministrationClubJoin(pnHeader));
                c.revalidate();
                c.repaint();
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
//	      pnCenter.add("Center", pnContent);
        c.add("Center", pnCenter);
        c.revalidate();
        c.repaint();

    }

    public void adminOthers() {
        c.remove(pnCenter);
        pnCenter.removeAll();
        pnCenter.setLayout(new BorderLayout());

        JPanel pnContent = new JPanel();

        /* 헤더 */
        JPanel pnHeader = new JPanel();
        pnHeader.setLayout(new BorderLayout());
        pnHeader.setBackground(Color.GRAY);
        JLabel lbTitle = new JLabel("기타 기능들");
        lbTitle.setHorizontalAlignment(JLabel.LEFT);

        JPanel pnBtn = new JPanel();
        JButton btnSetYearSemester = new JButton("학년/학기 설정");    //학년/학기 설정
        JButton btnEnterHandle = new JButton("입학 처리");    //TODO 신입생 입학 처리

        btnSetYearSemester.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //pnCenter.removeAll();
                //pnCenter.add("North", pnHeader);
                pnCenter.remove(pnContent);

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
        btnEnterHandle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adminEnterHandle();
            }
        });


        pnBtn.add(btnSetYearSemester);
        pnBtn.add(btnEnterHandle);

        pnHeader.add("Center", lbTitle);
        pnHeader.add("East", pnBtn);
        pnCenter.add("North", pnHeader);
        c.add("Center", pnCenter);
        c.revalidate();
        c.repaint();
    }

    public void adminEnterHandle() {
        if (pnCenter.getComponentCount() > 1) {
            pnCenter.remove(1);
        }
        JPanel pnContent = new JPanel();

        /* 내용 */
        pnContent.removeAll();
        pnContent.setLayout(new GridLayout(1, 3, 5, 5));
        JPanel pnStudent = new JPanel();
        pnStudent.setLayout(new BorderLayout());
        JPanel pnTuition = new JPanel();
        pnTuition.setLayout(new BorderLayout());
        JPanel pnTutoring = new JPanel();
        pnTutoring.setLayout(new BorderLayout());
        JButton btnEnter = new JButton("등록");
        btnEnter.setMinimumSize(new Dimension(10, 200));

        JPanel pnStudentGrid = new JPanel();
        pnStudentGrid.setLayout(new GridLayout(8, 2, 5, 5));
        JTextField student_student_no = new JTextField();
        JTextField student_student_name = new JTextField();
        JTextField student_student_address = new JTextField();
        JTextField student_student_phone = new JTextField();
        JTextField student_student_email = new JTextField();
        JTextField student_student_account = new JTextField();
        JTextField student_major_no = new JTextField();
        JTextField student_minor_no = new JTextField();

        JPanel pnTuitionGrid = new JPanel();
        pnTuitionGrid.setLayout(new GridLayout(7, 2, 5, 5));
        JTextField tuition_student_no = new JTextField();
        tuition_student_no.setEditable(false);
        JTextField tuition_tuition_year = new JTextField();
        JTextField tuition_tuition_semester = new JTextField();
        JTextField tuition_tuition_fee = new JTextField();
        JTextField tuition_tuition_payment = new JTextField();
        tuition_tuition_payment.setEditable(false);
        JTextField tuition_last_payment_date = new JTextField();
        JTextField tuition_grade_semester = new JTextField();
        tuition_grade_semester.setText("1학년1학기");    // 편입생 없음
        tuition_grade_semester.setEditable(false);

        JPanel pnTutoringGrid = new JPanel();
        pnTutoringGrid.setLayout(new GridLayout(3, 2, 5, 5));
        JTextField tutoring_student_no = new JTextField();
        tutoring_student_no.setEditable(false);
        JTextField tutoring_professor_no = new JTextField();
        JTextField tutoring_grade_semester = new JTextField();
        tutoring_grade_semester.setText("1학년1학기");
        tutoring_grade_semester.setEditable(false);

        /* 학생 입력 */
        student_student_no.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                tuition_student_no.setText(student_student_no.getText());
                tutoring_student_no.setText(student_student_no.getText());
            }
        });

        pnStudentGrid.add(new JLabel("student_no"));
        pnStudentGrid.add(student_student_no);
        pnStudentGrid.add(new JLabel("student_name"));
        pnStudentGrid.add(student_student_name);
        pnStudentGrid.add(new JLabel("student_address"));
        pnStudentGrid.add(student_student_address);
        pnStudentGrid.add(new JLabel("student_phone"));
        pnStudentGrid.add(student_student_phone);
        pnStudentGrid.add(new JLabel("student_email"));
        pnStudentGrid.add(student_student_email);
        pnStudentGrid.add(new JLabel("student_account"));
        pnStudentGrid.add(student_student_account);
        pnStudentGrid.add(new JLabel("major_no"));
        pnStudentGrid.add(student_major_no);
        pnStudentGrid.add(new JLabel("minor_no"));
        pnStudentGrid.add(student_minor_no);
        pnStudent.add("North", new JLabel("INSERT INTO student VALUES"));
        pnStudent.add("Center", pnStudentGrid);

        /* 등록 입력*/
        tuition_tuition_fee.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                tuition_tuition_payment.setText(tuition_tuition_fee.getText());
            }
        });

        pnTuitionGrid.add(new JLabel("student_no"));
        pnTuitionGrid.add(tuition_student_no);
        pnTuitionGrid.add(new JLabel("tuition_year"));
        pnTuitionGrid.add(tuition_tuition_year);
        pnTuitionGrid.add(new JLabel("tuition_semester"));
        pnTuitionGrid.add(tuition_tuition_semester);
        pnTuitionGrid.add(new JLabel("tuition_fee"));
        pnTuitionGrid.add(tuition_tuition_fee);
        pnTuitionGrid.add(new JLabel("tuition_payment"));
        pnTuitionGrid.add(tuition_tuition_payment);
        pnTuitionGrid.add(new JLabel("last_payment_date"));
        pnTuitionGrid.add(tuition_last_payment_date);
        pnTuitionGrid.add(new JLabel("grade_semester"));
        pnTuitionGrid.add(tuition_grade_semester);
        pnTuition.add("North", new JLabel("INSERT INTO tuition VALUES"));
        pnTuition.add("Center", pnTuitionGrid);

        /* 지도교수 입력*/
        pnTutoringGrid.add(new JLabel("student_no"));
        pnTutoringGrid.add(tutoring_student_no);
        pnTutoringGrid.add(new JLabel("professor_no"));
        pnTutoringGrid.add(tutoring_professor_no);
        pnTutoringGrid.add(new JLabel("grade_semester"));
        pnTutoringGrid.add(tutoring_grade_semester);
        pnTutoring.add("North", new JLabel("INSERT INTO tutoring VALUES"));
        pnTutoring.add("Center", pnTutoringGrid);

        btnEnter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (student_student_no.getText().equals("") || student_student_name.getText().trim().equals("") || student_student_address.getText().trim().equals("") || student_student_phone.getText().equals("") || student_student_email.getText().equals("") || student_student_account.getText().trim().equals("") || student_major_no.getText().equals("") || student_minor_no.getText().equals("") || tuition_tuition_year.getText().equals("") || tuition_tuition_semester.getText().equals("") || tuition_tuition_fee.getText().equals("") || tuition_last_payment_date.getText().equals("") || tutoring_professor_no.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "모두 입력해주세요", "", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!student_student_no.getText().matches(regExp)) {
                    JOptionPane.showMessageDialog(null, "학번은 숫자입니다.", "", JOptionPane.ERROR_MESSAGE);
                    student_student_no.requestFocus();
                    return; // 학번이 숫자가 아닐 때
                }
                if (selectStudentNo().contains(" " + student_student_no.getText() + " ") == true) {
                    JOptionPane.showMessageDialog(null, "이미 존재하는 학번입니다.", "", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (selectDepartmentNo().contains(" " + student_major_no.getText() + " ") == false) {
                    JOptionPane.showMessageDialog(null, "전공 학과가 존재하지 않습니다.", "", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (student_major_no.getText().equals(student_minor_no.getText())) {
                    JOptionPane.showMessageDialog(null, "전공과 다른 부전공을 입력해주세요.", "", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!student_minor_no.getText().equals("") && selectDepartmentNo().contains(" " + student_minor_no.getText() + " ") == false) {
                    JOptionPane.showMessageDialog(null, "부전공 학과가 존재하지 않습니다.", "", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!student_student_phone.getText().matches("(01[016789])-(\\d{3,4})-(\\d{4})")){
                    JOptionPane.showMessageDialog(null, "전화번호 형식이 올바르지 않습니다.", "", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (isValidEmail(student_student_email.getText()) == false) {
                    JOptionPane.showMessageDialog(null, "이메일 형식이 올바르지 않습니다.", "", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!tuition_tuition_year.getText().matches(regExp)){
                    JOptionPane.showMessageDialog(null, "등록연도를 확인해주세요", "", JOptionPane.ERROR_MESSAGE);
                    return; // 년도가 숫자가 아닐 때
                }
                if (!(tuition_tuition_semester.getText().equals("1") && !tuition_tuition_semester.getText().equals("2")) ) {
                    JOptionPane.showMessageDialog(null, "등록학기를 확인해주세요", "", JOptionPane.ERROR_MESSAGE);
                    return; // 학기가 올바르지 않을 때
                }
                if (!tuition_tuition_fee.getText().matches(regExp)) {
                    JOptionPane.showMessageDialog(null, "고지금액을 확인해주세요", "", JOptionPane.ERROR_MESSAGE);
                    tuition_tuition_fee.requestFocus();
                    return; // 고지금액이 숫자가 아닐 때
                }
                if (isValidDate(tuition_last_payment_date.getText()) == false) {
                    JOptionPane.showMessageDialog(null, "납부일자를 확인해주세요", "", JOptionPane.ERROR_MESSAGE);
                    return; // 날짜 형식이 유효하지 않을 때
                }
                if (isDeadline(tuition_last_payment_date.getText(), tuition_tuition_year.getText(), tuition_tuition_semester.getText()) == false) {
                    JOptionPane.showMessageDialog(null, "납부기간이 아닙니다.", "", JOptionPane.ERROR_MESSAGE);
                    return; // 날짜 형식이 유효하지 않을 때
                }

                if (selectProfessorNo().contains(" " + tutoring_professor_no.getText() + " ") == false) {
                    JOptionPane.showMessageDialog(null, "존재하지 않는 직번입니다.", "", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try { // 날짜 형식 맞춰서 입력
                    tuition_last_payment_date.setText(dateFormat.format(dateFormat.parse(tuition_last_payment_date.getText())));
                } catch (ParseException e2) {
                    JOptionPane.showMessageDialog(null, "납부일자를 확인해주세요", "", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int result = JOptionPane.showConfirmDialog(null, "학생 입학 처리를 하시겠습니까?", "", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try {   // 학생 삽입
                        stmt = con.createStatement();
                        String query = String.format(
                                "INSERT INTO student VALUES(%s, '%s', '%s', '%s', '%s', '%s', %s%s)",
                                student_student_no.getText(), student_student_name.getText(),
                                student_student_address.getText(), student_student_phone.getText(),
                                student_student_email.getText(), student_student_account.getText(),
                                student_major_no.getText(),
                                student_minor_no.getText().equals("") ? student_minor_no.getText()
                                        : ", " + student_minor_no.getText());
                        System.out.println(query);
                        stmt.execute(query);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "학생 삽입 실패 : " + ex, "", JOptionPane.ERROR_MESSAGE);
                    }
                    try {   //등록 삽입
                        stmt = con.createStatement();
                        String query = String.format("INSERT INTO tuition VALUES(%s, %s, %s, %s, %s, '%s', '%s')",
                                tuition_student_no.getText(), tuition_tuition_year.getText(),
                                tuition_tuition_semester.getText(), tuition_tuition_fee.getText(),
                                tuition_tuition_payment.getText(), tuition_last_payment_date.getText(),
                                tuition_grade_semester.getText());
                        System.out.println(query);
                        stmt.execute(query);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "등록 삽입 실패 : " + ex, "", JOptionPane.ERROR_MESSAGE);
                    }
                    try {   //지도관계 삽입
                        stmt = con.createStatement();
                        String query = String.format("INSERT INTO tutoring VALUES(%s, %s,'1학년1학기')", tutoring_student_no.getText(), tutoring_professor_no.getText());
                        System.out.println(query);
                        stmt.execute(query);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "지도관계 삽입 실패 : " + ex, "", JOptionPane.ERROR_MESSAGE);
                    }
                    JOptionPane.showMessageDialog(null, "학생 입학 처리가 완료되었습니다.", "", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        pnContent.add(pnStudent);
        pnContent.add(pnTuition);
        pnContent.add(pnTutoring);
        pnCenter.add("Center", pnContent);
        pnCenter.add("South", btnEnter);
        pnCenter.revalidate();
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

        String[] tableHeader = {"강의번호", "강좌이름", "담당교수", "시간표", "취득학점", "강좌시간", "강의실", "개설학과"};
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
                if (!rs.getString(5).equals("")) {
                    if (rs.getString(5).equals(rs.getString(7))) {
                        time += rs.getString(5) + rs.getString(7) + mapTime(rs.getString(6));
                    } else {
                        time = rs.getString(5) + mapTime(rs.getString(6));
                        if (!rs.getString(7).equals(("")))
                            time += "," + rs.getString(7) + mapTime(rs.getString(8));
                    }
                }
                String[] str = {rs.getInt(1) + " - " + rs.getInt(2), rs.getString(3), rs.getString(4), time,
                        rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12)};
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

        String[] tableHeader = {"학번", "이름", "중간(30%)", "기말(40%)", "기타(20%)", "출석(10%)", "총점", "평점"};
        String[][] tableContents = null;
        ArrayList<String[]> strList = new ArrayList<String[]>();
        try {
            stmt = con.createStatement();
            String query = "SELECT s.student_no, s.student_name, c.midterm_score, c.finals_score, c.other_score, c.attendance_score, c.total_score, c.grade\r\n"
                    + "FROM course c\r\n" + "LEFT JOIN lecture l ON l.lecture_no = c.lecture_no\r\n"
                    + "LEFT JOIN student s ON s.student_no = c.student_no\r\n" + "WHERE c.lecture_no = " + lectureNo;
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String[] str = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7), rs.getString(8)};
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
        JLabel lbMidtermScore = new JLabel("중간고사(30%)");
        lbMidtermScore.setHorizontalAlignment(JLabel.RIGHT);
        JLabel lbFinalScore = new JLabel("기말고사(40%)");
        lbFinalScore.setHorizontalAlignment(JLabel.RIGHT);
        JLabel lbOtherScore = new JLabel("기타(20%)");
        lbOtherScore.setHorizontalAlignment(JLabel.RIGHT);
        JLabel lbAttandence = new JLabel("출석(10%)");
        lbAttandence.setHorizontalAlignment(JLabel.RIGHT);
        JLabel lbTotalScore = new JLabel("총점");
        lbTotalScore.setHorizontalAlignment(JLabel.RIGHT);
        JLabel lbGrade = new JLabel("평점");
        lbGrade.setHorizontalAlignment(JLabel.RIGHT);

        JTextField tfStudentNo = new JTextField();
        JTextField tfStudentName = new JTextField();
        JTextField tfMidtermScore = new JTextField();
        JTextField tfFinalsScore = new JTextField();
        JTextField tfOtherScore = new JTextField();
        JTextField tfAttandence = new JTextField();
        JTextField tfTotalScore = new JTextField();
        JTextField tfGrade = new JTextField();

        JPanel pnBtn = new JPanel();
        JButton btnInput = new JButton("변경");
        JButton btnCancel = new JButton("취소");

        tfStudentNo.setText(studentNo);
        tfStudentNo.setEditable(false);

        tfStudentName.setText(studentName);
        tfStudentName.setEditable(false);

        tfMidtermScore.setText(midtermScore);
        tfMidtermScore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 엔터 키 입력되면 버튼 활성화
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
            }

        });
        tfMidtermScore.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                btnInput.setEnabled(false);
                btnCancel.setEnabled(false);
            }

            public void focusLost(FocusEvent e) {
                btnInput.setEnabled(true);
                btnCancel.setEnabled(true);
                try {
                    if (Double.parseDouble(tfMidtermScore.getText()) > 100.0
                            || Double.parseDouble(tfMidtermScore.getText()) < 0) {
                        JOptionPane.showMessageDialog(null, "0~100점으로 입력해주세요", "", JOptionPane.PLAIN_MESSAGE);
                        tfMidtermScore.setText("");
                        tfTotalScore.setText(calculateTotalScore(tfMidtermScore.getText(), tfFinalsScore.getText(),
                                tfOtherScore.getText(), tfAttandence.getText()));
                        tfMidtermScore.requestFocus();
                        return;
                    }
                } catch (NumberFormatException ex) {
                    if (!tfMidtermScore.getText().equals("")) { // 빈칸이 아닌경우
                        JOptionPane.showMessageDialog(null, "숫자만 입력할 수 있습니다..", "", JOptionPane.PLAIN_MESSAGE);
                        tfMidtermScore.setText("");
                        tfTotalScore.setText(calculateTotalScore(tfMidtermScore.getText(), tfFinalsScore.getText(),
                                tfOtherScore.getText(), tfAttandence.getText()));
                        tfMidtermScore.requestFocus();
                        return;
                    }
                }
                tfTotalScore.setText(calculateTotalScore(tfMidtermScore.getText(), tfFinalsScore.getText(),
                        tfOtherScore.getText(), tfAttandence.getText()));
            }
        });

        tfFinalsScore.setText(finalsScore);
        tfFinalsScore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 엔터 키 입력되면 버튼 활성화
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
            }

        });
        tfFinalsScore.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                btnInput.setEnabled(false);
                btnCancel.setEnabled(false);
            }

            public void focusLost(FocusEvent e) {
                btnInput.setEnabled(true);
                btnCancel.setEnabled(true);
                try {
                    if (Double.parseDouble(tfFinalsScore.getText()) > 100.0
                            || Double.parseDouble(tfMidtermScore.getText()) < 0) {
                        JOptionPane.showMessageDialog(null, "0~100점으로 입력해주세요", "", JOptionPane.PLAIN_MESSAGE);
                        tfFinalsScore.setText("");
                        tfTotalScore.setText(calculateTotalScore(tfMidtermScore.getText(), tfFinalsScore.getText(),
                                tfOtherScore.getText(), tfAttandence.getText()));
                        tfFinalsScore.requestFocus();
                        return;
                    }
                } catch (NumberFormatException ex) {
                    if (!tfFinalsScore.getText().equals("")) { // 빈칸이 아닌경우
                        JOptionPane.showMessageDialog(null, "숫자만 입력할 수 있습니다..", "", JOptionPane.PLAIN_MESSAGE);
                        tfFinalsScore.setText("");
                        tfTotalScore.setText(calculateTotalScore(tfMidtermScore.getText(), tfFinalsScore.getText(),
                                tfOtherScore.getText(), tfAttandence.getText()));
                        tfFinalsScore.requestFocus();
                        return;
                    }
                }
                tfTotalScore.setText(calculateTotalScore(tfMidtermScore.getText(), tfFinalsScore.getText(),
                        tfOtherScore.getText(), tfAttandence.getText()));
            }
        });

        tfOtherScore.setText(otherScore);
        tfOtherScore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 엔터 키 입력되면 버튼 활성화
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
            }

        });
        tfOtherScore.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                btnInput.setEnabled(false);
                btnCancel.setEnabled(false);
            }

            public void focusLost(FocusEvent e) {
                btnInput.setEnabled(true);
                btnCancel.setEnabled(true);
                try {
                    if (Double.parseDouble(tfOtherScore.getText()) > 100.0
                            || Double.parseDouble(tfMidtermScore.getText()) < 0) {
                        JOptionPane.showMessageDialog(null, "0~100점으로 입력해주세요", "", JOptionPane.PLAIN_MESSAGE);
                        tfOtherScore.setText("");
                        tfTotalScore.setText(calculateTotalScore(tfMidtermScore.getText(), tfFinalsScore.getText(),
                                tfOtherScore.getText(), tfAttandence.getText()));
                        tfOtherScore.requestFocus();
                        return;
                    }
                } catch (NumberFormatException ex) {
                    if (!tfOtherScore.getText().equals("")) { // 빈칸이 아닌경우
                        JOptionPane.showMessageDialog(null, "숫자만 입력할 수 있습니다..", "", JOptionPane.PLAIN_MESSAGE);
                        tfOtherScore.setText("");
                        tfTotalScore.setText(calculateTotalScore(tfMidtermScore.getText(), tfFinalsScore.getText(),
                                tfOtherScore.getText(), tfAttandence.getText()));
                        tfOtherScore.requestFocus();
                        return;
                    }
                }
                tfTotalScore.setText(calculateTotalScore(tfMidtermScore.getText(), tfFinalsScore.getText(),
                        tfOtherScore.getText(), tfAttandence.getText()));
            }
        });

        tfAttandence.setText(attendanceScore);
        tfAttandence.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 엔터 키 입력되면 버튼 활성화
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
            }

        });
        tfAttandence.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                btnInput.setEnabled(false);
                btnCancel.setEnabled(false);
            }

            public void focusLost(FocusEvent e) {
                btnInput.setEnabled(true);
                btnCancel.setEnabled(true);
                try {
                    if (Double.parseDouble(tfAttandence.getText()) > 100.0
                            || Double.parseDouble(tfMidtermScore.getText()) < 0) {
                        JOptionPane.showMessageDialog(null, "0~100점으로 입력해주세요", "", JOptionPane.PLAIN_MESSAGE);
                        tfAttandence.setText("");
                        tfTotalScore.setText(calculateTotalScore(tfMidtermScore.getText(), tfFinalsScore.getText(),
                                tfOtherScore.getText(), tfAttandence.getText()));
                        tfAttandence.requestFocus();
                        return;
                    }
                } catch (NumberFormatException ex) {
                    if (!tfAttandence.getText().equals("")) { // 빈칸이 아닌경우
                        JOptionPane.showMessageDialog(null, "숫자만 입력할 수 있습니다..", "", JOptionPane.PLAIN_MESSAGE);
                        tfAttandence.setText("");
                        tfTotalScore.setText(calculateTotalScore(tfMidtermScore.getText(), tfFinalsScore.getText(),
                                tfOtherScore.getText(), tfAttandence.getText()));
                        tfAttandence.requestFocus();
                        return;
                    }
                }
                tfTotalScore.setText(calculateTotalScore(tfMidtermScore.getText(), tfFinalsScore.getText(),
                        tfOtherScore.getText(), tfAttandence.getText()));
            }
        });

        tfTotalScore.setText(totalScore);
        tfTotalScore.setEditable(false);
        tfGrade.setText(grade);
        tfGrade.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 엔터 키 입력되면 버튼 활성화
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
            }

        });
        tfGrade.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                btnInput.setEnabled(false);
                btnCancel.setEnabled(false);
            }

            public void focusLost(FocusEvent e) {
                btnInput.setEnabled(true);
                btnCancel.setEnabled(true);
                if (!(tfGrade.getText().equals("A+") || tfGrade.getText().equals("A") || tfGrade.getText().equals("B+")
                        || tfGrade.getText().equals("B") || tfGrade.getText().equals("C+")
                        || tfGrade.getText().equals("C") || tfGrade.equals("D+") || tfGrade.getText().equals("D")
                        || tfGrade.getText().equals("F") || tfGrade.getText().equals("FA")
                        || tfGrade.getText().equals("P") || tfGrade.getText().equals("NP")
                        || tfGrade.getText().equals(""))) {
                    JOptionPane.showMessageDialog(null, "평점을 확인해주세요.", "", JOptionPane.PLAIN_MESSAGE);
                    tfGrade.setText("");
                    tfGrade.requestFocus();
                }
            }
        });

        pnBtn.setPreferredSize(new Dimension(430, 70));
        btnInput.setPreferredSize(new Dimension(200, 50));
        btnInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tfTotalScore.setText(calculateTotalScore(tfMidtermScore.getText(), tfFinalsScore.getText(),
                        tfOtherScore.getText(), tfAttandence.getText()));
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
        String[] tableHeader = {"학번", "이름", "학년", "이메일", "전화번호", "주소", "계좌", "전공", "부전공"};
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
                String[] str = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)};
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
                String[] tableHeader = {"강의번호", "강의명", "중간(30%)", "기말(40%)", "기타(20%)", "출석(10%)", "총점", "평점"};
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
                        String[] str = {rs1.getString(1), rs1.getString(2), rs1.getString(3), rs1.getString(4),
                                rs1.getString(5), rs1.getString(6), rs1.getString(7), rs1.getString(8)};
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
        String[] tableHeader = {"학과번호", "학과이름", "연락처", "사무실", "학과장 직번", "학과장 이름", "학과장 사무실", "학과장 연락처", "학과장 이메일"};
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
                String[] str = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)};
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

        String[] period_map = {"8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30",
                "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00",
                "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30",
                "24:00"};

        String[] columns = {"교시", "월", "화", "수", "목", "금", "토"};
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

            String[] columns_list = {"강의번호", "분반", "교과목명", "학점", "시간표"};
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

        String[] period_map = {"8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30",
                "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00",
                "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30",
                "24:00"};

        String[] columns = {"교시", "월", "화", "수", "목", "금", "토"};
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

            String[] columns_list = {"동아리번호", "이름", "회장", "회원 수", "지도교수", "동아리방"};
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

                        String[] columns_list = {"학번", "이름", "주소", "번호", "이메일", "계좌", "전공", "부전공"};
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

        String[] grade_list = {"A+", "A0", "B+", "B0", "C+", "C0", "D+", "D0", "FA", "F", "P", "NP"};
        Double[] gradepoints = {4.5, 4.0, 3.5, 3.0, 2.5, 2.0, 1.5, 1.0, 0.0, 0.0};
        Vector<String> grade = new Vector<String>();
        for (int i = 0; i < grade_list.length; i++) {
            grade.add(grade_list[i]);
        }

        try {
            stmt = con.createStatement();
            String query = "select l.lecture_no, l.lecture_name, l.lecture_credit, c.grade\r\n"
                    + "from lecture l, course c\r\n" + "where l.lecture_no = c.lecture_no and not(l.lecture_year >= "
                    + todayYear + " and l.lecture_semester >= " + todaySemester + " )\r\n" + "and c.student_no = "
                    + userInfo;
            rs = stmt.executeQuery(query);

            String[] columns_list = {"과목번호", "교과목명", "학점", "등급", "평점"};
            Vector<String> columns = new Vector<String>();
            Vector<Vector<String>> contents = new Vector<Vector<String>>();

            for (int i = 0; i < columns_list.length; i++)
                columns.add(columns_list[i]);
            Double GPA = 0.0;

            Integer getCredit = 0; // 취득학점 <8, ==10
            Integer gradeCredit = 0; // GPA반영학점 < 10
            Integer totalCredit = 0; // 신청학점

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

                    if (idx < 8) {
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
            scrollpane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(),
                    "<HTML><div style=\\\"text-align:left\\\">신청학점 : " + totalCredit + "<br />취득학점 : " + getCredit
                            + "<br />GPA : " + GPA + "</div></HTML>",
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("times new roman", Font.PLAIN, 15), Color.RED));
            pnCenter.add("Center", scrollpane);

            // pnHeader.add("South", gradeinfo);

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

        String[] period_map = {"8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30",
                "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00",
                "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30",
                "24:00"};

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

    public void initYearAndSemester() {
        Date now = new Date();
        SimpleDateFormat yearFormat = new SimpleDateFormat("YYYY");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        int year = Integer.parseInt(yearFormat.format(now));
        int month = Integer.parseInt(monthFormat.format(now));
        if (month == 1) { // 1월
            todayYear = Integer.toString(year - 1);
            todaySemester = "2";
        } else if (month > 1 && month < 8) { // 2, 3, ,4, 5, 6, 7
            todayYear = Integer.toString(year);
            todaySemester = "1";
        } else { // 8, 9, 10, 11, 12
            todayYear = Integer.toString(year);
            todaySemester = "2";
        }
    }

    public String calculateTotalScore(String midtermStr, String FinalsStr, String OtherStr, String AttandenceStr) {
        double midterm, finals, other, attendance;
        if (midtermStr.equals("")) {
            midterm = 0;
        } else {
            midterm = Double.parseDouble(midtermStr);
        }
        if (FinalsStr.equals("")) {
            finals = 0;
        } else {
            finals = Double.parseDouble(FinalsStr);
        }
        if (OtherStr.equals("")) {
            other = 0;
        } else {
            other = Double.parseDouble(OtherStr);
        }
        if (AttandenceStr.equals("")) {
            attendance = 0;
        } else {
            attendance = Double.parseDouble(AttandenceStr);
        }

        return Double.toString(midterm * 0.3 + finals * 0.4 + other * 0.2 + attendance * 0.1);
    }

    /* 유효성 검사 */
    // 이메일 형식 확인
    public static boolean isValidEmail(String email) {
        return Pattern.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", email);
    }

    // 날짜 유효성, 포멧 확인
    public boolean isValidDate(String checkDate) {
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(checkDate);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // 등록할 년도-학기와 등록금 납부월이 일치하지 않을 때 (늦게낸거는 등록하거나 수정할 필요 x)
    public boolean isDeadline(String checkDate, String year, String semester) {
        SimpleDateFormat deadlineDateFormat = new SimpleDateFormat("yyyy-MM", Locale.KOREA);
        deadlineDateFormat.setLenient(false);
        try {
            if (semester.equals("1")) {
                return deadlineDateFormat.format(deadlineDateFormat.parse(checkDate)).equals(year+"-02");
            } else if (semester.equals("2")) {
                return deadlineDateFormat.format(deadlineDateFormat.parse(checkDate)).equals(year+"-08");
            } else {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
    }

    // 학년/학기 형식 확인
    public boolean isValidGradeSemester(String grade_semester) {
        String[] grade = grade_semester.split("학년");
        if (grade.length == 2 && grade[1].split("학기").length == 1) {
            if (!grade[0].matches(regExp)) {
                return false; // 숫자가 아닌 연도
            }
            String[] semester = grade[1].split("학기");
            if ((!semester[0].equals("1") && !semester[0].equals("2")) || semester.length != 1) {
                return false; // 1학기 2학기 외에 다른 학기거나 학기 뒤에 다른 문자가 붙어있을때
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /* 쿼리 유효성 검사 */
    public String selectStudentNo() {
        String str = " ";
        try {
            stmt = con.createStatement();
            String query = "SELECT student_no FROM student";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                str += rs.getString(1);
                str += " ";
            }
        } catch (SQLException e) {
            System.out.println("쿼리 읽기 실패 :" + e);
        }
        return str;
    }

    public String selectProfessorNo() {
        String str = " ";
        try {
            stmt = con.createStatement();
            String query = "SELECT professor_no FROM professor";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                str += rs.getString(1);
                str += " ";
            }
        } catch (SQLException e) {
            System.out.println("쿼리 읽기 실패 :" + e);
        }
        return str;
    }

    public String selectDepartmentNo() {
        String str = " ";
        try {
            stmt = con.createStatement();
            String query = "SELECT department_no FROM department";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                str += rs.getString(1);
                str += " ";
            }
        } catch (SQLException e) {
            System.out.println("쿼리 읽기 실패 :" + e);
        }
        return str;
    }

    public String selectClubNo() {
        String str = " ";
        try {
            stmt = con.createStatement();
            String query = "SELECT club_no FROM club";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                str += rs.getString(1);
                str += " ";
            }
        } catch (SQLException e) {
            System.out.println("쿼리 읽기 실패 :" + e);
        }
        return str;
    }

    public boolean deleteFrom(String query) {
        try {
            stmt = con.createStatement();
            stmt.execute(query);
        } catch (SQLException e) {
            System.out.println("삭제 실패 :" + e);
            return false;
        }
        return true;
    }

    public boolean updateSet(String query) {
        try {
            stmt = con.createStatement();
            stmt.execute(query);
        } catch (SQLException e) {
            System.out.println("수정 실패 :" + e);
            return false;
        }
        return true;
    }

    /* 최근 학년-학기, 등록금 완납 안된 학기는 무시 */
    public boolean selectLastGradeSemester(String student_no, String grade_semester) { // 학년-학기 형식에 맞는 것만 입력됨
        String str = "";
        String grade = "";
        String semester = "";
        String[] newGradeSemester = grade_semester.split("학년");

        try {
            stmt = con.createStatement();
            String query = String.format(
                    "SELECT grade_semester FROM tuition WHERE student_no = %s AND tuition_fee = tuition_payment ORDER BY grade_semester DESC LIMIT 1",
                    student_no);
            rs = stmt.executeQuery(query);
            rs.next();
            str += rs.getString(1); // 마지막 등록완료된 학년/학기
        } catch (SQLException e) {
            System.out.println("쿼리 읽기 실패 :" + e);
        }
        grade = str.split("학년")[0];
        semester = (str.split("학년")[1]).split("학기")[0];
        if (semester.equals("2")) {
            grade = Integer.toString(Integer.parseInt(grade) + 1);
            semester = "1";
        } else {
            semester = "2";
        }
        if (grade_semester.equals(grade + "학년" + semester + "학기")) { // 새로 등록할 학년/학기가 (마지막 등록했던 학년/학기의 다음 학년/학기와 )일치하는지
            return true;
        } else {
            return false;
        }
    }

    /* 현재 학년-학기보다 낮은 학기인가 */
    public boolean isNextGradeSemesters(String student_no, String grade_semester) {
        // 학년-학기 형식에 맞는 것만 입력됨
        String str = "", grade = "", semester = "", grade1 = "", semester1 = "";
        try {
            stmt = con.createStatement();
            String query = String.format(
                    "SELECT grade_semester FROM tuition WHERE student_no = %s ORDER BY grade_semester DESC LIMIT 1",
                    student_no);
            rs = stmt.executeQuery(query);
            rs.next();
            str += rs.getString(1);
        } catch (SQLException e) {
            System.out.println("마지막 등록완료된 학년-학기 조회 실패 :" + e);
        }
        // 마지막 등록완료된 학년-학기
        grade = str.split("학년")[0];
        semester = (str.split("학년")[1]).split("학기")[0];
        grade1 = grade_semester.split("학년")[0];
        semester1 = (grade_semester.split("학년")[1]).split("학기")[0];
        // 검사
        if (Integer.parseInt(grade) < Integer.parseInt(grade1)) {
            System.out.println(grade + "-" + semester);
            return true;    // 예) 현재 2학년1학기 등록, 입력은 3학년 1학기
        }
        if (grade.equals(grade1) && semester.equals("1") && semester1.equals("2")) {
            System.out.println(grade + ":" + semester);
            return true;     // 예) 현재 2학년1학기 등록, 입력은 2학년 2학기
        }
        return false;    //이전 또는 현재 학기
    }

    /* 등록 여부 */
    public boolean didEnroll(String studentNo, String tuitionYear, String tuitionSemester) {
        try {
            stmt = con.createStatement();
            String query = String.format(
                    "SELECT COUNT(*) FROM tuition WHERE student_no = %s AND tuition_year = %s AND tuition_semester = %s",
                    studentNo, tuitionYear, tuitionSemester);
            rs = stmt.executeQuery(query);
            rs.next();
            if (rs.getInt(1) == 0) {
                return false; // 등록 안되어있음
            } else {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("쿼리 읽기 실패 :" + e);
        }
        return true;
    }

    /* 동아리 가입여부 */
    public boolean isJoiningClub(String student_no, String club_no) {
        try {
            stmt = con.createStatement();
            String query = String.format("SELECT COUNT(*) FROM club_join WHERE student_no = %s AND club_no = %s",
                    student_no, club_no);
            rs = stmt.executeQuery(query);
            rs.next();
            if (rs.getInt(1) == 0) {
                return false; // 등록 안되어있음
            } else {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("쿼리 읽기 실패 :" + e);
        }
        return true;
    }

    // DB 테이블 반환

    public JScrollPane showTableProfessor() {
        /* 내용 */
        String[] tableHeader = {"professor_no", "professor_name", "professor_address", "professor_phone",
                "professor_email"};
        String[][] tableContents = null;
        ArrayList<String[]> strList = new ArrayList<String[]>();
        try {
            stmt = con.createStatement();
            String query = "SELECT professor_no, professor_name, professor_address, professor_phone, professor_email FROM professor";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String[] str = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)};
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
        String[] tableHeader = {"department_no", "department_name", "department_contact", "department_office",
                "professor_no"};
        String[][] tableContents = null;
        ArrayList<String[]> strList = new ArrayList<String[]>();
        try {
            stmt = con.createStatement();
            String query = "SELECT department_no, department_name, department_contact, department_office, professor_no FROM department";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String[] str = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)};
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
        String[] tableHeader = {"professor_no", "department_no"};
        String[][] tableContents = null;
        ArrayList<String[]> strList = new ArrayList<String[]>();
        try {
            stmt = con.createStatement();
            String query = "SELECT professor_no, department_no FROM affiliated_professor";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String[] str = {rs.getString(1), rs.getString(2)};
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
        String[] tableHeader = {"lecture_no", "lecture_class_no", "lecture_name", "lecture_day1", "lecture_period1",
                "lecture_day2", "lecture_period2", "lecture_credit", "lecture_time", "lecture_room", "department_no",
                "professor_no", "lecture_year", "lecture_semester"};
        String[][] tableContents = null;
        ArrayList<String[]> strList = new ArrayList<String[]>();
        try {
            stmt = con.createStatement();
            String query = "SELECT lecture_no, lecture_class_no, lecture_name, lecture_day1, lecture_period1, lecture_day2, lecture_period2, lecture_credit, lecture_time, lecture_room, department_no, professor_no, lecture_year, lecture_semester FROM lecture";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String[] str = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10),
                        rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14)};
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
        String[] tableHeader = {"student_no", "student_name", "student_address", "student_phone", "student_email",
                "student_account", "major_no", "minor_no"};
        String[][] tableContents = null;
        ArrayList<String[]> strList = new ArrayList<String[]>();
        try {
            stmt = con.createStatement();
            String query = "SELECT student_no, student_name, student_address, student_phone, student_email, student_account, major_no, minor_no FROM student";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String[] str = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7), rs.getString(8)};
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
        String[] tableHeader = {"student_no", "tuition_year", "tuition_semester", "tuition_fee", "tuition_payment",
                "last_payment_date", "grade_semester"};
        String[][] tableContents = null;
        ArrayList<String[]> strList = new ArrayList<String[]>();
        try {
            stmt = con.createStatement();
            String query = "SELECT student_no, tuition_year, tuition_semester, tuition_fee, tuition_payment, last_payment_date, grade_semester FROM tuition ORDER BY tuition_year DESC, tuition_semester DESC, student_no";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String[] str = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7)};
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
        String[] tableHeader = {"student_no", "professor_no", "grade_semester"};
        String[][] tableContents = null;
        ArrayList<String[]> strList = new ArrayList<String[]>();
        try {
            stmt = con.createStatement();
            String query = "SELECT student_no, professor_no, grade_semester FROM tutoring";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String[] str = {rs.getString(1), rs.getString(2), rs.getString(3)};
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
        String[] tableHeader = {"lecture_no", "student_no", "midterm_score", "finals_score", "other_score",
                "attendance_score", "total_score", "grade"};
        String[][] tableContents = null;
        ArrayList<String[]> strList = new ArrayList<String[]>();
        try {
            stmt = con.createStatement();
            String query = "SELECT lecture_no, student_no, midterm_score, finals_score, other_score, attendance_score, total_score, grade FROM course";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String[] str = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7), rs.getString(8)};
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
        String[] tableHeader = {"club_no", "club_name", "club_room", "professor_no", "student_no",
                "club_total_member"};
        String[][] tableContents = null;
        ArrayList<String[]> strList = new ArrayList<String[]>();
        try {
            stmt = con.createStatement();
            String query = "SELECT club_no, club_name, club_room, professor_no, student_no, club_total_member FROM club";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String[] str = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6)};
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
        String[] tableHeader = {"club_no", "student_no"};
        String[][] tableContents = null;
        ArrayList<String[]> strList = new ArrayList<String[]>();
        try {
            stmt = con.createStatement();
            String query = "SELECT club_no, student_no FROM club_join";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String[] str = {rs.getString(1), rs.getString(2)};
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

    // TODO 미완료 : professor, department, affiliatedProfessor, student, tuition
    public JPanel adminAdministrationProfessor(JPanel pnHeader) {
        c.remove(pnCenter);
        pnCenter.removeAll();
        pnCenter.add("North", pnHeader);
        pnCenter.add("South", showTableProfessor());
        JPanel pnBtn = (JPanel) pnHeader.getComponent(1);

        JPanel pnContent = new JPanel();
        pnContent.setLayout(new GridLayout(1, 3, 5, 5));
        JPanel pnInsert = new JPanel();
        pnInsert.setLayout(new BorderLayout());
        JPanel pnUpdate = new JPanel();
        pnUpdate.setLayout(new BorderLayout());
        JPanel pnDelete = new JPanel();
        pnDelete.setLayout(new BorderLayout());

        /* 입력 */
        /* 삽입 */

        JPanel pnInsertGrid = new JPanel();
        pnInsertGrid.setLayout(new GridLayout(6, 2, 5, 5));
        JTextField insert_professor_no = new JTextField();
        JTextField insert_professor_name = new JTextField();
        JTextField insert_professor_address = new JTextField();
        JTextField insert_professor_phone = new JTextField();
        JTextField insert_professor_email = new JTextField();
        JButton btnInsert = new JButton("입력");

        pnInsertGrid.add(new JLabel("professor_no"));
        pnInsertGrid.add(insert_professor_no);
        pnInsertGrid.add(new JLabel("professor_name"));
        pnInsertGrid.add(insert_professor_name);
        pnInsertGrid.add(new JLabel("professor_address"));
        pnInsertGrid.add(insert_professor_address);
        pnInsertGrid.add(new JLabel("professor_phone"));
        pnInsertGrid.add(insert_professor_phone);
        pnInsertGrid.add(new JLabel("professor_email"));
        pnInsertGrid.add(insert_professor_email);
        pnInsertGrid.add(new JLabel(""));
        pnInsertGrid.add(btnInsert);
        pnInsert.add("North", new JLabel("INSERT INTO professor VALUES"));
        pnInsert.add("Center", pnInsertGrid);

        /* 수정 */
        JPanel pnUpdateGrid = new JPanel();
        pnUpdateGrid.setLayout(new GridLayout(7, 2, 5, 5));
        JCheckBox update_professor_no_check = new JCheckBox("professor_no");
        JCheckBox update_professor_name_check = new JCheckBox("professor_name");
        JCheckBox update_professor_address_check = new JCheckBox("professor_address");
        JCheckBox update_professor_phone_check = new JCheckBox("professor_phone");
        JCheckBox update_professor_email_check = new JCheckBox("professor_email");

        JTextField update_professor_no = new JTextField();
        JTextField update_professor_name = new JTextField();
        JTextField update_professor_address = new JTextField();
        JTextField update_professor_phone = new JTextField();
        JTextField update_professor_email = new JTextField();
        JTextField update_where = new JTextField();
        JButton btnUpdate = new JButton("수정");
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String query = "UPDATE professor SET ";
                if (update_professor_no_check.isSelected()) {
                    query += update_professor_no_check.getText() + " = " + update_professor_no.getText() + ", ";
                }
                if (update_professor_name_check.isSelected()) {
                    query += update_professor_name_check.getText() + " = '" + update_professor_name.getText() + "', ";
                }
                if (update_professor_address_check.isSelected()) {
                    query += update_professor_address_check.getText() + " = '" + update_professor_address.getText()
                            + "', ";
                }
                if (update_professor_phone_check.isSelected()) {
                    query += update_professor_phone_check.getText() + " = '" + update_professor_phone.getText()
                            + "' , ";
                }
                if (update_professor_email_check.isSelected()) {
                    query += update_professor_email_check.getText() + " = '" + update_professor_email.getText() + "', ";
                }
                query = query.substring(0, query.length() - 2);
                if (!update_where.getText().equals("")) {
                    query += " WHERE " + update_where.getText();
                }
                updateSet(query);
                ((AbstractButton) pnBtn.getComponent(0)).doClick();

            }
        });

        pnUpdateGrid.add(update_professor_no_check);
        pnUpdateGrid.add(update_professor_no);
        pnUpdateGrid.add(update_professor_name_check);
        pnUpdateGrid.add(update_professor_name);
        pnUpdateGrid.add(update_professor_address_check);
        pnUpdateGrid.add(update_professor_address);
        pnUpdateGrid.add(update_professor_phone_check);
        pnUpdateGrid.add(update_professor_phone);
        pnUpdateGrid.add(update_professor_email_check);
        pnUpdateGrid.add(update_professor_email);
        pnUpdateGrid.add(new JLabel("WHERE "));
        pnUpdateGrid.add(update_where);
        pnUpdateGrid.add(new JLabel(""));
        pnUpdateGrid.add(btnUpdate);
        pnUpdate.add("North", new JLabel("UPDATE professor SET"));
        pnUpdate.add("Center", pnUpdateGrid);

        /* 삭제 */
        JTextField delete_where = new JTextField();
        JButton btnDelete = new JButton("삭제");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "삭제 하시겠습니까?", "", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    if (deleteFrom("DELETE FROM professor WHERE " + delete_where.getText()) == true) {
                        JOptionPane.showMessageDialog(null, "삭제 완료", "", JOptionPane.PLAIN_MESSAGE);
                        ((AbstractButton) pnBtn.getComponent(0)).doClick();
                    } else {
                        JOptionPane.showMessageDialog(null, "삭제 실패 :" + e, "", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        pnDelete.add("North", new JLabel("DELETE FROM professor WHERE"));
        pnDelete.add("Center", delete_where);
        pnDelete.add("South", btnDelete);

        pnContent.add(pnInsert);
        pnContent.add(pnUpdate);
        pnContent.add(pnDelete);
        pnCenter.add("Center", pnContent);
        return pnCenter;

    }

    public JPanel adminAdministrationDepartment(JPanel pnHeader) {
        c.remove(pnCenter);
        pnCenter.removeAll();
        pnCenter.add("North", pnHeader);
        pnCenter.add("South", showTableDepartment());
        JPanel pnBtn = (JPanel) pnHeader.getComponent(1);

        JPanel pnContent = new JPanel();
        pnContent.setLayout(new GridLayout(1, 3, 5, 5));
        JPanel pnInsert = new JPanel();
        pnInsert.setLayout(new BorderLayout());
        JPanel pnUpdate = new JPanel();
        pnUpdate.setLayout(new BorderLayout());
        JPanel pnDelete = new JPanel();
        pnDelete.setLayout(new BorderLayout());

        /* 입력 */
        JPanel pnInsertGrid = new JPanel();
        pnInsertGrid.setLayout(new GridLayout(6, 2, 5, 5));
        JTextField insert_department_no = new JTextField();
        JTextField insert_department_name = new JTextField();
        JTextField insert_department_contact = new JTextField();
        JTextField insert_department_office = new JTextField();
        JTextField insert_professor_no = new JTextField();
        JButton btnInsert = new JButton("입력");
        btnInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });

        pnInsertGrid.add(new JLabel("department_no"));
        pnInsertGrid.add(insert_department_no);
        pnInsertGrid.add(new JLabel("department_name"));
        pnInsertGrid.add(insert_department_name);
        pnInsertGrid.add(new JLabel("department_contact"));
        pnInsertGrid.add(insert_department_contact);
        pnInsertGrid.add(new JLabel("department_office"));
        pnInsertGrid.add(insert_department_office);
        pnInsertGrid.add(new JLabel("professor_no"));
        pnInsertGrid.add(insert_professor_no);
        pnInsertGrid.add(new JLabel(""));
        pnInsertGrid.add(btnInsert);
        pnInsert.add("North", new JLabel("INSERT INTO department VALUES"));
        pnInsert.add("Center", pnInsertGrid);

        /* 수정 */
        JPanel pnUpdateGrid = new JPanel();
        pnUpdateGrid.setLayout(new GridLayout(7, 2, 5, 5));
        JCheckBox update_department_no_check = new JCheckBox("department_no");
        JCheckBox update_department_name_check = new JCheckBox("department_name");
        JCheckBox update_department_contact_check = new JCheckBox("department_contact");
        JCheckBox update_department_office_check = new JCheckBox("department_office");
        JCheckBox update_professor_no_check = new JCheckBox("professor_no");

        JTextField update_department_no = new JTextField();
        JTextField update_department_name = new JTextField();
        JTextField update_department_contact = new JTextField();
        JTextField update_department_office = new JTextField();
        JTextField update_professor_no = new JTextField();
        JTextField update_where = new JTextField();
        JButton btnUpdate = new JButton("수정");
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String query = "UPDATE department SET ";
                if (update_department_no_check.isSelected()) {
                    query += update_department_no_check.getText() + " = " + update_department_no.getText() + ", ";
                }
                if (update_department_name_check.isSelected()) {
                    query += update_department_name_check.getText() + " = '" + update_department_name.getText() + "', ";
                }
                if (update_department_contact_check.isSelected()) {
                    query += update_department_contact_check.getText() + " = '" + update_department_contact.getText()
                            + "', ";
                }
                if (update_department_office_check.isSelected()) {
                    query += update_department_office_check.getText() + " = '" + update_department_office.getText()
                            + "', ";
                }
                if (update_professor_no_check.isSelected()) {
                    query += update_professor_no_check.getText() + " = " + update_professor_no.getText() + ", ";
                }
                query = query.substring(0, query.length() - 2);
                if (!update_where.getText().equals("")) {
                    query += " WHERE " + update_where.getText();
                }
                updateSet(query);
                ((AbstractButton) pnBtn.getComponent(1)).doClick();

            }
        });

        pnUpdateGrid.add(update_department_no_check);
        pnUpdateGrid.add(update_department_no);
        pnUpdateGrid.add(update_department_name_check);
        pnUpdateGrid.add(update_department_name);
        pnUpdateGrid.add(update_department_contact_check);
        pnUpdateGrid.add(update_department_contact);
        pnUpdateGrid.add(update_department_office_check);
        pnUpdateGrid.add(update_department_office);
        pnUpdateGrid.add(update_professor_no_check);
        pnUpdateGrid.add(update_professor_no);
        pnUpdateGrid.add(new JLabel("WHERE "));
        pnUpdateGrid.add(update_where);
        pnUpdateGrid.add(new JLabel(""));
        pnUpdateGrid.add(btnUpdate);
        pnUpdate.add("North", new JLabel("UPDATE department SET"));
        pnUpdate.add("Center", pnUpdateGrid);

        /* 삭제 */
        JTextField delete_where = new JTextField();
        JButton btnDelete = new JButton("삭제");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "삭제 하시겠습니까?", "", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    if (deleteFrom("DELETE FROM department WHERE " + delete_where.getText()) == true) {
                        JOptionPane.showMessageDialog(null, "삭제 완료", "", JOptionPane.PLAIN_MESSAGE);
                        ((AbstractButton) pnBtn.getComponent(1)).doClick();
                    } else {
                        JOptionPane.showMessageDialog(null, "삭제 실패 :" + e, "", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        pnDelete.add("North", new JLabel("DELETE FROM department WHERE"));
        pnDelete.add("Center", delete_where);
        pnDelete.add("South", btnDelete);

        pnContent.add(pnInsert);
        pnContent.add(pnUpdate);
        pnContent.add(pnDelete);
        pnCenter.add("Center", pnContent);
        return pnCenter;

    }

    public JPanel adminAdministrationAffiliatedProfessor(JPanel pnHeader) {

        c.remove(pnCenter);
        pnCenter.removeAll();
        pnCenter.add("North", pnHeader);
        pnCenter.add("South", showTableAffiliatedProfessor());
        JPanel pnBtn = (JPanel) pnHeader.getComponent(1);

        JPanel pnContent = new JPanel();
        pnContent.setLayout(new GridLayout(1, 3, 5, 5));
        JPanel pnInsert = new JPanel();
        pnInsert.setLayout(new BorderLayout());
        JPanel pnUpdate = new JPanel();
        pnUpdate.setLayout(new BorderLayout());
        JPanel pnDelete = new JPanel();
        pnDelete.setLayout(new BorderLayout());

        JPanel pnInsertGrid = new JPanel();
        pnInsertGrid.setLayout(new GridLayout(3, 2, 5, 5));
        JTextField insert_professor_no = new JTextField();
        JTextField insert_department_no = new JTextField();
        JButton btnInsert = new JButton("입력");
        btnInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });

        pnInsertGrid.add(new JLabel("professor_no"));
        pnInsertGrid.add(insert_professor_no);
        pnInsertGrid.add(new JLabel("department_no"));
        pnInsertGrid.add(insert_department_no);
        pnInsertGrid.add(new JLabel(""));
        pnInsertGrid.add(btnInsert);
        pnInsert.add("North", new JLabel("INSERT INTO affiliated_professor VALUES"));
        pnInsert.add("Center", pnInsertGrid);

        /* 수정 */
        JPanel pnUpdateGrid = new JPanel();
        pnUpdateGrid.setLayout(new GridLayout(4, 2, 5, 5));
        JCheckBox update_professor_no_check = new JCheckBox("professor_no");
        JCheckBox update_department_no_check = new JCheckBox("department_no");
        JTextField update_professor_no = new JTextField();
        JTextField update_department_no = new JTextField();
        JTextField update_where = new JTextField();
        JButton btnUpdate = new JButton("수정");
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String query = "UPDATE affiliated_professor SET ";
                if (update_professor_no_check.isSelected()) {
                    query += update_professor_no_check.getText() + " = " + update_professor_no.getText() + ", ";
                }
                if (update_department_no_check.isSelected()) {
                    query += update_department_no_check.getText() + " = " + update_department_no.getText() + ", ";
                }
                query = query.substring(0, query.length() - 2);
                if (!update_where.getText().equals("")) {
                    query += " WHERE " + update_where.getText();
                }
                updateSet(query);
                ((AbstractButton) pnBtn.getComponent(2)).doClick();

            }
        });

        pnUpdateGrid.add(update_professor_no_check);
        pnUpdateGrid.add(update_professor_no);
        pnUpdateGrid.add(update_department_no_check);
        pnUpdateGrid.add(update_department_no);
        pnUpdateGrid.add(new JLabel("WHERE "));
        pnUpdateGrid.add(update_where);
        pnUpdateGrid.add(new JLabel(""));
        pnUpdateGrid.add(btnUpdate);
        pnUpdate.add("North", new JLabel("UPDATE affiliated_professor SET"));
        pnUpdate.add("Center", pnUpdateGrid);

        /* 삭제 */
        JTextField delete_where = new JTextField();
        JButton btnDelete = new JButton("삭제");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "삭제 하시겠습니까?", "", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    if (deleteFrom("DELETE FROM affiliated_professor WHERE " + delete_where.getText()) == true) {
                        JOptionPane.showMessageDialog(null, "삭제 완료", "", JOptionPane.PLAIN_MESSAGE);
                        ((AbstractButton) pnBtn.getComponent(2)).doClick();
                    } else {
                        JOptionPane.showMessageDialog(null, "삭제 실패 :" + e, "", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        pnDelete.add("North", new JLabel("DELETE FROM affiliated_professor WHERE"));
        pnDelete.add("Center", delete_where);
        pnDelete.add("South", btnDelete);

        pnContent.add(pnInsert);
        pnContent.add(pnUpdate);
        pnContent.add(pnDelete);
        pnCenter.add("Center", pnContent);
        return pnCenter;

    }

    //	public JPanel adminAdministrationLecture(JPanel pnHeader) {}
    public JPanel adminAdministrationStudent(JPanel pnHeader) {
        c.remove(pnCenter);
        pnCenter.removeAll();
        pnCenter.add("North", pnHeader);
        pnCenter.add("South", showTableStudent());
        JPanel pnBtn = (JPanel) pnHeader.getComponent(1);

        JPanel pnContent = new JPanel();
        pnContent.setLayout(new GridLayout(1, 3, 5, 5));
        JPanel pnInsert = new JPanel();
        //pnInsert.setLayout(new BorderLayout());
        JPanel pnUpdate = new JPanel();
        pnUpdate.setLayout(new BorderLayout());
        JPanel pnDelete = new JPanel();
        pnDelete.setLayout(new BorderLayout());

        /* 입력 */
        JLabel banInsert = new JLabel("새로운 학생 삽입은 입학처리 기능을 이용해주세요");
        JButton enterShortCut = new JButton("바로가기");
        enterShortCut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adminOthers();
                adminEnterHandle();
            }
        });
        pnInsert.add(banInsert);
        pnInsert.add(enterShortCut);

        /* 수정 */
        JPanel pnUpdateGrid = new JPanel();
        pnUpdateGrid.setLayout(new GridLayout(10, 2, 5, 5));
        JCheckBox update_student_no_check = new JCheckBox("student_no");
        JCheckBox update_student_name_check = new JCheckBox("student_name");
        JCheckBox update_student_address_check = new JCheckBox("student_address");
        JCheckBox update_student_phone_check = new JCheckBox("student_phone");
        JCheckBox update_student_email_check = new JCheckBox("student_email");
        JCheckBox update_student_account_check = new JCheckBox("student_account");
        JCheckBox update_major_no_check = new JCheckBox("major_no");
        JCheckBox update_minor_no_check = new JCheckBox("minor_no");
        /* 전공과 부전공은 동시에 바꿔야 한다. */
//        update_major_no_check.addItemListener(new ItemListener() {
//            public void itemStateChanged(ItemEvent e) {
//                if (update_major_no_check.isSelected()) {
//                    JOptionPane.showMessageDialog(null, "전공/부전공 비교를 위해 함께 변경되어야 합니다.", "", JOptionPane.PLAIN_MESSAGE);
//                    update_minor_no_check.setSelected(true);
//                } else { // !update_major_no_check.isSelected()
//                    update_minor_no_check.setSelected(false);
//                }
//            }
//        });
//        update_minor_no_check.addItemListener(new ItemListener() {
//            public void itemStateChanged(ItemEvent e) {
//                if (update_minor_no_check.isSelected()) {
//                    update_major_no_check.setSelected(true);
//                } else { // !update_minor_no_check.isSelected()
//                    update_major_no_check.setSelected(false);
//                }
//            }
//        });

        JTextField update_student_no = new JTextField();
        JTextField update_student_name = new JTextField();
        JTextField update_student_address = new JTextField();
        JTextField update_student_phone = new JTextField();
        JTextField update_student_email = new JTextField();
        JTextField update_student_account = new JTextField();
        JTextField update_major_no = new JTextField();
        JTextField update_minor_no = new JTextField();
        JTextField update_where = new JTextField();
        JButton btnUpdate = new JButton("수정");
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String query = "UPDATE student SET ";
                if (update_where.getText().equals("")) {
                    if (update_student_no_check.isSelected() || (!update_major_no_check.isSelected() && update_minor_no_check.isSelected()) || (update_major_no_check.isSelected() && !update_minor_no_check.isSelected())) {
                        JOptionPane.showMessageDialog(null, "전체의 학생의 학번 혹은 전공/부전공 중 하나의 값만 동시에 변경할 수는 없습니다.", "", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                if (update_student_no_check.isSelected()) {
                    if (update_student_no.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "학번을 입력하거나 체크를 해제해주세요", "", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (!update_student_no.getText().matches(regExp)) {
                        JOptionPane.showMessageDialog(null, "학번은 숫자입니다.", "", JOptionPane.ERROR_MESSAGE);
                        update_student_no.requestFocus();
                        return; // 학번이 숫자가 아닐 때
                    }
                    if (selectStudentNo().contains(" " + update_student_no.getText() + " ") == true) {
                        JOptionPane.showMessageDialog(null, "이미 존재하는 학번입니다.", "", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    query += update_student_no_check.getText() + " = " + update_student_no.getText() + ", ";
                }
                if (update_student_name_check.isSelected()) {
                    if (update_student_name.getText().trim().equals("")) {
                        JOptionPane.showMessageDialog(null, "이름을 입력하거나 체크를 해제해주세요", "", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    query += update_student_name_check.getText() + " = '" + update_student_name.getText() + "', ";
                }
                if (update_student_address_check.isSelected()) {
                    if (update_student_address.getText().trim().equals("")) {
                        JOptionPane.showMessageDialog(null, "주소를 입력하거나 체크를 해제해주세요", "", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    query += update_student_address_check.getText() + " = '" + update_student_address.getText() + "', ";
                }
                if (update_student_phone_check.isSelected()) {
                    if (update_student_phone.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "핸드폰번호를 입력하거나 체크를 해제해주세요", "", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (!update_student_phone.getText().matches("(01[016789])-(\\d{3,4})-(\\d{4})")){
                        JOptionPane.showMessageDialog(null, "전화번호 형식이 올바르지 않습니다.", "", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    query += update_student_phone_check.getText() + " = '" + update_student_phone.getText() + "', ";
                }
                if (update_student_email_check.isSelected()) {
                    if (update_student_email.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "이메일을 입력하거나 체크를 해제해주세요", "", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (isValidEmail(update_student_email.getText()) == false) {
                        JOptionPane.showMessageDialog(null, "이메일 형식이 올바르지 않습니다.", "", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    query += update_student_email_check.getText() + " = '" + update_student_email.getText() + "', ";
                }
                if (update_student_account_check.isSelected()) {
                    if (update_student_account.getText().trim().equals("")) {
                        JOptionPane.showMessageDialog(null, "계좌를 입력하거나 체크를 해제해주세요", "", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    query += update_student_account_check.getText() + " = '" + update_student_account.getText() + "', ";
                }
                if (update_major_no_check.isSelected()) {
                    if (update_major_no.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "전공을 입력하거나 체크를 해제해주세요", "", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (selectDepartmentNo().contains(" " + update_major_no.getText() + " ") == false) {
                        JOptionPane.showMessageDialog(null, "전공 학과가 존재하지 않습니다.", "", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    query += update_major_no_check.getText() + " = " + update_major_no.getText() + ", ";
                }
                if (update_minor_no_check.isSelected()) {
                    if (!update_minor_no.getText().equals("") && selectDepartmentNo().contains(" " + update_minor_no.getText() + " ") == false) {
                        JOptionPane.showMessageDialog(null, "부전공 학과가 존재하지 않습니다.", "", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    query += update_minor_no_check.getText() + " = " + update_minor_no.getText() + ", ";
                }
                // 부전공 또는 전공 하나만 변경할 경우 where로 선택되는 student는 한명이어야 전공-부전공 비교가 가능하다. 부전공-전공 모두 변경할 경우 상관x
                if (update_major_no_check.isSelected() && update_minor_no_check.isSelected()) {
                    if (update_major_no.getText().equals(update_minor_no.getText())) {
                        JOptionPane.showMessageDialog(null, "전공과 다른 부전공을 입력해주세요.", "", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else if ((!update_major_no_check.isSelected() && update_minor_no_check.isSelected()) || (update_major_no_check.isSelected() && !update_minor_no_check.isSelected())) {
                    try {   // 조건식에 해당하는 student가 한명인지 확인
                        String query1 = "SELECT COUNT(student_no) FROM student WHERE " + update_where.getText();
                        stmt = con.createStatement();
                        rs = stmt.executeQuery(query1);
                        rs.next();
                        if (rs.getInt(1) != 0 && rs.getInt(1) != 1) { //0은 무시되서 가능
                            JOptionPane.showMessageDialog(null, "부전공 또는 전공 하나만 변경할 경우 조건식에 해당하는 student는 한 명이어야합니다.", "", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    try {    // 현재 전공, 부전공 알아오기
                        String query1 = "SELECT major_no, minor_no FROM student WHERE " + update_where.getText();
                        stmt = con.createStatement();
                        rs = stmt.executeQuery(query1);
                        rs.next();
                        if (update_major_no_check.isSelected()) {   // 전공이 바뀌면 부전공과 비교
                            if (!(rs.getString(2) == null || rs.getString(2).length() == 0) && rs.getString(2).equals(update_major_no.getText())) {
                                JOptionPane.showMessageDialog(null, "기존 부전공과 같은 전공입니다.", "", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        } else if (update_minor_no_check.isSelected()) {    // 부전공이 바뀐다면 전공과 비교
                            if (rs.getString(1).equals(update_minor_no.getText())) {
                                JOptionPane.showMessageDialog(null, "기존 전공과 같은 부전공입니다.", "", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                query = query.substring(0, query.length() - 2);
                if (!update_where.getText().equals("")) {
                    query += " WHERE " + update_where.getText();
                }
                int result = JOptionPane.showConfirmDialog(null, "수정 하시겠습니까?", "", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    if (updateSet(query) == true) {
                        JOptionPane.showMessageDialog(null, "수정 완료", "", JOptionPane.PLAIN_MESSAGE);
                        ((AbstractButton) pnBtn.getComponent(4)).doClick();
                    } else {
                        JOptionPane.showMessageDialog(null, "수정 실패 :" + e, "", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        pnUpdateGrid.add(update_student_no_check);
        pnUpdateGrid.add(update_student_no);
        pnUpdateGrid.add(update_student_name_check);
        pnUpdateGrid.add(update_student_name);
        pnUpdateGrid.add(update_student_address_check);
        pnUpdateGrid.add(update_student_address);
        pnUpdateGrid.add(update_student_phone_check);
        pnUpdateGrid.add(update_student_phone);
        pnUpdateGrid.add(update_student_email_check);
        pnUpdateGrid.add(update_student_email);
        pnUpdateGrid.add(update_student_account_check);
        pnUpdateGrid.add(update_student_account);
        pnUpdateGrid.add(update_major_no_check);
        pnUpdateGrid.add(update_major_no);
        pnUpdateGrid.add(update_minor_no_check);
        pnUpdateGrid.add(update_minor_no);
        pnUpdateGrid.add(new JLabel("WHERE "));
        pnUpdateGrid.add(update_where);
        pnUpdateGrid.add(new JLabel(""));
        pnUpdateGrid.add(btnUpdate);
        pnUpdate.add("North", new JLabel("UPDATE student SET"));
        pnUpdate.add("Center", pnUpdateGrid);

        /* 삭제 */
        JTextField delete_where = new JTextField();
        JButton btnDelete = new JButton("삭제");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "삭제 하시겠습니까?", "", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    if (deleteFrom("DELETE FROM student WHERE " + delete_where.getText()) == true) {
                        JOptionPane.showMessageDialog(null, "삭제 완료", "", JOptionPane.PLAIN_MESSAGE);
                        ((AbstractButton) pnBtn.getComponent(4)).doClick();
                    } else {
                        JOptionPane.showMessageDialog(null, "삭제 실패 :" + e, "", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        pnDelete.add("North", new JLabel("DELETE FROM student WHERE"));
        pnDelete.add("Center", delete_where);
        pnDelete.add("South", btnDelete);

        pnContent.add(pnInsert);
        pnContent.add(pnUpdate);
        pnContent.add(pnDelete);
        pnCenter.add("Center", pnContent);
        return pnCenter;
    }

    public JPanel adminAdministrationTuition(JPanel pnHeader) {
        c.remove(pnCenter);
        pnCenter.removeAll();
        pnCenter.add("North", pnHeader);
        pnCenter.add("South", showTableTuition());
        JPanel pnBtn = (JPanel) pnHeader.getComponent(1);

        JPanel pnContent = new JPanel();
        pnContent.setLayout(new GridLayout(1, 3, 5, 5));
        JPanel pnInsert = new JPanel();
        pnInsert.setLayout(new BorderLayout());
        JPanel pnUpdate = new JPanel();
        pnUpdate.setLayout(new BorderLayout());
        JPanel pnDelete = new JPanel();
        pnDelete.setLayout(new BorderLayout());

        /* 입력 */
        JPanel pnInsertGrid = new JPanel();
        pnInsertGrid.setLayout(new GridLayout(8, 2, 5, 5));
        JTextField insert_student_no = new JTextField();
        JTextField insert_tuition_year = new JTextField();
        JTextField insert_tuition_semester = new JTextField();
        JTextField insert_tuition_fee = new JTextField();
        JTextField insert_tuition_payment = new JTextField();
        JTextField insert_last_payment_date = new JTextField();
        JTextField insert_grade_semester = new JTextField();
        JButton btnInsert = new JButton("입력");
        btnInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (!insert_tuition_fee.getText().matches(regExp)) {
                    JOptionPane.showMessageDialog(null, "고지금액을 확인해주세요", "", JOptionPane.ERROR_MESSAGE);
                    insert_tuition_fee.requestFocus();
                    return; // 고지금액이 숫자가 아닐 때
                }
                if (!insert_tuition_payment.getText().matches(regExp)) {
                    JOptionPane.showMessageDialog(null, "납부금액을 확인해주세요", "", JOptionPane.ERROR_MESSAGE);
                    insert_tuition_fee.requestFocus();
                    return; // 납부금액이 숫자가 아닐 때
                }
                if (selectStudentNo().contains(" " + insert_student_no.getText() + " ") == false) {
                    JOptionPane.showMessageDialog(null, "존재하지 않는 학번입니다.", "", JOptionPane.ERROR_MESSAGE);
                    return; // 존재하지 않는 학번일 때
                }
                if (didEnroll(insert_student_no.getText(), insert_tuition_year.getText(),
                        insert_tuition_semester.getText()) == true) {
                    JOptionPane.showMessageDialog(null, "이미 등록된 학생입니다.", "", JOptionPane.ERROR_MESSAGE);
                    return; // 이미 등록된 학생일 때
                }
                if (insert_tuition_payment.getText().equals("0") != insert_last_payment_date.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "납부금액 혹은 납부일자를 확인 해주세요", "", JOptionPane.ERROR_MESSAGE);
                    return; // 납부한 금액은 없는데 납부 날짜가 있을 때, 등록금을 납부했는데 납부한 날짜가 없을 때
                }
                if (!insert_last_payment_date.getText().equals("")
                        && isValidDate(insert_last_payment_date.getText()) == false) {
                    JOptionPane.showMessageDialog(null, "납부일자를 확인해주세요", "", JOptionPane.ERROR_MESSAGE);
                    return; // null이 아닌 날짜 형식이 유효하지 않을 때
                }
                if (isValidGradeSemester(insert_tuition_semester.getText()) == false
                        || selectLastGradeSemester(insert_student_no.getText(),
                        insert_tuition_semester.getText()) == false) {
                    JOptionPane.showMessageDialog(null, "학년/학기를 확인해주세요.", "", JOptionPane.ERROR_MESSAGE);
                    return; // 학년/학기 형식이 유효하지 않을 때, 마지막 등록한 학년/학기의 다음 학년/학기가 아닐 때
                }
                //TODO 등록이 안된 2020학년-2학기인데 2021년-1학기는 등록이 되어잇음

                try { // 날짜 형식 맞춰서 입력
                    insert_last_payment_date
                            .setText(dateFormat.format(dateFormat.parse(insert_last_payment_date.getText())));
                } catch (ParseException e2) {
                    JOptionPane.showMessageDialog(null, "납부일자를 확인해주세요", "", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int result = JOptionPane.showConfirmDialog(null, "실행 하시겠습니까?", "", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {

                    try {
                        stmt = con.createStatement();
                        String query = String.format("INSERT INTO tuition VALUES(%s, %s, %s, %s, %s, '%s', '%s')",
                                insert_student_no.getText(), insert_tuition_year.getText(),
                                insert_tuition_semester.getText(), insert_tuition_fee.getText(),
                                insert_tuition_payment.getText(), insert_last_payment_date.getText(),
                                insert_grade_semester.getText());
                        System.out.println(query);
                        stmt.execute(query);
                        JOptionPane.showMessageDialog(null, "실행이 정상적으로 종료하였습니다.", "", JOptionPane.PLAIN_MESSAGE);
                        JPanel pnBtn = (JPanel) pnHeader.getComponent(1);
                        ((AbstractButton) pnBtn.getComponent(5)).doClick();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "실패 : " + e, "", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });

        pnInsertGrid.add(new JLabel("student_no"));
        pnInsertGrid.add(insert_student_no);
        pnInsertGrid.add(new JLabel("tuition_year"));
        pnInsertGrid.add(insert_tuition_year);
        pnInsertGrid.add(new JLabel("tuition_semester"));
        pnInsertGrid.add(insert_tuition_semester);
        pnInsertGrid.add(new JLabel("tuition_fee"));
        pnInsertGrid.add(insert_tuition_fee);
        pnInsertGrid.add(new JLabel("tuition_payment"));
        pnInsertGrid.add(insert_tuition_payment);
        pnInsertGrid.add(new JLabel("last_payment_date"));
        pnInsertGrid.add(insert_last_payment_date);
        pnInsertGrid.add(new JLabel("grade_semester"));
        pnInsertGrid.add(insert_grade_semester);
        pnInsertGrid.add(new JLabel(""));
        pnInsertGrid.add(btnInsert);
        pnInsert.add("North", new JLabel("INSERT INTO tuition VALUES"));
        pnInsert.add("Center", pnInsertGrid);

        /* 수정 */
        JPanel pnUpdateGrid = new JPanel();
        pnUpdateGrid.setLayout(new GridLayout(9, 2, 5, 5));

        JCheckBox update_student_no_check = new JCheckBox("student_no");
        JCheckBox update_tuition_year_check = new JCheckBox("tuition_year");
        JCheckBox update_tuition_semester_check = new JCheckBox("tuition_semester");
        JCheckBox update_tuition_fee_check = new JCheckBox("tuition_fee");
        JCheckBox update_tuition_payment_check = new JCheckBox("tuition_payment");
        JCheckBox update_last_payment_date_check = new JCheckBox("last_payment_date");
        JCheckBox update_grade_semester_check = new JCheckBox("grade_semester");

        JTextField update_student_no = new JTextField();
        JTextField update_tuition_year = new JTextField();
        JTextField update_tuition_semester = new JTextField();
        JTextField update_tuition_fee = new JTextField();
        JTextField update_tuition_payment = new JTextField();
        JTextField update_last_payment_date = new JTextField();
        JTextField update_grade_semester = new JTextField();
        JTextField update_where = new JTextField();
        JButton btnUpdate = new JButton("수정");
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String query = "UPDATE tuition SET ";
                if (update_student_no_check.isSelected()) {
                    query += update_student_no_check.getText() + " = " + update_student_no.getText() + ", ";
                }
                if (update_tuition_year_check.isSelected()) {
                    query += update_tuition_year_check.getText() + " = " + update_tuition_year.getText() + ", ";
                }
                if (update_tuition_semester_check.isSelected()) {
                    query += update_tuition_semester_check.getText() + " = " + update_tuition_semester.getText() + ", ";
                }
                if (update_tuition_fee_check.isSelected()) {
                    query += update_tuition_fee_check.getText() + " = " + update_tuition_fee.getText() + ", ";
                }
                if (update_tuition_payment_check.isSelected()) {
                    query += update_tuition_payment_check.getText() + " = " + update_tuition_payment.getText() + ", ";
                }
                if (update_last_payment_date_check.isSelected()) {
                    query += update_last_payment_date_check.getText() + " = '" + update_last_payment_date.getText()
                            + "', ";
                }
                if (update_grade_semester_check.isSelected()) {
                    query += update_grade_semester_check.getText() + " = '" + update_grade_semester.getText() + "', ";
                }
                query = query.substring(0, query.length() - 2);
                if (!update_where.getText().equals("")) {
                    query += " WHERE " + update_where.getText();
                }
                updateSet(query);
                ((AbstractButton) pnBtn.getComponent(5)).doClick();

            }
        });

        pnUpdateGrid.add(update_student_no_check);
        pnUpdateGrid.add(update_student_no);
        pnUpdateGrid.add(update_tuition_year_check);
        pnUpdateGrid.add(update_tuition_year);
        pnUpdateGrid.add(update_tuition_semester_check);
        pnUpdateGrid.add(update_tuition_semester);
        pnUpdateGrid.add(update_tuition_fee_check);
        pnUpdateGrid.add(update_tuition_fee);
        pnUpdateGrid.add(update_tuition_payment_check);
        pnUpdateGrid.add(update_tuition_payment);
        pnUpdateGrid.add(update_last_payment_date_check);
        pnUpdateGrid.add(update_last_payment_date);
        pnUpdateGrid.add(update_grade_semester_check);
        pnUpdateGrid.add(update_grade_semester);
        pnUpdateGrid.add(new JLabel("WHERE "));
        pnUpdateGrid.add(update_where);
        pnUpdateGrid.add(new JLabel(""));
        pnUpdateGrid.add(btnUpdate);
        pnUpdate.add("North", new JLabel("UPDATE tuition SET"));
        pnUpdate.add("Center", pnUpdateGrid);

        /* 삭제 */
        JTextField delete_where = new JTextField();
        JButton btnDelete = new JButton("삭제");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "삭제 하시겠습니까?", "", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    if (deleteFrom("DELETE FROM tuition WHERE " + delete_where.getText()) == true) {
                        JOptionPane.showMessageDialog(null, "삭제 완료", "", JOptionPane.PLAIN_MESSAGE);
                        ((AbstractButton) pnBtn.getComponent(5)).doClick();
                    } else {
                        JOptionPane.showMessageDialog(null, "삭제 실패 :" + e, "", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        pnDelete.add("North", new JLabel("DELETE FROM tuition WHERE"));
        pnDelete.add("Center", delete_where);
        pnDelete.add("South", btnDelete);

        pnContent.add(pnInsert);
        pnContent.add(pnUpdate);
        pnContent.add(pnDelete);
        pnCenter.add("Center", pnContent);
        return pnCenter;
    }

    public JPanel adminAdministrationTutoring(JPanel pnHeader) {

        c.remove(pnCenter);
        pnCenter.removeAll();
        pnCenter.add("North", pnHeader);
        pnCenter.add("South", showTableTutoring());
        JPanel pnBtn = (JPanel) pnHeader.getComponent(1);

        JPanel pnContent = new JPanel();
        pnContent.setLayout(new GridLayout(1, 3, 5, 5));
        JPanel pnInsert = new JPanel();
        pnInsert.setLayout(new BorderLayout());
        JPanel pnUpdate = new JPanel();
        pnUpdate.setLayout(new BorderLayout());
        JPanel pnDelete = new JPanel();
        pnDelete.setLayout(new BorderLayout());

        /* 입력 */
        JLabel banInsert = new JLabel("입학처리 완료(등록금 완납시) 후 자동으로 등록 됩니다.");
        banInsert.setHorizontalAlignment(JLabel.CENTER);
        pnInsert.add("Center", banInsert);

//		JPanel pnInsertGrid = new JPanel();
//		pnInsertGrid.setLayout(new GridLayout(4, 2, 5, 5));
//		JTextField insert_student_no = new JTextField();
//		JTextField insert_professor_no = new JTextField();
//		JTextField insert_grade_semester = new JTextField();
//		JButton btnInsert = new JButton("입력");
//		btnInsert.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//			}
//		});
//
//		pnInsertGrid.add(new JLabel("student_no"));
//		pnInsertGrid.add(insert_student_no);
//		pnInsertGrid.add(new JLabel("professor_no"));
//		pnInsertGrid.add(insert_professor_no);
//		pnInsertGrid.add(new JLabel("grade_semester"));
//		pnInsertGrid.add(insert_grade_semester);
//		pnInsertGrid.add(new JLabel(""));
//		pnInsertGrid.add(btnInsert);
//		pnInsert.add("North", new JLabel("INSERT INTO tutoring VALUES"));
//		pnInsert.add("Center", pnInsertGrid);

        /* 수정 */
        JPanel pnUpdateGrid = new JPanel();
        pnUpdateGrid.setLayout(new GridLayout(4, 2, 5, 5));
        JLabel update_professor_no_check = new JLabel("professor_no");
        JLabel update_grade_semester_check = new JLabel("grade_semester");
        JLabel update_student_no_check = new JLabel("WHERE student_no = ");

        JTextField update_professor_no = new JTextField();
        JTextField update_grade_semester = new JTextField();
        JTextField update_student_no = new JTextField();
        JButton btnUpdate = new JButton("수정");
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (update_professor_no.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "직번을 입력해주세요", "", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (selectProfessorNo().contains(" " + update_professor_no.getText() + " ") == false) {
                    JOptionPane.showMessageDialog(null, "존재하지 않는 직번입니다.", "", JOptionPane.ERROR_MESSAGE);
                    return; // 존재하지 않는 직번일 때
                }
                if (update_grade_semester.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "학년-학기를 입력해주세요", "", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (isValidGradeSemester(update_grade_semester.getText()) == false || isNextGradeSemesters(update_student_no.getText(), update_grade_semester.getText()) == true) {
                    JOptionPane.showMessageDialog(null, "학년-학기를 확인해주세요", "", JOptionPane.ERROR_MESSAGE);
                    return;    // 학년-학기 형식이 유효하지 않거나 마지막 등록된 학년-학기보다 높은 경우
                }
                if (update_student_no.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "학번을 입력해주세요", "", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (selectStudentNo().contains(" " + update_student_no.getText() + " ") == false) {
                    JOptionPane.showMessageDialog(null, "존재하지 않는 학번입니다.", "", JOptionPane.ERROR_MESSAGE);
                    return; // 존재하지 않는 학번일 때
                }


                String query = "UPDATE tutoring SET professor_no = " + update_professor_no.getText() + ", grade_semester = '" + update_grade_semester.getText() + "' WHERE student_no = " + update_student_no.getText();
                updateSet(query);
                ((AbstractButton) pnBtn.getComponent(6)).doClick();
            }
        });

        pnUpdateGrid.add(update_professor_no_check);
        pnUpdateGrid.add(update_professor_no);
        pnUpdateGrid.add(update_grade_semester_check);
        pnUpdateGrid.add(update_grade_semester);
        pnUpdateGrid.add(update_student_no_check);
        pnUpdateGrid.add(update_student_no);
        pnUpdateGrid.add(new JLabel(""));
        pnUpdateGrid.add(btnUpdate);
        pnUpdate.add("North", new JLabel("UPDATE tutoring SET"));
        pnUpdate.add("Center", pnUpdateGrid);

        /* 삭제 */
        JLabel banDelete = new JLabel("학생 삭제시 자동으로 삭제됩니다.");
        banDelete.setHorizontalAlignment(JLabel.CENTER);
        pnDelete.add("Center", banDelete);
//		JTextField delete_where = new JTextField();
//		JButton btnDelete = new JButton("삭제");
//		btnDelete.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				int result = JOptionPane.showConfirmDialog(null, "삭제 하시겠습니까?", "", JOptionPane.OK_CANCEL_OPTION);
//				if (result == JOptionPane.OK_OPTION) {
//					if (deleteFrom("DELETE FROM club WHERE " + delete_where.getText()) == true) {
//						JOptionPane.showMessageDialog(null, "삭제 완료", "", JOptionPane.PLAIN_MESSAGE);
//						((AbstractButton) pnBtn.getComponent(6)).doClick();
//					} else {
//						JOptionPane.showMessageDialog(null, "삭제 실패 :" + e, "", JOptionPane.ERROR_MESSAGE);
//					}
//				}
//			}
//		});
//
//		pnDelete.add("North", new JLabel("DELETE FROM tutoring WHERE"));
//		pnDelete.add("Center", delete_where);
//		pnDelete.add("South", btnDelete);

        pnContent.add(pnInsert);
        pnContent.add(pnUpdate);
        pnContent.add(pnDelete);
        pnCenter.add("Center", pnContent);
        return pnCenter;

    }

    //	public JPanel adminAdministrationCourse(JPanel pnHeader) { }
    public JPanel adminAdministrationClub(JPanel pnHeader) {
        c.remove(pnCenter);
        pnCenter.removeAll();
        pnCenter.add("North", pnHeader);
        pnCenter.add("South", showTableClub());
        JPanel pnBtn = (JPanel) pnHeader.getComponent(1);

        JPanel pnContent = new JPanel();
        pnContent.setLayout(new GridLayout(1, 3, 5, 5));
        JPanel pnInsert = new JPanel();
        pnInsert.setLayout(new BorderLayout());
        JPanel pnUpdate = new JPanel();
        pnUpdate.setLayout(new BorderLayout());
        JPanel pnDelete = new JPanel();
        pnDelete.setLayout(new BorderLayout());

        JPanel pnInsertGrid = new JPanel();
        pnInsertGrid.setLayout(new GridLayout(7, 2, 5, 5));
        JTextField insert_club_no = new JTextField();
        JTextField insert_club_name = new JTextField();
        JTextField insert_club_total_member = new JTextField();
        insert_club_total_member.setText("1");
        insert_club_total_member.setEditable(false);
        JTextField insert_club_room = new JTextField();
        JTextField insert_professor_no = new JTextField();
        JTextField insert_student_no = new JTextField();
        JButton btnInsert = new JButton("입력");
        btnInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectClubNo().contains(" " + insert_club_no.getText() + " ") == true) {
                    JOptionPane.showMessageDialog(null, "이미 존재하는 동아리입니다.", "", JOptionPane.ERROR_MESSAGE);
                    return; // 존재하지 않는 동아리 번호일 때
                }
                if (insert_club_name.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "동아리명을 입력해주세요.", "", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (selectProfessorNo().contains(" " + insert_professor_no.getText() + " ") == false) {
                    JOptionPane.showMessageDialog(null, "존재하지 않는 직번입니다.", "", JOptionPane.ERROR_MESSAGE);
                    return; // 존재하지 않는 직번일 때
                }
                if (selectStudentNo().contains(" " + insert_student_no.getText() + " ") == false) {
                    JOptionPane.showMessageDialog(null, "존재하지 않는 학번입니다.", "", JOptionPane.ERROR_MESSAGE);
                    return; // 존재하지 않는 학번일 때
                }

                int result = JOptionPane.showConfirmDialog(null, "실행 하시겠습니까?", "", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        stmt = con.createStatement();
                        String query = String.format("INSERT INTO club VALUES(%s, '%s', %s, '%s', %s, %s)",
                                insert_club_no.getText(), insert_club_name.getText(),
                                insert_club_total_member.getText(), insert_club_room.getText(),
                                insert_professor_no.getText(), insert_student_no.getText());
                        System.out.println(query);
                        stmt.execute(query);
                    } catch (SQLException ex) {
                        System.out.println("새로운 동아리 추가 실패 :" + e);
                    }
                    try {
                        /* 동아리 회장은 자동으로 club_join에 추가 */
                        stmt = con.createStatement();
                        String query = String.format("INSERT INTO club_join VALUES(%s, '%s')", insert_student_no.getText(), insert_club_no.getText());
                        System.out.println(query);
                        stmt.execute(query);
                        JOptionPane.showMessageDialog(null, "실행이 정상적으로 종료하였습니다.", "", JOptionPane.PLAIN_MESSAGE);
                        ((AbstractButton) pnBtn.getComponent(8)).doClick();
                    } catch (SQLException ex) {
                        System.out.println("동아리 회장 추가 실패 :" + e);
                    }
                }
            }
        });

        pnInsertGrid.add(new JLabel("club_no"));
        pnInsertGrid.add(insert_club_no);
        pnInsertGrid.add(new JLabel("club_name"));
        pnInsertGrid.add(insert_club_name);
        pnInsertGrid.add(new JLabel("club_total_member"));
        pnInsertGrid.add(insert_club_total_member);
        pnInsertGrid.add(new JLabel("club_room"));
        pnInsertGrid.add(insert_club_room);
        pnInsertGrid.add(new JLabel("professor_no"));
        pnInsertGrid.add(insert_professor_no);
        pnInsertGrid.add(new JLabel("student_no"));
        pnInsertGrid.add(insert_student_no);
        pnInsertGrid.add(new JLabel(""));
        pnInsertGrid.add(btnInsert);
        pnInsert.add("North", new JLabel("INSERT INTO club VALUES"));
        pnInsert.add("Center", pnInsertGrid);

        /* 수정 */
        JPanel pnUpdateGrid = new JPanel();
        pnUpdateGrid.setLayout(new GridLayout(8, 2, 5, 5));
        JCheckBox update_club_no_check = new JCheckBox("club_no");
        JCheckBox update_club_name_check = new JCheckBox("club_name");
        JLabel update_club_total_member_check = new JLabel("club_total_member");
        JCheckBox update_club_room_check = new JCheckBox("club_room");
        JCheckBox update_professor_no_check = new JCheckBox("professor_no");
        JCheckBox update_student_no_check = new JCheckBox("student_no");
        JLabel update_where_check = new JLabel("WHERE ");

        JTextField update_club_no = new JTextField();
        JTextField update_club_name = new JTextField();
        JTextField update_club_total_member = new JTextField();
        update_club_total_member.setText("club_join 입력/삭제시 자동으로 수정됩니다.");
        update_club_total_member.setHorizontalAlignment(JTextField.CENTER);
        update_club_total_member.setEditable(false);
        JTextField update_club_room = new JTextField();
        JTextField update_professor_no = new JTextField();
        JTextField update_student_no = new JTextField();
        JTextField update_where = new JTextField();
        JButton btnUpdate = new JButton("수정");
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String query = "UPDATE club SET ";
                if (update_club_no_check.isSelected()) {
                    query += update_club_no_check.getText() + " = " + update_club_no.getText() + ", ";
                    if (selectClubNo().contains(" " + update_club_no.getText() + " ") == true) {
                        JOptionPane.showMessageDialog(null, "이미 존재하는 동아리입니다.", "", JOptionPane.ERROR_MESSAGE);
                        return; // 존재하지 않는 동아리 번호일 때
                    }
                }
                if (update_club_name_check.isSelected()) {
                    query += update_club_name_check.getText() + " = '" + update_club_name.getText() + "', ";
                }
                if (update_club_room_check.isSelected()) {
                    query += update_club_room_check.getText() + " = '" + update_club_room.getText() + "', ";
                }
                if (update_professor_no_check.isSelected()) {
                    query += update_professor_no_check.getText() + " = " + update_professor_no.getText() + ", ";
                    if (selectProfessorNo().contains(" " + update_professor_no.getText() + " ") == false) {
                        JOptionPane.showMessageDialog(null, "존재하지 않는 직번입니다.", "", JOptionPane.ERROR_MESSAGE);
                        return; // 존재하지 않는 직번일 때
                    }
                }
                if (update_student_no_check.isSelected()) {
                    query += update_student_no_check.getText() + " = " + update_student_no.getText() + ", ";
                    if (selectStudentNo().contains(" " + update_student_no.getText() + " ") == false) {
                        JOptionPane.showMessageDialog(null, "존재하지 않는 학번입니다.", "", JOptionPane.ERROR_MESSAGE);
                        return; // 존재하지 않는 학번일 때
                    }
                }

                query = query.substring(0, query.length() - 2);
                if (!update_where.getText().equals("")) {
                    query += " WHERE " + update_where.getText();
                }
                updateSet(query);
                ((AbstractButton) pnBtn.getComponent(8)).doClick();
            }
        });

        pnUpdateGrid.add(update_club_no_check);
        pnUpdateGrid.add(update_club_no);
        pnUpdateGrid.add(update_club_name_check);
        pnUpdateGrid.add(update_club_name);
        pnUpdateGrid.add(update_club_total_member_check);
        pnUpdateGrid.add(update_club_total_member);
        pnUpdateGrid.add(update_club_room_check);
        pnUpdateGrid.add(update_club_room);
        pnUpdateGrid.add(update_professor_no_check);
        pnUpdateGrid.add(update_professor_no);
        pnUpdateGrid.add(update_student_no_check);
        pnUpdateGrid.add(update_student_no);
        pnUpdateGrid.add(update_where_check);
        pnUpdateGrid.add(update_where);
        pnUpdateGrid.add(new JLabel(""));
        pnUpdateGrid.add(btnUpdate);
        pnUpdate.add("North", new JLabel("UPDATE club SET"));
        pnUpdate.add("Center", pnUpdateGrid);

        /* 삭제 */
        JTextField delete_where = new JTextField();
        JButton btnDelete = new JButton("삭제");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "삭제 하시겠습니까?", "", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    if (deleteFrom("DELETE FROM club WHERE " + delete_where.getText()) == true) {
                        JOptionPane.showMessageDialog(null, "삭제 완료", "", JOptionPane.PLAIN_MESSAGE);
                        ((AbstractButton) pnBtn.getComponent(8)).doClick();
                    } else {
                        JOptionPane.showMessageDialog(null, "삭제 실패 :" + e, "", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        pnDelete.add("North", new JLabel("DELETE FROM club WHERE"));
        pnDelete.add("Center", delete_where);
        pnDelete.add("South", btnDelete);

        pnContent.add(pnInsert);
        pnContent.add(pnUpdate);
        pnContent.add(pnDelete);
        pnCenter.add("Center", pnContent);
        return pnCenter;

    }

    public JPanel adminAdministrationClubJoin(JPanel pnHeader) {
        c.remove(pnCenter);
        pnCenter.removeAll();
        pnCenter.add("North", pnHeader);
        pnCenter.add("South", showTableClubJoin());
        JPanel pnBtn = (JPanel) pnHeader.getComponent(1);

        JPanel pnContent = new JPanel();
        pnContent.setLayout(new GridLayout(1, 3, 5, 5));
        JPanel pnInsert = new JPanel();
        pnInsert.setLayout(new BorderLayout());
        JPanel pnUpdate = new JPanel();
        pnUpdate.setLayout(new BorderLayout());
        JPanel pnDelete = new JPanel();
        pnDelete.setLayout(new BorderLayout());

        JPanel pnInsertGrid = new JPanel();
        pnInsertGrid.setLayout(new GridLayout(3, 2, 5, 5));
        JTextField insert_club_no = new JTextField();
        JTextField insert_student_no = new JTextField();
        JButton btnInsert = new JButton("입력");
        btnInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (selectClubNo().contains(" " + insert_club_no.getText() + " ") == false) {
                    JOptionPane.showMessageDialog(null, "존재하지 않는 동아리입니다.", "", JOptionPane.ERROR_MESSAGE);
                    return; // 존재하지 않는 동아리일 때
                }
                if (selectStudentNo().contains(" " + insert_student_no.getText() + " ") == false) {
                    JOptionPane.showMessageDialog(null, "존재하지 않는 학번입니다.", "", JOptionPane.ERROR_MESSAGE);
                    return; // 존재하지 않는 학번일 때
                }
                if (isJoiningClub(insert_student_no.getText(), insert_club_no.getText()) == true) {
                    JOptionPane.showMessageDialog(null, "이미 가입된 동아리입니다.", "", JOptionPane.ERROR_MESSAGE);
                    return; // 이미 가입되어 있을 때
                }
                int result = JOptionPane.showConfirmDialog(null, "실행 하시겠습니까?", "", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        stmt = con.createStatement();
                        String query = String.format("INSERT INTO club_join VALUES(%s, %s)",
                                insert_student_no.getText(), insert_club_no.getText());
                        System.out.println(query);
                        stmt.execute(query);
                    } catch (SQLException ex) {
                        System.out.println("동아리 추가 실패 :" + e);
                    }
                    try {
                        stmt = con.createStatement();
                        String query = String.format(
                                "UPDATE club SET club_total_member = (SELECT COUNT(*) FROM club_join WHERE club_no = %s) WHERE club_no = %s;",
                                insert_club_no.getText(), insert_club_no.getText());
                        System.out.println(query);
                        stmt.execute(query);
                        JOptionPane.showMessageDialog(null, "실행이 정상적으로 종료하였습니다.", "", JOptionPane.PLAIN_MESSAGE);
                        ((AbstractButton) pnBtn.getComponent(9)).doClick();
                    } catch (SQLException ex) {
                        System.out.println("동아리원 수 업데이트 실패 :" + e);
                    }
                    // TODO total_member update!
                }
            }
        });

        pnInsertGrid.add(new JLabel("club_no"));
        pnInsertGrid.add(insert_club_no);
        pnInsertGrid.add(new JLabel("student_no"));
        pnInsertGrid.add(insert_student_no);
        pnInsertGrid.add(new JLabel(""));
        pnInsertGrid.add(btnInsert);
        pnInsert.add("North", new JLabel("INSERT INTO club_join VALUES"));
        pnInsert.add("Center", pnInsertGrid);

        /* 수정 */
        JPanel pnUpdateGrid = new JPanel();
        pnUpdateGrid.setLayout(new GridLayout(4, 2, 5, 5));
        JCheckBox update_club_no_check = new JCheckBox("club_no");
        JCheckBox update_student_no_check = new JCheckBox("student_no");

        JTextField update_club_no = new JTextField();
        JTextField update_student_no = new JTextField();
        JTextField update_where = new JTextField();
        JButton btnUpdate = new JButton("수정");
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String query = "UPDATE club_join SET ";
                if (update_club_no_check.isSelected()) {
                    query += update_club_no_check.getText() + " = " + update_club_no.getText() + ", ";
                }
                if (update_student_no_check.isSelected()) {
                    query += update_student_no_check.getText() + " = " + update_student_no.getText() + ", ";
                }
                query = query.substring(0, query.length() - 2);
                if (!update_where.getText().equals("")) {
                    query += " WHERE " + update_where.getText();
                }
                updateSet(query);
                ((AbstractButton) pnBtn.getComponent(9)).doClick();

                /* 동아리장이 동아리 명단에 없는 동아리 찾아서 수정 */
                Map<String, String> studentNO_clubNo = new HashMap<>();
                try {
                    stmt = con.createStatement();
                    rs = stmt.executeQuery(
                            "SELECT student_no, club_no FROM club WHERE student_no NOT IN (SELECT j.student_no FROM club_join j WHERE j.club_no = club_no)");
                    while (rs.next()) {
                        studentNO_clubNo.put(rs.getString(2), rs.getString(1));
                    }
                } catch (SQLException ex) {
                    System.out.println("동아리 명단 조회 실패 : " + ex);
                }
                if (!studentNO_clubNo.isEmpty()) {
                    studentNO_clubNo.forEach((key, value) -> {
                        try {
                            stmt = con.createStatement();
                            String str = String.format("INSERT INTO club_join VALUES(%s, %s)", value, key);
                            stmt.execute(str);
                        } catch (SQLException ex) {
                            System.out.println("동아리장 추가 실패 (" + value + "," + key + ") : " + ex);
                        }
                    });
                    JOptionPane.showMessageDialog(null, "동아리장은 삭제할 수 없습니다.", "", JOptionPane.ERROR_MESSAGE);
                    ((AbstractButton) pnBtn.getComponent(9)).doClick();
                    return;
                }
                /* TODO 동아리원 수 수정 */
                Map<String, String> updateMemberCount = new HashMap<>();
                try {
                    stmt = con.createStatement();
                    rs = stmt.executeQuery("SELECT c.club_no, t.total FROM club c\r\n"
                            + "LEFT JOIN (	SELECT club_no, COUNT(club_no) AS total FROM club_join GROUP BY club_no) t ON t.club_no = c.club_no\r\n"
                            + "WHERE t.total != c.club_total_member");
                    while (rs.next()) {
                        updateMemberCount.put(rs.getString(1), rs.getString(2));
                    }
                } catch (SQLException ex) {
                    System.out.println("동아리원 수 수정 실패 : " + ex);
                }
                if (!updateMemberCount.isEmpty()) {
                    updateMemberCount.forEach((key, value) -> {
                        try {
                            stmt = con.createStatement();
                            String str = String.format("UPDATE club SET club_total_member = %s WHERE club_no = %s", value, key);
                            stmt.execute(str);
                        } catch (SQLException ex) {
                            System.out.println("동아리원 수 수정 실패 (" + value + "," + key + ") : " + ex);
                        }
                    });
                }
                JOptionPane.showMessageDialog(null, "삭제가 완료되었습니다.", "", JOptionPane.PLAIN_MESSAGE);
                ((AbstractButton) pnBtn.getComponent(9)).doClick();

            }
        });

        pnUpdateGrid.add(update_club_no_check);
        pnUpdateGrid.add(update_club_no);
        pnUpdateGrid.add(update_student_no_check);
        pnUpdateGrid.add(update_student_no);
        pnUpdateGrid.add(new JLabel("WHERE "));
        pnUpdateGrid.add(update_where);
        pnUpdateGrid.add(new JLabel(""));
        pnUpdateGrid.add(btnUpdate);
        pnUpdate.add("North", new JLabel("UPDATE club_join SET"));
        pnUpdate.add("Center", pnUpdateGrid);

        /* 삭제 */
        JTextField delete_where = new JTextField();
        JButton btnDelete = new JButton("삭제");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "삭제 하시겠습니까?", "", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    deleteFrom("DELETE FROM club_join WHERE " + delete_where.getText());

                    /* 동아리장이 동아리 명단에 없는 동아리 찾아서 수정 */
                    Map<String, String> studentNO_clubNo = new HashMap<>();
                    try {
                        stmt = con.createStatement();
                        rs = stmt.executeQuery(
                                "SELECT student_no, club_no FROM club WHERE student_no NOT IN (SELECT j.student_no FROM club_join j WHERE j.club_no = club_no)");
                        while (rs.next()) {
                            studentNO_clubNo.put(rs.getString(2), rs.getString(1));
                        }
                    } catch (SQLException ex) {
                        System.out.println("동아리 명단 조회 실패 : " + ex);
                    }
                    if (!studentNO_clubNo.isEmpty()) {
                        studentNO_clubNo.forEach((key, value) -> {
                            try {
                                stmt = con.createStatement();
                                String str = String.format("INSERT INTO club_join VALUES(%s, %s)", value, key);
                                stmt.execute(str);
                            } catch (SQLException ex) {
                                System.out.println("동아리장 추가 실패 (" + value + "," + key + ") : " + ex);
                            }
                        });
                        JOptionPane.showMessageDialog(null, "동아리장은 삭제할 수 없습니다.", "", JOptionPane.ERROR_MESSAGE);
                        ((AbstractButton) pnBtn.getComponent(9)).doClick();
                        return;
                    }
                    /* TODO 동아리원 수 수정 */
                    Map<String, String> updateMemberCount = new HashMap<>();
                    try {
                        stmt = con.createStatement();
                        rs = stmt.executeQuery("SELECT c.club_no, t.total FROM club c\r\n"
                                + "LEFT JOIN (	SELECT club_no, COUNT(club_no) AS total FROM club_join GROUP BY club_no) t ON t.club_no = c.club_no\r\n"
                                + "WHERE t.total != c.club_total_member");
                        while (rs.next()) {
                            updateMemberCount.put(rs.getString(1), rs.getString(2));
                        }
                    } catch (SQLException ex) {
                        System.out.println("동아리원 수 수정 실패 : " + ex);
                    }
                    if (!updateMemberCount.isEmpty()) {
                        updateMemberCount.forEach((key, value) -> {
                            try {
                                stmt = con.createStatement();
                                String str = String.format("UPDATE club SET club_total_member = %s WHERE club_no = %s", value, key);
                                stmt.execute(str);
                            } catch (SQLException ex) {
                                System.out.println("동아리원 수 수정 실패 (" + value + "," + key + ") : " + ex);
                            }
                        });
                    }
                    JOptionPane.showMessageDialog(null, "삭제가 완료되었습니다.", "", JOptionPane.PLAIN_MESSAGE);
                    ((AbstractButton) pnBtn.getComponent(9)).doClick();
                }
            }
        });

        pnDelete.add("North", new JLabel("DELETE FROM club_join WHERE"));
        pnDelete.add("Center", delete_where);
        pnDelete.add("South", btnDelete);

        pnContent.add(pnInsert);
        pnContent.add(pnUpdate);
        pnContent.add(pnDelete);
        pnCenter.add("Center", pnContent);
        return pnCenter;

    }

}

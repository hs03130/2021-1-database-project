import java.sql.*;

public class InitDataBase {
	public InitDataBase(Connection con,	Statement stmt) {
		try {
			stmt = con.createStatement();
			stmt.execute("DROP DATABASE IF EXISTS madang");
			stmt.execute("CREATE DATABASE madang");
			stmt.execute("commit");
			stmt.execute("USE madang");

			stmt.execute("CREATE TABLE IF NOT EXISTS professor ( \r\n"
					+ "	professor_no INT NOT NULL PRIMARY KEY,\r\n"
					+ "	professor_name VARCHAR(45) NOT NULL,\r\n"
					+ "	professor_address VARCHAR(45) NOT NULL,\r\n"
					+ "	professor_phone VARCHAR(45) NOT NULL,\r\n"
					+ "	professor_email VARCHAR(45) NOT NULL)");
			stmt.execute("CREATE TABLE IF NOT EXISTS department (\r\n"
					+ "	department_no INT NOT NULL PRIMARY KEY,\r\n"
					+ "	department_name VARCHAR(45) NOT NULL,\r\n"
					+ "	department_contact VARCHAR(45) NOT NULL,\r\n"
					+ "	department_office VARCHAR(45) NOT NULL,\r\n"
					+ "	professor_no INT NOT NULL,\r\n"
					+ "	FOREIGN KEY(professor_no) REFERENCES professor (professor_no) ON DELETE CASCADE ON UPDATE CASCADE)");
			stmt.execute("CREATE TABLE IF NOT EXISTS affiliated_professor (\r\n"
					+ "	professor_no INT NOT NULL,\r\n"
					+ "	department_no INT NOT NULL,\r\n"
					+ " PRIMARY KEY (professor_no, department_no),\r\n"
					+ "	FOREIGN KEY(professor_no) REFERENCES professor (professor_no) ON DELETE CASCADE ON UPDATE CASCADE,\r\n"
					+ "	FOREIGN KEY(department_no) REFERENCES department (department_no) ON DELETE CASCADE ON UPDATE CASCADE)");
			stmt.execute("CREATE TABLE IF NOT EXISTS student (\r\n"
					+ "	student_no INT NOT NULL PRIMARY KEY,\r\n"
					+ "	student_name VARCHAR(45) NOT NULL,\r\n"
					+ "	student_address VARCHAR(45) NOT NULL,\r\n"
					+ "	student_phone VARCHAR(45) NOT NULL,\r\n"
					+ "	student_email VARCHAR(45) NOT NULL,\r\n"
					+ "	student_account VARCHAR(45) NOT NULL,\r\n"
					+ "	major_no INT NOT NULL,\r\n"
					+ "	minor_no INT NULL,\r\n"
					+ "	FOREIGN KEY(major_no) REFERENCES department (department_no) ON DELETE RESTRICT ON UPDATE CASCADE,\r\n"
					+ " FOREIGN KEY(minor_no) REFERENCES department (department_no) ON DELETE RESTRICT ON UPDATE CASCADE)");
			stmt.execute("CREATE TABLE IF NOT EXISTS tuition (\r\n"
					+ "	student_no INT NOT NULL,\r\n"
					+ "	tuition_year INT NOT NULL,\r\n"
					+ "	tuition_semester INT NOT NULL,\r\n"
					+ "	tuition_fee INT NOT NULL,\r\n"
					+ "	tuition_payment INT NOT NULL,\r\n"
					+ "	last_payment_date VARCHAR(11) NULL,\r\n"
					+ "	grade_semester VARCHAR(45) NOT NULL,\r\n"
					+ "	PRIMARY KEY (student_no, tuition_year, tuition_semester),\r\n"
					+ "	FOREIGN KEY(student_no) REFERENCES student (student_no) ON DELETE CASCADE ON UPDATE CASCADE)");
			stmt.execute("CREATE TABLE IF NOT EXISTS tutoring (\r\n"
					+ "  student_no INT NOT NULL  PRIMARY KEY,\r\n"
					+ "  professor_no INT NOT NULL,\r\n"
					+ "  grade_semester VARCHAR(45) NOT NULL,\r\n"
					+ "  FOREIGN KEY(student_no) REFERENCES student (student_no) ON DELETE CASCADE ON UPDATE CASCADE,\r\n"
					+ "  FOREIGN KEY(professor_no) REFERENCES professor (professor_no) ON DELETE RESTRICT ON UPDATE CASCADE)");
			stmt.execute("CREATE TABLE IF NOT EXISTS club (\r\n"
					+ "	club_no INT NOT NULL PRIMARY KEY,\r\n"
					+ "	club_name VARCHAR(45) NOT NULL,\r\n"
					+ "	club_total_member INT NOT NULL,\r\n"
					+ "	club_room VARCHAR(45) NOT NULL,\r\n"
					+ "	professor_no INT NOT NULL,\r\n"
					+ "	student_no INT NOT NULL,\r\n"
					+ "	FOREIGN KEY(professor_no) REFERENCES professor (professor_no) ON DELETE RESTRICT ON UPDATE CASCADE,\r\n"
					+ "	FOREIGN KEY(student_no) REFERENCES student (student_no) ON DELETE RESTRICT ON UPDATE CASCADE)");
			stmt.execute("CREATE TABLE IF NOT EXISTS club_join (\r\n"
					+ "	student_no INT NOT NULL,\r\n"
					+ "	club_no INT NOT NULL,\r\n"
					+ "	PRIMARY KEY (student_no, club_no),\r\n"
					+ "	FOREIGN KEY(student_no) REFERENCES student (student_no) ON DELETE CASCADE ON UPDATE CASCADE,\r\n"
					+ "	FOREIGN KEY(club_no) REFERENCES club (club_no) ON DELETE CASCADE ON UPDATE CASCADE)");
			stmt.execute("CREATE TABLE IF NOT EXISTS lecture (\r\n"
					+ "	lecture_no INT NOT NULL PRIMARY KEY,\r\n"
					+ "	lecture_class_no INT NOT NULL,\r\n"
					+ "	lecture_name VARCHAR(45) NOT NULL,\r\n"
					+ "	lecture_day1 VARCHAR(45) NULL,\r\n"
					+ "	lecture_period1 VARCHAR(45) NULL,\r\n"
					+ "	lecture_day2 VARCHAR(45) NULL,\r\n"
					+ "	lecture_period2 VARCHAR(45) NULL,\r\n"
					+ "	lecture_credit INT NOT NULL,\r\n"
					+ "	lecture_time INT NOT NULL,\r\n"
					+ "	lecture_room VARCHAR(45) NOT NULL,\r\n"
					+ " department_no INT NOT NULL,\r\n"
					+ " professor_no INT NOT NULL,\r\n"
					+ "	lecture_year INT NULL,\r\n"
					+ "	lecture_semester INT NULL,\r\n"
					+ "	FOREIGN KEY(professor_no) REFERENCES professor (professor_no) ON DELETE RESTRICT ON UPDATE CASCADE,\r\n"
					+ " FOREIGN KEY(department_no) REFERENCES department (department_no) ON DELETE RESTRICT ON UPDATE CASCADE)");
			stmt.execute("CREATE TABLE IF NOT EXISTS course (\r\n"
					+ "	lecture_no INT NOT NULL,\r\n"
					+ "	student_no INT NOT NULL,\r\n"
					+ "	attendance_score INT NULL,\r\n"
					+ "	midterm_score INT NULL,\r\n"
					+ "	finals_score INT NULL,\r\n"
					+ "	other_score INT NULL,\r\n"
					+ " total_score INT NULL,\r\n"
					+ "	grade VARCHAR(2) NULL,\r\n"
					+ " PRIMARY KEY (lecture_no, student_no),\r\n"
					+ "	FOREIGN KEY(lecture_no) REFERENCES lecture (lecture_no) ON DELETE CASCADE ON UPDATE CASCADE,\r\n"
					+ "	FOREIGN KEY(student_no) REFERENCES student (student_no) ON DELETE CASCADE ON UPDATE CASCADE)");
			
			/* ���� */
			stmt.execute("INSERT INTO professor VALUES(1, '�ְ���', '��912', '02-3408-3106', 'chukh@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(2, '������', '��813', '02-3408-3633', 'ejkwak@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(3, '������', '��714', '02-3408-3111', 'parkyh@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(4, '������', '��310', '02-3408-3953', 'entendu@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(5, '������', '��613', '02-6935-2526', 'peace1642@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(6, '���μ�', '��711', '02-3408-3130', 'inlee@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(7, '��â��', '��507', '02-3408-3148', 'changbyeon@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(8, '�ս���', '��512', '02-3408-3706', 'shsohn@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(9, '������', '��510', '02-3408-3137', 'LJONGEUN@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(10, '����', '��427', '02-6935-2486', 'alexkkim@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(11, '������', '��509', '02-3408-3717', 'hrlee@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(12, '���¼�', '��608', '02-3408-3824', 'yss2@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(13, '����ȣ', '��105', '02-3408-3163', 'dhmoon@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(14, '������', '��621', '02-6935-2469', 'seheon.oh@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(15, '�ű�', '��214', '02-3408-3215', 'shink@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(16, '�����', '��316', '02-3408-3228', 'kimyh@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(17, '������', '��1128', '02-3408-3943', 'sjkang@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(18, 'Ȳ����', '��606A', '02-3408-3642', 'sbhwang@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(19, '������', '��215A', '02-3408-3897', 'jhchin@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(20, '�ǿ���', '��923', '02-3408-3295', 'ojkwon@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(21, '�ŵ���', '��825', '02-3408-3241', 'dshin@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(22, '�����', '��625', '02-3408-3795', 'wikim@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(23, '�迵��', '��701', '02-6935-2424', 'alwaysgabi@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(24, '�鼺��', '��601B', '02-3408-3797', 'sbaik@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(25, '������', '��606', '02-3408-3696', 'hyungkim@sejong.ac.kr')");
			
			/* �а� */
			stmt.execute("INSERT INTO department VALUES(1, '������а�', '02-3408-4301', '��908', 1)");
			stmt.execute("INSERT INTO department VALUES(2, '������а�', '02-3408-3302', '��805', 2)");
			stmt.execute("INSERT INTO department VALUES(3, '�Ͼ��Ϲ��а�', '02-3408-3303', '��715', 3)");
			stmt.execute("INSERT INTO department VALUES(4, '����޸Ӵ�ƼĮ����', '02-3408-3929', '��828B', 4)");
			stmt.execute("INSERT INTO department VALUES(5, '�����а�', '02-3408-3305', '��915', 5)");
			stmt.execute("INSERT INTO department VALUES(6, '�����а�', '02-3408-3304', '��706', 6)");
			stmt.execute("INSERT INTO department VALUES(7, '�����а�', '02-3408-3308', '��604', 7)");
			stmt.execute("INSERT INTO department VALUES(8, '�̵��Ŀ�´����̼��а�', '02-3408-3307', '��506', 8)");
			stmt.execute("INSERT INTO department VALUES(9, '�����а�', '02-3408-3306', '��704B', 9)");
			stmt.execute("INSERT INTO department VALUES(10, '�濵�а�', '02-3408-3311', '��317', 10)");
			stmt.execute("INSERT INTO department VALUES(11, 'ȣ�ڰ����濵�а�', '02-3408-3312', '��517', 11)");
			stmt.execute("INSERT INTO department  (12, '�ܽİ濵�а�', '02-3408-3313', '��519', 12)");
			stmt.execute("INSERT INTO department VALUES(13, '��������', '02-3408-3315', '��313', 13)");
			stmt.execute("INSERT INTO department VALUES(14, '����õ���а�', '02-3408-3316', '��113', 14)");
			stmt.execute("INSERT INTO department VALUES(15, 'ȭ�а�', '02-3408-3317', '��215', 15)");
			stmt.execute("INSERT INTO department VALUES(16, '��ǰ�������', '02-3408-3319', '��407', 16)");
			stmt.execute("INSERT INTO department VALUES(17, '���̿����հ���', '02-3408-3334', '��612', 17)");
			stmt.execute("INSERT INTO department VALUES(18, '���̿�����ڿ�����', '02-3408-3435', '��211B', 18)");
			stmt.execute("INSERT INTO department VALUES(19, 'ü���а�', '02-3408-3661', '��611', 19)");
			stmt.execute("INSERT INTO department VALUES(20, '����������Ű��а�', '02-3408-2546', '��1107', 20)");
			stmt.execute("INSERT INTO department VALUES(21, '��ǻ�Ͱ��а�', '02-3408-3321', '��442', 21)");
			stmt.execute("INSERT INTO department VALUES(22, '����Ʈ�����а�', '02-3408-3667', '��402', 22)");
			stmt.execute("INSERT INTO department VALUES(23, '������ȣ�а�', '02-3408-4181', '��404', 23)");
			stmt.execute("INSERT INTO department VALUES(24, '�����ͻ��̾��а�', '02-6935-2544', '��403', 24)");
			stmt.execute("INSERT INTO department VALUES(25, '���ɱ������к�', '02-3408-3900', '��405', 25)");
			
			/* �л� */
			stmt.execute("INSERT INTO student VALUES(2001, '��ö��1', '����Ư���� ������ ���ڵ� 1', '010-1111-2001', 'st2001@sju.ac.kr', '����1000000000001', 1, NULL)");
			stmt.execute("INSERT INTO student VALUES(2002, '��ö��2', '����Ư���� ������ ���ڵ� 2', '010-1111-2002', 'st2002@sju.ac.kr', '����1000000000002', 2, NULL)");
			stmt.execute("INSERT INTO student VALUES(2003, '��ö��3', '����Ư���� ������ ���ڵ� 3', '010-1111-2003', 'st2003@sju.ac.kr', '����1000000000003', 3, NULL)");
			stmt.execute("INSERT INTO student VALUES(2004, '��ö��4', '����Ư���� ������ ���ڵ� 4', '010-1111-2004', 'st2004@sju.ac.kr', '����1000000000004', 4, NULL)");
			stmt.execute("INSERT INTO student VALUES(2005, '��ö��5', '����Ư���� ������ ���ڵ� 5', '010-1111-2005', 'st2005@sju.ac.kr', '����1000000000005', 5, NULL)");
			stmt.execute("INSERT INTO student VALUES(2006, '��ö��6', '����Ư���� ������ ���ڵ� 6', '010-1111-2006', 'st2006@sju.ac.kr', '����1000000000006', 6, NULL)");
			stmt.execute("INSERT INTO student VALUES(2007, '��ö��7', '����Ư���� ������ ���ڵ� 7', '010-1111-2007', 'st2007@sju.ac.kr', '����1000000000007', 7, NULL)");
			stmt.execute("INSERT INTO student VALUES(2008, '��ö��8', '����Ư���� ������ ���ڵ� 8', '010-1111-2008', 'st2008@sju.ac.kr', '����1000000000008', 8, NULL)");
			stmt.execute("INSERT INTO student VALUES(2009, '��ö��9', '����Ư���� ������ ���ڵ� 9', '010-1111-2009', 'st2009@sju.ac.kr', '����1000000000009', 9, NULL)");
			stmt.execute("INSERT INTO student VALUES(2010, '��ö��10', '����Ư���� ������ ���ڵ� 10', '010-1111-2010', 'st2010@sju.ac.kr', '����1000000000010', 10, NULL)");
			stmt.execute("INSERT INTO student VALUES(2011, '��ö��11', '����Ư���� ������ ���ڵ� 11', '010-1111-2011', 'st2011@sju.ac.kr', '����1000000000011', 11, NULL)");
			stmt.execute("INSERT INTO student VALUES(2012, '��ö��12', '����Ư���� ������ ���ڵ� 12', '010-1111-2012', 'st2012@sju.ac.kr', '����1000000000012', 12, NULL)");
			stmt.execute("INSERT INTO student VALUES(2013, '��ö��13', '����Ư���� ������ ���ڵ� 13', '010-1111-2013', 'st2013@sju.ac.kr', '����1000000000013', 13, NULL)");
			stmt.execute("INSERT INTO student VALUES(2014, '��ö��14', '����Ư���� ������ ���ڵ� 14', '010-1111-2014', 'st2014@sju.ac.kr', '����1000000000014', 14, NULL)");
			stmt.execute("INSERT INTO student VALUES(2015, '��ö��15', '����Ư���� ������ ���ڵ� 15', '010-1111-2015', 'st2015@sju.ac.kr', '����1000000000015', 15, NULL)");
			stmt.execute("INSERT INTO student VALUES(2016, '��ö��26', '����Ư���� ������ ���ڵ� 26', '010-1111-2026', 'st2016@sju.ac.kr', '����1000000000016', 16, NULL)");
			stmt.execute("INSERT INTO student VALUES(2017, '��ö��27', '����Ư���� ������ ���ڵ� 27', '010-1111-2027', 'st2017@sju.ac.kr', '����1000000000017', 17, NULL)");
			stmt.execute("INSERT INTO student VALUES(2018, '��ö��28', '����Ư���� ������ ���ڵ� 28', '010-1111-2028', 'st2018@sju.ac.kr', '����1000000000018', 18, NULL)");
			stmt.execute("INSERT INTO student VALUES(2019, '��ö��29', '����Ư���� ������ ���ڵ� 29', '010-1111-2029', 'st2019@sju.ac.kr', '����1000000000019', 19, NULL)");
			stmt.execute("INSERT INTO student VALUES(2020, '��ö��30', '����Ư���� ������ ���ڵ� 30', '010-1111-2030', 'st2020@sju.ac.kr', '����1000000000020', 20, NULL)");
			stmt.execute("INSERT INTO student VALUES(2021, '��ö��31', '����Ư���� ������ ���ڵ� 31', '010-1111-2031', 'st2021@sju.ac.kr', '����1000000000021', 21, NULL)");
			stmt.execute("INSERT INTO student VALUES(2022, '��ö��32', '����Ư���� ������ ���ڵ� 32', '010-1111-2032', 'st2022@sju.ac.kr', '����1000000000022', 22, NULL)");
			stmt.execute("INSERT INTO student VALUES(2023, '��ö��33', '����Ư���� ������ ���ڵ� 33', '010-1111-2033', 'st2023@sju.ac.kr', '����1000000000023', 23, NULL)");
			stmt.execute("INSERT INTO student VALUES(2024, '��ö��34', '����Ư���� ������ ���ڵ� 34', '010-1111-2034', 'st2024@sju.ac.kr', '����1000000000024', 24, NULL)");
			stmt.execute("INSERT INTO student VALUES(2025, '��ö��35', '����Ư���� ������ ���ڵ� 35', '010-1111-2035', 'st2025@sju.ac.kr', '����1000000000025', 25, NULL)");
			stmt.execute("INSERT INTO student VALUES(2101, '�迵��1', '����Ư���� ������ ���ڵ� 36', '010-1111-2036', 'st2101@sju.ac.kr', '����1000000000001', 1, NULL)");
			stmt.execute("INSERT INTO student VALUES(2102, '�迵��2', '����Ư���� ������ ���ڵ� 37', '010-1111-2037', 'st2102@sju.ac.kr', '����1000000000002', 2, NULL)");
			stmt.execute("INSERT INTO student VALUES(2103, '�迵��3', '����Ư���� ������ ���ڵ� 38', '010-1111-2038', 'st2103@sju.ac.kr', '����1000000000003', 3, NULL)");
			stmt.execute("INSERT INTO student VALUES(2104, '�迵��4', '����Ư���� ������ ���ڵ� 39', '010-1111-2039', 'st2104@sju.ac.kr', '����1000000000004', 4, NULL)");
			stmt.execute("INSERT INTO student VALUES(2105, '�迵��5', '����Ư���� ������ ���ڵ� 40', '010-1111-2040', 'st2105@sju.ac.kr', '����1000000000005', 5, NULL)");
			stmt.execute("INSERT INTO student VALUES(2106, '�迵��6', '����Ư���� ������ ���ڵ� 41', '010-1111-2041', 'st2106@sju.ac.kr', '����1000000000006', 6, NULL)");
			stmt.execute("INSERT INTO student VALUES(2107, '�迵��7', '����Ư���� ������ ���ڵ� 42', '010-1111-2042', 'st2107@sju.ac.kr', '����1000000000007', 22, NULL)");
			stmt.execute("INSERT INTO student VALUES(2108, '�迵��8', '����Ư���� ������ ���ڵ� 43', '010-1111-2043', 'st2108@sju.ac.kr', '����1000000000008', 23, NULL)");
			stmt.execute("INSERT INTO student VALUES(2109, '�迵��9', '����Ư���� ������ ���ڵ� 44', '010-1111-2044', 'st2109@sju.ac.kr', '����1000000000009', 24, NULL)");
			stmt.execute("INSERT INTO student VALUES(2110, '�迵��10', '����Ư���� ������ ���ڵ� 45', '010-1111-2045', 'st2110@sju.ac.kr', '����1000000000010', 25, NULL)");
			
			/* ��� */
			stmt.execute("INSERT INTO tuition VALUES(2001, 2020, 1, 4000000, 4000000, '2020-02-22', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2002, 2020, 1, 2870000, 2870000, '2020-02-23', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2003, 2020, 1, 3900000, 3900000, '2020-02-24', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2004, 2020, 1, 4000000, 4000000, '2020-02-25', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2005, 2020, 1, 3900000, 3900000, '2020-02-26', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2006, 2020, 1, 2100000, 2100000, '2020-02-27', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2007, 2020, 1, 3000000, 3000000, '2020-02-22', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2008, 2020, 1, 3100000, 3100000, '2020-02-23', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2009, 2020, 1, 3000000, 3000000, '2020-02-24', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2010, 2020, 1, 2100000, 2100000, '2020-02-25', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2011, 2020, 1, 4000000, 4000000, '2020-02-26', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2012, 2020, 1, 2870000, 2870000, '2020-02-27', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2013, 2020, 1, 3900000, 3900000, '2020-02-22', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2014, 2020, 1, 3100000, 3100000, '2020-02-22', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2015, 2020, 1, 3000000, 3000000, '2020-02-22', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2016, 2020, 1, 3000000, 3000000, '2020-02-22', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2017, 2020, 1, 3100000, 3100000, '2020-02-23', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2018, 2020, 1, 3000000, 3000000, '2020-02-24', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2019, 2020, 1, 2100000, 2100000, '2020-02-25', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2020, 2020, 1, 4000000, 4000000, '2020-02-26', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2021, 2020, 1, 2870000, 2870000, '2020-02-27', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2022, 2020, 1, 3900000, 3900000, '2020-02-22', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2023, 2020, 1, 3000000, 3000000, '2020-02-23', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2024, 2020, 1, 3100000, 3100000, '2020-02-24', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2025, 2020, 1, 3000000, 3000000, '2020-02-25', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2001, 2020, 2, 2100000, 2100000, '2020-08-02', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2002, 2020, 2, 4000000, 4000000, '2020-08-03', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2003, 2020, 2, 2870000, 2870000, '2020-08-04', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2004, 2020, 2, 2870000, 2870000, '2020-08-05', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2005, 2020, 2, 3900000, 3900000, '2020-08-06', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2006, 2020, 2, 3000000, 3000000, '2020-08-07', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2007, 2020, 2, 3100000, 3100000, '2020-08-08', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2008, 2020, 2, 3000000, 3000000, '2020-08-09', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2009, 2020, 2, 2100000, 2100000, '2020-08-10', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2010, 2020, 2, 4000000, 4000000, '2020-08-11', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2011, 2020, 2, 2870000, 2870000, '2020-08-02', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2012, 2020, 2, 3900000, 3900000, '2020-08-03', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2013, 2020, 2, 3000000, 3000000, '2020-08-04', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2014, 2020, 2, 3100000, 3100000, '2020-08-05', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2015, 2020, 2, 3000000, 3000000, '2020-08-06', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2016, 2020, 2, 2100000, 2100000, '2020-08-07', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2017, 2020, 2, 4000000, 4000000, '2020-08-08', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2018, 2020, 2, 2870000, 2870000, '2020-08-09', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2019, 2020, 2, 2870000, 2870000, '2020-08-10', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2020, 2020, 2, 3900000, 3900000, '2020-08-11', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2021, 2020, 2, 3000000, 3000000, '2020-08-12', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2022, 2020, 2, 3100000, 3100000, '2020-08-13', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2023, 2020, 2, 3000000, 3000000, '2020-08-14', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2024, 2020, 2, 2100000, 2100000, '2020-08-15', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2025, 2020, 2, 4000000, 4000000, '2020-08-16', '1�г�2�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2001, 2021, 1, 2870000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2002, 2021, 1, 3900000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2003, 2021, 1, 4000000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2004, 2021, 1, 3900000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2005, 2021, 1, 2100000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2006, 2021, 1, 2100000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2007, 2021, 1, 3000000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2008, 2021, 1, 3100000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2009, 2021, 1, 3000000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2010, 2021, 1, 2100000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2011, 2021, 1, 4000000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2012, 2021, 1, 2870000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2013, 2021, 1, 3900000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2014, 2021, 1, 3100000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2015, 2021, 1, 2100000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2016, 2021, 1, 3000000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2017, 2021, 1, 3100000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2018, 2021, 1, 3000000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2019, 2021, 1, 2100000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2020, 2021, 1, 4000000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2021, 2021, 1, 2870000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2022, 2021, 1, 3900000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2023, 2021, 1, 3100000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2024, 2021, 1, 3000000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2025, 2021, 1, 3000000, 0, '', '2�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2101, 2021, 1, 3100000, 0, '', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2102, 2021, 1, 3000000, 0, '', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2103, 2021, 1, 2100000, 0, '', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2104, 2021, 1, 4000000, 0, '', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2105, 2021, 1, 2870000, 0, '', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2106, 2021, 1, 3900000, 0, '', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2107, 2021, 1, 3000000, 0, '', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2108, 2021, 1, 3100000, 0, '', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2109, 2021, 1, 3000000, 0, '', '1�г�1�б�')");
			stmt.execute("INSERT INTO tuition VALUES(2110, 2021, 1, 4000000, 0, '', '1�г�1�б�')");
		
			/* ���� �Ҽ� */
			stmt.execute("INSERT INTO affiliated_professor VALUES(1, 1)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(2, 2)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(3, 3)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(4, 4)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(5, 5)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(6, 6)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(7, 7)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(8, 8)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(9, 9)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(10, 10)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(11, 11)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(12, 12)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(13, 13)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(14, 14)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(15, 15)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(16, 16)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(17, 17)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(18, 18)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(19, 19)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(20, 20)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(21, 21)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(22, 22)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(23, 23)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(24, 24)");
			stmt.execute("INSERT INTO affiliated_professor VALUES(25, 25)");
			
			/* �������� */
			stmt.execute("INSERT INTO tutoring VALUES(2001, 1, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2002, 2, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2003, 3, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2004, 4, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2005, 5, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2006, 6, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2007, 7, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2008, 8, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2009, 9, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2010, 10, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2011, 11, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2012, 12, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2013, 13, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2014, 14, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2015, 15, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2016, 16, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2017, 17, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2018, 18, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2019, 19, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2020, 20, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2021, 21, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2022, 22, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2023, 23, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2024, 24, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2025, 25, '2�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2101, 21, '1�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2102, 11, '1�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2103, 12, '1�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2104, 13, '1�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2105, 21, '1�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2106, 15, '1�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2107, 16, '1�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2108, 17, '1�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2109, 18, '1�г�1�б�')");
			stmt.execute("INSERT INTO tutoring VALUES(2110, 21, '1�г�1�б�')");
			
			/* ���Ƹ� */
			stmt.execute("INSERT INTO club VALUES(1, '���Ƹ�1', 2, '��601', 1, 2001)");
			stmt.execute("INSERT INTO club VALUES(2, '���Ƹ�2', 2, '��602', 2, 2002)");
			stmt.execute("INSERT INTO club VALUES(3, '���Ƹ�3', 2, '��603', 3, 2003)");
			stmt.execute("INSERT INTO club VALUES(4, '���Ƹ�4', 2, '��604', 4, 2004)");
			stmt.execute("INSERT INTO club VALUES(5, '���Ƹ�5', 2, '��605', 5, 2005)");
			stmt.execute("INSERT INTO club VALUES(6, '���Ƹ�6', 2, '��501', 6, 2006)");
			stmt.execute("INSERT INTO club VALUES(7, '���Ƹ�7', 2, '��502', 7, 2007)");
			stmt.execute("INSERT INTO club VALUES(8, '���Ƹ�8', 2, '��503', 8, 2008)");
			stmt.execute("INSERT INTO club VALUES(9, '���Ƹ�9', 2, '��504', 9, 2009)");
			stmt.execute("INSERT INTO club VALUES(10, '���Ƹ�10', 2, '��505', 10, 2010)");
			stmt.execute("INSERT INTO club VALUES(11, '���Ƹ�11', 2, '��401', 11, 2011)");
			stmt.execute("INSERT INTO club VALUES(12, '���Ƹ�12', 2, '��402', 12, 2012)");
			stmt.execute("INSERT INTO club VALUES(13, '���Ƹ�13', 2, '��403', 13, 2013)");
			stmt.execute("INSERT INTO club VALUES(14, '���Ƹ�14', 2, '��404', 14, 2014)");
			stmt.execute("INSERT INTO club VALUES(15, '���Ƹ�15', 2, '��405', 15, 2015)");
			stmt.execute("INSERT INTO club VALUES(16, '���Ƹ�16', 2, '��301', 16, 2016)");
			stmt.execute("INSERT INTO club VALUES(17, '���Ƹ�17', 2, '��302', 17, 2017)");
			stmt.execute("INSERT INTO club VALUES(18, '���Ƹ�18', 2, '��303', 18, 2018)");
			stmt.execute("INSERT INTO club VALUES(19, '���Ƹ�19', 2, '��304', 19, 2019)");
			stmt.execute("INSERT INTO club VALUES(20, '���Ƹ�20', 2, '��305', 20, 2020)");
			stmt.execute("INSERT INTO club VALUES(21, '���Ƹ�21', 1, '��201', 21, 2021)");
			stmt.execute("INSERT INTO club VALUES(22, '���Ƹ�22', 1, '��202', 22, 2022)");
			stmt.execute("INSERT INTO club VALUES(23, '���Ƹ�23', 1, '��203', 23, 2023)");
			stmt.execute("INSERT INTO club VALUES(24, '���Ƹ�24', 1, '��204', 24, 2024)");
			stmt.execute("INSERT INTO club VALUES(25, '���Ƹ�25', 1, '��205', 25, 2025)");
			
			/* ���Ƹ��� */
			stmt.execute("INSERT INTO club_join VALUES(2001, 1)");
			stmt.execute("INSERT INTO club_join VALUES(2002, 2)");
			stmt.execute("INSERT INTO club_join VALUES(2003, 3)");
			stmt.execute("INSERT INTO club_join VALUES(2004, 4)");
			stmt.execute("INSERT INTO club_join VALUES(2005, 5)");
			stmt.execute("INSERT INTO club_join VALUES(2006, 6)");
			stmt.execute("INSERT INTO club_join VALUES(2007, 7)");
			stmt.execute("INSERT INTO club_join VALUES(2008, 8)");
			stmt.execute("INSERT INTO club_join VALUES(2009, 9)");
			stmt.execute("INSERT INTO club_join VALUES(2010, 10)");
			stmt.execute("INSERT INTO club_join VALUES(2011, 11)");
			stmt.execute("INSERT INTO club_join VALUES(2012, 12)");
			stmt.execute("INSERT INTO club_join VALUES(2013, 13)");
			stmt.execute("INSERT INTO club_join VALUES(2014, 14)");
			stmt.execute("INSERT INTO club_join VALUES(2015, 15)");
			stmt.execute("INSERT INTO club_join VALUES(2016, 16)");
			stmt.execute("INSERT INTO club_join VALUES(2017, 17)");
			stmt.execute("INSERT INTO club_join VALUES(2018, 18)");
			stmt.execute("INSERT INTO club_join VALUES(2019, 19)");
			stmt.execute("INSERT INTO club_join VALUES(2020, 20)");
			stmt.execute("INSERT INTO club_join VALUES(2021, 21)");
			stmt.execute("INSERT INTO club_join VALUES(2022, 22)");
			stmt.execute("INSERT INTO club_join VALUES(2023, 23)");
			stmt.execute("INSERT INTO club_join VALUES(2024, 24)");
			stmt.execute("INSERT INTO club_join VALUES(2025, 25)");
			stmt.execute("INSERT INTO club_join VALUES(2101, 1)");
			stmt.execute("INSERT INTO club_join VALUES(2102, 2)");
			stmt.execute("INSERT INTO club_join VALUES(2103, 3)");
			stmt.execute("INSERT INTO club_join VALUES(2104, 4)");
			stmt.execute("INSERT INTO club_join VALUES(2105, 5)");
			stmt.execute("INSERT INTO club_join VALUES(2106, 6)");
			stmt.execute("INSERT INTO club_join VALUES(2107, 7)");
			stmt.execute("INSERT INTO club_join VALUES(2108, 8)");
			stmt.execute("INSERT INTO club_join VALUES(2109, 9)");
			stmt.execute("INSERT INTO club_join VALUES(2110, 10)");
			stmt.execute("INSERT INTO club_join VALUES(2101, 11)");
			stmt.execute("INSERT INTO club_join VALUES(2102, 12)");
			stmt.execute("INSERT INTO club_join VALUES(2103, 13)");
			stmt.execute("INSERT INTO club_join VALUES(2104, 14)");
			stmt.execute("INSERT INTO club_join VALUES(2105, 15)");
			stmt.execute("INSERT INTO club_join VALUES(2106, 16)");
			stmt.execute("INSERT INTO club_join VALUES(2107, 17)");
			stmt.execute("INSERT INTO club_join VALUES(2108, 18)");
			stmt.execute("INSERT INTO club_join VALUES(2109, 19)");
			stmt.execute("INSERT INTO club_join VALUES(2110, 20)");
			
			/* ���� */
			stmt.execute("INSERT INTO lecture VALUES(1, 1, '�ѱ�����������', '��', '15,16,17,18,19,20', '', '', 3, 3, '��301', 1, 1, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(2, 1, '������ǳ�', '��', '3,4,5', '��', '3,4,5', 3, 3, '��403', 2, 2, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(3, 1, '�Ϻ����', '��', '15,16,17', '��', '15,16,17', 3, 3, '��404A', 3, 3, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(4, 1, 'â�������������', '��', '12,13,14', '', '', 1, 1.5, '�д������', 4, 4, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(5, 1, '�����а���', 'ȭ', '12,13,14', '��', '12,13,14', 3, 3, '��307', 5, 5, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(6, 1, '��������Ž��', 'ȭ', '9,10,11,12,13,14', '', '', 3, 3, '��407', 6, 6, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(7, 1, '���ο�����', '��', '3,4,5,6,7,8', '', '', 3, 3, '��311', 7, 7, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(8, 1, '��۱�ȹ', '��', '12,13,14,15,16,17', '', '', 3, 3, '��501B', 8, 8, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(9, 1, '��������1', '��', '9,10,11,12,13,14', '', '', 3, 3, '��720', 9, 9, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(10, 1, '���غ���', '��', '3,4,5,6,7,8', '', '', 3, 3, '��208', 10, 10, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(11, 1, '�����濵��', '��', '12,13,14', '��', '12,13,14', 3, 3, '��617', 11, 11, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(12, 1, '��������', 'ȭ', '15,16,17,18,19,20', '', '', 3, 3, '��428', 12, 12, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(13, 1, '�ؼ��а���1', '��', '6,7,8', '��', '6,7,8', 3, 3, '��211', 13, 13, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(14, 1, '����1', '��', '6,7,8', '��', '6,7,8', 3, 3, '��405', 14, 14, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(15, 1, '����ȭ��1', 'ȭ', '9,10,11', '��', '9,10,11', 3, 3, '��401', 15, 15, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(16, 1, '����ȭ��', '��', '15,16,17', '��', '15,16,17', 3, 3, '��101', 16, 16, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(17, 1, '�Ϲݹ̻����й׽���', 'ȭ', '17,18,19,20', '��', '17,18,19,20', 3, 4, '��B202', 17, 17, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(18, 1, '����ȭ��', 'ȭ', '13,14,15', '��', '13,14,15', 3, 3, '��205', 18, 18, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(19, 1, 'ü������', 'ȭ', '16,14,15,16,17', '', '', 3, 3, '��102', 19, 19, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(20, 1, '����ȸ��', '��', '12,13,14', '��', '12,13,14', 3, 3, '��B105A', 20, 20, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(21, 1, '�����ͺ��̽�', 'ȭ', '3,4,5', '��', '3,4,5', 3, 3, '��B103', 21, 21, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(22, 1, '�ڷᱸ���׽ǽ�', '��', '9,10,11,21,22,23,24', '��', '9,10,11', 4, 5, '��B209', 22, 22, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(23, 1, '������ȣ�ͺ����Ǳ���', '��', '12,13,14', '��', '12,13,14', 3, 3, '��B204', 23, 23, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(24, 1, '�����ͺм�����', '��', '3,4,5,6,7,8', '', '', 3, 3, '��B110', 24, 24, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(25, 1, '�ڷᱸ���׽ǽ�', '��', '9,10,11,18,19,20,21', '��', '9,10,11', 4, 5, '��B106', 25, 25, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(26, 1, '��ü����', 'ȭ', '9,10,11,12,13,14', '', '', 3, 3, '��503', 1, 1, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(27, 1, '�̱���ȸ�͹���', '��', '12,13,14,15,16,17', '', '', 3, 3, '��403', 2, 2, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(28, 1, '�Ϻ���ȸȭ', '��', '18,19,20', '��', '18,19,20', 3, 3, '��404A', 3, 3, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(29, 1, '������ȸ����1', '', '', '', '', 1, 1, '', 4, 4, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(30, 1, '��ȭ��������ǽ�1', '��', '21,22,23,24', '', '', 1, 2, '��307', 5, 5, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(31, 1, '�������', '��', '15,16,17,18,19,20', '', '', 3, 3, '��407', 6, 6, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(32, 1, '����������', 'ȭ', '18,19,20', '��', '18,19,20', 3, 3, '��311', 7, 7, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(33, 1, '��ȭ�Ͱ���', '��', '15,16,17,18,19,20', '', '', 3, 3, '��501B', 8, 8, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(34, 1, '��������2', '��', '3,4,5,6,7,8', '', '', 3, 3, '��720', 9, 9, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(35, 1, '�������', '��', '12,13,14', '��', '12,13,14', 3, 3, '��431', 10, 10, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(36, 1, '��������', '��', '3,4,5,6,7,8', '', '', 3, 3, '��206', 11, 11, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(37, 1, '���ΰ���', 'ȭ', '9,10,11', '��', '9,10,11', 3, 3, '��428', 12, 12, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(38, 1, 'Ȯ����', '��', '6,7,8', '��', '6,7,8', 3, 3, '��130', 13, 13, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(39, 1, '����õ����', 'ȭ', '15,16,17', '��', '15,16,17', 3, 3, '��601', 14, 14, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(40, 1, '�ݼ�����ȭ��', 'ȭ', '9,10,11', '��', '9,10,11', 3, 3, '��401', 15, 15, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(41, 1, '��ǰ���߽���', '��', '12,13,14,15,16,17,18,19', '', '', 3, 4, '��714', 16, 16, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(42, 1, '��Ʈ����������', '��', '3,4,5,6,7,8', '', '', 3, 3, '��712', 17, 17, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(43, 1, '�Ĺ������й׽���', 'ȭ', '6,7,8', '��', '6,7,8', 3, 3, '��205', 18, 18, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(44, 1, 'ü����', '��', '5,6,7,8,9,10', '', '', 3, 3, '��101', 19, 19, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(45, 1, '���ʼ���', 'ȭ', '9,10,11,12,13,14', '', '', 3, 3, '��905', 20, 20, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(46, 1, '���C���α׷��ֹ׽ǽ�', 'ȭ', '12,13,14', '��', '12,13,14', 3, 3, '��B201', 21, 21, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(47, 1, '�����л���', 'ȭ', '15,16,17', '��', '15,16,17', 3, 3, '��102', 22, 22, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(48, 1, '�ý������α׷���', 'ȭ', '14,15,16', '��', '14,15,16', 3, 3, '��B105', 23, 23, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(49, 1, '�����ͽð�ȭ', 'ȭ', '9,10,11', '��', '9,10,11', 3, 3, '��B110', 24, 24, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(50, 1, '������', '��', '12,13,14', 'ȭ', '6,7,8', 3, 3, '��201', 25, 25, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(51, 1, '�ѱ�����������', '��', '15,16,17,18,19,20', '', '', 3, 3, '��301', 1, 1, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(52, 1, '�������', '��', '9,10,11,12,13,14', '', '', 3, 3, '��301', 1, 1, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(53, 1, '������ǳ�', '��', '6,7,8', '��', '6,7,8', 3, 3, '��403', 2, 2, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(54, 1, '������������', '��', '6,7,8,9,10,11', '', '', 3, 3, '��403', 2, 2, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(55, 1, '�Ϻ����', '��', '15,16,17', '��', '15,16,17', 3, 3, '��404A', 3, 3, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(56, 1, '�Ϻ����۹�', 'ȭ', '9,10,11', '��', '9,10,11', 3, 3, '��502', 3, 3, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(57, 1, '�����ذ������ѱ۾���͹�ǥ', '��', '7,8,9,10', '', '', 3, 2, '��202', 4, 4, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(58, 1, '�����а���', 'ȭ', '12,13,14', '��', '12,13,14', 3, 3, '��307', 5, 5, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(59, 1, '�η��Ǽ��繮ȭ', '��', '9,10,11,12,13,14', '', '', 3, 3, '��307', 5, 5, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(60, 1, '��������Ž��', 'ȭ', '9,10,11,12,13,14', '', '', 3, 3, '��407', 6, 6, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(61, 1, '����ö��������', '��', '9,10,11,12,13,14', '', '', 3, 3, '��407', 6, 6, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(62, 1, '���ο�����', '��', '3,4,5,6,7,8', '', '', 3, 3, '��311', 7, 7, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(63, 1, '��å��', 'ȭ', '18,19,20', '��', '18,19,20', 3, 3, '��501B', 7, 7, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(64, 1, '��۱�ȹ', '��', '12,13,14,15,16,17', '', '', 3, 3, '��501B', 8, 8, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(65, 1, '�����ü��ȹ', 'ȭ', '3,4,5,6,7,8', '', '', 3, 3, '��501B', 8, 8, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(66, 1, '��������1', '��', '9,10,11,12,13,14', '', '', 3, 3, '��720', 9, 9, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(67, 1, '���غ���', '��', '3,4,5,6,7,8', '', '', 3, 3, '��208', 10, 10, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(68, 1, '�����濵��', '��', '12,13,14', '��', '12,13,14', 3, 3, '��617', 11, 11, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(69, 1, 'ȣ�ڰ濵��', '��', '15,16,17,18,19,20', '', '', 3, 3, '��617', 11, 11, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(70, 1, '��������', 'ȭ', '15,16,17,18,19,20', '', '', 3, 3, '��428', 12, 12, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(71, 1, '�ַ���', 'ȭ', '6,7,8', '��', '6,7,8', 3, 3, '��428', 12, 12, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(72, 1, '���������', 'ȭ', '6,7,8', '��', '6,7,8', 3, 3, '��211', 13, 13, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(73, 1, '���շ�', 'ȭ', '15,16,17', '��', '15,16,17', 3, 3, '��130', 13, 13, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(74, 1, '����1', '��', '6,7,8', '��', '6,7,8', 3, 3, '��405', 14, 14, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(75, 1, '����2', '��', '15,16,17', '��', '15,16,17', 3, 3, '��405', 14, 14, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(76, 1, '����ȭ��1', 'ȭ', '9,10,11', '��', '9,10,11', 3, 3, '��401', 15, 15, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(77, 1, '��ȭ��', 'ȭ', '15,16,17', '��', '15,16,17', 3, 3, '��402', 15, 15, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(78, 1, '����ȭ��', '��', '15,16,17', '��', '15,16,17', 3, 3, '��101', 16, 16, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(79, 1, '�Ϲݹ̻����й׽���', 'ȭ', '6,7,8,9', '��', '3,4,5,6', 4, 3, '��B04', 16, 16, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(80, 1, '�Ϲݹ̻����й׽���', 'ȭ', '17,18,19,20', '��', '17,18,19,20', 3, 4, '��B202', 17, 17, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(81, 1, '������', '��', '18,19,20', '��', '18,19,20', 3, 3, '��B202', 17, 17, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(82, 1, '�Ϲݹ̻����й׽���', '��', '12,13,14,15', '��', '12,13,14,15', 3, 4, '��203', 18, 18, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(83, 1, '����ȭ��', 'ȭ', '13,14,15', '��', '13,14,15', 3, 3, '��205', 18, 18, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(84, 1, '����', '��', '15,16,17,18,19,20', '', '', 3, 3, '��102', 19, 19, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(85, 1, '����ȸ��', '��', '12,13,14', '��', '12,13,14', 3, 3, '��B105A', 20, 20, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(86, 1, '�����ͺ��̽�', 'ȭ', '3,4,5', '��', '3,4,5', 3, 3, '��B103', 21, 21, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(87, 1, '��ġ�ؼ�', 'ȭ', '12,13,14', '��', '12,13,14', 3, 3, '��B102', 22, 22, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(88, 1, '�ڷᱸ���׽ǽ�', '��', '15,16,17', '��', '15,16,17,21,22,23,24', 4, 5, '��B102', 22, 22, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(89, 1, '��ǻ�ͳ�Ʈ��ũ', 'ȭ', '12,13,14', '��', '12,13,14', 3, 3, '��B105', 23, 23, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(90, 1, '������', 'ȭ', '3,4,5', '��', '3,4,5', 3, 3, '��B102', 23, 23, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(91, 1, '�����ͺм�����', '��', '3,4,5,6,7,8', '', '', 3, 3, '��B110', 24, 24, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(92, 1, '�ǻ�����м�', 'ȭ', '15,16,17', '��', '15,16,17', 3, 3, '��B110', 24, 24, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(93, 1, '�ڷᱸ���׽ǽ�', '��', '9,10,11,18,19,20,21', '��', '9,10,11', 4, 5, '��B106', 25, 25, 2021, 1)");
			
			/* ���� */
			stmt.execute("INSERT INTO course VALUES(1,2001,100,100,100,100,100,'A+')");
			stmt.execute("INSERT INTO course VALUES(26,2001,100,38,0,80,37,'F')");
			stmt.execute("INSERT INTO course VALUES(1,2002,100,45,55,80,62,'C+')");
			stmt.execute("INSERT INTO course VALUES(2,2002,100,40,80,78,70,'C+')");
			stmt.execute("INSERT INTO course VALUES(26,2002,100,50,90,90,79,'B+')");
			stmt.execute("INSERT INTO course VALUES(27,2002,100,78,100,100,93,'B+')");
			stmt.execute("INSERT INTO course VALUES(2,2003,100,38,99,90,79,'B+')");
			stmt.execute("INSERT INTO course VALUES(3,2003,100,50,100,100,85,'B+')");
			stmt.execute("INSERT INTO course VALUES(27,2003,100,25,50,80,54,'F')");
			stmt.execute("INSERT INTO course VALUES(28,2003,100,50,40,80,57,'C+')");
			stmt.execute("INSERT INTO course VALUES(3,2004,100,78,80,78,81,'B+')");
			stmt.execute("INSERT INTO course VALUES(4,2004,100,25,50,90,56,'C+')");
			stmt.execute("INSERT INTO course VALUES(5,2004,100,0,90,100,66,'C+')");
			stmt.execute("INSERT INTO course VALUES(28,2004,100,90,38,90,70,'C+')");
			stmt.execute("INSERT INTO course VALUES(29,2004,null,null,null,null,50,'NP')");
			stmt.execute("INSERT INTO course VALUES(43,2004,100,50,90,80,77,'B+')");
			stmt.execute("INSERT INTO course VALUES(4,2005,100,80,88,80,85,'B+')");
			stmt.execute("INSERT INTO course VALUES(5,2005,100,90,95,78,91,'B+')");
			stmt.execute("INSERT INTO course VALUES(29,2005,null,null,null,null,80,'P')");
			stmt.execute("INSERT INTO course VALUES(30,2005,100,100,25,100,70,'C+')");
			stmt.execute("INSERT INTO course VALUES(6,2006,100,100,78,90,89,'B+')");
			stmt.execute("INSERT INTO course VALUES(11,2006,100,18,77,100,66,'C+')");
			stmt.execute("INSERT INTO course VALUES(30,2006,100,50,50,80,61,'C+')");
			stmt.execute("INSERT INTO course VALUES(31,2006,100,10,10,80,33,'F')");
			stmt.execute("INSERT INTO course VALUES(6,2007,100,50,77,78,71,'C+')");
			stmt.execute("INSERT INTO course VALUES(7,2007,100,10,80,90,63,'C+')");
			stmt.execute("INSERT INTO course VALUES(31,2007,100,18,18,100,43,'F')");
			stmt.execute("INSERT INTO course VALUES(32,2007,100,99,90,90,94,'B+')");
			stmt.execute("INSERT INTO course VALUES(7,2008,100,18,48,100,55,'C+')");
			stmt.execute("INSERT INTO course VALUES(8,2008,100,90,80,80,85,'B+')");
			stmt.execute("INSERT INTO course VALUES(32,2008,100,52,78,80,73,'C+')");
			stmt.execute("INSERT INTO course VALUES(33,2008,100,100,18,78,63,'C+')");
			stmt.execute("INSERT INTO course VALUES(8,2009,100,99,50,90,78,'B+')");
			stmt.execute("INSERT INTO course VALUES(9,2009,100,52,88,100,81,'B+')");
			stmt.execute("INSERT INTO course VALUES(33,2009,100,50,90,90,79,'B+')");
			stmt.execute("INSERT INTO course VALUES(34,2009,100,10,99,100,73,'C+')");
			stmt.execute("INSERT INTO course VALUES(9,2010,100,100,90,80,92,'B+')");
			stmt.execute("INSERT INTO course VALUES(10,2010,100,50,95,80,79,'B+')");
			stmt.execute("INSERT INTO course VALUES(34,2010,100,18,50,78,51,'F')");
			stmt.execute("INSERT INTO course VALUES(35,2010,100,90,10,90,59,'C+')");
			stmt.execute("INSERT INTO course VALUES(10,2011,100,10,78,100,64,'C+')");
			stmt.execute("INSERT INTO course VALUES(35,2011,100,99,18,90,65,'C+')");
			stmt.execute("INSERT INTO course VALUES(36,2011,100,52,90,100,82,'B+')");
			stmt.execute("INSERT INTO course VALUES(11,2012,100,90,0,80,53,'F')");
			stmt.execute("INSERT INTO course VALUES(12,2012,100,99,90,80,92,'B+')");
			stmt.execute("INSERT INTO course VALUES(36,2012,100,40,78,78,69,'C+')");
			stmt.execute("INSERT INTO course VALUES(37,2012,100,45,90,90,78,'B+')");
			stmt.execute("INSERT INTO course VALUES(12,2013,100,52,100,100,86,'B+')");
			stmt.execute("INSERT INTO course VALUES(13,2013,100,40,78,90,71,'C+')");
			stmt.execute("INSERT INTO course VALUES(37,2013,100,40,100,100,82,'B+')");
			stmt.execute("INSERT INTO course VALUES(38,2013,100,38,90,80,73,'C+')");
			stmt.execute("INSERT INTO course VALUES(14,2014,100,40,80,80,70,'C+')");
			stmt.execute("INSERT INTO course VALUES(38,2014,100,50,100,78,81,'B+')");
			stmt.execute("INSERT INTO course VALUES(39,2014,100,78,50,90,71,'C+')");
			stmt.execute("INSERT INTO course VALUES(14,2015,100,38,0,100,41,'F')");
			stmt.execute("INSERT INTO course VALUES(15,2015,100,50,90,90,79,'B+')");
			stmt.execute("INSERT INTO course VALUES(39,2015,100,25,90,100,74,'C+')");
			stmt.execute("INSERT INTO course VALUES(40,2015,100,80,95,80,88,'B+')");
			stmt.execute("INSERT INTO course VALUES(13,2016,100,45,25,80,50,'F')");
			stmt.execute("INSERT INTO course VALUES(15,2016,100,78,100,78,89,'B+')");
			stmt.execute("INSERT INTO course VALUES(16,2016,100,25,50,90,56,'C+')");
			stmt.execute("INSERT INTO course VALUES(40,2016,100,0,78,100,61,'C+')");
			stmt.execute("INSERT INTO course VALUES(41,2016,100,90,90,90,91,'B+')");
			stmt.execute("INSERT INTO course VALUES(16,2017,100,80,80,100,86,'B+')");
			stmt.execute("INSERT INTO course VALUES(17,2017,100,0,50,80,46,'F')");
			stmt.execute("INSERT INTO course VALUES(18,2015,100,100,90,80,92,'B+')");
			stmt.execute("INSERT INTO course VALUES(41,2017,100,99,78,78,87,'B+')");
			stmt.execute("INSERT INTO course VALUES(42,2017,100,52,90,90,80,'B+')");
			stmt.execute("INSERT INTO course VALUES(17,2018,100,90,88,100,92,'B+')");
			stmt.execute("INSERT INTO course VALUES(18,2018,100,50,100,90,83,'B+')");
			stmt.execute("INSERT INTO course VALUES(42,2018,100,100,100,100,100,'A+')");
			stmt.execute("INSERT INTO course VALUES(19,2019,100,10,50,80,49,'F')");
			stmt.execute("INSERT INTO course VALUES(43,2019,100,90,100,80,93,'B+')");
			stmt.execute("INSERT INTO course VALUES(44,2019,100,99,100,78,95,'A+')");
			stmt.execute("INSERT INTO course VALUES(19,2020,100,18,10,90,37,'F')");
			stmt.execute("INSERT INTO course VALUES(20,2020,100,90,18,100,64,'C+')");
			stmt.execute("INSERT INTO course VALUES(45,2020,100,52,10,90,48,'F')");
			stmt.execute("INSERT INTO course VALUES(20,2021,100,99,90,100,96,'A+')");
			stmt.execute("INSERT INTO course VALUES(21,2021,100,52,78,80,73,'C+')");
			stmt.execute("INSERT INTO course VALUES(45,2021,100,100,18,80,63,'C+')");
			stmt.execute("INSERT INTO course VALUES(48,2021,100,99,45,78,73,'C+')");
			stmt.execute("INSERT INTO course VALUES(21,2022,100,100,25,90,68,'C+')");
			stmt.execute("INSERT INTO course VALUES(22,2022,100,10,0,100,33,'F')");
			stmt.execute("INSERT INTO course VALUES(25,2023,100,45,78,90,73,'C+')");
			stmt.execute("INSERT INTO course VALUES(46,2022,100,50,90,100,81,'B+')");
			stmt.execute("INSERT INTO course VALUES(47,2022,100,90,40,80,69,'C+')");
			stmt.execute("INSERT INTO course VALUES(23,2023,100,90,100,80,93,'B+')");
			stmt.execute("INSERT INTO course VALUES(46,2023,100,10,99,78,68,'C+')");
			stmt.execute("INSERT INTO course VALUES(21,2024,100,50,80,90,75,'B+')");
			stmt.execute("INSERT INTO course VALUES(23,2024,100,99,50,100,80,'B+')");
			stmt.execute("INSERT INTO course VALUES(24,2024,100,52,90,90,80,'B+')");
			stmt.execute("INSERT INTO course VALUES(46,2024,100,18,52,100,56,'C+')");
			stmt.execute("INSERT INTO course VALUES(22,2021,100,18,90,100,71,'C+')");
			stmt.execute("INSERT INTO course VALUES(24,2025,100,40,95,80,76,'B+')");
			stmt.execute("INSERT INTO course VALUES(25,2025,100,40,77,80,69,'C+')");
			stmt.execute("INSERT INTO course VALUES(48,2025,100,52,40,78,57,'C+')");
			stmt.execute("INSERT INTO course VALUES(49,2025,100,99,38,90,73,'C+')");
			stmt.execute("INSERT INTO course VALUES(50,2025,100,52,50,100,66,'C+')");
			stmt.execute("INSERT INTO course VALUES(50,2023,100,78,70,90,79,'B+')");
		} catch (SQLException e) {
			System.out.println("�������ʱ�ȭ ���� :" + e);
		}
	}
}
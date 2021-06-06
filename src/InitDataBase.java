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
			
			/* 교수 */
			stmt.execute("INSERT INTO professor VALUES(1, '주경희', '집912', '02-3408-3106', 'chukh@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(2, '곽은주', '집813', '02-3408-3633', 'ejkwak@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(3, '박유하', '집714', '02-3408-3111', 'parkyh@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(4, '문종현', '우310', '02-3408-3953', 'entendu@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(5, '정영권', '집613', '02-6935-2526', 'peace1642@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(6, '이인숙', '집711', '02-3408-3130', 'inlee@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(7, '변창흠', '집507', '02-3408-3148', 'changbyeon@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(8, '손승혜', '집512', '02-3408-3706', 'shsohn@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(9, '이종은', '집510', '02-3408-3137', 'LJONGEUN@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(10, '김경원', '광427', '02-6935-2486', 'alexkkim@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(11, '이형룡', '광509', '02-3408-3717', 'hrlee@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(12, '유승석', '광608', '02-3408-3824', 'yss2@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(13, '문동호', '다105', '02-3408-3163', 'dhmoon@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(14, '오세헌', '영621', '02-6935-2469', 'seheon.oh@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(15, '신구', '영214', '02-3408-3215', 'shink@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(16, '김용휘', '영316', '02-3408-3228', 'kimyh@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(17, '강신정', '충1128', '02-3408-3943', 'sjkang@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(18, '황성빈', '충606A', '02-3408-3642', 'sbhwang@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(19, '진중현', '충215A', '02-3408-3897', 'jhchin@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(20, '권오진', '충923', '02-3408-3295', 'ojkwon@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(21, '신동일', '센825', '02-3408-3241', 'dshin@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(22, '김원일', '센625', '02-3408-3795', 'wikim@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(23, '김영갑', '센701', '02-6935-2424', 'alwaysgabi@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(24, '백성욱', '율601B', '02-3408-3797', 'sbaik@sejong.ac.kr')");
			stmt.execute("INSERT INTO professor VALUES(25, '김형석', '센606', '02-3408-3696', 'hyungkim@sejong.ac.kr')");
			
			/* 학과 */
			stmt.execute("INSERT INTO department VALUES(1, '국어국문학과', '02-3408-4301', '집908', 1)");
			stmt.execute("INSERT INTO department VALUES(2, '영어영문학과', '02-3408-3302', '집805', 2)");
			stmt.execute("INSERT INTO department VALUES(3, '일어일문학과', '02-3408-3303', '집715', 3)");
			stmt.execute("INSERT INTO department VALUES(4, '대양휴머니티칼리지', '02-3408-3929', '광828B', 4)");
			stmt.execute("INSERT INTO department VALUES(5, '역사학과', '02-3408-3305', '집915', 5)");
			stmt.execute("INSERT INTO department VALUES(6, '교육학과', '02-3408-3304', '집706', 6)");
			stmt.execute("INSERT INTO department VALUES(7, '행정학과', '02-3408-3308', '집604', 7)");
			stmt.execute("INSERT INTO department VALUES(8, '미디어커뮤니케이션학과', '02-3408-3307', '집506', 8)");
			stmt.execute("INSERT INTO department VALUES(9, '경제학과', '02-3408-3306', '광704B', 9)");
			stmt.execute("INSERT INTO department VALUES(10, '경영학과', '02-3408-3311', '광317', 10)");
			stmt.execute("INSERT INTO department VALUES(11, '호텔관광경영학과', '02-3408-3312', '광517', 11)");
			stmt.execute("INSERT INTO department  (12, '외식경영학과', '02-3408-3313', '광519', 12)");
			stmt.execute("INSERT INTO department VALUES(13, '수학전공', '02-3408-3315', '영313', 13)");
			stmt.execute("INSERT INTO department VALUES(14, '물리천문학과', '02-3408-3316', '영113', 14)");
			stmt.execute("INSERT INTO department VALUES(15, '화학과', '02-3408-3317', '영215', 15)");
			stmt.execute("INSERT INTO department VALUES(16, '식품생명공학', '02-3408-3319', '율407', 16)");
			stmt.execute("INSERT INTO department VALUES(17, '바이오융합공학', '02-3408-3334', '충612', 17)");
			stmt.execute("INSERT INTO department VALUES(18, '바이오산업자원공학', '02-3408-3435', '다211B', 18)");
			stmt.execute("INSERT INTO department VALUES(19, '체육학과', '02-3408-3661', '충611', 19)");
			stmt.execute("INSERT INTO department VALUES(20, '전자정보통신공학과', '02-3408-2546', '충1107', 20)");
			stmt.execute("INSERT INTO department VALUES(21, '컴퓨터공학과', '02-3408-3321', '센442', 21)");
			stmt.execute("INSERT INTO department VALUES(22, '소프트웨어학과', '02-3408-3667', '센402', 22)");
			stmt.execute("INSERT INTO department VALUES(23, '정보보호학과', '02-3408-4181', '센404', 23)");
			stmt.execute("INSERT INTO department VALUES(24, '데이터사이언스학과', '02-6935-2544', '센403', 24)");
			stmt.execute("INSERT INTO department VALUES(25, '지능기전공학부', '02-3408-3900', '센405', 25)");
			
			/* 학생 */
			stmt.execute("INSERT INTO student VALUES(2001, '김철수1', '서울특별시 광진구 군자동 1', '010-1111-2001', 'st2001@sju.ac.kr', '국민1000000000001', 1, NULL)");
			stmt.execute("INSERT INTO student VALUES(2002, '김철수2', '서울특별시 광진구 군자동 2', '010-1111-2002', 'st2002@sju.ac.kr', '국민1000000000002', 2, NULL)");
			stmt.execute("INSERT INTO student VALUES(2003, '김철수3', '서울특별시 광진구 군자동 3', '010-1111-2003', 'st2003@sju.ac.kr', '국민1000000000003', 3, NULL)");
			stmt.execute("INSERT INTO student VALUES(2004, '김철수4', '서울특별시 광진구 군자동 4', '010-1111-2004', 'st2004@sju.ac.kr', '국민1000000000004', 4, NULL)");
			stmt.execute("INSERT INTO student VALUES(2005, '김철수5', '서울특별시 광진구 군자동 5', '010-1111-2005', 'st2005@sju.ac.kr', '국민1000000000005', 5, NULL)");
			stmt.execute("INSERT INTO student VALUES(2006, '김철수6', '서울특별시 광진구 군자동 6', '010-1111-2006', 'st2006@sju.ac.kr', '국민1000000000006', 6, NULL)");
			stmt.execute("INSERT INTO student VALUES(2007, '김철수7', '서울특별시 광진구 군자동 7', '010-1111-2007', 'st2007@sju.ac.kr', '국민1000000000007', 7, NULL)");
			stmt.execute("INSERT INTO student VALUES(2008, '김철수8', '서울특별시 광진구 군자동 8', '010-1111-2008', 'st2008@sju.ac.kr', '국민1000000000008', 8, NULL)");
			stmt.execute("INSERT INTO student VALUES(2009, '김철수9', '서울특별시 광진구 군자동 9', '010-1111-2009', 'st2009@sju.ac.kr', '국민1000000000009', 9, NULL)");
			stmt.execute("INSERT INTO student VALUES(2010, '김철수10', '서울특별시 광진구 군자동 10', '010-1111-2010', 'st2010@sju.ac.kr', '국민1000000000010', 10, NULL)");
			stmt.execute("INSERT INTO student VALUES(2011, '김철수11', '서울특별시 광진구 군자동 11', '010-1111-2011', 'st2011@sju.ac.kr', '국민1000000000011', 11, NULL)");
			stmt.execute("INSERT INTO student VALUES(2012, '김철수12', '서울특별시 광진구 군자동 12', '010-1111-2012', 'st2012@sju.ac.kr', '국민1000000000012', 12, NULL)");
			stmt.execute("INSERT INTO student VALUES(2013, '김철수13', '서울특별시 광진구 군자동 13', '010-1111-2013', 'st2013@sju.ac.kr', '국민1000000000013', 13, NULL)");
			stmt.execute("INSERT INTO student VALUES(2014, '김철수14', '서울특별시 광진구 군자동 14', '010-1111-2014', 'st2014@sju.ac.kr', '국민1000000000014', 14, NULL)");
			stmt.execute("INSERT INTO student VALUES(2015, '김철수15', '서울특별시 광진구 군자동 15', '010-1111-2015', 'st2015@sju.ac.kr', '국민1000000000015', 15, NULL)");
			stmt.execute("INSERT INTO student VALUES(2016, '김철수26', '서울특별시 광진구 군자동 26', '010-1111-2026', 'st2016@sju.ac.kr', '국민1000000000016', 16, NULL)");
			stmt.execute("INSERT INTO student VALUES(2017, '김철수27', '서울특별시 광진구 군자동 27', '010-1111-2027', 'st2017@sju.ac.kr', '국민1000000000017', 17, NULL)");
			stmt.execute("INSERT INTO student VALUES(2018, '김철수28', '서울특별시 광진구 군자동 28', '010-1111-2028', 'st2018@sju.ac.kr', '국민1000000000018', 18, NULL)");
			stmt.execute("INSERT INTO student VALUES(2019, '김철수29', '서울특별시 광진구 군자동 29', '010-1111-2029', 'st2019@sju.ac.kr', '국민1000000000019', 19, NULL)");
			stmt.execute("INSERT INTO student VALUES(2020, '김철수30', '서울특별시 광진구 군자동 30', '010-1111-2030', 'st2020@sju.ac.kr', '국민1000000000020', 20, NULL)");
			stmt.execute("INSERT INTO student VALUES(2021, '김철수31', '서울특별시 광진구 군자동 31', '010-1111-2031', 'st2021@sju.ac.kr', '국민1000000000021', 21, NULL)");
			stmt.execute("INSERT INTO student VALUES(2022, '김철수32', '서울특별시 광진구 군자동 32', '010-1111-2032', 'st2022@sju.ac.kr', '국민1000000000022', 22, NULL)");
			stmt.execute("INSERT INTO student VALUES(2023, '김철수33', '서울특별시 광진구 군자동 33', '010-1111-2033', 'st2023@sju.ac.kr', '국민1000000000023', 23, NULL)");
			stmt.execute("INSERT INTO student VALUES(2024, '김철수34', '서울특별시 광진구 군자동 34', '010-1111-2034', 'st2024@sju.ac.kr', '국민1000000000024', 24, NULL)");
			stmt.execute("INSERT INTO student VALUES(2025, '김철수35', '서울특별시 광진구 군자동 35', '010-1111-2035', 'st2025@sju.ac.kr', '국민1000000000025', 25, NULL)");
			stmt.execute("INSERT INTO student VALUES(2101, '김영희1', '서울특별시 광진구 군자동 36', '010-1111-2036', 'st2101@sju.ac.kr', '신한1000000000001', 1, NULL)");
			stmt.execute("INSERT INTO student VALUES(2102, '김영희2', '서울특별시 광진구 군자동 37', '010-1111-2037', 'st2102@sju.ac.kr', '신한1000000000002', 2, NULL)");
			stmt.execute("INSERT INTO student VALUES(2103, '김영희3', '서울특별시 광진구 군자동 38', '010-1111-2038', 'st2103@sju.ac.kr', '신한1000000000003', 3, NULL)");
			stmt.execute("INSERT INTO student VALUES(2104, '김영희4', '서울특별시 광진구 군자동 39', '010-1111-2039', 'st2104@sju.ac.kr', '신한1000000000004', 4, NULL)");
			stmt.execute("INSERT INTO student VALUES(2105, '김영희5', '서울특별시 광진구 군자동 40', '010-1111-2040', 'st2105@sju.ac.kr', '신한1000000000005', 5, NULL)");
			stmt.execute("INSERT INTO student VALUES(2106, '김영희6', '서울특별시 광진구 군자동 41', '010-1111-2041', 'st2106@sju.ac.kr', '신한1000000000006', 6, NULL)");
			stmt.execute("INSERT INTO student VALUES(2107, '김영희7', '서울특별시 광진구 군자동 42', '010-1111-2042', 'st2107@sju.ac.kr', '신한1000000000007', 22, NULL)");
			stmt.execute("INSERT INTO student VALUES(2108, '김영희8', '서울특별시 광진구 군자동 43', '010-1111-2043', 'st2108@sju.ac.kr', '신한1000000000008', 23, NULL)");
			stmt.execute("INSERT INTO student VALUES(2109, '김영희9', '서울특별시 광진구 군자동 44', '010-1111-2044', 'st2109@sju.ac.kr', '신한1000000000009', 24, NULL)");
			stmt.execute("INSERT INTO student VALUES(2110, '김영희10', '서울특별시 광진구 군자동 45', '010-1111-2045', 'st2110@sju.ac.kr', '신한1000000000010', 25, NULL)");
			
			/* 등록 */
			stmt.execute("INSERT INTO tuition VALUES(2001, 2020, 1, 4000000, 4000000, '2020-02-22', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2002, 2020, 1, 2870000, 2870000, '2020-02-23', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2003, 2020, 1, 3900000, 3900000, '2020-02-24', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2004, 2020, 1, 4000000, 4000000, '2020-02-25', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2005, 2020, 1, 3900000, 3900000, '2020-02-26', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2006, 2020, 1, 2100000, 2100000, '2020-02-27', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2007, 2020, 1, 3000000, 3000000, '2020-02-22', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2008, 2020, 1, 3100000, 3100000, '2020-02-23', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2009, 2020, 1, 3000000, 3000000, '2020-02-24', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2010, 2020, 1, 2100000, 2100000, '2020-02-25', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2011, 2020, 1, 4000000, 4000000, '2020-02-26', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2012, 2020, 1, 2870000, 2870000, '2020-02-27', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2013, 2020, 1, 3900000, 3900000, '2020-02-22', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2014, 2020, 1, 3100000, 3100000, '2020-02-22', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2015, 2020, 1, 3000000, 3000000, '2020-02-22', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2016, 2020, 1, 3000000, 3000000, '2020-02-22', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2017, 2020, 1, 3100000, 3100000, '2020-02-23', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2018, 2020, 1, 3000000, 3000000, '2020-02-24', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2019, 2020, 1, 2100000, 2100000, '2020-02-25', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2020, 2020, 1, 4000000, 4000000, '2020-02-26', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2021, 2020, 1, 2870000, 2870000, '2020-02-27', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2022, 2020, 1, 3900000, 3900000, '2020-02-22', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2023, 2020, 1, 3000000, 3000000, '2020-02-23', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2024, 2020, 1, 3100000, 3100000, '2020-02-24', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2025, 2020, 1, 3000000, 3000000, '2020-02-25', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2001, 2020, 2, 2100000, 2100000, '2020-08-02', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2002, 2020, 2, 4000000, 4000000, '2020-08-03', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2003, 2020, 2, 2870000, 2870000, '2020-08-04', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2004, 2020, 2, 2870000, 2870000, '2020-08-05', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2005, 2020, 2, 3900000, 3900000, '2020-08-06', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2006, 2020, 2, 3000000, 3000000, '2020-08-07', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2007, 2020, 2, 3100000, 3100000, '2020-08-08', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2008, 2020, 2, 3000000, 3000000, '2020-08-09', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2009, 2020, 2, 2100000, 2100000, '2020-08-10', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2010, 2020, 2, 4000000, 4000000, '2020-08-11', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2011, 2020, 2, 2870000, 2870000, '2020-08-02', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2012, 2020, 2, 3900000, 3900000, '2020-08-03', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2013, 2020, 2, 3000000, 3000000, '2020-08-04', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2014, 2020, 2, 3100000, 3100000, '2020-08-05', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2015, 2020, 2, 3000000, 3000000, '2020-08-06', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2016, 2020, 2, 2100000, 2100000, '2020-08-07', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2017, 2020, 2, 4000000, 4000000, '2020-08-08', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2018, 2020, 2, 2870000, 2870000, '2020-08-09', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2019, 2020, 2, 2870000, 2870000, '2020-08-10', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2020, 2020, 2, 3900000, 3900000, '2020-08-11', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2021, 2020, 2, 3000000, 3000000, '2020-08-12', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2022, 2020, 2, 3100000, 3100000, '2020-08-13', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2023, 2020, 2, 3000000, 3000000, '2020-08-14', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2024, 2020, 2, 2100000, 2100000, '2020-08-15', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2025, 2020, 2, 4000000, 4000000, '2020-08-16', '1학년2학기')");
			stmt.execute("INSERT INTO tuition VALUES(2001, 2021, 1, 2870000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2002, 2021, 1, 3900000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2003, 2021, 1, 4000000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2004, 2021, 1, 3900000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2005, 2021, 1, 2100000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2006, 2021, 1, 2100000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2007, 2021, 1, 3000000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2008, 2021, 1, 3100000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2009, 2021, 1, 3000000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2010, 2021, 1, 2100000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2011, 2021, 1, 4000000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2012, 2021, 1, 2870000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2013, 2021, 1, 3900000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2014, 2021, 1, 3100000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2015, 2021, 1, 2100000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2016, 2021, 1, 3000000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2017, 2021, 1, 3100000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2018, 2021, 1, 3000000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2019, 2021, 1, 2100000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2020, 2021, 1, 4000000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2021, 2021, 1, 2870000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2022, 2021, 1, 3900000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2023, 2021, 1, 3100000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2024, 2021, 1, 3000000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2025, 2021, 1, 3000000, 0, '', '2학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2101, 2021, 1, 3100000, 0, '', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2102, 2021, 1, 3000000, 0, '', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2103, 2021, 1, 2100000, 0, '', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2104, 2021, 1, 4000000, 0, '', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2105, 2021, 1, 2870000, 0, '', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2106, 2021, 1, 3900000, 0, '', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2107, 2021, 1, 3000000, 0, '', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2108, 2021, 1, 3100000, 0, '', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2109, 2021, 1, 3000000, 0, '', '1학년1학기')");
			stmt.execute("INSERT INTO tuition VALUES(2110, 2021, 1, 4000000, 0, '', '1학년1학기')");
		
			/* 교수 소속 */
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
			
			/* 지도관계 */
			stmt.execute("INSERT INTO tutoring VALUES(2001, 1, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2002, 2, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2003, 3, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2004, 4, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2005, 5, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2006, 6, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2007, 7, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2008, 8, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2009, 9, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2010, 10, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2011, 11, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2012, 12, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2013, 13, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2014, 14, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2015, 15, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2016, 16, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2017, 17, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2018, 18, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2019, 19, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2020, 20, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2021, 21, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2022, 22, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2023, 23, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2024, 24, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2025, 25, '2학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2101, 21, '1학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2102, 11, '1학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2103, 12, '1학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2104, 13, '1학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2105, 21, '1학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2106, 15, '1학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2107, 16, '1학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2108, 17, '1학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2109, 18, '1학년1학기')");
			stmt.execute("INSERT INTO tutoring VALUES(2110, 21, '1학년1학기')");
			
			/* 동아리 */
			stmt.execute("INSERT INTO club VALUES(1, '동아리1', 2, '학601', 1, 2001)");
			stmt.execute("INSERT INTO club VALUES(2, '동아리2', 2, '학602', 2, 2002)");
			stmt.execute("INSERT INTO club VALUES(3, '동아리3', 2, '학603', 3, 2003)");
			stmt.execute("INSERT INTO club VALUES(4, '동아리4', 2, '학604', 4, 2004)");
			stmt.execute("INSERT INTO club VALUES(5, '동아리5', 2, '학605', 5, 2005)");
			stmt.execute("INSERT INTO club VALUES(6, '동아리6', 2, '학501', 6, 2006)");
			stmt.execute("INSERT INTO club VALUES(7, '동아리7', 2, '학502', 7, 2007)");
			stmt.execute("INSERT INTO club VALUES(8, '동아리8', 2, '학503', 8, 2008)");
			stmt.execute("INSERT INTO club VALUES(9, '동아리9', 2, '학504', 9, 2009)");
			stmt.execute("INSERT INTO club VALUES(10, '동아리10', 2, '학505', 10, 2010)");
			stmt.execute("INSERT INTO club VALUES(11, '동아리11', 2, '학401', 11, 2011)");
			stmt.execute("INSERT INTO club VALUES(12, '동아리12', 2, '학402', 12, 2012)");
			stmt.execute("INSERT INTO club VALUES(13, '동아리13', 2, '학403', 13, 2013)");
			stmt.execute("INSERT INTO club VALUES(14, '동아리14', 2, '학404', 14, 2014)");
			stmt.execute("INSERT INTO club VALUES(15, '동아리15', 2, '학405', 15, 2015)");
			stmt.execute("INSERT INTO club VALUES(16, '동아리16', 2, '학301', 16, 2016)");
			stmt.execute("INSERT INTO club VALUES(17, '동아리17', 2, '학302', 17, 2017)");
			stmt.execute("INSERT INTO club VALUES(18, '동아리18', 2, '학303', 18, 2018)");
			stmt.execute("INSERT INTO club VALUES(19, '동아리19', 2, '학304', 19, 2019)");
			stmt.execute("INSERT INTO club VALUES(20, '동아리20', 2, '학305', 20, 2020)");
			stmt.execute("INSERT INTO club VALUES(21, '동아리21', 1, '학201', 21, 2021)");
			stmt.execute("INSERT INTO club VALUES(22, '동아리22', 1, '학202', 22, 2022)");
			stmt.execute("INSERT INTO club VALUES(23, '동아리23', 1, '학203', 23, 2023)");
			stmt.execute("INSERT INTO club VALUES(24, '동아리24', 1, '학204', 24, 2024)");
			stmt.execute("INSERT INTO club VALUES(25, '동아리25', 1, '학205', 25, 2025)");
			
			/* 동아리원 */
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
			
			/* 강의 */
			stmt.execute("INSERT INTO lecture VALUES(1, 1, '한국문학의이해', '금', '15,16,17,18,19,20', '', '', 3, 3, '집301', 1, 1, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(2, 1, '영어속의논리', '월', '3,4,5', '수', '3,4,5', 3, 3, '집403', 2, 2, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(3, 1, '일본어문법', '월', '15,16,17', '수', '15,16,17', 3, 3, '집404A', 3, 3, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(4, 1, '창업과기업가정신', '수', '12,13,14', '', '', 1, 1.5, '학대공연장', 4, 4, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(5, 1, '역사학개론', '화', '12,13,14', '목', '12,13,14', 3, 3, '집307', 5, 5, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(6, 1, '교육학의탐구', '화', '9,10,11,12,13,14', '', '', 3, 3, '집407', 6, 6, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(7, 1, '정부와행정', '금', '3,4,5,6,7,8', '', '', 3, 3, '집311', 7, 7, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(8, 1, '방송기획', '수', '12,13,14,15,16,17', '', '', 3, 3, '집501B', 8, 8, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(9, 1, '경제원론1', '월', '9,10,11,12,13,14', '', '', 3, 3, '광720', 9, 9, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(10, 1, '손해보험', '금', '3,4,5,6,7,8', '', '', 3, 3, '광208', 10, 10, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(11, 1, '관광경영론', '월', '12,13,14', '수', '12,13,14', 3, 3, '광617', 11, 11, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(12, 1, '조리원리', '화', '15,16,17,18,19,20', '', '', 3, 3, '광428', 12, 12, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(13, 1, '해석학개론1', '월', '6,7,8', '수', '6,7,8', 3, 3, '광211', 13, 13, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(14, 1, '역학1', '월', '6,7,8', '수', '6,7,8', 3, 3, '영405', 14, 14, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(15, 1, '유기화학1', '화', '9,10,11', '목', '9,10,11', 3, 3, '영401', 15, 15, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(16, 1, '유기화학', '월', '15,16,17', '수', '15,16,17', 3, 3, '충101', 16, 16, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(17, 1, '일반미생물학및실험', '화', '17,18,19,20', '목', '17,18,19,20', 3, 4, '영B202', 17, 17, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(18, 1, '유기화학', '화', '13,14,15', '목', '13,14,15', 3, 3, '이205', 18, 18, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(19, 1, '체육원리', '화', '16,14,15,16,17', '', '', 3, 3, '용102', 19, 19, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(20, 1, '전기회로', '월', '12,13,14', '수', '12,13,14', 3, 3, '센B105A', 20, 20, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(21, 1, '데이터베이스', '화', '3,4,5', '목', '3,4,5', 3, 3, '센B103', 21, 21, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(22, 1, '자료구조및실습', '월', '9,10,11,21,22,23,24', '수', '9,10,11', 4, 5, '센B209', 22, 22, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(23, 1, '정보보호와보안의기초', '월', '12,13,14', '수', '12,13,14', 3, 3, '센B204', 23, 23, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(24, 1, '데이터분석개론', '금', '3,4,5,6,7,8', '', '', 3, 3, '센B110', 24, 24, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(25, 1, '자료구조및실습', '수', '9,10,11,18,19,20,21', '목', '9,10,11', 4, 5, '센B106', 25, 25, 2020, 1)");
			stmt.execute("INSERT INTO lecture VALUES(26, 1, '매체언어론', '화', '9,10,11,12,13,14', '', '', 3, 3, '집503', 1, 1, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(27, 1, '미국사회와문학', '금', '12,13,14,15,16,17', '', '', 3, 3, '집403', 2, 2, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(28, 1, '일본어회화', '월', '18,19,20', '수', '18,19,20', 3, 3, '집404A', 3, 3, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(29, 1, '세종사회봉사1', '', '', '', '', 1, 1, '', 4, 4, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(30, 1, '문화유적현장실습1', '금', '21,22,23,24', '', '', 1, 2, '집307', 5, 5, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(31, 1, '교육통계', '수', '15,16,17,18,19,20', '', '', 3, 3, '집407', 6, 6, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(32, 1, '관료제도론', '화', '18,19,20', '목', '18,19,20', 3, 3, '집311', 7, 7, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(33, 1, '대화와가십', '월', '15,16,17,18,19,20', '', '', 3, 3, '집501B', 8, 8, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(34, 1, '경제원론2', '금', '3,4,5,6,7,8', '', '', 3, 3, '광720', 9, 9, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(35, 1, '생산관리', '월', '12,13,14', '수', '12,13,14', 3, 3, '광431', 10, 10, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(36, 1, '관광법규', '금', '3,4,5,6,7,8', '', '', 3, 3, '광206', 11, 11, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(37, 1, '와인개론', '화', '9,10,11', '목', '9,10,11', 3, 3, '광428', 12, 12, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(38, 1, '확률론', '월', '6,7,8', '수', '6,7,8', 3, 3, '다130', 13, 13, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(39, 1, '은하천문학', '화', '15,16,17', '목', '15,16,17', 3, 3, '영601', 14, 14, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(40, 1, '금속착물화학', '화', '9,10,11', '목', '9,10,11', 3, 3, '영401', 15, 15, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(41, 1, '식품개발실험', '목', '12,13,14,15,16,17,18,19', '', '', 3, 4, '광714', 16, 16, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(42, 1, '스트레스생물학', '목', '3,4,5,6,7,8', '', '', 3, 3, '광712', 17, 17, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(43, 1, '식물육종학및실험', '화', '6,7,8', '목', '6,7,8', 3, 3, '이205', 18, 18, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(44, 1, '체육사', '수', '5,6,7,8,9,10', '', '', 3, 3, '용101', 19, 19, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(45, 1, '기초설계', '화', '9,10,11,12,13,14', '', '', 3, 3, '충905', 20, 20, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(46, 1, '고급C프로그래밍및실습', '화', '12,13,14', '목', '12,13,14', 3, 3, '센B201', 21, 21, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(47, 1, '디지털사운드', '화', '15,16,17', '목', '15,16,17', 3, 3, '센102', 22, 22, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(48, 1, '시스템프로그래밍', '화', '14,15,16', '목', '14,15,16', 3, 3, '센B105', 23, 23, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(49, 1, '데이터시각화', '화', '9,10,11', '목', '9,10,11', 3, 3, '센B110', 24, 24, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(50, 1, '동역학', '월', '12,13,14', '화', '6,7,8', 3, 3, '이201', 25, 25, 2020, 2)");
			stmt.execute("INSERT INTO lecture VALUES(51, 1, '한국문학의이해', '금', '15,16,17,18,19,20', '', '', 3, 3, '집301', 1, 1, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(52, 1, '국어문법론', '월', '9,10,11,12,13,14', '', '', 3, 3, '집301', 1, 1, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(53, 1, '영어속의논리', '월', '6,7,8', '수', '6,7,8', 3, 3, '집403', 2, 2, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(54, 1, '영문법의이해', '금', '6,7,8,9,10,11', '', '', 3, 3, '집403', 2, 2, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(55, 1, '일본어문법', '월', '15,16,17', '수', '15,16,17', 3, 3, '집404A', 3, 3, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(56, 1, '일본어작문', '화', '9,10,11', '목', '9,10,11', 3, 3, '집502', 3, 3, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(57, 1, '문제해결을위한글쓰기와발표', '금', '7,8,9,10', '', '', 3, 2, '이202', 4, 4, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(58, 1, '역사학개론', '화', '12,13,14', '목', '12,13,14', 3, 3, '집307', 5, 5, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(59, 1, '인류의선사문화', '금', '9,10,11,12,13,14', '', '', 3, 3, '집307', 5, 5, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(60, 1, '교육학의탐구', '화', '9,10,11,12,13,14', '', '', 3, 3, '집407', 6, 6, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(61, 1, '교육철학의이해', '금', '9,10,11,12,13,14', '', '', 3, 3, '집407', 6, 6, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(62, 1, '정부와행정', '금', '3,4,5,6,7,8', '', '', 3, 3, '집311', 7, 7, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(63, 1, '정책론', '화', '18,19,20', '목', '18,19,20', 3, 3, '집501B', 7, 7, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(64, 1, '방송기획', '수', '12,13,14,15,16,17', '', '', 3, 3, '집501B', 8, 8, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(65, 1, '광고매체기획', '화', '3,4,5,6,7,8', '', '', 3, 3, '집501B', 8, 8, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(66, 1, '경제원론1', '월', '9,10,11,12,13,14', '', '', 3, 3, '광720', 9, 9, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(67, 1, '손해보험', '금', '3,4,5,6,7,8', '', '', 3, 3, '광208', 10, 10, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(68, 1, '관광경영론', '월', '12,13,14', '수', '12,13,14', 3, 3, '광617', 11, 11, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(69, 1, '호텔경영론', '목', '15,16,17,18,19,20', '', '', 3, 3, '광617', 11, 11, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(70, 1, '조리원리', '화', '15,16,17,18,19,20', '', '', 3, 3, '광428', 12, 12, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(71, 1, '주류학', '화', '6,7,8', '목', '6,7,8', 3, 3, '광428', 12, 12, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(72, 1, '선형대수학', '화', '6,7,8', '목', '6,7,8', 3, 3, '광211', 13, 13, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(73, 1, '조합론', '화', '15,16,17', '목', '15,16,17', 3, 3, '다130', 13, 13, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(74, 1, '역학1', '월', '6,7,8', '수', '6,7,8', 3, 3, '영405', 14, 14, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(75, 1, '역학2', '월', '15,16,17', '수', '15,16,17', 3, 3, '영405', 14, 14, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(76, 1, '유기화학1', '화', '9,10,11', '목', '9,10,11', 3, 3, '영401', 15, 15, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(77, 1, '생화학', '화', '15,16,17', '목', '15,16,17', 3, 3, '영402', 15, 15, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(78, 1, '유기화학', '월', '15,16,17', '수', '15,16,17', 3, 3, '충101', 16, 16, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(79, 1, '일반미생물학및실험', '화', '6,7,8,9', '목', '3,4,5,6', 4, 3, '율B04', 16, 16, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(80, 1, '일반미생물학및실험', '화', '17,18,19,20', '목', '17,18,19,20', 3, 4, '영B202', 17, 17, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(81, 1, '유전학', '월', '18,19,20', '수', '18,19,20', 3, 3, '영B202', 17, 17, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(82, 1, '일반미생물학및실험', '수', '12,13,14,15', '금', '12,13,14,15', 3, 4, '세203', 18, 18, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(83, 1, '유기화학', '화', '13,14,15', '목', '13,14,15', 3, 3, '이205', 18, 18, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(84, 1, '골프', '월', '15,16,17,18,19,20', '', '', 3, 3, '용102', 19, 19, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(85, 1, '전기회로', '월', '12,13,14', '수', '12,13,14', 3, 3, '센B105A', 20, 20, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(86, 1, '데이터베이스', '화', '3,4,5', '목', '3,4,5', 3, 3, '센B103', 21, 21, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(87, 1, '수치해석', '화', '12,13,14', '목', '12,13,14', 3, 3, '센B102', 22, 22, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(88, 1, '자료구조및실습', '월', '15,16,17', '수', '15,16,17,21,22,23,24', 4, 5, '센B102', 22, 22, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(89, 1, '컴퓨터네트워크', '화', '12,13,14', '목', '12,13,14', 3, 3, '센B105', 23, 23, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(90, 1, '정수론', '화', '3,4,5', '목', '3,4,5', 3, 3, '센B102', 23, 23, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(91, 1, '데이터분석개론', '금', '3,4,5,6,7,8', '', '', 3, 3, '센B110', 24, 24, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(92, 1, '의사결정분석', '화', '15,16,17', '목', '15,16,17', 3, 3, '센B110', 24, 24, 2021, 1)");
			stmt.execute("INSERT INTO lecture VALUES(93, 1, '자료구조및실습', '수', '9,10,11,18,19,20,21', '목', '9,10,11', 4, 5, '센B106', 25, 25, 2021, 1)");
			
			/* 수강 */
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
			System.out.println("데이터초기화 실패 :" + e);
		}
	}
}
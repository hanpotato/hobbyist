package com.hobbyist.notice.model.dao;

import static common.JdbcTemplate.close;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.hobbyist.notice.model.vo.Notice;
import com.hobbyist.writer.model.dao.WriterDao;
import com.hobbyist.writer.model.vo.WriterEnroll;

public class NoticeDao {

	private Properties prop = new Properties();
	
	public NoticeDao() {
		String fileName = WriterDao.class.getResource("/sql/notice/notice.properties").getPath();
		try {
			prop.load(new FileReader(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int insertNotice(Connection conn, Notice no) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("insertNotice");
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, no.getNoticeSort());
			pstmt.setString(2, no.getNoticeTitle());
			pstmt.setString(3, no.getNoticeWriter());
			pstmt.setString(4, no.getNoticeContent());
			pstmt.setString(5, no.getNoticeFilenameOriginal());
			pstmt.setString(6, no.getNoticeFilenameRenamed());
			pstmt.setString(7, no.getNoticeImgnameOriginal());
			pstmt.setString(8, no.getNoticeImgnameRenamed());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return result;
	}

	// 검색결과에 다른 리스트 갯수 가져오기
	public int searchCount(Connection conn, String keyword) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		String sql = prop.getProperty("searchCount");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + keyword + "%");
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 리스트 최근 등록일순으로 가져오기
	public List<Notice> descEnroll(Connection conn, String keyword, int cPage, int numPerPage) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Notice> list = new ArrayList();
		String sql = prop.getProperty("descEnroll");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + keyword + "%");
			pstmt.setInt(2, (cPage-1)*numPerPage+1);
			pstmt.setInt(3, cPage*numPerPage);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Notice no = new Notice();
				
				no.setNoticeNo(rs.getInt("notice_no"));
				no.setNoticeSort(rs.getString("notice_sort"));
				no.setNoticeTitle(rs.getString("notice_title"));
				no.setNoticeWriter(rs.getString("notice_writer"));
				no.setNoticeContent(rs.getString("notice_content"));
				no.setNoticeDate(rs.getDate("notice_date"));
				no.setNoticeFilenameOriginal(rs.getString("notice_filename_original"));
				no.setNoticeFilenameRenamed(rs.getString("notice_filename_renamed"));
				no.setNoticeImgnameOriginal(rs.getString("notice_imgname_original"));
				no.setNoticeImgnameRenamed(rs.getString("notice_imgname_renamed"));
				no.setNoticeReadcount(rs.getInt("notice_readcount"));
				no.setNoticeStatus(rs.getString("notice_status"));
				list.add(no);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return list;
	}
	
	
}

package com.hobbyist.admin.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.hobbyist.board.model.service.BoardService;
import com.hobbyist.board.model.vo.Board;
import com.oreilly.servlet.MultipartRequest;

import common.rename.MyFileRenamePolicy;

/**
 * Servlet implementation class BoardReviseEndServlet
 */
@WebServlet("/admin/community/adminCommunityReviseEnd")
public class AdminCommunityReviseEndServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminCommunityReviseEndServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(!ServletFileUpload.isMultipartContent(request)) {
			request.setAttribute("msg", "경고! 접근방식에 문제가 발생하였습니다.");
			request.setAttribute("loc", "/admin/community/adminCommunityList");
			request.getRequestDispatcher("/views/common/msg.jsp").forward(request, response);
		}
		
		int boardNo = Integer.parseInt(request.getParameter("boardNo"));
		
		String dir = getServletContext().getRealPath("/upload");
		String filePath = dir + File.separator + "board";
		
		int maxSize = 10*1024*1024;
		
		MultipartRequest mr = new MultipartRequest(request, filePath, maxSize, "UTF-8", new MyFileRenamePolicy());
		
		Board b = new Board();
		
		b.setBoardNo(boardNo);
		b.setBoardTitle(mr.getParameter("title"));
		b.setBoardWriter(mr.getParameter("writer"));
		b.setBoardContent(mr.getParameter("content"));
		b.setBoardOriginalFileName(mr.getOriginalFileName("up_file"));
		b.setBoardReNamedFileName(mr.getFilesystemName("up_file"));
		
		int result = new BoardService().updateBoard(b);
		
		String msg = "";
		String loc = "/admin/community/adminCommunityList";
		String view = "/views/common/msg.jsp";
		
		if(result > 0) {
			msg = "게시물이 수정되었습니다.";
		} else {
			msg = "게시물 수정에 실패하였습니다.";
		}
		
		request.setAttribute("msg", msg);
		request.setAttribute("loc", loc);
		request.getRequestDispatcher(view).forward(request, response);
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
package com.kiaev.client.board;

import com.kiaev.client.board.Board;
import com.kiaev.client.board.BoardService;
import com.kiaev.client.login.Login;
import com.kiaev.client.member.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // 이 클래스가 웹 요청(주소)을 처리하는 컨트롤러임을 명시합니다.
@RequestMapping("/board") // 공통 주소인 /board 를 맨 앞에 붙여줍니다.
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;

	// 1. 게시판 목록 화면 조회 (명세서의 GET /board/list)
	/*
	 * @GetMapping("/list") public String boardList(Pageable pageable, Model model)
	 * { // 서비스에서 페이징된 게시글 목록을 가져옵니다. Page<Board> boardPage =
	 * boardService.getVisibleBoardList(pageable);
	 * 
	 * // Model에 데이터를 담고, 페이지 정보를 전달합니다. model.addAttribute("boardPage", boardPage);
	 * 
	 * // boardList.html 페이지로 데이터를 전달하여 화면을 렌더링합니다. return "client/board/boardList";
	 * }
	 */
	/*
	 * @GetMapping("/list") public ResponseEntity<List<Board>> getBoardList(Pageable
	 * pageable) { // 페이징된 게시글 목록을 가져옵니다. Page<Board> boardPage =
	 * boardService.getVisibleBoardList(pageable);
	 * 
	 * // 페이징된 게시글 목록을 ResponseEntity로 반환 return
	 * ResponseEntity.ok(boardPage.getContent()); }
	 */

	// 1. 게시판 목록 화면 조회 (주석 해제 및 수정)
	@GetMapping("/list")
	public String boardList(@PageableDefault(size = 5) Pageable pageable, Model model) {
		// 서비스에서 페이징된 게시글 목록을 가져옵니다.
		Page<Board> boardPage = boardService.getVisibleBoardList(pageable);

		// Model에 데이터를 담습니다.

		// HTML의 th:each="board : ${boardList}" 를 위해 content를 따로 담아줍니다.
		model.addAttribute("boardList", boardPage.getContent());
		// 하단 페이지 네비게이션을 위해 page 객체도 담아줍니다.
		model.addAttribute("boardPage", boardPage);

		return "client/board/boardList";
	}

	// 2. 게시글 상세 화면 조회 (명세서의 GET /board/detail)
	@GetMapping("/detail")
	public String boardDetail(@RequestParam("boardNo") Long boardNo, Model model) {
		Board board = boardService.getBoardDetail(boardNo);
		model.addAttribute("board", board);
		return "client/board/boardDetail";
	}

	// 3. 글 작성 폼 화면 이동 (명세서의 GET /board/write)
	@GetMapping("/write")
	public String boardWriteForm() {
		return "client/board/boardWrite";
	}

	/*
	// 4. 실제 글 작성 처리 (명세서의 POST /board/write)
	@PostMapping("/write")
	public String boardWriteProcess(@ModelAttribute Board board, HttpSession session) { // session 추가
		Login loginUser = (Login) session.getAttribute("loginUser"); // 추가

		// 로그인한 회원번호를 게시글에 저장
		if (loginUser != null) {
			board.setMemberNo(loginUser.getMemberNo()); // 추가
		}

		boardService.saveBoard(board);
		// 저장이 끝나면 다시 목록 화면으로 알아서 이동(Redirect) 시킵니다.
		return "redirect:/board/list";
	}
	*/
	// 4. 실제 글 작성 처리
    @PostMapping("/write")
    public String boardWriteProcess(@ModelAttribute Board board, HttpSession session) {
        // [중요] 세션에서 로그인한 회원 정보를 꺼냅니다.
    	Login loginUser = (Login) session.getAttribute("loginUser");
        
        // [중요] 게시글 객체에 로그인한 회원의 번호를 심어줍니다.
        // 그래야 나중에 마이페이지에서 "내 글"만 골라올 수 있어요!
    	if (loginUser != null) {
    		board.setMemberNo(loginUser.getMemberNo());
    		}

        boardService.saveBoard(board);
        return "redirect:/board/list";
    }

	// 5. 글 수정 폼 이동  (인터셉터가 로그인 체크함)
	@GetMapping("/edit")
	public String boardEditForm(@RequestParam("boardNo") Long boardNo, Model model) {
		// 1. 기존에 작성된 게시글 데이터를 DB에서 불러옵니다. (상세 조회 로직 재사용)
		Board board = boardService.getBoardDetail(boardNo);

		// 2. 불러온 데이터를 화면에 뿌려주기 위해 Model에 담습니다.
		model.addAttribute("board", board);

		// 3. 수정용 HTML 폼 파일(boardEdit.html)로 이동합니다.
		return "client/board/boardEdit";
	}

	/*
	// 6. 실제 게시글 수정 처리 (POST)
	@PostMapping("/update")
	public String boardUpdateProcess(@ModelAttribute Board board) {
		// 1. 서비스의 수정 로직을 실행합니다.
		boardService.updateBoard(board);

		// 2. 수정이 완벽하게 끝나면, 방금 수정한 그 글의 상세 페이지로 다시 휙! 하고 돌아가게 합니다.
		return "redirect:/board/detail?boardNo=" + board.getBoardNo();
	}
	*/
	
	// 6. 실제 게시글 수정 처리
    @PostMapping("/update")
    public String boardUpdateProcess(@ModelAttribute Board board, HttpSession session) {
    	Login loginUser = (Login) session.getAttribute("loginUser");
        
        if (loginUser != null) {
            board.setMemberNo(loginUser.getMemberNo());
        }
        
        boardService.updateBoard(board);
        return "redirect:/board/detail?boardNo=" + board.getBoardNo();
    }

	// 7. 게시글 삭제 처리 (POST)
	@PostMapping("/delete")
	public String boardDelete(@RequestParam("boardNo") Long boardNo) {
		// 1. 서비스에게 "이 번호의 글을 지워줘!" 라고 명령합니다.
		boardService.deleteBoard(boardNo);

		// 2. 삭제가 끝나면 알아서 게시판 목록 화면으로 돌아가게 합니다.
		return "redirect:/board/list";
	}

}
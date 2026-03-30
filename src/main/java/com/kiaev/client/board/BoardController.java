package com.kiaev.client.board;

import com.kiaev.client.board.Board;
import com.kiaev.client.board.BoardService;
import lombok.RequiredArgsConstructor;
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
    @GetMapping("/list")
    public String boardList(Model model) {
        // 서비스에게 "DB에서 게시글 다 가져와!" 라고 시킵니다.
        List<Board> list = boardService.getBoardList();
        
        // 가져온 데이터를 화면(HTML)으로 전달하기 위해 Model에 담습니다.
        model.addAttribute("boardList", list);
        
        // 보여줄 HTML 파일의 경로를 반환합니다. 
        // (src/main/resources/templates/client/board/boardList.html 을 의미함)
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

    // 4. 실제 글 작성 처리 (명세서의 POST /board/write)
    @PostMapping("/write")
    public String boardWriteProcess(@ModelAttribute Board board) {
        boardService.saveBoard(board);
        // 저장이 끝나면 다시 목록 화면으로 알아서 이동(Redirect) 시킵니다.
        return "redirect:/board/list";
    }
    
    @GetMapping("/edit")
    public String boardEditForm(@RequestParam("boardNo") Long boardNo, Model model) {
        // 1. 기존에 작성된 게시글 데이터를 DB에서 불러옵니다. (상세 조회 로직 재사용)
        Board board = boardService.getBoardDetail(boardNo);
        
        // 2. 불러온 데이터를 화면에 뿌려주기 위해 Model에 담습니다.
        model.addAttribute("board", board);
        
        // 3. 수정용 HTML 폼 파일(boardEdit.html)로 이동합니다.
        return "client/board/boardEdit";
    }
    
 // [8-3] 실제 게시글 수정 처리 (POST)
    @PostMapping("/update")
    public String boardUpdateProcess(@ModelAttribute Board board) {
        // 1. 서비스의 수정 로직을 실행합니다.
        boardService.updateBoard(board);
        
        // 2. 수정이 완벽하게 끝나면, 방금 수정한 그 글의 상세 페이지로 다시 휙! 하고 돌아가게 합니다.
        return "redirect:/board/detail?boardNo=" + board.getBoardNo();
    }
    
 // [9-1] 게시글 삭제 처리 (POST)
    @PostMapping("/delete")
    public String boardDelete(@RequestParam("boardNo") Long boardNo) {
        // 1. 서비스에게 "이 번호의 글을 지워줘!" 라고 명령합니다.
        boardService.deleteBoard(boardNo);
        
        // 2. 삭제가 끝나면 알아서 게시판 목록 화면으로 돌아가게 합니다.
        return "redirect:/board/list";
    }
}
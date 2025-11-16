package com.andreea.ticket_tracker.controllers;

import com.andreea.ticket_tracker.entity.Board;
import com.andreea.ticket_tracker.services.BoardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BoardController {

    private final BoardService boardService;

    public BoardController(final BoardService boardService){
        this.boardService = boardService;
    }

    @PostMapping("/board")
    public Board createBoard(@RequestBody Board board){
        return boardService.createBoard(board);
    }

    @GetMapping("/boards")
    public List<Board> getAllBoards(){
        return boardService.getAllBoards();
    }

    @GetMapping("/board/{id}")
    public Board getProject(@PathVariable Long id){
        return boardService.getBoard(id);
    }

    @PutMapping("/board/{id}")
    public Board updateBoard(@PathVariable Long id, @RequestBody Board board){
        return boardService.updateBoard(id, board);
    }

    @DeleteMapping("/board/{id}")
    public String deleteBoard(@PathVariable Long id){
        boardService.deleteBoard(id);
        return "Board deleted: " + id;
    }
}

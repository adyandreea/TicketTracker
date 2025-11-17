package com.andreea.ticket_tracker.controllers;

import com.andreea.ticket_tracker.dto.request.BoardRequestDTO;
import com.andreea.ticket_tracker.dto.response.BoardResponseDTO;
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
    public BoardResponseDTO createBoard(@RequestBody BoardRequestDTO dto){
        return boardService.createBoard(dto);
    }

    @GetMapping("/boards")
    public List<BoardResponseDTO> getAllBoards(){
        return boardService.getAllBoards();
    }

    @GetMapping("/board/{id}")
    public BoardResponseDTO getBoard(@PathVariable Long id){
        return boardService.getBoard(id);
    }

    @PutMapping("/board/{id}")
    public BoardResponseDTO updateBoard(@PathVariable Long id, @RequestBody BoardRequestDTO dto){
        return boardService.updateBoard(id, dto);
    }

    @DeleteMapping("/board/{id}")
    public String deleteBoard(@PathVariable Long id){
        boardService.deleteBoard(id);
        return "Board deleted: " + id;
    }
}

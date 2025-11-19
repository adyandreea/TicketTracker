package com.andreea.ticket_tracker.controllers;

import com.andreea.ticket_tracker.dto.request.BoardRequestDTO;
import com.andreea.ticket_tracker.dto.response.BoardResponseDTO;
import com.andreea.ticket_tracker.dto.response.SuccessDTO;
import com.andreea.ticket_tracker.handler.ResponseHandler;
import com.andreea.ticket_tracker.services.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BoardController {

    private final BoardService boardService;

    public BoardController(final BoardService boardService){
        this.boardService = boardService;
    }

    @PostMapping("/board")
    public ResponseEntity<SuccessDTO> createBoard(@RequestBody BoardRequestDTO dto){
        boardService.createBoard(dto);
        return ResponseHandler.created("Board created successfully");
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
    public ResponseEntity<SuccessDTO> updateBoard(@PathVariable Long id, @RequestBody BoardRequestDTO dto){
        boardService.updateBoard(id, dto);
        return ResponseHandler.updated("Board updated successfully");
    }

    @DeleteMapping("/board/{id}")
    public ResponseEntity<SuccessDTO> deleteBoard(@PathVariable Long id){
        boardService.deleteBoard(id);
        return ResponseHandler.deleted("Board deleted successfully");
    }
}

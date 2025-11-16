package com.andreea.ticket_tracker.services;

import com.andreea.ticket_tracker.entity.Board;
import com.andreea.ticket_tracker.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public Board createBoard(Board board){
        return boardRepository.save(board);
    }

    public List<Board> getAllBoards(){
        return boardRepository.findAll();
    }

    public Board getBoard(Long id){
        return boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
    }

    public Board updateBoard(Long id, Board newBoard){
        return boardRepository.findById(id)
                .map(board -> {
                    board.setName(newBoard.getName());
                    board.setDescription(newBoard.getDescription());
                    return boardRepository.save(board);
                })
                .orElseThrow(() -> new RuntimeException("Board not Found"));
    }

    public void deleteBoard(Long id){
        boardRepository.deleteById(id);
    }
}

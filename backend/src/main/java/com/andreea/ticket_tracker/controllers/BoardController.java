package com.andreea.ticket_tracker.controllers;

import com.andreea.ticket_tracker.dto.request.BoardRequestDTO;
import com.andreea.ticket_tracker.dto.response.BoardResponseDTO;
import com.andreea.ticket_tracker.dto.response.ErrorDTO;
import com.andreea.ticket_tracker.dto.response.SuccessDTO;
import com.andreea.ticket_tracker.handler.ResponseHandler;
import com.andreea.ticket_tracker.services.BoardService;
import com.andreea.ticket_tracker.swagger.SwaggerHttpStatus;
import com.andreea.ticket_tracker.swagger.SwaggerMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Board API", description = "API for board management")
@RestController
@RequestMapping("/api/v1/boards")
@CrossOrigin(origins = "${frontend.url}")
public class BoardController {

    private final BoardService boardService;

    public BoardController(final BoardService boardService){
        this.boardService = boardService;
    }

    @Operation(summary = "Creates a new board.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.BOARD_SUCCESSFULLY_CREATED,
                        content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = SuccessDTO.class))}),
                @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST,
                        content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ErrorDTO.class))}),
                @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR,
                        content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ErrorDTO.class))})
        }
    )
    @PostMapping
    public ResponseEntity<SuccessDTO> createBoard(@Valid @RequestBody BoardRequestDTO dto){
        boardService.createBoard(dto);
        return ResponseHandler.created("Board created successfully");
    }

    @Operation(summary = "Returns all the boards.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.RETURN_BOARDS,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BoardResponseDTO[].class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))})
    }
    )
    @GetMapping
    public List<BoardResponseDTO> getAllBoards(){
        return boardService.getAllBoards();
    }

    @Operation(summary = "Returns a board.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.RETURN_BOARD,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BoardResponseDTO.class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))})
    }
    )
    @GetMapping("/{id}")
    public BoardResponseDTO getBoard(@PathVariable Long id){
        return boardService.getBoard(id);
    }

    @Operation(summary = "Updates the board.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.BOARD_SUCCESSFULLY_UPDATED,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SuccessDTO.class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))})
    }
    )
    @PutMapping("/{id}")
    public ResponseEntity<SuccessDTO> updateBoard(@PathVariable Long id, @Valid @RequestBody BoardRequestDTO dto){
        boardService.updateBoard(id, dto);
        return ResponseHandler.updated("Board updated successfully");
    }

    @Operation(summary = "Deletes the board.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.BOARD_SUCCESSFULLY_DELETED,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SuccessDTO.class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR,
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class))})
    }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessDTO> deleteBoard(@PathVariable Long id){
        boardService.deleteBoard(id);
        return ResponseHandler.deleted("Board deleted successfully");
    }
}

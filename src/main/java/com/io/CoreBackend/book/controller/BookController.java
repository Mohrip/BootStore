package com.io.CoreBackend.book.controller;



import com.io.CoreBackend.book.dto.UpdateBookDto;
import com.io.CoreBackend.book.dto.ResponseBookDto;
import com.io.CoreBackend.book.dto.CreateBookDto;
import com.io.CoreBackend.book.service.BookService;
import com.io.CoreBackend.shared.exception.BookNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {


    private final BookService bookService;


    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/allbooks")
    public Page<ResponseBookDto> findAll(
        @PageableDefault(size =9500, sort = "id")
        Pageable pageable) {
        return bookService.findAll(pageable);
    }

//    @GetMapping("/id")
//    public Optional<ResponseBookDto> findById(@RequestParam Long id) {
//        return bookService.findById(id);
//    }
    @GetMapping("/id")
    public ResponseEntity<ResponseBookDto> findById(@RequestParam Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BookNotFoundException(id));
    }


@GetMapping("/search")
public Page<ResponseBookDto> findByTitle(@RequestParam String title, @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return bookService.findByTitle(title, pageable);
}

    @PostMapping("/addbook")
    public ResponseEntity<ResponseBookDto> addBook(@Valid @RequestBody CreateBookDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(dto));
    }


    @PutMapping("/{id}")
    public ResponseBookDto updateBook(@PathVariable Long id, @Valid @RequestBody UpdateBookDto dto) {
        return bookService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

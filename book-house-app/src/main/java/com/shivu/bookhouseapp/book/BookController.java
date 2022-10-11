package com.shivu.bookhouseapp.book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;

import com.shivu.bookhouseapp.author.Author;
import com.shivu.bookhouseapp.author.AuthorRepository;
import com.shivu.bookhouseapp.userbooks.UserBooks;
import com.shivu.bookhouseapp.userbooks.UserBooksPrimaryKey;
import com.shivu.bookhouseapp.userbooks.UserBooksRepository;

import reactor.core.publisher.Mono;

/**
 * BookController
 */
@Controller
public class BookController {

    public final String IMAGE_ROOT = "https://covers.openlibrary.org/b/id/";

    public final String WORK = "https://openlibrary.org/works/";

    public final String AUTHOR = "https://openlibrary.org/authors/";

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authRepository;

    @Autowired
    UserBooksRepository ubRepo;

    private WebClient webClient;

    public BookController(WebClient.Builder wBuilder) {
        this.webClient = wBuilder
                .build();
    }

    @GetMapping(value = "/books/{bookId}")
    public String getBookById(@PathVariable("bookId") String bookId, Model model,
            @AuthenticationPrincipal OAuth2User principal) {
        Book book;
        Optional<Book> findBook = bookRepository.findById(bookId);
        if (findBook.isPresent()) {
            book = findBook.get();
            String coverImg = "/images/download.png";
            if (book.getCoversIds() != null && book.getCoversIds().size() > 0) {
                coverImg = IMAGE_ROOT + book.getCoversIds().get(0) + "-L.jpg";
            }
            model.addAttribute("coverImg", coverImg);
            model.addAttribute("book", book);
        } else {
            if(bookId.charAt(bookId.length()-1) == 'M')
                return"book-not-found";
            Mono<SearchBook> bodyToMono = this.webClient
                    .get()
                    .uri(WORK + "{bookId}" + ".json", bookId)
                    .retrieve()
                    .bodyToMono(SearchBook.class);
            SearchBook result = bodyToMono.block();

            book = new Book();
            book.setName(result.getTitle());
            book.setId(bookId);
            if (result.getDescription() != null) {
                String desc = result.getDescription().toString();
                if (desc.contains("{")) {
                    book.setDescription(desc.substring(desc.lastIndexOf("=") + 1));
                } else {
                    book.setDescription(desc);
                }
            }
            book.setCoversIds(result.getCovers());
            if (result.getSubjects() != null) {
                if (result.getSubjects().size() < 5)
                    book.setGenres(result.getSubjects());
                else
                    book.setGenres(result.getSubjects().subList(0, 4));
            }
            String coverImg = "/images/download.png";
            if (book.getCoversIds() != null && book.getCoversIds().size() > 0) {
                coverImg = IMAGE_ROOT + book.getCoversIds().get(0) + "-L.jpg";
            }
            model.addAttribute("coverImg", coverImg);
            model.addAttribute("book", book);
            List<Authors> authorsObj = result.getAuthors();
            authorsObj = authorsObj.stream().map(a -> {
                SearchAuthor auth = a.getAuthor();
                auth.setKey(auth.getKey().replace("/authors/", ""));
                a.getAuthor().setKey(a.getAuthor().getKey().replace("/authors/", ""));
                return a;
            }).collect(Collectors.toList());
            List<String> auths = new ArrayList<>();
            for (Authors authors : authorsObj) {
                String key = authors.getAuthor().getKey();
                Mono<Author> bodyToMon = this.webClient
                        .get()
                        .uri(AUTHOR + key + ".json")
                        .retrieve()
                        .bodyToMono(Author.class);
                Author auth = bodyToMon.block();
                auth.setId(key);
                authRepository.save(auth);
                auths.add(auth.getName());
                
            }
            book.setAuthorNames(auths);
            bookRepository.save(book);
        }
        if (principal != null && principal.getAttribute("name") != null) {
            String userId = principal.getAttribute("name");
            model.addAttribute("loginUser", userId);
            UserBooksPrimaryKey pkey = new UserBooksPrimaryKey();
            pkey.setBookId(bookId);
            pkey.setUserId(userId);
            Optional<UserBooks> findUserBook = ubRepo.findById(pkey);
            if (findUserBook.isPresent()) {
                UserBooks userBook = findUserBook.get();
                model.addAttribute("userBook", userBook);
            } else {
                model.addAttribute("userBook", new UserBooks());
            }
        }
        return "book";
    }
}
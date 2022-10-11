package com.shivu.bookhouseapp.userbooks;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import com.shivu.bookhouseapp.book.Book;
import com.shivu.bookhouseapp.book.BookRepository;
import com.shivu.bookhouseapp.booksbyuser.BooksByUser;
import com.shivu.bookhouseapp.booksbyuser.BooksByUserRepository;

@Controller
public class UserBooksController {

    @Autowired
    UserBooksRepository uBRepo;

    @Autowired
    BooksByUserRepository booksByUserRepository;

    @Autowired
    BookRepository bookRepository;

    @PostMapping("/addUserBook")
    public ModelAndView addBookForUser(@RequestBody MultiValueMap<String, String> formData,
            @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null || principal.getAttribute("name") == null)
            return null;
        String bookId = formData.getFirst("bookID");
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (!optionalBook.isPresent()) {
            return new ModelAndView("redirect:/");
        }
        Book book = optionalBook.get();
        UserBooks userBook = new UserBooks();
        String userId = principal.getAttribute("name");

        UserBooksPrimaryKey pk = new UserBooksPrimaryKey();
        pk.setBookId(bookId);
        pk.setUserId(userId);
        userBook.setKey(pk);
        if (formData.containsKey("startedDate"))
            userBook.setStartedDate(LocalDate.parse(formData.getFirst("startedDate")));
        if (formData.containsKey("completedDate"))
            userBook.setCompletedDate(LocalDate.parse(formData.getFirst("completedDate")));
        if (formData.containsKey("rating"))
            userBook.setRating(Integer.parseInt(formData.getFirst("rating")));
        if (formData.containsKey("readingStatus"))
            userBook.setReadingStatus(formData.getFirst("readingStatus"));
        uBRepo.save(userBook);

        BooksByUser booksByUser = new BooksByUser();
        booksByUser.setId(userId);
        booksByUser.setBookId(bookId);
        booksByUser.setBookName(book.getName());
        booksByUser.setCoverIds(book.getCoversIds());
        booksByUser.setAuthorNames(book.getAuthorNames());
        booksByUser.setReadingStatus(formData.getFirst("readingStatus"));
        booksByUser.setRating(Integer.parseInt(formData.getFirst("rating")));
        booksByUserRepository.save(booksByUser);
        return new ModelAndView("redirect:/books/" + bookId);
    }
}

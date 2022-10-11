package com.shivu.bookhouseapp.home;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shivu.bookhouseapp.booksbyuser.BooksByUser;
import com.shivu.bookhouseapp.booksbyuser.BooksByUserRepository;

@Controller
class HomeController {

    @Autowired
    BooksByUserRepository booksByuserRepo;

    public final String IMAGE_ROOT = "https://covers.openlibrary.org/b/id/";

    @RequestMapping("/")
    public String home(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal == null || principal.getAttribute("name") == null) {
            return "login";
        }
        String userId = principal.getAttribute("name");
        Slice<BooksByUser> bookSlice = booksByuserRepo.findAllById(userId, CassandraPageRequest.of(0, 100));
        List<BooksByUser> userBooks = bookSlice.getContent();
        userBooks = userBooks.stream().distinct().map(book -> {
            String coverImg = "/images/download.png";
            if (book.getCoverIds() != null && book.getCoverIds().size() > 0) {
                coverImg = IMAGE_ROOT + book.getCoverIds().get(0) + "-M.jpg";
            }
            book.setCoverUrl(coverImg);
            return book;
        }).collect(Collectors.toList());

        model.addAttribute("userBooks", userBooks);
        model.addAttribute("userName", principal.getAttribute("name") + "\'s");
        return "home";
    }

}

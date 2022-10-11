package com.shivu.bookhouseapp.search;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Controller
public class SearchController {

    private final String COVER_IMG = "https://covers.openlibrary.org/b/id/";
    
    private final WebClient webClient;

    public SearchController(WebClient.Builder wBuilder){
        this.webClient = wBuilder
                                .exchangeStrategies(ExchangeStrategies.builder()
                                                    .codecs(configurer -> configurer 
                                                            .defaultCodecs()
                                                            .maxInMemorySize(16 *1024 * 1024))
                                                    .build())
                                .baseUrl("http://openlibrary.org/search.json")
                                .build();
    }

    @GetMapping(value = "/search")
    public String getSearchResults(@RequestParam String query, Model model){
        Mono<SearchResult> bodyToMono = this.webClient
                                                .get()
                                                .uri("?q={query}", query)
                                                .retrieve()
                                                .bodyToMono(SearchResult.class);
        SearchResult result = bodyToMono.block();
        List<SearchResultBook> books = result.getDocs()
                                             .stream()
                                             .limit(25)
                                             .map(r -> {
                                                r.setKey(r.getKey().replace("/works/", ""));
                                                String coverId = r.getCover_i();
                                                if (StringUtils.hasText(coverId)) {
                                                    coverId = COVER_IMG + coverId + "-M.jpg";
                                                }else
                                                    coverId = "images/download.jpg";
                                                r.setCover_i(coverId);
                                                if(r.getSubject() != null && r.getSubject().size() > 3)
                                                    r.setSubject(r.getSubject()
                                                                  .stream()
                                                                  .limit(3)
                                                                  .collect(Collectors.toList()));
                                                return r;
                                            })
                                             .collect(Collectors.toList());
        model.addAttribute("searchResults", books);
        return "search";
    }
}

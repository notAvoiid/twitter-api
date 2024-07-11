package com.abreu.spring_security.controller;

import com.abreu.spring_security.entities.Role;
import com.abreu.spring_security.entities.Tweet;
import com.abreu.spring_security.entities.dto.tweet.CreateTweetDTO;
import com.abreu.spring_security.repositories.TweetRepository;
import com.abreu.spring_security.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
public class TweetController {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public TweetController(TweetRepository tweetRepository,
                           UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/tweets")
    public ResponseEntity<Void> createTweet(
            @RequestBody CreateTweetDTO data,
            JwtAuthenticationToken token
    ) {

        var user = userRepository.findById(UUID.fromString(token.getName()));
        var tweet = new Tweet(user.get(), data.content());

        tweetRepository.save(tweet);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tweets/{id}")
    public ResponseEntity<Void> deleteTweet(
            @PathVariable Long id,
            JwtAuthenticationToken token
    ) {
        var user = userRepository.findById(UUID.fromString(token.getName()));
        var tweet = tweetRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        var isAdmin = user.get().getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));


        if (isAdmin || tweet.getUser().getUserId().equals(UUID.fromString(token.getName())))
            tweetRepository.deleteById(id);
        else return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.ok().build();
    }

}

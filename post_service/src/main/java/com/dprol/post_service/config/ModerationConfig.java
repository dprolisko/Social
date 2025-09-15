package com.dprol.post_service.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component

public class ModerationConfig {

    @Getter
    private Set<String> sortedWords;

    @Value("${post.moderator.path-curse-words}")
    private Path path;

    public void Init(){
        try(Stream<String> lines = Files.lines(path)){
            sortedWords = lines.map(String::trim).map(String::toLowerCase).collect(Collectors.toSet());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public boolean checkWord(String word){
        String[] words = word.split("\\W+");
        return Arrays.stream(words).allMatch(word1 -> sortedWords.contains(word1));
    }
}

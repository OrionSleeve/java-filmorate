package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    private int id;
    @NotBlank()
    @Size(max = 200)
    private String name;
    @NotNull
    @Size(max = 200)
    private String description;
    @ReleaseDate
    private LocalDate releaseDate;
    @Positive
    private int duration;
    @JsonIgnore
    final Set<Integer> likes = new HashSet<>();

    public int getCountLikes() {
        return likes.size();
    }
}

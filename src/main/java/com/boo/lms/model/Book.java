package com.boo.lms.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @NotNull (message = "ISBN required please")
    String isbn;

    @NotNull (message = "Title required please")
    String title;

    @NotNull (message = "Author requir" +
            "ed please")
    String author;

    @Min(value = 1000, message = "Publication year expected to be after 1000")
    int publicationYear;

    @Min(value = 0, message = "Available Copies required in whole numbers")
    @Setter
    int availableCopies;
}

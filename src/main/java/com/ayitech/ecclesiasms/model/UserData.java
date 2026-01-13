package com.ayitech.ecclesiasms.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_data")
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Size(max = 160)
    private String message;

    @ElementCollection
    @Size(max = 100)
    private List<@NotBlank @Size(min = 10, max = 15) String> phoneNumbers;

    @NotBlank
    private String originationIdentity;

    private LocalDateTime sentAt;

    private String status;


}


package com.sndp.agil.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Data
@Entity
public class BlacklistedToken {
    @Id
    private String token;
    private Date expirationDate;

}
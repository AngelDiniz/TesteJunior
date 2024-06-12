package com.picpaysimplificado.picpaysimplificado.domain.transaction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import com.picpaysimplificado.picpaysimplificado.domain.user.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "transaction")
@Table(name = "transaction")
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode

public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name ="receiver_id")
    private User receiver;

    private LocalDateTime timestamp;


}

package co.com.bancolombia.redis.template.model;

import lombok.*;

import java.util.List;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionListDTO {
    List<TransactionDTO> transactions;
}

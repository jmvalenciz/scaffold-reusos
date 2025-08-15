package co.com.bancolombia.consumer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Meta {
    @JsonProperty("_messageId")
    String messageId;
    @JsonProperty("_requestDateTime")
    LocalDateTime requestDateTime;
    @JsonProperty("_applicationId")
    String applicationId;
}
package co.com.bancolombia.api.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseWrapper {
    Object data;
    Meta meta;
}

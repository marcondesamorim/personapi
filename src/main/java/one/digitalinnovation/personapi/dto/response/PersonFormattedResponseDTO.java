package one.digitalinnovation.personapi.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PersonFormattedResponseDTO {

    private List<String> message;
}
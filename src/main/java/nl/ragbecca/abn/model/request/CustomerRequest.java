package nl.ragbecca.abn.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomerRequest {
    private String name;
    private String dateOfBirth;
    private List<AddressRequest> addresses;
    private EmployerRequest employer;
    private Boolean isEmployed;
}

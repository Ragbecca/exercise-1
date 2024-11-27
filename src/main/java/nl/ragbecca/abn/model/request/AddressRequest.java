package nl.ragbecca.abn.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {
    private String city;
    private String postalCode;
    private String country;
    private String addressType;
}

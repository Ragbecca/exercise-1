package nl.ragbecca.abn.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.ragbecca.abn.model.util.AddressType;

@Entity(name = "Address")
@Table(name = "Address")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String addressId;

    @Setter
    private String city;

    @Setter
    private String postalCode;

    @Setter
    private String country;

    @Setter
    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    public Address(String city, String postalCode, String country, AddressType addressType) {
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.addressType = addressType;
    }
}

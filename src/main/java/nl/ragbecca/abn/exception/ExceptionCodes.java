package nl.ragbecca.abn.exception;

public class ExceptionCodes {
    public static final String ADDRESS_TYPE_INVALID_CODE = "INVALID_ADDRESS_TYPE";
    public static final String ADDRESS_TYPE_INVALID_EXCEPTION = "Please enter valid address type";
    public static final String COUNTRY_NO_ADDRESSES_FOUND_CODE = "NO_ADDRESSES_FOR_COUNTRY";
    public static final String COUNTRY_NO_ADDRESSES_FOUND_EXCEPTION = "No addresses for country: %s";
    public static final String CUSTOMER_NOT_FOUND_CODE = "CUSTOMER_NOT_FOUND";
    public static final String CUSTOMER_NOT_FOUND_EXCEPTION = "Customer not found by id %s";
    public static final String INVALID_DATE_OF_BIRTH_CODE = "INVALID_DATE_OF_BIRTH";
    public static final String INVALID_DATE_OF_BIRTH_EXCEPTION = "Date of birth invalid, should be yyyy-MM-dd";

    private ExceptionCodes() {
        // Default private constructor to hide public one
    }
}

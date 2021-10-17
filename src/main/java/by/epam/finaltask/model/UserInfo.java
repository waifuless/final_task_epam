package by.epam.finaltask.model;

public class UserInfo {

    private final int user_id;
    private final String phone_number;
    private final String first_name;
    private final String last_name;
    private final String address;
    private final String city;
    private final String region;
    private final String postalCode;

    public UserInfo(int user_id, String phone_number, String first_name, String last_name,
                    String address, String city, String region, String postalCode) {
        this.user_id = user_id;
        this.phone_number = phone_number;
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.city = city;
        this.region = region;
        this.postalCode = postalCode;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        if (user_id != userInfo.user_id) return false;
        if (phone_number != null ? !phone_number.equals(userInfo.phone_number) : userInfo.phone_number != null)
            return false;
        if (first_name != null ? !first_name.equals(userInfo.first_name) : userInfo.first_name != null) return false;
        if (last_name != null ? !last_name.equals(userInfo.last_name) : userInfo.last_name != null) return false;
        if (address != null ? !address.equals(userInfo.address) : userInfo.address != null) return false;
        if (city != null ? !city.equals(userInfo.city) : userInfo.city != null) return false;
        if (region != null ? !region.equals(userInfo.region) : userInfo.region != null) return false;
        return postalCode != null ? postalCode.equals(userInfo.postalCode) : userInfo.postalCode == null;
    }

    @Override
    public int hashCode() {
        int result = user_id;
        result = 31 * result + (phone_number != null ? phone_number.hashCode() : 0);
        result = 31 * result + (first_name != null ? first_name.hashCode() : 0);
        result = 31 * result + (last_name != null ? last_name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "user_id=" + user_id +
                ", phone_number='" + phone_number + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", region='" + region + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}

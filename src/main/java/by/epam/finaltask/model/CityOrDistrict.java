package by.epam.finaltask.model;

public class CityOrDistrict implements DaoEntity<CityOrDistrict> {

    private final long cityOrDistrictId;
    private final String cityOrDistrictName;
    private final long regionId;

    public CityOrDistrict(long cityOrDistrictId, String cityOrDistrictName, long regionId) {
        this.cityOrDistrictId = cityOrDistrictId;
        this.cityOrDistrictName = cityOrDistrictName;
        this.regionId = regionId;
    }

    public CityOrDistrict(String cityOrDistrictName, long regionId) {
        this.cityOrDistrictId = -1;
        this.cityOrDistrictName = cityOrDistrictName;
        this.regionId = regionId;
    }

    public long getCityOrDistrictId() {
        return cityOrDistrictId;
    }

    public String getCityOrDistrictName() {
        return cityOrDistrictName;
    }

    public long getRegionId() {
        return regionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CityOrDistrict that = (CityOrDistrict) o;

        if (cityOrDistrictId != that.cityOrDistrictId) return false;
        if (regionId != that.regionId) return false;
        return cityOrDistrictName != null ? cityOrDistrictName.equals(that.cityOrDistrictName) : that.cityOrDistrictName == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (cityOrDistrictId ^ (cityOrDistrictId >>> 32));
        result = 31 * result + (cityOrDistrictName != null ? cityOrDistrictName.hashCode() : 0);
        result = 31 * result + (int) (regionId ^ (regionId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "CityOrDistrict{" +
                "cityOrDistrictId=" + cityOrDistrictId +
                ", cityOrDistrictName='" + cityOrDistrictName + '\'' +
                ", regionId=" + regionId +
                '}';
    }

    @Override
    public CityOrDistrict createWithId(long id) {
        return new CityOrDistrict(id, cityOrDistrictName, regionId);
    }
}

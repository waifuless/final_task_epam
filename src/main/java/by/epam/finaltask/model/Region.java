package by.epam.finaltask.model;

public class Region implements DaoEntity<Region> {

    private final long regionId;
    private final String regionName;

    public Region(long regionId, String regionName) {
        this.regionId = regionId;
        this.regionName = regionName;
    }

    public Region(String regionName) {
        this.regionId = -1;
        this.regionName = regionName;
    }

    public long getRegionId() {
        return regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Region region = (Region) o;

        if (regionId != region.regionId) return false;
        return regionName != null ? regionName.equals(region.regionName) : region.regionName == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (regionId ^ (regionId >>> 32));
        result = 31 * result + (regionName != null ? regionName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Region{" +
                "regionId=" + regionId +
                ", regionName='" + regionName + '\'' +
                '}';
    }

    @Override
    public Region createWithId(long id) {
        return new Region(id, regionName);
    }
}

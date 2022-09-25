import java.util.List;

public class OrderList {
    public List<Order> getCreatedOrders() {
        return orders;
    }

    public void setCreatedOrders(List<Order> orders) {
        this.orders = orders;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<AvailableStation> getAvailableStations() {
        return availableStations;
    }

    public void setAvailableStations(List<AvailableStation> availableStations) {
        this.availableStations = availableStations;
    }

    private List<Order> orders;
    private PageInfo pageInfo;
    private List<AvailableStation> availableStations;

    public OrderList(List<Order> orders, PageInfo pageInfo, List<AvailableStation> availableStations) {
        this.orders = orders;
        this.pageInfo = pageInfo;
        this.availableStations = availableStations;
     }

    public OrderList() {
    }
}

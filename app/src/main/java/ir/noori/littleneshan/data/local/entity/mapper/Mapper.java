package ir.noori.littleneshan.data.local.entity.mapper;

import ir.noori.littleneshan.data.local.entity.AddressEntity;
import ir.noori.littleneshan.data.model.LocationModel;
import ir.noori.littleneshan.data.model.SearchItem;

public class Mapper {


    public static AddressEntity mapToAddressEntity(SearchItem item) {
        if (item == null) return null;

        AddressEntity entity = new AddressEntity();
        entity.setTitle(item.getTitle());
        entity.setAddress(item.getAddress());
        entity.setNeighbourhood(item.getNeighbourhood());
        entity.setRegion(item.getRegion());
        entity.setType(item.getType());
        entity.setCategory(item.getCategory());

        LocationModel location = item.getLocation();
        if (location != null) {
            entity.setLat(location.getX());
            entity.setLng(location.getY());
        }

        return entity;
    }

    public static SearchItem mapToSearchItem(AddressEntity entity) {
        if (entity == null) return null;

        SearchItem item = new SearchItem();
        item.setTitle(entity.getTitle());
        item.setAddress(entity.getAddress());
        item.setNeighbourhood(entity.getNeighbourhood());
        item.setRegion(entity.getRegion());
        item.setType(entity.getType());
        item.setCategory(entity.getCategory());

        LocationModel location = new LocationModel();
        location.setX(entity.getLat());
        location.setY(entity.getLng());
        item.setLocation(location);

        return item;
    }

}

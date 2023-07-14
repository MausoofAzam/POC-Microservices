package HotelService.services;

import HotelService.entities.Hotel;

import java.util.List;

public interface HotelService {

//    create Hotel info
    Hotel create(Hotel hotel);
//get list of Hotels
    List<Hotel> getAll();
//get Hotel by id
    Hotel get(String id);
}

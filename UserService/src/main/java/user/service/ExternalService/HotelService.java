package user.service.ExternalService;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import user.service.entities.Hotel;

@FeignClient(name = "HOTEL-SERVICE")
public interface HotelService {


    @GetMapping("/hotels/{hotelId}")
    Hotel getHotel(@PathVariable("hotelId") String hotelId);
}

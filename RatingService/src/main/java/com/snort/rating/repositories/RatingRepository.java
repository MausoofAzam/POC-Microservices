package com.snort.rating.repositories;

import com.snort.rating.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, String > {


    //custom query
    List<Rating> findByUserId(String userId);
    List<Rating> findByHotelId(String  hotelId);
}

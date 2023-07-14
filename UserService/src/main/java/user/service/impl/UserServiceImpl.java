package user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import user.service.ExternalService.HotelService;
import user.service.entities.Hotel;
import user.service.entities.Rating;
import user.service.entities.User;
import user.service.exceptions.ResourceNotFoundException;
import user.service.repositories.UserRepositories;
import user.service.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepositories userRepositories;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    @Value("${rating.service.url}")
    private String ratingServiceUrl;

    @Value("${hotel.service.url}")
    private String hotelServiceUrl;
    @Override
    public User saveUser(User user) {
        //generate unique userid
        String randomUserId= UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepositories.save(user);
    }

    @Override
    public List<User> getAllUser() {
        List<User> userList= userRepositories.findAll();

        return userList;
    }


   /* @Override
    public User getUser(String userId) {
        User user=  userRepositories.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given id is not available on server"));
        //fetch Rating for the above user from Rating service
        //localhost:8083/ratings/users/a4bbcd30-64d3-4a21-8ea5-9992e011a132

        String ratingServiceUrlForUser =ratingServiceUrl+userId;

//        ArrayList<Rating> ratingsOfUser = restTemplate.getForObject("http://localhost:8083/ratings/users/a4bbcd30-64d3-4a21-8ea5-9992e011a132", ArrayList.class);
        Rating[] ratingsOfUser = restTemplate.getForObject(ratingServiceUrlForUser, Rating[].class);
        log.info("{} ",ratingsOfUser);
        List<Rating> ratings= Arrays.stream(ratingsOfUser).collect(Collectors.toList());
//        List<Rating> ratings = Arrays.stream(ratingsOfUser).toList();
        List<Rating> ratingList = ratings.stream().map(rating -> {
            //api call to hotel service to get the Hotel

            String hotelServiceURL=    hotelServiceUrl+rating.getHotelId();
            ResponseEntity<Hotel> forEntity = restTemplate.getForEntity(hotelServiceURL, Hotel.class);
            Hotel hotel = forEntity.getBody();
            log.info("Response Status code : {}", forEntity.getStatusCode());
            //set the hotel to rating
            rating.setHotel(hotel );
            //return the rating

            return rating;
        }).collect(Collectors.toList());

        user.setRatings(ratingList);
        return user;
    }*/

//using feign client
    @Override
    public User getUser(String userId) {
        User user=  userRepositories.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given id is not available on server"));

        String ratingServiceUrlForUser =ratingServiceUrl+userId;

//        ArrayList<Rating> ratingsOfUser = restTemplate.getForObject("http://localhost:8083/ratings/users/a4bbcd30-64d3-4a21-8ea5-9992e011a132", ArrayList.class);
        Rating[] ratingsOfUser = restTemplate.getForObject(ratingServiceUrlForUser, Rating[].class);
        log.info("{} ",ratingsOfUser);
        List<Rating> ratings= Arrays.stream(ratingsOfUser).collect(Collectors.toList());
//        List<Rating> ratings = Arrays.stream(ratingsOfUser).toList();
        List<Rating> ratingList = ratings.stream().map(rating -> {
            //api call to hotel service to get the Hotel

            String hotelServiceURL=    hotelServiceUrl+rating.getHotelId();
            Hotel hotel = hotelService.getHotel(rating.getHotelId());
            //set the hotel to rating
            rating.setHotel(hotel );
            //return the rating

            return rating;
        }).collect(Collectors.toList());

        user.setRatings(ratingList);
        return user;
    }
}

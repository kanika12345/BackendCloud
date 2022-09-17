package com.tweetapp.controller;

import com.tweetapp.kafka.Producer;
import com.tweetapp.model.Login;
import com.tweetapp.model.Reply;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.model.UserForgotPassword;
import com.tweetapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1.0/tweets")
public class UserController {

	Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
    Producer producer;

    @Autowired
    private UserService userService;

    //User Registration
    @PostMapping("/register")
    public User registration(@RequestBody User user){
    	//logger.info("User created successfully!!!...");
        userService.registerUser(user);
        return user;
    }

    //User Login
    @PostMapping("/login")
    public String login(@RequestBody Login login){
    	logger.debug("----Inside UserController-> login()------");
        User user1 = userService.login(login.getUsername(),login.getPassword());
        if(user1!= null){
            String username = user1.getUserName();
            return username+"successfully logged in";
        }
        else{
            return "no such user";
        }
    }

    //Get all users
    @GetMapping("/users/all")
    public List<User> getUsers(){
    	logger.info("Retriving all the user");
        return userService.listUsers();
    }

    //Get user by Username
    @GetMapping("/user/searchuname/{username}")
    public List<User> findByUsername(@PathVariable String username){
        return userService.findByUsername(username);
    }

    //Get user by UserId
    @PostMapping("/user/searchid")
    public User findByUserId(@RequestBody String userId){
    	logger.info("Getting User");
    	System.out.println("value1"+userId);
    	return userService.findByUserId(userId);
    	}

    //Forgot Password
    @PostMapping("/forgot")
    public String forgotPassword(@RequestBody UserForgotPassword user){
        //System.out.println(credentials);
    	logger.info("Forgot Password request received ");
        return userService.forgotPassword(user.getUserid(),user.getNewpassword());
    }

    //Post a tweet
    @PostMapping("/addTweet")
    public Tweet newTweet(@RequestBody Map<String,String> addTweet){
    	logger.info("Adding Tweet");
        return userService.postTweet(addTweet.get("tweet"),addTweet.get("username"));
    }

    //get all tweets
    @GetMapping("/allTweets")
    public List<Tweet> listTweets(){
    	logger.info("Retriving all the tweets by the user");
        return userService.getAllTweets();
    }

    //get all tweets of a user
    @PostMapping("/allTweetsOfUser")
    public List<Tweet> listTweetsOfaUser(@RequestBody String username){
    	logger.info("All tweets of user:"+username);
        return userService.getAllTweetsOfUser(username);
    }

    //update tweet
    @PutMapping("/updateTweet")
    public Tweet updateTweet(@RequestBody Map<String,String> newTweet){
    	logger.info("Updating tweet by id:"+ newTweet.get("id"));
        return userService.updateTweet(newTweet.get("id"),newTweet.get("content"));
    }

    //Delete tweet
    @DeleteMapping ("/deleteTweet")
    public String deleteTweet(@RequestBody Map<String,String> deleteInfo){
    	logger.info("Delete tweet by id:"+ deleteInfo.get("id"));
        return userService.deleteTweet(deleteInfo.get("id"));
    }

    //like tweet
    @PutMapping("/likeTweet")
    public List<String> likeTweet(@RequestBody Map<String,String> details){
    	logger.info("Updating like by id:"+ details.get("id"));
        return userService.likeTweet(details.get("id"),details.get("username"));
    }

    //Reply to a tweet
    @PostMapping("/replyTweet")
    public Reply replyTweet(@RequestBody Map<String,String> reply){
    	logger.info("Replying tweet by id:"+ reply.get("tweetid"));
        return userService.postTweetReply(reply.get("tweetid"),reply.get("username"),reply.get("tweetreply"));
    }

    //Get all replies
    @GetMapping("/getAllReplies")
    public List<Reply> listReplies(){
    	logger.info("All replies");
        return userService.getAllReplies();
    }

}

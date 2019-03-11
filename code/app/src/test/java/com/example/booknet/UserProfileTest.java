package com.example.booknet;

import org.junit.Test;
import static org.junit.Assert.*;
public class UserProfileTest {
    @Test
    public void Constructors(){

        UserProfile user = new UserProfile("name","email@site.com","555-555-5555");

        assertEquals("name",user.getName());
        assertEquals("email@site.com",user.getEmail());
        assertEquals("555-555-5555",user.getPhoneNumber());
    }

    @Test
    public void Setters(){
        UserProfile user = new UserProfile("name","email@site.com","555-555-5555");

        user.setName("newname");
        user.setEmail("myEmail@site.com");
        user.setPhoneNumber("123-555-2525");

        assertEquals("newname",user.getName());
        assertEquals("myEmail@site.com",user.getEmail());
        assertEquals("123-555-2525",user.getPhoneNumber());
    }

    @Test
    public void EmailFormat(){
        assertTrue(false);
        //TODO: implement later
    }
    @Test
    public void PhoneFormat(){
        assertTrue(false);
        //TODO: implement later
    }

    @Test
    public void EditUserProfile(){
        UserProfile user = new UserProfile("name","email@site.com","555-555-5555");

        user.setName("newname");
        user.setEmail("myEmail@site.com");
        user.setPhoneNumber("123-555-2525");

        assertEquals("newname",user.getName());
        assertEquals("myEmail@site.com",user.getEmail());
        assertEquals("123-555-2525",user.getPhoneNumber());
    }
}

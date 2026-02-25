package com.yuosef.springbootstartertemplate.models.Mappers;

import com.yuosef.springbootstartertemplate.models.Dtos.UserDto;
import com.yuosef.springbootstartertemplate.models.User;

public class Usermapper {

   public static UserDto toDto(User user){
    return new  UserDto(user.getId(),
            user.getName(),
            user.getEmail(),
            user.getMobileNumber(),
            user.getPwd(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getRoles(),
            user.getAuthProvider());
   };
    public static User toEntity(UserDto userDto){
       return new User(userDto.getId(),
               userDto.getName(),
               userDto.getEmail(),
               userDto.getMobileNumber(),
               userDto.getPwd(),
               userDto.getAuthorities(),
               userDto.getAuthProvider());
   };

}

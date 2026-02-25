package com.yuosef.springbootstartertemplate.Models.Mappers;

import com.yuosef.springbootstartertemplate.Models.Dtos.UserDto;
import com.yuosef.springbootstartertemplate.Models.User;

public class Usermapper {

   public static UserDto toDto(User user){
    return new  UserDto(user.getId(),
            user.getName(),
            user.getEmail(),
            user.getMobileNumber(),
            user.getPwd(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getRoles());
   };
    public static User toEntity(UserDto userDto){
       return new User(userDto.getId(),
               userDto.getName(),
               userDto.getEmail(),
               userDto.getMobileNumber(),
               userDto.getPwd(),
               userDto.getAuthorities());
   };

}

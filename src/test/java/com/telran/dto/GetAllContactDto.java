package com.telran.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class GetAllContactDto {
    List<ContactDto> contacts;
}
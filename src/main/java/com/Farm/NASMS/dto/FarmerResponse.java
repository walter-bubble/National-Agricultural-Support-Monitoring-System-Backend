package com.Farm.NASMS.dto;

import com.Farm.NASMS.model.Farmer;

public record FarmerResponse(
        Long id,
        String name,
        Long nationalId,
        String phoneNumber,
        String email,
        String county,
        Double farmSize,
        String titleNumber,
        Long userId,
        String userName
) {
    public static FarmerResponse from(Farmer farmer) {
        return new FarmerResponse(
                farmer.getId(),
                farmer.getName(),
                farmer.getNationalId(),
                farmer.getPhoneNumber(),
                farmer.getEmail(),
                farmer.getCounty(),
                farmer.getFarmSize(),
                farmer.getTitleNumber(),
                farmer.getUser() != null ? farmer.getUser().getId() : null,
                farmer.getUser() != null ? farmer.getUser().getUserName() : null
        );
    }
}

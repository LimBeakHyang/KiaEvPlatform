package com.kiaev.client.charging;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargingRepository extends JpaRepository<Charging, String> {
}
package com.phoenix.amazon.AmazonBackend.repository;

import com.phoenix.amazon.AmazonBackend.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAddressRepository extends JpaRepository<Address,String>{
}
